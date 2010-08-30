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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import net.libreworks.stellarbase.jdbc.symbols.AggregateField;
import net.libreworks.stellarbase.jdbc.symbols.Criterion;
import net.libreworks.stellarbase.jdbc.symbols.Expression;
import net.libreworks.stellarbase.jdbc.symbols.Field;
import net.libreworks.stellarbase.jdbc.symbols.Junction;
import net.libreworks.stellarbase.jdbc.symbols.Sort;

/**
 * Turns symbols into a SQL query
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class Translator
{
    private String table;
    private String idQuote;
    
    public Translator(DatabaseMetaData dbMeta) throws SQLException
    {
        this.idQuote = dbMeta.getIdentifierQuoteString();
    }
    
    public Translator setTable(String name)
    {
        this.table = name;
        return this;
    }
    
    public Fragment translateField(Field field, boolean quote)
    {
        String fieldName = quote ? idQuote + field.getName() + idQuote : field.getName();
        if ( !StringUtils.isBlank(table) ) {
            fieldName = table + "." + fieldName;
        }
        return new Fragment(field instanceof AggregateField ?
                ((AggregateField)field).getFunction() + "(" + fieldName + ")" :
                fieldName);
    }
    
    public Fragment translateSort(Sort sort, boolean quote)
    {
        return new Fragment(translateField(sort.getField(), quote) + " "
                + (sort.isAscending() ? "ASC" : "DESC"));
    }
    
    public Fragment translateCriterion(Criterion criterion, boolean quote)
    {
        Fragment fragment = null;
        if ( criterion instanceof Expression ) {
            fragment = translateExpression((Expression) criterion, quote);
        } else if ( criterion instanceof Junction ) {
            fragment = translateJunction((Junction) criterion, quote);
        }
        return fragment;
    }
    
    public Fragment translateExpression(Expression expression, boolean quote)
    {
        return null;
    }
    
    public Fragment translateJunction(Junction junction, boolean quote)
    {
        return null;
    }
}
