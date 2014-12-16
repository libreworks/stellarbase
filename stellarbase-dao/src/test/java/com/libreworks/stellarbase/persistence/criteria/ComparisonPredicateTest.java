/**
 * Copyright 2010 LibreWorks contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Jonathan Hawk
 */
package com.libreworks.stellarbase.persistence.criteria;

import static org.junit.Assert.*;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

public class ComparisonPredicateTest
{
    private ComparisonPredicate object;

    @Test
    public void testHashCode()
    {
        object = new ComparisonPredicate(ComparisonPredicate.Operator.EQ, fieldNamed("foo"), ValueExpression.of("a"), null, false);
        int hashCode = new HashCodeBuilder().append(ComparisonPredicate.Operator.EQ).append(fieldNamed("foo")).append(ValueExpression.of("a")).append((Object)null).append(false).toHashCode();
        assertEquals(hashCode, object.hashCode());
    }

    @Test
    public void testEvaluateEq()
    {
        assertTrue(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.EQ, "foo", "foo", null));
        assertFalse(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.EQ, "foo", "bar", null));
    }
    
    @Test
    public void testEvaluateNeq()
    {
        assertFalse(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.NE, "foo", "foo", null));
        assertTrue(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.NE, "foo", "bar", null));
    }
    
    @Test
    public void testEvaluateGt()
    {
        assertTrue(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.GT, 5, 4, null));
        assertFalse(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.GT, 4, 5, null));
        assertFalse(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.GT, 4, 4, null));
    }
    
    @Test
    public void testEvaluateLt()
    {
        assertTrue(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.LT, 4, 5, null));
        assertFalse(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.LT, 5, 4, null));
        assertFalse(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.LT, 4, 4, null));
    }
    
    @Test
    public void testEvaluateGe()
    {
        assertTrue(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.GE, 5, 4, null));
        assertFalse(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.GE, 4, 5, null));
        assertTrue(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.GE, 4, 4, null));
    }
    
    @Test
    public void testEvaluateLe()
    {
        assertTrue(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.LE, 4, 5, null));
        assertFalse(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.LE, 5, 4, null));
        assertTrue(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.LE, 4, 4, null));
    }

    @Test
    public void testEvaluateBetween()
    {
        assertTrue(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.BETWEEN, 5, 1, 10));
        assertTrue(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.BETWEEN, 1, 1, 10));
        assertTrue(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.BETWEEN, 10, 1, 10));
        assertFalse(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.BETWEEN, -4, 1, 10));
        assertFalse(ComparisonPredicate.evaluate(ComparisonPredicate.Operator.BETWEEN, 15, 1, 10));
    }

    private Field<?> fieldNamed(String name)
    {
    	return new FieldImpl<Object>(name, Object.class);
    }
}
