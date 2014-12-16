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
 * Immutable specification of a query.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public interface Query
{
	/**
	 * Gets the offset position of the first result the query will retrieve.
	 * 
	 * @return the offset position
	 */
	int getFirstResult();
	
	/**
	 * Gets the group by expressions of this query.
	 * 
	 * @return the group expressions or an empty Clause
	 */
	List<Expression<?>> getGroupBy();
	
	/**
	 * Gets the group restrictions of this query.
	 * 
	 * @return the group restrictions or null
	 */
	Predicate getHaving();
	
	/**
	 * Gets the maximum number of results that will be returned.
	 * 
	 * <p>If not specified, this will return {@link Integer#MAX_VALUE}.
	 * 
	 * @return the max results
	 */
	int getMaxResults();
	
	/**
	 * Gets the sort order of this query.
	 * 
	 * @return the sort order or an empty Clause
	 */
	List<Order> getOrderBy();
	
	/**
	 * Gets the projections of this query (including any group projections).
	 * 
	 * <p>In the event that <em>all</em> projections are allowed, this will
	 * return null (e.g. <code>SELECT * FROM x</code>).
	 * 
	 * @return the projections or an empty Clause
	 */
	List<Projection<?>> getSelect();
	
	/**
	 * Gets the restrictions of this query
	 * 
	 * @return the restrictions or null
	 */
	Predicate getWhere();

	/**
	 * Gets whether the query returns unique results.
	 * 
	 * @return whether the query returns unique results
	 */
	boolean isDistinct();
}
