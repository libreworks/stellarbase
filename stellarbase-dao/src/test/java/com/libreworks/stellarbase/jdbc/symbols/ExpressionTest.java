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
package com.libreworks.stellarbase.jdbc.symbols;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.libreworks.stellarbase.collections.FluentValues;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;

public class ExpressionTest
{
    private Expression object;
    
    @Before
    public void setUp() throws Exception
    {
        object = new Expression(new Field("foo"), Operator.eq, "a"); 
    }

    @Test
    public void testHashCode()
    {
        int hashCode = new HashCodeBuilder(7, 13).append(new Field("foo")).append(Operator.eq).append("a").toHashCode();
        assertEquals(hashCode, object.hashCode());
    }

    @Test
    public void testEvaluate()
    {
        assertTrue(object.evaluate(Collections.singletonMap("foo", "a")));
        Expression b = new Expression(new Field("foo"), Operator.eq, new Field("bar"));
        FluentValues values = new FluentValues().set("foo", "a").set("bar", "a");
        assertTrue(b.evaluate(values));
    }

    @Test
    public void testGetAllFields()
    {
        Collection<Field> fields1 = object.getAllFields();
        assertTrue(fields1.contains(new Field("foo")));
        object.right = new Field("bar");
        Collection<Field> fields2 = object.getAllFields();
        assertTrue(fields2.contains(new Field("foo")));
        assertTrue(fields2.contains(new Field("bar")));
    }
    
    @Test
    public void testGetLeft()
    {
        assertEquals(new Field("foo"), object.getLeft());
    }

    @Test
    public void testGetOperator()
    {
        assertSame(Operator.eq, object.getOperator());
    }

    @Test
    public void testGetRight()
    {
        assertEquals("a", object.getRight());
    }

    @Test
    public void testEqualsObject()
    {
        assertTrue(object.equals(object));
        assertFalse(object.equals(null));
        assertFalse(object.equals("b"));
        Expression other = new Expression(new Field("foo"), Operator.eq, "a");
        assertTrue(object.equals(other));
    }

    @Test
    public void testToString()
    {
        assertEquals("foo = 'a'", object.toString());
        assertEquals("foo IS NULL", new Expression(object.left, Operator.is, null).toString());
        assertEquals("foo BETWEEN 1 AND 5", new Expression(object.left, Operator.between, new Object[]{1, 5}).toString());
        assertEquals("foo NOT IN ('a','b','c')", new Expression(object.left, Operator.notIn, new Object[]{"a", "b", "c"}).toString());
    }

    @Test
    public void testEqFieldObject()
    {
        Field field = new Field("test");
        Object value = 7;
        Expression object = Expression.eq(field, value);
        assertSame(field, object.getLeft());
        assertSame(value, object.getRight());
        assertSame(Operator.eq, object.getOperator());
    }

    @Test
    public void testEqStringObject()
    {
        String name = "foobar";
        Object value = 7;
        Expression object = Expression.eq(name, value);
        assertEquals(name, object.getLeft().getName());
        assertSame(value, object.getRight());
        assertSame(Operator.eq, object.getOperator());
    }

    @Test
    public void testNeqFieldObject()
    {
        Field field = new Field("test");
        Object value = 7;
        Expression object = Expression.neq(field, value);
        assertSame(field, object.getLeft());
        assertSame(value, object.getRight());
        assertSame(Operator.neq, object.getOperator());
    }

    @Test
    public void testNeqStringObject()
    {
        String name = "foobar";
        Object value = 7;
        Expression object = Expression.neq(name, value);
        assertEquals(name, object.getLeft().getName());
        assertSame(value, object.getRight());
        assertSame(Operator.neq, object.getOperator());
    }

    @Test
    public void testLeFieldObject()
    {
        Field field = new Field("test");
        Object value = 7;
        Expression object = Expression.le(field, value);
        assertSame(field, object.getLeft());
        assertSame(value, object.getRight());
        assertSame(Operator.le, object.getOperator());
    }

    @Test
    public void testLeStringObject()
    {
        String name = "foobar";
        Object value = 7;
        Expression object = Expression.le(name, value);
        assertEquals(name, object.getLeft().getName());
        assertSame(value, object.getRight());
        assertSame(Operator.le, object.getOperator());
    }

