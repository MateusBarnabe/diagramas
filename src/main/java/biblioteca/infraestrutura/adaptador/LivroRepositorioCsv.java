package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Livro;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador Stage 2 - Implementação em CSV da porta de repositório de Livros.
 * Persiste dados em arquivo CSV sem alterar a lógica de negócio.
 */
public class LivroRepositorioCsv implements PortaLivroRepositorio {
    private final String caminhoArquivo;
    private static final String SEPARADOR = ",";
    private static final String CABECALHO = "id,titulo,autor,isbn,quantidadeDisponivel";

    public LivroRepositorioCsv(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        inicializarArquivo();
    }

    private void inicializarArquivo() {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
                writer.write(CABECALHO);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Erro ao criar arquivo CSV: " + e.getMessage());
            }
        }
    }

    @Override
    public void salvar(Livro livro) {
        // Lê todos os livros, remove o livro se já existir, adiciona o novo e salva
        List<String> linhas = lerTodasAsLinhas();
        linhas.removeIf(linha -> linha.startsWith(livro.getId() + ","));
        
        // Adiciona a nova linha
        String novaLinha = livro.getId() + SEPARADOR + 
                          livro.getTitulo() + SEPARADOR + 
                          livro.getAutor() + SEPARADOR + 
                          livro.getIsbn() + SEPARADOR + 
                          livro.getQuantidadeDisponivel();
        linhas.add(novaLinha);
        
        escreverLinhas(linhas);
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean primeiraLinha = true;
            
            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue; // Pula cabeçalho
                }
                
                String[] partes = linha.split(SEPARADOR);
                if (partes.length >= 5 && Long.parseLong(partes[0]) == id) {
                    Livro livro = new Livro(
                        Long.parseLong(partes[0]),
                        partes[1],
                        partes[2],
                        partes[3],
                        Integer.parseInt(partes[4])
                    );
                    return Optional.of(livro);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo CSV: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Livro> listarTodos() {
        List<Livro> livros = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean primeiraLinha = true;
            
            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue; // Pula cabeçalho
                }
                
                String[] partes = linha.split(SEPARADOR);
                if (partes.length >= 5) {
                    Livro livro = new Livro(
                        Long.parseLong(partes[0]),
                        partes[1],
                        partes[2],
                        partes[3],
                        Integer.parseInt(partes[4])
                    );
                    livros.add(livro);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo CSV: " + e.getMessage());
        }
        return livros;
    }

    @Override
    public void remover(Long id) {
        List<String> linhas = lerTodasAsLinhas();
        linhas.removeIf(linha -> linha.startsWith(id + ","));
        escreverLinhas(linhas);
    }

    private List<String> lerTodasAsLinhas() {
        List<String> linhas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo CSV: " + e.getMessage());
        }
        return linhas;
    }

    private void escreverLinhas(List<String> linhas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivo CSV: " + e.getMessage());
        }
    }
}
