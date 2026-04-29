package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Usuario;

/**
 * Adaptador de notificação que envia notificações via console.
 * Stage 1 - será convertido para implementar uma porta na Stage 2.
 */
public class NotificacaoConsole {
    
    public void notificarAtraso(Usuario usuario, String mensagem) {
        System.out.println("[NOTIFICAÇÃO] Para " + usuario.getNome() + " (" + usuario.getEmail() + "): " + mensagem);
    }

    public void notificarEmprestimo(Usuario usuario, String mensagem) {
        System.out.println("[NOTIFICAÇÃO] Para " + usuario.getNome() + " (" + usuario.getEmail() + "): " + mensagem);
    }
}
