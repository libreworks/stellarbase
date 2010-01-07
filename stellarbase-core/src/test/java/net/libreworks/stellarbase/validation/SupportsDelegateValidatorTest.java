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
import java.util.Date;
import net.libreworks.stellarbase.test.SimpleBean;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class SupportsDelegateValidatorTest
{
	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.SupportsDelegateValidator#supports(java.lang.Class)}.
	 */
	@Test
	public void testSupports()
	{
		SupportsDelegateValidator object = new SupportsDelegateValidator(new TestValidator(), SimpleBean.class, Date.class);
		assertTrue(object.supports(SimpleBean.class));
		assertTrue(object.supports(Date.class));
		assertFalse(object.supports(Integer.class));
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.SupportsDelegateValidator#validate(java.lang.Object, org.springframework.validation.Errors)}.
	 */
	@Test
	public void testValidate()
	{
		TestValidator validator = new TestValidator();
		SupportsDelegateValidator object = new SupportsDelegateValidator(validator, SimpleBean.class, Date.class);
		SimpleBean target = new SimpleBean();
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "target");
		object.validate(target, errors);
		assertEquals(1, validator.count);
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.SupportsDelegateValidator#toString()}.
	 */
	@Test
	public void testToString()
	{
		TestValidator validator = new TestValidator();
		SupportsDelegateValidator object = new SupportsDelegateValidator(validator, SimpleBean.class, Date.class);
		assertEquals(validator + " supporting class net.libreworks.stellarbase.test.SimpleBean, class java.util.Date", object.toString());
	}
	
	protected class TestValidator implements Validator
	{
		protected int count;
		
		public boolean supports(Class<?> clazz)
        {
	        return false;
        }
		public void validate(Object target, Errors errors)
        {
			++count;
        }
	}
}
