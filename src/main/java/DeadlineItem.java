public class DeadlineItem extends ListItem {
    private String byDate;

    public DeadlineItem(String task, String byDate) {
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

}
