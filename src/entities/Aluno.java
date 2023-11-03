package entities;

public class Aluno {
	private int id;
	private String nome;
	private String login;
	private String senha;

	public Aluno(String nome, String login, String senha) {
		this.nome = nome;
		this.login = login;
		this.senha = senha;
	}
	
	public Aluno(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Aluno(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Aluno [id = " + id + ", nome = " + nome + "]\n";
	}

}
