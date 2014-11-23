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
package com.libreworks.stellarbase.jdbc;

import static org.junit.Assert.*;

import com.libreworks.stellarbase.jdbc.symbols.Expression;
import com.libreworks.stellarbase.jdbc.symbols.Field;
import com.libreworks.stellarbase.jdbc.symbols.Junction;
import com.libreworks.stellarbase.jdbc.symbols.Sort;
import com.libreworks.stellarbase.jdbc.symbols.SymbolClause;

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
		Fragment c1 = object.translate(Expression.eq("a", 4), false);
		assertEquals("a = ?", c1.getSql());
		assertEquals(4, c1.getParameters().iterator().next());
		Fragment c2 = object.translate(Junction.and(Expression.eq("b", "foo"), Expression.neq("c", "bar")), false);
		assertEquals("(b = ? AND c <> ?)", c2.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c2.getParameters().toArray());
		assertEquals("\"foobar\"", object.translate(Field.named("foobar"), true).getSql());
		assertEquals("bar ASC", object.translate(Sort.asc("bar"), false).getSql());
		SymbolClause<Field> clause = new SymbolClause<Field>();
		clause.add(Field.named("a"))
			.add(Field.named("b"))
			.add(Field.named("c"));
		assertEquals("a, b, c", object.translate(clause, false).getSql());
	}

	@Test
	public void testTranslateField()
	{
		assertEquals("\"foobar\"", object.translateField(Field.named("foobar"), true).getSql());
		assertEquals("foobar", object.translateField(Field.named("foobar"), false).getSql());
		assertEquals("COUNT(\"foobar\")", object.translateField(Field.count("foobar", "foo"), true).getSql());
		assertEquals("SUM(foobar)", object.translateField(Field.sum("foobar", "foo"), false).getSql());
		assertEquals("MID(foobar, 1, 2)", object.translateField(Field.raw("MID(foobar, 1, 2)", "foo"), true).getSql());
		assertEquals("MID(foobar, 1, 2)", object.translateField(Field.raw("MID(foobar, 1, 2)", "foo"), false).getSql());
	}
	
	@Test
	public void testTranslateFieldWithTable()
	{
		object.setTable("test");
		assertEquals("test.\"foobar\"", object.translateField(Field.named("foobar"), true).getSql());
		assertEquals("test.foobar", object.translateField(Field.named("foobar"), false).getSql());
		assertEquals("COUNT(test.\"foobar\")", object.translateField(Field.count("foobar", "foo"), true).getSql());
		assertEquals("SUM(test.foobar)", object.translateField(Field.sum("foobar", "foo"), false).getSql());
	}

	@Test
	public void testTranslateSort()
	{
		assertEquals("\"bar\" ASC", object.translateSort(Sort.asc("bar"), true).getSql());
		assertEquals("bar ASC", object.translateSort(Sort.asc("bar"), false).getSql());
		assertEquals("\"bar\" DESC", object.translateSort(Sort.desc("bar"), true).getSql());
		assertEquals("bar DESC", object.translateSort(Sort.desc("bar"), false).getSql());
	}

	@Test
	public void testTranslateCriterion()
	{
		Fragment c1 = object.translateCriterion(Expression.eq("a", 4), false);
		assertEquals("a = ?", c1.getSql());
		assertEquals(4, c1.getParameters().iterator().next());
		Fragment c2 = object.translateCriterion(Junction.and(Expression.eq("b", "foo"), Expression.neq("c", "bar")), false);
		assertEquals("(b = ? AND c <> ?)", c2.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c2.getParameters().toArray());
		Fragment c3 = object.translateCriterion(Expression.eq("a", 4), true);
		assertEquals("\"a\" = ?", c3.getSql());
		assertEquals(4, c3.getParameters().iterator().next());
		Fragment c4 = object.translateCriterion(Junction.and(Expression.eq("b", "foo"), Expression.neq("c", "bar")), true);
		assertEquals("(\"b\" = ? AND \"c\" <> ?)", c4.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c4.getParameters().toArray());
	}

	@Test
	public void testTranslateExpression()
	{
		Fragment e1 = object.translateExpression(Expression.eq("foo", "bar"), false);
		assertEquals("foo = ?", e1.getSql());
		assertArrayEquals(new Object[]{"bar"}, e1.getParameters().toArray());
		
		Fragment e2 = object.translateExpression(Expression.isNull("a"), true);
		assertEquals("\"a\" IS NULL", e2.getSql());
		assertTrue(e2.getParameters().isEmpty());
		
		Fragment e3 = object.translateExpression(Expression.between("b", "a", "z"), true);
		assertEquals("\"b\" BETWEEN ? AND ?", e3.getSql());
		assertArrayEquals(new Object[]{"a", "z"}, e3.getParameters().toArray());
		
		Fragment e4 = object.translateExpression(Expression.notIn("c", 1,2,3,4,5), false);
		assertEquals("c NOT IN (?,?,?,?,?)", e4.getSql());
		assertArrayEquals(new Object[]{1,2,3,4,5}, e4.getParameters().toArray());
		
		Fragment e5 = object.translateExpression(Expression.gt("a", Field.named("b")), true);
		assertEquals("\"a\" > \"b\"", e5.getSql());
		assertTrue(e5.getParameters().isEmpty());
	}

	@Test
	public void testTranslateJunction()
	{
		Fragment c1 = object.translateJunction(Junction.or(Expression.eq("b", "foo"), Expression.neq("c", "bar")), false);
		assertEquals("(b = ? OR c <> ?)", c1.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c1.getParameters().toArray());
		Fragment c2 = object.translateJunction(Junction.and(Expression.eq("b", "foo"), Expression.neq("c", "bar")), false);
		assertEquals("(b = ? AND c <> ?)", c2.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c2.getParameters().toArray());
		Fragment c3 = object.translateJunction(Junction.or(Expression.eq("b", "foo"), Expression.neq("c", "bar")), true);
		assertEquals("(\"b\" = ? OR \"c\" <> ?)", c3.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c3.getParameters().toArray());
		Fragment c4 = object.translateJunction(Junction.and(Expression.eq("b", "foo"), Expression.neq("c", "bar")), true);
		assertEquals("(\"b\" = ? AND \"c\" <> ?)", c4.getSql());
		assertArrayEquals(new Object[]{"foo", "bar"}, c4.getParameters().toArray());
		Fragment c5 = object.translateJunction(Junction.and(Junction.or(Expression.eq("a", 1), Expression.eq("b", 2)), Expression.eq("c", 3)), false);
		assertEquals("((a = ? OR b = ?) AND c = ?)", c5.getSql());
		assertArrayEquals(new Object[]{1, 2, 3}, c5.getParameters().toArray());
	}

	@Test
	public void testTranslateClause()
	{
		SymbolClause<Field> clause = new SymbolClause<Field>();
		clause.add(Field.named("a"))
			.add(Field.named("b"))
			.add(Field.named("c"));
		assertEquals("a, b, c", object.translateClause(clause, false).getSql());
		assertEquals("\"a\", \"b\", \"c\"", object.translateClause(clause, true).getSql());
	}
}
