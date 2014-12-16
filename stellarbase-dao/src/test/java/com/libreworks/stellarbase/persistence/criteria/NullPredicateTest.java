package com.libreworks.stellarbase.persistence.criteria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;

public class NullPredicateTest
{
    @Test
    public void testEvaluateIs()
    {
        assertTrue(new NullPredicate(ValueExpression.of(ObjectUtils.NULL), false).evaluate(null));
        assertFalse(new NullPredicate(ValueExpression.of("foo"), false).evaluate(null));
    }
    
    @Test
    public void testEvaluateIsNot()
    {
        assertFalse(new NullPredicate(ValueExpression.of(ObjectUtils.NULL), true).evaluate(null));
        assertTrue(new NullPredicate(ValueExpression.of("foo"), true).evaluate(null));
    }
}
