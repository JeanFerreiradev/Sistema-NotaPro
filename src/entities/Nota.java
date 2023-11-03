package entities;

public class Nota {
	private int idNota;
	private int idAluno;
	private String nomeAluno;
	private int idProfessor;
	private String disciplina;
	private double nota;
	
	public Nota(int idNota, int idAluno, String nomeAluno, int idProfessor, String disciplina, double nota) {
		this.idNota = idNota;
		this.idAluno = idAluno;
		this.nomeAluno = nomeAluno;
		this.idProfessor = idProfessor;
		this.disciplina = disciplina;
		this.nota = nota;
	}

	public Nota(int idAluno, String nomeAluno, int idProfessor, String disciplina, double nota) {
		this.idAluno = idAluno;
		this.nomeAluno = nomeAluno;
		this.idProfessor = idProfessor;
		this.disciplina = disciplina;
		this.nota = nota;
	}

	public int getIdProfessor() {
		return idProfessor;
	}

	public void setIdProfessor(int idProfessor) {
		this.idProfessor = idProfessor;
	}

	public int getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(int idAluno) {
		this.idAluno = idAluno;
	}

	public String getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public double getNota() {
		return nota;
	}

	public void setNota(double nota) {
		this.nota = nota;
	}
	
	public String getNomeAluno() {
		return nomeAluno;
	}
	
	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}
	
	public int getIdNota() {
		return idNota;
	}

	@Override
	public String toString() {
		return "Nota [idNota=" + idNota + ", idAluno=" + idAluno + ", nomeAluno=" + nomeAluno + ", idProfessor=" + idProfessor
				+ ", disciplina=" + disciplina + ", nota=" + nota + "]\n";
	}
	
}
