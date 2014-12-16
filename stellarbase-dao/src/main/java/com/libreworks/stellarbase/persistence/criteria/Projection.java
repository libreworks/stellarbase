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
 * A query result set projection made up of an Expression and an optional alias.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 * @param <T> The type of contained Expression
 */
public interface Projection<T> extends Symbol
{
	/**
	 * Gets the contained Expression that this projection returns.
	 * 
	 * @return the contained Expression
	 */
	Expression<T> getExpression();
	
	/**
	 * Gets the alias (if any) for this projection
	 * 
	 * @return the alias (or null)
	 */
	String getAlias();
	
	/**
	 * Whether this projection appears in the query's Group By clause.
	 * 
	 * @return whether this projection is also in the group by clause
	 */
	boolean isGrouped();
}
