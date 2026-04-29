package biblioteca.dominio.servico;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.SituacaoEmprestimo;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.evento.EventBus;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import biblioteca.dominio.porta.entrada.PortaEmprestimo;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;
import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;
import biblioteca.dominio.porta.saida.PortaNotificacao;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço de empréstimos Stage 3 - Event-driven Architecture.
 * Publica eventos ao realizar operações sem conhecer os consumidores.
 * Totalmente desacoplado via EventBus.
 */
public class EmprestimoServicoComEventos implements PortaEmprestimo {
    private final PortaLivroRepositorio livroRepositorio;
    private final PortaUsuarioRepositorio usuarioRepositorio;
    private final PortaEmprestimoRepositorio emprestimoRepositorio;
    private final PortaNotificacao notificacao;
    private final EventBus<EmprestimoRealizadoEvento> eventosBusEmprestimo;
    private final EventBus<DevolucaoRegistradaEvento> eventosBusDevolucao;
    private Long proximoId = 1L;

    public EmprestimoServicoComEventos(PortaLivroRepositorio livroRepositorio,
                                      PortaUsuarioRepositorio usuarioRepositorio,
                                      PortaEmprestimoRepositorio emprestimoRepositorio,
                                      PortaNotificacao notificacao,
                                      EventBus<EmprestimoRealizadoEvento> eventosBusEmprestimo,
                                      EventBus<DevolucaoRegistradaEvento> eventosBusDevolucao) {
        this.livroRepositorio = livroRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.emprestimoRepositorio = emprestimoRepositorio;
        this.notificacao = notificacao;
        this.eventosBusEmprestimo = eventosBusEmprestimo;
        this.eventosBusDevolucao = eventosBusDevolucao;
    }

    @Override
    public Emprestimo realizarEmprestimo(Long usuarioId, Long livroId) {
        // Validar usuário
        Optional<Usuario> usuarioOpt = usuarioRepositorio.buscarPorId(usuarioId);
        if (!usuarioOpt.isPresent()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        // Validar livro
        Optional<Livro> livroOpt = livroRepositorio.buscarPorId(livroId);
        if (!livroOpt.isPresent()) {
            throw new IllegalArgumentException("Livro não encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        Livro livro = livroOpt.get();

        // Verificar se usuário está suspenso
        if (usuario.getSituacao().name().equals("SUSPENSO")) {
            throw new IllegalArgumentException("Usuário suspenso não pode realizar empréstimos");
        }

        // Realizar empréstimo (regra de negócio no domínio)
        livro.realizarEmprestimo();

        // Criar empréstimo
        LocalDate dataRetirada = LocalDate.now();
        LocalDate dataPrevistaDevolucao = dataRetirada.plusDays(14);
        Emprestimo emprestimo = new Emprestimo(proximoId++, livro, usuario, dataRetirada, dataPrevistaDevolucao);
        
        emprestimoRepositorio.salvar(emprestimo);

        // PUBLICAR EVENTO - sem conhecer os consumidores
        EmprestimoRealizadoEvento evento = new EmprestimoRealizadoEvento(
            emprestimo.getId(),
            usuario.getId(),
            livro.getId(),
            dataRetirada,
            dataPrevistaDevolucao,
            usuario.getNome(),
            usuario.getEmail(),
            livro.getTitulo()
        );
        eventosBusEmprestimo.publicar(evento);

        return emprestimo;
    }

    @Override
    public void registrarDevolucao(Long emprestimoId) {
        Optional<Emprestimo> emprestimoOpt = emprestimoRepositorio.buscarPorId(emprestimoId);
        if (!emprestimoOpt.isPresent()) {
            throw new IllegalArgumentException("Empréstimo não encontrado");
        }

        Emprestimo emprestimo = emprestimoOpt.get();
        LocalDate dataDevolucao = LocalDate.now();
        
        // Registrar devolução
        emprestimo.registrarDevolucao(dataDevolucao);
        emprestimo.getLivro().registrarDevolucao();

        // PUBLICAR EVENTO - sem conhecer os consumidores
        boolean comAtraso = dataDevolucao.isAfter(emprestimo.getDataPrevistaDevolucao());
        DevolucaoRegistradaEvento evento = new DevolucaoRegistradaEvento(
            emprestimo.getId(),
            dataDevolucao,
            comAtraso,
            emprestimo.getUsuario().getNome(),
            emprestimo.getUsuario().getEmail(),
            emprestimo.getLivro().getTitulo()
        );
        eventosBusDevolucao.publicar(evento);
    }

    @Override
    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepositorio.listarTodos().stream()
            .filter(e -> e.getSituacao() == SituacaoEmprestimo.ATIVO)
            .collect(Collectors.toList());
    }

    @Override
    public List<Emprestimo> verificarAtrasos() {
        List<Emprestimo> emprestimosAtivos = listarEmprestimosAtivos();
        
        for (Emprestimo emprestimo : emprestimosAtivos) {
            if (emprestimo.estaAtrasado()) {
                // Notificar atraso
                notificacao.notificarAtraso(emprestimo.getUsuario(), emprestimo);
            }
        }

        return emprestimosAtivos.stream()
            .filter(Emprestimo::estaAtrasado)
            .collect(Collectors.toList());
    }
}
