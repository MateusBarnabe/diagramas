# Sistema de Biblioteca - Projeto de Arquitetura de Software

## Visão Geral

Este projeto implementa um sistema de gerenciamento de acervo e empréstimos de livros para uma rede de bibliotecas municipais, demonstrando três etapas progressivas de evolução arquitetural:

1. **Etapa 1**: Arquitetura em Camadas
2. **Etapa 2**: Arquitetura Hexagonal (Ports and Adapters)
3. **Etapa 3**: Comunicação Assíncrona por Eventos

## Estrutura do Projeto

```
biblioteca/
├── dominio/                    # Camada de domínio (núcleo de negócio)
│   ├── Livro.java
│   ├── Usuario.java
│   ├── Emprestimo.java
│   ├── SituacaoEmprestimo.java
│   ├── SituacaoUsuario.java
│   ├── porta/                  # Portas (abstrações) - Etapa 2+
│   │   ├── entrada/
│   │   │   └── PortaEmprestimo.java
│   │   └── saida/
│   │       ├── PortaLivroRepositorio.java
│   │       ├── PortaUsuarioRepositorio.java
│   │       ├── PortaEmprestimoRepositorio.java
│   │       └── PortaNotificacao.java
│   ├── servico/                # Serviços de aplicação
│   │   ├── EmprestimoServico.java           # Etapa 2
│   │   └── EmprestimoServicoComEventos.java # Etapa 3
│   └── evento/                 # Eventos de domínio - Etapa 3
│       ├── EventBus.java
│       ├── EmprestimoRealizadoEvento.java
│       └── DevolucaoRegistradaEvento.java
├── infraestrutura/
│   └── adaptador/              # Adaptadores (implementações concretas)
│       ├── LivroRepositorio.java                 # Etapa 1
│       ├── UsuarioRepositorio.java               # Etapa 1
│       ├── EmprestimoRepositorio.java            # Etapa 1
│       ├── LivroRepositorioMemoria.java          # Etapa 2
│       ├── LivroRepositorioCsv.java              # Etapa 2
│       ├── UsuarioRepositorioMemoria.java        # Etapa 2
│       ├── EmprestimoRepositorioMemoria.java     # Etapa 2
│       ├── NotificacaoConsole.java               # Etapa 1
│       ├── NotificacaoConsoleAdapter.java        # Etapa 2
│       ├── ServicoDeNotificacao.java             # Etapa 3
│       └── ServicoDeLog.java                     # Etapa 3
├── aplicacao/                  # Serviços de aplicação - Etapa 1
│   ├── LivroServico.java
│   ├── UsuarioServico.java
│   └── EmprestimoServico.java
└── apresentacao/
    └── Main.java               # Demonstração de todas as etapas
```

## Compilação e Execução

### Pré-requisitos
- Java 17 ou superior
- Git (para controle de versão)

### Compilação

```bash
cd "Atividade 6"
mkdir -p bin
javac -d bin -sourcepath src/main/java src/main/java/biblioteca/apresentacao/Main.java
```

### Execução

```bash
java -cp bin biblioteca.apresentacao.Main
```

A execução demonstrará todas as três etapas em sequência.

## Descrição das Etapas

### Etapa 1: Arquitetura em Camadas

**Objetivo**: Implementar separação clara entre camadas com fluxo de dependência unidirecional.

**Características**:
- **Camada de Domínio**: Entidades POJO (Livro, Usuario, Emprestimo) com regras de negócio
- **Camada de Infraestrutura**: Repositórios em memória (HashMap)
- **Camada de Aplicação**: Serviços que orquestram os casos de uso
- **Camada de Apresentação**: Main.java demonstrando fluxo completo

**Casos de Uso Implementados**:
1. `realizarEmprestimo(usuarioId, livroId)` - Realiza novo empréstimo
2. `registrarDevolucao(emprestimoId)` - Registra devolução
3. `listarEmprestimosAtivos()` - Lista empréstimos em andamento
4. `verificarAtrasos()` - Identifica empréstimos atrasados

**Restrição de Dependência**: A camada de domínio não importa classes das outras camadas.

### Etapa 2: Arquitetura Hexagonal (Ports and Adapters)

**Objetivo**: Isolar completamente o domínio de detalhes de infraestrutura através de portas e adaptadores.

**Características**:
- **Portas de Entrada**: `PortaEmprestimo` define interface de casos de uso
- **Portas de Saída**: Interfaces para repositórios e notificações
- **Adaptadores**: Implementações intercambiáveis (Memória vs CSV)
- **Serviço de Domínio**: `EmprestimoServico` implementa portas e depende apenas delas

**Adaptadores Implementados**:
1. **Repositórios em Memória**: LivroRepositorioMemoria, etc.
2. **Repositório CSV**: LivroRepositorioCsv com persistência em arquivo
3. **Notificação Console**: NotificacaoConsoleAdapter

