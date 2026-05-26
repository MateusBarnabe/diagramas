package biblioteca.infraestrutura.evento;

import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;
import biblioteca.dominio.Livro;
import biblioteca.dominio.Usuario;

public class ServicoDeNotificacao {
    private final PortaLivroRepositorio livroRepo;
    private final PortaUsuarioRepositorio usuarioRepo;

    public ServicoDeNotificacao(PortaLivroRepositorio livroRepo, PortaUsuarioRepositorio usuarioRepo) {
        this.livroRepo = livroRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public void procesarEmprestimoRealizado(EmprestimoRealizadoEvento evento) {
        Usuario usuario = usuarioRepo.buscarPorId(evento.usuarioId()).orElse(null);
        Livro livro = livroRepo.buscarPorId(evento.livroId()).orElse(null);

        if (usuario != null && livro != null) {
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║        [NOTIFICAÇÃO AUTOMÁTICA DE EMPRÉSTIMO]         ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println("✓ Usuário: " + usuario.getNome());
            System.out.println("✓ Email: " + usuario.getEmail());
            System.out.println("✓ Livro: " + livro.getTitulo());
            System.out.println("✓ Data de Retirada: " + evento.dataRetirada());
            System.out.println("✓ Lembre-se de devolver em 7 dias!");
            System.out.println();
        }
    }

    public void procesarDevolucaoRegistrada(DevolucaoRegistradaEvento evento) {
        String status = evento.comAtraso() ? "⚠️  COM ATRASO" : "✓ NO PRAZO";
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║        [NOTIFICAÇÃO DE DEVOLUÇÃO - " + status + "]      ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("Data da Devolução: " + evento.dataDevolucao());
        System.out.println();
    }
}
