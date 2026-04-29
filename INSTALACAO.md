# Guia de Instalação e Execução

## Pré-requisitos

- **Java**: versão 17 ou superior
  - Verificar: `java -version`
  - Download: https://www.oracle.com/java/technologies/downloads/

- **Git**: para controle de versão (opcional)
  - Download: https://git-scm.com/

## Passos para Compilação

1. **Abrir terminal/cmd** na pasta do projeto:
   ```bash
   cd "Atividade 6"
   ```

2. **Criar diretório para bytecode**:
   ```bash
   mkdir -p bin
   ```

3. **Compilar o projeto**:
   ```bash
   javac -d bin -sourcepath src/main/java src/main/java/biblioteca/apresentacao/Main.java
   ```

   Se sucesso, nenhuma mensagem de erro será exibida.

## Passos para Execução

1. **Executar a aplicação**:
   ```bash
   java -cp bin biblioteca.apresentacao.Main
   ```

2. **Verificar saída**:
   - Devem aparecer 3 seções (Etapa 1, 2, 3)
   - Cada etapa mostrará sua demonstração

3. **Arquivos gerados**:
   - `livros.csv` - Dados persistidos pela Etapa 2
   - `biblioteca.log` - Auditoria da Etapa 3

## Estrutura do Projeto

```
Atividade 6/
├── src/main/java/biblioteca/
│   ├── dominio/              # Classes de domínio
│   ├── infraestrutura/       # Adaptadores/Repositórios
│   ├── aplicacao/            # Serviços (Etapa 1)
│   └── apresentacao/         # Main.java
├── bin/                      # Bytecode compilado (criado após compilação)
├── README.md                 # Documentação principal
├── ETAPAS.md                 # Descrição das etapas
├── TESTES.md                 # Resultados de teste
└── INSTALACAO.md             # Este arquivo
```

## Troubleshooting

### Erro: "javac não encontrado"
- Verificar se Java está instalado: `java -version`
- Adicionar Java ao PATH das variáveis de ambiente

### Erro: "classe não encontrada"
- Verificar se compilou sem erros: `javac ...`
- Verificar se está no diretório correto
- Tentar limpar e recompilar:
  ```bash
  rm -rf bin
  mkdir -p bin
  javac -d bin -sourcepath src/main/java src/main/java/biblioteca/apresentacao/Main.java
  ```

### Erro de encoding (caracteres estranhos)
- Normal em ambiente Windows com caracteres especiais
- Não afeta a funcionalidade do programa

## Dúvidas Frequentes

**P: Por que há 3 versões de EmprestimoServico?**
R: 
- Etapa 1: `biblioteca.aplicacao.EmprestimoServico` - usa repositórios concretos
- Etapa 2: `biblioteca.dominio.servico.EmprestimoServico` - implementa PortaEmprestimo
- Etapa 3: `EmprestimoServicoComEventos` - publica eventos

**P: Como adiciono mais dados de teste?**
R: Modifique o método `demonstrarEtapa1()` em `Main.java` adicionando mais chamadas a `livroServico.cadastrarLivro()`, etc.

**P: Posso usar um IDE?**
R: Sim! Eclipse, IntelliJ ou VSCode podem compilar e executar diretamente.
