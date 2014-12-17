package com.libreworks.stellarbase.persistence.criteria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Optional;

public class NullPredicateTest
{
    @Test
    public void testEvaluateIs()
    {
        assertTrue(new NullPredicate(ValueExpression.of(Optional.absent()), false).evaluate(null));
        assertFalse(new NullPredicate(ValueExpression.of("foo"), false).evaluate(null));
    }
    
    @Test
    public void testEvaluateIsNot()
    {
        assertFalse(new NullPredicate(ValueExpression.of(Optional.absent()), true).evaluate(null));
        assertTrue(new NullPredicate(ValueExpression.of("foo"), true).evaluate(null));
    }
}
