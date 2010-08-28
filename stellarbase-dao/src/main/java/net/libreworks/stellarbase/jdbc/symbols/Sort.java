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
package net.libreworks.stellarbase.jdbc.symbols;

import org.springframework.util.Assert;

/**
 * A struct containing a field and a sorting direction.
 * 
 * Ported from the PHP framework Xyster by Jonathan Hawk, the original author.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class Sort implements Symbol
{
    private static final long serialVersionUID = 1L;

    private Field field;
    private boolean ascending;
    
    private static final String ASC = " ASC";
    private static final String DESC = " DESC";
    
    protected Sort(Field field, boolean ascending)
    {
        Assert.notNull(field);
        this.field = field;
        this.ascending = ascending;
    }

    public Field getField()
    {
        return field;
    }

    public boolean isAscending()
    {
        return ascending;
    }
    
    @Override
    public String toString()
    {
        return field.getName() + (ascending ? ASC : DESC);
    }
    
    /**
     * Creates a new ascending Sort for the field specified
     * 
     * @param name The field name
     * @return The ascending Sort
     */
    public static Sort asc(String name)
    {
        return new Sort(Field.named(name), true);
    }
    
    /**
     * Creates a new ascending Sort for the field specified
     * 
     * @param field The field
     * @return The ascending Sort
     */
    public static Sort asc(Field field)
    {
        return new Sort(field, true);
    }
    
    /**
     * Creates a new descending Sort for the field specified
     * 
     * @param name The field name
     * @return The descending Sort
     */
    public static Sort desc(String name)
    {
        return new Sort(Field.named(name), false);
    }
    
    /**
     * Creates a new descending Sort for the field specified
     * 
     * @param field The field
     * @return The descending Sort
     */
    public static Sort desc(Field field)
    {
        return new Sort(field, false);
    }
}
