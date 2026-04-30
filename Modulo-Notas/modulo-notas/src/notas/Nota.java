package notas;

public class Nota {
    private int id;
    private String idEstudiante;
    private String curso;
    private String idEvaluacion;
    private double valor;

    public Nota(int id, String idEstudiante, String curso, String idEvaluacion, double valor) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.curso = curso;
        this.idEvaluacion = idEvaluacion;
        this.valor = valor;
    }

    public int getId() { return id; }
    public String getIdEstudiante() { return idEstudiante; }
    public String getCurso() { return curso; }
    public String getIdEvaluacion() { return idEvaluacion; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    @Override
    public String toString() {
        return String.format("[ID:%d] Estudiante: %s | Curso: %s | Evaluacion: %s | Nota: %.2f",
                id, idEstudiante, curso, idEvaluacion, valor);
    }
}
