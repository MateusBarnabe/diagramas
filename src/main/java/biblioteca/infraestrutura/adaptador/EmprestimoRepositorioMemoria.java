package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador Stage 2 - Implementação em memória da porta de repositório de Empréstimos.
 * Pode ser substituído por outras implementações sem alterar o domínio.
 */
public class EmprestimoRepositorioMemoria implements PortaEmprestimoRepositorio {
    private final Map<Long, Emprestimo> emprestimos = new HashMap<>();

    @Override
    public void salvar(Emprestimo emprestimo) {
        emprestimos.put(emprestimo.getId(), emprestimo);
    }

    @Override
    public Optional<Emprestimo> buscarPorId(Long id) {
        return Optional.ofNullable(emprestimos.get(id));
    }

    @Override
    public List<Emprestimo> listarTodos() {
        return emprestimos.values().stream().collect(Collectors.toList());
    }

    @Override
    public void remover(Long id) {
        emprestimos.remove(id);
    }
}
