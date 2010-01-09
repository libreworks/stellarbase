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
import net.libreworks.stellarbase.test.SimpleBean;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class OneOfRuleTest
{
	private OneOfRule object;
	
	/**
	 * Sets up
	 */
	@Before
	public void setUp()
	{
		object = new OneOfRule("name", "Alice", "Bob", "Carol");
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.OneOfRule#getLabel()}.
	 */
	@Test
	public void testGetLabel()
	{
		assertEquals("OneOf", object.getLabel());
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.OneOfRule#getConstraintLabel()}.
	 */
	@Test
	public void testGetConstraintLabel()
	{
		assertEquals("[Alice, Bob, Carol]", object.getConstraintLabel());
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.AbstractOneFieldRule#validate(java.lang.Object, org.springframework.validation.Errors)}.
	 */
	@Test
	public void testValidate()
	{
		SimpleBean target = new SimpleBean();
		target.setName("Alice");
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "target");
		object.validate(target, errors);
		assertFalse(errors.hasErrors());
	}
	
	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.AbstractOneFieldRule#validate(java.lang.Object, org.springframework.validation.Errors)}.
	 */
	@Test
	public void testValidateBad()
	{
		SimpleBean target = new SimpleBean();
		target.setName("Diane");
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "target");
		object.validate(target, errors);
		assertTrue(errors.hasErrors());
	}
}
