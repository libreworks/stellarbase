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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.libreworks.stellarbase.util.Arguments;
import com.libreworks.stellarbase.util.ValueUtils;

/**
 * A Predicate which tests an Expression for null equivalence.
 *  
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class NullPredicate extends AbstractPredicate
{
	private static final long serialVersionUID = 1L;
	
	private final Expression<?> inner;
	
	/**
	 * Creates a new NullPredicate
	 * 
	 * @param inner the inner expression
	 * @param negated whether the expression is negated
	 */
	public NullPredicate(Expression<?> inner, boolean negated)
	{
		super(negated);
		this.inner = Arguments.checkNull(inner);
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
		}
		if (obj == null || !(obj instanceof NullPredicate))
			return false;
		NullPredicate other = (NullPredicate) obj;
		return new EqualsBuilder()
			.append(inner, other.inner)
			.append(isNegated(), other.isNegated())
			.isEquals();
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
			.append(inner)
			.append(isNegated())
			.toHashCode();
	}
	
	/**
	 * @return the inner
	 */
	public Expression<?> getInner()
	{
		return inner;
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Predicate#not()
	 */
	@Override
	public Predicate not()
	{
		return new NullPredicate(inner, true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#evaluate(java.lang.Object)
	 */
	@Override
	public Boolean evaluate(Object object)
	{
		Object a = inner.evaluate(object);
		return isNegated() ? !ValueUtils.equivalentNull(a, null) :
			ValueUtils.equivalentNull(a, null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return inner.toString() + " IS" + (isNegated() ? " NOT" : "") +  " NULL";
	}
}
