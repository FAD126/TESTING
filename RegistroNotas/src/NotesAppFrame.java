
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class NotesAppFrame extends JFrame {

    private final GradeRepository repo;

    private final DefaultTableModel tableModel;
    private final JTable table;

    // labels
    private final JLabel label1;
    private final JLabel label2;

    private final JComboBox studentField;
    private final JComboBox subjectField;
    private final JTextField gradeField;

    private final JButton addButton;
    private final JButton updateButton;
    private final JButton deleteButton;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Integer selectedId = null;

    public NotesAppFrame(GradeRepository repo) {
        super("Registro de Notas");
        this.repo = repo;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Mostrar como pantalla completa (maximizada)
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Estudiante", "Asignatura", "Calificación", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScroll = new JScrollPane(table);

        JPanel editorPanel = new JPanel(new BorderLayout(10, 10));
        editorPanel.setBorder(BorderFactory.createTitledBorder("Editar nota"));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.anchor = GridBagConstraints.WEST;

        // instancia de labels
        // label de inicio
        label1 = new JLabel("Registrar nota de estudiantes");
        label1.setFont(new Font("Mono Space", 1, 20));

        // mensaje para usuario
        label2 = new JLabel("Complete los siguientes campos para poder registrar la nota del estudiante:");
        label2.setFont(new Font("Mono Space", 0, 14));

        // campos de ingreso
        subjectField = new JComboBox();
        studentField = new JComboBox();
        gradeField = new JTextField();

        // Ajuste de estilo (espaciado consistente)
        Font fieldFont = new Font("Mono Space", 0, 14);
        subjectField.setFont(fieldFont);
        studentField.setFont(fieldFont);
        gradeField.setFont(fieldFont);

        // opciones para subject (ComboBox)
        subjectField.addItem("-");
        subjectField.addItem("Matematicas");
        subjectField.addItem("Programacion");
        subjectField.addItem("Redes");

        // opciones para student (ComboBox)
        studentField.addItem("-");
        studentField.addItem("Alejandro");
        studentField.addItem("Esdras");
        studentField.addItem("Farid");
        studentField.addItem("Leonardo");

        // posicionamiento
        // labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(label1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(label2, gbc);

        // Asignatura (etiqueta con buen margen)
        JLabel asignaturaLabel = new JLabel("Asignatura:");
        asignaturaLabel.setFont(fieldFont);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(asignaturaLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(subjectField, gbc);

        // Estudiante (etiqueta con buen margen)
        JLabel estudianteLabel = new JLabel("Estudiante:");
        estudianteLabel.setFont(fieldFont);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(estudianteLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(studentField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel calificacionLabel = new JLabel("Calificación:");
        calificacionLabel.setFont(fieldFont);
        fieldsPanel.add(calificacionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(gradeField, gbc);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Agregar");
        updateButton = new JButton("Actualizar");
        deleteButton = new JButton("Eliminar");

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);

        editorPanel.add(fieldsPanel, BorderLayout.CENTER);
        editorPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Colocar editor (ingreso de datos) a la izquierda y la tabla a la derecha
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorPanel, tableScroll);
        // Proporción adaptable al tamaño de la ventana (más espacio para la tabla)
        splitPane.setResizeWeight(0.4);
        splitPane.setDividerLocation(0.4);

        add(splitPane, BorderLayout.CENTER);

        // Menu principal
        // (se define más abajo)
        buildMenu();

        // Selection -> load into editor
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                int row = table.getSelectedRow();
                if (row < 0) {
                    selectedId = null;
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    addButton.setEnabled(true);
                    subjectField.setSelectedIndex(0);
                    studentField.setSelectedIndex(0);
                    gradeField.setText("");
                    return;
                }

                // Cuando hay selección (editar/eliminar), bloquear el alta
                addButton.setEnabled(false);

                Object idObj = tableModel.getValueAt(row, 0);
                selectedId = (idObj instanceof Integer) ? (Integer) idObj : Integer.parseInt(String.valueOf(idObj));

                Optional<GradeRecord> recordOpt = repo.findById(selectedId);
                if (recordOpt.isPresent()) {
                    GradeRecord r = recordOpt.get();
                    studentField.setSelectedItem(r.getStudentName());
                    subjectField.setSelectedItem(r.getSubject());
                    gradeField.setText(String.valueOf(r.getGrade()));
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        // Actions
        addButton.addActionListener(e -> onAdd());
        updateButton.addActionListener(e -> onUpdate());
        deleteButton.addActionListener(e -> onDelete());

        refreshTable();
    }

    // mensajes de error
    private void onAdd() {
        Object studentSelected = studentField.getSelectedItem();
        Object subjectSelected = subjectField.getSelectedItem();
        String student = (studentSelected == null) ? "" : String.valueOf(studentSelected).trim();
        String subject = (subjectSelected == null) ? "" : String.valueOf(subjectSelected).trim();
        String gradeRaw = gradeField.getText().trim();

        if (student.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El estudiante no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (subject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La asignatura no puede estar vacía.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gradeRaw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La calificación no puede estar vacía.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int grade;
        try {
            if (gradeRaw.contains(".") || gradeRaw.contains(",")) {
                throw new NumberFormatException("decimal");
            }
            grade = Integer.parseInt(gradeRaw);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Calificación inválida. Usa un entero (sin decimales).", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (grade < 0 || grade > 20) {
            JOptionPane.showMessageDialog(this, "Nota fuera del rango", "Validacion", JOptionPane.WARNING_MESSAGE);
        }

        repo.create(student, subject, grade);

        clearSelection();
        refreshTable();
    }

    private void onUpdate() {
        if (selectedId == null) {
            return;
        }

        Object studentSelected = studentField.getSelectedItem();
        Object subjectSelected = subjectField.getSelectedItem();
        String student = (studentSelected == null) ? "" : String.valueOf(studentSelected).trim();
        String subject = (subjectSelected == null) ? "" : String.valueOf(subjectSelected).trim();
        String gradeRaw = gradeField.getText().trim();

        if (student.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El estudiante no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (subject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La asignatura no puede estar vacía.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gradeRaw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La calificación no puede estar vacía.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int grade;
        try {
            if (gradeRaw.contains(".") || gradeRaw.contains(",")) {
                throw new NumberFormatException("decimal");
            }
            grade = Integer.parseInt(gradeRaw);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Calificación inválida. Usa un entero (sin decimales).", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = repo.update(selectedId, student, subject, grade);

        if (!ok) {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el registro (id no encontrado).", "Error", JOptionPane.ERROR_MESSAGE);
        }
        refreshTable();
    }

    private void onDelete() {
        if (selectedId == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar el registro seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        repo.delete(selectedId);
        clearSelection();
        refreshTable();
    }

    private void clearSelection() {
        selectedId = null;
        table.clearSelection();
        studentField.setSelectedIndex(0);
        subjectField.setSelectedIndex(0);
        gradeField.setText("");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        addButton.setEnabled(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        List<GradeRecord> records = repo.findAll();
        for (GradeRecord r : records) {
            tableModel.addRow(new Object[]{
                r.getId(),
                r.getStudentName(),
                r.getSubject(),
                r.getGrade(),
                dtf.format(r.getCreatedAt())
            });
        }
    }

    private void buildMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Menú Archivo
        JMenu archivo = new JMenu("Archivo");

        JMenuItem exportPdf = new JMenuItem("Exportar PDF");
        exportPdf.addActionListener(e -> exportToPdf());

        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(e -> dispose());

        archivo.add(exportPdf);
        archivo.addSeparator();
        archivo.add(salir);

        // Menú Acerca de
        JMenu acerca = new JMenu("Ayuda");

        JMenuItem acercaCreadores = new JMenuItem("Acerca de creadores");
        acercaCreadores.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Creadores:\n- Alejandro Anco\n\nProyecto: Registro de Notas",
                "Acerca de creadores",
                JOptionPane.INFORMATION_MESSAGE
        ));

        acerca.add(acercaCreadores);

        menuBar.add(archivo);
        menuBar.add(acerca);

        setJMenuBar(menuBar);
    }

    private void exportToPdf() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar PDF");
        fileChooser.setSelectedFile(new java.io.File("notas.pdf"));
        fileChooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF", "pdf")
        );

        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        java.io.File outputFile = fileChooser.getSelectedFile();
        if (outputFile == null) {
            return;
        }

        // Agregar extensión .pdf si no la tiene
        if (!outputFile.getName().toLowerCase().endsWith(".pdf")) {
            outputFile = new java.io.File(outputFile.getAbsolutePath() + ".pdf");
        }

        // Confirmar sobreescritura
        if (outputFile.exists()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "El archivo ya existe. ¿Deseas reemplazarlo?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            PdfExporter.exportGradesToPdf(repo.findAll(), outputFile);
            JOptionPane.showMessageDialog(this,
                    "PDF exportado correctamente.",
                    "Exportar PDF",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al exportar el PDF: " + ex.getMessage(),
                    "Exportar PDF",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
