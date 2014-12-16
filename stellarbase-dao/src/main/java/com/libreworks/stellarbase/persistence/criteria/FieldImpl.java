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

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.PropertyAccessorFactory;

import com.libreworks.stellarbase.util.Arguments;

/**
 * A simple implementation of Field.
 *  
 * @author Jonathan Hawk
 * @since 1.0.0
 * @param <T> The evaluated return type
 */
public class FieldImpl<T> extends AbstractField<T>
{
	private static final long serialVersionUID = 1L;
	
	private final String name;
	
	/**
	 * Creates a new Field
	 * 
	 * @param name The field name
	 * @param javaType The type returned by accessing the field on an object
	 */
	public FieldImpl(String name, Class<T> javaType)
	{
		super(javaType);
		this.name = Arguments.checkBlank(name);
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
		if (obj == null || !(obj instanceof FieldImpl))
			return false;
		@SuppressWarnings("rawtypes")
		FieldImpl other = (FieldImpl) obj;
		return new EqualsBuilder()
			.append(name, other.name)
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
			.append(name)
			.append(getJavaType())
			.toHashCode();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#evaluate(java.lang.Object)
	 */
	@Override
	public T evaluate(Object object)
	{
		Object value = null;
		if (object instanceof Map<?,?>) {
			value = ((Map<?,?>) object).get(name);
		} else {
			value = PropertyAccessorFactory.forBeanPropertyAccess(object)
				.getPropertyValue(name);
		}
		return getJavaType().cast(value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Field#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getName();
	}
}
