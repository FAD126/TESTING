package notas;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GestorNotas gestor = new GestorNotas();
        Scanner sc = new Scanner(System.in);

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   MÓDULO DE EVALUACIONES / NOTAS     ║");
        System.out.println("║       Grupo C — UCSM 2026            ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Registrar nota");
            System.out.println("2. Editar nota");
            System.out.println("3. Calcular promedio de un estudiante");
            System.out.println("4. Listar todas las notas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1":
                    menuRegistrar(gestor, sc);
                    break;
                case "2":
                    menuEditar(gestor, sc);
                    break;
                case "3":
                    menuPromedio(gestor, sc);
                    break;
                case "4":
                    System.out.println("\n=== LISTADO DE NOTAS ===");
                    System.out.println(gestor.listarNotas());
                    break;
                case "0":
                    salir = true;
                    System.out.println("Saliendo del sistema. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente nuevamente.");
                    break;
            }
        }

        sc.close();
    }

    // ── RF-01 ──
    private static void menuRegistrar(GestorNotas gestor, Scanner sc) {
        System.out.println("\n[ REGISTRAR NOTA ]");

        // ID Estudiante: código de exactamente 10 dígitos numéricos, no negativo
        String idEst;
        while (true) {
            System.out.print("Codigo de estudiante (10 digitos): ");
            idEst = sc.nextLine().trim();
            if (idEst.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("  ERROR: Debe ingresar exactamente 10 digitos numericos (sin letras ni signos).");
            }
        }

        // Nombre del curso: solo letras (y espacios), sin números
        String nombreCurso;
        while (true) {
            System.out.print("Nombre del curso: ");
            nombreCurso = sc.nextLine().trim();
            if (nombreCurso.isEmpty() || !nombreCurso.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+")) {
                System.out.println("  ERROR: El nombre del curso solo puede contener letras (sin numeros ni simbolos).");
            } else {
                break;
            }
        }

        // Practica o Teoría
        String modalidad;
        while (true) {
            System.out.print("Practica / Teoria: ");
            modalidad = sc.nextLine().trim().toLowerCase();
            if (modalidad.equals("practica") || modalidad.equals("teoria")) {
                break;
            } else {
                System.out.println("  ERROR: Solo se acepta 'practica' o 'teoria'.");
            }
        }

        // Sección o Grupo según modalidad
        String sufijo;
        if (modalidad.equals("teoria")) {
            while (true) {
                System.out.print("Seccion (A, B o C): ");
                String seccion = sc.nextLine().trim().toUpperCase();
                if (seccion.matches("[A-C]")) {
                    sufijo = seccion;
                    break;
                } else {
                    System.out.println("  ERROR: La seccion debe ser una sola letra entre A y C.");
                }
            }
        } else {
            while (true) {
                System.out.print("Grupo (01 al 10): ");
                String grupo = sc.nextLine().trim();
                if (grupo.matches("0[1-9]") || grupo.equals("10")) {
                    sufijo = grupo;
                    break;
                } else {
                    System.out.println("  ERROR: El grupo debe ser un valor del 01 al 10 (ej: 01, 05, 10).");
                }
            }
        }

        // Armar nombre completo del curso
        String cursoCompleto = nombreCurso + "-" + sufijo;
        System.out.println("  Curso registrado: " + cursoCompleto);

        // ID Evaluación
        System.out.print("ID Evaluacion: ");
        String idEval = sc.nextLine().trim();

        // Nota: solo esta parte se repite si hay error
        double valor;
        while (true) {
            System.out.print("Nota (0-20): ");
            String entrada = sc.nextLine().trim().replace(",", ".");
            try {
                valor = Double.parseDouble(entrada);
                if (valor < 0 || valor > 20) {
                    System.out.println("  ERROR: La nota debe estar entre 0 y 20.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("  ERROR: Ingrese un numero valido para la nota.");
            }
        }

        System.out.println(gestor.registrarNota(idEst, cursoCompleto, idEval, valor));
    }

    // ── RF-02 ──
    private static void menuEditar(GestorNotas gestor, Scanner sc) {
        System.out.println("\n[ EDITAR NOTA ]");
        System.out.println("=== LISTADO DE NOTAS ===");
        System.out.println(gestor.listarNotas());

        System.out.print("\nID de la nota a editar: ");
        int id = leerInt(sc);

        // Solo se repite la parte de la nota si hay error
        double valor;
        while (true) {
            System.out.print("Nueva nota (0-20): ");
            String entrada = sc.nextLine().trim().replace(",", ".");
            try {
                valor = Double.parseDouble(entrada);
                if (valor < 0 || valor > 20) {
                    System.out.println("  ERROR: La nota debe estar entre 0 y 20.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("  ERROR: Ingrese un numero valido para la nota.");
            }
        }

        System.out.println(gestor.editarNota(id, valor));
    }

    // ── RF-03 ──
    private static void menuPromedio(GestorNotas gestor, Scanner sc) {
        System.out.println("\n[ PROMEDIO AUTOMÁTICO ]");

        String idEst;
        while (true) {
            System.out.print("Codigo de estudiante (10 digitos): ");
            idEst = sc.nextLine().trim();
            if (idEst.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("  ERROR: Debe ingresar exactamente 10 digitos numericos.");
            }
        }

        System.out.println(gestor.calcularPromedio(idEst));
    }

    // ── Helper entero ──
    private static int leerInt(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
