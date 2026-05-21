package biblioteca.aplicacao;

import biblioteca.dominio.SituacaoUsuario;
import biblioteca.dominio.Usuario;
import biblioteca.infraestrutura.UsuarioRepositorio;

public class UsuarioServico {
    public void registrarUsuario(String nome, String email) {
        UsuarioRepositorio usuarioRepositorio = UsuarioRepositorio.getInstance();
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSituacao(SituacaoUsuario.ATIVO);
        usuarioRepositorio.salvar(usuario);
    }
}
