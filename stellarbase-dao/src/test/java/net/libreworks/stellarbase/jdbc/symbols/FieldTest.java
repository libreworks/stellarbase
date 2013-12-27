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
package net.libreworks.stellarbase.jdbc.symbols;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;

public class FieldTest
{
    private Field object;
    
    @Before
    public void setUp() throws Exception
    {
        object = new Field("foo", "bar");
    }

    @Test
    public void testHashCode()
    {
        int hashCode = new HashCodeBuilder()
            .append("foo")
            .append("bar")
            .toHashCode();
        assertEquals(hashCode, object.hashCode());
    }

    @Test
    public void testEqualsObject()
    {
        assertTrue(object.equals(object));
        assertFalse(object.equals(null));
        assertFalse(object.equals("foo"));
        
        Field other = new Field("a", "b");
        assertFalse(object.equals(other));
        other.name = "foo";
        other.alias = "bar";
        assertTrue(object.equals(other));
    }

    @Test
    public void testGetAlias()
    {
        object.setAlias("baz");
        assertEquals("baz", object.getAlias());
    }

    @Test
    public void testGetName()
    {
        assertEquals("foo", object.getName());
    }

    @Test
    public void testToString()
    {
        assertEquals("foo", object.toString());
    }

    @Test
    public void testAsc()
    {
        Sort s = object.asc();
        assertSame(object, s.getField());
        assertTrue(s.isAscending());
    }

    @Test
    public void testDesc()
    {
        Sort s = object.desc();
        assertSame(object, s.getField());
        assertFalse(s.isAscending());
    }

    @Test
    public void testEq()
    {
        Integer value = 98765;
        Expression e = object.eq(value);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.eq);
        assertSame(value, e.getRight());
    }

    @Test
    public void testNeq()
    {
        Integer value = 12345;
        Expression e = object.neq(value);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.neq);
        assertSame(value, e.getRight());
    }

    @Test
    public void testLt()
    {
        Integer value = 4;
        Expression e = object.lt(value);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.lt);
        assertSame(value, e.getRight());
    }

    @Test
    public void testLe()
    {
        Integer value = 3;
        Expression e = object.le(value);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.le);
        assertSame(value, e.getRight());
    }

    @Test
    public void testGt()
    {
        Integer value = 4;
        Expression e = object.gt(value);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.gt);
        assertSame(value, e.getRight());
    }

    @Test
    public void testGe()
    {
        Integer value = 4;
        Expression e = object.ge(value);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.ge);
        assertSame(value, e.getRight());
    }

    @Test
    public void testLike()
    {
        String a = "abcdefg%";
        Expression e = object.like(a);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.like);
        assertSame(a, e.getRight());
    }

    @Test
    public void testNotLike()
    {
        String a = "abcdefg%";
        Expression e = object.notLike(a);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.notLike);
        assertSame(a, e.getRight());
    }

    @Test
    public void testBetween()
    {
        String a = "a";
        String b = "z";
        Expression e = object.between(a, b);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.between);
        assertArrayEquals(new Object[]{a, b}, (Object[])e.getRight());
    }

    @Test
    public void testNotBetween()
    {
        String a = "a";
        String b = "z";
        Expression e = object.notBetween(a, b);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.notBetween);
        assertArrayEquals(new Object[]{a, b}, (Object[])e.getRight());
    }

    @Test
    public void testInObjectArray()
    {
        Object[] a = new Object[]{"a", "b", "c", "d"};
        Expression e = object.in(a);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.in);
        assertArrayEquals(a, (Object[])e.getRight());
    }

    @Test
    public void testInCollectionOfQ()
    {
        Collection<?> a = Arrays.asList("a", "b", "c");
        Expression e = object.in(a);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.in);
        assertArrayEquals(a.toArray(), (Object[])e.getRight());
    }

    @Test
    public void testNotInObjectArray()
    {
        Object[] a = new Object[]{"a", "b", "c", "d"};
        Expression e = object.notIn(a);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.notIn);
        assertArrayEquals(a, (Object[])e.getRight());
    }

    @Test
    public void testNotInCollectionOfQ()
    {
        Collection<?> a = Arrays.asList("a", "b", "c");
        Expression e = object.notIn(a);
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.notIn);
        assertArrayEquals(a.toArray(), (Object[])e.getRight());

    }

    @Test
    public void testIsNull()
    {
        Expression e = object.isNull();
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.is);
        assertNull(e.getRight());
    }

    @Test
    public void testIsNotNull()
    {
        Expression e = object.isNotNull();
        assertSame(e.getLeft(), object);
        assertSame(e.getOperator(), Operator.isNot);
        assertNull(e.getRight());
    }

    @Test
    public void testNamedString()
    {
        Field object = Field.named("foo");
        assertEquals("foo", object.getName());
        
        Field o2 = Field.named("COUNT(foo)");
        assertTrue(o2 instanceof AggregateField);
        assertEquals("foo", o2.getName());
        assertEquals(Aggregate.COUNT, ((AggregateField)o2).getFunction());
    }

    @Test
    public void testNamedStringString()
    {
        Field object = Field.named("foo", "bar");
        assertEquals("foo", object.getName());
        assertEquals("bar", object.getAlias());
    }

    @Test
    public void testRawString()
    {
    	Field object = Field.raw("SUBSTRING(aoeu, 1, 2)", "midstr");
    	assertTrue(object instanceof RawField);
    	assertEquals("SUBSTRING(aoeu, 1, 2)", object.getName());
    	assertEquals("midstr", object.getAlias());
    }
    
    @Test
    public void testGroupString()
    {
        GroupField object = Field.group("foo");
        assertEquals("foo", object.getName());
    }

    @Test
    public void testGroupStringString()
    {
        GroupField object = Field.group("foo", "baz");
        assertEquals("foo", object.getName());
        assertEquals("baz", object.getAlias());
    }

    @Test
    public void testAggregate()
    {
        AggregateField object = Field.aggregate(Aggregate.MIN, "foo", "bar");
        assertEquals("foo", object.getName());
        assertEquals("bar", object.getAlias());
        assertSame(Aggregate.MIN, object.getFunction());
    }

    @Test
    public void testCount()
    {
        AggregateField object = Field.count("foo", "bar");
        assertEquals("foo", object.getName());
        assertEquals("bar", object.getAlias());
        assertSame(Aggregate.COUNT, object.getFunction());
    }

    @Test
    public void testSum()
    {
        AggregateField object = Field.sum("foo", "bar");
        assertEquals("foo", object.getName());
        assertEquals("bar", object.getAlias());
        assertSame(Aggregate.SUM, object.getFunction());
    }

    @Test
    public void testAvg()
    {
        AggregateField object = Field.avg("foo", "bar");
        assertEquals("foo", object.getName());
        assertEquals("bar", object.getAlias());
        assertSame(Aggregate.AVG, object.getFunction());
    }

    @Test
    public void testMax()
    {
        AggregateField object = Field.max("foo", "bar");
        assertEquals("foo", object.getName());
        assertEquals("bar", object.getAlias());
        assertSame(Aggregate.MAX, object.getFunction());
    }

    @Test
    public void testMin()
    {
        AggregateField object = Field.min("foo", "bar");
        assertEquals("foo", object.getName());
        assertEquals("bar", object.getAlias());
        assertSame(Aggregate.MIN, object.getFunction());
    }
}
