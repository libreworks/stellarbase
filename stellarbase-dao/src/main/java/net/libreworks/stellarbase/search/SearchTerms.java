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
package net.libreworks.stellarbase.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A data transfer object to hold search terms: required, optional, forbidden.
 *
 * @author Jonathan Hawk
 * @version $Id$
 */
public class SearchTerms implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> any = new ArrayList<String>();
	private ArrayList<String> all = new ArrayList<String>();
	private ArrayList<String> not = new ArrayList<String>();
	
	/**
	 * Adds the terms to the list of those that are optional.
	 * 
	 * @param terms The optional terms
	 * @return provides a fluent interface
	 */
	public SearchTerms any(String... terms)
	{
		any.addAll(Arrays.asList(terms));
		return this;
	}
	
	/**
	 * Adds the terms to the list of those that are required.
	 * 
	 * @param terms The required terms
	 * @return provides a fluent interface
	 */
	public SearchTerms all(String... terms)
	{
		all.addAll(Arrays.asList(terms));
		return this;
	}
	
	/**
	 * Adds the terms to the list of those that are forbidden.
	 * 
	 * @param terms The forbidden terms
	 * @return provides a fluent interface
	 */
	public SearchTerms not(String... terms)
	{
		not.addAll(Arrays.asList(terms));
		return this;
	}
}