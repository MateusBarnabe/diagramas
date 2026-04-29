package biblioteca.aplicacao;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.SituacaoEmprestimo;
import biblioteca.dominio.Usuario;
import biblioteca.infraestrutura.adaptador.EmprestimoRepositorioMemoria;
import biblioteca.infraestrutura.adaptador.LivroRepositorio;
import biblioteca.infraestrutura.adaptador.UsuarioRepositorio;
import biblioteca.infraestrutura.adaptador.NotificacaoConsole;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço de aplicação para gerenciar empréstimos.
 * Implementa os casos de uso principais do sistema.
 */
public class EmprestimoServico {
    private final LivroRepositorio livroRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final EmprestimoRepositorioMemoria emprestimoRepositorio;
    private final NotificacaoConsole notificacao;
    private Long proximoId = 1L;

    public EmprestimoServico(LivroRepositorio livroRepositorio,
                            UsuarioRepositorio usuarioRepositorio,
                            EmprestimoRepositorioMemoria emprestimoRepositorio,
                            NotificacaoConsole notificacao) {
        this.livroRepositorio = livroRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.emprestimoRepositorio = emprestimoRepositorio;
        this.notificacao = notificacao;
    }

    /**
     * Realiza um empréstimo de um livro para um usuário.
     * Caso de uso obrigatório.
     */
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

    /**
     * Registra a devolução de um livro.
     * Caso de uso obrigatório.
     */
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

    /**
     * Lista todos os empréstimos ativos.
     * Caso de uso obrigatório.
     */
    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepositorio.listarTodos().stream()
            .filter(e -> e.getSituacao() == SituacaoEmprestimo.ATIVO)
            .collect(Collectors.toList());
    }

    /**
     * Verifica empréstimos em atraso e atualiza sua situação.
     * Caso de uso obrigatório.
     */
    public List<Emprestimo> verificarAtrasos() {
        List<Emprestimo> emprestimosAtivos = listarEmprestimosAtivos();
        
        for (Emprestimo emprestimo : emprestimosAtivos) {
            if (emprestimo.estaAtrasado()) {
                // Notificar atraso
                notificacao.notificarAtraso(emprestimo.getUsuario(),
                    "Empréstimo em atraso: " + emprestimo.getLivro().getTitulo() + 
                    ". Data limite era: " + emprestimo.getDataPrevistaDevolucao());
            }
        }

        return emprestimosAtivos.stream()
            .filter(Emprestimo::estaAtrasado)
            .collect(Collectors.toList());
    }
}
