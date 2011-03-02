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
import java.util.HashMap;
import org.junit.Test;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class FluentValuesTest
{

	/**
	 * Test method for {@link net.libreworks.stellarbase.collections.FluentValues#set(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testSet()
	{
		FluentValues object = new FluentValues();
		assertSame(object, object.set("foo", "bar"));
		assertEquals("bar", object.get("foo"));
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.collections.FluentValues#setAll(java.util.Map)}.
	 */
	@Test
	public void testSetAll()
	{
		HashMap<String,Object> values = new HashMap<String,Object>();
		values.put("name", "Bob");
		values.put("age", new Integer(41));
		FluentValues object = new FluentValues();
		assertSame(object, object.setAll(values));
		assertEquals(values, object);
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.collections.FluentValues#put(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testPut()
	{
		FluentValues object = new FluentValues();
		object.put("foo", "bar");
		assertEquals("bar", object.get("foo"));
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.collections.FluentValues#putAll(java.util.Map)}.
	 */
	@Test
	public void testPutAll()
	{
		HashMap<String,Object> values = new HashMap<String,Object>();
		values.put("name", "Bob");
		values.put("age", new Integer(41));
		FluentValues object = new FluentValues();
		object.putAll(values);
		assertEquals(values, object);
	}
	
	/**
	 * Test method for {@link FluentValues#toString()}
	 */
	@Test
	public void testToString()
	{
		HashMap<String,Object> values = new HashMap<String,Object>();
		values.put("name", "Bob");
		values.put("age", new Integer(41));
		FluentValues object = new FluentValues(values);
		assertEquals(values.toString(), object.toString());
	}
}
