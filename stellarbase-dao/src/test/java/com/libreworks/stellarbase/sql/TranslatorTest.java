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
package com.libreworks.stellarbase.sql;

import static org.junit.Assert.*;

import com.libreworks.stellarbase.persistence.criteria.FieldImpl;
import com.libreworks.stellarbase.persistence.criteria.Junction;
import com.libreworks.stellarbase.persistence.criteria.ValueExpression;
import com.libreworks.stellarbase.sql.Fragment;
import com.libreworks.stellarbase.sql.Translator;

import org.junit.Before;
import org.junit.Test;

public class TranslatorTest
{
	private Translator object;

	@Before
	public void setUp() throws Exception
	{
		object = new Translator(new StubDatabaseMetadata());
	}

	@Test
	public void testSetTable()
	{
		assertSame(object, object.setTable("foo"));
		assertEquals("foo", object.table);
	}

	@Test
	public void testTranslate()
	{
		FieldImpl<?> a = new FieldImpl<Object>("a", Object.class);
		FieldImpl<?> b = new FieldImpl<Object>("b", Object.class);
		FieldImpl<?> c = new FieldImpl<Object>("c", Object.class);
		
		Fragment c1 = object.translate(a.eq(new ValueExpression<Integer>(4)), false);
		assertEquals("a = ?", c1.getSql());
		assertEquals(4, c1.getParameters().iterator().next());
		
		Fragment c2 = object.translate(Junction.and(b.eq(new ValueExpression<String>("foo")), c.ne(new ValueExpression<String>("bar"))), false);
		assertEquals("(b = ? AND c <> ?)", c2.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c2.getParameters().toArray());
		
		FieldImpl<?> foobar = new FieldImpl<Object>("foobar", Object.class);
		assertEquals("\"foobar\"", object.translate(foobar, true).getSql());
		
		FieldImpl<?> bar = new FieldImpl<Object>("bar", Object.class);
		assertEquals("bar ASC", object.translate(bar.asc(), false).getSql());
	}

	@Test
	public void testTranslateField()
	{
		FieldImpl<?> foobar = new FieldImpl<Object>("foobar", Object.class);
		assertEquals("\"foobar\"", object.translateField(foobar, true).getSql());
		assertEquals("foobar", object.translateField(foobar, false).getSql());
	}
	
	@Test
	public void testTranslateFieldWithTable()
	{
		FieldImpl<?> foobar = new FieldImpl<Object>("foobar", Object.class);
		
		object.setTable("test");
		assertEquals("test.\"foobar\"", object.translateField(foobar, true).getSql());
		assertEquals("test.foobar", object.translateField(foobar, false).getSql());
	}
	
	@Test
	public void testTranslateExpression()
	{
		FieldImpl<?> foobar = new FieldImpl<Object>("foobar", Object.class);
		ValueExpression<Integer> i4 = new ValueExpression<Integer>(4);
		
		assertEquals("COUNT(\"foobar\")", object.translateExpression(foobar.count(), true).getSql());
		assertEquals("SUM(foobar)", object.translateExpression(foobar.sum(), false).getSql());
		
		object.setTable("test");
		assertEquals("COUNT(test.\"foobar\")", object.translateExpression(foobar.count(), true).getSql());
		assertEquals("SUM(test.foobar)", object.translateExpression(foobar.sum(), false).getSql());
		
		Fragment f = object.translateExpression(i4, true);
		assertEquals("?", f.getSql());
		assertTrue(f.getParameters().contains(4));
	}

	@Test
	public void testTranslateSort()
	{
		FieldImpl<?> bar = new FieldImpl<Object>("bar", Object.class);
		
		assertEquals("\"bar\" ASC", object.translateSort(bar.asc(), true).getSql());
		assertEquals("bar ASC", object.translateSort(bar.asc(), false).getSql());
		assertEquals("\"bar\" DESC", object.translateSort(bar.desc(), true).getSql());
		assertEquals("bar DESC", object.translateSort(bar.desc(), false).getSql());
	}

	@Test
	public void testTranslateCriterion()
	{
		FieldImpl<?> a = new FieldImpl<Object>("a", Object.class);
		FieldImpl<?> b = new FieldImpl<Object>("b", Object.class);
		FieldImpl<?> c = new FieldImpl<Object>("c", Object.class);
		
		ValueExpression<String> foo = new ValueExpression<String>("foo");
		ValueExpression<String> bar = new ValueExpression<String>("bar");
		
		ValueExpression<Integer> i4 = new ValueExpression<Integer>(4);
		
		Fragment c1 = object.translateCriterion(a.eq(i4), false);
		assertEquals("a = ?", c1.getSql());
		assertEquals(4, c1.getParameters().iterator().next());
		Fragment c2 = object.translateCriterion(Junction.and(b.eq(foo), c.ne(bar)), false);
		assertEquals("(b = ? AND c <> ?)", c2.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c2.getParameters().toArray());
		Fragment c3 = object.translateCriterion(a.eq(i4), true);
		assertEquals("\"a\" = ?", c3.getSql());
		assertEquals(4, c3.getParameters().iterator().next());
		Fragment c4 = object.translateCriterion(Junction.and(b.eq(foo), c.ne(bar)), true);
		assertEquals("(\"b\" = ? AND \"c\" <> ?)", c4.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c4.getParameters().toArray());
	}

