public class ToDoItem extends ListItem {
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

}
