
import java.time.LocalDateTime;
import java.util.Objects;

public class GradeHistoryRecord {

    private final int historyId;
    private final int gradeId;

    private final int oldGrade;
    private final int newGrade;

    private final LocalDateTime changedAt;

    public GradeHistoryRecord(int historyId, int gradeId, int oldGrade, int newGrade, LocalDateTime changedAt) {
        this.historyId = historyId;
        this.gradeId = gradeId;
        this.oldGrade = oldGrade;
        this.newGrade = newGrade;
        this.changedAt = changedAt;
    }

    public int getHistoryId() {
        return historyId;
    }

    public int getGradeId() {
        return gradeId;
    }

    public int getOldGrade() {
        return oldGrade;
    }

    public int getNewGrade() {
        return newGrade;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GradeHistoryRecord)) {
            return false;
        }
        GradeHistoryRecord other = (GradeHistoryRecord) o;
        return historyId == other.historyId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyId);
    }
}
