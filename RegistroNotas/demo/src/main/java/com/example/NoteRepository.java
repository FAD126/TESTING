package com.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NoteRepository {

    private final Map<Integer, Note> notesById = new HashMap<>();
    private int nextId = 1;

    public synchronized Note create(String title, String content) {
        int id = nextId++;
        Note note = new Note(id, title, content, LocalDateTime.now());
        notesById.put(id, note);
        return note;
    }

    public synchronized List<Note> findAll() {
        List<Note> result = new ArrayList<>(notesById.values());
        result.sort(Comparator.comparing(Note::getCreatedAt));
        return result;
    }

    public synchronized Optional<Note> findById(int id) {
        return Optional.ofNullable(notesById.get(id));
    }

    public synchronized boolean update(int id, String newTitle, String newContent) {
        Note existing = notesById.get(id);
        if (existing == null) {
            return false;
        }
        existing.setTitle(newTitle);
        existing.setContent(newContent);
        return true;
    }

    public synchronized boolean delete(int id) {
        return notesById.remove(id) != null;
    }

    public synchronized void clear() {
        notesById.clear();
        nextId = 1;
    }
}
