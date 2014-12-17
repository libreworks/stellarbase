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

public class FieldImplTest
{
    private FieldImpl<?> object;
    
    @Before
    public void setUp() throws Exception
    {
        object = new FieldImpl<Object>("foo", Object.class);
    }

    @Test
    public void testHashCode()
    {
    	Object object2 = new FieldImpl<Object>("foo", Object.class);
    	Object object3 = new FieldImpl<Object>("bar", Object.class);
        assertEquals(object2.hashCode(), object.hashCode());
        assertNotEquals(object3.hashCode(), object.hashCode());
    }

    @Test
    public void testEqualsObject()
    {
        assertTrue(object.equals(object));
        assertFalse(object.equals(null));
        assertFalse(object.equals("foo"));
        
        FieldImpl<?> other = new FieldImpl<Object>("a", Object.class);
        assertFalse(object.equals(other));
        FieldImpl<?> other2 = new FieldImpl<Object>("foo", Object.class);
        assertTrue(object.equals(other2));
    }

    @Test
    public void testGetName()
    {
        assertEquals("foo", object.getName());
    }

    @Test
    public void testToString()
    {
        assertEquals("foo", object.toString());
    }
}
