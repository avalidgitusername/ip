package recordbase.types;

/**
 * Represents a task without a specified deadline or duration.
 */
public class ToDoItem extends ListItem {
    /**
     * Constructs a {@code ToDoItem} with the specified task description.
     *
     * @param task the description of the task
     */
    public ToDoItem(String taskDesc) {
        this(taskDesc, Priority.MEDIUM);
    }

    /**
     * Constructs a {@code ToDoItem} with the specified description and priority.
     *
     * @param taskDesc the description of the task
     * @param priority the priority of the task
     */
    public ToDoItem(String taskDesc, Priority priority) {
        super(taskDesc, priority);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[T] ");

        sb.append(super.toString());

        return sb.toString();
    }

    @Override
    public String saveString() {
        return String.format("T, %s, %d, '%s'", this.isDone ? "1" : "0",
                priority.getLevel(), this.taskDesc);
    }
}
