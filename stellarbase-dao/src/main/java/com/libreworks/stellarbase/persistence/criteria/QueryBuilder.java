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

import java.util.Collection;

/**
 * An object that can create a Query.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public interface QueryBuilder<T extends QueryBuilder<T>>
{
	/**
	 * Builds the immutable query
	 * 
	 * @return The built Query
	 * @throws IllegalStateException if the table name hasn't been set
	 */
	Query build();
	
	/**
	 * Sets whether the query will omit duplicates in its results.
	 * 
	 * @param distinct Whether the query is distinct
	 * @return provides a fluent interface
	 */
	T distinct(boolean distinct);
	
	/**
	 * Adds group-related Predicates to the query.
	 * 
	 * <p>This method will replace any previous predicates. If {@code predicates}
	 * is null or empty, all previous predicates will be cleared.
	 * 
	 * @param predicates The predicates to add, none of which can be null
	 * @return provides a fluent interface
	 */
	T having(Collection<Expression<Boolean>> predicates);
	
	/**
	 * Add a group-related Predicate to the query.
	 * 
	 * <p>This method will replace any previous predicates. If {@code predicate}
	 * is null or empty, all previous predicates will be cleared.
	 * 
	 * @param predicate The predicate to add, which cannot be null
	 * @return provides a fluent interface
	 */
	T having(Expression<Boolean> predicates);
	
	/**
	 * Adds Order declarations to the query.
	 * 
	 * <p>This method will replace any previous Orders. If {@code orders} is
	 * null or empty, all previous Orders will be cleared.
	 * 
	 * @param order The orders to add, none of which can be null
	 * @return provides a fluent interface
	 */
	T orderBy(Collection<Order> order);
	
	/**
	 * Adds Order declarations to the query.
	 * 
	 * <p>This method will replace any previous Orders. If {@code orders} is
	 * null or empty, all previous Orders will be cleared.
	 * 
	 * @param order The orders to add, none of which can be null
	 * @return provides a fluent interface
	 */
	T orderBy(Order... order);
	
	/**
	 * Adds projections to the query.
	 * 
	 * <p>Any projections that return true for {@link Projection#isGrouped()}
	 * will be added to the query's group by clause. 
	 * 
	 * <p>This method will replace any previous projections (including group by
	 * expressions). If {@code projections} is null or empty, all previous
	 * projections will be cleared.
	 * 
	 * @param projections The projections to add, none of which can be null
	 * @return provides a fluent interface
	 */
	T select(Collection<Projection<?>> projections);
	
	/**
	 * Adds projections to the query.
	 * 
	 * <p>Any projections that return true for {@link Projection#isGrouped()}
	 * will be added to the query's group by clause. 
	 * 
	 * <p>This method will replace any previous projections (including group by
	 * expressions). If {@code projections} is null or empty, all previous
	 * projections will be cleared.
	 * 
	 * @param projections The projections to add, none of which can be null
	 * @return provides a fluent interface
	 */
	T select(Projection<?>... projections);
	
	/**
	 * Sets the offset position of the first result the query will retrieve.
	 * 
	 * @param first the offset
	 * @return provides a fluent interface
	 * @throws IllegalArgumentException if {@code first} is less than zero
	 */
	T setFirstResult(int first);	
	
	/**
	 * Sets the maximum number of results that will be returned.
	 * 
	 * @param max the maximum number
	 * @return provides a fluent interface
	 * @throws IllegalArgumentException if {@code max} is less than one
	 */
	T setMaxResults(int max);

	/**
	 * Adds Predicates to the query.
	 * 
	 * <p>This method will replace any previous predicates. If {@code predicates}
	 * is null or empty, all previous predicates will be cleared.
	 * 
	 * @param predicates The predicates to add, none of which can be null
	 * @return provides a fluent interface
	 */
	T where(Collection<Expression<Boolean>> predicates);
	
	/**
	 * Adds a Predicate to the query.
	 * 
	 * <p>This method will replace any previous predicates. If {@code predicate}
	 * is null or empty, all previous predicates will be cleared.
	 * 
	 * @param predicate The predicate to add, which cannot be null
	 * @return provides a fluent interface
	 */
	T where(Expression<Boolean> predicate);
}
