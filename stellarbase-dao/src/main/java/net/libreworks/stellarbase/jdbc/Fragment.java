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

import java.util.ArrayList;

/**
 * Parameters for a SQL query.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class Fragment
{
    private String sql;
    private ArrayList<Object> parameters = new ArrayList<Object>();
    
    public Fragment(String sql)
    {
        this.sql = sql;
    }
    
    public String getSql()
    {
        return sql;
    }
    
    public ArrayList<Object> getParameters()
    {
        return parameters;
    }
    
    public void addParameters(Fragment fragment)
    {
        parameters.addAll(fragment.parameters);
    }
}
