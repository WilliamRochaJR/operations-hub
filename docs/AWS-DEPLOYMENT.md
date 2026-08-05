# Deploy da PoC na AWS

## Objetivo

Este guia conecta o walking skeleton local à AWS sem mudar sua arquitetura funcional. Terraform cria a infraestrutura, Docker produz imagens imutáveis, ECR armazena essas imagens e Helm instala os quatro workloads no EKS.

Nenhum comando deste documento deve ser executado em uma conta sem antes revisar custos e permissões. `terraform plan` é leitura; `terraform apply` cria recursos cobrados.

## Fluxo completo

```mermaid
flowchart LR
    git[GitHub] -->|OIDC temporário| gha[GitHub Actions]
    gha -->|build e push| ecr[(Amazon ECR)]
    gha -->|helm upgrade| eks[Amazon EKS]
    ecr -->|pull por SHA| eks

    user([Usuário]) --> alb[Application Load Balancer]
    alb -->|/| web[React Web]
    alb -->|/api| bff[NestJS BFF]
    bff --> orders[Orders Spring Boot]
    bff --> audit[Audit Spring Boot]
    orders --> rds[(RDS PostgreSQL)]
    audit --> rds
    orders -. OrderCreated .-> msk[(MSK Serverless)]
    msk -. evento .-> audit

    secrets[Secrets Manager] -. credenciais .-> gha
```

## O que já está codificado

```text
infra/
├── terraform/environments/poc/  # VPC, EKS, ECR, RDS, MSK, Secrets e Budget
└── helm/operations-hub/         # Deployments, Services, ConfigMap e Ingress
```

O Terraform cria:

- VPC em duas zonas de disponibilidade;
- sub-redes públicas, privadas e de banco;
- um NAT Gateway compartilhado para reduzir a quantidade de recursos;
- EKS com um node group Spot pequeno;
- quatro repositórios ECR;
- RDS PostgreSQL privado;
- MSK Serverless com autenticação IAM;
- segredo do banco no Secrets Manager;
- orçamento mensal opcional;
- identidade usada pelo AWS Load Balancer Controller.

O Helm instala `web`, `bff`, `orders-service` e `audit-service`. O Ingress envia `/api` ao BFF e as demais rotas ao React. RDS e MSK não rodam dentro do Kubernetes.

## Pré-requisitos

- uma conta AWS não produtiva;
- AWS CLI autenticado por SSO ou outra credencial temporária;
- Terraform, Docker, `kubectl` e Helm;
- permissão para VPC, EKS, EC2, IAM, ECR, RDS, MSK, Secrets Manager e Budgets;
- um bucket S3 separado para o state do Terraform;
- um papel IAM com confiança OIDC para o GitHub Actions.

Confira a identidade antes de qualquer mudança:

```bash
aws sts get-caller-identity
aws configure get region
```

## 1. Preparar o state remoto

Crie uma vez um bucket S3 com versionamento e bloqueio de acesso público. O bucket de state não faz parte deste mesmo Terraform para evitar dependência circular.

```bash
cd infra/terraform/environments/poc
cp backend.hcl.example backend.hcl
cp terraform.tfvars.example terraform.tfvars
```

Edite os dois arquivos locais. Eles são ignorados pelo Git. Defina especialmente região, e-mail de alerta e limite mensal.

## 2. Revisar antes de criar

```bash
terraform init -backend-config=backend.hcl
terraform fmt -check -recursive
terraform validate
terraform plan -out=poc.tfplan
terraform show poc.tfplan
```

Somente depois de revisar o plano:

```bash
terraform apply poc.tfplan
```

O `apply` não foi executado durante a criação desta estrutura.

## 3. Conectar o kubectl

```bash
aws eks update-kubeconfig \
  --region "$(terraform output -raw aws_region)" \
  --name "$(terraform output -raw cluster_name)"
kubectl get nodes
```

## 4. Instalar o AWS Load Balancer Controller

Terraform prepara a identidade do controller, mas o componente Kubernetes é instalado pelo Helm:

