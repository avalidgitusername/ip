package recordbase.types;

public class ToDoItem extends ListItem {
    public ToDoItem(String taskDesc) {
        super(taskDesc);
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
        return String.format("T, %s, '%s'", this.isDone ? "1" : "0", this.taskDesc);
    }
}
