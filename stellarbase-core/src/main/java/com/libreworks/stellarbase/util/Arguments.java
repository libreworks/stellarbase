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
package com.libreworks.stellarbase.util;

import java.util.Arrays;
import java.util.Collection;

import com.libreworks.stellarbase.text.Strings;

/**
 * A utility to assist in parameter validation.
 * 
 * @author Jonathan Hawk
 */
public class Arguments
{
	private Arguments()
	{
	}
	
	/**
	 * Ensures that a given Object isn't null.
	 * 
	 * @param object The object to verify
	 * @param message An optional message for the Exception
	 * @return the same provided object
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T> T checkNull(T object)
	{
		return checkNull(object, "This argument is required; it cannot be null");
	}
	
	/**
	 * Ensures that a given Object isn't null.
	 * 
	 * @param object The object to verify
	 * @param message An optional message for the Exception
	 * @return the same provided object
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T> T checkNull(T object, String message)
	{
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
		return object;
	}

	/**
	 * Ensures that a given Object is an instance of a certain class.
	 * 
	 * @param c The intended instance type
	 * @param object The object to verify
	 * @return the same provided object
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T> T checkInstanceOf(Class<?> c, T object)
	{
		return checkInstanceOf(c, object);
	}
	
	/**
	 * Ensures that a given Object is an instance of a certain class.
	 * 
	 * @param c The intended instance type
	 * @param object The object to verify
	 * @param message An optional message for the Exception
	 * @return the same provided object
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T> T checkInstanceOf(Class<?> c, T object, String message)
	{
		if (!checkNull(c, "The first parameter to checkInstanceOf must not be null").isInstance(object)) {
			throw new IllegalArgumentException(message);
		}
		return object;
	}
	
	/**
	 * Ensures that a given Collection isn't null nor empty.
	 * 
	 * @param object The collection to verify
	 * @return the same provided collection
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T extends Collection<?>> T checkEmpty(T object)
	{
		return checkEmpty(object, "This collection argument is required; it cannot be null nor empty");
	}
	
	/**
	 * Ensures that a given Collection isn't null nor empty.
	 * 
	 * @param object The collection to verify
	 * @param message An optional message for the Exception
	 * @return the same provided collection
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T extends Collection<?>> T checkEmpty(T object, String message)
	{
		if (object == null || object.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		return object;
	}
	
	/**
	 * Ensures that a given array isn't null nor empty.
	 * 
	 * @param object The array to verify
	 * @return the same provided array
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T> T[] checkEmpty(T[] object)
	{
		return checkEmpty(object, "This array argument is required; it cannot be null nor empty");
	}
	
	/**
	 * Ensures that a given array isn't null nor empty.
	 * 
	 * @param object The array to verify
	 * @param message An optional message for the Exception
	 * @return the same provided array
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T> T[] checkEmpty(T[] object, String message)
	{
		if (object == null || object.length == 0) {
			throw new IllegalArgumentException(message);
		}
		return object;
	}
	
	/**
	 * Ensures that a given Collection isn't null, empty, nor containing null.
	 * 
	 * @param object The collection to verify
	 * @return the same provided collection
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T extends Collection<?>> T checkContainsNull(T object)
	{
		return checkContainsNull(object, "This collection argument is required (it cannot be null nor empty), and it cannot contain null values");
	}
	
	/**
	 * Ensures that a given Collection isn't null nor containing null.
	 * 
	 * @param object The collection to verify
	 * @param message An optional message for the Exception
	 * @return the same provided collection
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T extends Collection<?>> T checkContainsNull(T object, String message)
	{
		if (checkNull(object, message).contains(null)) {
			throw new IllegalArgumentException(message);
		}
		return object;
	}
	
	/**
	 * Ensures that a given array isn't null nor containing null.
	 * 
	 * @param object The array to verify
	 * @return the same provided array
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T> T[] checkContainsNull(T[] object)
	{
		return checkContainsNull(object, "This array argument is required (it cannot be null nor empty), and it cannot contain null values");
	}
	
	/**
	 * Ensures that a given array isn't null nor containing null.
	 * 
	 * @param object The array to verify
	 * @param message An optional message for the Exception
	 * @return the same provided array
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T> T[] checkContainsNull(T[] object, String message)
	{
		if (Arrays.asList(checkNull(object, message)).contains(null)) {
			throw new IllegalArgumentException(message);
		}
		return object;
	}
	
	/**
	 * Ensures that a given CharSequence isn't null, empty, nor containing only whitespace characters.
	 * 
	 * @param object The string to verify
	 * @return the same provided CharSequence
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T extends CharSequence> T checkBlank(T object)
	{
		return checkBlank(object, "This String argument is required; it cannot be null, empty, nor containing only whitespace");
	}
	
	/**
	 * Ensures that a given CharSequence isn't null, empty, nor containing only whitespace characters.
	 * 
	 * @param object The string to verify
	 * @param message An optional message for the Exception
	 * @return the same provided CharSequence
	 * @throws IllegalArgumentException if {@code object} is invalid
	 */
	public static <T extends CharSequence> T checkBlank(T object, String message)
	{
		if (Strings.isBlank(object)) {
			throw new IllegalArgumentException(message);
		}
		return object;
	}
}
