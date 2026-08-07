# Backlog e critérios de aceite

## Marco 0 — Walking skeleton integrado

### WLK-01 — Entregar o primeiro fluxo ponta a ponta

**Critérios de aceite**

- React exibe uma página simples de estado da plataforma;
- BFF agrega respostas dos serviços Spring;
- Orders Service executa consulta real no PostgreSQL;
- Orders Service publica `OrderCreated` via outbox;
- Audit Service consome o evento e persiste a auditoria sem duplicação;
- correlation ID atravessa todas as camadas;
- fluxo possui smoke test automatizado.

### WLK-02 — Executar todo o sistema localmente

**Critérios de aceite**

- Docker Compose inicia web, BFF, serviços, PostgreSQL e Kafka;
- health checks controlam a prontidão;
- dados e configuração de demonstração são reproduzíveis;
- fluxo ponta a ponta funciona com um único procedimento documentado.

### WLK-03 — Publicar o primeiro ambiente AWS

**Critérios de aceite**

- imagens são publicadas no ECR;
- Helm instala os componentes no EKS;
- Orders Service acessa o RDS;
- serviços produzem e consomem eventos no Kafka KRaft efêmero;
- workflow manual publica somente o PR explicitamente escolhido;
- ambiente saudável permanece por 20 minutos e é destruído automaticamente;
- watchdog remove ambientes com lease expirado;
- migration e smoke test fazem parte do deploy;
- logs permitem rastrear uma requisição completa.

## Épico 1 — Fundação

### FND-01 — Inicializar monorepo

**Critérios de aceite**

- web, BFF e API iniciam separadamente;
- comandos de lint, teste e build estão documentados;
- versões de runtime estão fixadas;
- health checks existem no BFF e na API.

### FND-02 — Ambiente integrado

**Critérios de aceite**

- PostgreSQL inicia pelo Docker Compose;
- API aplica migrations automaticamente no perfil local;
- um único comando inicia o ambiente integrado;
- dados de demonstração são reproduzíveis.

### FND-03 — CI inicial

**Critérios de aceite**

- pull requests executam lint, testes e build;
- falha em qualquer etapa bloqueia a validação;
- caches não comprometem reprodutibilidade.

### FND-04 — Imagens Docker

**Critérios de aceite**

- web, BFF e API possuem Dockerfiles multi-stage;
- containers executam como usuário sem privilégios;
- imagens possuem health checks e tamanho inspecionado;
- configuração é recebida por ambiente, sem segredos embutidos.

### FND-05 — Kubernetes e AWS

**Critérios de aceite**

- chart Helm instala web, BFF e API;
- workloads possuem probes e recursos definidos;
- imagens são obtidas do Amazon ECR;
- aplicação utiliza PostgreSQL no Amazon RDS;
- segredos não ficam versionados no Git;
- procedimento de provisionamento e destruição está documentado.

### FND-06 — Entrega contínua

**Critérios de aceite**

- merge em `main` publica imagens imutáveis;
- deploy em desenvolvimento é automático;
- produção exige aprovação protegida;
- migrations executam uma única vez antes da aplicação;
- smoke test valida o deploy;
- rollback está documentado.

## Épico 2 — Consulta de pedidos

### ORD-01 — Listar pedidos na API

**Critérios de aceite**

- suporta paginação, ordenação e filtros documentados;
- parâmetros inválidos retornam 400;
- resultado possui metadados de paginação;
- consulta tem teste de integração com PostgreSQL.

### ORD-02 — Expor listagem no BFF

**Critérios de aceite**

- traduz parâmetros do browser para a API;
- converte o contrato para DTO orientado à interface;
- timeout e indisponibilidade retornam erro padronizado;
- correlation ID é propagado.

### ORD-03 — Construir tela de listagem

**Critérios de aceite**

- filtros ficam refletidos na URL;
- busca possui debounce;
- loading, vazio e erro possuem apresentações distintas;
- tabela no desktop tem alternativa adequada em telas pequenas;
- controles têm nomes acessíveis e funcionam por teclado;
- comportamento principal possui testes de componente.

## Épico 3 — Detalhe e tratamento

### ORD-04 — Consultar detalhe

**Critérios de aceite**

- resposta contém itens e histórico ordenado;
- pedido inexistente retorna 404 padronizado;
- interface permite link direto e retorno à lista preservando filtros.

### ORD-05 — Alterar status

**Critérios de aceite**

- API valida a transição de forma transacional;
- transição inválida retorna 409;
- histórico registra origem, destino e data;
- UI impede duplo envio e anuncia sucesso/erro;
- cache de lista, detalhe e resumo é reconciliado;
- regras e interação possuem testes.

## Épico 4 — Dashboard e robustez

### DSH-01 — Exibir resumo operacional

**Critérios de aceite**

- BFF entrega indicadores e pedidos recentes em um contrato de tela;
- cards não dependem apenas de cor;
- falha parcial tem comportamento definido e testado.

### QLT-01 — Jornada E2E

**Critérios de aceite**

- Playwright cobre filtro, detalhe e mudança de status;
- teste usa dados determinísticos;
- evidência de falha é armazenada no CI.

### QLT-02 — Auditoria de qualidade

**Critérios de aceite**

- fluxo crítico não apresenta violações críticas no axe;
- navegação completa é possível por teclado;
- bundle e Web Vitals são inspecionados;
- descobertas e eventuais concessões ficam documentadas.

## Ordem recomendada

1. FND-01, FND-02, FND-03 e FND-04 no nível mínimo necessário
2. WLK-01
3. FND-05 e FND-06 no nível mínimo necessário
4. WLK-02 e WLK-03
5. ORD-01, ORD-02 e ORD-03 como primeira fatia real
6. ORD-04
7. ORD-05, integrando o Audit Service
8. DSH-01
9. QLT-01 e QLT-02

Os itens de fundação não precisam estar completos em sua forma final antes do walking skeleton. Eles começam mínimos e são endurecidos conforme as fatias de negócio evoluem.

Cada item deve atravessar todas as camadas necessárias antes do próximo grande recurso. A prioridade é entregar fatias verticais demonstráveis.
