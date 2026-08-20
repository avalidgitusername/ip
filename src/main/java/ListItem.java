public class ListItem {
    private final String task;

    public ListItem(String task) {
        this.task = task;
    };

    @Override
    public String toString() {
        return this.task;
    }

}
