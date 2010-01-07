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

/**
 * Base validation rule.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public abstract class AbstractRule implements Rule
{
	@Override
	public String toString()
	{
		return getLabel() + "[" + getConstraints() + "]";
	}
	
	/**
	 * Gets the constraints of the rule.
	 * 
	 * This information might include which fields are checked and what criteria
	 * they must meet. This information is used in the toString method and is 
	 * up to implementors.
	 * 
	 * @return The constraints of the rule
	 */
	abstract public String getConstraints();
	
	/**
	 * Gets the label of the rule.
	 * 
	 * A label is an identifying string that briefly describes the purpose of
	 * the rule. Usually it is identical or similar to the class name (for
	 * instance "Regex" or "Required").
	 * 
	 * @return The rule label
	 */
	abstract public String getLabel();
}
