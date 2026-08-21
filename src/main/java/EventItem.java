public class EventItem extends ListItem {

    private String fromDate;
    private String toDate;
    public EventItem(String task, String fromDate, String toDate) {
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

}
