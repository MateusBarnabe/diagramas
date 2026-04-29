package biblioteca.dominio.servico;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.SituacaoEmprestimo;
import biblioteca.dominio.Usuario;
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
 * Serviço de empréstimos Stage 2 - Hexagonal Architecture.
 * Implementa PortaEmprestimo (Input Port) e depende apenas de portas (Output Ports).
 * Totalmente desacoplado de implementações concretas.
 */
public class EmprestimoServico implements PortaEmprestimo {
    private final PortaLivroRepositorio livroRepositorio;
    private final PortaUsuarioRepositorio usuarioRepositorio;
    private final PortaEmprestimoRepositorio emprestimoRepositorio;
    private final PortaNotificacao notificacao;
    private Long proximoId = 1L;

    public EmprestimoServico(PortaLivroRepositorio livroRepositorio,
                            PortaUsuarioRepositorio usuarioRepositorio,
                            PortaEmprestimoRepositorio emprestimoRepositorio,
                            PortaNotificacao notificacao) {
        this.livroRepositorio = livroRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.emprestimoRepositorio = emprestimoRepositorio;
        this.notificacao = notificacao;
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

        // Notificar usuário
        notificacao.notificarEmprestimo(usuario, 
            "Empréstimo realizado: " + livro.getTitulo() + 
            ". Data prevista de devolução: " + dataPrevistaDevolucao);

        return emprestimo;
    }

    @Override
    public void registrarDevolucao(Long emprestimoId) {
        Optional<Emprestimo> emprestimoOpt = emprestimoRepositorio.buscarPorId(emprestimoId);
        if (!emprestimoOpt.isPresent()) {
            throw new IllegalArgumentException("Empréstimo não encontrado");
        }

        Emprestimo emprestimo = emprestimoOpt.get();
        
        // Registrar devolução
        emprestimo.registrarDevolucao(LocalDate.now());
        emprestimo.getLivro().registrarDevolucao();

        notificacao.notificarEmprestimo(emprestimo.getUsuario(),
            "Devolução registrada para: " + emprestimo.getLivro().getTitulo());
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
