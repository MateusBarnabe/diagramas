package biblioteca.aplicacao;

import biblioteca.dominio.Livro;
import biblioteca.infraestrutura.adaptador.LivroRepositorio;
import java.util.List;
import java.util.Optional;

/**
 * Serviço de aplicação para gerenciar livros.
 */
public class LivroServico {
    private final LivroRepositorio livroRepositorio;

    public LivroServico(LivroRepositorio livroRepositorio) {
        this.livroRepositorio = livroRepositorio;
    }

    public void cadastrarLivro(Long id, String titulo, String autor, String isbn, int quantidade) {
        Livro livro = new Livro(id, titulo, autor, isbn, quantidade);
        livroRepositorio.salvar(livro);
    }

    public Optional<Livro> buscarLivro(Long id) {
        return livroRepositorio.buscarPorId(id);
    }

    public List<Livro> listarLivros() {
        return livroRepositorio.listarTodos();
    }

    public void atualizarLivro(Long id, String titulo, String autor, int quantidade) {
        Optional<Livro> livro = livroRepositorio.buscarPorId(id);
        if (livro.isPresent()) {
            livro.get().setTitulo(titulo);
            livro.get().setAutor(autor);
            livro.get().setQuantidadeDisponivel(quantidade);
        }
    }
}