**Demonstração**: A Main substitui o adaptador de repositório sem alterar o serviço ou domínio.

### Etapa 3: Comunicação Assíncrona por Eventos

**Objetivo**: Desacoplar efeitos colaterais do fluxo principal usando padrão Publisher/Subscriber.

**Características**:
- **EventBus Genérico**: Implementação tipada com generics
- **Eventos de Domínio**: EmprestimoRealizadoEvento, DevolucaoRegistradaEvento (records)
- **Handlers Desacoplados**: ServicoDeNotificacao, ServicoDeLog como consumidores

**Fluxo de Eventos**:
1. `EmprestimoServico` publica `EmprestimoRealizadoEvento` após criar empréstimo
2. Múltiplos handlers recebem o evento via `EventBus`:
   - ServicoDeNotificacao envia notificação ao usuário
   - ServicoDeLog registra em arquivo
3. Nenhuma dependência direta entre publicador e consumidores

**Arquivo de Log**: `biblioteca.log` contém registro com timestamp de todas as operações.

## Decisões de Design

### 1. Padrão MVC vs Hexagonal
Escolhemos implementar primeiro o padrão em camadas simples (Etapa 1) para depois evoluir para Hexagonal. Isso reflete aprendizado progressivo.

### 2. Records vs Classes Convencionais (Etapa 3)
Utilizamos `record` para eventos por sua imutabilidade natural e sintaxe concisa. Compatível com Java 16+.

### 3. EventBus Genérico
Implementado sem dependência de frameworks. Uso de `Consumer<T>` permite flexibilidade:
```java
EventBus<EmprestimoRealizadoEvento> eventos = new EventBus<>();
eventos.assinar(handler::processar);
eventos.publicar(evento);
```

### 4. CSV vs Banco de Dados
CSV foi escolhido para Etapa 2 por:
- Simplicidade sem dependências externas
- Demonstração clara do padrão Adapter
- Facilidade de validação manual (arquivo legível)

### 5. Geração de ID de Empréstimo
Utiliza contador `Long proximoId` em memória por simplicidade. Em produção, seria UUID ou sequência de BD.

## Dificuldades Encontradas e Soluções

### 1. Separação entre Etapa 1 e 2
**Problema**: Etapa 1 usava implementações concretas que precisavam ser abstraídas na Etapa 2.
**Solução**: Criou-se pacote `dominio/servico/` com nova implementação usando portas, mantendo Stage 1 para demonstração histórica.

### 2. Substituição de Adaptador CSV
**Problema**: CSV em produção é complexo (parsing, formatação).
**Solução**: Implementação simplificada que lê/escreve linhas separadas por vírgula com tratamento básico de erro.

### 3. Eventos com Dados Complementares
**Problema**: Events precisam conter informação de usuário/livro para handlers funcionarem sem acesso a repositórios.
**Solução**: EmprestimoRealizadoEvento inclui nome, email e título do livro.

### 4. EventBus sem Ordem de Entrega
**Problema**: EventBus atual não garante ordem de processamento nem manejo de exceções.
**Solução**: Documentado no código; em produção usar framework como RabbitMQ/Kafka com guarantees.

## Testes de Validação

Para validar a implementação:

1. **Compile sem erros**:
   ```bash
   javac -d bin -sourcepath src/main/java src/main/java/biblioteca/apresentacao/Main.java
   ```

2. **Execução bem-sucedida**:
   ```bash
   java -cp bin biblioteca.apresentacao.Main
   ```

3. **Verificar saída**:
   - Etapa 1 deve mostrar notificações de empréstimo
   - Etapa 2 deve mostrar substituição de adaptador CSV funcionando
   - Etapa 3 deve mostrar eventos sendo processados por múltiplos handlers

4. **Arquivo de log**:
   ```bash
   cat biblioteca.log
   ```
   Deve conter registros com timestamp de empréstimos e devoluções.

## Extensões Possíveis

1. **Banco de Dados**: Substituir CSV por adaptador JPA/JDBC
2. **API REST**: Adicionar adaptador de entrada com Spring Web
3. **Eventos Assíncrono**: Integrar com RabbitMQ ou Kafka
4. **Autenticação**: Adicionar porta de segurança
5. **Testes Unitários**: JUnit 5 para validar regras de domínio
6. **Documentação OpenAPI**: Swagger para adapters REST

## Conformidade com Requisitos

- [x] Etapa 1: Separação clara de camadas, casos de uso funcionais
- [x] Etapa 2: Portas definidas no domínio, adaptadores na infraestrutura, substituição funcional
- [x] Etapa 3: EventBus genérico, handlers desacoplados, log com timestamp
- [x] Compilação sem erros
- [x] Histórico Git com múltiplos commits
- [x] README documentando compilação, decisões e dificuldades

## Autor
Estudante de Engenharia de Software

## Licença
Acadêmica - Uso educacional
