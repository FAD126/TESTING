package Notas;

public class NotaCurso {
	private int idEstudiante;
	private int idCurso;
	private double notaF1;
	private double notaF2;
	private double notaF3;
	private double promedio;

	public NotaCurso(int idEstudiante, int idCurso, double notaF1, double notaF2, double notaF3) {
		super();
		this.idEstudiante = idEstudiante;
		this.idCurso = idCurso;
		this.notaF1 = notaF1 = 0;
		this.notaF2 = notaF2 = 0;
		this.notaF3 = notaF3 = 0;
		this.promedio = (notaF1 + notaF2 + notaF3) / 3;
	}

	public NotaCurso(int idEstudiante, int idCurso) {
		super();
		this.idEstudiante = idEstudiante;
		this.idCurso = idCurso;
	}

	public int getIdEstudiante() {
		return idEstudiante;
	}

	public void setIdEstudiante(int idEstudiante) {
		this.idEstudiante = idEstudiante;
	}

	public int getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}

	public double getNotaF1() {
		return notaF1;
	}

	public void setNotaF1(double notaF1) {
		this.notaF1 = notaF1;
	}

	public double getNotaF2() {
		return notaF2;
	}

	public void setNotaF2(double notaF2) {
		this.notaF2 = notaF2;
	}

	public double getNotaF3() {
		return notaF3;
	}

	public void setNotaF3(double notaF3) {
		this.notaF3 = notaF3;
	}

	public double getPromedio() {
		return promedio;
	}

	@Override
	public String toString() {
		return "NotaCurso [idEstudiante=" + idEstudiante + ", idCurso=" + idCurso + ", notaF1=" + notaF1 + ", notaF2="
				+ notaF2 + ", notaF3=" + notaF3 + ", promedio=" + promedio + "]";
	}


	
}
