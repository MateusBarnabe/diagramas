package biblioteca.apresentacao;

import biblioteca.aplicacao.LivroServico;
import biblioteca.aplicacao.RepositorioServico;
import biblioteca.aplicacao.UsuarioServico;


public class Main {

	public static void main(String[] args) {
		//Cadastrar livro
		LivroServico livroServico = new LivroServico();
		livroServico.adicionarLivro("O Senhor dos Anéis", "J.R.R. Tolkien", 5);

		//Cadastrar usuário
		UsuarioServico usuarioServico = new UsuarioServico();
		usuarioServico.registrarUsuario("João Silva", "joao.silva@email.com");

		//Realizar empréstimo
		RepositorioServico emprestimoServico = new RepositorioServico();
		emprestimoServico.realizarEmprestimo(1L, 1L); // Supondo que o livro e o usuário tenham ID 1

		//Registrar devolução
		emprestimoServico.registrarDevolucao(1L); // Supondo que o empréstimo tenha ID 1

	}
}
        