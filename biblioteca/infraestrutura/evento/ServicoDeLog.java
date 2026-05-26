package biblioteca.infraestrutura.evento;

import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServicoDeLog {
    private final String caminhoArquivo;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ServicoDeLog(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public void registrarEmprestimoRealizado(EmprestimoRealizadoEvento evento) {
        String mensagem = String.format("[%s] EMPRÉSTIMO REALIZADO - ID: %d, Usuário: %d, Livro: %d, Data: %s",
                LocalDateTime.now().format(formatter),
                evento.emprestimoId(),
                evento.usuarioId(),
                evento.livroId(),
                evento.dataRetirada());
        registrarNoArquivo(mensagem);
    }

    public void registrarDevolucaoRegistrada(DevolucaoRegistradaEvento evento) {
        String statusAtraso = evento.comAtraso() ? "COM ATRASO" : "NO PRAZO";
        String mensagem = String.format("[%s] DEVOLUÇÃO REGISTRADA - ID: %d, Data: %s, Status: %s",
                LocalDateTime.now().format(formatter),
                evento.emprestimoId(),
                evento.dataDevolucao(),
                statusAtraso);
        registrarNoArquivo(mensagem);
    }

    private void registrarNoArquivo(String mensagem) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            writer.write(mensagem);
            writer.newLine();
            System.out.println("📝 Log registrado: " + mensagem);
        } catch (IOException e) {
            System.err.println("Erro ao registrar log: " + e.getMessage());
        }
    }
}
