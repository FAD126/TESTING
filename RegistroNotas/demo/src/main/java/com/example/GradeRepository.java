package com.example;

import java.time.LocalDateTime;
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

    private int nextId = 1;

    public synchronized GradeRecord create(String studentName, String subject, int grade) {
        validateGradeRange(grade);
        int id = nextId++;
        GradeRecord record = new GradeRecord(id, studentName, subject, grade, LocalDateTime.now());
        gradesById.put(id, record);
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
        return true;
    }

    public synchronized boolean delete(int id) {
        return gradesById.remove(id) != null;
    }

    public synchronized void clear() {
        gradesById.clear();
        nextId = 1;
        historyRepo.clear();
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
}
