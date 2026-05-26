package biblioteca.aplicacao;

import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.Livro;

public class LivroServicoHexagonal {
    private final PortaLivroRepositorio livroRepo;

    public LivroServicoHexagonal(PortaLivroRepositorio livroRepo) {
        this.livroRepo = livroRepo;
    }

    public void adicionarLivro(String titulo, String autor, String isbn, int quantidade) {
        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setIsbn(isbn);
        livro.setQuantidadeDisponivel(quantidade);
        livroRepo.salvar(livro);
    }

    public Livro buscarLivroPorId(Long id) {
        return livroRepo.buscarPorId(id).orElse(null);
    }
}
