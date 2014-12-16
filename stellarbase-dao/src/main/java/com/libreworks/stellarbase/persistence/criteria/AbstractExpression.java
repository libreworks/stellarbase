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

import com.google.common.collect.ImmutableList;

/**
 * Abstract superclass for Expression objects
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 * @param <T> The evaluated return type
 */
public abstract class AbstractExpression<T> implements Expression<T>
{
	private static final long serialVersionUID = 1L;

	private final Class<T> javaType;
	
	@SuppressWarnings("unchecked")
	public AbstractExpression(Class<T> javaType)
	{
		this.javaType = (Class<T>) (javaType == null ? Object.class : javaType);
	}

	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#as(java.lang.String)
	 */
	@Override
	public Projection<T> as(String alias)
	{
		return new ProjectionImpl<T>(this, alias, false);
	}

	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#grouped(java.lang.String)
	 */
	@Override
	public Projection<T> grouped(String alias)
	{
		return new ProjectionImpl<T>(this, alias, true);
	}

	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#getJavaType()
	 */
	@Override
	public Class<? extends T> getJavaType()
	{
		return javaType;
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#asc()
	 */
	@Override
	public Order asc()
	{
	    return new OrderImpl(this, true);
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#desc()
	 */
	@Override
	public Order desc()
	{
	    return new OrderImpl(this, false);
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#eq(com.libreworks.stellarbase.persistence.criteria.Expression)
	 */
	@Override
	public Predicate eq(Expression<?> value)
	{
	    return new ComparisonPredicate(ComparisonPredicate.Operator.EQ, this, value, null, false);
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#ne(com.libreworks.stellarbase.persistence.criteria.Expression)
	 */
	@Override
	public Predicate ne(Expression<?> value)
	{
	    return new ComparisonPredicate(ComparisonPredicate.Operator.NE, this, value, null, false);
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#lt(com.libreworks.stellarbase.persistence.criteria.Expression)
	 */
	@Override
	public Predicate lt(Expression<?> value)
	{
	    return new ComparisonPredicate(ComparisonPredicate.Operator.LT, this, value, null, false);
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#le(com.libreworks.stellarbase.persistence.criteria.Expression)
	 */
	@Override
	public Predicate le(Expression<?> value)
	{
	    return new ComparisonPredicate(ComparisonPredicate.Operator.LE, this, value, null, false);		
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#gt(com.libreworks.stellarbase.persistence.criteria.Expression)
	 */
	@Override
	public Predicate gt(Expression<?> value)
	{
	    return new ComparisonPredicate(ComparisonPredicate.Operator.GT, this, value, null, false);
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#ge(com.libreworks.stellarbase.persistence.criteria.Expression)
	 */
	@Override
	public Predicate ge(Expression<?> value)
	{
	    return new ComparisonPredicate(ComparisonPredicate.Operator.GE, this, value, null, false);
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#like(com.libreworks.stellarbase.persistence.criteria.Expression)
	 */
	@Override
	public Predicate like(Expression<?> value)
	{
		return new LikePredicate(this, value, false);
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#notLike(com.libreworks.stellarbase.persistence.criteria.Expression)
	 */
	@Override
	public Predicate notLike(Expression<?> value)
	{
		return new LikePredicate(this, value, true);
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#between(com.libreworks.stellarbase.persistence.criteria.Expression, com.libreworks.stellarbase.persistence.criteria.Expression)
	 */
	@Override
	public Predicate between(Expression<? extends Comparable<?>> a, Expression<? extends Comparable<?>> b)
	{
	    return new ComparisonPredicate(ComparisonPredicate.Operator.BETWEEN, this, a, b, false);
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#notBetween(com.libreworks.stellarbase.persistence.criteria.Expression, com.libreworks.stellarbase.persistence.criteria.Expression)
	 */
	@Override
	public Predicate notBetween(Expression<? extends Comparable<?>> a, Expression<? extends Comparable<?>> b)
	{
	    return new ComparisonPredicate(ComparisonPredicate.Operator.BETWEEN, this, a, b, true);
	}
	
    /* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#in(com.libreworks.stellarbase.persistence.criteria.Expression)
	 */
	@Override
	public Predicate in(Expression<?>... values)
	{
		return new InPredicate(this, ImmutableList.copyOf(values), false);
	}
	
    /* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#in(java.util.Collection)
	 */
	@Override
	public Predicate in(Collection<? extends Expression<?>> values)
	{
	    return new InPredicate(this, values, false);
	}

    /* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#notIn(com.libreworks.stellarbase.persistence.criteria.Expression)
	 */
	@Override
	public Predicate notIn(Expression<?>... values)
	{
		return new InPredicate(this, ImmutableList.copyOf(values), true);
	}
	
    /* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#notIn(java.util.Collection)
	 */
    @Override
	public Predicate notIn(Collection<? extends Expression<?>> values)
    {
		return new InPredicate(this, values, true);
    }
    
    /* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#isNull()
	 */
    @Override
	public Predicate isNull()
    {
        return new NullPredicate(this, false);
    }
    
    /* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#isNotNull()
	 */
    @Override
	public Predicate isNotNull()
    {
        return new NullPredicate(this, true);
    }
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#count()
	 */
	@Override
	public Expression<Long> count()
	{
		return new CountExpression(this, false);
	}

	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#countDistinct()
	 */
	@Override
	public Expression<Long> countDistinct()
	{
		return new CountExpression(this, true);
	}	
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#sum()
	 */
	@Override
	public Expression<Number> sum()
	{
		return AggregateExpression.sum(this);
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#avg()
	 */
	@Override
	public Expression<Number> avg()
	{
		return AggregateExpression.avg(this);
	}
	
	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#max()
	 */
	@Override
	public Expression<T> max()
	{
		return AggregateExpression.max(this);
	}

	/* (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#min()
	 */
	@Override
	public Expression<T> min()
	{
		return AggregateExpression.min(this);
	}	
}
