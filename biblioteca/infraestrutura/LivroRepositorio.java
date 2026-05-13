package biblioteca.infraestrutura;

import java.util.HashMap;

import biblioteca.dominio.Livro;

public class LivroRepositorio {
    private HashMap<Long, Livro> livros = new HashMap<>();
    private Long proximoId = 1L;

    public void salvar(Livro livro) {
        if (livro.getId() == null) {
            livro.setId(proximoId++);
        }
        livros.put(livro.getId(), livro);
    }

    public Livro buscarPorId(Long id) {
        return livros.get(id);
    }

    public HashMap<Long, Livro> listarTodos() {
        return livros;
    }

    public void deletar(Long id) {
        livros.remove(id);
    }

}
