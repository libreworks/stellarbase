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
package net.libreworks.stellarbase.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A collection of Symbols.
 * 
 * Ported from the PHP framework Xyster by Jonathan Hawk, the original author.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class SymbolClause<T extends Symbol> implements Clause<T>
{
	private static final long serialVersionUID = 1L;
	
	protected ArrayList<T> items = new ArrayList<T>();
	
	/**
	 * Creates a new empty Clause.
	 */
	public SymbolClause()
	{
	}
	
	/**
	 * Creates a new Clause filling it with the provided symbols
	 * 
	 * @param symbols The symbols to add
	 */
	public SymbolClause(Collection<? extends T> symbols)
	{
		items.addAll(symbols);
	}
	
	/**
	 * Creates a new Clause filling it with the provided symbol
	 * 
	 * @param symbol The symbol to add
	 */
	public SymbolClause(T symbol)
	{
		items.add(symbol);
	}

	public SymbolClause<T> add(T e)
	{
		items.add(e);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof SymbolClause))
			return false;
		SymbolClause<?> other = (SymbolClause<?>) obj;
		return items.equals(other.items);
	}

	public Collection<T> getSymbols()
	{
		return Collections.unmodifiableCollection(items);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(3, 17)
			.append(items)
			.toHashCode();
	}

	public boolean isEmpty()
	{
		return items.isEmpty();
	}

	public Iterator<T> iterator()
	{
		return items.iterator();
	}

	public SymbolClause<T> merge(Clause<? extends T> c)
	{
		items.addAll(c.getSymbols());
		return this;
	}

	public boolean remove(T o)
	{
		return items.remove(o);
	}

	public int size()
	{
		return items.size();
	}
	
	@Override
	public String toString()
	{
		ArrayList<String> stringified = new ArrayList<String>();
		for(T item : items){
			stringified.add(item.toString());
		}
		return StringUtils.join(stringified.toArray(), ", ");
	}
}
