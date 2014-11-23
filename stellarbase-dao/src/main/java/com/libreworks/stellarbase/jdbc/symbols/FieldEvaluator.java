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

import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * A translator that pulls the values from a Map or Object.
 * 
 * Ported from the PHP framework Xyster by Jonathan Hawk, the original author.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class FieldEvaluator
{
	protected String name;

	/**
	 * Creates a FieldEvaluator for a name
	 * 
	 * @param name The name
	 */
	public FieldEvaluator(String name)
	{
		this.name = name;
	}

	/**
	 * Creates a FieldEvaluator for a field
	 * 
	 * @param field The field
	 */
	public FieldEvaluator(Field field)
	{
		this.name = field.getName();
	}

	/**
	 * Evaluates this name against the object (i.e. reads its property value).
	 *  
	 * @param obj Either a Map or an Object.
	 * @return The value evaluated
	 */
	public Object evaluate(Object obj)
	{
		Object value = null;
		if (obj instanceof Map<?,?>) {
			value = ((Map<?,?>) obj).get(name);
		} else {
			PropertyAccessorFactory.forBeanPropertyAccess(obj)
					.getPropertyValue(name);
		}
		return value;
	}
	
	/**
	 * A shortcut instead of creating a new getter.
	 * 
	 * @param obj The object
	 * @param field The field name
	 * @return The value returned
	 */
	public static Object get(Object obj, Object field)
	{
		FieldEvaluator getter = field instanceof Field ? new FieldEvaluator(
				(Field) field)
				: new FieldEvaluator(ObjectUtils.toString(field));
		return getter.evaluate(obj);
	}
}
