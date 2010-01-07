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
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class RuleValidatorTest implements Rule
{
	private RuleValidator object;
	private int called = 0;
	
	/**
	 * Setup the test
	 */
	@Before
	public void setUp()
	{
		object = new RuleValidator(Arrays.asList(new Rule[]{this}));
		called = 0;
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.RuleValidator#supports(java.lang.Class)}.
	 */
	@Test
	public void testSupports()
	{
		assertTrue(object.supports(Object.class));
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.RuleValidator#validate(java.lang.Object, org.springframework.validation.Errors)}.
	 */
	@Test
	public void testValidate()
	{
		Integer target = 0;
		object.validate(target, new BeanPropertyBindingResult(target, "target"));
		assertEquals(1, called);
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.RuleValidator#toString()}.
	 */
	@Test
	public void testToString()
	{
		assertEquals("RuleValidator([" + this + "])", object.toString());
	}

	/**
	 * Self-shunting test
	 * 
	 * @param target
	 * @param errors
	 */
	public void validate(Object target, Errors errors)
    {
		++called;
    }
}
