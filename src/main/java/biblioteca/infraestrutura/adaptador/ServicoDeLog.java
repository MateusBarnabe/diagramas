package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Handler Stage 3 - Serviço que registra eventos em arquivo de log.
 * Consumidor independente de eventos, totalmente desacoplado do publicador.
 */
public class ServicoDeLog {
    private static final String ARQUIVO_LOG = "biblioteca.log";
    private static final DateTimeFormatter FORMATO_DATA_HORA = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Consome evento de empréstimo e registra no log.
     */
    public void aoEmprestimoCriado(EmprestimoRealizadoEvento evento) {
        String mensagem = String.format(
            "[%s] EMPRÉSTIMO REALIZADO - ID: %d, Usuário: %s, Livro: %s, Data Devolução: %s",
            LocalDateTime.now().format(FORMATO_DATA_HORA),
            evento.emprestimoId(),
            evento.nomeUsuario(),
            evento.tituloLivro(),
            evento.dataPrevistaDevolucao()
        );
        registrarNoLog(mensagem);
    }

    /**
     * Consome evento de devolução e registra no log.
     */
    public void aoDevolucoRegistrada(DevolucaoRegistradaEvento evento) {
        String status = evento.comAtraso() ? "COM ATRASO" : "NO PRAZO";
        String mensagem = String.format(
            "[%s] DEVOLUÇÃO REGISTRADA - ID: %d, Usuário: %s, Livro: %s, Status: %s",
            LocalDateTime.now().format(FORMATO_DATA_HORA),
            evento.emprestimoId(),
            evento.nomeUsuario(),
            evento.tituloLivro(),
            status
        );
        registrarNoLog(mensagem);
    }

    private void registrarNoLog(String mensagem) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(ARQUIVO_LOG, true))) {
            writer.write(mensagem);
            writer.newLine();
            System.out.println("[LOG] " + mensagem);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo de log: " + e.getMessage());
        }
    }
}
