package databute.databuter.entry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EntryKeyTest {

    private static final String KEY = "KEY";

    @Test
    public void testEquals() throws EmptyEntryKeyException {
        final EntryKey entryKey1 = new EntryKey(KEY);
        final EntryKey entryKey2 = new EntryKey(KEY);
        assertEquals(entryKey1, entryKey2);
    }

    @Test
    public void testHashCode() throws EmptyEntryKeyException {
        final EntryKey entryKey1 = new EntryKey(KEY);
        final EntryKey entryKey2 = new EntryKey(KEY);
        assertEquals(entryKey1.hashCode(), entryKey2.hashCode());
    }
}