package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;
import biblioteca.dominio.Usuario;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositorioMemoria implements PortaUsuarioRepositorio {
    private HashMap<Long, Usuario> usuarios = new HashMap<>();
    private Long proximoId = 1L;

    @Override
    public void salvar(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(proximoId++);
        }
        usuarios.put(usuario.getId(), usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public List<Usuario> listarTodos() {
        return new java.util.ArrayList<>(usuarios.values());
    }

    @Override
    public void remover(Long id) {
        usuarios.remove(id);
    }
}
