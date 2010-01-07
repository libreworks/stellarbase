/**
 * 
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
public class RangeRuleTest
{
	private RangeRule object;
	
	/**
	 * Sets up
	 */
	@Before
	public void setUp()
	{
		object = new RangeRule("count", 0.0, 10.0);
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.RangeRule#getLabel()}.
	 */
	@Test
	public void testGetLabel()
	{
		assertEquals("Range", object.getLabel());
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.RangeRule#getConstraintLabel()}.
	 */
	@Test
	public void testGetConstraintLabel()
	{
		assertEquals("0.0\u201310.0", object.getConstraintLabel());
	}

	/**
	 * Test method for {@link net.libreworks.stellarbase.validation.AbstractOneFieldRule#validate(java.lang.Object, org.springframework.validation.Errors)}.
	 */
	@Test
	public void testValidate()
	{
		SimpleBean target = new SimpleBean();
		target.setCount(5);
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "target");
		object.validate(target, errors);
		assertFalse(errors.hasErrors());
	}
	
	/**
	 * Test too small
	 */
	@Test
	public void testValidate2()
	{
		SimpleBean target = new SimpleBean();
		target.setCount(-1);
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "target");
		object.validate(target, errors);
		assertTrue(errors.hasErrors());
	}
}
