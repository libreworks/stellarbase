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

public class OrderImplTest
{
    private OrderImpl object;
    
    @Before
    public void setUp() throws Exception
    {
        object = new OrderImpl(fieldNamed("foobar"), true);
    }

    @Test
    public void testHashCode()
    {
    	Object object2 = new OrderImpl(fieldNamed("foobar"), true);
    	Object object3 = new OrderImpl(fieldNamed("nope"), true);
        assertEquals(object2.hashCode(), object.hashCode());
        assertNotEquals(object3.hashCode(), object.hashCode());
    }

    @Test
    public void testEqualsObject()
    {
        assertTrue(object.equals(object));
        assertFalse(object.equals(null));
        assertFalse(object.equals(""));
        
        Order other = new OrderImpl(object.getExpression(), true);
        assertTrue(object.equals(other));
        Order another = new OrderImpl(object.getExpression(), false);
        assertFalse(object.equals(another));
    }

    @Test
    public void testGetField()
    {
        assertEquals(fieldNamed("foobar"), object.getExpression());
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
        Field<?> field = fieldNamed("what");
        Order object = new OrderImpl(field, true);
        assertSame(field, object.getExpression());
        assertTrue(object.isAscending());
    }

    @Test
    public void testDescField()
    {
        Field<?> field = fieldNamed("what");
        Order object = new OrderImpl(field, false);
        assertSame(field, object.getExpression());
        assertFalse(object.isAscending());
    }

    private Field<?> fieldNamed(String name)
    {
    	return new FieldImpl<Object>(name, Object.class);
    }
}
