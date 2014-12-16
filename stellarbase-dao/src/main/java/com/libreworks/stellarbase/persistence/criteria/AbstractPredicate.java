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

/**
 * Abstract superclass for Predicate objects.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public abstract class AbstractPredicate extends AbstractExpression<Boolean> implements Predicate 
{
	private static final long serialVersionUID = 1L;
	
	private final boolean negated;

	public AbstractPredicate(boolean negated)
	{
		super(Boolean.class);
		this.negated = negated;
	}

	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.persistence.criteria.Predicate#isNegated()
	 */
	public boolean isNegated()
	{
		return negated;
	}
}
