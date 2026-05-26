package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;
import biblioteca.dominio.Emprestimo;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EmprestimoRepositorioMemoria implements PortaEmprestimoRepositorio {
    private HashMap<Long, Emprestimo> emprestimos = new HashMap<>();
    private Long proximoId = 1L;

    @Override
    public void salvar(Emprestimo emprestimo) {
        if (emprestimo.getId() == null) {
            emprestimo.setId(proximoId++);
        }
        emprestimos.put(emprestimo.getId(), emprestimo);
    }

    @Override
    public Optional<Emprestimo> buscarPorId(Long id) {
        return Optional.ofNullable(emprestimos.get(id));
    }

    @Override
    public List<Emprestimo> listarTodos() {
        return new java.util.ArrayList<>(emprestimos.values());
    }

    @Override
    public void remover(Long id) {
        emprestimos.remove(id);
    }
}
