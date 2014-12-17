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

import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.springframework.util.ObjectUtils;

import com.google.common.base.Objects;
import com.libreworks.stellarbase.text.Characters;
import com.libreworks.stellarbase.text.Strings;
import com.libreworks.stellarbase.util.Arguments;

/**
 * A predicate which matches an Expression against a pattern.
 *   
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class LikePredicate extends AbstractPredicate
{
	private static final long serialVersionUID = 1L;

	private final Expression<?> inner;
	private final Expression<?> pattern;

	/**
	 * Creates a new LikePredicate
	 * 
	 * @param inner the inner expression
	 * @param pattern the pattern against which {@code inner} is matched
	 * @param negated whether the predicate is negated
	 */
	public LikePredicate(Expression<?> inner, Expression<?> pattern, boolean negated)
	{
		super(negated);
		this.inner = Arguments.checkNull(inner);
		this.pattern = Arguments.checkNull(pattern);
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
		} else if (obj instanceof LikePredicate) {
			LikePredicate other = (LikePredicate) obj;
			return Objects.equal(inner, other.inner) &&
				Objects.equal(pattern, other.pattern) &&
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
		return Objects.hashCode(inner, pattern, isNegated());
	}
	
	/**
	 * @return the inner
	 */
	public Expression<?> getInner()
	{
		return inner;
	}

	/**
	 * @return the pattern
	 */
	public Expression<?> getPattern()
	{
		return pattern;
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Predicate#not()
	 */
	@Override
	public Predicate not()
	{
		return new LikePredicate(inner, pattern, true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#evaluate(java.lang.Object)
	 */
	@Override
	public Boolean evaluate(Object object)
	{
		String a = ObjectUtils.getDisplayString(inner.evaluate(object));
		String p = ObjectUtils.getDisplayString(pattern.evaluate(object));
		if (a.equalsIgnoreCase(p)) {
			return !isNegated();
		} else {
			StringTokenizer st = new StringTokenizer(p, "%_", true);
			StringBuilder sb = new StringBuilder("(?ius)").append(Characters.CARET);
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (Strings.PERCENT.equals(token)) {
					sb.append(".*");
				} else if (Strings.UNDERSCORE.equals(token)) {
					sb.append(Strings.DOT);
				} else {
					sb.append(Pattern.quote(token));
				}
			}
			boolean match = a.matches(sb.append(Characters.DOLLAR).toString());
			return isNegated() ? !match : match;
		}
	}
}
