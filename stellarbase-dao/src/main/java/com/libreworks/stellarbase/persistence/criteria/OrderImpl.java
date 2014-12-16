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

/**
 * Defines ordering against an expression. 
 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class OrderImpl implements Order
{
	private static final long serialVersionUID = 1L;
	
	private final Expression<?> expression;
	private final boolean ascending;

    public static final String ASC = " ASC";
    public static final String DESC = " DESC";
    
    /**
     * Creates a new Sort Specification
     * 
     * @param expression The inner expression
     * @param ascending Whether the sort specification is ascending
     */
	public OrderImpl(Expression<?> expression, boolean ascending)
	{
		this.expression = Arguments.checkNull(expression);
		this.ascending = ascending;
	}
    
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null || !(obj instanceof Order))
            return false;
        Order other = (Order) obj;
        return new EqualsBuilder()
            .append(ascending, other.isAscending())
            .append(expression, other.getExpression())
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
            .append(ascending)
            .append(expression)
            .toHashCode();
    }
    
    /*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.persistence.criteria.Order#getExpression()
     */
    @Override
	public Expression<?> getExpression()
	{
		return expression;
	}

    /*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.persistence.criteria.Order#isAscending()
     */
    @Override
    public boolean isAscending()
    {
        return ascending;
    }
    
    /*
     * (non-Javadoc)
     * @see com.libreworks.stellarbase.persistence.criteria.Order#reverse()
     */
	@Override
	public Order reverse()
	{
		return new OrderImpl(expression, !ascending);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
    public String toString()
    {
        return expression.toString().concat(ascending ? ASC : DESC);
    }
}
