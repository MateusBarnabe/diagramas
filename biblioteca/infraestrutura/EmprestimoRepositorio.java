package biblioteca.infraestrutura;

import java.util.HashMap;

import biblioteca.dominio.Emprestimo;

public class EmprestimoRepositorio {
    private HashMap<Long, Emprestimo> emprestimos = new HashMap<>();
    private Long proximoId = 1L;

    public void salvar(Emprestimo emprestimo) {
        if (emprestimo.getId() == null) {
            emprestimo.setId(proximoId++);
        }
        emprestimos.put(emprestimo.getId(), emprestimo);
    }

    public Emprestimo buscarPorId(Long id) {
        return emprestimos.get(id);
    }
    public HashMap<Long, Emprestimo> listarTodos() {
        return emprestimos;
    }

    public void deletar(Long id) {
        emprestimos.remove(id);
    }
}
