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
 * Represents a value returned from a query; vaguely akin to {@link javax.persistence.Selection}.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public interface Expression<T> extends Symbol
{
	/**
	 * Attempts to evaluate this Expression against the passed object.
	 * 
	 * @param object The object against which to evaluate this Expression
	 * @return the evaluated result
	 */
	T evaluate(Object object);

	/**
	 * Gets the runtime Java type returned by evaluating this Expression
	 * 
	 * @return The runtime Java type returned by evaluating this Expression
	 */
	Class<? extends T> getJavaType();
	
	/**
	 * Creates a Projection for this expression.
	 * 
	 * @param alias The alias (or null)
	 * @return the created Projection
	 */
	Projection<T> as(String alias);
	
	/**
	 * Creates a Group Projection for this expression.
	 * 
	 * @param alias The alias (or null)
	 * @return the created Projection
	 */
	Projection<T> grouped(String alias);
	
	/**
	 * Creates an ascending Order for this field.
	 * 
	 * @return The ascending Order
	 */
	Order asc();

	/**
	 * Creates a descending Order for this field.
	 * 
	 * @return The descending Order
	 */
	Order desc();

	/**
	 * Creates an equals Predicate for this field.
	 * 
	 * @param value The comparison value
	 * @return The equals Predicate
	 */
	Predicate eq(Expression<?> value);

	/**
	 * Creates a not-equals Predicate for this field.
	 * 
	 * @param value The comparison value
	 * @return The equals Predicate
	 */
	Predicate ne(Expression<?> value);

	/**
	 * Creates a less-than Predicate for this field.
	 * 
	 * @param value The comparison value
	 * @return The less than Predicate
	 */
	Predicate lt(Expression<?> value);

	/**
	 * Creates a less-than-or-equal-to Predicate for this field.
	 * 
	 * @param value The comparison value
	 * @return The less than Predicate
	 */
	Predicate le(Expression<?> value);

	/**
	 * Creates a greater-than Predicate for this field.
	 * 
	 * @param value The comparison value
	 * @return The greater than Predicate
	 */
	Predicate gt(Expression<?> value);

	/**
	 * Creates a greater-than-or-equal-to Predicate for this field.
	 * 
	 * @param value The comparison value
	 * @return The greater than Predicate
	 */
	Predicate ge(Expression<?> value);

	/**
	 * Creates a LIKE Predicate for this field.
	 * 
	 * @param value The comparison value
	 * @return The LIKE Predicate
	 */
	Predicate like(Expression<?> value);

	/**
	 * Creates a NOT LIKE Predicate for this field.
	 * 
	 * @param value The comparison value
	 * @return The NOT LIKE Predicate
	 */
	Predicate notLike(Expression<?> value);

	/**
	 * Creates a BETWEEN Predicate for this field.
	 * 
	 * @param a The starting value
	 * @param b The ending value
	 * @return The BETWEEN Predicate
	 */
	Predicate between(Expression<? extends Comparable<?>> a, Expression<? extends Comparable<?>> b);

	/**
	 * Creates a NOT BETWEEN Predicate for this field.
	 * 
	 * @param a The starting value
	 * @param b The ending value
	 * @return The NOT BETWEEN Predicate
	 */
	Predicate notBetween(Expression<? extends Comparable<?>> a,
		Expression<? extends Comparable<?>> b);

	/**
	 * Creates an IN Predicate for this field.
	 * 
	 * @param values The values
	 * @return The IN Predicate
	 */
	Predicate in(Expression<?>... values);

	/**
	 * Creates an IN Predicate for this field.
	 * 
	 * @param values The values
	 * @return The IN Predicate
	 */
	Predicate in(Collection<? extends Expression<?>> values);

	/**
	 * Creates a NOT IN Predicate for this field.
	 * 
	 * @param values The values
	 * @return The NOT IN Predicate
	 */
	Predicate notIn(Expression<?>... values);

	/**
	 * Creates a NOT IN Predicate for this field.
	 * 
	 * @param values The values
	 * @return The NOT IN Predicate
	 */
	Predicate notIn(Collection<? extends Expression<?>> values);

	/**
	 * Creates an IS NULL Predicate for this field.
	 * 
	 * @return The IS NULL Predicate
	 */
	Predicate isNull();

	/**
	 * Creates an IS NOT NULL Predicate for this field.
	 * 
	 * @return The IS NOT NULL Predicate
	 */
	Predicate isNotNull();

	/**
	 * Creates a new Expression to count the values in a tuple
	 * 
	 * @return The Expression created
	 */
	Expression<Long> count();

	/**
	 * Creates a new Expression to count the values in a tuple
	 * 
	 * @return The Expression created
	 */
	Expression<Long> countDistinct();

	/**
	 * Creates a new Expression to sum the values in a field
	 * 
	 * @return The Expression created
	 */
	Expression<Number> sum();

	/**
	 * Creates a new Expression to average the values in a field
	 * 
	 * @return The Expression created
	 */
	Expression<Number> avg();

	/**
	 * Creates a new Expression to find the maximum value in a field
	 * 
	 * @return The Expression created
	 */
	Expression<T> max();

	/**
	 * Creates a new Expression to find the minimum value in a field
	 * 
	 * @return The Expression created
	 */
	Expression<T> min();
}
