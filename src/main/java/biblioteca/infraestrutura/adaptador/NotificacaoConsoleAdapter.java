package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.porta.saida.PortaNotificacao;

/**
 * Adaptador Stage 2 - Implementação de notificação via console.
 * Implementa a porta PortaNotificacao.
 * Pode ser substituído por adaptador de email, SMS, etc.
 */
public class NotificacaoConsoleAdapter implements PortaNotificacao {
    
    @Override
    public void notificarAtraso(Usuario usuario, Emprestimo emprestimo) {
        System.out.println("[NOTIFICAÇÃO] Para " + usuario.getNome() + " (" + usuario.getEmail() + "): " +
                "Empréstimo em atraso: " + emprestimo.getLivro().getTitulo() + 
                ". Data limite era: " + emprestimo.getDataPrevistaDevolucao());
    }

    @Override
    public void notificarEmprestimo(Usuario usuario, String mensagem) {
        System.out.println("[NOTIFICAÇÃO] Para " + usuario.getNome() + " (" + usuario.getEmail() + "): " + mensagem);
    }
}
