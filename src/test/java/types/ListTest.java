package types;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import recordbase.exceptions.RecordException;
import recordbase.types.List;
import recordbase.types.ListItem;

public class ListTest {
    @Test
    void deleteItem_removesCorrectItemAndUpdatesList() {
        List list = new List();

        list.addToDoItem("Read book");
        list.addDeadlineItem(
                "Submit report",
                LocalDateTime.of(2026, 9, 1, 23, 59));
        list.addEventItem(
                "Team meeting",
                LocalDateTime.of(2026, 9, 2, 10, 0),
                LocalDateTime.of(2026, 9, 2, 11, 0));

        String deletedItem = list.deleteItem(1);

        assertEquals("[D] [ ] Submit report (Due: 2026-09-01T23:59)",
                deletedItem);

        assertEquals(2, list.getItems().size());
        assertEquals("[T] [ ] Read book", list.getItem(0).toString());
        assertEquals(
                "[E] [ ] Team meeting (From: 2026-09-02T10:00 To: 2026-09-02T11:00)",
                list.getItem(1).toString());
    }

    @Test
    void deleteItem_withInvalidIndex_throwsRecordException() {
        List list = new List();
        assertThrows(RecordException.class, () -> list.deleteItem(-1));
        assertThrows(RecordException.class, () -> list.deleteItem(0));
        assertThrows(RecordException.class, () -> list.deleteItem(1));

        list.addToDoItem("Read book");

        assertThrows(RecordException.class, () -> list.deleteItem(-1));
        assertThrows(RecordException.class, () -> list.deleteItem(1));
    }

    @Test
    void deleteItem_withInvalidIndex2_throwsRecordException() {
        List list = new List();
        list.addToDoItem("Only item");

        RecordException negativeIndexException =
                assertThrows(RecordException.class, () -> list.deleteItem(-1));

        RecordException tooLargeIndexException =
                assertThrows(RecordException.class, () -> list.deleteItem(1));

        assertEquals(
                "ListError: No such index to delete.",
                negativeIndexException.getMessage());

        assertEquals(
                "ListError: No such index to delete.",
                tooLargeIndexException.getMessage());
    }
    @Test
    void addItem_addsItemAndReturnsCorrectIndex() {
        List list = new List();
        ListItem item = new ListItem("Generic task");

        int index = list.addItem(item);

        assertEquals(0, index);
        assertEquals(item, list.getItem(0));
        assertEquals(1, list.getItems().size());
    }

    @Test
    void addItem_returnsSequentialIndexes() {
        List list = new List();

        assertEquals(0, list.addItem(new ListItem("First")));
        assertEquals(1, list.addItem(new ListItem("Second")));
        assertEquals(2, list.addItem(new ListItem("Third")));

        assertEquals(3, list.getItems().size());
    }
    @Test
    void addToDoItem_addsToDoItemAndReturnsIndex() {
        List list = new List();

        int index = list.addToDoItem("Read book");

        assertEquals(0, index);
        assertEquals("[T] [ ] Read book", list.getItem(index).toString());
        assertEquals(1, list.getItems().size());
    }
    @Test
    void addDeadlineItem_addsDeadlineItemAndReturnsIndex() {
        List list = new List();
        LocalDateTime deadline = LocalDateTime.of(2026, 9, 1, 23, 59);

        int index = list.addDeadlineItem("Submit report", deadline);

        assertEquals(0, index);
        assertEquals(
                "[D] [ ] Submit report (Due: 2026-09-01T23:59)",
                list.getItem(index).toString());
        assertEquals(1, list.getItems().size());
    }
    @Test
    void addEventItem_addsEventItemAndReturnsIndex() {
        List list = new List();
        LocalDateTime from = LocalDateTime.of(2026, 9, 2, 10, 0);
        LocalDateTime to = LocalDateTime.of(2026, 9, 2, 11, 0);

        int index = list.addEventItem("Team meeting", from, to);

        assertEquals(0, index);
        assertEquals(
                "[E] [ ] Team meeting (From: 2026-09-02T10:00 To: 2026-09-02T11:00)",
                list.getItem(index).toString());
        assertEquals(1, list.getItems().size());
    }
    @Test
    void deleteItem_removesCorrectItemAndShiftsRemainingItems() {
        List list = new List();

        list.addToDoItem("First");
        list.addToDoItem("Second");
        list.addToDoItem("Third");

        String deletedItem = list.deleteItem(1);

        assertEquals("[T] [ ] Second", deletedItem);
        assertEquals(2, list.getItems().size());
        assertEquals("[T] [ ] First", list.getItem(0).toString());
        assertEquals("[T] [ ] Third", list.getItem(1).toString());
    }
    @Test
    void deleteItem_canDeleteFirstAndLastItems() {
        List list = new List();

        list.addToDoItem("First");
        list.addToDoItem("Second");
        list.addToDoItem("Third");

        assertEquals("[T] [ ] First", list.deleteItem(0));
        assertEquals("[T] [ ] Third", list.deleteItem(1));

        assertEquals(1, list.getItems().size());
        assertEquals("[T] [ ] Second", list.getItem(0).toString());
    }
    
