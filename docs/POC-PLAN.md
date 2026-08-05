# Plano da PoC

## 1. Visão do produto

O **Operations Hub** é um painel interno para uma equipe acompanhar e tratar pedidos que exigem atenção operacional.

Problema demonstrado: operadores precisam localizar rapidamente pedidos, entender seu histórico e avançar seu status com segurança. O painel reduz navegação dispersa e apresenta uma visão orientada a tarefas.

### Usuário principal

Analista de operações que trabalha diariamente com uma fila de pedidos.

### Jornada principal

1. Visualizar indicadores resumidos da operação.
2. Pesquisar e filtrar pedidos por texto, status e período.
3. Paginar e ordenar os resultados.
4. Abrir os detalhes de um pedido.
5. Consultar cliente, itens e histórico de status.
6. Alterar o status, recebendo confirmação visual ou erro recuperável.

## 2. Escopo do MVP

### Incluído

- dashboard com indicadores de volume por status;
- listagem responsiva de pedidos;
- busca, filtros, ordenação e paginação no servidor;
- página ou drawer de detalhes;
- transição de status com validação de negócio;
- estados de carregamento, vazio e erro;
- feedback por toast e confirmação para ação sensível;
- API REST documentada com OpenAPI;
- persistência em PostgreSQL e migrations;
- dados de demonstração reproduzíveis;
- testes nas camadas principais;
- execução local via Docker Compose;
- CI com lint, testes e build.

### Fora do MVP

- autenticação real e gestão de usuários;
- pagamentos, emissão fiscal ou integração logística real;
- atualização em tempo real;
- aplicação mobile nativa;
- implantação em nuvem;
- microsserviços adicionais além de Orders e Audit.

Autenticação pode ser simulada com um usuário fixo e um header de correlação. Isso mantém o foco nas competências pedidas sem introduzir uma plataforma de identidade inteira.

## 3. Stack proposta

| Camada | Tecnologia | Papel na PoC |
|---|---|---|
| Web | React 19, TypeScript e Vite | Interface principal |
| Roteamento | React Router | Rotas, parâmetros e filtros na URL |
| Estado remoto | TanStack Query | Cache, sincronização e mutations |
| Estado de UI | Zustand | Preferências e estado transversal pequeno |
| Formulários | React Hook Form + Zod | Entrada e validação tipada |
| Estilos | Tailwind CSS | Design responsivo e consistente |
| Componentes | Componentes próprios acessíveis | Demonstrar composição sem ocultar fundamentos |
| BFF | NestJS + TypeScript | Adaptar e agregar dados para a interface |
| Backend | Java 21 + Spring Boot | Microsserviços de pedidos e auditoria |
| Dados | PostgreSQL + Flyway | Persistência e migrations |
| Eventos | Apache Kafka | Integração assíncrona entre Orders e Audit |
| Testes web | Vitest, Jest DOM e React Testing Library | Unidade e componentes |
| Testes BFF | Jest + Supertest | Unidade e contrato HTTP |
| Testes backend | JUnit 5, Mockito e Testcontainers | Unidade e integração |
| E2E | Playwright | Jornada crítica em navegador |
| Contratos | OpenAPI | Documentação e alinhamento entre camadas |
| Ambiente | Docker Compose | Execução reproduzível |
| Containers | Docker | Empacotamento de web, BFF e API |
| Orquestração | Kubernetes + Helm | Deploy declarativo e configuração por ambiente |
| Nuvem | AWS EKS, RDS, ECR e serviços de apoio | Ambiente remoto da PoC |
| CI/CD | GitHub Actions | Qualidade, testes, imagens e deploy |

As versões exatas serão fixadas somente no início da implementação, após verificar compatibilidade e versões estáveis atuais.

## 4. Como os requisitos serão evidenciados

| Competência | Evidência planejada |
|---|---|
| React e hooks | hooks de filtros, debounce, preferências e atualização de pedido |
| `useEffect` | sincronização estritamente necessária com APIs externas do browser |
| `useMemo` | derivação mensurável de configuração/visualização, sem uso ornamental |
| `useCallback` | callbacks estáveis somente onde houver benefício para componentes memoizados |
| Custom hooks | `useOrderFilters`, `useOrders` e `useUpdateOrderStatus` |
| Estado global | Zustand para preferências; URL para filtros; Query para estado remoto |
| TypeScript | modo strict e contratos tipados em todo o front-end/BFF |
| Estilização | Tailwind com tokens CSS e componentes responsivos |
| BFF/NestJS | endpoint de visão do dashboard e adaptação de payloads da API Java |
| REST | recursos, paginação, filtros, validação e respostas de erro consistentes |
| Java/Spring | regras de transição, casos de uso, JPA e controller REST |
| Testes | pirâmide com unidade, componente, integração e uma jornada E2E |
| Git | commits pequenos, convenção e template de pull request |
| Performance | divisão por rota, cache, debounce e acompanhamento de Web Vitals |
| Acessibilidade | navegação por teclado, foco, semântica e teste automatizado com axe |
| Microsserviços | Orders e Audit com responsabilidades e dados próprios |
| CI/CD | pipeline de validação e imagem de container pronta para publicação |
| Docker | containers para web, BFF, API e banco no ambiente integrado |
| Kubernetes | manifests/Helm, probes, recursos e configuração externa |
| AWS | EKS para workloads, ECR para imagens e RDS PostgreSQL para dados |
| Kafka | evento `OrderCreated`, outbox e consumo idempotente desde o walking skeleton |

