package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import connection.Conexao;
import entities.Aluno;
import entities.Nota;
import entities.Professor;

import org.mindrot.jbcrypt.BCrypt;

public class ProfessorDao {
	static Connection connection = Conexao.getConexao();
	static PreparedStatement ps;
	static ResultSet rs;

	public static Integer buscarId(String login) {
		String sql = "SELECT idprofessor FROM PROFESSOR WHERE login = ?";
		ps = null;
		rs = null;

		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, login);
			rs = ps.executeQuery();

			if (rs.next()) {
				int idProfessor = rs.getInt("idprofessor");
				return idProfessor;
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar id do professor / " + e.getMessage());
			return null;
		}
	}

	public static boolean cadastrarProfessor(Professor prof) {
		String sql = "INSERT INTO PROFESSOR (nome, login, senha) VALUES (?, ?, ?)";
		ps = null;

		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, prof.getNome());
			ps.setString(2, prof.getLogin());
			ps.setString(3, prof.getSenha());
			ps.execute();

			return true;
		} catch (SQLException e) {
			System.out.println("Erro ao realizar cadastro / " + e.getMessage());
			return false;
		}
	}

	public static boolean checkPassword(String senha, String senhaArmazenada) {
		return BCrypt.checkpw(senha, senhaArmazenada);
	}

	public static boolean loginProfessor(String login, String senha) {
		String sql = "SELECT login, senha FROM PROFESSOR WHERE login = ?";
		ps = null;
		rs = null;

		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, login);
			rs = ps.executeQuery();

			if (rs.next()) {
				String senhaArmazenada = rs.getString("senha");

				if (checkPassword(senha, senhaArmazenada)) {
					return true;
				} else {
					JOptionPane.showMessageDialog(null, "Senha incorreta!");
					return false;
				}
			} else {
				JOptionPane.showMessageDialog(null, "Usuário não encontrado !");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao realizar login / " + e.getMessage());
			return false;
		}
	}

	public static List<Aluno> listarAlunos() {
		String sql = "SELECT idaluno, nome FROM ALUNO";
		List<Aluno> alunos = new ArrayList<>();
		ps = null;
		rs = null;

		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				int retrievedIdAluno = rs.getInt("idaluno");
				String retrievedNome = rs.getString("nome");

				Aluno aluno = new Aluno(retrievedIdAluno, retrievedNome);
				alunos.add(aluno);
			}

			return alunos;
		} catch (SQLException e) {
			System.out.println("Erro ao listar alunos / " + e.getMessage());
			return null;
		}
	}

	public static List<Nota> listarNotasAluno(int idAluno) {
		String sql = "SELECT idnota, idaluno, nomealuno, idprofessor, disciplina, nota FROM NOTAS WHERE idaluno = ?";
		List<Nota> notas = new ArrayList<>();
		ps = null;
		rs = null;

		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAluno);

			rs = ps.executeQuery();
			while (rs.next()) {
				int retrievedIdNota = rs.getInt("idnota");
				int retrievedIdAluno = rs.getInt("idaluno");
				String retrievedNomeAluno = rs.getString("nomealuno");
				int retrievedIdProfessor = rs.getInt("idprofessor");
				String retrievedDisciplina = rs.getString("disciplina");
				double retrievedNota = rs.getDouble("nota");

				Nota nota = new Nota(retrievedIdNota, retrievedIdAluno, retrievedNomeAluno, retrievedIdProfessor, retrievedDisciplina,
						retrievedNota);
				notas.add(nota);
			}
			if (notas.isEmpty()) {
				return null;
			} else {
				return notas;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao listar notas do aluno / " + e.getMessage());
			return null;
		}
	}

	public static String buscarNomeAluno(int idAluno) {
		String sql = "SELECT nome FROM ALUNO WHERE idaluno = ?";
		ps = null;
		rs = null;

		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAluno);

			rs = ps.executeQuery();
			if (rs.next()) {
				String nomeAluno = rs.getString("nome");
				return nomeAluno;
			} else {
				JOptionPane.showMessageDialog(null, "Aluno não encontrado!");
				return null;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar nome do aluno / " + e.getMessage());
			return null;
		}
	}

	public static boolean cadastrarNota(Nota nota) {
		String sql = "INSERT INTO NOTAS (idaluno, nomealuno, idprofessor, disciplina, nota) VALUES (?, ?, ?, ?, ?)";
		ps = null;

		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, nota.getIdAluno());
			ps.setString(2, nota.getNomeAluno());
			ps.setInt(3, nota.getIdProfessor());
			ps.setString(4, nota.getDisciplina());
			ps.setDouble(5, nota.getNota());

			ps.execute();
			return true;
		} catch (SQLException e) {
			System.out.println("Erro ao cadastrar nota / " + e.getMessage());
			return false;
		}
	}

	public static boolean editarNota(int idNota, double nota) {
		String sql = "UPDATE NOTAS SET nota = ? WHERE idnota = ?";
		ps = null;

		try {
			ps = connection.prepareStatement(sql);
			ps.setDouble(1, nota);
			ps.setInt(2, idNota);

			int rowsAffected = ps.executeUpdate();
			if(rowsAffected > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao editar nota / " + e.getMessage());
			return false;
		}
	}

	public static boolean excluirNota(int idNota) {
		String sql = "DELETE FROM NOTAS WHERE idnota = ?";
		ps = null;

		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idNota);

			int rowsAffected = ps.executeUpdate();
			if(rowsAffected > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao excluir nota / " + e.getMessage());
			return false;
		}
	}

	public static boolean encerrarConexoes() {
		try {
			if (connection != null)
				connection.close();
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
			return true;
		} catch (SQLException e) {
			System.out.println("Erro ao encerrar conexões / " + e.getMessage());
			return false;
		}
	}
}
