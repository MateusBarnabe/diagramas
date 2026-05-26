package biblioteca.apresentacao;

import biblioteca.aplicacao.EmprestimoServico;
import biblioteca.aplicacao.LivroServicoHexagonal;
import biblioteca.aplicacao.UsuarioServicoHexagonal;
import biblioteca.dominio.porta.entrada.PortaEmprestimo;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;
import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;
import biblioteca.dominio.porta.saida.PortaNotificacao;
import biblioteca.dominio.evento.EventBus;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import biblioteca.infraestrutura.adaptador.*;
import biblioteca.infraestrutura.evento.ServicoDeNotificacao;
import biblioteca.infraestrutura.evento.ServicoDeLog;

public class Main {

    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE BIBLIOTECA - ARQUITETURA HEXAGONAL COM EVENTOS     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        // ================== ETAPA 2: Arquitetura Hexagonal ==================
        System.out.println("█ ETAPA 2: DEMONSTRAÇÃO DE ADAPTADORES HEXAGONAIS\n");

        demonstrarComAdaptadorMemoria();
        System.out.println("\n" + "=".repeat(65) + "\n");
        demonstrarComAdaptadorCsv();

        // ================== ETAPA 3: Eventos Assíncronos ==================
        System.out.println("\n\n█ ETAPA 3: DEMONSTRAÇÃO DE EVENTOS ASSÍNCRONOS\n");
        demonstrarComEventos();
    }

    private static void demonstrarComAdaptadorMemoria() {
        System.out.println("► Composição com ADAPTADOR EM MEMÓRIA\n");

        // Composição de dependências - com repositórios em memória
        PortaLivroRepositorio livroRepo = new LivroRepositorioMemoria();
        PortaUsuarioRepositorio usuarioRepo = new UsuarioRepositorioMemoria();
        PortaEmprestimoRepositorio emprestimoRepo = new EmprestimoRepositorioMemoria();
        PortaNotificacao notificacao = new NotificacaoConsole();

        // Criar buses de eventos vazios para esta demonstração
        EventBus<EmprestimoRealizadoEvento> busEmprestimo = new EventBus<>();
        EventBus<DevolucaoRegistradaEvento> busDevolucao = new EventBus<>();

        // Serviços de aplicação
        LivroServicoHexagonal livroServico = new LivroServicoHexagonal(livroRepo);
        UsuarioServicoHexagonal usuarioServico = new UsuarioServicoHexagonal(usuarioRepo);
        PortaEmprestimo emprestimoServico = new EmprestimoServico(
                livroRepo, usuarioRepo, emprestimoRepo, notificacao, busEmprestimo, busDevolucao
        );

        // Usar o serviço
        livroServico.adicionarLivro("Clean Code", "Robert C. Martin", "978-0132350884", 5);
        livroServico.adicionarLivro("Design Patterns", "Gang of Four", "978-0201633610", 3);
        usuarioServico.registrarUsuario("Alice Silva", "alice@email.com");
        usuarioServico.registrarUsuario("Bob Santos", "bob@email.com");

        emprestimoServico.realizarEmprestimo(1L, 1L);
        emprestimoServico.realizarEmprestimo(2L, 2L);

        System.out.println("✓ Empréstimos ativos: " + emprestimoServico.listarEmprestimosAtivos().size());
    }

    private static void demonstrarComAdaptadorCsv() {
        System.out.println("► Composição com ADAPTADOR CSV (Sem alterar lógica de negócio)\n");

        // Composição de dependências - com repositórios em CSV
        PortaLivroRepositorio livroRepoCsv = new LivroRepositorioCsv("livros.csv");
        PortaUsuarioRepositorio usuarioRepoCsv = new UsuarioRepositorioCsv("usuarios.csv");
        PortaEmprestimoRepositorio emprestimoRepoCsv = new EmprestimoRepositorioCsv("emprestimos.csv");
        PortaNotificacao notificacao = new NotificacaoConsole();

        // Criar buses de eventos vazios para esta demonstração
        EventBus<EmprestimoRealizadoEvento> busEmprestimo = new EventBus<>();
        EventBus<DevolucaoRegistradaEvento> busDevolucao = new EventBus<>();

        // Serviços de aplicação - com os mesmos nomes de classe
        LivroServicoHexagonal livroServicoCsv = new LivroServicoHexagonal(livroRepoCsv);
        UsuarioServicoHexagonal usuarioServicoCsv = new UsuarioServicoHexagonal(usuarioRepoCsv);
        PortaEmprestimo emprestimoServicoCsv = new EmprestimoServico(
                livroRepoCsv, usuarioRepoCsv, emprestimoRepoCsv, notificacao, busEmprestimo, busDevolucao
        );

        // A lógica é IDÊNTICA, apenas os adaptadores mudaram
        livroServicoCsv.adicionarLivro("The Pragmatic Programmer", "Hunt & Thomas", "978-0201616224", 4);
        usuarioServicoCsv.registrarUsuario("Charlie Costa", "charlie@email.com");

        try {
            emprestimoServicoCsv.realizarEmprestimo(1L, 1L);
            System.out.println("✓ Livro salvo em CSV com sucesso!");
        } catch (Exception e) {
            System.out.println("⚠ Nota: CSV pode estar vazio na primeira execução");
        }

        System.out.println("✓ Adaptadores trocados sem alterar uma linha de código do domínio!");
    }

    private static void demonstrarComEventos() {
        System.out.println("► FUNCIONAMENTO DE EVENTOS ASSÍNCRONOS (Desacoplamento via EventBus)\n");

        // Composição com EventBus
        PortaLivroRepositorio livroRepo = new LivroRepositorioMemoria();
        PortaUsuarioRepositorio usuarioRepo = new UsuarioRepositorioMemoria();
        PortaEmprestimoRepositorio emprestimoRepo = new EmprestimoRepositorioMemoria();
        PortaNotificacao notificacao = new NotificacaoConsole();

        // Criar buses de eventos
        EventBus<EmprestimoRealizadoEvento> busEmprestimo = new EventBus<>();
        EventBus<DevolucaoRegistradaEvento> busDevolucao = new EventBus<>();

        // Serviços de aplicação
        LivroServicoHexagonal livroServico = new LivroServicoHexagonal(livroRepo);
        UsuarioServicoHexagonal usuarioServico = new UsuarioServicoHexagonal(usuarioRepo);
        PortaEmprestimo emprestimoServico = new EmprestimoServico(
                livroRepo, usuarioRepo, emprestimoRepo, notificacao, busEmprestimo, busDevolucao
        );

        // Consumidores de eventos
        ServicoDeNotificacao servicoNotificacao = new ServicoDeNotificacao(livroRepo, usuarioRepo);
        ServicoDeLog servicoLog = new ServicoDeLog("biblioteca.log");

        // Registrar handlers no bus - SEM ACOPLAMENTO
        busEmprestimo.assinar(evento -> servicoNotificacao.procesarEmprestimoRealizado(evento));
        busEmprestimo.assinar(evento -> servicoLog.registrarEmprestimoRealizado(evento));

        busDevolucao.assinar(evento -> servicoNotificacao.procesarDevolucaoRegistrada(evento));
        busDevolucao.assinar(evento -> servicoLog.registrarDevolucaoRegistrada(evento));

        // Dados
        livroServico.adicionarLivro("Java Concurrency", "Brian Goetz", "978-0321349606", 2);
        livroServico.adicionarLivro("Spring in Action", "Craig Walls", "978-1617299056", 1);
        usuarioServico.registrarUsuario("Diana Lima", "diana@email.com");
        usuarioServico.registrarUsuario("Eduardo Pires", "eduardo@email.com");

        // Operações que disparam eventos
        System.out.println("▶ Realizando empréstimo (evento será publicado)...\n");
        emprestimoServico.realizarEmprestimo(1L, 1L);

        System.out.println("▶ Registrando devolução (evento será publicado)...\n");
        emprestimoServico.registrarDevolucao(1L);

        System.out.println("✓ EmprestimoServico não conhece ServicoDeNotificacao nem ServicoDeLog!");
        System.out.println("✓ Comunicação exclusiva via EventBus!");
        System.out.println("✓ Logs salvos em 'biblioteca.log'");
    }
}
