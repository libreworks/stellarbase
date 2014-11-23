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
import java.util.Arrays;
import java.util.Collection;

import com.libreworks.stellarbase.util.ValueUtils;

import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Range;

/**
 * An enum representing SQL operators: most of them, that is.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public enum Operator
{
	eq("="),
	neq("<>"),
	ge(">="),
	le("<="),
	lt("<"),
	gt(">"),
	in("IN"),
	notIn("NOT IN"),
	is("IS"),
	isNot("IS NOT"),
	like("LIKE"),
	notLike("NOT LIKE"),
	between("BETWEEN"),
	notBetween("NOT BETWEEN");
	
	private String sql;
	
	Operator(String sql)
	{
		this.sql = sql;
	}

	/**
	 * Gets the SQL equivalent of this operator.
	 * 
	 * @return The SQL equivalent
	 */
	public String getSql()
	{
		return sql;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public boolean evaluate(Object a, Object b)
	{
		boolean result = false;
		if ( eq.equals(this) ) {
			result = ObjectUtils.equals(a, b);
		} else if ( neq.equals(this) ) {
			result = !ObjectUtils.equals(a, b);
		} else if (ge.equals(this) || gt.equals(this) || lt.equals(this)
				|| le.equals(this)) {
			if ((ge.equals(this) || le.equals(this))
					&& ObjectUtils.equals(a, b)) {
				result = true;
			} else {
				Comparable ca = a instanceof Comparable ? (Comparable) a
						: ObjectUtils.toString(a);
				Comparable cb = b instanceof Comparable ? (Comparable) b
						: ObjectUtils.toString(b);
				int compare = ca.compareTo(cb);
				result = ((ge.equals(this) || gt.equals(this)) && compare > 0)
						|| ((le.equals(this) || lt.equals(this)) && compare < 0);
			}
		} else if ( in.equals(this) || notIn.equals(this) ) {
			if ( b instanceof Collection ) {
				result = ((Collection) b).contains(a);
			} else if ( b instanceof Object[] ) {
				result = ArrayUtils.contains((Object[]) b, a);
			}
			if ( b != null && notIn.equals(this) ) {
				result = !result;
			}
		} else if ( like.equals(this) || notLike.equals(this) ) {
			String bs = ObjectUtils.toString(b);
			boolean lookBeg = bs.endsWith("%"); 
			boolean lookEnd = bs.startsWith("%");
			boolean lookIn = lookBeg && lookEnd;
			if ( lookIn ) {
				result = ObjectUtils.toString(a).contains(bs.substring(1, bs.length()-2));
			} else if ( lookBeg ) {
				result = ObjectUtils.toString(a).startsWith(bs.substring(0, bs.length()-1));
			} else if ( lookEnd ) {
				result = ObjectUtils.toString(a).endsWith(bs.substring(1));
			}
			if ( notLike.equals(this) ) {
				result = !result;
			}
		} else if ( between.equals(this) || notBetween.equals(this) ) {
			if ( b != null ) {
				ArrayList<Object> values = new ArrayList<Object>(
						b instanceof Object[] ? Arrays.asList((Object[]) b)
								: (Collection<?>) b);				
				if ( a instanceof Number ) {
					Number na = (Number)a;
					Range<Number> nr = Range.between(ValueUtils.value(
							na.getClass(), values.get(0)), ValueUtils.value(
									na.getClass(), values.get(1)), ComparableComparator.getInstance());
					result = nr.contains(na);
				} else {
					Comparable c1 = values.get(0) instanceof Comparable ?
							(Comparable) values.get(0)
							: ObjectUtils.toString(values.get(0));
					Comparable c2 = values.get(1) instanceof Comparable ?
							(Comparable) values.get(1)
							: ObjectUtils.toString(values.get(1));
					result = c1.compareTo(a) >= 0 && c2.compareTo(a) <= 0;
				}
				if ( notBetween.equals(this) ) {
					result = !result;
				}
			}
		} else if ( is.equals(this) || isNot.equals(this) ) {
		    result = a == b;
		    if ( isNot.equals(this) ) {
		        result = !result;
		    }
		}
		return result;
	}
}
