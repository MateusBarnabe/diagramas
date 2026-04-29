package biblioteca.dominio;

/**
 * Entidade de domínio que representa um Livro no sistema de biblioteca.
 * Encapsula as regras de negócio relacionadas a empréstimos de livros.
 */
public class Livro {
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private int quantidadeDisponivel;

    public Livro(Long id, String titulo, String autor, String isbn, int quantidadeDisponivel) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    /**
     * Realiza o empréstimo de um livro se houver quantidade disponível.
     * Regra de negócio: verifica disponibilidade antes de decrementar o estoque.
     * 
     * @throws IllegalArgumentException se não houver cópias disponíveis
     */
    public void realizarEmprestimo() {
        if (quantidadeDisponivel <= 0) {
            throw new IllegalArgumentException("Livro '" + titulo + "' não tem cópias disponíveis");
        }
        quantidadeDisponivel--;
    }

    /**
     * Registra a devolução de uma cópia do livro.
     */
    public void registrarDevolucao() {
        quantidadeDisponivel++;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", isbn='" + isbn + '\'' +
                ", quantidadeDisponivel=" + quantidadeDisponivel +
                '}';
    }
}
