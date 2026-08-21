public class List {
    private int listItemCounter = 0;

    private final ListItem[] listItems;

    public List(int maxSize) {
        this.listItems = new ListItem[maxSize];
    }

    public ListItem getItem(int index) {
        return index < this.listItemCounter ? this.listItems[index] : null;
        // return this.listItems[index];
    }

    public int addItem(String task) {
        if (this.listItemCounter < listItems.length) {
            this.listItems[this.listItemCounter] = new ListItem(task);
            this.listItemCounter++;
            return this.listItemCounter-1;
        } else {
            System.out.println("List is full");
            return -1;
        }
    }
        public int addEventItem(String task, String fromDate, String toDate) {
        if (this.listItemCounter < listItems.length) {
            this.listItems[this.listItemCounter] = new EventItem(task, fromDate, toDate);
            this.listItemCounter++;
            return this.listItemCounter-1;
        } else {
            System.out.println("List is full");
            return -1;
        }
    }
        public int addDeadlineItem(String task, String byDate) {
        if (this.listItemCounter < listItems.length) {
            this.listItems[this.listItemCounter] = new DeadlineItem(task, byDate);
            this.listItemCounter++;
            return this.listItemCounter-1;
        } else {
            System.out.println("List is full");
            return -1;
        }
    }
        public int addToDoItem(String task) {
        if (this.listItemCounter < listItems.length) {
            this.listItems[this.listItemCounter] = new ToDoItem(task);
            this.listItemCounter++;
            return this.listItemCounter-1;
        } else {
            System.out.println("List is full");
            return -1;
        }
    }

    public void setListItemDone(int index) {
        if (index < 0 || index >= this.listItemCounter) {
            throw new RecordException("Error in mark: No such item on list.");
        } else {
            this.listItems[index].setDone();
            System.out.println("Nice...You've marked the item done.");
            System.out.println(this.listItems[index]);
        }
    }

    public void setListItemNotDone(int index) {
        if (index < 0 || index >= this.listItemCounter) {
            throw new RecordException("Error in unmark: No such item on list.");
        } else {
            this.listItems[index].setNotDone();
            
            System.out.println("Alright. Item marked as not done.");
            System.out.println(this.listItems[index]);
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
