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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Objects;
import com.libreworks.stellarbase.math.SafeMath;
import com.libreworks.stellarbase.util.Arguments;

/**
 * An Expression which can calculate an aggregate value over a set of results.
 *  
 * @author Jonathan Hawk
 * @since 1.0.0
 * @param <T> The evaluated return type
 */
public class AggregateExpression<T> extends AbstractExpression<T>
{
	private static final long serialVersionUID = 1L;

	private final Function function;
	private final Expression<?> argument;
	
	/**
	 * Creates a new aggregate function
	 * 
	 * @param function The set function, must not be null
	 * @param argument The inner expression, must not be null
	 * @param javaType The type the aggregate function will return
	 */
	public AggregateExpression(Function function, Expression<?> argument, Class<T> javaType)
	{
		super(javaType);
		this.function = Arguments.checkNull(function);
		this.argument = Arguments.checkNull(argument);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		} else if (obj instanceof AggregateExpression) {
			@SuppressWarnings("rawtypes")
			AggregateExpression other = (AggregateExpression) obj;
			return Objects.equal(function, other.function) &&
				Objects.equal(argument, other.argument) &&
				Objects.equal(getJavaType(), getJavaType());
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(function, argument, getJavaType());
	}
	
	/**
	 * @return the function
	 */
	public Function getFunction()
	{
		return function;
	}

	/**
	 * @return the argument
	 */
	public Expression<?> getArgument()
	{
		return argument;
	}

	/**
	 * Performs the aggregate function on a set of objects.
	 * 
	 * <p>In the case of SUM and AVG, this method will forcibly convert
	 * evaluated values into a Number via
	 * {@link SafeMath#value(Class, Object)}. This means that non-numbers are
	 * evaluated as zero.
	 * 
	 * <p>For MIN and MAX, the evaluated values must be Comparable.
	 * 
	 * @param objects The objects to aggregate
	 * @return The aggregate result
	 * @throws ClassCastException if the Expression return type isn't compatible with the aggregate result type
	 * @throws IllegalStateException if using MIN or MAX and one of the evaluated values isn't Comparable 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T aggregate(Collection<Object> objects)
	{
		if (Function.AVG == function || Function.SUM == function) {
			Class<? extends Number> nc = Number.class.isAssignableFrom(getJavaType()) ?
				(Class<? extends Number>)getJavaType() : BigDecimal.class; // safe cast because of ternary
			ArrayList<BigDecimal> values = new ArrayList<BigDecimal>(objects.size());
			for (Object o : objects) {
				values.add(SafeMath.value(BigDecimal.class, argument.evaluate(o)));
			}
			Number sum = SafeMath.sum(values, nc);
			// might throw ClassCastException
			return getJavaType().cast(Function.AVG == function ?
				SafeMath.divide(sum, objects.size(), nc) : sum);
		} else {
			ArrayList<Comparable> evals = new ArrayList<Comparable>(objects.size());
			for (Object o : objects) {
				Object eval = argument.evaluate(o);
				if (eval != null && !Comparable.class.isAssignableFrom(eval.getClass())) {
					throw new IllegalStateException("The " + function.name() + " function can only operate on Comparable objects");
				}				
				evals.add((Comparable) eval); // safe cast because we would have thrown the exception otherwise
			}
			// might throw ClassCastException
			return (T) (Function.MAX == function ? Collections.max(evals) : Collections.min(evals));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @throws UnsupportedOperationException because an aggregate function can't be evaluated on a single object
	 */
	@Override
	public T evaluate(Object object)
	{
		throw new UnsupportedOperationException("There is no way to resolve this function with a single result");
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new StringBuilder().append(function.name()).append('(')
			.append(argument).append(')').toString();
	}
	
	/**
	 * Creates a new aggregate expression for the MAX set function.
	 * 
	 * @param argument the inner expression 
	 * @return the new aggregate expression
	 */	
	@SuppressWarnings("unchecked")
	public static <X> AggregateExpression<X> max(Expression<X> argument)
	{
		return new AggregateExpression<X>(Function.MAX, argument, (Class<X>) argument.getJavaType());
	}
	
	/**
	 * Creates a new aggregate expression for the MIN set function.
	 * 
	 * @param argument the inner expression 
	 * @return the new aggregate expression
	 */	
	@SuppressWarnings("unchecked")
	public static <X> AggregateExpression<X> min(Expression<X> argument)
	{
		return new AggregateExpression<X>(Function.MIN, argument, (Class<X>) argument.getJavaType());
	}
	
	/**
	 * Creates a new aggregate expression for the SUM set function.
	 * 
	 * @param argument the inner expression 
	 * @return the new aggregate expression
	 */	
	public static AggregateExpression<Number> sum(Expression<?> argument)
	{
		return new AggregateExpression<Number>(Function.SUM, argument, Number.class);
	}
	
	/**
	 * Creates a new aggregate expression for the AVG set function.
	 * 
	 * @param argument the inner expression 
	 * @return the new aggregate expression
	 */
	public static AggregateExpression<Number> avg(Expression<?> argument)
	{
		return new AggregateExpression<Number>(Function.AVG, argument, Number.class);
	}
	
	/**
	 * Represents the set functions.
	 */
	public static enum Function
	{
		SUM,
		MAX,
		MIN,
		AVG;
	}	
}
