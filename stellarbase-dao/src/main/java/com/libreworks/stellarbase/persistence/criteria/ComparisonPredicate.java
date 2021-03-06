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

import java.text.MessageFormat;

import com.google.common.base.Objects;
import com.google.common.collect.Ordering;
import com.libreworks.stellarbase.util.Arguments;

/**
 * An expression that compares two or three values
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class ComparisonPredicate extends AbstractPredicate
{
	private static final long serialVersionUID = 1L;
	
	private final Operator operator;
	private final Expression<?> a;
	private final Expression<?> b;
	private final Expression<?> c;
	
	private static final Ordering<Comparable<?>> COMPARATOR = Ordering.natural().nullsFirst();
	
	/**
	 * Creates a new ComparisonPredicate.
	 * 
	 * @param operator The comparison operator, must not be null
	 * @param a The first expression, must not be null
	 * @param b The second expression, must not be null
	 * @param c The third expression, only used for {@link Operator#BETWEEN}
	 * @param negated whether this predicate is negated
	 */
	public ComparisonPredicate(Operator operator, Expression<?> a, Expression<?> b, Expression<?> c, boolean negated)
	{
		super(negated);
		this.operator = Arguments.checkNull(operator);
		this.a = Arguments.checkNull(a);
		this.b = Arguments.checkNull(b);
		this.c = Operator.BETWEEN == operator ? Arguments.checkNull(c) : null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		} else if (obj instanceof ComparisonPredicate) {
			ComparisonPredicate other = (ComparisonPredicate) obj;
			return Objects.equal(operator, other.operator) &&
				Objects.equal(a, other.a) &&
				Objects.equal(b, other.b) &&
				Objects.equal(c, other.c) &&
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
		return Objects.hashCode(operator, a, b, c, isNegated());
	}
	
	/**
	 * @return the operator
	 */
	public Operator getOperator()
	{
		return operator;
	}

	/**
	 * @return the a
	 */
	public Expression<?> getA()
	{
		return a;
	}

	/**
	 * @return the b
	 */
	public Expression<?> getB()
	{
		return b;
	}

	/**
	 * @return the c
	 */
	public Expression<?> getC()
	{
		return c;
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Predicate#not()
	 */
	@Override
	public Predicate not()
	{
		return new ComparisonPredicate(operator, a, b, c, true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws ClassCastException if the result of any contained expression isn't {@link Comparable} 
	 */
	@Override
	public Boolean evaluate(Object object)
	{
		boolean result = evaluate(operator, a.evaluate(object),
			b.evaluate(object), c == null ? null : c.evaluate(object));
		return isNegated() ? !result : result;
	}
	
	protected static boolean evaluate(Operator operator, Object ea, Object eb, Object ec)
	{
		boolean result = false;
		if (Operator.EQ == operator) {
			result = Objects.equal(ea, eb);
		} else if (Operator.NE == operator) {
			result = !Objects.equal(ea, eb);
		} else if (Operator.GE == operator || Operator.GT == operator ||
			Operator.LT == operator || Operator.LE == operator) {
			if ((Operator.GE == operator || Operator.LE == operator) &&
				Objects.equal(ea, eb)) {
				result = true;
			} else {
				int compare = compare(ea, eb); 
				result = ((Operator.GE == operator || Operator.GT == operator) && compare > 0)
						|| ((Operator.LE == operator || Operator.LT == operator) && compare < 0);
			}
		} else if (Operator.BETWEEN == operator) {
			result = compare(eb, ea) <= 0 && compare(ea, ec) <= 0;
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	private static int compare(Object a, Object b)
	{
		return COMPARATOR.compare((Comparable)a, (Comparable)b);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return isNegated() ?
			"NOT(" + MessageFormat.format(operator.pattern, a, b, c) + ")" :
				MessageFormat.format(operator.pattern, a, b, c);
	}
	
	/**
	 * Represents the actual operation being performed.
	 */
	public enum Operator
	{
		EQ("{0} = {1}"),
		NE("{0} <> {1}"),
		LT("{0} < {1}"),
		LE("{0} <= {1}"),
		GT("{0} > {1}"),
		GE("{0} >= {1}"),
		BETWEEN("{0} BETWEEN {1} AND {2}");
		
		private final String pattern;
		
		Operator(String pattern)
		{
			this.pattern = pattern;
		}
	}	
}
