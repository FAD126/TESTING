package ManejoNotas;

import java.util.ArrayList;
import Notas.*;

public class ManejoNotas {

    public static ArrayList<NotaCurso> notas;

    public ManejoNotas() {
        notas = new ArrayList<>();
    }

    public void registrarNota(int idEst, int idCur, int fase, double nota) {

        for (NotaCurso n : notas) {

            if (n.getIdEstudiante() == idEst &&
                n.getIdCurso() == idCur) {

                switch (fase) {

                    case 1:
                        n.setNotaF1(nota);
                        break;

                    case 2:
                        n.setNotaF2(nota);
                        break;

                    case 3:
                        n.setNotaF3(nota);
                        break;
                }
            }
        }
    }

    public static void main(String[] args) {

        Curso testing = new Curso(1, "Testing");

        Estudiante est1 =
            new Estudiante(2023400331, "Jose", "Yto");

        NotaCurso nc1 =
            new NotaCurso(2023400331, 1);

        ManejoNotas mn = new ManejoNotas();

        mn.notas.add(nc1);

        mn.registrarNota(2023400331, 1, 1, 11.0);

        for (NotaCurso n : mn.notas) {
            System.out.println(n);
        }
    }
}