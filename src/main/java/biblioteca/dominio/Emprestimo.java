package biblioteca.dominio;

import java.time.LocalDate;

/**
 * Entidade de domínio que representa um Empréstimo de livro.
 */
public class Emprestimo {
    private Long id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataRetirada;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataRealDevolucao;
    private SituacaoEmprestimo situacao;

    public Emprestimo(Long id, Livro livro, Usuario usuario, LocalDate dataRetirada,
                      LocalDate dataPrevistaDevolucao) {
        this.id = id;
        this.livro = livro;
        this.usuario = usuario;
        this.dataRetirada = dataRetirada;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.situacao = SituacaoEmprestimo.ATIVO;
        this.dataRealDevolucao = null;
    }

    /**
     * Registra a devolução do livro e atualiza a situação do empréstimo.
     */
    public void registrarDevolucao(LocalDate dataRealDevolucao) {
        this.dataRealDevolucao = dataRealDevolucao;
        
        // Verifica se houve atraso
        if (dataRealDevolucao.isAfter(dataPrevistaDevolucao)) {
            this.situacao = SituacaoEmprestimo.ATRASADO;
        } else {
            this.situacao = SituacaoEmprestimo.DEVOLVIDO;
        }
    }

    /**
     * Verifica se o empréstimo está em atraso comparando com a data atual.
     */
    public boolean estaAtrasado() {
        return situacao == SituacaoEmprestimo.ATIVO && 
               LocalDate.now().isAfter(dataPrevistaDevolucao);
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Livro getLivro() {
        return livro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataRealDevolucao() {
        return dataRealDevolucao;
    }

    public SituacaoEmprestimo getSituacao() {
        return situacao;
    }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", livro=" + livro.getTitulo() +
                ", usuario=" + usuario.getNome() +
                ", dataRetirada=" + dataRetirada +
                ", dataPrevistaDevolucao=" + dataPrevistaDevolucao +
                ", situacao=" + situacao +
                '}';
    }
}
