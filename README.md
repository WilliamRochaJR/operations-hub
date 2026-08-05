# Operations Hub — PoC Full Stack

PoC de um painel de operações de pedidos, planejada para demonstrar competências de desenvolvimento Full Stack com maior profundidade no front-end.

O fluxo principal permitirá consultar pedidos, aplicar filtros, abrir detalhes e alterar o status de um pedido. A solução será composta por:

- aplicação web em React e TypeScript;
- BFF em NestJS;
- microsserviços em Java 21 e Spring Boot;
- eventos assíncronos com Apache Kafka;
- testes unitários, de componente, integração e end-to-end;
- ambiente local com Docker Compose e pipeline de CI.

O primeiro walking skeleton está em implementação. Já existem a aplicação React, o BFF, Orders Service, Audit Service, contrato `OrderCreated`, transactional outbox, consumidor Kafka idempotente, Docker Compose e CI inicial.

## Estrutura

```text
apps/
├── web/                 # React + TypeScript
└── bff/                 # NestJS
microservices/
├── orders-service/      # Spring Boot
└── audit-service/       # Spring Boot
contracts/events/        # contratos Kafka versionados
infra/
├── helm/                 # deploy Kubernetes
└── terraform/            # infraestrutura AWS
```

## Execução local

Pré-requisitos: Node 22, Java 21, Maven, Docker e Docker Compose.

```bash
npm install
docker compose up --build
```

Depois da inicialização:

- Web: `http://localhost:8080`
- BFF: `http://localhost:3000/api`
- Orders Service: `http://localhost:8081`
- Audit Service: `http://localhost:8082`
- PostgreSQL: `localhost:5433`
- Kafka: `localhost:9092`

Para validar os projetos sem containers:

```bash
npm run typecheck
npm test
npm run build
mvn -f microservices/orders-service/pom.xml test
mvn -f microservices/audit-service/pom.xml test
```

Para executar a jornada E2E com o ambiente integrado ativo:

```bash
npx playwright install chromium
docker compose up --build --detach --wait
npm run test:e2e
```

## Documentação

- [Plano da PoC](docs/POC-PLAN.md)
- [Arquitetura e decisões](docs/ARCHITECTURE.md)
- [Backend for Frontend (BFF)](docs/BFF.md)
- [Backlog e critérios de aceite](docs/BACKLOG.md)
- [Guia de desenvolvimento ágil](docs/AGILE-DEVELOPMENT.md)
- [Infraestrutura, AWS e ambientes](docs/INFRASTRUCTURE.md)
- [Como realizar o deploy na AWS](docs/AWS-DEPLOYMENT.md)
- [Testes e CI/CD](docs/TESTING-CICD.md)
- [Matriz de adoção tecnológica](docs/TECHNOLOGY-ADOPTION.md)
- [Architecture Decision Records](docs/adr/README.md)

## Objetivo de portfólio

O projeto deve ser pequeno o bastante para ser concluído e polido, mas completo o bastante para evidenciar decisões técnicas reais: separação de responsabilidades, tratamento de erros, contratos REST, estado de interface, testes, acessibilidade, performance e automação.
