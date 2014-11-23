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

import org.springframework.validation.Errors;

/**
 * A validation rule.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public interface Rule
{
	/**
	 * Error code for empty fields
	 */
	public static final String FIELD_EMPTY = "field.empty";
	/**
	 * Error code for invalid fields
	 */
	public static final String FIELD_INVALID = "field.invalid";
	
	/**
	 * Validate the supplied object.
	 * 
	 * The supplied errors instance can be used to report any resulting
	 * validation errors.
	 * 
	 * @param target the object that is to be validated (can be {@code null}) 
	 * @param errors contextual state about the validation process (never {@code null}) 
	 */
	void validate(Object target, Errors errors);
}
