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

import java.util.Collection;

/**
 * A collection of Symbols.
 * 
 * Ported from the PHP framework Xyster by Jonathan Hawk, the original author.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 * @param <T> The type of element in the clause
 */
public interface Clause<T extends Symbol> extends Symbol, Iterable<T>
{
	/**
	 * Adds the symbol to the clause
	 * 
	 * @param e The symbol to add
	 * @return provides a fluent interface
	 */
	public Clause<T> add(T e);
	
	/**
	 * Gets the symbols in the clause
	 * 
	 * @return The symbols in the clause
	 */
	public Collection<T> getSymbols();

	/**
	 * Whether the clause is empty
	 * 
	 * @return whether the clause is empty
	 */
	public boolean isEmpty();
	
	/**
	 * Removes a symbol from the clause.
	 * 
	 * @param e The symbol to remove
	 * @return Whether the clause was modified
	 */
	public boolean remove(T e);
	
	/**
	 * The number of symbols in the clause
	 * 
	 * @return The number of symbols in the clause
	 */
	public int size();
}