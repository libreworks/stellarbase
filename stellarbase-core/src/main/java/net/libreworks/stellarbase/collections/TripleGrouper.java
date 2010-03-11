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
 * A map wrapper which simplifies grouping three key values.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <A> The far outer group type
 * @param <B> The outer group type
 * @param <C> The inner group type
 * @param <V> The element type
 */
public class TripleGrouper<A,B,C,V> extends AbstractMapDelegate<A,Map<B,Map<C,Collection<V>>>>
{
    private static final long serialVersionUID = 1L;
	private TripleGrouperFactory<A,B,C,V> factory;
	
	/**
	 * Creates a new TripleGrouper using a {@link LinkedHashMap} for the groups (guarantees insert order for iteration).
	 */
	public TripleGrouper()
	{
		this(new TripleGrouperFactory<A,B,C,V>()
		{
			public Map<A,Map<B,Map<C,Collection<V>>>> getFarOuterGroupContainer()
		    {
			    return new LinkedHashMap<A,Map<B,Map<C,Collection<V>>>>();
		    }
			public Map<B,Map<C,Collection<V>>> getOuterGroupContainer()
		    {
			    return new LinkedHashMap<B,Map<C,Collection<V>>>();
		    }
			public Collection<V> getContainer()
		    {
			    return new ArrayList<V>();
		    }
			public Map<C,Collection<V>> getGroupContainer()
		    {
			    return new LinkedHashMap<C,Collection<V>>();
		    }			
		});
	}
	
	/**
	 * Creates a new TripleGrouper using a custom factory
	 * 
	 * @param factory The custom factory
	 */
	public TripleGrouper(TripleGrouperFactory<A,B,C,V> factory)
	{
		super(factory.getFarOuterGroupContainer());
		this.factory = factory;
	}

	protected void ensureCapacity(A group1, B group2, C group3)
	{
		if (!delegate.containsKey(group1)){
			delegate.put(group1, factory.getOuterGroupContainer());
		}
		if (!delegate.get(group1).containsKey(group2)){
			delegate.get(group1).put(group2, factory.getGroupContainer());
		}
		if (!delegate.get(group1).get(group2).containsKey(group3)){
			delegate.get(group1).get(group2).put(group3, factory.getContainer());
		}
	}
	
	/**
	 * Adds an element to the group.
	 * 
	 * @param group1 First group key
	 * @param group2 Second group key
	 * @param group3 Third group key
	 * @param value The element to add
	 */
	public void add(A group1, B group2, C group3, V value)
	{
		ensureCapacity(group1, group2, group3);
		delegate.get(group1).get(group2).get(group3).add(value);
	}
	
	/**
	 * Adds a collection of elements to the group
	 * 
	 * @param group1 First group key
	 * @param group2 Second group key
	 * @param group3 Third group key
	 * @param values The elements to add
	 */
	public void addAll(A group1, B group2, C group3, Collection<V> values)
	{
		ensureCapacity(group1, group2, group3);
		delegate.get(group1).get(group2).get(group3).addAll(values);
	}
	
	/**
	 * Adds a map of collections to the group
	 * 
	 * @param group1 First group key
	 * @param group2 Second group key
	 * @param groups Map of third group keys to collections of values
	 */
	public void addAll(A group1, B group2, Map<C,Collection<V>> groups)
	{
		if (!delegate.containsKey(group1)){
			delegate.put(group1, factory.getOuterGroupContainer());
		}
		if (!delegate.get(group1).containsKey(group2)){
			delegate.get(group1).put(group2, factory.getGroupContainer());
		}
		delegate.get(group1).get(group2).putAll(groups);
	}
	
	/**
	 * Adds a map of groups to the group
	 *  
	 * @param group1 First group key
	 * @param groups Map of second group keys to maps of third group keys to values
	 */
	public void addAll(A group1, Map<B,Map<C,Collection<V>>> groups)
	{
		for(Map.Entry<B,Map<C,Collection<V>>> entry : groups.entrySet()){
			addAll(group1, entry.getKey(), entry.getValue());
		}
	}
	
	public Map<B,Map<C,Collection<V>>> put(A key, Map<B,Map<C,Collection<V>>> value)
    {
		throw new UnsupportedOperationException("Use the add method");
    }

	public void putAll(Map<? extends A,? extends Map<B,Map<C,Collection<V>>>> m)
    {
		throw new UnsupportedOperationException("Use the add method");
    }
}
