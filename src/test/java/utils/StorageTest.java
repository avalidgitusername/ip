package utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import recordbase.types.RecordList;
import recordbase.utils.ListParser;
import recordbase.utils.Storage;

/** Tests persistence of fields that are not present in legacy save records. */
public class StorageTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    void saveAndLoad_scheduledToDo_preservesDateAndTime() {
        RecordList original = new RecordList();
        ListParser.parseToDo("todo Buy milk 20260115 14:30", original);
        Path saveFile = temporaryDirectory.resolve("tasks.txt");

        Storage.saveToFile(original, saveFile.toString());
        RecordList restored = new RecordList();
        Storage.loadFromFile(restored, saveFile.toString());

        assertTrue(restored.getItem(0).toString().contains("Scheduled: 15 Jan 2026, 2:30 PM"));
    }
}
