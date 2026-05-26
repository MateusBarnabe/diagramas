package biblioteca.dominio.porta.saida;

import biblioteca.dominio.Usuario;
import biblioteca.dominio.Emprestimo;

public interface PortaNotificacao {
    void notificarEmprestimo(Usuario usuario, Emprestimo emprestimo);
    void notificarDevolucao(Usuario usuario, Emprestimo emprestimo);
    void notificarAtraso(Usuario usuario, Emprestimo emprestimo);
}
