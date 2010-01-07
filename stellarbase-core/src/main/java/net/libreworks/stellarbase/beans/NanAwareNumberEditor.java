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
package net.libreworks.stellarbase.beans;

import org.springframework.beans.propertyeditors.CustomNumberEditor;

/**
 * Makes sure that "NaN" gets parsed as a null Number
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class NanAwareNumberEditor extends CustomNumberEditor
{
	private static final String NAN = "NaN";
	
	/**
	 * @param numberClass Number subclass to generate
	 * @param allowEmpty if empty strings should be allowed
	 */
	public NanAwareNumberEditor(Class<?> numberClass, boolean allowEmpty)
	{
		super(numberClass, allowEmpty);
	}
	
	@Override
	public void setAsText(String text)
	{
		super.setAsText(NAN.equals(text) ? null : text);
	}
}
