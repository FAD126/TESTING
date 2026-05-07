package com.example;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class NoteRepositoryTest {

    private GradeRepository repo;

    @Before
    public void setUp() {
        repo = new GradeRepository();
    }

    @Test
    public void create_findAll_andUpdate_andDelete() {
        GradeRecord r1 = repo.create("Est1", "Matematicas", 10);
        GradeRecord r2 = repo.create("Est2", "Lengua", 9);

        assertEquals(2, repo.findAll().size());
        assertEquals("Est1", repo.findById(r1.getId()).get().getStudentName());
        assertEquals("Est2", repo.findById(r2.getId()).get().getStudentName());

        boolean updated = repo.update(r1.getId(), "Est1", "Matematicas", 11);

        assertTrue(updated);
        assertEquals(11, repo.findById(r1.getId()).get().getGrade());

        boolean deleted = repo.delete(r2.getId());
        assertTrue(deleted);
        assertFalse(repo.findById(r2.getId()).isPresent());

        List<GradeRecord> remaining = repo.findAll();
        assertEquals(1, remaining.size());
        assertEquals(r1.getId(), remaining.get(0).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_gradeBelowRange_throws() {
        repo.create("Est", "Mat", -1);

    }

    @Test(expected = IllegalArgumentException.class)
    public void create_gradeAboveRange_throws() {
        repo.create("Est", "Mat", 21);

    }

    @Test
    public void update_missing_returnsFalse_andDelete_missing_returnsFalse() {
        assertFalse(repo.update(999, "x", "y", 1));

        assertFalse(repo.delete(999));
    }

    @Test
    public void update_valid_createsHistory() {
        GradeRecord r1 = repo.create("Est1", "Matematicas", 10);

        boolean updated = repo.update(r1.getId(), "Est1", "Matematicas", 12);
        assertTrue(updated);

        List<GradeHistoryRecord> hist = repo.findHistoryByGradeId(r1.getId());
        assertEquals(1, hist.size());
        assertEquals(10, hist.get(0).getOldGrade());
        assertEquals(12, hist.get(0).getNewGrade());

    }

    @Test
    public void averages_global_student_subject() {
        repo.create("Est1", "Mat", 10);
        repo.create("Est1", "Lengua", 20);
        repo.create("Est2", "Mat", 0);

        // Global: (10 + 20 + 0) / 3 = 10
        assertEquals(10.0, repo.computeGlobalAverage(), 0.0001);

        // Est1: (10 + 20) / 2 = 15
        assertEquals(15.0, repo.computeAverageByStudent("Est1"), 0.0001);

        // Mat: (10 + 0) / 2 = 5
        assertEquals(5.0, repo.computeAverageBySubject("Mat"), 0.0001);

    }
}
