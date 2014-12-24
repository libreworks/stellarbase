package com.libreworks.stellarbase.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TrimmerTest
{
	// as specified by unicode standard
	static final String WHITESPACE = "\u2002\u3000\r\u0085\u200A\u2005\u2000\u3000"
	      + "\u2029\u000B\u3000\u2008\u2003\u205F\u3000\u1680"
	      + "\u0009\u0020\u2006\u2001\u202F\u00A0\u000C\u2009"
	      + "\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000";

    @Test
	public void testNormal()
	{
    	Trimmer object = Trimmer.WHITESPACE;
    	assertEquals("", object.trim(null));
    	assertEquals("", object.trim(""));
    	assertEquals("", object.trim(WHITESPACE));
    	assertEquals("ao eu", object.trim(WHITESPACE + "ao eu" + WHITESPACE));
	}
    
    @Test
    public void testReturnNull()
    {
    	Trimmer object = Trimmer.WHITESPACE.toNull();
    	assertNull(object.trim(null));
    	assertNull(object.trim(""));
    	assertNull(object.trim(WHITESPACE));
    	assertEquals("ao eu", object.trim(WHITESPACE + "ao eu" + WHITESPACE));
    }
	
    @Test
    public void testLeading()
    {
    	Trimmer object = Trimmer.WHITESPACE.leading();
    	assertEquals("", object.trim(WHITESPACE));
    	assertEquals("ao eu".concat(WHITESPACE), object.trim(WHITESPACE + "ao eu" + WHITESPACE));
    }
	
    @Test
    public void testTrailing()
    {
    	Trimmer object = Trimmer.WHITESPACE.trailing();
    	assertEquals("", object.trim(WHITESPACE));
    	assertEquals(WHITESPACE.concat("ao eu"), object.trim(WHITESPACE + "ao eu" + WHITESPACE));
    }
    
	@Test
	public void testCharacters()
	{
		Trimmer object = Trimmer.on(".0").trailing();
        assertEquals("12",object.trim("120.00"));
        assertEquals("121",object.trim("121.00"));
	}
	
	@Test
	public void testEmptyCharacters()
	{
    	Trimmer object = Trimmer.on("");
    	assertEquals("", object.trim(null));
    	assertEquals("", object.trim(""));
    	assertEquals(WHITESPACE, object.trim(WHITESPACE));
    	assertEquals(WHITESPACE + "ao eu" + WHITESPACE, object.trim(WHITESPACE + "ao eu" + WHITESPACE));
	}
}
