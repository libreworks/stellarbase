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
package com.libreworks.stellarbase.persistence.criteria;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AggregateExpressionTest
{
    private AggregateExpression<?> object;
    
    @Before
    public void setUp() throws Exception
    {
        object = AggregateExpression.avg(fieldNamed("foobar"));
    }

    @Test
    public void testToString()
    {
        assertEquals("AVG(foobar)", object.toString());
    }

    @Test
    public void testGetFunction()
    {
        assertSame(AggregateExpression.Function.AVG, object.getFunction());
    }

    private Field<?> fieldNamed(String name)
    {
    	return new FieldImpl<Object>(name, Object.class);
    }    
}
