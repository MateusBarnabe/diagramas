package biblioteca.aplicacao;

import biblioteca.dominio.Livro;
import biblioteca.infraestrutura.LivroRepositorio;

public class LivroServico {
    
    public void adicionarLivro(String titulo, String autor, int quantidade) {
        LivroRepositorio livroRepositorio = LivroRepositorio.getInstance();
        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setQuantidadeDisponivel(quantidade);
        livroRepositorio.salvar(livro);
    }


}
