package biblioteca.dominio.evento;

import java.time.LocalDate;

/**
 * Evento de domínio que representa um empréstimo realizado.
 * Publicado quando um empréstimo é criado com sucesso.
 */
public record EmprestimoRealizadoEvento(
    Long emprestimoId,
    Long usuarioId,
    Long livroId,
    LocalDate dataRetirada,
    LocalDate dataPrevistaDevolucao,
    String nomeUsuario,
    String emailUsuario,
    String tituloLivro
) {
}
