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
package net.libreworks.stellarbase.collections;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class DoubleGroupTotalTest
{
	private DoubleGroupTotal object;
	
	@Before
	public void setUp() throws Exception
	{
		object = new DoubleGroupTotal();
	}

	@Test
	public void testAdd()
	{
		object.add("foo", "bar", 9.0);
		object.add("foo", "bar", 17.0);
		object.add("foo", "baz", 1.0);
		assertEquals(26.0, object.get("foo").get("bar").doubleValue(), 0);
		assertEquals(1.0, object.get("foo").get("baz").doubleValue(), 0);
	}

	@Test
	public void testAddAll()
	{
		object.addAll("foo", "bar", Arrays.asList(1.0, 2.0, 3.0, 4.0));
		object.addAll("foo", "baz", Arrays.asList(1.0, 2.0));
		object.addAll("foo", "baz", Arrays.asList(0.5));
		assertEquals(10.0, object.get("foo").get("bar").doubleValue(), 0);
		assertEquals(3.5, object.get("foo").get("baz").doubleValue(), 0);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testPut()
	{
		object.put(null, null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testPutAll()
	{
		object.putAll(null);
	}
}
