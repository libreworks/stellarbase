package com.libreworks.stellarbase.jdbc.symbols;

import static org.junit.Assert.*;

import java.util.Collection;

import com.libreworks.stellarbase.collections.FluentValues;

import org.junit.Before;
import org.junit.Test;

public class JunctionTest
{
    private Junction object;
    
    @Before
    public void setUp() throws Exception
    {
        object = new Junction(Expression.isNull("a"), Expression.isNull("b"), false);
    }

    @Test
    public void testEvaluate()
    {
        Junction object = Junction.and(Expression.eq("foo", "bar"), Expression.eq("baz", "foo"));
        assertTrue(object.evaluate(new FluentValues().set("foo", "bar").set("baz", "foo")));
        assertFalse(object.evaluate(new FluentValues().set("foo", "bah").set("baz", "foo")));
        assertFalse(object.evaluate(new FluentValues().set("foo", "bar").set("baz", "fop")));
        
        Junction object2 = Junction.or(Expression.isNull("foo"), Expression.isNull("bar"));
        assertFalse(object2.evaluate(new FluentValues().set("foo", "b").set("bar", "a")));
        assertTrue(object2.evaluate(new FluentValues().set("foo", null).set("bar", "a")));
        assertTrue(object2.evaluate(new FluentValues().set("foo", "a").set("bar", null)));
    }

    @Test
    public void testAnd()
    {
        Junction object = Junction.and(Expression.eq("foo", "bar"), Expression.eq("baz", "foo"));
        assertTrue(object.isConjunction());
    }

    @Test
    public void testOr()
    {
        Junction object = Junction.or(Expression.eq("foo", "bar"), Expression.eq("baz", "foo"));
        assertFalse(object.isConjunction());
    }

    @Test
    public void testAdd()
    {
        Expression e = Expression.lt("thing", 5);
        assertSame(object, object.add(e));
        assertTrue(object.criteria.contains(e));
    }

    @Test
    public void testGetAllFields()
    {
        Collection<Field> fields = object.getAllFields();
        assertTrue(fields.contains(new Field("a")));
        assertTrue(fields.contains(new Field("b")));
    }
    
    @Test
    public void testGetSymbols()
    {
        assertTrue(object.getSymbols().containsAll(object.criteria));
    }

    @Test
    public void testIsConjunction()
    {
        assertFalse(object.isConjunction());
        Junction other = new Junction(Expression.eq("a", "b"), Expression.eq("b", "c"), true);
        assertTrue(other.isConjunction());
    }

    @Test
    public void testIsEmpty()
    {
        assertFalse(object.isEmpty());
        object.criteria.clear();
        assertTrue(object.isEmpty());
    }

    @Test
    public void testIterator()
    {
        assertNotNull(object.iterator());
    }

    @Test
    public void testRemove()
    {
        Expression e = Expression.eq("foo", "bar");
        object.add(e);
        assertTrue(object.getSymbols().contains(e));
        object.remove(e);
        assertFalse(object.getSymbols().contains(e));
    }

    @Test
    public void testSize()
    {
        assertEquals(2, object.size());
        object.add(Expression.isNull("c"));
        assertEquals(3, object.size());
    }

    @Test
    public void testToString()
    {
        assertEquals("( a IS NULL OR b IS NULL )", object.toString());
    }
}
