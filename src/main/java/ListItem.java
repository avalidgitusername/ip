public class ListItem {
    private final String task;
    private boolean isDone = false;

    public ListItem(String task) {
        this.task = task;
    };

    public void setDone() {
        this.isDone = true;
    }

    public void setNotDone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.isDone ? "X" : " "); // Ternary operator
        sb.append("] ");
        sb.append(this.task);
        return sb.toString();
        // return "[" + this + "] " + this.task;
    }

}
