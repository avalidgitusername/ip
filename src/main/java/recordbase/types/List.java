package recordbase.types;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class List {
    private int listItemCounter = 0;

    // private final ListItem[] listItems;
    private  ArrayList<ListItem> listItems;

    public List() {
        this.listItems = new ArrayList<>();
    }

    public ListItem getItem(int index) {
        return (index >= 0 && index < this.listItems.size()) ? this.listItems.get(index) : null;
        // return this.listItems[index];
    }

    /**
     * Inserts an initialized ListItem into the current list.
     * @param item
     */
    public int addItem(ListItem item) {
        this.listItems.add(item);
        this.listItemCounter++;
        return this.listItemCounter-1;

        // if (this.listItemCounter < listItems.size() {
        //     this.listItems.add(new ListItem(task));
        //     this.listItemCounter++;
        //     return this.listItemCounter-1;
        // } else {
        //     System.out.println("List is full");
        //     return -1;
        // }
    }

    public String deleteItem(int index) {
        if (index >= 0 && index < this.listItems.size()) {
            // Temporary string to return initial value before deletion.
            String tmpStr = this.listItems.get(index).toString();
            this.listItems.remove(index);

            this.listItemCounter--;

            return tmpStr;
        } else {
            throw new RecordException("ListError: No such index to delete.");
        }
    }

    public int addEventItem(String task, LocalDateTime fromDate, LocalDateTime toDate) {
        this.listItems.add(new EventItem(task, fromDate, toDate));
        this.listItemCounter++;
        return this.listItemCounter-1;
        // if (this.listItemCounter < listItems.size()) {
        //     this.listItems.add(new EventItem(task, fromDate, toDate));
        //     this.listItemCounter++;
        //     return this.listItemCounter-1;
        // } else {
        //     System.out.println("List is full");
        //     return -1;
        // }
    }

    public int addDeadlineItem(String task, LocalDateTime byDate) {
        this.listItems.add(new DeadlineItem(task, byDate));
        this.listItemCounter++;
        return this.listItemCounter-1;
        // if (this.listItemCounter < listItems.size()) {
        //     this.listItems.add(new DeadlineItem(task, byDate));
        //     this.listItemCounter++;
        //     return this.listItemCounter-1;
        // } else {
        //     System.out.println("List is full");
        //     return -1;
        // }
    }

    public int addToDoItem(String task) {
        this.listItems.add(new ToDoItem(task));
        this.listItemCounter++;
        return this.listItemCounter-1;
        // if (this.listItemCounter < listItems.size()) {
        //     this.listItems.add(new ToDoItem(task));
        //     this.listItemCounter++;
        //     return this.listItemCounter-1;
        // } else {
        //     System.out.println("List is full");
        //     return -1;
        // }
    }

    public void setListItemDone(int index) {
        if (index < 0 || index >= this.listItemCounter) {
            throw new RecordException("Error in mark: No such item on list.");
        } else {
            this.listItems.get(index).setDone();
            System.out.println("Nice...You've marked the item done.");
            System.out.println(this.listItems.get(index));
        }
    }

    public void setListItemNotDone(int index) {
        if (index < 0 || index >= this.listItemCounter) {
            throw new RecordException("Error in unmark: No such item on list.");
        } else {
            this.listItems.get(index).setNotDone();
            
            System.out.println("Alright. Item marked as not done.");
            System.out.println(this.listItems.get(index));
        }
    }

    /**
     * Returns the entire listItems within the current list.
     * @return
     */
    public ArrayList<ListItem> getItems() {
        return this.listItems;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        if (!this.listItems.isEmpty()) {
            for (int i = 0; i < this.listItems.size(); i++) {
                sb.append(i+1);
                sb.append(". ");
                sb.append(this.listItems.get(i));
                sb.append("\n");
            }
        return sb.toString();
        } else {
            return "No items in list!";
        }
    }
    
}
