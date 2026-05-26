package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.SituacaoUsuario;
import java.io.*;
import java.util.*;

public class UsuarioRepositorioCsv implements PortaUsuarioRepositorio {
    private final String caminhoArquivo;
    private Long proximoId = 1L;

    public UsuarioRepositorioCsv(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        carregarDoArquivo();
    }

    @Override
    public void salvar(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(proximoId++);
        }
        atualizarArquivo();
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return listarTodos().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        File arquivo = new File(caminhoArquivo);
        
        if (!arquivo.exists()) {
            return usuarios;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }
                
                String[] campos = linha.split(",");
                if (campos.length >= 4) {
                    Usuario usuario = new Usuario();
                    usuario.setId(Long.parseLong(campos[0]));
                    usuario.setNome(campos[1]);
                    usuario.setEmail(campos[2]);
                    usuario.setSituacao(SituacaoUsuario.valueOf(campos[3]));
                    usuarios.add(usuario);
                    
                    if (usuario.getId() >= proximoId) {
                        proximoId = usuario.getId() + 1;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    @Override
    public void remover(Long id) {
        atualizarArquivo();
    }

    private void atualizarArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            writer.write("id,nome,email,situacao\n");
            for (Usuario usuario : listarTodos()) {
                writer.write(String.format("%d,%s,%s,%s\n",
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getSituacao()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarDoArquivo() {
        listarTodos();
    }
}