    @Test
    void setListItemDone_marksItemAsDone() {
        List list = new List();
        list.addToDoItem("Complete assignment");

        list.setListItemDone(0);

        assertEquals("[T] [X] Complete assignment",
                list.getItem(0).toString());
    }
    @Test
    void setListItemNotDone_marksPreviouslyDoneItemAsNotDone() {
        List list = new List();
        list.addToDoItem("Complete assignment");

        list.setListItemDone(0);
        list.setListItemNotDone(0);

        assertEquals("[T] [ ] Complete assignment",
                list.getItem(0).toString());
    }
    @Test
    void setListItemDone_withInvalidIndex_throwsRecordException() {
        List list = new List();
        list.addToDoItem("Task");

        RecordException negativeIndexException =
                assertThrows(
                        RecordException.class,
                        () -> list.setListItemDone(-1));

        RecordException tooLargeIndexException =
                assertThrows(
                        RecordException.class,
                        () -> list.setListItemDone(1));

        assertEquals(
                "Error in mark: No such item on list.",
                negativeIndexException.getMessage());

        assertEquals(
                "Error in mark: No such item on list.",
                tooLargeIndexException.getMessage());
    }
    @Test
    void setListItemNotDone_withInvalidIndex_throwsRecordException() {
        List list = new List();
        list.addToDoItem("Task");

        RecordException negativeIndexException =
                assertThrows(
                        RecordException.class,
                        () -> list.setListItemNotDone(-1));

        RecordException tooLargeIndexException =
                assertThrows(
                        RecordException.class,
                        () -> list.setListItemNotDone(1));

        assertEquals(
                "Error in unmark: No such item on list.",
                negativeIndexException.getMessage());

        assertEquals(
                "Error in unmark: No such item on list.",
                tooLargeIndexException.getMessage());
    }
    @Test
    void getItem_returnsNullForInvalidIndex() {
        List list = new List();
        list.addToDoItem("Task");

        assertEquals(null, list.getItem(-1));
        assertEquals(null, list.getItem(1));
    }
    @Test
    void getItems_returnsAllItemsInInsertionOrder() {
        List list = new List();

        list.addToDoItem("First");
        list.addDeadlineItem(
                "Second",
                LocalDateTime.of(2026, 9, 1, 12, 0));
        list.addEventItem(
                "Third",
                LocalDateTime.of(2026, 9, 2, 10, 0),
                LocalDateTime.of(2026, 9, 2, 11, 0));

        ArrayList<ListItem> items = list.getItems();

        assertEquals(3, items.size());
        assertEquals("[T] [ ] First", items.get(0).toString());
        assertEquals(
                "[D] [ ] Second (Due: 2026-09-01T12:00)",
                items.get(1).toString());
        assertEquals(
                "[E] [ ] Third (From: 2026-09-02T10:00 To: 2026-09-02T11:00)",
                items.get(2).toString());
    }
    @Test
    void toString_returnsMessageForEmptyList() {
        List list = new List();

        assertEquals("No items in list!", list.toString());
    }
    @Test
    void toString_returnsNumberedItemsWithNewlines() {
        List list = new List();

        list.addToDoItem("First");
        list.addToDoItem("Second");

        assertEquals(
                "1. [T] [ ] First\n"
            + "2. [T] [ ] Second\n",
                list.toString());
    }

