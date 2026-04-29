package biblioteca.dominio.porta.saida;

import biblioteca.dominio.Livro;
import java.util.List;
import java.util.Optional;

/**
 * Porta de saída (Output Port) - Abstração para repositório de Livros.
 * Qualquer implementação deve ser substituível sem alterar o domínio.
 */
public interface PortaLivroRepositorio {
    void salvar(Livro livro);
    Optional<Livro> buscarPorId(Long id);
    List<Livro> listarTodos();
    void remover(Long id);
}
