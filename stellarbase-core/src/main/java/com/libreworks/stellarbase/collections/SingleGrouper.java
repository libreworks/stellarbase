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
package com.libreworks.stellarbase.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A map wrapper which simplifies grouping values.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <K> The group type
 * @param <V> The element type
 */
public class SingleGrouper<K,V> extends AbstractMapDelegate<K,Collection<V>>
{
    private static final long serialVersionUID = 1L;
    
	private SingleGrouperFactory<K,V> factory;
	
	/**
	 * Creates a new SingleGrouper using a LinkedHashMap for the groups (guarantees insert order for interation). 
	 */
	public SingleGrouper()
    {
	    this(new SingleGrouperFactory<K,V>()
		{
			public Collection<V> getContainer()
            {
	            return new ArrayList<V>();
            }
			public Map<K,Collection<V>> getGroupContainer()
            {
	            return new LinkedHashMap<K,Collection<V>>();
            }
		});
    }

	/**
	 * Creates a new SingleGrouper using a custom factory
	 * 
	 * @param factory The custom factory
	 */
	public SingleGrouper(SingleGrouperFactory<K,V> factory)
	{
		super(factory.getGroupContainer());
		this.factory = factory;
	}
	
	/**
	 * Adds an element to a group.
	 * 
	 * @param group The group
	 * @param element The element
	 */
	public void add(K group, V element)
	{
		if (!delegate.containsKey(group)){
			delegate.put(group, factory.getContainer());
		}
		delegate.get(group).add(element);
	}
	
	/**
	 * Adds the elements to a group.
	 * 
	 * @param group The group
	 * @param elements The elements
	 */
	public void addAll(K group, Collection<? extends V> elements)
	{
		if (!delegate.containsKey(group)){
			delegate.put(group, factory.getContainer());
		}
		delegate.get(group).addAll(elements);
	}
	
	public Collection<V> put(K key, Collection<V> value)
    {
		throw new UnsupportedOperationException("Use the add method");
    }

	public void putAll(Map<? extends K,? extends Collection<V>> m)
    {
		throw new UnsupportedOperationException("Use the add method");
    }
}
