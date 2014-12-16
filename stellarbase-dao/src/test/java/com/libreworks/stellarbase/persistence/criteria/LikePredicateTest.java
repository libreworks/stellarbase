package com.libreworks.stellarbase.persistence.criteria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class LikePredicateTest
{
	
    @Test
    public void testEvaluateLike()
    {
    	ValueExpression<String> spatula = ValueExpression.of("Spatula");
    	ValueExpression<String> knife = ValueExpression.of("Knife");
    	
    	for (String pattern : Arrays.asList("Spat%", "%ula", "%atul%", "Sp%la", "Spa_ula")) {
    		assertTrue("Doesn't match pattern " + pattern, new LikePredicate(spatula, ValueExpression.of(pattern), false).evaluate(null));
    	}
    	for (String pattern : Arrays.asList("Spat%", "%ula", "%atul%", "Sp%la", "Spa_ula")) {
    		assertFalse("Matches pattern " + pattern, new LikePredicate(knife, ValueExpression.of(pattern), false).evaluate(null));
    	}
    }
    
    @Test
    public void testEvaluateNotLike()
    {
    	ValueExpression<String> spatula = ValueExpression.of("Spatula");
    	ValueExpression<String> knife = ValueExpression.of("Knife");
    	
    	for (String pattern : Arrays.asList("Spat%", "%ula", "%atul%", "Sp%la", "Spa_ula")) {
    		assertFalse("Matches pattern " + pattern, new LikePredicate(spatula, ValueExpression.of(pattern), true).evaluate(null));
    	}
    	for (String pattern : Arrays.asList("Spat%", "%ula", "%atul%", "Sp%la", "Spa_ula")) {
    		assertTrue("Doesn't match pattern " + pattern, new LikePredicate(knife, ValueExpression.of(pattern), true).evaluate(null));
    	}
    }
}
