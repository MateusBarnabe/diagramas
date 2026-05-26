package biblioteca.porta.saida;

import java.util.List;
import java.util.Optional;

import biblioteca.dominio.Livro;

public interface PortaLivroRepositorio {
    
    void salvar(Livro livro);
    Optional<Livro> buscarPorId(Long id);
    List<Livro> listarTodos();
    void remover(Long id);

}
