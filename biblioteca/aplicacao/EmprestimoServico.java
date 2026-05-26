package biblioteca.aplicacao;

import biblioteca.dominio.porta.entrada.PortaEmprestimo;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;
import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;
import biblioteca.dominio.porta.saida.PortaNotificacao;
import biblioteca.dominio.evento.EventBus;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.SituacaoEmprestimo;
import biblioteca.dominio.SituacaoUsuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EmprestimoServico implements PortaEmprestimo {
    private final PortaLivroRepositorio livroRepo;
    private final PortaUsuarioRepositorio usuarioRepo;
    private final PortaEmprestimoRepositorio emprestimoRepo;
    private final PortaNotificacao notificacao;
    private final EventBus<EmprestimoRealizadoEvento> eventosBusEmprestimo;
    private final EventBus<DevolucaoRegistradaEvento> eventosBusDevolucao;

    public EmprestimoServico(
            PortaLivroRepositorio livroRepo,
            PortaUsuarioRepositorio usuarioRepo,
            PortaEmprestimoRepositorio emprestimoRepo,
            PortaNotificacao notificacao,
            EventBus<EmprestimoRealizadoEvento> eventosBusEmprestimo,
            EventBus<DevolucaoRegistradaEvento> eventosBusDevolucao) {
        this.livroRepo = livroRepo;
        this.usuarioRepo = usuarioRepo;
        this.emprestimoRepo = emprestimoRepo;
        this.notificacao = notificacao;
        this.eventosBusEmprestimo = eventosBusEmprestimo;
        this.eventosBusDevolucao = eventosBusDevolucao;
    }

    @Override
    public Emprestimo realizarEmprestimo(Long usuarioId, Long livroId) {
        Optional<Usuario> usuarioOpt = usuarioRepo.buscarPorId(usuarioId);
        if (!usuarioOpt.isPresent() || usuarioOpt.get().getSituacao() == SituacaoUsuario.INATIVO) {
            throw new IllegalArgumentException("Usuário inválido ou inativo");
        }

        Optional<Livro> livroOpt = livroRepo.buscarPorId(livroId);
        if (!livroOpt.isPresent()) {
            throw new IllegalArgumentException("Livro não encontrado");
        }

        Livro livro = livroOpt.get();
        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new IllegalArgumentException("Livro não possui quantidade disponível para empréstimo");
        }

        Usuario usuario = usuarioOpt.get();
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setSituacao(SituacaoEmprestimo.EMPRESTADO);
        emprestimo.setDataRetirada(LocalDate.now().toString());
        emprestimo.setDataDevolucao(LocalDate.now().plusDays(7).toString());

        emprestimoRepo.salvar(emprestimo);
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroRepo.salvar(livro);

        notificacao.notificarEmprestimo(usuario, emprestimo);

        // Publicar evento
        if (emprestimo.getId() != null) {
            eventosBusEmprestimo.publicar(new EmprestimoRealizadoEvento(
                    emprestimo.getId(),
                    usuario.getId(),
                    livro.getId(),
                    LocalDate.now()
            ));
        }

        return emprestimo;
    }

    @Override
    public void registrarDevolucao(Long emprestimoId) {
        Optional<Emprestimo> emprestimoOpt = emprestimoRepo.buscarPorId(emprestimoId);
        if (!emprestimoOpt.isPresent()) {
            throw new IllegalArgumentException("Empréstimo não encontrado");
        }

        Emprestimo emprestimo = emprestimoOpt.get();
        if (emprestimo.getSituacao() == SituacaoEmprestimo.DEVOLVIDO) {
            throw new IllegalStateException("Empréstimo já foi devolvido");
        }

        boolean comAtraso = LocalDate.now().isAfter(LocalDate.parse(emprestimo.getDataDevolucao()));
        emprestimo.setSituacao(SituacaoEmprestimo.DEVOLVIDO);
        emprestimo.setDataDevolucao(LocalDate.now().toString());

        Livro livro = emprestimo.getLivro();
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);

        emprestimoRepo.salvar(emprestimo);
        livroRepo.salvar(livro);

        notificacao.notificarDevolucao(emprestimo.getUsuario(), emprestimo);

        // Publicar evento
        eventosBusDevolucao.publicar(new DevolucaoRegistradaEvento(
                emprestimo.getId(),
                LocalDate.now(),
                comAtraso
        ));
    }

    @Override
    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepo.listarTodos().stream()
                .filter(e -> e.getSituacao() == SituacaoEmprestimo.EMPRESTADO)
                .toList();
    }

    @Override
    public List<Emprestimo> verificarAtrasos() {
        atualizarAtrasos();
        return emprestimoRepo.listarTodos().stream()
                .filter(e -> e.getSituacao() == SituacaoEmprestimo.ATRASADO)
                .toList();
    }

    private List<Emprestimo> atualizarAtrasos() {
        return emprestimoRepo.listarTodos().stream()
                .filter(e -> e.getSituacao() == SituacaoEmprestimo.EMPRESTADO &&
                        LocalDate.parse(e.getDataDevolucao()).isBefore(LocalDate.now()))
                .peek(e -> e.setSituacao(SituacaoEmprestimo.ATRASADO))
                .toList();
    }
}
