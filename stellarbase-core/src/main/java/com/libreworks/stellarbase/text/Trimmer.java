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

import com.google.common.base.CharMatcher;
import com.libreworks.stellarbase.util.Arguments;

/**
 * An immutable object which trims the whitespace from CharSequence objects.
 * 
 * <p>A Trimmer can be configured to trim just the leading whitespace, just the
 * trailing whitespace, or both (the default).
 * 
 * <p>It can be configured to return either an empty String (the default) or
 * {@code null} when the trim operation finishes and results in a zero-length
 * String.
 * 
 * <p>You can supply your own String to use containing characters to trim. If
 * {@code null} is provided as the String containing trimmable characters (which
 * is the default), the Trimmer will remove anything for which
 * {@link Character#isWhitespace(char)} succeeds. 
 * 
 * <p>Internally, this class delegates character matching to Guava's 
 * {@link CharMatcher} class. Indeed, you may wonder why you wouldn't just use
 * a CharMatcher yourself, such as: {@code CharMatcher.WHITESPACE.trimFrom(string)}.
 * Simple answer: the ability to configure return null or empty, as well as the
 * ability to pass null to the {@link #trim(CharSequence)} method. 
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class Trimmer
{
	private final CharMatcher matcher;
	private final boolean returnNull;
	private final boolean leading;
	private final boolean trailing;
	
	/**
	 * A Trimmer that matches all Unicode whitespace characters.
	 * 
	 * @return a new Trimmer
	 * @see {@link CharMatcher#WHITESPACE}
	 */	
	public static final Trimmer WHITESPACE = new Trimmer(CharMatcher.WHITESPACE, false, true, true); 
	
	private Trimmer(CharMatcher matcher, boolean returnNull, boolean leading, boolean trailing)
	{
		this.matcher = Arguments.checkNull(matcher);
		this.returnNull = returnNull;
		this.leading = leading;
		this.trailing = trailing;
	}
	
	/**
	 * Creates a new Trimmer that delegates to the provided CharMatcher.
	 * 
	 * @param matcher the matcher
	 * @return a new Trimmer
	 */	
	public static Trimmer on(CharMatcher matcher)
	{
		return CharMatcher.WHITESPACE.equals(matcher) ?
			WHITESPACE : new Trimmer(matcher, false, true, true);
	}
	
	/**
	 * Creates a new Trimmer that will strip any characters in {@code toTrim}.
	 * 
	 * @param toTrim the characters to strip
	 * @return a new Trimmer
	 * @see {@link CharMatcher#anyOf(CharSequence)}
	 */
	public static Trimmer on(CharSequence toTrim)
	{
		return new Trimmer(CharMatcher.anyOf(toTrim), false, true, true);
	}
	
	/**
	 * Creates and returns a new Trimmer that only trims characters from the beginning of a String.
	 * 
	 * <p>If this Trimmer is already leading-only, this method returns {@code this}.
	 * 
	 * @return a new leading-only Trimmer
	 */
	public Trimmer leading()
	{
		return (leading && !trailing) ?
			this : new Trimmer(matcher, returnNull, true, false);
	}
	
	/**
	 * Creates and returns a new Trimmer that only trims characters from the end of a String.
	 * 
	 * <p>If this Trimmer is already trailing-only, this method returns {@code this}.
	 * 
	 * @return a new trailing-only Trimmer
	 */
	public Trimmer trailing()
	{
		return (!leading && trailing) ?
			this : new Trimmer(matcher, returnNull, false, true);
	}
	
	/**
	 * Creates and returns a new Trimmer that will return an empty String if all characters are stripped.
	 * 
	 * @return a new Trimmer that returns an empty String upon truncation
	 */
	public Trimmer toEmpty()
	{
		return !returnNull ?
			this : new Trimmer(matcher, false, leading, trailing);
	}
	
	/**
	 * Creates and returns a new Trimmer that will return null if all characters are stripped.
	 * 
	 * @return a new Trimmer that returns null upon truncation
	 */
	public Trimmer toNull()
	{
		return returnNull ?
			this : new Trimmer(matcher, true, leading, trailing);
	}
	
	/**
	 * Returns a trimmed String according to the configuration details.
	 *  
	 * @param value The CharSequence to trim
	 * @return The trimmed String
	 */
	public String trim(final CharSequence value)
	{
		CharSequence trimmed = value == null ? "" : value;
		if (leading && trailing) {
			trimmed = matcher.trimFrom(trimmed);
		} else if (leading) {
			trimmed = matcher.trimLeadingFrom(trimmed);
		} else if (trailing) {
			trimmed = matcher.trimTrailingFrom(trimmed);
		}
		return trimmed.length() == 0 ? (returnNull ? null : "") : trimmed.toString();
	}
}
