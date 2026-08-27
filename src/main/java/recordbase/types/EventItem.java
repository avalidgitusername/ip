package recordbase.types;
import java.time.LocalDateTime;

public class EventItem extends ListItem {

    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    public EventItem(String task, LocalDateTime fromDate, LocalDateTime toDate) {
        super(task);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[E] ");

        sb.append("[");
        sb.append(this.isDone ? "X" : " "); // Ternary operator
        sb.append("] ");
        sb.append(this.task);

        String s1 = String.format(" (From: %s To: %s)", this.fromDate, this.toDate);
        sb.append(s1);
        return sb.toString();
    }

    @Override
    public String saveString() {
        return String.format("E, %s, '%s', '%s', '%s'", this.isDone ? "1" : "0", this.task, this.fromDate, this.toDate);
    }
}
