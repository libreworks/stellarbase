package com.libreworks.stellarbase.persistence.criteria;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import com.libreworks.stellarbase.collections.Maps;

import org.junit.Before;
import org.junit.Test;

public class JunctionTest
{
    private Junction object;
    
    @Before
    public void setUp() throws Exception
    {
        object = Junction.or(fieldNamed("a").isNull(), fieldNamed("b").isNull());
    }

    @Test
    public void testEvaluate()
    {
        Junction object = Junction.and(fieldNamed("foo").eq(ValueExpression.of("bar")), fieldNamed("baz").eq(ValueExpression.of("foo")));
        assertTrue(object.evaluate(ImmutableMap.of("foo", "bar", "baz", "foo")));
        assertFalse(object.evaluate(ImmutableMap.of("foo", "bah", "baz", "foo")));
        assertFalse(object.evaluate(ImmutableMap.of("foo", "bar", "baz", "fop")));
        
        Junction object2 = Junction.or(fieldNamed("foo").isNull(), fieldNamed("bar").isNull());
        assertFalse(object2.evaluate(ImmutableMap.of("foo", "b", "bar", "a")));
        assertTrue(object2.evaluate(Maps.newLinked("foo", null).add("bar", "a").toMap()));
        assertTrue(object2.evaluate(Maps.newLinked("foo", "a").add("bar", null).toMap()));
    }

    @Test
    public void testAnd()
    {
        Junction object = Junction.and(fieldNamed("foo").eq(ValueExpression.of("bar")), fieldNamed("baz").eq(ValueExpression.of("foo")));
        assertTrue(object.isConjunction());
    }

    @Test
    public void testOr()
    {
        Junction object = Junction.or(fieldNamed("foo").eq(ValueExpression.of("bar")), fieldNamed("baz").eq(ValueExpression.of("foo")));
        assertFalse(object.isConjunction());
    }

    @Test
    public void testGetSymbols()
    {
        assertTrue(object.getSymbols().contains(fieldNamed("a").isNull()));
        assertTrue(object.getSymbols().contains(fieldNamed("b").isNull()));
    }

    @Test
    public void testIsConjunction()
    {
        assertFalse(object.isConjunction());
        Junction other = Junction.and(fieldNamed("a").eq(ValueExpression.of("b")), fieldNamed("b").eq(ValueExpression.of("c")));
        assertTrue(other.isConjunction());
    }

    @Test
    public void testIterator()
    {
        assertNotNull(object.iterator());
    }

    @Test
    public void testToString()
    {
        assertEquals("( a IS NULL OR b IS NULL )", object.toString());
    }

    private Field<?> fieldNamed(String name)
    {
    	return new FieldImpl<Object>(name, Object.class);
    }
}
