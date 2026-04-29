package biblioteca.dominio.evento;

import java.time.LocalDate;

/**
 * Evento de domínio que representa uma devolução registrada.
 * Publicado quando um empréstimo é devolvido.
 */
public record DevolucaoRegistradaEvento(
    Long emprestimoId,
    LocalDate dataDevolucao,
    boolean comAtraso,
    String nomeUsuario,
    String emailUsuario,
    String tituloLivro
) {
}
