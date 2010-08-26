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
package net.libreworks.stellarbase.jdbc;

/**
 * An enum representing SQL operators: most of them, that is.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public enum Operator
{
	eq("="),
	neq("<>"),
	ge(">="),
	le("<="),
	lt("<"),
	gt(">"),
	in("IN"),
	notIn("NOT IN"),
	is("IS"),
	isNot("IS NOT"),
	like("LIKE"),
	notLike("NOT LIKE"),
	between("BETWEEN"),
	notBetween("NOT BETWEEN");
	
	private String sql;
	
	Operator(String sql)
	{
		this.sql = sql;
	}

	/**
	 * Gets the SQL equivalent of this operator.
	 * 
	 * @return The SQL equivalent
	 */
	public String getSql()
	{
		return sql;
	}
}
