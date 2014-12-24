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
	public static final String TAB = "\t";
	public static final String NL = "\n";
	public static final String CR = "\r";
	public static final String SPACE = " ";

	// for those silly people who think Y and N make a good boolean. silly.
	public static final String Y = "Y";
	public static final String N = "N";
	
	// Maths
	public static final String MINUS = "\u2212";
	public static final String PLUS = "+";
	public static final String TIMES = "\u00d7";
	public static final String DIVIDE = "\u00f7";
	public static final String EQUALS = "=";
	
	// numbers
	public static final String DOLLAR = "$";
	public static final String PERCENT = "%";
	public static final String ZERO = "0";
	public static final String ONE = "1";
	
	// punctuation
	public static final String DOT = ".";
	public static final String COMMA = ",";
	public static final String QUESTION = "?";
	public static final String COLON = ":";
	public static final String SEMICOLON = ";";
	public static final String BANG = "!";
	public static final String QUOTE = "\"";
	public static final String APOSTROPHE = "'";
	public static final String SLASH = "/";
	public static final String DASH = "-";
	public static final String ENDASH = "\u2013";
	public static final String EMDASH = "\u2014";
	public static final String HELLIP = "\u2026";

	// typography
	public static final String BACKSLASH = "\\";
	public static final String PIPE = "|";
	public static final String HASH = "#";
	public static final String AT = "@";
	public static final String CARET = "^";
	public static final String STAR = "*";
	public static final String AMPERSAND = "&";
	public static final String TILDE = "~";
	public static final String UNDERSCORE = "_";
	
	// common synonyms
	public static final String PERIOD = DOT;
	public static final String POUND = HASH;
	public static final String EXCLAMATION = BANG;
	public static final String ASTERISK = STAR;
	
	// because everybody loves The Cloud!
	public static final String CLOUD = "\u2601";
	// seriously! SSV! Snowman Separated Values. Stop laughing.
	public static final String SNOWMAN = "\u2603";
	// you never know when this will come in handy
	public static final String POO = "\uD83D\uDCA9";
	
	// Java keywords
	public static final String TRUE = Boolean.TRUE.toString();
	public static final String FALSE = Boolean.FALSE.toString();
	public static final String NULL = "null";
	public static final String EMPTY = "";
	
	public static final Set<String> TRUTHY = ImmutableSet.of(TRUE, ONE, Y);
	
	private Strings() {}
	
	/**
	 * Whether the object's string representation is blank.
	 * 
	 * <p>A null object will return true.
	 * 
	 * <p>This method is really just a shortcut for {@code isBlank(object.toString())}.
	 * 
	 * @param object
	 * @return whether the string representation of the value is
	 * @see Strings#isBlank(CharSequence)
	 */
	public static boolean isBlank(Object object)
	{
		return object == null || isBlank(object.toString()); 
	}
	
	/**
	 * Whether the provided value is null, empty, or contains only whitespace characters.
	 * 
	 * @param cs The value to test
	 * @return whether the value is blank
	 */
	public static boolean isBlank(CharSequence cs)
	{
		if (cs != null) {
			int len = cs.length();
			for (int i = 0; i < len; i++) {
				if (!Character.isWhitespace(cs.charAt(i))) {
					return false;
				}
			}
		}
		return true;
	}
	
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
		if (value == null || value.isEmpty()) {
			return false;
		}
		String trimmed = Trimmer.WHITESPACE.trim(value);
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
		value = Trimmer.WHITESPACE.trim(value);
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
		return name == null ? null : name.substring(name.lastIndexOf(Characters.DOT) + 1);
	}	
}
