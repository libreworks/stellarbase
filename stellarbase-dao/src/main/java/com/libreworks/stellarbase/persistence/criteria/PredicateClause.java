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
package com.libreworks.stellarbase.persistence.criteria;

import java.util.List;

/**
 * A predicate clause is an infix expression of {@link Predicate} objects.
 * 
 * <p>A typical example of a junction is a SQL where clause: 
 * <code>( <var>predicate</var> AND <var>predicate</var> )</code> 
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public interface PredicateClause extends Predicate, Iterable<Expression<Boolean>>
{
	/**
	 * Gets the boolean expressions in the clause, none of which are null
	 * 
	 * @return The boolean expressions
	 */
	public List<Expression<Boolean>> getSymbols();
	
	/**
	 * Whether true if this is a conjunction, false if a disjunction.
	 * 
	 * @return Whether this predicate is a conjunction
	 */
	boolean isConjunction();
}
