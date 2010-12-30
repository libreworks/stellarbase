package net.libreworks.stellarbase.web.json;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.libreworks.stellarbase.collections.FluentValues;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

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
			Map<String,?> expected = Collections.singletonMap("error", new FluentValues()
				.set("message", e.getMessage())
				.set("type", e.getClass().getName())
				.set("line", String.valueOf(e.getStackTrace()[0].getLineNumber()))
				.set("file", e.getStackTrace()[0].getClassName()));
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
			throw new BindException(bindingResult);
		} catch ( BindException e ) {
			List<?> errors = e.getFieldErrors();
			ArrayList<Map<String,?>> jsonErrors = new ArrayList<Map<String,?>>(errors.size());
			Locale locale = Locale.getDefault();
			for(Object o : errors) {
				FieldError err = (FieldError)o;
				jsonErrors.add(new FluentValues()
					.set("field", err.getField())
					.set("message", messageSource.getMessage(err, locale)));
			}
			Map<String,?> expected = Collections.singletonMap("errors", jsonErrors);
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
