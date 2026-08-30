package recordbase.types;

import java.time.LocalDateTime;

public class EventItem extends ListItem {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    public EventItem(String taskDesc, LocalDateTime fromDate, LocalDateTime toDate) {
        super(taskDesc);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[E] ");

        sb.append(super.toString());

        String s1 = String.format(" (From: %s To: %s)", this.fromDate, this.toDate);
        sb.append(s1);
        return sb.toString();
    }

    @Override
    public String saveString() {
        return String.format("E, %s, '%s', '%s', '%s'", this.isDone ? "1" : "0",
            this.taskDesc, this.fromDate, this.toDate);
    }
}
