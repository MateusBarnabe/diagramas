package biblioteca.porta.entrada;

import java.util.List;

import biblioteca.dominio.Emprestimo;

public interface PortaEmprestimo {
    Emprestimo realizarEmprestimo(Long usuarioId, Long livroId);
    void registrarDevolucao(Long emprestimoId);
    List<Emprestimo> listarEmprestimosAtivos();
    List<Emprestimo> verificarAtrasos();
}

