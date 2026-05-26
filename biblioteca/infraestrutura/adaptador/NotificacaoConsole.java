package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.porta.saida.PortaNotificacao;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.Emprestimo;

public class NotificacaoConsole implements PortaNotificacao {

    @Override
    public void notificarEmprestimo(Usuario usuario, Emprestimo emprestimo) {
        System.out.println("=== NOTIFICAÇÃO DE EMPRÉSTIMO ===");
        System.out.println("Usuário: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Livro: " + emprestimo.getLivro().getTitulo());
        System.out.println("Data de Retirada: " + emprestimo.getDataRetirada());
        System.out.println("Data Prevista de Devolução: " + emprestimo.getDataDevolucao());
        System.out.println("==================================\n");
    }

    @Override
    public void notificarDevolucao(Usuario usuario, Emprestimo emprestimo) {
        System.out.println("=== NOTIFICAÇÃO DE DEVOLUÇÃO ===");
        System.out.println("Usuário: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Livro: " + emprestimo.getLivro().getTitulo());
        System.out.println("Data de Devolução: " + emprestimo.getDataDevolucao());
        System.out.println("==================================\n");
    }

    @Override
    public void notificarAtraso(Usuario usuario, Emprestimo emprestimo) {
        System.out.println("=== NOTIFICAÇÃO DE ATRASO ===");
        System.out.println("ALERTA: Empréstimo em atraso!");
        System.out.println("Usuário: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Livro: " + emprestimo.getLivro().getTitulo());
        System.out.println("Data Prevista de Devolução: " + emprestimo.getDataDevolucao());
        System.out.println("==============================\n");
    }
}
