package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.evento.EmprestimoRealizadoEvento;

/**
 * Handler Stage 3 - Serviço que envia notificações para usuários.
 * Consumidor independente de eventos, totalmente desacoplado do publicador.
 */
public class ServicoDeNotificacao {

    /**
     * Consome evento de empréstimo e notifica o usuário.
     */
    public void aoEmprestimoCriado(EmprestimoRealizadoEvento evento) {
        String mensagem = String.format(
            "Caro(a) %s,\n\n" +
            "Seu empréstimo foi realizado com sucesso!\n" +
            "Livro: %s\n" +
            "Data de retirada: %s\n" +
            "Data prevista de devolução: %s\n\n" +
            "Obrigado por usar nossa biblioteca!",
            evento.nomeUsuario(),
            evento.tituloLivro(),
            evento.dataRetirada(),
            evento.dataPrevistaDevolucao()
        );
        
        System.out.println("[NOTIFICAÇÃO DE EMPRÉSTIMO] Enviado para " + evento.emailUsuario() + ":");
        System.out.println(mensagem);
        System.out.println();
    }
}
