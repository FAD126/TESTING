
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeHistoryRepository {

    private final Map<Integer, GradeHistoryRecord> byHistoryId = new HashMap<>();
    private final Map<Integer, List<GradeHistoryRecord>> byGradeId = new HashMap<>();

    private int nextHistoryId = 1;

    public synchronized GradeHistoryRecord add(int gradeId, int oldGrade, int newGrade) {
        int historyId = nextHistoryId++;
        GradeHistoryRecord record = new GradeHistoryRecord(historyId, gradeId, oldGrade, newGrade, LocalDateTime.now());
        byHistoryId.put(historyId, record);
        byGradeId.computeIfAbsent(gradeId, k -> new ArrayList<>()).add(record);
        return record;
    }

    public synchronized List<GradeHistoryRecord> findByGradeId(int gradeId) {
        List<GradeHistoryRecord> list = byGradeId.get(gradeId);
        if (list == null) {
            return new ArrayList<>();
        }
        List<GradeHistoryRecord> copy = new ArrayList<>(list);
        copy.sort(Comparator.comparing(GradeHistoryRecord::getChangedAt));
        return copy;
    }

    public synchronized void clear() {
        byHistoryId.clear();
        byGradeId.clear();
        nextHistoryId = 1;
    }
}
