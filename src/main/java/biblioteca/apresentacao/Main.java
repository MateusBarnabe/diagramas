package biblioteca.apresentacao;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.evento.EventBus;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import biblioteca.dominio.porta.entrada.PortaEmprestimo;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;
import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;
import biblioteca.dominio.porta.saida.PortaNotificacao;
import biblioteca.dominio.servico.EmprestimoServico;
import biblioteca.dominio.servico.EmprestimoServicoComEventos;
import biblioteca.infraestrutura.adaptador.*;
import java.util.List;

/**
 * Classe Main que demonstra todas as três etapas do projeto.
 * Stage 1: Arquitetura em Camadas
 * Stage 2: Arquitetura Hexagonal (Ports and Adapters)
 * Stage 3: Comunicação Assíncrona por Eventos
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("DEMONSTRAÇÃO COMPLETA - SISTEMA DE BIBLIOTECA");
        System.out.println("Etapa 1, 2 e 3");
        System.out.println("=".repeat(80));

        // ========== ETAPA 1 - ARQUITETURA EM CAMADAS ==========
        demonstrarEtapa1();

        System.out.println("\n");

        // ========== ETAPA 2 - ARQUITETURA HEXAGONAL ==========
        demonstrarEtapa2();

        System.out.println("\n");

        // ========== ETAPA 3 - COMUNICAÇÃO ASSÍNCRONA POR EVENTOS ==========
        demonstrarEtapa3();

        System.out.println("\n" + "=".repeat(80));
        System.out.println("FIM DA DEMONSTRAÇÃO");
        System.out.println("=".repeat(80) + "\n");
    }

    /**
     * Demonstra a Etapa 1 - Arquitetura em Camadas.
     */
    private static void demonstrarEtapa1() {
        System.out.println("\n" + "-".repeat(80));
        System.out.println("ETAPA 1 - ARQUITETURA EM CAMADAS");
        System.out.println("-".repeat(80));

        // Inicializar repositórios
        LivroRepositorio livroRepositorio = new LivroRepositorio();
        UsuarioRepositorio usuarioRepositorio = new UsuarioRepositorio();
        EmprestimoRepositorioMemoria emprestimoRepositorio = new EmprestimoRepositorioMemoria();
        NotificacaoConsole notificacao = new NotificacaoConsole();

        // Inicializar serviços
        biblioteca.aplicacao.LivroServico livroServico = new biblioteca.aplicacao.LivroServico(livroRepositorio);
        biblioteca.aplicacao.UsuarioServico usuarioServico = new biblioteca.aplicacao.UsuarioServico(usuarioRepositorio);
        biblioteca.aplicacao.EmprestimoServico emprestimoServico = new biblioteca.aplicacao.EmprestimoServico(
            livroRepositorio, usuarioRepositorio, emprestimoRepositorio, notificacao);

        // Cadastrar livros
        System.out.println("\n[1] Cadastrando livros...");
        livroServico.cadastrarLivro(1L, "Clean Code", "Robert Martin", "978-0132350884", 3);
        livroServico.cadastrarLivro(2L, "Design Patterns", "Gamma et al.", "978-0201633610", 2);
        System.out.println("✓ 2 livros cadastrados");

        // Cadastrar usuários
        System.out.println("\n[2] Cadastrando usuários...");
        usuarioServico.cadastrarUsuario(1L, "João Silva", "joao@example.com");
        usuarioServico.cadastrarUsuario(2L, "Maria Santos", "maria@example.com");
        System.out.println("✓ 2 usuários cadastrados");

        // Realizar empréstimos
        System.out.println("\n[3] Realizando empréstimos...");
        try {
            Emprestimo emp1 = emprestimoServico.realizarEmprestimo(1L, 1L);
            System.out.println("✓ Empréstimo 1: " + emp1.getLivro().getTitulo());
            
            Emprestimo emp2 = emprestimoServico.realizarEmprestimo(2L, 2L);
            System.out.println("✓ Empréstimo 2: " + emp2.getLivro().getTitulo());
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }

        // Listar empréstimos ativos
        System.out.println("\n[4] Empréstimos ativos: " + emprestimoServico.listarEmprestimosAtivos().size());
    }

    /**
     * Demonstra a Etapa 2 - Arquitetura Hexagonal com substituição de adaptador.
     */
    private static void demonstrarEtapa2() {
        System.out.println("\n" + "-".repeat(80));
        System.out.println("ETAPA 2 - ARQUITETURA HEXAGONAL (PORTS AND ADAPTERS)");
        System.out.println("-".repeat(80));

        // ===== Composição com Adaptador em Memória =====
        System.out.println("\n[FASE 1] Usando Adaptador em Memória:");
        System.out.println("-".repeat(40));

        PortaLivroRepositorio livroRepoMemoria = new LivroRepositorioMemoria();
        PortaUsuarioRepositorio usuarioRepoMemoria = new UsuarioRepositorioMemoria();
        PortaEmprestimoRepositorio emprestimoRepoMemoria = new EmprestimoRepositorioMemoria();
        PortaNotificacao notificacaoMemoria = new NotificacaoConsoleAdapter();

        PortaEmprestimo servicoMemoria = new EmprestimoServico(
            livroRepoMemoria, usuarioRepoMemoria, emprestimoRepoMemoria, notificacaoMemoria);

        // Cadastrar dados
        livroRepoMemoria.salvar(new biblioteca.dominio.Livro(
            10L, "Refactoring", "Martin Fowler", "978-0134757599", 2));
        livroRepoMemoria.salvar(new biblioteca.dominio.Livro(
            11L, "Code Complete", "Steve McConnell", "978-0735619678", 1));
        
        usuarioRepoMemoria.salvar(new biblioteca.dominio.Usuario(
            10L, "Ana Costa", "ana@example.com", biblioteca.dominio.SituacaoUsuario.ATIVO));
        usuarioRepoMemoria.salvar(new biblioteca.dominio.Usuario(
            11L, "Carlos Mendes", "carlos@example.com", biblioteca.dominio.SituacaoUsuario.ATIVO));

        // Executar caso de uso
        try {
            Emprestimo emp1 = servicoMemoria.realizarEmprestimo(10L, 10L);
            System.out.println("✓ Empréstimo realizado: " + emp1.getLivro().getTitulo());
            
            Emprestimo emp2 = servicoMemoria.realizarEmprestimo(11L, 11L);
            System.out.println("✓ Empréstimo realizado: " + emp2.getLivro().getTitulo());

            System.out.println("✓ Total de empréstimos: " + servicoMemoria.listarEmprestimosAtivos().size());
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }

        // ===== Composição com Adaptador CSV =====
        System.out.println("\n[FASE 2] Substituindo Adaptador para CSV (mesmo código de negócio):");
        System.out.println("-".repeat(40));

        PortaLivroRepositorio livroRepoCsv = new LivroRepositorioCsv("livros.csv");
        PortaEmprestimo servicoCsv = new EmprestimoServico(
            livroRepoCsv, usuarioRepoMemoria, emprestimoRepoMemoria, notificacaoMemoria);

        // Salvar livros no CSV
        livroRepoCsv.salvar(new biblioteca.dominio.Livro(
            20L, "Domain-Driven Design", "Eric Evans", "978-0321125675", 1));
        livroRepoCsv.salvar(new biblioteca.dominio.Livro(
            21L, "The Pragmatic Programmer", "Hunt & Thomas", "978-0201616224", 2));

        try {
            Emprestimo emp3 = servicoCsv.realizarEmprestimo(10L, 20L);
            System.out.println("✓ Empréstimo com CSV: " + emp3.getLivro().getTitulo());
            
            List<biblioteca.dominio.Livro> livrosCSV = livroRepoCsv.listarTodos();
            System.out.println("✓ Livros persistidos em CSV: " + livrosCSV.size());
            livrosCSV.forEach(l -> System.out.println("  → " + l.getTitulo()));
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }

        System.out.println("\n✓ Demonstrado: Adaptador foi substituído sem alterar o domínio ou o serviço!");
    }

    /**
     * Demonstra a Etapa 3 - Comunicação Assíncrona por Eventos.
     */
    private static void demonstrarEtapa3() {
        System.out.println("\n" + "-".repeat(80));
        System.out.println("ETAPA 3 - COMUNICAÇÃO ASSÍNCRONA POR EVENTOS");
        System.out.println("-".repeat(80));

        // Criar barramentos de eventos
        EventBus<EmprestimoRealizadoEvento> eventosBusEmprestimo = new EventBus<>();
        EventBus<DevolucaoRegistradaEvento> eventosBusDevolucao = new EventBus<>();

        // Criar handlers desacoplados
        ServicoDeNotificacao servicoNotificacao = new ServicoDeNotificacao();
        ServicoDeLog servicoLog = new ServicoDeLog();

        // Registrar consumidores no barramento (sem que o publicador os conheça)
        eventosBusEmprestimo.assinar(servicoNotificacao::aoEmprestimoCriado);
        eventosBusEmprestimo.assinar(servicoLog::aoEmprestimoCriado);

        eventosBusDevolucao.assinar(servicoLog::aoDevolucoRegistrada);

        // Inicializar repositórios e serviço com eventos
        PortaLivroRepositorio livroRepo = new LivroRepositorioMemoria();
        PortaUsuarioRepositorio usuarioRepo = new UsuarioRepositorioMemoria();
        PortaEmprestimoRepositorio emprestimoRepo = new EmprestimoRepositorioMemoria();
        PortaNotificacao notificacao = new NotificacaoConsoleAdapter();

        PortaEmprestimo servicoComEventos = new EmprestimoServicoComEventos(
            livroRepo, usuarioRepo, emprestimoRepo, notificacao,
            eventosBusEmprestimo, eventosBusDevolucao);

        // Preparar dados
        livroRepo.salvar(new biblioteca.dominio.Livro(
            30L, "Microservices Patterns", "Chris Richardson", "978-1617294549", 1));
        livroRepo.salvar(new biblioteca.dominio.Livro(
            31L, "Building Microservices", "Sam Newman", "978-1491950357", 2));

        usuarioRepo.salvar(new biblioteca.dominio.Usuario(
            30L, "Lucas Oliveira", "lucas@example.com", biblioteca.dominio.SituacaoUsuario.ATIVO));
        usuarioRepo.salvar(new biblioteca.dominio.Usuario(
            31L, "Beatriz Ferreira", "beatriz@example.com", biblioteca.dominio.SituacaoUsuario.ATIVO));

        System.out.println("\n[1] Publicando eventos de empréstimo:");
        System.out.println("(Múltiplos consumidores receberão os eventos automaticamente)");
        System.out.println("-".repeat(40));

        try {
            Emprestimo emp1 = servicoComEventos.realizarEmprestimo(30L, 30L);
            System.out.println("→ Empréstimo criado: ID " + emp1.getId());

            Emprestimo emp2 = servicoComEventos.realizarEmprestimo(31L, 31L);
            System.out.println("→ Empréstimo criado: ID " + emp2.getId());
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }

        System.out.println("\n[2] Registrando devoluções e publicando eventos:");
        System.out.println("-".repeat(40));

        try {
            servicoComEventos.registrarDevolucao(1L);
            System.out.println("→ Devolução registrada: ID 1");

            servicoComEventos.registrarDevolucao(2L);
            System.out.println("→ Devolução registrada: ID 2");
        } catch (Exception e) {
            System.out.println("✗ Erro: " + e.getMessage());
        }

        System.out.println("\n✓ Demonstrado: Eventos foram publicados e consumidos de forma desacoplada!");
        System.out.println("✓ Verificar arquivo 'biblioteca.log' para registros de auditoria.");
    }
}