## 5. Princípios de implementação

- O front-end não acessa diretamente a API Java; todo consumo passa pelo BFF.
- O BFF conhece necessidades de apresentação, mas não concentra regras de negócio.
- O Orders Service é a fonte de verdade para pedidos e transições de status.
- Filtros compartilháveis permanecem na URL, não em um store global.
- Estado de servidor não é duplicado no Zustand.
- Erros têm formato previsível e um `correlationId` ponta a ponta.
- Acessibilidade e responsividade fazem parte dos critérios de aceite.
- Complexidade só será adicionada quando demonstrar uma decisão relevante.

## 6. Fases de entrega

### Fase 0 — Walking skeleton integrado

- monorepo, padrões de código e contratos mínimos;
- React, BFF e dois serviços Spring conectados ponta a ponta;
- PostgreSQL acessado por um fluxo real;
- Kafka exercitado por publicação e consumo reais;
- Docker Compose com todo o sistema;
- imagens Docker, Kubernetes e ambiente AWS mínimo;
- CI/CD inicial com deploy e smoke test;
- página visual simples para comprovar a integração.

### Fase 1 — Leitura de pedidos

- modelo, migration e seed;
- listagem REST com filtros e paginação;
- adaptação no BFF;
- tela responsiva com loading, vazio e erro;
- testes unitários e de componentes.

### Fase 2 — Detalhes e ação

- endpoint de detalhes e histórico;
- drawer/página de detalhes acessível;
- alteração de status com regras de transição;
- mutation, feedback e recuperação de erro;
- testes de integração.

### Fase 3 — Dashboard e qualidade

- indicadores agregados;
- testes E2E da jornada crítica;
- auditoria de a11y e performance;
- observabilidade básica e documentação OpenAPI.

### Fase 4 — Empacotamento

- imagens Docker;
- Docker Compose local completo;
- chart Helm e ambiente Kubernetes;
- infraestrutura AWS documentada e reproduzível;
- pipeline CI/CD completo;
- documentação de execução e decisões;
- vídeo ou GIF curto da aplicação para o portfólio.

## 7. Definição de pronto

Uma história está pronta quando:

- critérios funcionais e estados alternativos estão cobertos;
- TypeScript/Java compilam sem erros;
- lint e formatação passam;
- testes proporcionais ao risco foram adicionados;
- interface funciona em viewport móvel e desktop;
- fluxo é utilizável por teclado e sem erros críticos de axe;
- erros são tratados sem expor detalhes internos;
- documentação relevante foi atualizada;
- pipeline está verde.

## 8. Métricas-alvo da PoC

- jornada principal coberta por ao menos um teste Playwright;
- regras de transição de status integralmente cobertas por testes unitários;
- nenhum problema crítico de acessibilidade no fluxo principal;
- Lighthouse local como referência: pelo menos 90 em acessibilidade e boas práticas;
- resposta paginada e cacheada, sem carregar toda a base no browser;
- build, lint e testes executados automaticamente no CI.

Cobertura percentual global não será usada isoladamente como meta. O foco será cobrir regras, estados e riscos relevantes.

## 9. Riscos e controles

| Risco | Controle |
|---|---|
| Escopo excessivo | uma jornada vertical antes de funcionalidades extras |
| BFF virar proxy sem valor | incluir agregação de dashboard e DTO orientado à tela |
| BFF absorver regra de negócio | manter transições e invariantes no Spring |
| Uso artificial de hooks | justificar hooks por comportamento e medição |
| Muitos serviços para uma PoC | limitar a Orders e Audit, exigindo responsabilidade clara |
| Testes frágeis | testar comportamento público, não detalhes internos |
| Ambiente pesado | perfis local e integrado; containers focados no fluxo completo |

## 10. Decisões pendentes antes de codificar

1. Confirmar se o domínio de pedidos representa bem o portfólio desejado.
2. Escolher entre drawer e rota dedicada para detalhes — a rota dedicada é a recomendação inicial por acessibilidade e compartilhamento.
3. Definir identidade visual mínima: neutra corporativa ou marca fictícia.
4. Decidir se o deploy público faz parte da entrega; não é necessário para validar a arquitetura local.
