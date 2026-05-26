package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.Livro;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class LivroRepositorioMemoria implements PortaLivroRepositorio {
    private HashMap<Long, Livro> livros = new HashMap<>();
    private Long proximoId = 1L;

    @Override
    public void salvar(Livro livro) {
        if (livro.getId() == null) {
            livro.setId(proximoId++);
        }
        livros.put(livro.getId(), livro);
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return Optional.ofNullable(livros.get(id));
    }

    @Override
    public List<Livro> listarTodos() {
        return new java.util.ArrayList<>(livros.values());
    }

    @Override
    public void remover(Long id) {
        livros.remove(id);
    }
}
