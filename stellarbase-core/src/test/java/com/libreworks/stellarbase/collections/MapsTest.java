/**
 * Copyright 2014 LibreWorks contributors
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
package com.libreworks.stellarbase.collections;

import static org.junit.Assert.*;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

public class MapsTest
{
	@Test
	public void testLinked()
	{
		LinkedHashMap<String,Integer> expected = new LinkedHashMap<String, Integer>();
		expected.put("foo", 1);
		expected.put("bar", 2);
		expected.put("baz", 3);
		expected.put("hi", 4);
		expected.put("bye", 5);
		expected.put("later", 6);
		
		Maps.MapBuilder<String,Integer> object = Maps.<String,Integer>newLinked()
			.add("foo", 1)
			.add("bar", 2, "baz", 3)
			.add("hi", 4, "bye", 5, "later", 6);
		assertEquals(expected, object.toMap());
		assertEquals(ImmutableMap.copyOf(expected), object.toImmutableMap());
	}
	
	@Test
	public void testSorted()
	{
		TreeMap<String,Integer> expected = new TreeMap<String, Integer>();
		expected.put("foo", 1);
		expected.put("bar", 2);
		expected.put("baz", 3);
		expected.put("hi", 4);
		expected.put("bye", 5);
		expected.put("later", 6);
		
		Maps.SortedBuilder<String,Integer> object = Maps.<String,Integer>newSorted()
			.add("foo", 1)
			.add("bar", 2, "baz", 3, "hi", 4, "bye", 5, "later", 6);
		assertEquals(expected, object.toMap());
		assertEquals(ImmutableSortedMap.copyOfSorted(expected), object.toImmutableMap());
	}

	@Test
	public void testEnum()
	{
		EnumMap<Color,String> fixture = new EnumMap<Color,String>(Color.class);
		fixture.put(Color.RED, "aoeu");
		fixture.put(Color.ORANGE, "htns");
		
		EnumMap<Color,String> expected = new EnumMap<Color,String>(Color.class);
		expected.put(Color.YELLOW, "asdf");
		expected.put(Color.GREEN, "jkl;");
		expected.put(Color.BLUE, "hello");
		
		Maps.EnumBuilder<Color,String> object = Maps.newEnum(fixture)
			.addAll(expected);
		expected.putAll(fixture);
		assertEquals(expected, object.toMap());
		assertEquals(ImmutableMap.copyOf(expected), object.toImmutableMap());
	}
	
	enum Color
	{
		RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET;
	}
}
