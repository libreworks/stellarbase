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

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.springframework.util.Assert;

/**
 * Simple Map delegate class.
 * 
 * Note that delegate Maps must be Serializable.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <K> The map key type
 * @param <V> The map value type
 */
@SuppressWarnings("serial")
public abstract class AbstractMapDelegate<K,V> implements Map<K,V>, Serializable
{
	protected Map<K,V> delegate;

	/**
	 * @param delegate the map to wrap
	 */
	public AbstractMapDelegate(Map<K,V> delegate)
	{
		Assert.notNull(delegate);
		if (!(delegate instanceof Serializable)){
			throw new IllegalArgumentException("Delegate map must be Serializable");
		}
		this.delegate = delegate;
	}

	/**
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		delegate.clear();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key)
	{
		return delegate.containsKey(key);
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value)
	{
		return delegate.containsValue(value);
	}

	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	public Set<java.util.Map.Entry<K,V>> entrySet()
	{
		return delegate.entrySet();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.Map#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
    public boolean equals(Object o)
    {
	    return this == o || delegate.equals(o) ||
	    	(o instanceof AbstractMapDelegate &&
	    		delegate.equals(((AbstractMapDelegate)o).delegate));
    }

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public V get(Object key)
	{
		return delegate.get(key);
	}

	/**
	 * @return
	 * @see java.util.Map#hashCode()
	 */
	public int hashCode()
	{
		return delegate.hashCode();
	}

	/**
	 * @return
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		return delegate.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.Map#keySet()
	 */
	public Set<K> keySet()
	{
		return delegate.keySet();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public V remove(Object key)
	{
		return delegate.remove(key);
	}

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		return delegate.size();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return delegate.toString();
	}
	
	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	public Collection<V> values()
	{
		return delegate.values();
	}
}
