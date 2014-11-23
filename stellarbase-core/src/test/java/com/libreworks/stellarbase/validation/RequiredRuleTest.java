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
package com.libreworks.stellarbase.validation;

import static org.junit.Assert.*;
import java.util.Date;
import com.libreworks.stellarbase.test.SimpleBean;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 * Test for {@link RequiredRule}.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class RequiredRuleTest
{
	/**
	 * Tests validate with no arguments
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testValidateIllegal()
	{
		new RequiredRule();
	}
	
	/**
	 * Tests validate
	 */
	@Test
	public void testValidate()
	{
		RequiredRule object = new RequiredRule("name", "count", "when");
		SimpleBean target = new SimpleBean();
		target.setName("Test");
		target.setCount(2);
		target.setWhen(new Date());
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "target");
		object.validate(target, errors);
		assertFalse(errors.hasErrors());
	}
	
	/**
	 * Tests validate
	 */
	@Test
	public void testValidateBad()
	{
		RequiredRule object = new RequiredRule("name", "count", "when");
		SimpleBean target = new SimpleBean();
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "target");
		object.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(3, errors.getFieldErrorCount());
	}

	/**
	 * Tests toString
	 */
	@Test
	public void testToString()
	{
		RequiredRule object = new RequiredRule("field1", "field2", "field3");
		assertEquals("Required[field1,field2,field3]", object.toString());
	}
}
