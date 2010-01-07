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
public class RegexRuleTest
{
	protected RegexRule object;

	/**
	 * Sets up class
	 */
	@Before
	public void setUp()
	{
		object = new RegexRule("name", "[a-hj-z]+"); // every letter but "i"
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.RegexRule#getLabel()}.
	 */
	@Test
	public void testGetLabel()
	{
		assertEquals("Regex", object.getLabel());
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.RegexRule#getConstraintLabel()}.
	 */
	@Test
	public void testGetConstraintLabel()
	{
		assertEquals("[a-hj-z]+", object.getConstraintLabel());
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.AbstractOneFieldRule#validate(java.lang.Object, org.springframework.validation.Errors)}.
	 */
	@Test
	public void testValidate()
	{
		SimpleBean target = new SimpleBean();
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "target");
		target.setName("abcdefghmnopqz");
		object.validate(target, errors);
		assertFalse(errors.hasErrors());
	}

	/**
	 * Test method for validate
	 */
	@Test
	public void testValidateBad()
	{
		SimpleBean target = new SimpleBean();
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "target");
		target.setName("abcdefghminopqz");
		object.validate(target, errors);
		assertTrue(errors.hasErrors());
	}
}