    @Test
    public void searchItems_matchingIsCaseInsensitive_returnsMatchingItem() {
        List list = new List();

        list.addToDoItem("Buy groceries");
        list.addToDoItem("Read John's book");
        list.addToDoItem("Watch \"The Matrix\"");
        list.addDeadlineItem("Finish CS2103T assignment", LocalDateTime.now());
        ArrayList<ListItem> results = list.searchItems("BUY GROCERIES");

        assertEquals(1, results.size());
        assertTrue(results.get(0).toString().contains("Buy groceries"));
    }

    @Test
    public void searchItems_partialMatch_returnsMatchingItems() {
        List list = new List();

        list.addToDoItem("Buy groceries");
        list.addToDoItem("Read John's book");
        list.addToDoItem("Watch \"The Matrix\"");
        list.addToDoItem("Finish CS2103T assignment");
        ArrayList<ListItem> results = list.searchItems("book");

        assertEquals(1, results.size());
        assertTrue(results.get(0).toString().contains("Read John's book"));
    }

    @Test
    public void searchItems_apostropheInSearchTerm_returnsMatchingItem() {
        List list = new List();

        list.addToDoItem("Buy groceries");
        list.addToDoItem("Read John's book");
        list.addToDoItem("Watch \"The Matrix\"");
        list.addToDoItem("Finish CS2103T assignment");
        ArrayList<ListItem> results = list.searchItems("John's book");

        assertEquals(1, results.size());
        assertTrue(results.get(0).toString().contains("Read John's book"));
    }

    @Test
    public void searchItems_doubleQuotesInSearchTerm_returnsMatchingItem() {
        List list = new List();

        list.addToDoItem("Buy groceries");
        list.addToDoItem("Read John's book");
        list.addToDoItem("Watch \"The Matrix\"");
        list.addToDoItem("Finish CS2103T assignment");
        ArrayList<ListItem> results = list.searchItems("\"The Matrix\"");

        assertEquals(1, results.size());
        assertTrue(results.get(0).toString().contains("Watch \"The Matrix\""));
    }

    @Test
    public void searchItems_multipleMatches_returnsAllMatchingItems() {
        List list = new List();

        list.addToDoItem("Buy groceries");
        list.addDeadlineItem("Read John's book", LocalDateTime.now());
        list.addToDoItem("Watch \"The Matrix\"");
        list.addToDoItem("Finish CS2103T assignment");
        list.addEventItem("Buy groceries for John's dinner", LocalDateTime.now(), LocalDateTime.now());

        ArrayList<ListItem> results = list.searchItems("OHN");

        assertEquals(2, results.size());
    }

    @Test
    public void searchItems_noMatch_returnsEmptyList() {
        List list = new List();

        ArrayList<ListItem> results = list.searchItems("nonexistent task");

        assertTrue(results.isEmpty());
    }

    @Test
    public void searchItems_exactMatch_returnsMatchingItem() {
        List list = new List();

        list.addToDoItem("Buy groceries");
        list.addToDoItem("Read John's book");
        list.addToDoItem("Watch \"The Matrix\"");
        list.addToDoItem("Finish CS2103T assignment");
        ArrayList<ListItem> results = list.searchItems("Finish CS2103T assignment");

        assertEquals(1, results.size());
        assertTrue(results.get(0).toString().contains("Finish CS2103T assignment"));
    }
}
