package recordbase.types;
public class ListItem {
    protected final String taskDesc;
    protected boolean isDone = false;

    public ListItem(String taskDesc) {
        this.taskDesc = taskDesc;
    }

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
        sb.append(this.taskDesc);
        return sb.toString();
    }
}
