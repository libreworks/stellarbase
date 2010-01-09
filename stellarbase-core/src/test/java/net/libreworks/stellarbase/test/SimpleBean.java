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
package net.libreworks.stellarbase.test;

import java.util.Date;

/**
 * A bean for testing.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class SimpleBean
{
	private Long id;
	private String name;
	private Integer count;
	private Double money;
	private Date when;
	private Boolean flag;

	/**
	 * @return the count
	 */
	public Integer getCount()
	{
		return count;
	}

	/**
	 * @return the flag
	 */
	public Boolean getFlag()
	{
		return flag;
	}

	/**
     * @return the id
     */
    public Long getId()
    {
    	return id;
    }

	/**
	 * @return the money
	 */
	public Double getMoney()
	{
		return money;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the when
	 */
	public Date getWhen()
	{
		return when;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count)
	{
		this.count = count;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(Boolean flag)
	{
		this.flag = flag;
	}

	/**
     * @param id the id to set
     */
    public void setId(Long id)
    {
    	this.id = id;
    }

	/**
	 * @param money the money to set
	 */
	public void setMoney(Double money)
	{
		this.money = money;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param when the when to set
	 */
	public void setWhen(Date when)
	{
		this.when = when;
	}
}
