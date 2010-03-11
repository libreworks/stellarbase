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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import org.junit.Test;

/**
 * Test for {@link SingleGrouper}
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class SingleGrouperTest
{
	/**
	 * Tests the constructor
	 */
	@Test
	public void testConstructor()
	{
		StubSingleGrouperFactory factory = new StubSingleGrouperFactory();
		SingleGrouper<String,Double> object = new SingleGrouper<String,Double>(factory);
		ArrayList<Double> decimals = new ArrayList<Double>();
		decimals.add(0.1);
		decimals.add(0.2);
		decimals.add(0.3);
		object.addAll("doubles", decimals);
		assertTrue(factory.getContainerCalled);
		assertTrue(factory.getGroupContainerCalled);
		assertEquals(HashSet.class, object.get("doubles").getClass());
	}
	
	/**
	 * Tests the {@link SingleGrouper#add(Object, Object)} method
	 */
	@Test
	public void testAdd()
	{
		SingleGrouper<String,Integer> object = new SingleGrouper<String,Integer>();
		for (int i = 0; i < 10; i++) {
			String key = (i % 2 == 0) ? "foo" : "bar";
			object.add(key, new Integer(i));
		}
		assertTrue(object.containsKey("foo"));
		assertEquals(5, object.get("foo").size());
		assertEquals(Arrays.asList(new Integer(0), new Integer(2), new Integer(
		    4), new Integer(6), new Integer(8)), object.get("foo"));
		assertTrue(object.containsKey("bar"));
		assertEquals(5, object.get("bar").size());
		assertEquals(Arrays.asList(new Integer(1), new Integer(3), new Integer(
		    5), new Integer(7), new Integer(9)), object.get("bar"));
	}

	/**
	 * Test for {@link SingleGrouper#addAll(Object, java.util.Collection)}
	 */
	@Test
	public void testAddAll()
	{
		Collection<Integer> evens = Arrays.asList(new Integer(0),
		    new Integer(2), new Integer(4), new Integer(6), new Integer(8));
		Collection<Integer> odds = Arrays.asList(new Integer(1),
		    new Integer(3), new Integer(5), new Integer(7), new Integer(9));
		SingleGrouper<String,Integer> object = new SingleGrouper<String,Integer>();
		object.addAll("odds", odds);
		object.addAll("evens", evens);
		assertTrue(object.containsKey("evens"));
		assertEquals(5, object.get("evens").size());
		assertEquals(evens, object.get("evens"));
		assertTrue(object.containsKey("odds"));
		assertEquals(5, object.get("odds").size());
		assertEquals(odds, object.get("odds"));
	}

	/**
	 * Test for {@link SingleGrouper#put(Object, java.util.Collection)}
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testPut()
	{
		SingleGrouper<String,Integer> object = new SingleGrouper<String,Integer>();
		object.put(null, null);
	}

	/**
	 * Test for {@link SingleGrouper#putAll(java.util.Map)}
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testPutAll()
	{
		SingleGrouper<String,Integer> object = new SingleGrouper<String,Integer>();
		object.putAll(null);
	}
	
	protected class StubSingleGrouperFactory implements SingleGrouperFactory<String,Double>
	{
		protected boolean getContainerCalled = false;
		protected boolean getGroupContainerCalled = false;
		
		public Collection<Double> getContainer()
        {
			getContainerCalled = true;
	        return new HashSet<Double>();
        }

		public Map<String,Collection<Double>> getGroupContainer()
        {
			getGroupContainerCalled = true;
	        return new TreeMap<String,Collection<Double>>();
        }
	}
}
