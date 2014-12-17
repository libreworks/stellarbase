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
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.libreworks.stellarbase.util.Arguments;
import com.libreworks.stellarbase.util.ValueUtils;

/**
 * A Predicate which determines an Expression's presence in a list of values.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class InPredicate extends AbstractPredicate
{
	private static final long serialVersionUID = 1L;
	
	private final Expression<?> inner;
	private final ImmutableList<Expression<?>> values;

	/**
	 * Creates a new InPredicate
	 * 
	 * @param inner the inner expression
	 * @param values the values against which {@code inner} is tested
	 * @param negated whether this predicate is negated
	 */
	public InPredicate(Expression<?> inner, Collection<? extends Expression<?>> values, boolean negated)
	{
		super(negated);
		this.inner = Arguments.checkNull(inner);
		this.values = ImmutableList.copyOf(Arguments.checkEmpty(values));
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
		} else if (obj instanceof InPredicate) {
			InPredicate other = (InPredicate) obj;
			return Objects.equal(inner, other.inner) &&
				Objects.equal(values, other.values) &&
				isNegated() == other.isNegated();
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
		return Objects.hashCode(inner, values,isNegated());
	}
	
	/**
	 * @return the inner
	 */
	public Expression<?> getInner()
	{
		return inner;
	}

	/**
	 * @return the values
	 */
	public List<Expression<?>> getValues()
	{
		return values;
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Predicate#not()
	 */
	@Override
	public Predicate not()
	{
		return new InPredicate(inner, values, true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#evaluate(java.lang.Object)
	 */
	@Override
	public Boolean evaluate(Object object)
	{
		Object a = inner.evaluate(object);
		for (Expression<?> expression : values) {
			if (nullSafeEquals(a, expression.evaluate(object))) {
				return !isNegated();
			}
		}
		return isNegated();
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return inner.toString() + (isNegated() ? " NOT" : "") + " IN (" +
			Joiner.on(", ").join(values) + ")"; 
	}
	
	private boolean nullSafeEquals(Object a, Object b)
	{
		return Objects.equal(a, b) || ValueUtils.equivalentNull(a, b);
	}
}
