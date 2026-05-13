package biblioteca.infraestrutura;

import java.util.HashMap;

import biblioteca.dominio.Usuario;

public class UsuarioRepositorio {
    private HashMap<Long, Usuario> usuarios = new HashMap<>();
    private Long proximoId = 1L;

    public void salvar(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(proximoId++);
        }
        usuarios.put(usuario.getId(), usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarios.get(id);
    }

    public HashMap<Long, Usuario> listarTodos() {
        return usuarios;
    }

    public void deletar(Long id) {
        usuarios.remove(id);
    }
}
