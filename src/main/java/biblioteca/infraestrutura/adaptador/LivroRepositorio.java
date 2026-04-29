package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Livro;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositório em memória para Livros.
 * Implementação Stage 1 - será convertida para interface na Stage 2.
 */
public class LivroRepositorio {
    private final Map<Long, Livro> livros = new HashMap<>();

    public void salvar(Livro livro) {
        livros.put(livro.getId(), livro);
    }

    public Optional<Livro> buscarPorId(Long id) {
        return Optional.ofNullable(livros.get(id));
    }

    public List<Livro> listarTodos() {
        return livros.values().stream().collect(Collectors.toList());
    }

    public void remover(Long id) {
        livros.remove(id);
    }
}
