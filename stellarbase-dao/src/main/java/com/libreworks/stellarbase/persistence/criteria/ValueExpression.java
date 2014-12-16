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

import com.google.common.base.Supplier;
import com.libreworks.stellarbase.util.Arguments;

/**
 * An expression that returns a constant value.
 * 
 * <p>This class is serializable so long as it only contains a Serializable value
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 * @param <T> The type of constant value
 */
public class ValueExpression<T> extends AbstractExpression<T> implements Supplier<T>
{
	private static final long serialVersionUID = 1L;
	
	private final T value;
	
	/**
	 * Creates a new literal expression
	 * 
	 * @param value The value, must not be null
	 * @param alias 
	 */
	@SuppressWarnings("unchecked")
	public ValueExpression(T value)
	{
		super((Class<T>) value.getClass());
		this.value = value;
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
		if (obj == null || !(obj instanceof ValueExpression))
			return false;
		@SuppressWarnings("rawtypes")
		ValueExpression other = (ValueExpression) obj;
		return new EqualsBuilder()
			.append(value, other.value)
			.append(getJavaType(), other.getJavaType())
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
			.append(value)
			.append(getJavaType())
			.toHashCode();
	}
	
	/**
	 * Gets the constant value
	 * 
	 * @return the constant value
	 */
	public T get()
	{
		return value;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#evaluate(java.lang.Object)
	 */
	@Override
	public T evaluate(Object object)
	{
		return value;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return value.toString();
	}
	
	/**
	 * Shortcut to create a new ValueExpression.
	 * 
	 * @param value The value
	 * @return The created expression
	 */
	public static <T> ValueExpression<T> of(T value)
	{
		return new ValueExpression<T>(Arguments.checkNull(value));
	}
}
