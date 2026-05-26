package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;
import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.SituacaoEmprestimo;
import java.io.*;
import java.util.*;

public class EmprestimoRepositorioCsv implements PortaEmprestimoRepositorio {
    private final String caminhoArquivo;
    private Long proximoId = 1L;

    public EmprestimoRepositorioCsv(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        carregarDoArquivo();
    }

    @Override
    public void salvar(Emprestimo emprestimo) {
        if (emprestimo.getId() == null) {
            emprestimo.setId(proximoId++);
        }
        atualizarArquivo();
    }

    @Override
    public Optional<Emprestimo> buscarPorId(Long id) {
        return listarTodos().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Emprestimo> listarTodos() {
        List<Emprestimo> emprestimos = new ArrayList<>();
        File arquivo = new File(caminhoArquivo);
        
        if (!arquivo.exists()) {
            return emprestimos;
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
                if (campos.length >= 6) {
                    Emprestimo emprestimo = new Emprestimo();
                    emprestimo.setId(Long.parseLong(campos[0]));
                    emprestimo.setDataRetirada(campos[4]);
                    emprestimo.setDataDevolucao(campos[5]);
                    emprestimo.setSituacao(SituacaoEmprestimo.valueOf(campos[3]));
                    emprestimos.add(emprestimo);
                    
                    if (emprestimo.getId() >= proximoId) {
                        proximoId = emprestimo.getId() + 1;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emprestimos;
    }

    @Override
    public void remover(Long id) {
        atualizarArquivo();
    }

    private void atualizarArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            writer.write("id,usuarioId,livroId,situacao,dataRetirada,dataDevolucao\n");
            for (Emprestimo emprestimo : listarTodos()) {
                writer.write(String.format("%d,%d,%d,%s,%s,%s\n",
                        emprestimo.getId(),
                        emprestimo.getUsuario().getId(),
                        emprestimo.getLivro().getId(),
                        emprestimo.getSituacao(),
                        emprestimo.getDataRetirada(),
                        emprestimo.getDataDevolucao()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarDoArquivo() {
        listarTodos();
    }
}
