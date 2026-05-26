package biblioteca.aplicacao;

import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.SituacaoUsuario;

public class UsuarioServicoHexagonal {
    private final PortaUsuarioRepositorio usuarioRepo;

    public UsuarioServicoHexagonal(PortaUsuarioRepositorio usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public void registrarUsuario(String nome, String email) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSituacao(SituacaoUsuario.ATIVO);
        usuarioRepo.salvar(usuario);
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepo.buscarPorId(id).orElse(null);
    }
}