    @Test
    public void testLtFieldObject()
    {
        Field field = new Field("test");
        Object value = 7;
        Expression object = Expression.lt(field, value);
        assertSame(field, object.getLeft());
        assertSame(value, object.getRight());
        assertSame(Operator.lt, object.getOperator());
    }

    @Test
    public void testLtStringObject()
    {
        String name = "foobar";
        Object value = 7;
        Expression object = Expression.lt(name, value);
        assertEquals(name, object.getLeft().getName());
        assertSame(value, object.getRight());
        assertSame(Operator.lt, object.getOperator());
    }

    @Test
    public void testGtFieldObject()
    {
        Field field = new Field("test");
        Object value = 7;
        Expression object = Expression.gt(field, value);
        assertSame(field, object.getLeft());
        assertSame(value, object.getRight());
        assertSame(Operator.gt, object.getOperator());
    }

    @Test
    public void testGtStringObject()
    {
        String name = "foobar";
        Object value = 7;
        Expression object = Expression.gt(name, value);
        assertEquals(name, object.getLeft().getName());
        assertSame(value, object.getRight());
        assertSame(Operator.gt, object.getOperator());
    }

    @Test
    public void testGeFieldObject()
    {
        Field field = new Field("test");
        Object value = 7;
        Expression object = Expression.ge(field, value);
        assertSame(field, object.getLeft());
        assertSame(value, object.getRight());
        assertSame(Operator.ge, object.getOperator());
    }

    @Test
    public void testGeStringObject()
    {
        String name = "foobar";
        Object value = 7;
        Expression object = Expression.ge(name, value);
        assertEquals(name, object.getLeft().getName());
        assertSame(value, object.getRight());
        assertSame(Operator.ge, object.getOperator());
    }

    @Test
    public void testLikeFieldObject()
    {
        Field field = new Field("test");
        Object value = "value%";
        Expression object = Expression.like(field, value);
        assertSame(field, object.getLeft());
        assertSame(value, object.getRight());
        assertSame(Operator.like, object.getOperator());
    }

    @Test
    public void testLikeStringObject()
    {
        String name = "foobar";
        Object value = "value%";
        Expression object = Expression.like(name, value);
        assertEquals(name, object.getLeft().getName());
        assertSame(value, object.getRight());
        assertSame(Operator.like, object.getOperator());
    }

    @Test
    public void testNotLikeFieldObject()
    {
        Field field = new Field("test");
        Object value = "value%";
        Expression object = Expression.notLike(field, value);
        assertSame(field, object.getLeft());
        assertSame(value, object.getRight());
        assertSame(Operator.notLike, object.getOperator());
    }

    @Test
    public void testNotLikeStringObject()
    {
        String name = "foobar";
        Object value = "value%";
        Expression object = Expression.notLike(name, value);
        assertEquals(name, object.getLeft().getName());
        assertSame(value, object.getRight());
        assertSame(Operator.notLike, object.getOperator());
    }

    @Test
    public void testBetweenFieldComparableOfQComparableOfQ()
    {
        Field field = new Field("test");
        String a = "abc";
        String b = "def";
        Expression object = Expression.between(field, a, b);
        assertSame(field, object.getLeft());
        assertArrayEquals(new String[]{a, b}, (Object[])object.getRight());
        assertSame(Operator.between, object.getOperator());
    }

    @Test
    public void testBetweenStringComparableOfQComparableOfQ()
    {
        String name = "foobar";
        String a = "abc";
        String b = "def";
        Expression object = Expression.between(name, a, b);
        assertEquals(name, object.getLeft().getName());
        assertArrayEquals(new String[]{a, b}, (Object[])object.getRight());
        assertSame(Operator.between, object.getOperator());
    }

    @Test
    public void testNotBetweenFieldComparableOfQComparableOfQ()
    {
        Field field = new Field("test");
        String a = "abc";
        String b = "def";
        Expression object = Expression.notBetween(field, a, b);
        assertSame(field, object.getLeft());
        assertArrayEquals(new String[]{a, b}, (Object[])object.getRight());
        assertSame(Operator.notBetween, object.getOperator());
    }

