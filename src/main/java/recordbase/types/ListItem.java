package recordbase.types;

/**
 * Represents a generic task item in the Record application.
 */
public class ListItem {
    protected final String taskDesc;
    protected boolean isDone = false;

    /**
     * Creates a {@code ListItem} with the specified task description.
     *
     * @param task the description of the task
     */
    public ListItem(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    /**
     * Marks the tasks as completed.
     */
    public void setDone() {
        this.isDone = true;
    }

    /**
     * Marks the tasks as un-completed.
     */
    public void setNotDone() {
        this.isDone = false;
    }


    /**
     * Returns a string representation of the {@code ListItem} suitable for persistent storage.
     *
     * @return the string representation of this item for storage
     */
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
