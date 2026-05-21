package biblioteca.aplicacao;

import biblioteca.dominio.SituacaoUsuario;

import java.time.LocalDate;
import java.util.List;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.SituacaoEmprestimo;
import biblioteca.dominio.Usuario;
import biblioteca.infraestrutura.EmprestimoRepositorio;
import biblioteca.infraestrutura.LivroRepositorio;
import biblioteca.infraestrutura.UsuarioRepositorio;

public class RepositorioServico {
    
    public Emprestimo realizarEmprestimo(Long usuarioID, long livroID){
        EmprestimoRepositorio emprestimoRepositorio = EmprestimoRepositorio.getInstance();
        UsuarioRepositorio usuarioRepositorio = UsuarioRepositorio.getInstance();
        Usuario usuario = usuarioRepositorio.buscarPorId(usuarioID);
        if (usuario == null || usuario.getSituacao() == SituacaoUsuario.INATIVO) {
            throw new IllegalArgumentException("Usuário inválido ou inativo");
        }

        LivroRepositorio livroRepositorio = LivroRepositorio.getInstance();
        Livro livro = livroRepositorio.buscarPorId(livroID);
        if (livro == null) {
            throw new IllegalArgumentException("Livro não encontrado");
        }

        if (livro.getQuantidadeDisponivel() > 0) {
            
            Emprestimo emprestimo = new Emprestimo();

            emprestimo.setUsuario(usuario);
            emprestimo.setLivro(livro);
            emprestimo.setSituacao(SituacaoEmprestimo.EMPRESTADO);
            emprestimo.setDataRetirada(LocalDate.now().toString());
            emprestimo.setDataDevolucao(LocalDate.now().plusDays(7).toString());

            emprestimoRepositorio.salvar(emprestimo);
            livro.realizarEmprestimo();
            livroRepositorio.salvar(livro);

            return emprestimo;
        } else {
            throw new IllegalArgumentException("Livro não possui quantidade disponível para empréstimo");
        }
    }

    public void registrarDevolucao(Long emprestimoID) {
        EmprestimoRepositorio emprestimoRepositorio = EmprestimoRepositorio.getInstance();
        Emprestimo emprestimo = emprestimoRepositorio.buscarPorId(emprestimoID);
        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não encontrado");
        }

        if (emprestimo.getSituacao() == SituacaoEmprestimo.EMPRESTADO) {
            emprestimo.setSituacao(SituacaoEmprestimo.DEVOLVIDO);
            emprestimo.setDataDevolucao(LocalDate.now().toString());
            
            Livro livro = emprestimo.getLivro();
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
            
            LivroRepositorio livroRepositorio = LivroRepositorio.getInstance();
            emprestimoRepositorio.salvar(emprestimo);
            livroRepositorio.salvar(livro);
        } else {
            throw new IllegalStateException("Empréstimo já foi devolvido");
        }
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        EmprestimoRepositorio emprestimoRepositorio = EmprestimoRepositorio.getInstance();
        List<Emprestimo> emprestimosAtivos = emprestimoRepositorio.listarTodos().values().stream()
                .filter(e -> e.getSituacao() == SituacaoEmprestimo.EMPRESTADO)
                .toList();
        return emprestimosAtivos;
    }

    //Implementar futuramente a validação de Atrasos
    public List<Emprestimo> verificarAtrasos(){
        atualizarAtrasos();
        EmprestimoRepositorio emprestimoRepositorio = EmprestimoRepositorio.getInstance();
        List<Emprestimo> emprestimosAtrasados = emprestimoRepositorio.listarTodos().values().stream()
                .filter(e -> e.getSituacao() == SituacaoEmprestimo.ATRASADO)
                .toList();
        return emprestimosAtrasados;
    }

    public List<Emprestimo> atualizarAtrasos(){
        EmprestimoRepositorio emprestimoRepositorio = EmprestimoRepositorio.getInstance();
        List<Emprestimo> emprestimosAtrasados = emprestimoRepositorio.listarTodos().values().stream()
                .filter(e -> e.getSituacao() == SituacaoEmprestimo.EMPRESTADO && LocalDate.parse(e.getDataDevolucao()).isBefore(LocalDate.now()))
                .toList();

        emprestimosAtrasados.forEach(e -> e.setSituacao(SituacaoEmprestimo.ATRASADO));
        return emprestimosAtrasados;
    }
}
