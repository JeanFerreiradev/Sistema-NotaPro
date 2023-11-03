package application;

import java.security.SecureRandom;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

import dao.AlunoDao;
import dao.ProfessorDao;
import entities.Aluno;
import entities.Nota;
import entities.Professor;

public class Main {

	public static String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return BCrypt.gensalt(12);
	}

	public static String hashPassword(String senha, String salt) {
		return BCrypt.hashpw(senha, salt);
	}

	public static void menuPrincipal() {
		System.out.println("\n---MENU---");
		System.out.println("0) SAIR");
		System.out.println("1) Cadastrar como PROFESSOR");
		System.out.println("2) Login como PROFESSOR");
		System.out.println("3) Cadastrar como ALUNO");
		System.out.println("4) Login como ALUNO");
	}

	public static void menuProfessor1() {
		System.out.println("\n---MENU PROFESSOR 1---");
		System.out.println("0) SAIR");
		System.out.println("1) Visualizar notas");
		System.out.println("2) Cadastrar nota");
		System.out.println("3) Listar alunos");
	}

	public static void menuProfessor2() {
		System.out.println("\n---MENU PROFESSOR 2---");
		System.out.println("0) SAIR");
		System.out.println("1) Editar nota");
		System.out.println("2) Excluir nota");
	}

	public static void menuAluno() {
		System.out.println("\n---MENU ALUNO---");
		System.out.println("0) SAIR");
		System.out.println("1) Visualizar notas");
	}

	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		int opcao;

		do {
			menuPrincipal();
			System.out.print("Digite uma opção: ");
			opcao = console.nextInt();
			console.nextLine();

			switch (opcao) {
			case 0:
				System.out.println("Programa fechado!");
				ProfessorDao.encerrarConexoes();
				break;
			case 1:
				System.out.print("Digite seu nome: ");
				String nomeProf = console.nextLine();
				System.out.print("Digite seu login: ");
				String loginProf = console.nextLine();
				System.out.print("Digite sua senha: ");
				String senhaProf = console.nextLine();
				String saltProf = generateSalt();
				String senhaProfHashed = hashPassword(senhaProf, saltProf);

				Professor prof = new Professor(nomeProf, loginProf, senhaProfHashed);
				if (ProfessorDao.cadastrarProfessor(prof))
					System.out.println("Cadastro bem-sucedido!");

				break;
			case 2:
				System.out.print("Digite seu login: ");
				String loginProf2 = console.nextLine();
				System.out.print("Digite sua senha: ");
				String senhaProf2 = console.nextLine();
				if (ProfessorDao.loginProfessor(loginProf2, senhaProf2)) {
					System.out.println("Login bem-sucedido!");
					System.out.println("\n---Lista alunos---");
					System.out.println(ProfessorDao.listarAlunos());
					Integer idProfessor = ProfessorDao.buscarId(loginProf2);
					int opcaoProf1;
					do {
						menuProfessor1();
						System.out.print("Digite uma opção: ");
						opcaoProf1 = console.nextInt();
						console.nextLine();
						switch (opcaoProf1) {
						case 0:
							System.out.println("Voltando para menu principal...");
							break;
						case 1:
							System.out.print("Digite o id do aluno: ");
							int idAluno = console.nextInt();
							System.out.println("---Notas aluno---");
							if (ProfessorDao.listarNotasAluno(idAluno) != null) {
								System.out.println(ProfessorDao.listarNotasAluno(idAluno));
								int opcaoProf2;
								do {
									menuProfessor2();
									System.out.print("Digite uma opção: ");
									opcaoProf2 = console.nextInt();
									switch (opcaoProf2) {
									case 0:
										System.out.println("Voltando para menu professor 1...");
										break;
									case 1:
										System.out.print("Digite o id da nota: ");
										int idNota = console.nextInt();
										System.out.print("Digite a nova nota: ");
										double novaNota = console.nextDouble();
										if (ProfessorDao.editarNota(idNota, novaNota)) {
											System.out.println("Nota alterada com sucesso!");
										} else {
											System.out.println("Nota não encontrada!");
										}
										break;
									case 2:
										System.out.print("Digite o id da nota: ");
										int idNota2 = console.nextInt();
										console.nextLine();
										System.out.print("Tem certeza que deseja excluir essa nota(S/N)? ");
										String excluir = console.nextLine();
										if (excluir.charAt(0) == 'S' || excluir.charAt(0) == 's') {
											if (ProfessorDao.excluirNota(idNota2)) {
												System.out.println("Nota excluída com sucesso!");
											} else {
												System.out.println("Nota não encontrada!");
											}
										} else {
											System.out.println("Operação de exclusão cancelada!");
										}
										break;
									default:
										System.out.println("Opção inválida, escolha novamente:");
										break;
									}
								} while (opcaoProf2 != 0);
							} else {
								System.out.println("Este aluno ainda não tem notas cadastradas!");
							}
							break;
						case 2:
							System.out.print("Digite o id do aluno: ");
							int idAluno2 = console.nextInt();
							console.nextLine();
							System.out.print("Digite a disciplina: ");
							String disciplina = console.nextLine();
							System.out.print("Digite a nota: ");
							double nota = console.nextDouble();
							String nomeAluno = ProfessorDao.buscarNomeAluno(idAluno2);
							Nota notaAluno = new Nota(idAluno2, nomeAluno, idProfessor, disciplina, nota);
							if (ProfessorDao.cadastrarNota(notaAluno)) {
								System.out.println("Nota cadastrada!");
							}
							break;
						case 3:
							System.out.println("\n---Lista alunos---");
							System.out.println(ProfessorDao.listarAlunos());
							break;
						default:
							System.out.println("Opção inválida, escolha novamente:");
							break;
						}
					} while (opcaoProf1 != 0);
				}
				break;
			case 3:
				System.out.print("Digite seu nome: ");
				String nomeAluno = console.nextLine();
				System.out.print("Digite seu login: ");
				String loginAluno = console.nextLine();
				System.out.print("Digite sua senha: ");
				String senhaAluno = console.nextLine();
				String saltAluno = generateSalt();
				String senhaAlunoHashed = hashPassword(senhaAluno, saltAluno);

				Aluno aluno = new Aluno(nomeAluno, loginAluno, senhaAlunoHashed);
				if (AlunoDao.cadastrarAluno(aluno))
					System.out.println("Cadastro bem-sucedido!");
				break;
			case 4:
				System.out.print("Digite seu login: ");
				String loginAluno2 = console.nextLine();
				System.out.print("Digite sua senha: ");
				String senhaAluno2 = console.nextLine();
				if (AlunoDao.loginAluno(loginAluno2, senhaAluno2)) {
					System.out.println("Login bem-sucedido!");
					int opcaoAluno;
					do {
						menuAluno();
						System.out.print("Digite uma opção: ");
						opcaoAluno = console.nextInt();
						console.nextLine();
						switch (opcaoAluno) {
						case 0:
							System.out.println("Voltando para menu principal...");
							break;
						case 1:
							Integer idAluno = AlunoDao.buscarId(loginAluno2);
							System.out.println("---Notas aluno---");
							if (AlunoDao.listarNotasAluno(idAluno) != null) {
								System.out.println(AlunoDao.listarNotasAluno(idAluno));
							} else {
								System.out.println("Você ainda não tem notas cadastradas!");
							}
							break;
						default:
							System.out.println("Opção inválida, escolha novamente:");
							break;
						}
					} while (opcaoAluno != 0);
				}
				break;
			default:
				System.out.println("Opção inválida, escolha novamente:");
				break;
			}

		} while (opcao != 0);

		console.close();
	}

}
