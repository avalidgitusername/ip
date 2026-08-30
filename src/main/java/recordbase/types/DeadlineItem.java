package recordbase.types;

import java.time.LocalDateTime;

public class DeadlineItem extends ListItem {
    private LocalDateTime byDate;

    public DeadlineItem(String taskDesc, LocalDateTime byDate) {
        super(taskDesc);
        this.byDate = byDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[D] ");

        sb.append(super.toString());

        String s1 = String.format(" (Due: %s)", this.byDate);
        sb.append(s1);
        return sb.toString();
    }

    @Override
    public String saveString() {
        return String.format("D, %s, '%s', '%s'", this.isDone ? "1" : "0", this.taskDesc, this.byDate);
    }
}
