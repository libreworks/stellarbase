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
package net.libreworks.stellarbase.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * A delegating validator that overrides the supports method.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class SupportsDelegateValidator implements Validator
{
	private Validator delegate;
	private Class<?>[] supports;
	
	/**
	 * Creates a new SimpleDelegateValidator
	 * 
	 * @param delegate The validator to use as a delegate
	 * @param supports The classes that this validator supports (at least 1)
	 */
	public SupportsDelegateValidator(Validator delegate, Class<?>... supports)
	{
		Assert.notNull(delegate);
		this.delegate = delegate;
		Assert.notEmpty(supports);
		this.supports = supports;
	}
	
	public boolean supports(Class<?> clazz)
	{
		for(Class<?> cls : supports){
			if ( cls.isAssignableFrom(clazz) ) {
				return true;
			}
		}
		return false;
	}

	public void validate(Object target, Errors errors)
	{
		delegate.validate(target, errors);
	}
	
	public String toString()
	{
		return delegate.toString() + " supporting " + StringUtils.join(supports, ", ");
	}
}
