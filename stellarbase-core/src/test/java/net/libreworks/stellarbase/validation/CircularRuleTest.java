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
package net.libreworks.stellarbase.validation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class CircularRuleTest
{
	private CircularRule object;
	
	@Before
	public void setUp() throws Exception
	{
		object = new CircularRule("parent");
	}

	@Test
	public void testGetConstraints()
	{
		assertEquals("parent", object.getConstraints());
	}

	@Test
	public void testGetLabel()
	{
		assertEquals("Circular", object.getLabel());
	}

	@Test
	public void testValidate()
	{
		TestBean b1 = new TestBean();
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(b1, "target");
		object.validate(b1, errors);
		assertEquals(0, errors.getErrorCount());
		b1.setParent(b1);
		object.validate(b1, errors);
		assertTrue(errors.hasFieldErrors("parent"));
	}
	
	protected static class TestBean
	{
		private TestBean parent;

		public void setParent(TestBean parent)
		{
			this.parent = parent;
		}

		public TestBean getParent()
		{
			return parent;
		}
	}
}
