
import java.time.LocalDateTime;

public class DeadlineItem extends ListItem {
    private LocalDateTime byDate;

    public DeadlineItem(String task, LocalDateTime byDate) {
        super(task);
        this.byDate = byDate;
    };

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Can do string formatting later.
        sb.append("[D] ");
        
        sb.append("[");
        sb.append(this.isDone ? "X" : " "); // Ternary operator
        sb.append("] ");

        sb.append(this.task);

        String s1 = String.format(" (Due: %s)", this.byDate);
        sb.append(s1);
        return sb.toString();
    }
    
    @Override
    public String saveString() {
        return String.format("D, %s, '%s', '%s'", this.isDone ? "1" : "0", this.task, this.byDate);
    }
}
