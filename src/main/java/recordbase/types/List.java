package recordbase.types;

import java.time.LocalDateTime;
import java.util.ArrayList;

import recordbase.exceptions.RecordException;

/**
 * Represents a collection of {@code ListItem} objects and provides operations for managing the items in the list.
 *
 * <p>The list supports adding, removing, retrieving, and updating the completion status of items.</p>
 */

public class List {
    private int listItemCounter = 0;
    private ArrayList<ListItem> listItems;

    /**
     * Creates an empty {@code List}.
     */
    public List() {
        this.listItems = new ArrayList<>();
    }

    /**
     * Returns the {@code ListItem} at the specified index in the list.
     *
     * @param index the index of the item to retrieve
     * @return the {@code ListItem} at the specified index, or {@code null} if the index is out of bounds
     */
    public ListItem getItem(int index) {
        return (index >= 0 && index < this.listItems.size()) ? this.listItems.get(index) : null;
    }

    /**
     * Adds the specified item to the end of the list.
     *
     * @param item the item to add to the list
     * @return the index of the newly created item
     */
    public int addItem(ListItem item) {
        this.listItems.add(item);
        this.listItemCounter++;

        return this.listItemCounter - 1;
    }

    /**
     * Removes the item at the specified index from the list.
     *
     * @param index the index of the item to remove
     * @return the string description of the removed item
     * @throws RecordException if the specified index is out of bounds
     */
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

    /**
     * Adds an {@code EventItem} with the specified task description and event duration to end of the list.
     *
     * @param task the description of the task
     * @param fromDate the date and time when the event starts
     * @param toDate the date and time when the event ends
     * @return the index of the newly added event item
     */
    public int addEventItem(String task, LocalDateTime fromDate, LocalDateTime toDate) {
        this.listItems.add(new EventItem(task, fromDate, toDate));
        this.listItemCounter++;

        return this.listItemCounter - 1;
    }
    /**
     * Adds an {@code DeadlineItem} with the specified task description and deadline to end of the list.
     *
     * @param task the description of the task
     * @param byDate the date and time which the task should be completed
     * @return the index of the newly added deadline item
     */
    public int addDeadlineItem(String task, LocalDateTime byDate) {
        this.listItems.add(new DeadlineItem(task, byDate));
        this.listItemCounter++;

        return this.listItemCounter - 1;
    }
    /**
     * Adds an {@code ToDoItem} with the specified task description to end of the list.
     *
     * @param task the description of the task
     * @return the index of the newly added to-do item
     */
    public int addToDoItem(String task) {
        this.listItems.add(new ToDoItem(task));
        this.listItemCounter++;

        return this.listItemCounter - 1;
    }

    /**
     * Marks the item at the specified index as done.
     *
     * @param index the index of the item to mark as done
     * @throws RecordException if the specified index is out of bounds
     */
    public void setListItemDone(int index) {
        if (index < 0 || index >= this.listItemCounter) {
            throw new RecordException("Error in mark: No such item on list.");
        } else {
            this.listItems.get(index).setDone();
            System.out.println("Nice...You've marked the item done.");
            System.out.println(this.listItems.get(index));
        }
    }

    /**
     * Marks the item at the specified index as not done.
     *
     * @param index the index of the item to mark as not done
     * @throws RecordException if the specified index is out of bounds
     */
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
     * Returns all items in the current list.
     *
     * @return the {@code ArrayList} containing all items in the list
     */
    public ArrayList<ListItem> getItems() {
        return this.listItems;
    }

    /**
     * Searches the description of all tasks in the current list for a specific string.
     * @param searchStr
     * @return An ArrayList of all matching ListItems
     */
    public ArrayList<ListItem> searchItems(String searchStr) {
        ArrayList<ListItem> matches = new ArrayList<>();
        String searchTermLowercase = searchStr.toLowerCase();

        if (this.listItems != null) {
            for (ListItem item : this.listItems) {
                if (((ListItem) item).toString().toLowerCase().contains(searchTermLowercase)) {
                    matches.add(item);
                }
            }
        }

        return matches;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        if (!this.listItems.isEmpty()) {
            for (int i = 0; i < this.listItems.size(); i++) {
                sb.append(i + 1);
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
