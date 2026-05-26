package biblioteca.porta.saida;

import java.util.List;
import java.util.Optional;

import biblioteca.dominio.Usuario;

public interface PortaUsuarioRepositorio {
    
    void salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    List<Usuario> listarTodos();
    void remover(Long id);

}
