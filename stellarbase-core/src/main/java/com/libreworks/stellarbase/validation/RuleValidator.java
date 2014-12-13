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
package com.libreworks.stellarbase.validation;

import java.util.Collection;

import org.springframework.util.ClassUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.google.common.collect.ImmutableList;
import com.libreworks.stellarbase.util.Arguments;

/**
 * A validator which validates an object using a series of {@link Rule} objects.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class RuleValidator implements Validator
{
	protected Collection<Rule> rules = ImmutableList.of();
	
	/**
	 * Creates a new RuleValidator.
	 * 
	 * @param rules The rules to use for validation
	 * @throws NullPointerException if any {@link Rule} in the collection is null
	 */
	public RuleValidator(Collection<Rule> rules)
	{
		this.rules = ImmutableList.copyOf(Arguments.checkContainsNull(rules));
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class<?> clazz)
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors)
	{
		for(Rule rule : rules) {
			rule.validate(target, errors);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new StringBuilder(ClassUtils.getShortName(getClass()))
				.append('(').append(rules).append(')').toString();
	}
}
