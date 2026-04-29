## Etapa 1: Arquitetura em Camadas

- Implementadas classes de domínio: Livro, Usuario, Emprestimo
- Repositórios em memória (HashMap)
- Serviços de aplicação orchestrando casos de uso
- Demonstração funcional em Main.java

## Etapa 2: Arquitetura Hexagonal

- Portas definidas no pacote dominio/porta
- Adaptadores: Memória e CSV
- EmprestimoServico implementa PortaEmprestimo
- Desacoplamento completo entre domínio e infraestrutura

## Etapa 3: Comunicação Assíncrona por Eventos

- EventBus genérico com suporte a múltiplos assinantes
- Eventos: EmprestimoRealizadoEvento, DevolucaoRegistradaEvento
- Handlers desacoplados: ServicoDeNotificacao, ServicoDeLog
- Arquivo de log com timestamp de operações
