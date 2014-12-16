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
package com.libreworks.stellarbase.sql;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.libreworks.stellarbase.util.Arguments;

/**
 * Immutable SQL fragment and any parameters.
 * 
 * @author Jonathan Hawk
 */
public class Fragment implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final String sql;
    private final List<Object> parameters;

    /**
     * Creates a new SQL fragment with no parameters.
     * 
     * @param sql The SQL fragment (cannot be blank).
     */
    public Fragment(String sql)
    {
        this.sql = Arguments.checkBlank(sql);
        this.parameters = ImmutableList.of();
    }
    
    /**
     * Creates a new SQL fragment with the given parameters.
     * 
     * <p>Note that {@code null} is not permitted as a parameter value. Since in
     * the SQL standard, {@code NULL} cannot be tested for equality, you should
     * be using {@code IS NULL} or {@code IS NOT NULL} in your SQL.
     * 
     * @param sql The SQL fragment (cannot be blank)
     * @param parameters The parameters, none of which can be null
     */
    public Fragment(String sql, List<?> parameters)
    {
    	this.sql = Arguments.checkBlank(sql);
    	this.parameters = ImmutableList.copyOf(Arguments.checkNull(parameters));
    }
    
    /**
     * @return the SQL fragment
     */
    public String getSql()
    {
        return sql;
    }
    
    /**
     * @return the bound parameters
     */
    public List<Object> getParameters()
    {
        return parameters;
    }
}
