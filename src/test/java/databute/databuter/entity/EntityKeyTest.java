package databute.databuter.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EntityKeyTest {

    private static final String KEY = "KEY";

    @Test
    public void testEquals() throws EmptyEntityKeyException {
        final EntityKey entityKey1 = new EntityKey(KEY);
        final EntityKey entityKey2 = new EntityKey(KEY);
        assertEquals(entityKey1, entityKey2);
    }

    @Test
    public void testHashCode() throws EmptyEntityKeyException {
        final EntityKey entityKey1 = new EntityKey(KEY);
        final EntityKey entityKey2 = new EntityKey(KEY);
        assertEquals(entityKey1.hashCode(), entityKey2.hashCode());
    }
}