    @Test
    public void testNotBetweenStringComparableOfQComparableOfQ()
    {
        String name = "foobar";
        String a = "abc";
        String b = "def";
        Expression object = Expression.notBetween(name, a, b);
        assertEquals(name, object.getLeft().getName());
        assertArrayEquals(new String[]{a, b}, (Object[])object.getRight());
        assertSame(Operator.notBetween, object.getOperator());
    }

    @Test
    public void testNotInFieldCollectionOfQ()
    {
        Field field = new Field("test");
        Collection<String> values = Arrays.asList("a", "b", "c");
        Expression object = Expression.notIn(field, values);
        assertSame(field, object.getLeft());
        assertArrayEquals(values.toArray(), (Object[])object.getRight());
        assertSame(Operator.notIn, object.getOperator());
    }

    @Test
    public void testNotInFieldObjectArray()
    {
        Field field = new Field("test");
        Object[] values = new Object[]{"a", "b", "c", "d"};
        Expression object = Expression.notIn(field, values);
        assertSame(field, object.getLeft());
        assertArrayEquals(values, (Object[])object.getRight());
        assertSame(Operator.notIn, object.getOperator());
    }

    @Test
    public void testNotInStringCollectionOfQ()
    {
        String name = "foobar";
        Collection<String> values = Arrays.asList("a", "b", "c");
        Expression object = Expression.notIn(name, values);
        assertEquals(name, object.getLeft().getName());
        assertArrayEquals(values.toArray(), (Object[])object.getRight());
        assertSame(Operator.notIn, object.getOperator());
    }

    @Test
    public void testNotInStringObjectArray()
    {
        String name = "foobar";
        Object[] values = new Object[]{"a", "b", "c", "d"};
        Expression object = Expression.notIn(name, values);
        assertEquals(name, object.getLeft().getName());
        assertArrayEquals(values, (Object[])object.getRight());
        assertSame(Operator.notIn, object.getOperator());
    }

    @Test
    public void testInFieldCollectionOfQ()
    {
        Field field = new Field("test");
        Collection<String> values = Arrays.asList("a", "b", "c");
        Expression object = Expression.in(field, values);
        assertSame(field, object.getLeft());
        assertArrayEquals(values.toArray(), (Object[])object.getRight());
        assertSame(Operator.in, object.getOperator());
    }

    @Test
    public void testInFieldObjectArray()
    {
        Field field = new Field("test");
        Object[] values = new Object[]{"a", "b", "c"};
        Expression object = Expression.in(field, values);
        assertSame(field, object.getLeft());
        assertArrayEquals(values, (Object[])object.getRight());
        assertSame(Operator.in, object.getOperator());
    }

    @Test
    public void testInStringCollectionOfQ()
    {
        String name = "foobar";
        Collection<String> values = Arrays.asList("a", "b", "c");
        Expression object = Expression.in(name, values);
        assertEquals(name, object.getLeft().getName());
        assertArrayEquals(values.toArray(), (Object[])object.getRight());
        assertSame(Operator.in, object.getOperator());
    }

    @Test
    public void testInStringObjectArray()
    {
        String name = "foobar";
        Object[] values = new Object[]{"a", "b", "c", "d"};
        Expression object = Expression.in(name, values);
        assertEquals(name, object.getLeft().getName());
        assertArrayEquals(values, (Object[])object.getRight());
        assertSame(Operator.in, object.getOperator());
    }

    @Test
    public void testIsNullString()
    {
        String name = "foobar";
        Expression object = Expression.isNull(name);
        assertEquals(name, object.getLeft().getName());
        assertNull(object.getRight());
        assertSame(Operator.is, object.getOperator());
    }

    @Test
    public void testIsNullField()
    {
        Field field = new Field("test");
        Expression object = Expression.isNull(field);
        assertSame(field, object.getLeft());
        assertNull(object.getRight());
        assertSame(Operator.is, object.getOperator());
    }

    @Test
    public void testIsNotNullString()
    {
        String name = "foobar";
        Expression object = Expression.isNotNull(name);
        assertEquals(name, object.getLeft().getName());
        assertNull(object.getRight());
        assertSame(Operator.isNot, object.getOperator());
    }

    @Test
    public void testIsNotNullField()
    {
        Field field = new Field("test");
        Expression object = Expression.isNotNull(field);
        assertSame(field, object.getLeft());
        assertNull(object.getRight());
        assertSame(Operator.isNot, object.getOperator());
    }
}
