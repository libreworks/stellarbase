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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A map wrapper which simplifies grouping two key values.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <A> The outer group type
 * @param <B> The inner group type
 * @param <V> The element type
 */
public class DoubleGrouper<A,B,V> extends AbstractMapDelegate<A,Map<B,Collection<V>>>
{
    private static final long serialVersionUID = 1L;
    
	private DoubleGrouperFactory<A,B,V> factory;
	
	/**
	 * Creates a new DoubleGrouper using a {@link LinkedHashMap} for the groups (guarantees insert order for iteration).
	 */
	public DoubleGrouper()
    {
		this(new DoubleGrouperFactory<A,B,V>()
		{
			public Map<A,Map<B,Collection<V>>> getOuterGroupContainer()
            {
	            return new LinkedHashMap<A,Map<B,Collection<V>>>();
            }
			public Collection<V> getContainer()
            {
	            return new ArrayList<V>();
            }
			public Map<B,Collection<V>> getGroupContainer()
            {
	            return new LinkedHashMap<B,Collection<V>>();
            }
		});
    }
	
	/**
	 * Creates a new DoubleGrouper using a custom factory
	 * 
	 * @param factory The custom factory
	 */
	public DoubleGrouper(DoubleGrouperFactory<A,B,V> factory)
	{
		super(factory.getOuterGroupContainer());
		this.factory = factory;
	}
	
	/**
	 * Adds an element to the group.
	 * 
	 * @param group1 First group key
	 * @param group2 Second group key
	 * @param value The value to add
	 */
	public void add(A group1, B group2, V value)
	{
		if (!delegate.containsKey(group1)){
			delegate.put(group1, factory.getGroupContainer());
		}
		if (!delegate.get(group1).containsKey(group2)){
			delegate.get(group1).put(group2, factory.getContainer());
		}
		delegate.get(group1).get(group2).add(value);
	}
	
	/**
	 * Adds a collection of elements to the group
	 * 
	 * @param group1 First group key
	 * @param group2 Second group key
	 * @param values The value to add
	 */
	public void addAll(A group1, B group2, Collection<? extends V> values)
	{
		if (!delegate.containsKey(group1)){
			delegate.put(group1, factory.getGroupContainer());
		}
		if (!delegate.get(group1).containsKey(group2)){
			delegate.get(group1).put(group2, factory.getContainer());
		}
		delegate.get(group1).get(group2).addAll(values);
	}
	
	/**
	 * Adds a map of collections to the group
	 * 
	 * @param group1 First group key
	 * @param groups Map of second group keys to collection of values
	 */
	public void addAll(A group1, Map<B,? extends Collection<V>> groups)
	{
		if (!delegate.containsKey(group1)){
			delegate.put(group1, factory.getGroupContainer());
		}
		delegate.get(group1).putAll(groups);
	}

	public Map<B,Collection<V>> put(A key, Map<B,Collection<V>> value)
    {
		throw new UnsupportedOperationException("Use the add method");
    }

	public void putAll(Map<? extends A,? extends Map<B,Collection<V>>> m)
    {
		throw new UnsupportedOperationException("Use the add method");
    }
}
