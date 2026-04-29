package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Usuario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositório em memória para Usuários.
 * Implementação Stage 1 - será convertida para interface na Stage 2.
 */
public class UsuarioRepositorio {
    private final Map<Long, Usuario> usuarios = new HashMap<>();

    public void salvar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    public List<Usuario> listarTodos() {
        return usuarios.values().stream().collect(Collectors.toList());
    }

    public void remover(Long id) {
        usuarios.remove(id);
    }
}
