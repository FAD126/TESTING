
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GradeRepository {

    private static final int MIN_GRADE = 0;
    private static final int MAX_GRADE = 20;

    private final Map<Integer, GradeRecord> gradesById = new HashMap<>();
    private final GradeHistoryRepository historyRepo = new GradeHistoryRepository();
    private final Path dataFile;

    private int nextId = 1;

    public GradeRepository() {
        this(Paths.get("notas.txt"));
    }

    public GradeRepository(Path dataFile) {
        this.dataFile = dataFile;
        loadFromFile();
    }

    public synchronized GradeRecord create(String studentName, String subject, int grade) {
        validateGradeRange(grade);
        int id = nextId++;
        GradeRecord record = new GradeRecord(id, studentName, subject, grade, LocalDateTime.now());
        gradesById.put(id, record);
        saveToFile();
        return record;
    }

    public synchronized List<GradeRecord> findAll() {
        List<GradeRecord> result = new ArrayList<>(gradesById.values());
        result.sort(Comparator.comparing(GradeRecord::getCreatedAt));
        return result;
    }

    public synchronized Optional<GradeRecord> findById(int id) {
        return Optional.ofNullable(gradesById.get(id));
    }

    public synchronized boolean existsByStudentAndSubject(String studentName, String subject) {
        return existsByStudentAndSubjectExcludingId(studentName, subject, null);
    }

    public synchronized boolean existsByStudentAndSubjectExcludingId(String studentName, String subject, Integer excludedId) {
        String normalizedStudent = normalize(studentName);
        String normalizedSubject = normalize(subject);

        for (GradeRecord record : gradesById.values()) {
            if (excludedId != null && record.getId() == excludedId) {
                continue;
            }
            if (normalizedStudent.equals(normalize(record.getStudentName()))
                    && normalizedSubject.equals(normalize(record.getSubject()))) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean update(int id, String studentName, String subject, int grade) {
        validateGradeRange(grade);

        GradeRecord existing = gradesById.get(id);
        if (existing == null) {
            return false;
        }

        int oldGrade = existing.getGrade();

        existing.setStudentName(studentName);
        existing.setSubject(subject);
        existing.setGrade(grade);

        historyRepo.add(id, oldGrade, grade);
        saveToFile();
        return true;
    }

    public synchronized boolean delete(int id) {
        boolean deleted = gradesById.remove(id) != null;
        if (deleted) {
            saveToFile();
        }
        return deleted;
    }

    public synchronized void clear() {
        gradesById.clear();
        nextId = 1;
        historyRepo.clear();
        saveToFile();
    }

    public synchronized List<GradeHistoryRecord> findHistoryByGradeId(int gradeId) {
        return historyRepo.findByGradeId(gradeId);
    }

    public synchronized Double computeGlobalAverage() {
        if (gradesById.isEmpty()) {
            return null;
        }
        double sum = 0.0;
        for (GradeRecord r : gradesById.values()) {
            sum += r.getGrade();
        }
        return sum / gradesById.size();
    }

    public synchronized Double computeAverageByStudent(String student) {
        if (student == null) {
            return null;
        }
        String s = student.trim();
        if (s.isEmpty()) {
            return null;
        }

        double sum = 0.0;
        int count = 0;
        for (GradeRecord r : gradesById.values()) {
            if (s.equals(r.getStudentName())) {
                sum += r.getGrade();
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        return sum / count;
    }

    public synchronized Double computeAverageBySubject(String subject) {
        if (subject == null) {
            return null;
        }
        String sub = subject.trim();
        if (sub.isEmpty()) {
            return null;
        }

        double sum = 0.0;
        int count = 0;
        for (GradeRecord r : gradesById.values()) {
            if (sub.equals(r.getSubject())) {
                sum += r.getGrade();
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        return sum / count;
    }

    private void validateGradeRange(int grade) {
        if (grade < MIN_GRADE || grade > MAX_GRADE) {
            throw new IllegalArgumentException("La calificación debe estar entre 0 y 20. Valor recibido: " + grade);
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private void loadFromFile() {
        if (!Files.exists(dataFile)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(dataFile, StandardCharsets.UTF_8);
            int maxId = 0;

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|", -1);
                if (parts.length != 5) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0]);
                    LocalDateTime createdAt = LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    String studentName = decode(parts[2]);
                    String subject = decode(parts[3]);
                    int grade = Integer.parseInt(parts[4]);
                    validateGradeRange(grade);

                    gradesById.put(id, new GradeRecord(id, studentName, subject, grade, createdAt));
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (RuntimeException ex) {
                    // Ignorar lineas invalidas para no impedir que la app inicie.
                }
            }

            nextId = maxId + 1;
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo leer el archivo de notas: " + dataFile, ex);
        }
    }

    private void saveToFile() {
        List<GradeRecord> records = findAll();
        List<String> lines = new ArrayList<>();

        for (GradeRecord record : records) {
            lines.add(record.getId()
                    + "|" + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(record.getCreatedAt())
                    + "|" + encode(record.getStudentName())
                    + "|" + encode(record.getSubject())
                    + "|" + record.getGrade());
        }

        try {
            Path parent = dataFile.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(dataFile, lines, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo guardar el archivo de notas: " + dataFile, ex);
        }
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("UTF-8 no esta disponible.", ex);
        }
    }

    private String decode(String value) {
        try {
            return URLDecoder.decode(value == null ? "" : value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("UTF-8 no esta disponible.", ex);
        }
    }
}
