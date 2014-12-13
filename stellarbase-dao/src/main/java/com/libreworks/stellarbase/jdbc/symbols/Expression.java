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
package com.libreworks.stellarbase.jdbc.symbols;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.ImmutableList;
import com.libreworks.stellarbase.text.Characters;
import com.libreworks.stellarbase.text.Strings;
import com.libreworks.stellarbase.util.Arguments;

/**
 * An expression is a boolean evaluation comparing a column against a value.
 * 
 * Ported from the PHP framework Xyster by Jonathan Hawk, the original author.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class Expression extends Criterion
{
    private static final long serialVersionUID = 1L;
    
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

	@Override
    public Collection<Field> getAllFields()
    {
		ImmutableList.Builder<Field> b = ImmutableList.<Field>builder().add(left);
        if ( right instanceof Field ) {
            b.add((Field)right);
        }
        return b.build();
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
			sb.append(Strings.NULL.toUpperCase());
		} else if (right instanceof Object[]
				&& (Operator.between.equals(operator) || Operator.notBetween
						.equals(operator))) {
			Object[] values = (Object[]) right;
			sb.append(toStringScalar(values[0])).append(Junction.AND)
					.append(toStringScalar(values[1]));
		} else if (right instanceof Object[]
				&& (Operator.in.equals(operator) || Operator.notIn
						.equals(operator))) {
			ArrayList<String> quoted = new ArrayList<String>();
			for (Object o : (Object[]) right) {
				quoted.add(toStringScalar(o));
			}
			sb.append('(').append(StringUtils.join(quoted, Characters.COMMA)).append(')');
		} else {
			sb.append(toStringScalar(right));
		}
		return new StringBuilder(left.toString()).append(Characters.SPACE)
				.append(operator.getSql()).append(Characters.SPACE).append(sb)
				.toString();
	}

	private String toStringScalar(Object value)
	{
		return value instanceof Number || value instanceof Field
				|| NumberUtils.isNumber(value.toString()) ? value.toString()
				: Strings.APOSTROPHE + value.toString().replace(Strings.APOSTROPHE, "''") + Strings.APOSTROPHE;
	}
	
    /**
     * Equals Expression ( field = 'value' )
     * 
     * @param field The field
     * @param value The value
     * @return The EQ Expression
     */
	public static Expression eq(Field field, Object value)
	{
	    return new Expression(field, Operator.eq, value);
	}
	
    /**
     * Equals Expression ( field = 'value' )
     * 
     * @param name The field name
     * @param value The value
     * @return The EQ Expression
     */
	public static Expression eq(String name, Object value)
	{
	    return new Expression(Field.named(name), Operator.eq, value);
	}
	
    /**
     * Not equals Expression ( field <> 'value' )
     * 
     * @param field The field
     * @param value The value
     * @return The NEQ Expression
     */
	public static Expression neq(Field field, Object value)
	{
	    return new Expression(field, Operator.neq, value);
	}
	
    /**
     * Not equals Expression ( field <> 'value' )
     * 
     * @param name The field name
     * @param value The value
     * @return The NEQ Expression
     */
	public static Expression neq(String name, Object value)
	{
	    return new Expression(Field.named(name), Operator.neq, value);
	}
	
    /**
     * Less than or equal to Expression ( field <= 3 )
     * 
     * @param field The field
     * @param value The value
     * @return The LE Expression
     */
	public static Expression le(Field field, Object value)
	{
	    return new Expression(field, Operator.le, value);
	}
	
    /**
     * Less than or equal to Expression ( field <= 3 )
     * 
     * @param name The field name
     * @param value The value
     * @return The LE Expression
     */
	public static Expression le(String name, Object value)
	{
	    return new Expression(Field.named(name), Operator.le, value);
	}
	
    /**
     * Less than Expression ( field < 3 )
     * 
     * @param field The field
     * @param value The value
     * @return The LT Expression
     */
	public static Expression lt(Field field, Object value)
	{
	    return new Expression(field, Operator.lt, value);
	}
	
    /**
     * Less than Expression ( field < 3 )
     * 
     * @param name The field name
     * @param value The value
     * @return The LT Expression
     */
	public static Expression lt(String name, Object value)
	{
	    return new Expression(Field.named(name), Operator.lt, value);
	}
	
    /**
     * Greater than Expression ( field > 2 )
     * 
     * @param field The field
     * @param value The value
     * @return The GT Expression
     */
	public static Expression gt(Field field, Object value)
	{
	    return new Expression(field, Operator.gt, value);
	}
	
    /**
     * Greater than Expression ( field > 2 )
     * 
     * @param name The field name
     * @param value The value
     * @return The GT Expression
     */
	public static Expression gt(String name, Object value)
	{
	    return new Expression(Field.named(name), Operator.gt, value);
	}
	
    /**
     * Greater than or equal to Expression ( field >= 2 )
     * 
     * @param field The field
     * @param value The value
     * @return The GE Expression
     */
	public static Expression ge(Field field, Object value)
	{
	    return new Expression(field, Operator.ge, value);
	}
	
    /**
     * Greater than or equal to Expression ( field >= 2 )
     * 
     * @param name The field name
     * @param value The value
     * @return The GE Expression
     */
	public static Expression ge(String name, Object value)
	{
	    return new Expression(Field.named(name), Operator.ge, value);
	}

    /**
     * LIKE Expression ( field LIKE '%value' )
     * 
     * @param field The field
     * @param value The value
     * @return The LIKE Expression
     */
	public static Expression like(Field field, Object value)
	{
	    return new Expression(field, Operator.like, value);
	}

    /**
     * LIKE Expression ( field LIKE '%value' )
     * 
     * @param name The field name
     * @param value The value
     * @return The LIKE Expression
     */
	public static Expression like(String name, Object value)
	{
	    return new Expression(Field.named(name), Operator.like, value);
	}
	
    /**
     * NOT LIKE Expression ( field NOT LIKE '%value' )
     * 
     * @param field The field
     * @param value The value
     * @return The NOT LIKE Expression
     */
	public static Expression notLike(Field field, Object value)
	{
	    return new Expression(field, Operator.notLike, value);
	}
	
    /**
     * NOT LIKE Expression ( field NOT LIKE '%value' )
     * 
     * @param name The field name
     * @param value The value
     * @return The NOT LIKE Expression
     */
	public static Expression notLike(String name, Object value)
	{
	    return new Expression(Field.named(name), Operator.notLike, value);
	}
	
    /**
     * BETWEEN Expression ( field BETWEEN 'value' AND 'value' )
     * 
     * @param field The field
     * @param a The first value
     * @param b The second value
     * @return The BETWEEN Expression
     */
	public static Expression between(Field field, Comparable<?> a, Comparable<?> b)
	{
	    return new Expression(field, Operator.between, new Object[]{a, b});
	}
	
    /**
     * BETWEEN Expression ( field BETWEEN 'value' AND 'value' )
     * 
     * @param name The field name
     * @param a The first value
     * @param b The second value
     * @return The BETWEEN Expression
     */
	public static Expression between(String name, Comparable<?> a, Comparable<?> b)
	{
	    return new Expression(Field.named(name), Operator.between, new Object[]{a, b});
	}
	
    /**
     * NOT BETWEEN Expression ( field NOT BETWEEN 'value' AND 'value' )
     * 
     * @param field The field
     * @param a The first value
     * @param b The second value
     * @return The NOT BETWEEN Expression
     */
    public static Expression notBetween(Field field, Comparable<?> a, Comparable<?> b)
    {
        return new Expression(field, Operator.notBetween, new Object[]{a, b});
    }
    
    /**
     * NOT BETWEEN Expression ( field NOT BETWEEN 'value' AND 'value' )
     * 
     * @param name The field name
     * @param a The first value
     * @param b The second value
     * @return The NOT BETWEEN Expression
     */
    public static Expression notBetween(String name, Comparable<?> a, Comparable<?> b)
    {
        return new Expression(Field.named(name), Operator.notBetween, new Object[]{a, b});
    }
	
    /**
     * Not in expression ( field NOT IN ( 1,1,2,3,5,8,13,21,'fibonacci' ) )
     *  
     * @param field The field
     * @param values The values
     * @return The NOT IN Expression
     */
    public static Expression notIn(Field field, Collection<?> values)
    {
        return notIn(field, Arguments.checkNull(values).toArray());
    }
    
    /**
     * Not in expression ( field NOT IN ( 1,1,2,3,5,8,13,21,'fibonacci' ) )
     *  
     * @param field The field
     * @param values The values
     * @return The NOT IN Expression
     */
    public static Expression notIn(Field field, Object... values)
    {
        return new Expression(field, Operator.notIn, values);
    }

    /**
     * Not in expression ( field NOT IN ( 1,1,2,3,5,8,13,21,'fibonacci' ) )
     *  
     * @param name The field name
     * @param values The values
     * @return The NOT IN Expression
     */
    public static Expression notIn(String name, Collection<?> values)
    {
        return notIn(name, Arguments.checkNull(values).toArray());
    }
    
    /**
     * Not in expression ( field NOT IN ( 1,1,2,3,5,8,13,21,'fibonacci' ) )
     *  
     * @param name The field name
     * @param values The values
     * @return The NOT IN Expression
     */
    public static Expression notIn(String name, Object... values)
    {
        return new Expression(Field.named(name), Operator.notIn, values);
    }
    
    /**
     * In expression ( field IN ( 1,1,2,3,5,8,13,21,'fibonacci' ) )
     *  
     * @param field The field
     * @param values The values
     * @return The IN Expression
     */
    public static Expression in(Field field, Collection<?> values)
    {
        return in(field, Arguments.checkNull(values).toArray());
    }
    
    /**
     * In expression ( field IN ( 1,1,2,3,5,8,13,21,'fibonacci' ) )
     *  
     * @param field The field
     * @param values The values
     * @return The IN Expression
     */
    public static Expression in(Field field, Object... values)
    {
        return new Expression(field, Operator.in, values);
    }
    
    /**
     * In expression ( field IN ( 1,1,2,3,5,8,13,21,'fibonacci' ) )
     *  
     * @param name The field name
     * @param values The values
     * @return The IN Expression
     */
    public static Expression in(String name, Collection<?> values)
    {
        return in(name, Arguments.checkNull(values).toArray());
    }
    
    /**
     * In expression ( field IN ( 1,1,2,3,5,8,13,21,'fibonacci' ) )
     *  
     * @param name The field name
     * @param values The values
     * @return The IN Expression
     */
    public static Expression in(String name, Object... values)
    {
        return new Expression(Field.named(name), Operator.in, values);
    }
    
    /**
     * IS NULL expression
     * 
     * @param name The field name
     * @return The IS Expression
     */
    public static Expression isNull(String name)
    {
        return new Expression(Field.named(name), Operator.is, null);
    }
    
    /**
     * IS NULL expression
     * 
     * @param field The field
     * @return The IS Expression
     */
    public static Expression isNull(Field field)
    {
        return new Expression(field, Operator.is, null);
    }

    /**
     * IS NOT NULL expression
     * 
     * @param name The field name
     * @return The IS NOT Expression
     */
    public static Expression isNotNull(String name)
    {
        return new Expression(Field.named(name), Operator.isNot, null);
    }
    
    /**
     * IS NOT NULL expression
     * 
     * @param field The field
     * @return The IS NOT Expression
     */
    public static Expression isNotNull(Field field)
    {
        return new Expression(field, Operator.isNot, null);
    }
}
