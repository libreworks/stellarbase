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

import static org.junit.Assert.*;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;

public class SortTest
{
    private Sort object;
    
    @Before
    public void setUp() throws Exception
    {
        object = new Sort(new Field("foobar"), true);
    }

    @Test
    public void testHashCode()
    {
        int hashCode = new HashCodeBuilder()
            .append(true)
            .append(object.getField())
            .toHashCode();
        assertEquals(hashCode, object.hashCode());
    }

    @Test
    public void testEqualsObject()
    {
        assertTrue(object.equals(object));
        assertFalse(object.equals(null));
        assertFalse(object.equals(""));
        
        Sort other = new Sort(object.getField(), true);
        assertTrue(object.equals(other));
        Sort another = new Sort(object.getField(), false);
        assertFalse(object.equals(another));
    }

    @Test
    public void testGetField()
    {
        assertEquals(new Field("foobar"), object.getField());
    }

    @Test
    public void testIsAscending()
    {
        assertTrue(object.isAscending());
    }

    @Test
    public void testToString()
    {
        assertEquals("foobar ASC", object.toString());
    }

    @Test
    public void testAscField()
    {
        Field field = new Field("what");
        Sort object = Sort.asc(field);
        assertSame(field, object.getField());
        assertTrue(object.isAscending());
    }

    @Test
    public void testAscString()
    {
        Sort object = Sort.asc("what");
        assertEquals("what", object.getField().getName());
        assertTrue(object.isAscending());
    }

    @Test
    public void testDescField()
    {
        Field field = new Field("what");
        Sort object = Sort.desc(field);
        assertSame(field, object.getField());
        assertFalse(object.isAscending());
    }

    @Test
    public void testDescString()
    {
        Sort object = Sort.desc("what");
        assertEquals("what", object.getField().getName());
        assertFalse(object.isAscending());
    }
}
