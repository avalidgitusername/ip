import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public int addItem(String task) {
        this.listItems.add(new ListItem(task));
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

    // Creates all parent folders, and saves entire list into a given file with a "CSV" like file structure.
    public void saveToFile(String strPath) {
        Path path = Paths.get(strPath);
        if (Files.notExists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                System.out.println("Unable to do something with creating directories");
                // e.printStackTrace();
            }
        }
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))) {
            for (ListItem item : this.listItems) {
                // Escapt the apostrophe to prevent errors.
                String tempStr = item.saveString().replaceAll("\'", "\\\'");
                writer.write(tempStr);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to do something with bufferedwriter");
            // e.printStackTrace();
        }
    }

    public void loadFromFile(String strPath) {
        Path path = Paths.get(strPath);
        if (Files.notExists(path)) {
           // Do nothing.
           throw new RecordException("No save file to load from.");
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                switch (tempStr.charAt(0)) {
                    // For all cases, we add the task from the 1st instance of "'" found
                    case 'T' -> {
                        int taskIndexStart = tempStr.indexOf(", '");
                        int taskIndexEnd = tempStr.lastIndexOf("'");
                        
                        String task = tempStr.substring(taskIndexStart + 3, taskIndexEnd);
                        addToDoItem(task);
                        if (tempStr.charAt(3) == '1') {
                            this.listItems.getLast().setDone();
                        }
                        break;
                    }
                    case 'D' -> {
                        int taskIndexStart = tempStr.indexOf(", '");
                        int taskIndexEnd = tempStr.indexOf("', ", taskIndexStart + 3);

                        int taskByDateStart = tempStr.indexOf(", '", taskIndexEnd + 1);
                        int taskByDateEnd = tempStr.lastIndexOf("'");

                        String task = tempStr.substring(taskIndexStart + 3, taskIndexEnd);
                        String byDateStr = tempStr.substring(taskByDateStart + 3, taskByDateEnd);
                        LocalDateTime byDate = LocalDateTime.parse(byDateStr);
                        addDeadlineItem(task, byDate);
                        if (tempStr.charAt(3) == '1') {
                            this.listItems.getLast().setDone();
                        }
                        break;
                    }
                    case 'E' -> {
                        int taskIndexStart = tempStr.indexOf(", '");
                        int taskIndexEnd = tempStr.indexOf("', ", taskIndexStart + 3);

                        int taskFromDateStart = tempStr.indexOf(", '", taskIndexEnd + 1);
                        int taskFromDateEnd = tempStr.indexOf("', ", taskFromDateStart + 3);

                        int taskToDateStart = tempStr.indexOf(", '", taskFromDateEnd + 1);
                        int taskToDateEnd = tempStr.lastIndexOf("'");

                        String task = tempStr.substring(taskIndexStart + 3, taskIndexEnd);
                        String fromDateStr = tempStr.substring(taskFromDateStart + 3, taskFromDateEnd);
                        String toDateStr = tempStr.substring(taskToDateStart + 3, taskToDateEnd);
                        LocalDateTime fromDate = LocalDateTime.parse(fromDateStr);
                        LocalDateTime toDate = LocalDateTime.parse(toDateStr);
                        addEventItem(task, fromDate, toDate);
                        if (tempStr.charAt(3) == '1') {
                            this.listItems.getLast().setDone();
                        }
                        break;
                    }
                    default -> {
                        break;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Unable to do something with bufferedreader");
            // e.printStackTrace();
        }
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
