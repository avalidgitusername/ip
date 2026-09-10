package recordbase.types;

/**
 * Represents a generic task item in the Record application.
 */
public abstract class ListItem {
    protected final String taskDesc;
    protected boolean isDone = false;

    /**
     * Creates a {@code ListItem} with the specified task description.
     *
     * @param task the description of the item
     */
    public ListItem(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    /**
     * Marks the item as completed.
     */
    public void setDone() {
        this.isDone = true;
    }

    /**
     * Marks the item as un-completed.
     */
    public void setNotDone() {
        this.isDone = false;
    }


    /**
     * Returns a string representation of the {@code ListItem} suitable for persistent storage.
     *
     * @return the string representation of this item for storage
     */
    public abstract String saveString();

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
