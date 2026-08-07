# Architecture Decision Records

Os ADRs registram decisões arquiteturais relevantes, seu contexto e suas consequências. Depois de aceita, uma decisão não é apagada: se mudar, um novo ADR substitui o anterior e preserva o histórico.

## Status possíveis

- **Proposto**: aguardando validação;
- **Aceito**: decisão vigente;
- **Substituído**: trocado por outro ADR;
- **Descontinuado**: não se aplica mais.

## Índice

| ADR | Decisão | Status |
|---|---|---|
| [ADR-0001](0001-integration-first-walking-skeleton.md) | Começar por um walking skeleton integrado | Aceito |
| [ADR-0002](0002-kafka-in-first-vertical-slice.md) | Integrar Kafka na primeira fatia vertical | Aceito |
| [ADR-0003](0003-aws-identity-and-secret-management.md) | Usar identidades temporárias e segredos gerenciados na AWS | Aceito |
| [ADR-0004](0004-ephemeral-cost-controlled-aws-poc.md) | Executar a PoC AWS sob demanda, por PR e com expiração automática | Aceito |

## Modelo

```text
# ADR-NNNN — Título

- Status
- Data
- Responsáveis
- Substitui
- Substituído por

## Contexto
## Decisão
## Alternativas consideradas
## Consequências positivas
## Consequências negativas e controles
## Critérios de validação
## Gatilhos para revisão
```
