package application;

import javax.swing.*;
import java.security.SecureRandom;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import dao.AlunoDao;
import dao.ProfessorDao;
import entities.Aluno;
import entities.Nota;
import entities.Professor;

public class InterfaceVisual {
	private static Integer idProfessor;
	private static Integer idAluno;

	public static String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return BCrypt.gensalt(12);
	}

	public static String hashPassword(String senha, String salt) {
		return BCrypt.hashpw(senha, salt);
	}

	private static void exibirMensagem(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem);
	}

	private static void exibirDialogCadastrarProfessor() {
		JTextField nomeProfField = new JTextField();
		JTextField loginProfField = new JTextField();
		JTextField senhaProfField = new JTextField();

		Object[] mensagem = { "Nome:", nomeProfField, "Login:", loginProfField, "Senha:", senhaProfField };

		int resultado = JOptionPane.showConfirmDialog(null, mensagem, "Cadastrar Professor",
				JOptionPane.OK_CANCEL_OPTION);

		if (resultado == JOptionPane.OK_OPTION) {
			String nomeProf = nomeProfField.getText();
			String loginProf = loginProfField.getText();
			String senhaProf = senhaProfField.getText();
			String saltProf = generateSalt();
			String senhaProfHashed = hashPassword(senhaProf, saltProf);
			Professor prof = new Professor(nomeProf, loginProf, senhaProfHashed);
			boolean sucesso = ProfessorDao.cadastrarProfessor(prof);

			if (sucesso)
				exibirMensagem("Professor cadastrado com sucesso.");
		}
	}

	private static boolean exibirDialogLoginProfessor() {
		JTextField loginProfField = new JTextField();
		JTextField senhaProfField = new JTextField();

		Object[] mensagem = { "Login:", loginProfField, "Senha:", senhaProfField };

		int resultado = JOptionPane.showConfirmDialog(null, mensagem, "Login Professor", JOptionPane.OK_CANCEL_OPTION);

		if (resultado == JOptionPane.OK_OPTION) {
			String loginProf = loginProfField.getText();
			String senhaProf = senhaProfField.getText();
			boolean sucesso = ProfessorDao.loginProfessor(loginProf, senhaProf);
			idProfessor = ProfessorDao.buscarId(loginProf);
			if (sucesso) {
				exibirMensagem("Login bem-sucedido.");
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private static void exibirDialogCadastrarAluno() {
		JTextField nomeAlunoField = new JTextField();
		JTextField loginAlunoField = new JTextField();
		JTextField senhaAlunoField = new JTextField();

		Object[] mensagem = { "Nome:", nomeAlunoField, "Login:", loginAlunoField, "Senha:", senhaAlunoField };

		int resultado = JOptionPane.showConfirmDialog(null, mensagem, "Cadastrar Aluno", JOptionPane.OK_CANCEL_OPTION);

		if (resultado == JOptionPane.OK_OPTION) {
			String nomeAluno = nomeAlunoField.getText();
			String loginAluno = loginAlunoField.getText();
			String senhaAluno = senhaAlunoField.getText();
			String saltAluno = generateSalt();
			String senhaAlunoHashed = hashPassword(senhaAluno, saltAluno);
			Aluno aluno = new Aluno(nomeAluno, loginAluno, senhaAlunoHashed);
			boolean sucesso = AlunoDao.cadastrarAluno(aluno);

			if (sucesso)
				exibirMensagem("Aluno cadastrado com sucesso.");
		}
	}

	private static boolean exibirDialogLoginAluno() {
		JTextField loginAlunoField = new JTextField();
		JTextField senhaAlunoField = new JTextField();

		Object[] mensagem = { "Login:", loginAlunoField, "Senha:", senhaAlunoField };

		int resultado = JOptionPane.showConfirmDialog(null, mensagem, "Login Aluno", JOptionPane.OK_CANCEL_OPTION);

		if (resultado == JOptionPane.OK_OPTION) {
			String loginAluno = loginAlunoField.getText();
			String senhaAluno = senhaAlunoField.getText();
			boolean sucesso = AlunoDao.loginAluno(loginAluno, senhaAluno);
			idAluno = AlunoDao.buscarId(loginAluno);

			if (sucesso) {
				exibirMensagem("Login bem-sucedido.");
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private static void encerrarPrograma() {
		ProfessorDao.encerrarConexoes();
		System.exit(0);
	}

	private static void exibirListaAlunos() {
		List<Aluno> alunos = ProfessorDao.listarAlunos();
		if(alunos == null) {
			exibirMensagem("Ainda não existem alunos cadastrados!");
		} else {
			StringBuilder message = new StringBuilder("Lista de alunos:\n");
			for(Aluno aluno : alunos) {
				message.append(aluno.toString()).append("\n");
			}
			JOptionPane.showMessageDialog(null, message.toString(), "Lista de Alunos", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private static void exibirDialogCadastrarNota() {
		JTextField idAlunoField = new JTextField();
		JTextField disciplinaField = new JTextField();
		JTextField notaField = new JTextField();

		Object[] mensagem = { "Id aluno:", idAlunoField, "Disciplina:", disciplinaField, "Nota:", notaField };

		int resultado = JOptionPane.showConfirmDialog(null, mensagem, "Cadastrar nota", JOptionPane.OK_CANCEL_OPTION);

		if (resultado == JOptionPane.OK_OPTION) {
			int idAluno = Integer.parseInt(idAlunoField.getText());
			String disciplina = disciplinaField.getText();
			double nota = Double.parseDouble(notaField.getText());
			String nomeAluno = ProfessorDao.buscarNomeAluno(idAluno);
			Nota nota1 = new Nota(idAluno, nomeAluno, idProfessor, disciplina, nota);
			boolean sucesso = ProfessorDao.cadastrarNota(nota1);

			if (sucesso)
				exibirMensagem("Nota cadastrada com sucesso.");
		}
	}

	private static boolean exibirNotasParaProfessor(int idAluno) {
		List<Nota> notas = ProfessorDao.listarNotasAluno(idAluno);
		if(notas == null) {
			exibirMensagem("Este aluno ainda não tem notas cadastradas!");
			return false;
		} else {
			StringBuilder message = new StringBuilder("Notas aluno:\n");
			for(Nota nota : notas) {
				message.append(nota.toString()).append("\n");
			}
			JOptionPane.showMessageDialog(null, message.toString(), "Notas aluno", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
	}
	
	private static boolean exibirNotasParaAluno(int idAluno) {
		List<Nota> notas = AlunoDao.listarNotasAluno(idAluno);
		if(notas == null) {
			exibirMensagem("Você ainda não tem notas cadastradas!");
			return false;
		} else {
			StringBuilder message = new StringBuilder("Notas aluno:\n");
			for(Nota nota : notas) {
				message.append(nota.toString()).append("\n");
			}
			JOptionPane.showMessageDialog(null, message.toString(), "Notas aluno", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
	}

	private static boolean exibirDialogVisualizarNotas() {
		JTextField idAlunoField = new JTextField();

		Object[] mensagem = { "Id aluno:", idAlunoField };

		int resultado = JOptionPane.showConfirmDialog(null, mensagem, "Visualizar notas", JOptionPane.OK_CANCEL_OPTION);

		if (resultado == JOptionPane.OK_OPTION) {
			int idAluno = Integer.parseInt(idAlunoField.getText());
			boolean sucesso = exibirNotasParaProfessor(idAluno);

			if (sucesso) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private static void exibirDialogEditarNota() {
		JTextField idNotaField = new JTextField();
		JTextField novaNotaField = new JTextField();

		Object[] mensagem = { "Id nota:", idNotaField, "Nova nota:", novaNotaField };

		int resultado = JOptionPane.showConfirmDialog(null, mensagem, "Editar nota", JOptionPane.OK_CANCEL_OPTION);

		if (resultado == JOptionPane.OK_OPTION) {
			int idNota = Integer.parseInt(idNotaField.getText());
			double novaNota = Double.parseDouble(novaNotaField.getText());
			boolean sucesso = ProfessorDao.editarNota(idNota, novaNota);

			if (sucesso) {
				exibirMensagem("Nota alterada com sucesso.");
			} else {
				exibirMensagem("Nota não econtrada!");
			}
		}
	}

	private static void exibirDialogExcluirNota() {
		JTextField idNotaField = new JTextField();

		Object[] mensagem = { "Id nota:", idNotaField };

		int resultado = JOptionPane.showConfirmDialog(null, mensagem, "Excluir nota", JOptionPane.OK_CANCEL_OPTION);

		if (resultado == JOptionPane.OK_OPTION) {
			int idNota = Integer.parseInt(idNotaField.getText());
			boolean sucesso = ProfessorDao.excluirNota(idNota);

			if (sucesso) {
				exibirMensagem("Nota excluída com sucesso.");
			} else {
				exibirMensagem("Nota não econtrada!");
			}
		}
	}

	private static void exibirMenuPrincipal() {
		boolean sair = false;
		while (!sair) {
			String[] opcoes = { "Cadastrar como PROFESSOR", "Login como PROFESSOR", "Cadastrar como ALUNO",
					"Login como ALUNO", "Sair" };

			int opcaoSelecionada = JOptionPane.showOptionDialog(null, "Menu Principal", "NOTA PRO",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);

			switch (opcaoSelecionada) {
			case 0:
				exibirDialogCadastrarProfessor();
				break;
			case 1:
				if (exibirDialogLoginProfessor()) {
					exibirMenuProfessor1();
				}
				break;
			case 2:
				exibirDialogCadastrarAluno();
				break;
			case 3:
				if(exibirDialogLoginAluno()) {
					exibirMenuAluno();
				}
				break;
			case 4:
				sair = true;
				break;
			}
		}

		encerrarPrograma();
	}

	private static void exibirMenuProfessor1() {
		boolean sair = false;
		while (!sair) {
			String[] opcoes = { "Visualizar notas", "Cadastrar nota", "Listar alunos", "Sair" };

			int opcaoSelecionada = JOptionPane.showOptionDialog(null, "Menu Professor 1", "NOTA PRO",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);

			switch (opcaoSelecionada) {
			case 0:
				if (exibirDialogVisualizarNotas()) {
					exibirMenuProfessor2();
				}
				break;
			case 1:
				exibirDialogCadastrarNota();
				break;
			case 2:
				exibirListaAlunos();
				break;
			case 3:
				sair = true;
				break;
			}
		}
	}

	private static void exibirMenuProfessor2() {
		boolean sair = false;
		while (!sair) {
			String[] opcoes = { "Editar nota", "Excluir nota", "Sair" };

			int opcaoSelecionada = JOptionPane.showOptionDialog(null, "Menu Professor 2", "NOTA PRO",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);

			switch (opcaoSelecionada) {
			case 0:
				exibirDialogEditarNota();
				break;
			case 1:
				exibirDialogExcluirNota();
				break;
			case 2:
				sair = true;
				break;
			}
		}
	}

	private static void exibirMenuAluno() {
		boolean sair = false;
		while (!sair) {
			String[] opcoes = { "Visualizar notas", "Sair" };

			int opcaoSelecionada = JOptionPane.showOptionDialog(null, "Menu Professor 1", "NOTA PRO",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);

			switch (opcaoSelecionada) {
			case 0:
				exibirNotasParaAluno(idAluno);
				break;
			case 1:
				sair = true;
				break;
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> exibirMenuPrincipal());
	}
}
