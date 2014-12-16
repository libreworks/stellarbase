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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.ImmutableSet;
import com.libreworks.stellarbase.text.Strings;

/**
 * An Expression that does an aggregate count.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class CountExpression extends AbstractExpression<Long>
{
	private static final long serialVersionUID = 1L;
	
	private final Expression<?> argument;
	private final boolean distinct;
	
	public static final String COUNT = "COUNT";
	public static final String DISTINCT = "DISTINCT ";
	
	/**
	 * Creates a new CountExpression.
	 * 
	 * <p>If {@code argument} is null, it's equivalent to COUNT(*), and
	 * distinct will be set to false.
	 * 
	 * @param argument the expression to count values
	 * @param distinct whether only distinct values are counted
	 */
	public CountExpression(Expression<?> argument, boolean distinct)
	{
		super(Long.class);
		this.argument = argument;
		this.distinct = argument == null ? false : distinct;
	}

	/**
	 * Aggregates the expression over a collection of values.
	 * 
	 * @param objects the objects to aggregate
	 * @return the aggregated calculation
	 */
	public Long aggregate(Collection<Object> objects)
	{
		if (CollectionUtils.isEmpty(objects)) {
			return NumberUtils.LONG_ZERO;
		}
		if (argument != null) {
			ArrayList<Object> values = new ArrayList<Object>(objects.size());
			for (Object o : objects) {
				values.add(argument.evaluate(o));
			}
			values.removeAll(Collections.singleton(null));
			return Long.valueOf(distinct ? ImmutableSet.copyOf(values).size() : values.size());
		} else {
			return Long.valueOf(objects.size());
		}
	}
	
	/**
	 * @return the argument
	 */
	public Expression<?> getArgument()
	{
		return argument;
	}

	/**
	 * @return the distinct
	 */
	public boolean isDistinct()
	{
		return distinct;
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#evaluate(java.lang.Object)
	 */
	@Override
	public Long evaluate(Object object)
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
		return new StringBuilder().append(COUNT).append('(')
			.append(distinct ? DISTINCT : Strings.EMPTY)
			.append(argument == null ? Strings.STAR : argument).append(')')
			.toString();
	}
}
