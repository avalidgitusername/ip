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
    public ToDoItem(String task) {
        super(task);
    };

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[T] ");
        
        sb.append("[");
        sb.append(this.isDone ? "X" : " "); // Ternary operator
        sb.append("] ");
        sb.append(this.task);
        return sb.toString();
    }

    @Override
    public String saveString() {
        return String.format("T, %s, '%s'", this.isDone ? "1" : "0", this.task);
    }
}