```bash
helm repo add eks https://aws.github.io/eks-charts
helm repo update eks
helm upgrade --install aws-load-balancer-controller eks/aws-load-balancer-controller \
  --namespace kube-system \
  --set clusterName="$(terraform output -raw cluster_name)" \
  --set serviceAccount.create=true \
  --set serviceAccount.name=aws-load-balancer-controller
```

O papel usa uma policy AWS ampla para manter a primeira PoC legível. Essa simplificação está marcada no código e deve ser substituída pela policy mínima oficial antes de qualquer uso produtivo.

## 5. Publicar as imagens

Cada imagem recebe o SHA do commit; `latest` não é usado no CD.

```mermaid
sequenceDiagram
    participant CI as GitHub Actions
    participant ECR as Amazon ECR
    participant EKS as Amazon EKS
    CI->>CI: testa e constrói
    CI->>ECR: push web, bff, orders e audit com SHA
    CI->>EKS: helm upgrade com o mesmo SHA
    EKS->>ECR: pull das quatro imagens
    CI->>EKS: aguarda rollout e executa smoke test
```

O workflow `.github/workflows/deploy-poc.yml` automatiza esse processo. Configure no Environment `poc` do GitHub:

| Tipo | Nome | Exemplo/origem |
|---|---|---|
| Variable | `AWS_REGION` | `us-east-1` |
| Variable | `AWS_ROLE_ARN` | role OIDC de deploy |
| Variable | `EKS_CLUSTER_NAME` | output `cluster_name` |
| Variable | `ECR_REGISTRY` | `<account>.dkr.ecr.<region>.amazonaws.com` |
| Variable | `RDS_ENDPOINT` | output `rds_endpoint` |
| Variable | `MSK_BOOTSTRAP_SERVERS` | output `msk_bootstrap_brokers_sasl_iam` |
| Secret | `DATABASE_SECRET_ARN` | output `database_secret_arn` |

O React é construído com `VITE_API_URL=/api`, portanto usa a mesma origem pública do ALB. O navegador não acessa diretamente os microsserviços.

## 6. Segredo do banco

Durante o deploy, o workflow lê o JSON no Secrets Manager e cria/atualiza o Secret `operations-hub-database` no namespace. A senha não aparece em `values.yaml`, logs intencionais ou argumentos do Helm.

Em uma evolução de produção, essa sincronização deve migrar para External Secrets Operator ou Secrets Store CSI Driver com Pod Identity.

## 7. MSK e autenticação IAM

Localmente, Spring usa Kafka sem autenticação. Na AWS, o chart ativa o profile Spring `aws`, que configura:

```text
security.protocol=SASL_SSL
sasl.mechanism=AWS_MSK_IAM
sasl.jaas.config=software.amazon.msk.auth.iam.IAMLoginModule required;
sasl.client.callback.handler.class=software.amazon.msk.auth.iam.IAMClientCallbackHandler
```

Os dois projetos Spring incluem `software.amazon.msk:aws-msk-iam-auth`. Para a PoC, a permissão Kafka está no papel dos nodes; a evolução recomendada é uma identidade exclusiva para cada workload.

## 8. Validar o deploy

```bash
kubectl get pods,services,ingress -n operations-hub
kubectl rollout status deployment/web -n operations-hub
kubectl rollout status deployment/bff -n operations-hub
kubectl rollout status deployment/orders-service -n operations-hub
kubectl rollout status deployment/audit-service -n operations-hub
```

Quando o Ingress tiver endereço:

```bash
APP_URL="http://$(kubectl get ingress operations-hub -n operations-hub -o jsonpath='{.status.loadBalancer.ingress[0].hostname}')"
curl --fail "$APP_URL/health"
curl --fail "$APP_URL/api/health"
```

Depois, criar um pedido pela interface confirma o caminho completo RDS → outbox → MSK → Audit.

## 9. Destruir e controlar custos

Remova primeiro o release e confirme que o ALB desapareceu; depois destrua o Terraform:

```bash
helm uninstall operations-hub -n operations-hub
kubectl delete namespace operations-hub
terraform plan -destroy -out=destroy.tfplan
terraform apply destroy.tfplan
```

Revise também recursos órfãos no console. EKS, NAT Gateway, RDS e MSK Serverless geram cobrança mesmo com pouco ou nenhum tráfego.
