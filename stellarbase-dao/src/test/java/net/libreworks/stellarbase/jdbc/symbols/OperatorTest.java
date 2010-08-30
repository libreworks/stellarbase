package net.libreworks.stellarbase.jdbc.symbols;

import static org.junit.Assert.*;

import org.junit.Test;

public class OperatorTest
{
    @Test
    public void testGetSql()
    {
        assertEquals("=", Operator.eq.getSql());
        assertEquals("<>", Operator.neq.getSql());
        assertEquals(">=", Operator.ge.getSql());
        assertEquals("<=", Operator.le.getSql());
        assertEquals(">", Operator.gt.getSql());
        assertEquals("<", Operator.lt.getSql());
        assertEquals("LIKE", Operator.like.getSql());
        assertEquals("NOT LIKE", Operator.notLike.getSql());
        assertEquals("IN", Operator.in.getSql());
        assertEquals("NOT IN", Operator.notIn.getSql());
        assertEquals("IS", Operator.is.getSql());
        assertEquals("IS NOT", Operator.isNot.getSql());
        assertEquals("BETWEEN", Operator.between.getSql());
        assertEquals("NOT BETWEEN", Operator.notBetween.getSql());
    }

    @Test
    public void testEvaluateEq()
    {
        assertTrue(Operator.eq.evaluate("foo", "foo"));
        assertFalse(Operator.eq.evaluate("foo", "bar"));
    }
    
    @Test
    public void testEvaluateNeq()
    {
        assertFalse(Operator.neq.evaluate("foo", "foo"));
        assertTrue(Operator.neq.evaluate("foo", "bar"));
    }
    
    @Test
    public void testEvaluateIs()
    {
        assertTrue(Operator.is.evaluate(null, null));
        assertFalse(Operator.is.evaluate("foo", "bar"));
    }
    
    @Test
    public void testEvaluateIsNot()
    {
        assertFalse(Operator.isNot.evaluate(null, null));
        assertTrue(Operator.isNot.evaluate("foo", "bar"));
    }
    
    @Test
    public void testEvaluateGt()
    {
        assertTrue(Operator.gt.evaluate(5, 4));
        assertFalse(Operator.gt.evaluate(4, 5));
        assertFalse(Operator.gt.evaluate(4, 4));
    }
    
    @Test
    public void testEvaluateLt()
    {
        assertTrue(Operator.lt.evaluate(4, 5));
        assertFalse(Operator.lt.evaluate(5, 4));
        assertFalse(Operator.lt.evaluate(4, 4));
    }
    
    @Test
    public void testEvaluateGe()
    {
        assertTrue(Operator.ge.evaluate(5, 4));
        assertFalse(Operator.ge.evaluate(4, 5));
        assertTrue(Operator.ge.evaluate(4, 4));
    }
    
    @Test
    public void testEvaluateLe()
    {
        assertTrue(Operator.le.evaluate(4, 5));
        assertFalse(Operator.le.evaluate(5, 4));
        assertTrue(Operator.le.evaluate(4, 4));
    }
    
    @Test
    public void testEvaluateLike()
    {
        assertTrue(Operator.like.evaluate("Spatula", "Spat%"));
        assertTrue(Operator.like.evaluate("Spatula", "%ula"));
        assertTrue(Operator.like.evaluate("Spatula", "%atul%"));
        assertFalse(Operator.like.evaluate("Knife", "Spat%"));
        assertFalse(Operator.like.evaluate("Knife", "%ula"));
        assertFalse(Operator.like.evaluate("Knife", "%atul%"));
    }
    
    @Test
    public void testEvaluateNotLike()
    {
        assertFalse(Operator.notLike.evaluate("Spatula", "Spat%"));
        assertFalse(Operator.notLike.evaluate("Spatula", "%ula"));
        assertFalse(Operator.notLike.evaluate("Spatula", "%atul%"));
        assertTrue(Operator.notLike.evaluate("Knife", "Spat%"));
        assertTrue(Operator.notLike.evaluate("Knife", "%ula"));
        assertTrue(Operator.notLike.evaluate("Knife", "%atul%"));
    }
    
    @Test
    public void testEvaluateBetween()
    {
        assertTrue(Operator.between.evaluate(5, new Object[]{1, 10}));
        assertTrue(Operator.between.evaluate(1, new Object[]{1, 10}));
        assertTrue(Operator.between.evaluate(10, new Object[]{1, 10}));
        assertFalse(Operator.between.evaluate(-4, new Object[]{1, 10}));
        assertFalse(Operator.between.evaluate(15, new Object[]{1, 10}));
    }
    
    @Test
    public void testEvaluateNotBetween()
    {
        assertFalse(Operator.notBetween.evaluate(5, new Object[]{1, 10}));
        assertFalse(Operator.notBetween.evaluate(1, new Object[]{1, 10}));
        assertFalse(Operator.notBetween.evaluate(10, new Object[]{1, 10}));
        assertTrue(Operator.notBetween.evaluate(-4, new Object[]{1, 10}));
        assertTrue(Operator.notBetween.evaluate(15, new Object[]{1, 10}));
    }
    
    @Test
    public void testEvaluateIn()
    {
        assertTrue(Operator.in.evaluate(5, new Object[]{1, 3, 5, 7}));
        assertFalse(Operator.in.evaluate(6, new Object[]{1, 3, 5, 7}));
    }
    
    @Test
    public void testEvaluateNotIn()
    {
        assertFalse(Operator.notIn.evaluate(5, new Object[]{1, 3, 5, 7}));
        assertTrue(Operator.notIn.evaluate(6, new Object[]{1, 3, 5, 7}));
    }
}
