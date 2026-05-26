package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.Livro;
import java.io.*;
import java.util.*;

public class LivroRepositorioCsv implements PortaLivroRepositorio {
    private final String caminhoArquivo;
    private Long proximoId = 1L;

    public LivroRepositorioCsv(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        carregarDoArquivo();
    }

    @Override
    public void salvar(Livro livro) {
        if (livro.getId() == null) {
            livro.setId(proximoId++);
        }
        atualizarArquivo();
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return listarTodos().stream()
                .filter(l -> l.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Livro> listarTodos() {
        List<Livro> livros = new ArrayList<>();
        File arquivo = new File(caminhoArquivo);
        
        if (!arquivo.exists()) {
            return livros;
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
                if (campos.length >= 5) {
                    Livro livro = new Livro();
                    livro.setId(Long.parseLong(campos[0]));
                    livro.setTitulo(campos[1]);
                    livro.setAutor(campos[2]);
                    livro.setIsbn(campos[3]);
                    livro.setQuantidadeDisponivel(Integer.parseInt(campos[4]));
                    livros.add(livro);
                    
                    if (livro.getId() >= proximoId) {
                        proximoId = livro.getId() + 1;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return livros;
    }

    @Override
    public void remover(Long id) {
        atualizarArquivo();
    }

    private void atualizarArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            writer.write("id,titulo,autor,isbn,quantidadeDisponivel\n");
            for (Livro livro : listarTodos()) {
                writer.write(String.format("%d,%s,%s,%s,%d\n",
                        livro.getId(),
                        livro.getTitulo(),
                        livro.getAutor(),
                        livro.getIsbn(),
                        livro.getQuantidadeDisponivel()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarDoArquivo() {
        listarTodos();
    }
}
