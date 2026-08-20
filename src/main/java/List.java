public class List {
    private int listItemCounter = 0;

    private final ListItem[] listItems;

    public List(int maxSize) {
        this.listItems = new ListItem[maxSize];
    }

    public boolean addItem(String task) {
        if (this.listItemCounter < listItems.length) {
            this.listItems[this.listItemCounter] = new ListItem(task);
            this.listItemCounter++;
            return true;
        } else {
            System.out.println("List is full");
            return false;
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < listItemCounter; i++) {
            sb.append(i+1);
            sb.append(". ");
            sb.append(this.listItems[i]);
            sb.append("\n");
        }
        return sb.toString();
    }
    
}
