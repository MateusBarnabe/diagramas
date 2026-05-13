package biblioteca.dominio;


public class Emprestimo {
    private Long id;
    private Usuario usuario;
    private Livro livro;
    private SituacaoEmprestimo situacao;
    private String dataRetirada;
    private String dataDevolucao;

    public Emprestimo() {
    }

    public Emprestimo(Long id, Usuario usuario, Livro livro, SituacaoEmprestimo situacao, String dataRetirada, String dataDevolucao) {
        this.id = id;
        this.usuario = usuario;
        this.livro = livro;
        this.situacao = situacao;
        this.dataRetirada = dataRetirada;
        this.dataDevolucao = dataDevolucao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public SituacaoEmprestimo getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoEmprestimo situacao) {
        this.situacao = situacao;
    }

    public String getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(String dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public String getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(String dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

}
