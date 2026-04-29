package biblioteca.aplicacao;

import biblioteca.dominio.Usuario;
import biblioteca.dominio.SituacaoUsuario;
import biblioteca.infraestrutura.adaptador.UsuarioRepositorio;
import java.util.List;
import java.util.Optional;

/**
 * Serviço de aplicação para gerenciar usuários.
 */
public class UsuarioServico {
    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioServico(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public void cadastrarUsuario(Long id, String nome, String email) {
        Usuario usuario = new Usuario(id, nome, email, SituacaoUsuario.ATIVO);
        usuarioRepositorio.salvar(usuario);
    }

    public Optional<Usuario> buscarUsuario(Long id) {
        return usuarioRepositorio.buscarPorId(id);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.listarTodos();
    }

    public void suspenderUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepositorio.buscarPorId(id);
        if (usuario.isPresent()) {
            usuario.get().setSituacao(SituacaoUsuario.SUSPENSO);
        }
    }

    public void reativarUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepositorio.buscarPorId(id);
        if (usuario.isPresent()) {
            usuario.get().setSituacao(SituacaoUsuario.ATIVO);
        }
    }
}
