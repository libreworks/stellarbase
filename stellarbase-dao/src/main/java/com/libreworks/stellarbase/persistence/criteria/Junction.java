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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.libreworks.stellarbase.util.Arguments;

/**
 * The de facto implementation of PredicateClause.
 *  
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class Junction extends AbstractPredicate implements PredicateClause
{
	private static final long serialVersionUID = 1L;

	private final ImmutableList<Expression<Boolean>> symbols;
	private final boolean conjunction;
	
	public static final String OR = " OR ";
	public static final String AND = " AND ";	

	/**
	 * Creates a new Junction.
	 * 
	 * @param symbols The predicates to add, none of which can be null
	 * @param conjunction true if this is a conjunction (AND), false if a disjunction (OR)
	 * @param negated Whether the entire junction is negated
	 */
	public Junction(Collection<Expression<Boolean>> symbols, boolean conjunction, boolean negated)
	{
		super(negated);
		this.symbols = ImmutableList.copyOf(Arguments.checkContainsNull(symbols));
		this.conjunction = conjunction;
	}
	
	/**
	 * Creates a new Junction using an "AND" operator (conjunction).
	 * 
	 * <p>This is a shortcut for {@code
	 * builder(true).add(a).add(b).build();
	 * }
	 * 
	 * @param a The left Criterion
	 * @param b The right Criterion
	 * @return The new Junction
	 */
	public static Junction and(Expression<Boolean> a, Expression<Boolean> b)
	{
		return builder(true).add(a).add(b).build();
	}
	
	/**
	 * Creates a new Junction using an "OR" operator (disjunction).
	 * 
	 * <p>This is a shortcut for {@code
	 * builder(false).add(a).add(b).build();
	 * }
	 * 
	 * @param a The left Criterion
	 * @param b The right Criterion
	 * @return The new Junction
	 */
	public static Junction or(Expression<Boolean> a, Expression<Boolean> b)
	{
		return builder(false).add(a).add(b).build();
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
		} else if (obj instanceof PredicateClause) {
			PredicateClause other = (PredicateClause) obj;
			return Objects.equal(symbols, other.getSymbols()) &&
				conjunction == other.isConjunction() &&
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
		return Objects.hashCode(symbols, conjunction, isNegated());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Predicate#not()
	 */
	@Override
	public Predicate not()
	{
		return new Junction(symbols, conjunction, true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Expression#evaluate(java.lang.Object)
	 */
	@Override
	public Boolean evaluate(Object object)
	{
		boolean ok = true;
		if ( !conjunction ) {
			ok = false;
			for(Expression<Boolean> c : symbols){
				if ( c.evaluate(object) ) {
					ok = true;
					break;
				}
			}
		} else {
			for(Expression<Boolean> c : symbols){
				if ( !c.evaluate(object) ) {
					ok = false;
					break;
				}
			}
		}
		return isNegated() ? !ok : ok;
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Clause#getSymbols()
	 */
	@Override
	public List<Expression<Boolean>> getSymbols()
	{
		return symbols;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Expression<Boolean>> iterator()
	{
		return symbols.iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.PredicateClause#isConjunction()
	 */
	@Override
	public boolean isConjunction()
	{
		return conjunction;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return (isNegated() ? "NOT" : "") + "( " +
			Joiner.on(conjunction ? AND : OR).join(symbols) + " )";
	}

	/**
	 * Returns an object that assists with Junction construction.
	 * 
	 * @param conjunction whether the Junction is a conjunction (AND)
	 * @return the new builder
	 */
	public static Builder builder(boolean conjunction)
	{
		return new Builder(conjunction);
	}
	
	public static class Builder
	{
		private final LinkedList<Expression<Boolean>> predicates = new LinkedList<Expression<Boolean>>();
		private final boolean conjunction;
		private boolean negated = false;

		private Builder(boolean conjunction)
		{
			this.conjunction = conjunction;
		}
		
		/**
		 * Add a boolean expression to the Junction.
		 * 
		 * @param predicate the boolean expression to add, null is ignored
		 * @return provides a fluent interface
		 */		
		public Builder add(Expression<Boolean> predicate)
		{
			if (predicate instanceof PredicateClause && conjunction == ((PredicateClause) predicate).isConjunction()) {
				this.predicates.addAll(((PredicateClause)predicate).getSymbols());
			} else if (predicate != null) {
				this.predicates.add(predicate);
			}
			return this;
		}

		/**
		 * Adds several boolean expressions to the Junction.
		 * 
		 * @param predicates the boolean expressions to add
		 * @return provides a fluent interface
		 */		
		public Builder addAll(Predicate... predicates)
		{
			if (predicates != null) {
				for (Predicate p : predicates) {
					this.add(p);
				}
			}
			return this;
		}

		/**
		 * Adds several boolean expressions to the Junction.
		 * 
		 * @param predicates the boolean expressions to add
		 * @return provides a fluent interface
		 */
		public Builder addAll(Collection<? extends Expression<Boolean>> predicates)
		{
			if (predicates != null) {
				for (Expression<Boolean> p : predicates) {
					this.add(p);
				}
			}
			return this;
		}
		
		/**
		 * Sets whether this Junction is negated.
		 * 
		 * @param negated whether this Junction is negated 
		 * @return provides a fluent interface
		 */
		public Builder negated(boolean negated)
		{
			this.negated = negated;
			return this;
		}
		
		/**
		 * Creates and returns a new Junction based on the previous input.
		 * 
		 * <p>If no {@code Expression<Boolean>} objects have been added, this
		 * method returns null.
		 * 
		 * @return the new Junction
		 */
		public Junction build()
		{
			if (predicates.size() == 0) {
				return null;
			} else {
				return new Junction(predicates, conjunction, negated);
			}			
		}
	}
}
