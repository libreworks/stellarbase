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
package net.libreworks.stellarbase.web.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.libreworks.stellarbase.collections.FluentValues;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

/**
 * Translates errors into something viewable in JSON format.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class ErrorConverter implements MessageSourceAware
{
	private MessageSource messageSource;

	/**
	 * Turns an Exception or Error into the correct JSON error object.
	 * 
	 * <p>The JSON returned will be like the following:</p>
	 * <pre>{
	 * "error": {
	 *     "message": "An exception has occurred because things went wrong.",
	 *     "type": "java.lang.IllegalArgumentException",
	 *     "line": 20,
	 *     "file": "com.example.ClassWhereErrorOccurred"
	 * }
	 * }</pre>
	 * 
	 * @param e The error to convert
	 * @return A Map which can be turned into JSON
	 */
	public Map<String,?> convert(Throwable e)
	{
		if ( e instanceof BindException ) {
			return convert((BindException)e);
		}
		return Collections.singletonMap("error", new FluentValues()
			.set("message", e.getMessage())
			.set("type", e.getClass().getName())
			.set("line", String.valueOf(e.getStackTrace()[0].getLineNumber()))
			.set("file", e.getStackTrace()[0].getClassName()));
	}
	
	/**
	 * Turns a Spring BindException into the correct JSON errors object.
	 * 
	 * <p>The JSON returned will be like the following:</p>
	 * <pre>{
	 * "errors": [
	 *     {
	 *         "field": "username",
	 *         "message": "The username is invalid."
	 *     },
	 *     {
	 *         "field": "password",
	 *         "message": "The password is silly."
	 *     }
	 * ]
	 * }</pre>
	 * 
	 * @param e The error to translate
	 * @return A map that can be turned into JSON
	 */
	public Map<String,?> convert(BindException e)
	{
		List<?> errors = e.getFieldErrors();
		ArrayList<Map<String,?>> jsonErrors = new ArrayList<Map<String,?>>(errors.size());
		Locale locale = Locale.getDefault();
		for(Object o : errors) {
			FieldError err = (FieldError)o;
			jsonErrors.add(new FluentValues()
				.set("field", err.getField())
				.set("message", messageSource.getMessage(err, locale)));
		}
		return Collections.singletonMap("errors", jsonErrors);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.MessageSourceAware#setMessageSource(org.springframework.context.MessageSource)
	 */
	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
}
