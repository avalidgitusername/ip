package recordbase.types;
import java.time.LocalDateTime;

/**
 * Represents a task that occurs over a specified period of time.
 * 
 * <p>A {@code EventItem} stores the task description together with its start and end date and time.</p>
 */
public class EventItem extends ListItem {

    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    /**
     * Constructs a {@code EventItem} with the specified task description, start date and time, end date and time.
     * 
     * @param task the description of the task
     * @param fromDate the date and time when the event starts
     * @param toDate the date and time when the event ends
     */
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
