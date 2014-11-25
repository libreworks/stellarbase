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

/**
 * Static class with some pretty common character constants.
 *
 * @author Jonathan Hawk
 * @see {@link Strings}
 * @since 1.0.0
 */
public class Characters
{
	// whitespace
	public static final char TAB = '\t';
	public static final char NL = '\n';
	public static final char CR = '\r';
	public static final char SPACE = ' ';

	// for those silly people who think Y and N make a good boolean. silly.
	public static final char Y = 'Y';
	public static final char N = 'N';
	
	// Maths
	public static final char MINUS = '\u2212';
	public static final char PLUS = '+';
	public static final char TIMES = '\u00d7';
	public static final char DIVIDE = '\u00f7';
	public static final char EQUALS = '=';
	
	// numbers
	public static final char DOLLAR = '$';
	public static final char PERCENT = '%';
	public static final char ZERO = '0';
	public static final char ONE = '1';
	
	// punctuation
	public static final char DOT = '.';
	public static final char COMMA = ',';
	public static final char QUESTION = '?';
	public static final char COLON = ':';
	public static final char SEMICOLON = ';';
	public static final char BANG = '!';
	public static final char QUOTE = '"';
	public static final char APOSTROPHE = '\'';
	public static final char SLASH = '/';
	public static final char DASH = '-';
	public static final char ENDASH = '\u2013';
	public static final char EMDASH = '\u2014';
	public static final char HELLIP = '\u2026';

	// typography
	public static final char BACKSLASH = '\\';
	public static final char PIPE = '|';
	public static final char HASH = '#';
	public static final char AT = '@';
	public static final char CARET = '^';
	public static final char STAR = '*';
	public static final char AMPERSAND = '&';
	public static final char TILDE = '~';
	public static final char UNDERSCORE = '_';
	
	// common synonyms
	public static final char PERIOD = DOT;
	public static final char POUND = HASH;
	public static final char EXCLAMATION = BANG;
	public static final char ASTERISK = STAR;
	
	// because everybody loves The Cloud!
	public static final char CLOUD = '\u2601'; 
	// seriously! SSV! Snowman Separated Values. Stop laughing.
	public static final char SNOWMAN = '\u2603';
	
	private Characters() {}
}
