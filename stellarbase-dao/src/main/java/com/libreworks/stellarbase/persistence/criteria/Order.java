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
package com.libreworks.stellarbase.persistence.criteria;

/**
 * Defines ordering against an expression 
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public interface Order extends Symbol
{
	/**
	 * Whether this Order is ascending
	 * 
	 * @return Whether this Order is ascending
	 */
	boolean isAscending();
	
	/**
	 * Gets the ordered Expression
	 * 
	 * @return The ordered expression
	 */
	Expression<?> getExpression();
	
	/**
	 * Returns a new Order with a the direction reversed.
	 * 
	 * @return A reversed Order
	 */
	Order reverse();
}