	@Test
	public void testTranslatePredicate()
	{
		FieldImpl<String> a = new FieldImpl<String>("a", String.class);
		FieldImpl<?> b = new FieldImpl<Object>("b", Object.class);
		FieldImpl<?> c = new FieldImpl<Object>("c", Object.class);
		FieldImpl<?> foo = new FieldImpl<Object>("foo", Object.class);

		ValueExpression<String> bar = new ValueExpression<String>("bar");
		ValueExpression<String> d = new ValueExpression<String>("d");
		ValueExpression<String> z = new ValueExpression<String>("z");
		ValueExpression<Integer> i1 = new ValueExpression<Integer>(1);
		ValueExpression<Integer> i2 = new ValueExpression<Integer>(2);
		ValueExpression<Integer> i3 = new ValueExpression<Integer>(3);
		ValueExpression<Integer> i4 = new ValueExpression<Integer>(4);
		ValueExpression<Integer> i5 = new ValueExpression<Integer>(5);

		Fragment e1 = object.translatePredicate(foo.eq(bar), false);
		assertEquals("foo = ?", e1.getSql());
		assertArrayEquals(new Object[]{"bar"}, e1.getParameters().toArray());
		
		Fragment e2 = object.translatePredicate(a.isNull(), true);
		assertEquals("\"a\" IS NULL", e2.getSql());
		assertTrue(e2.getParameters().isEmpty());
		
		Fragment e3 = object.translatePredicate(b.between(d, z), true);
		assertEquals("\"b\" BETWEEN ? AND ?", e3.getSql());
		assertArrayEquals(new Object[]{"d", "z"}, e3.getParameters().toArray());
		
		Fragment e4 = object.translatePredicate(c.notIn(i1, i2, i3, i4, i5), false);
		assertEquals("c NOT IN (?, ?, ?, ?, ?)", e4.getSql());
		assertArrayEquals(new Object[]{1,2,3,4,5}, e4.getParameters().toArray());
		
		Fragment e5 = object.translatePredicate(a.gt(b), true);
		assertEquals("\"a\" > \"b\"", e5.getSql());
		assertTrue(e5.getParameters().isEmpty());
	}

	@Test
	public void testTranslateJunction()
	{
		FieldImpl<?> a = new FieldImpl<Object>("a", Object.class);
		FieldImpl<?> b = new FieldImpl<Object>("b", Object.class);
		FieldImpl<?> c = new FieldImpl<Object>("c", Object.class);
		
		ValueExpression<String> foo = new ValueExpression<String>("foo");
		ValueExpression<String> bar = new ValueExpression<String>("bar");
		ValueExpression<Integer> i1 = new ValueExpression<Integer>(1);
		ValueExpression<Integer> i2 = new ValueExpression<Integer>(2);
		ValueExpression<Integer> i3 = new ValueExpression<Integer>(3);
		
		Fragment c1 = object.translateJunction(Junction.or(b.eq(foo), c.ne(bar)), false);
		assertEquals("(b = ? OR c <> ?)", c1.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c1.getParameters().toArray());
		Fragment c2 = object.translateJunction(Junction.and(b.eq(foo), c.ne(bar)), false);
		assertEquals("(b = ? AND c <> ?)", c2.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c2.getParameters().toArray());
		Fragment c3 = object.translateJunction(Junction.or(b.eq(foo), c.ne(bar)), true);
		assertEquals("(\"b\" = ? OR \"c\" <> ?)", c3.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c3.getParameters().toArray());
		Fragment c4 = object.translateJunction(Junction.and(b.eq(foo), c.ne(bar)), true);
		assertEquals("(\"b\" = ? AND \"c\" <> ?)", c4.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c4.getParameters().toArray());
		Fragment c5 = object.translateJunction(Junction.and(Junction.or(a.eq(i1), b.eq(i2)), c.eq(i3)), false);
		assertEquals("((a = ? OR b = ?) AND c = ?)", c5.getSql());
		assertArrayEquals(new Object[]{1, 2, 3}, c5.getParameters().toArray());
	}
}
