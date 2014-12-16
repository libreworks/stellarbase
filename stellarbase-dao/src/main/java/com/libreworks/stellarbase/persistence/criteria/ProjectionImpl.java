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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.libreworks.stellarbase.util.Arguments;

/**
 * The default implementation of Projection
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 * @param <T> The type of contained Expression 
 */
public class ProjectionImpl<T> implements Projection<T>
{
	private static final long serialVersionUID = 1L;
	
	private final Expression<T> expression;
	private final String alias;
	private final boolean grouped;
	
	/**
	 * Creates a new Projection
	 * 
	 * @param expression The inner expression
	 * @param alias The projection alias
	 * @param grouped Whether the projection is grouped
	 */
	public ProjectionImpl(Expression<T> expression, String alias, boolean grouped)
	{
		this.expression = Arguments.checkNull(expression);
		this.alias = StringUtils.isBlank(alias) ? null : alias.trim();
		this.grouped = grouped;
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
		if (obj == null || !(obj instanceof Projection))
			return false;
		@SuppressWarnings("rawtypes")
		Projection other = (Projection) obj;
		return new EqualsBuilder()
			.append(expression, other.getExpression())
			.append(alias, other.getAlias())
			.append(grouped, other.isGrouped())
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
			.append(expression)
			.append(alias)
			.append(grouped)
			.toHashCode();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Projection#getExpression()
	 */
	@Override
	public Expression<T> getExpression()
	{
		return expression;
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Projection#getAlias()
	 */
	@Override
	public String getAlias()
	{
		return alias;
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Projection#isGrouped()
	 */
	@Override
	public boolean isGrouped()
	{
		return grouped;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return expression.toString() + (alias == null ? "" : " AS " + alias);
	}
}
