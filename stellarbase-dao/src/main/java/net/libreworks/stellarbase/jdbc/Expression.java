/**
 * Copyright 2010 LibreWorks contributors
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
package net.libreworks.stellarbase.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.math.NumberUtils;

/**
 * An expression is a boolean evaluation comparing a column against a value.
 * 
 * Ported from the PHP framework Xyster by Jonathan Hawk, the original author.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class Expression
{
	protected Field left;
	protected Operator operator;
	protected Object right;

	protected Expression(Field field, Operator operator, Object value)
	{
		this.left = field;
		this.operator = operator;
		this.right = value;
	}

	/**
	 * @return the left
	 */
	public Field getLeft()
	{
		return left;
	}

	/**
	 * @return the operator
	 */
	public Operator getOperator()
	{
		return operator;
	}

	/**
	 * @return the right
	 */
	public Object getRight()
	{
		return right;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(7, 13).append(left).append(operator)
				.append(right).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Expression))
			return false;
		Expression other = (Expression) obj;
		return new EqualsBuilder().append(left, other.left)
				.append(operator, other.operator).append(right, other.right)
				.isEquals();
	}

	public boolean evaluate(Object obj)
	{
		Object a = FieldEvaluator.get(obj, left);
		Object b = right instanceof Field ? FieldEvaluator.get(obj, (Field)right) : right;
		return operator.evaluate(a, b);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (right == null) {
			sb.append("NULL");
		} else if (isMulti(right)
				&& (Operator.between.equals(operator) || Operator.notBetween
						.equals(operator))) {
			ArrayList<Object> values = new ArrayList<Object>(
					right instanceof Object[] ? Arrays.asList((Object[]) right)
							: (Collection<?>) right);
			sb.append(toStringScalar(values.get(0))).append(" AND ")
					.append(toStringScalar(values.get(1)));
		} else if (isMulti(right)
				&& (Operator.in.equals(operator) || Operator.notIn
						.equals(operator))) {
			ArrayList<Object> values = new ArrayList<Object>(
					right instanceof Object[] ? Arrays.asList((Object[]) right)
							: (Collection<?>) right);
			ArrayList<String> quoted = new ArrayList<String>();
			for (Object o : values) {
				quoted.add(toStringScalar(o));
			}
			sb.append('(').append(StringUtils.join(quoted, ',')).append(')');
		} else {
			sb.append(toStringScalar(right));
		}
		return left.toString() + ' ' + operator.toString() + ' '
				+ sb.toString();
	}

	private static boolean isMulti(Object object)
	{
		return object instanceof Object[] || object instanceof Collection<?>;
	}

	private String toStringScalar(Object value)
	{
		return value instanceof Number || value instanceof Field
				|| NumberUtils.isNumber(value.toString()) ? value.toString()
				: "'" + value.toString().replace("'", "''") + "'";
	}
}
