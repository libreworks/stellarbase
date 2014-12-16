package com.libreworks.stellarbase.persistence.criteria;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AbstractExpressionTest
{
	private Expression<?> object;
	
	@Before
	public void setUp()
	{
		object = new FooExpression();
	}
	
	@Test
	public void testAsc()
	{
		Order s = object.asc();
		assertSame(object, s.getExpression());
		assertTrue(s.isAscending());
	}

	@Test
	public void testDesc()
	{
		Order s = object.desc();
		assertSame(object, s.getExpression());
		assertFalse(s.isAscending());
	}

	@Test
	public void testEq()
	{
		ValueExpression<Integer> value = ValueExpression.of(98765);
		Predicate e = object.eq(value);
		assertTrue(e instanceof ComparisonPredicate);
		ComparisonPredicate cp = (ComparisonPredicate) e;
		assertSame(cp.getA(), object);
		assertSame(cp.getOperator(), ComparisonPredicate.Operator.EQ);
		assertSame(value, cp.getB());
	}

	@Test
	public void testNeq()
	{
		ValueExpression<Integer> value = ValueExpression.of(12345);
		Predicate e = object.ne(value);
		assertTrue(e instanceof ComparisonPredicate);
		ComparisonPredicate cp = (ComparisonPredicate) e;
		assertSame(cp.getA(), object);
		assertSame(cp.getOperator(), ComparisonPredicate.Operator.NE);
		assertSame(value, cp.getB());
	}

	@Test
	public void testLt()
	{
		ValueExpression<Integer> value = ValueExpression.of(4);
		Predicate e = object.lt(value);
		assertTrue(e instanceof ComparisonPredicate);
		ComparisonPredicate cp = (ComparisonPredicate) e;
		assertSame(cp.getA(), object);
		assertSame(cp.getOperator(), ComparisonPredicate.Operator.LT);
		assertSame(value, cp.getB());

	}

	@Test
	public void testLe()
	{
		ValueExpression<Integer> value = ValueExpression.of(3);
		Predicate e = object.le(value);
		assertTrue(e instanceof ComparisonPredicate);
		ComparisonPredicate cp = (ComparisonPredicate) e;
		assertSame(cp.getA(), object);
		assertSame(cp.getOperator(), ComparisonPredicate.Operator.LE);
		assertSame(value, cp.getB());
	}

	@Test
	public void testGt()
	{
		ValueExpression<Integer> value = ValueExpression.of(4);
		Predicate e = object.gt(value);
		assertTrue(e instanceof ComparisonPredicate);
		ComparisonPredicate cp = (ComparisonPredicate) e;
		assertSame(cp.getA(), object);
		assertSame(cp.getOperator(), ComparisonPredicate.Operator.GT);
		assertSame(value, cp.getB());	}

	@Test
	public void testGe()
	{
		ValueExpression<Integer> value = ValueExpression.of(4);
		Predicate e = object.ge(value);
		assertTrue(e instanceof ComparisonPredicate);
		ComparisonPredicate cp = (ComparisonPredicate) e;
		assertSame(cp.getA(), object);
		assertSame(cp.getOperator(), ComparisonPredicate.Operator.GE);
		assertSame(value, cp.getB());
	}

	@Test
	public void testLike()
	{
		ValueExpression<String> a = ValueExpression.of("abcdefg%");
		Predicate e = object.like(a);
		assertTrue(e instanceof LikePredicate);
		LikePredicate lp = (LikePredicate) e;
		assertSame(lp.getInner(), object);
		assertSame(a, lp.getPattern());
		assertFalse(e.isNegated());
	}

	@Test
	public void testNotLike()
	{
		ValueExpression<String> a = ValueExpression.of("abcdefg%");
		Predicate e = object.notLike(a);
		assertTrue(e instanceof LikePredicate);
		LikePredicate lp = (LikePredicate) e;
		assertSame(lp.getInner(), object);
		assertSame(a, lp.getPattern());
		assertTrue(e.isNegated());
	}

	@Test
	public void testBetween()
	{
		ValueExpression<String> a = ValueExpression.of("a");
		ValueExpression<String> b = ValueExpression.of("z");
		Predicate e = object.between(a, b);
		assertTrue(e instanceof ComparisonPredicate);
		ComparisonPredicate cp = (ComparisonPredicate) e;
		assertSame(cp.getA(), object);
		assertSame(cp.getOperator(), ComparisonPredicate.Operator.BETWEEN);
		assertSame(a, cp.getB());
		assertSame(b, cp.getC());
		assertFalse(cp.isNegated());
	}

	@Test
	public void testNotBetween()
	{
		ValueExpression<String> a = ValueExpression.of("a");
		ValueExpression<String> b = ValueExpression.of("z");
		Predicate e = object.notBetween(a, b);
		assertTrue(e instanceof ComparisonPredicate);
		ComparisonPredicate cp = (ComparisonPredicate) e;
		assertSame(cp.getA(), object);
		assertSame(cp.getOperator(), ComparisonPredicate.Operator.BETWEEN);
		assertSame(a, cp.getB());
		assertSame(b, cp.getC());
		assertTrue(cp.isNegated());
	}

	@Test
	public void testInObjectArray()
	{
		ValueExpression[] a = values("a", "b", "c", "d");
		Predicate e = object.in(a);
		assertTrue(e instanceof InPredicate);
		InPredicate ip = (InPredicate) e;
		assertSame(ip.getInner(), object);
		assertArrayEquals(a, ip.getValues().toArray(new ValueExpression[4]));
		assertFalse(ip.isNegated());
	}

	@Test
	public void testInCollectionOfQ()
	{
		List<ValueExpression<?>> a = valuesList("a", "b", "c", "d");
		Predicate e = object.in(a);
		assertTrue(e instanceof InPredicate);
		InPredicate ip = (InPredicate) e;
		assertSame(ip.getInner(), object);
		assertEquals(a, ip.getValues());
		assertFalse(ip.isNegated());
	}

	@Test
	public void testNotInObjectArray()
	{
		ValueExpression[] a = values("a", "b", "c", "d");
		Predicate e = object.notIn(a);
		assertTrue(e instanceof InPredicate);
		InPredicate ip = (InPredicate) e;
		assertSame(ip.getInner(), object);
		assertArrayEquals(a, ip.getValues().toArray(new ValueExpression[4]));
		assertTrue(ip.isNegated());
	}

	@Test
	public void testNotInCollectionOfQ()
	{
		List<ValueExpression<?>> a = valuesList("a", "b", "c", "d");
		Predicate e = object.notIn(a);
		assertTrue(e instanceof InPredicate);
		InPredicate ip = (InPredicate) e;
		assertSame(ip.getInner(), object);
		assertEquals(a, ip.getValues());
		assertTrue(ip.isNegated());
	}

	@Test
	public void testIsNull()
	{
		Predicate e = object.isNull();
		assertTrue(e instanceof NullPredicate);
		NullPredicate np = (NullPredicate) e;
		assertSame(np.getInner(), object);
		assertFalse(np.isNegated());
	}

	@Test
	public void testIsNotNull()
	{
		Predicate e = object.isNotNull();
		assertTrue(e instanceof NullPredicate);
		NullPredicate np = (NullPredicate) e;
		assertSame(np.getInner(), object);
		assertTrue(np.isNegated());
	}

	@Test
	public void testAsString()
	{
		Projection<?> p = object.as("aoeuhtns");
		assertEquals("aoeuhtns", p.getAlias());
		assertSame(object, p.getExpression());
		assertFalse(p.isGrouped());
	}
	
	@Test
	public void testGroupedString()
	{
		Projection<?> p = object.grouped("hello");
		assertEquals("hello", p.getAlias());
		assertSame(object, p.getExpression());
		assertTrue(p.isGrouped());
	}

	@Test
	public void testCountDistinct()
	{
		Field<?> foo = fieldNamed("foo");
		object = foo.countDistinct();
		assertTrue(object instanceof CountExpression);
		CountExpression agg = (CountExpression) object;
		assertEquals(foo, agg.getArgument());
		assertTrue(agg.isDistinct());
	}

	@Test
	public void testCount()
	{
		Field<?> foo = fieldNamed("foo");
		object = foo.count();
		assertTrue(object instanceof CountExpression);
		CountExpression agg = (CountExpression) object;
		assertEquals(foo, agg.getArgument());
		assertFalse(agg.isDistinct());
	}

	@Test
	public void testSum()
	{
		Field<?> foo = fieldNamed("foo");
		object = foo.sum();
		assertTrue(object instanceof AggregateExpression);
		AggregateExpression<?> agg = (AggregateExpression<?>) object;
		assertEquals(foo, agg.getArgument());
		assertSame(AggregateExpression.Function.SUM, agg.getFunction());
	}

	@Test
	public void testAvg()
	{
		Field<?> foo = fieldNamed("foo");
		object = foo.avg();
		assertTrue(object instanceof AggregateExpression);
		AggregateExpression<?> agg = (AggregateExpression<?>) object;
		assertEquals(foo, agg.getArgument());
		assertSame(AggregateExpression.Function.AVG, agg.getFunction());
	}

	@Test
	public void testMax()
	{
		Field<?> foo = fieldNamed("foo");
		object = foo.max();
		assertTrue(object instanceof AggregateExpression);
		AggregateExpression<?> agg = (AggregateExpression<?>) object;
		assertEquals(foo, agg.getArgument());
		assertSame(AggregateExpression.Function.MAX, agg.getFunction());
	}

	@Test
	public void testMin()
	{
		Field<?> foo = fieldNamed("foo");
		object = foo.min();
		assertTrue(object instanceof AggregateExpression);
		AggregateExpression<?> agg = (AggregateExpression<?>) object;
		assertEquals(foo, agg.getArgument());
		assertSame(AggregateExpression.Function.MIN, agg.getFunction());
	}		

	private List<ValueExpression<?>> valuesList(Object... values)
	{
		ArrayList<ValueExpression<?>> expressions = new ArrayList<ValueExpression<?>>(values.length);
		for (Object o : values) {
			expressions.add(ValueExpression.of(o));
		}
		return expressions;
	}
	
	private ValueExpression[] values(Object... values)
	{
		ValueExpression[] expressions = new ValueExpression[values.length];
		for (int i=0; i<values.length; i++) {
			expressions[i] = ValueExpression.of(values[i]);
		}
		return expressions;
	}
	
	private Field<?> fieldNamed(String name)
	{
		return new FieldImpl<Object>(name, Object.class);
	}	
	
	class FooExpression extends AbstractExpression<Object>
	{
		public FooExpression()
		{
			super(Object.class);
		}

		@Override
		public Object evaluate(Object object)
		{
			return null;
		}
	}
}
