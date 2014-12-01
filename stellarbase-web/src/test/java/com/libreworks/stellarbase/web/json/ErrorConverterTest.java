package com.libreworks.stellarbase.web.json;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ErrorConverterTest
{
	private ErrorConverter object;
	private MessageSource messageSource;
	
	@Before
	public void setUp()
	{
		object = new ErrorConverter();
		messageSource = new MessageSource()
		{
			public String getMessage(String code, Object[] args, String defaultMessage, Locale locale)
			{
				return null;
			}
			
			public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException
			{
				return null;
			}
			
			public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException
			{
				return "jsonError:" + StringUtils.join(resolvable.getCodes(), ',');
			}
		};
		object.setMessageSource(messageSource);
	}
	
	@Test
	public void testConvertThrowable()
	{
		try { 
			throw new IllegalArgumentException("You're illegal");
		} catch ( IllegalArgumentException e ) {
			Map<String,?> exported = object.convert(e);
			Map<String,?> expected = ImmutableMap.of("error", ImmutableMap.builder()
				.put("message", e.getMessage())
				.put("type", e.getClass().getName())
				.put("line", String.valueOf(e.getStackTrace()[0].getLineNumber()))
				.put("file", e.getStackTrace()[0].getClassName())
				.build());
			assertEquals(expected, exported);
		}
	}

	@Test
	public void testConvertBindException()
	{
		try {
			ExampleBean bean = new ExampleBean();
			BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(bean, "target");
			bindingResult.rejectValue("foo", "bar", new Object[]{"baz"}, "Hmm.");
			bindingResult.reject("bar", new Object[]{"baz"}, "Bad.");
			throw new BindException(bindingResult);
		} catch ( BindException e ) {
			ArrayList<Map<String,?>> jsonErrors = new ArrayList<Map<String,?>>(e.getErrorCount());
			Locale locale = Locale.getDefault();
			List<?> globalErrors = e.getGlobalErrors();
			for(Object o : globalErrors){
				ObjectError err = (ObjectError)o;
				jsonErrors.add(ImmutableMap.of("message", messageSource.getMessage(err, locale)));
			}
			List<?> errors = e.getFieldErrors();
			for(Object o : errors) {
				FieldError err = (FieldError)o;
				jsonErrors.add(ImmutableMap.of("field", err.getField(),
					"message", messageSource.getMessage(err, locale)));
			}
			Map<String,?> expected = ImmutableMap.of("errors", jsonErrors);
			assertEquals(expected, object.convert((Throwable)e));
		}
	}
	
	public class ExampleBean
	{
		private String foo;

		public String getFoo()
		{
			return foo;
		}

		public void setFoo(String foo)
		{
			this.foo = foo;
		}
	}
}
