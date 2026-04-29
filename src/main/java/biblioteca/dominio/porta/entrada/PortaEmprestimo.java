package biblioteca.dominio.porta.entrada;

import biblioteca.dominio.Emprestimo;
import java.util.List;

/**
 * Porta de entrada (Input Port) - Interface de caso de uso.
 * Define os contratos públicos do serviço de empréstimos.
 */
public interface PortaEmprestimo {
    Emprestimo realizarEmprestimo(Long usuarioId, Long livroId);
    void registrarDevolucao(Long emprestimoId);
    List<Emprestimo> listarEmprestimosAtivos();
    List<Emprestimo> verificarAtrasos();
}
