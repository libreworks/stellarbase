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
package com.libreworks.stellarbase.persistence;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

/**
 * A data transfer object that holds pagination information (i.e. items per page, item offset, field sort order)
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class Pagination
{
	private final Integer max;
	private final Integer offset;
	private final Map<String,Boolean> order;

	private static final Integer ZERO = Integer.valueOf(0);
	private static final Pagination DEFAULT = new Pagination(Integer.MAX_VALUE, 0, ImmutableMap.<String,Boolean>of());
	
	private Pagination(Integer max, Integer offset, Map<String,Boolean> order)
	{
		this.max = max;
		this.offset = offset;
		this.order = order;
	}
	
	/**
	 * Returns a default Pagination object, with no {@code offset}, {@link Integer#MAX_VALUE} as {@code max} and an empty {@code order} {@link Map}.
	 *    
	 * @return the default Pagination object
	 */
	public static Pagination create()
	{
		return DEFAULT;
	}
	
	/**
	 * Creates a Pagination object for the given max and offset values and an empty {@code order} {@link Map}.
	 * 
	 * @param max The max number of records to return. If {@code null} or a negative number is provided, this defaults to {@link Integer#MAX_VALUE}
	 * @param offset The offset in the records. If {@code null} or a negative number is provided, this defaults to {@value 0}
	 * @return the configured Pagination object 
	 */
	public static Pagination create(Integer max, Integer offset)
	{
		max = max == null || max < 0 ? Integer.MAX_VALUE : max;
		offset = offset == null || offset < 0 ? ZERO : offset;
		return Integer.MAX_VALUE == max.intValue() && ZERO.equals(offset) ?
			DEFAULT : new Pagination(max, offset, ImmutableMap.<String,Boolean>of());
	}
	
	/**
	 * Creates a Pagination object for the given max, offset, and order values.
	 * 
	 * <p>The {@code order} Map should have field names as keys and the direction
	 * as a {@link Boolean} (i.e. {@literal true} for ascending, {@literal false}
	 * for descending). Blank or null keys are ignored. Null values are
	 * interpreted as {@literal false}.
	 * 
	 * @param max The max number of records to return. If {@code null} or a negative number is provided, this defaults to {@link Integer#MAX_VALUE}
	 * @param offset The offset in the records. If {@code null} or a negative number is provided, this defaults to {@literal 0}
	 * @param order The field sort order. If {@code null} is provided, this defaults to an empty Map.
	 * @return the configured Pagination object 
	 */
	public static Pagination create(Integer max, Integer offset, Map<String,Boolean> order)
	{
		max = max == null ? Integer.MAX_VALUE : max;
		offset = offset == null ? ZERO : offset;
		ImmutableMap.Builder<String,Boolean> sort = ImmutableMap.<String, Boolean>builder();
		if (order != null) {
			for (Map.Entry<String,Boolean> entry : order.entrySet()) {
				if (!StringUtils.isBlank(entry.getKey())) {
					sort.put(entry.getKey(), Boolean.TRUE.equals(entry.getValue())
						? Boolean.TRUE : Boolean.FALSE);
				}
			}
		}
		Map<String,Boolean> sortOrder = sort.build();
		return Integer.MAX_VALUE == max.intValue() && ZERO.equals(offset) && order.isEmpty() ?
			DEFAULT : new Pagination(max, offset, sortOrder);
	}

	/**
	 * @return the max
	 */
	public Integer getMax()
	{
		return max;
	}

	/**
	 * @return the offset
	 */
	public Integer getOffset()
	{
		return offset;
	}

	/**
	 * @return the order
	 */
	public Map<String, Boolean> getOrder()
	{
		return order;
	}
}
