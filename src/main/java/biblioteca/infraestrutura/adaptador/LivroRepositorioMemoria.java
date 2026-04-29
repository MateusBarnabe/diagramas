package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Livro;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador Stage 2 - Implementação em memória da porta de repositório de Livros.
 * Pode ser substituído por LivroRepositorioCsv sem alterar o domínio.
 */
public class LivroRepositorioMemoria implements PortaLivroRepositorio {
    private final Map<Long, Livro> livros = new HashMap<>();

    @Override
    public void salvar(Livro livro) {
        livros.put(livro.getId(), livro);
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return Optional.ofNullable(livros.get(id));
    }

    @Override
    public List<Livro> listarTodos() {
        return livros.values().stream().collect(Collectors.toList());
    }

    @Override
    public void remover(Long id) {
        livros.remove(id);
    }
}
