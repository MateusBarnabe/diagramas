package biblioteca.porta.saida;

import java.util.List;
import java.util.Optional;

import biblioteca.dominio.Emprestimo;

public interface PortaEmprestimoRepositorio {
    
    void salvar(Emprestimo emprestimo);
    Optional<Emprestimo> buscarPorId(Long id);
    List<Emprestimo> listarTodos();
    void remover(Long id);

}
