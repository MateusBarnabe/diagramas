package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Usuario;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador Stage 2 - Implementação em memória da porta de repositório de Usuários.
 * Pode ser substituído por outras implementações sem alterar o domínio.
 */
public class UsuarioRepositorioMemoria implements PortaUsuarioRepositorio {
    private final Map<Long, Usuario> usuarios = new HashMap<>();

    @Override
    public void salvar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarios.values().stream().collect(Collectors.toList());
    }

    @Override
    public void remover(Long id) {
        usuarios.remove(id);
    }
}
