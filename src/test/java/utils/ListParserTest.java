package utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import recordbase.exceptions.RecordException;
import recordbase.types.RecordList;
import recordbase.utils.ListParser;

public class ListParserTest {
    private RecordList list;

    @BeforeEach
    void setUp() {
        list = new RecordList();
    }

    @Test
    void parseToDo_validCommandWithDate_itemAdded() {
        String command = "todo Buy milk 20260115 14:30";

        int index = ListParser.parseToDo(command, list);

        assertTrue(index == 0);
    }

    @Test
    void parseToDo_validCommandWithoutTime_itemAdded() {
        String command = "todo Buy milk 20260115";

        int index = ListParser.parseToDo(command, list);

        assertTrue(index == 0);
    }

    @Test
    void parseToDo_validCommand_itemAdded() {
        String command = "todo Buy milk";

        int index = ListParser.parseToDo(command, list);

        assertTrue(index == 0);
    }

    @Test
    void parseToDo_validCommandWithSpecialChars_itemAdded() {
        String command = "todo Buy milk '\"\'\"}><./where";

        int index = ListParser.parseToDo(command, list);

        assertTrue(index == 0);
    }

    @Test
    void parseToDo_nullCommand_exceptionThrown() {
        assertThrows(NullPointerException.class, () -> ListParser.parseToDo(null, list));
    }

    @Test
    void parseToDo_noTaskGivenWithSpace_exceptionThrown() {
        String command = "todo ";

        assertThrows(RecordException.class, () -> ListParser.parseToDo(command, list));
    }

    @Test
    void parseToDo_noTaskGivenNoSpace_exceptionThrown() {
        String command = "todo";

        assertThrows(RecordException.class, () -> ListParser.parseToDo(command, list));
    }

    @Test
    void parseDeadline_validDateAndTime_itemAdded() {
        String command = "deadline Submit report /by 20260115 14:30";

        int index = ListParser.parseDeadline(command, list);

        assertTrue(index == 0);
    }

    @Test
    void parseDeadline_validDateWithoutTime_itemAdded() {
        String command = "deadline Submit report /by 20260115";

        int index = ListParser.parseDeadline(command, list);

        assertTrue(index == 0);
    }

    @Test
    void parseDeadline_invalidNoDateTime_exceptionThrown() {
        String command = "deadline Submit report";

        assertThrows(RecordException.class, () -> ListParser.parseDeadline(command, list));
    }

    @Test
    void parseDeadline_invalidCalendarDate_exceptionThrown() {
        // February 30 is not a valid date.
        String command = "deadline Submit report /by 20260230 14:30";

        assertThrows(java.time.format.DateTimeParseException.class, () -> ListParser
                .parseDeadline(command, list));
    }

    @Test
    void parseDeadline_invalidTime_exceptionThrown() {
        // 25:00 is not a valid time.
        String command = "deadline Submit report /by 20260115 25:00";

        assertThrows(java.time.format.DateTimeParseException.class, () -> ListParser
                .parseDeadline(command, list));
    }

    @Test
    void parseDeadline_invalidDateFormat_exceptionThrown() {
        String command = "deadline Submit report /by 15-01-2026 14:30";

        assertThrows(RecordException.class, () -> ListParser.parseDeadline(command, list));
    }

    @Test
    void parseEvent_validDateAndTime_itemAdded() {
        String command = "event Team meeting /from 20260115 14:30 /to 20260115 15:30";

        int index = ListParser.parseEvent(command, list);

        assertTrue(index == 0);
    }

    @Test
    void parseEvent_validDatesWithoutTimes_itemAdded() {
        String command = "event Conference /from 20260115 /to 20260116";

        int index = ListParser.parseEvent(command, list);

        assertTrue(index == 0);
    }

    @Test
    void parseEvent_validFromDateToDateTime_itemAdded() {
        String command = "event Video session /from 20260115 /to 20260115 10:30";

        int index = ListParser.parseEvent(command, list);

        assertTrue(index == 0);
    }

    @Test
    void parseEvent_validFromDateTimeToDate_itemAdded() {
        String command = "event Running session /from 20260115 09:00 /to 20260115";

        int index = ListParser.parseEvent(command, list);

        assertTrue(index == 0);
    }

    @Test
    void parseEvent_invalidCommand_exceptionThrown() {
        String command = "event Team meeting";

        assertThrows(RecordException.class, () -> ListParser.parseEvent(command, list));
    }

    @Test
    void parseEvent_invalidStartDate_exceptionThrown() {
        String command = "event Team meeting /from 20261301 14:30 /to 20260115 15:30";

        assertThrows(java.time.format.DateTimeParseException.class, () -> ListParser
                .parseEvent(command, list));
    }

    @Test
    void parseEvent_invalidEndTime_exceptionThrown() {
        String command = "event Team meeting /from 20260115 14:30 /to 20260115 24:00";

        assertThrows(java.time.format.DateTimeParseException.class, () -> ListParser
                .parseEvent(command, list));
    }

    @Test
    void parseEvent_validEndBeforeStart_itemHandlingIsVerified() {
        String command = "event Team meeting /from 20260115 15:30 /to 20260115 14:30";

        /*
         * Whether this should succeed depends on your requirements.
         * If reversed events are invalid, this test should expect an exception.
         */
        assertDoesNotThrow(() -> ListParser.parseEvent(command, list));

        int index = ListParser.parseEvent(command, list);

        // Index 1 as assertDoesNotThrow should have added 1 also
        assertTrue(index == 1);
    }

    @Test
    void parseEvent_invalidDoubleFrom_itemHandlingIsVerified() {
        String command = "event Team meeting /from /from 20260115 15:30 /to /to 20260115 14:30";

        assertThrows(RecordException.class, () -> ListParser.parseEvent(command, list));
    }

    @Test
    void parseEvent_invalidDoubleTo_itemHandlingIsVerified() {
        String command = "event Team meeting /from /from 20260115 15:30 /to /to 20260115 14:30";

        assertThrows(RecordException.class, () -> ListParser.parseEvent(command, list));
    }
}
