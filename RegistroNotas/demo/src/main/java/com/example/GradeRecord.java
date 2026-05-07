package com.example;

import java.time.LocalDateTime;
import java.util.Objects;

public class GradeRecord {

    private final int id;
    private String studentName;
    private String subject;
    private int grade;
    private final LocalDateTime createdAt;

    public GradeRecord(int id, String studentName, String subject, int grade, LocalDateTime createdAt) {
        this.id = id;
        this.studentName = studentName;
        this.subject = subject;
        this.grade = grade;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GradeRecord)) {
            return false;
        }
        GradeRecord other = (GradeRecord) o;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return studentName + " - " + subject;
    }
}
