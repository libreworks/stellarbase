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
package com.libreworks.stellarbase.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.libreworks.stellarbase.util.Arguments;

/**
 * A data transfer object responsible for holding a subset from a bigger result.
 * 
 * <p>This is typically used with paged searches in which the current batch of
 * results is a subset of a larger result set. Using this object, a repository
 * can provide a result subset with a count of all results to allow the front-
 * end to provide pagination.
 * 
 * <p>{@code SearchResult} objects are Serializable so long as the objects
 * contained in the {@code results} list are also Serializable.
 * 
 * @author Jonathan Hawk
 * @param <T> The type of result contained within
 */
public class SearchResults<T> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final List<T> results;
	private final int count;

	/**
	 * Creates a new ResultHolder for a list and count of total results.
	 *  
	 * @param results The subset of results from the entire search
	 * @param count The number of results in the entire search
	 */
	public SearchResults(List<T> results, int count)
	{
		this.results = Arguments.checkNull(results).contains(null) ?
			Collections.unmodifiableList(new ArrayList<T>(results)) :
			ImmutableList.copyOf(results);
		this.count = count;
	}
	
	/**
	 * @return the results
	 */
	public List<T> getResults()
	{
		return results;
	}
	
	/**
	 * @return the count
	 */
	public int getCount()
	{
		return count;
	}
}
