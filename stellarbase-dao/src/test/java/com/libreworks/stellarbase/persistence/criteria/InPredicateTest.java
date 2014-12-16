package com.libreworks.stellarbase.persistence.criteria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class InPredicateTest
{
    @Test
    public void testEvaluateIn()
    {
        assertTrue(new InPredicate(ValueExpression.of(5), values(1, 3, 5, 7), false).evaluate(null));
        assertFalse(new InPredicate(ValueExpression.of(6), values(1, 3, 5, 7), false).evaluate(null));
    }
    
    @Test
    public void testEvaluateNotIn()
    {
        assertFalse(new InPredicate(ValueExpression.of(5), values(1, 3, 5, 7), true).evaluate(null));
        assertTrue(new InPredicate(ValueExpression.of(6), values(1, 3, 5, 7), true).evaluate(null));
    }

    @SuppressWarnings("unchecked")
	private <T> ArrayList<ValueExpression<T>> values(T... values)
	{
		ArrayList<ValueExpression<T>> expressions = new ArrayList<ValueExpression<T>>(values.length);
		for (T o : values) {
			expressions.add(ValueExpression.of(o));
		}
		return expressions;
	}	
}
