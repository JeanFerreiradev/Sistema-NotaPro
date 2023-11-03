package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

import connection.Conexao;
import entities.Aluno;
import entities.Nota;

public class AlunoDao {
	static Connection connection = Conexao.getConexao();
	static PreparedStatement ps;
	static ResultSet rs;
	
	public static Integer buscarId(String login) {
		String sql = "SELECT idaluno FROM ALUNO WHERE login = ?";
		ps = null;
		rs = null;
		
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, login);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				int idAluno = rs.getInt("idaluno");
				return idAluno;
			} else {
				return null;
			}
		} catch(SQLException e) {
			System.out.println("Erro ao buscar id do professor / " + e.getMessage());
			return null;
		}
	}
	
	public static boolean cadastrarAluno(Aluno aluno) {
		String sql = "INSERT INTO ALUNO (nome, login, senha) VALUES (?, ?, ?)";
		ps = null;
		
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, aluno.getNome());
			ps.setString(2, aluno.getLogin());
			ps.setString(3, aluno.getSenha());
			ps.execute();
			
			return true;
		} catch(SQLException e) {
			System.out.println("Erro ao realizar cadastro / " + e.getMessage());
			return false;
		}
	}
	
	public static boolean checkPassword(String senha, String senhaArmazenada) {
		return BCrypt.checkpw(senha, senhaArmazenada);
	}
	
	public static boolean loginAluno(String login, String senha) {
		String sql = "SELECT login, senha FROM ALUNO WHERE login = ?";
		ps = null;
		rs = null;
		
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, login);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				String senhaArmazenada = rs.getString("senha");
				
				if(checkPassword(senha, senhaArmazenada)) {
					return true;
				} else {
					JOptionPane.showMessageDialog(null, "Senha incorreta !");
					return false;
				}
			} else {
				JOptionPane.showMessageDialog(null, "Usuário não encontrado !");
				return false;
			}
		} catch(SQLException e) {
			System.out.println("Erro ao realizar login / " + e.getMessage());
			return false;
		}
	}
	
	public static List<Nota> listarNotasAluno(int idAluno) {
		String sql = "SELECT idaluno, nomealuno, idprofessor, disciplina, nota FROM NOTAS WHERE idaluno = ?";
		List<Nota> notas = new ArrayList<>();
		ps = null;
		rs = null;
		
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAluno);
			
			rs = ps.executeQuery();
			while(rs.next()) {
				int retrievedIdAluno = rs.getInt("idaluno");
				String retrievedNomeAluno = rs.getString("nomealuno");
				int retrievedIdProfessor = rs.getInt("idprofessor");
				String retrievedDisciplina = rs.getString("disciplina");
				double retrievedNota = rs.getDouble("nota");
				
				Nota nota = new Nota(retrievedIdAluno, retrievedNomeAluno, retrievedIdProfessor, retrievedDisciplina, retrievedNota);
				notas.add(nota);
			}
			
			if(notas.isEmpty()) {
				return null;
			} else {
				return notas;
			}
		} catch(SQLException e) {
			System.out.println("Erro ao listar notas do aluno / " + e.getMessage());
			return null;
		}
	}
}
