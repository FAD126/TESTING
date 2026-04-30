package notas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestorNotas {

    private List<Nota> notas = new ArrayList<>();
    private int contadorId = 1;

    // ─────────────────────────────────────────────
    // RF-01: Registrar Nota
    // ─────────────────────────────────────────────
    public String registrarNota(String idEstudiante, String curso, String idEvaluacion, double valor) {
        if (idEstudiante == null || idEstudiante.isBlank())
            return "ERROR: El ID del estudiante no puede estar vacío.";
        if (curso == null || curso.isBlank())
            return "ERROR: El curso no puede estar vacío.";
        if (idEvaluacion == null || idEvaluacion.isBlank())
            return "ERROR: El ID de la evaluación no puede estar vacío.";
        if (!rangoValido(valor))
            return "ERROR: La nota debe estar entre 0 y 20.";

        Nota nueva = new Nota(contadorId++, idEstudiante.trim(), curso.trim(), idEvaluacion.trim(), valor);
        notas.add(nueva);
        return "OK: Nota registrada exitosamente. " + nueva;
    }

    // ─────────────────────────────────────────────
    // RF-02: Editar Nota
    // ─────────────────────────────────────────────
    public String editarNota(int idNota, double nuevoValor) {
        if (!rangoValido(nuevoValor))
            return "ERROR: La nueva nota debe estar entre 0 y 20.";

        Nota nota = buscarPorId(idNota);
        if (nota == null)
            return "ERROR: No se encontró ninguna nota con ID " + idNota + ".";

        double anterior = nota.getValor();
        nota.setValor(nuevoValor);
        return String.format("OK: Nota actualizada. ID:%d | Valor anterior: %.2f -> Nuevo valor: %.2f",
                idNota, anterior, nuevoValor);
    }

    // ─────────────────────────────────────────────
    // RF-03: Promedio Automático
    // ─────────────────────────────────────────────
    public String calcularPromedio(String idEstudiante) {
        if (idEstudiante == null || idEstudiante.isBlank())
            return "ERROR: El ID del estudiante no puede estar vacío.";

        List<Nota> notasEstudiante = notas.stream()
                .filter(n -> n.getIdEstudiante().equalsIgnoreCase(idEstudiante.trim()))
                .collect(Collectors.toList());

        if (notasEstudiante.isEmpty())
            return "ERROR: No se encontraron notas para el estudiante " + idEstudiante + ".";
        if (notasEstudiante.size() < 2)
            return "INFO: Se necesitan al menos 2 notas para calcular promedio. Actualmente: 1 nota.";

        double promedio = notasEstudiante.stream()
                .mapToDouble(Nota::getValor)
                .average()
                .orElse(0);

        return String.format("Promedio de %s: %.2f (sobre %d evaluaciones)",
                idEstudiante, promedio, notasEstudiante.size());
    }

    // ─────────────────────────────────────────────
    // Listar todas las notas
    // ─────────────────────────────────────────────
    public String listarNotas() {
        if (notas.isEmpty())
            return "-- El registro se encuentra vacio --";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-12s | %-25s | %-15s | %s%n",
                "Estudiante", "Curso", "Evaluacion", "Nota"));
        sb.append("-".repeat(70)).append("\n");
        notas.forEach(n -> sb.append(
                String.format("%-12s | %-25s | %-15s | %.2f%n",
                        n.getIdEstudiante(), n.getCurso(), n.getIdEvaluacion(), n.getValor())
        ));
        return sb.toString().trim();
    }

    // ─────────────────────────────────────────────
    // Helpers privados
    // ─────────────────────────────────────────────
    private boolean rangoValido(double valor) {
        return valor >= 0 && valor <= 20;
    }

    private Nota buscarPorId(int id) {
        return notas.stream().filter(n -> n.getId() == id).findFirst().orElse(null);
    }
}
