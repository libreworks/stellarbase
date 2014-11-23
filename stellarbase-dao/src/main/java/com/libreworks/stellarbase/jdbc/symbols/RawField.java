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

/**
 * A field which should not be quoted when used in a statement.
 * 
 * @author Jonathan Hawk
 */
public class RawField extends Field
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new unquoted field
	 * 
	 * @param name The raw field
	 * @param alias The field alias
	 */
	public RawField(String name, String alias)
	{
		super(name, alias);
	}
}
