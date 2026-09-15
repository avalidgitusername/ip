package recordbase.types;

/**
 * Represents a generic task item in the Record application.
 */
public abstract class ListItem {
    protected final String taskDesc;
    protected final Priority priority;
    protected boolean isDone = false;

    /**
     * Creates a {@code ListItem} with the specified task description.
     *
     * @param task the description of the item
     */
    public ListItem(String taskDesc) {
        this(taskDesc, Priority.MEDIUM);
    }

    /**
     * Creates a {@code ListItem} with the specified task description and priority.
     *
     * @param taskDesc the description of the task
     * @param priority the priority of the task
     */
    public ListItem(String taskDesc, Priority priority) {
        assert taskDesc != null : "Task description must not be null";
        assert priority != null : "Priority must not be null";

        this.taskDesc = taskDesc;
        this.priority = priority;
    }

    /**
     * Returns this task's priority.
     *
     * @return task priority
     */
    public Priority getPriority() {
        return priority;
    }

    /** Returns the user-provided description of this task. */
    public String getTaskDescription() {
        return taskDesc;
    }

    /** Returns whether this task is currently completed. */
    public boolean isDone() {
        return isDone;
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
        sb.append(String.format(" [Priority: %s (%d)]", priority, priority.getLevel()));
        return sb.toString();
    }
}
