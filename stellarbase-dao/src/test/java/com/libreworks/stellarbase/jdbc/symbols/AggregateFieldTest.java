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
package com.libreworks.stellarbase.jdbc.symbols;

import static org.junit.Assert.*;

import java.util.regex.Matcher;

import org.junit.Before;
import org.junit.Test;

public class AggregateFieldTest
{
    private AggregateField object;
    
    @Before
    public void setUp() throws Exception
    {
        object = new AggregateField(Aggregate.AVG, "foobar");
    }

    @Test
    public void testToString()
    {
        assertEquals("AVG(foobar)", object.toString());
    }

    @Test
    public void testGetFunction()
    {
        assertSame(Aggregate.AVG, object.getFunction());
    }

    @Test
    public void testMatch()
    {
        Matcher m1 = AggregateField.match("notafunction");
        assertFalse(m1.matches());
        
        Matcher m2 = AggregateField.match("COUNT(foobaz)");
        assertTrue(m2.matches());
        assertEquals("COUNT", m2.group(1));
        assertEquals("foobaz", m2.group(2));
    }
}
