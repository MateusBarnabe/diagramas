package biblioteca.dominio.porta.saida;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Usuario;

/**
 * Porta de saída (Output Port) - Abstração para notificações.
 * Permite enviar notificações através de diferentes canais sem acoplamento.
 */
public interface PortaNotificacao {
    void notificarAtraso(Usuario usuario, Emprestimo emprestimo);
    void notificarEmprestimo(Usuario usuario, String mensagem);
}
