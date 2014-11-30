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
package com.libreworks.stellarbase.text;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableSet;

/**
 * Static class with some pretty common String constants and methods.
 * 
 * <p>Many constants are {@link String} versions of the constants in the
 * {@link Characters} class.
 * 
 * @author Jonathan Hawk
 * @see {@link Characters}
 * @see {@link Patterns}
 * @since 1.0.0
 */
public class Strings
{
	// whitespace
	public static final String TAB = String.valueOf(Characters.TAB);
	public static final String NL = String.valueOf(Characters.NL);
	public static final String CR = String.valueOf(Characters.CR);
	public static final String SPACE = String.valueOf(Characters.SPACE);

	// for those silly people who think Y and N make a good boolean. silly.
	public static final String Y = String.valueOf(Characters.Y);
	public static final String N = String.valueOf(Characters.N);
	
	// Maths
	public static final String MINUS = String.valueOf(Characters.MINUS);
	public static final String PLUS = String.valueOf(Characters.PLUS);
	public static final String TIMES = String.valueOf(Characters.TIMES);
	public static final String DIVIDE = String.valueOf(Characters.DIVIDE);
	public static final String EQUALS = String.valueOf(Characters.EQUALS);
	
	// numbers
	public static final String DOLLAR = String.valueOf(Characters.DOLLAR);
	public static final String PERCENT = String.valueOf(Characters.PERCENT);
	public static final String ZERO = String.valueOf(Characters.ZERO);
	public static final String ONE = String.valueOf(Characters.ONE);
	
	// punctuation
	public static final String DOT = String.valueOf(Characters.DOT);
	public static final String COMMA = String.valueOf(Characters.COMMA);
	public static final String QUESTION = String.valueOf(Characters.QUESTION);
	public static final String COLON = String.valueOf(Characters.COLON);
	public static final String SEMICOLON = String.valueOf(Characters.SEMICOLON);
	public static final String BANG = String.valueOf(Characters.BANG);
	public static final String QUOTE = String.valueOf(Characters.QUOTE);
	public static final String APOSTROPHE = String.valueOf(Characters.APOSTROPHE);
	public static final String SLASH = String.valueOf(Characters.SLASH);
	public static final String DASH = String.valueOf(Characters.DASH);
	public static final String ENDASH = String.valueOf(Characters.ENDASH);
	public static final String EMDASH = String.valueOf(Characters.EMDASH);
	public static final String HELLIP = String.valueOf(Characters.HELLIP);

	// typography
	public static final String BACKSLASH = String.valueOf(Characters.BACKSLASH);
	public static final String PIPE = String.valueOf(Characters.PIPE);
	public static final String HASH = String.valueOf(Characters.HASH);
	public static final String AT = String.valueOf(Characters.AT);
	public static final String CARET = String.valueOf(Characters.CARET);
	public static final String STAR = String.valueOf(Characters.STAR);
	public static final String AMPERSAND = String.valueOf(Characters.AMPERSAND);
	public static final String TILDE = String.valueOf(Characters.TILDE);
	public static final String UNDERSCORE = String.valueOf(Characters.UNDERSCORE);
	
	// common synonyms
	public static final String PERIOD = DOT;
	public static final String POUND = HASH;
	public static final String EXCLAMATION = BANG;
	public static final String ASTERISK = STAR;
	
	// because everybody loves The Cloud!
	public static final String CLOUD = String.valueOf(Characters.CLOUD);
	// seriously! SSV! Snowman Separated Values. Stop laughing.
	public static final String SNOWMAN = String.valueOf(Characters.SNOWMAN);
	// you never know when this will come in handy
	public static final String POO = "\uD83D\uDCA9";
	
	// Java keywords
	public static final String TRUE = Boolean.TRUE.toString();
	public static final String FALSE = Boolean.FALSE.toString();
	public static final String NULL = "null";
	public static final String EMPTY = StringUtils.EMPTY;
	
	public static final Set<String> TRUTHY = ImmutableSet.of(TRUE, ONE, Y);
	
	private Strings() {}
	
	/**
	 * Determines if the supplied string is "truthy".
	 * 
	 * <p>A "truthy" string is one of {@code true}, {@code 1}, or {@code Y}
	 * (determined in a case-insensitive manner). Anything else including
	 * {@code null} will return false.
	 * 
	 * @param value The value to test for "truthiness" or null
	 * @return Whether the String is "truthy"
	 */
	public static boolean isTruthy(String value)
	{
		if (value == null) {
			return false;
		}
		String trimmed = StringUtils.trimToEmpty(value);
		for (String v : TRUTHY) {
			if (v.equalsIgnoreCase(trimmed)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * If a String, once trimmed, is longer than {@code length}, it will be trimmed and terminated with an ellipsis.
	 * 
	 * @param value The string value (if null is passed, null will be returned)
	 * @param length The maximum length of string before cut
	 * @throws IllegalArgumentException if length is negative or zero
	 * @return A (potentially) cut String.
	 */
	public static String cut(String value, int length)
	{
		if (value == null) {
			return null;
		}
		if (length < 1) {
			throw new IllegalArgumentException("length must be greater than zero");
		}
		value = value.trim();
		if (value.length() < length) {
			return value;
		} else {
			return value.substring(0, length).concat(HELLIP);
		}
	}
	
	/**
	 * Gets the file extension of the filename provided.
	 * 
	 * @param name The file name
	 * @return The file extension
	 */
	public static String getExtension(String name)
	{
		return name == null ? null : name.substring(name.lastIndexOf('.') + 1);
	}	
}
