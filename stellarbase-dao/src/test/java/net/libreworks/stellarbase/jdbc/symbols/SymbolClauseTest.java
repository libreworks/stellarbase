package net.libreworks.stellarbase.jdbc.symbols;

import static org.junit.Assert.*;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;

public class SymbolClauseTest
{
    private SymbolClause<Field> object;
    
    @Before
    public void setUp() throws Exception
    {
        object = new SymbolClause<Field>();
    }

    @Test
    public void testHashCode()
    {
        object.add(new Field("foo"));
        int hashCode = new HashCodeBuilder(3, 17)
            .append(object.items)
            .toHashCode();
        assertEquals(hashCode, object.hashCode());
    }

    @Test
    public void testAdd()
    {
        Field f = new Field("f");
        assertSame(object, object.add(f));
        assertTrue(object.items.contains(f));
    }

    @Test
    public void testEqualsObject()
    {
        assertTrue(object.equals(object));
        assertFalse(object.equals(null));
        assertFalse(object.equals(""));
        
        SymbolClause<Field> other = new SymbolClause<Field>();
        assertTrue(object.equals(other));
        
        Field f = new Field("foobar");
        object.add(f);
        assertFalse(object.equals(other));
        other.add(f);
        assertTrue(object.equals(other));
    }

    @Test
    public void testGetSymbols()
    {
        assertTrue(object.getSymbols().isEmpty());
        Field f = new Field("foobar");
        object.add(f);
        assertTrue(object.getSymbols().contains(f));
    }

    @Test
    public void testIsEmpty()
    {
        assertTrue(object.isEmpty());
        Field f = new Field("foobar");
        object.add(f);
        assertFalse(object.isEmpty());
    }

    @Test
    public void testIterator()
    {
        Field f = new Field("foobar");
        object.add(f);
        assertNotNull(object.iterator());
        
    }

    @Test
    public void testMerge()
    {
        SymbolClause<Field> fields = new SymbolClause<Field>();
        for(int i=0; i<5; i++) {
            fields.add(new Field("test" + i));
        }
        assertTrue(object.isEmpty());
        assertSame(object, object.merge(fields));
        assertFalse(object.isEmpty());
    }

    @Test
    public void testRemove()
    {
        Field field = new Field("test1");
        object.add(field);
        assertTrue(object.items.contains(field));
        object.remove(field);
        assertFalse(object.items.contains(field));
    }

    @Test
    public void testSize()
    {
        assertEquals(0, object.size());
        Field field = new Field("test");
        object.add(field);
        assertEquals(1, object.size());
    }

    @Test
    public void testToString()
    {
        Field a = new Field("foo");
        Field b = new Field("bar");
        object.add(a);
        object.add(b);
        assertEquals("foo, bar", object.toString());
    }
}
