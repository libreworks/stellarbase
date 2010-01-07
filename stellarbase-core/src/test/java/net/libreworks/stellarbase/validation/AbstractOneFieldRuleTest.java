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
import org.junit.Test;
import org.springframework.validation.Errors;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class AbstractOneFieldRuleTest
{
	private String fieldValue = "awesome";
	
	/**
     * @return the awesome
     */
    public String getFieldValue()
    {
    	return fieldValue;
    }

	/**
     * @param awesome the awesome to set
     */
    public void setFieldValue(String awesome)
    {
    	this.fieldValue = awesome;
    }

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.AbstractOneFieldRule#validate(java.lang.Object, org.springframework.validation.Errors)}.
	 */
	@Test
	public void testValidate()
	{
		AwesomeRule object = new AwesomeRule();
		object.validate(this, null);
	}
	
	/**
	 * Test for getConstraints
	 */
	@Test
	public void testGetConstraints()
	{
		AwesomeRule object = new AwesomeRule();
		assertEquals("fieldValue:awesome", object.getConstraints());
	}
	
	/**
	 * Test method for toString
	 */
	@Test
	public void testToString()
	{
		AwesomeRule object = new AwesomeRule();
		assertEquals("Awesome[fieldValue:awesome]", object.toString());
	}
	
	protected class AwesomeRule extends AbstractOneFieldRule<String>
	{
		public AwesomeRule()
        {
	        super("fieldValue");
        }

		@Override
        protected void validateField(String value, Errors errors)
        {
			assertEquals("awesome", value);
        }

		@Override
        public String getConstraintLabel()
        {
	        return "awesome";
        }

		public String getLabel()
        {
	        return "Awesome";
        }
	}
}
