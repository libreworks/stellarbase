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
package net.libreworks.stellarbase.context;

import static org.junit.Assert.*;
import java.io.Serializable;
import java.util.Date;
import net.libreworks.stellarbase.model.Identifiable;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;

/**
 * @author Jonathan Hawk
 * @version $Id: UpdateEventTest.java 7 2010-01-07 06:48:34Z jonathanhawk $
 */
public class UpdateEventTest
{
	private UpdateEvent object;
	
	/**
	 * Sets up
	 */
	@SuppressWarnings("serial")
    @Before
	public void setUp()
	{
		object = new UpdateEvent(new Identifiable<Serializable>(){
			public String getCreatedBy()
            {
	            return null;
            }
			public Date getCreatedOn()
            {
	            return null;
            }
			public Serializable getId()
            {
	            return null;
            }
			public void setCreatedBy(String createdBy)
            {
	            
            }
			public void setCreatedOn(Date createdOn)
            {
            }
		}, new MutablePropertyValues(), "foobar1");
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.context.UpdateEvent#getOldValues()}.
	 */
	@Test
	public void testGetOldValues()
	{
		assertNotNull(object.getOldValues());
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.context.AbstractDataEvent#getEntity()}.
	 */
	@Test
	public void testGetEntity()
	{
		assertNotNull(object.getEntity());
		assertEquals(object.getEntity(), object.getSource());
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.context.AbstractDataEvent#getBy()}.
	 */
	@Test
	public void testGetBy()
	{
		assertEquals("foobar1", object.getBy());
	}
}
