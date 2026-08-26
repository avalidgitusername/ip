public class ListItem {
    protected final String task;
    protected boolean isDone = false;

    public ListItem(String task) {
        this.task = task;
    };

    public void setDone() {
        this.isDone = true;
    }

    public void setNotDone() {
        this.isDone = false;
    }

    // Returns a string representation for saving into a file
    public String saveString() {
        return "Stub String";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.isDone ? "X" : " "); // Ternary operator
        sb.append("] ");
        sb.append(this.task);
        return sb.toString();
    }

}
