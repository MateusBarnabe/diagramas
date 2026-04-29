package biblioteca.dominio.porta.saida;

import biblioteca.dominio.Emprestimo;
import java.util.List;
import java.util.Optional;

/**
 * Porta de saída (Output Port) - Abstração para repositório de Empréstimos.
 * Qualquer implementação deve ser substituível sem alterar o domínio.
 */
public interface PortaEmprestimoRepositorio {
    void salvar(Emprestimo emprestimo);
    Optional<Emprestimo> buscarPorId(Long id);
    List<Emprestimo> listarTodos();
    void remover(Long id);
}
