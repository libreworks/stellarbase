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

import java.util.regex.Pattern;

/**
 * Static class with some pretty common compiled regular expression patterns.
 *
 * @author Jonathan Hawk
 * @see {@link Strings}
 * @since 1.0.0
 */
public class Patterns
{
	public static final Pattern WHITESPACE = Pattern.compile("\\s+");
	public static final Pattern DOT = Pattern.compile(Strings.DOT);
	public static final Pattern COMMA = Pattern.compile(Strings.COMMA);
	public static final Pattern COMMA_SPACE = Pattern.compile(",\\s*");
	public static final Pattern DASH = Pattern.compile(Strings.DASH);
	public static final Pattern UNDERSCORE = Pattern.compile(Strings.UNDERSCORE);
	public static final Pattern PIPE = Pattern.compile(Strings.PIPE);
	public static final Pattern AMPERSAND = Pattern.compile(Strings.AMPERSAND);
	
	private Patterns() {}
}
