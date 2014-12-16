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

/**
 * A Predicate is a boolean statement
 * 
 * <p>A typical example of a predicate is a Java variable comparison:
 * <code><var>x</var> &gt; 1</code> 
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public interface Predicate extends Expression<Boolean>
{
	/**
	 * Whether this predicate is negated
	 * 
	 * @return Whether this predicate is negated
	 */
	boolean isNegated();
	
	/**
	 * Creates a negation of this predicate
	 * 
	 * @return A negation of this predicate
	 */
	Predicate not();
}
