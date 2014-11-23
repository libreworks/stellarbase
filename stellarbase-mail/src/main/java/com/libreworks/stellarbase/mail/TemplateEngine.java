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
package com.libreworks.stellarbase.mail;

import java.util.Map;
import org.springframework.mail.MailPreparationException;

/**
 * A front-end to a templating engine (usually Velocity or FreeMarker).
 * 
 * @author Jonathan Hawk
 * @version $Id: TemplateEngine.java 50 2010-01-21 03:04:26Z jonathanhawk $
 */
public interface TemplateEngine
{
	/**
	 * Merge the given template with the given model into a String.
	 * 
	 * Class implementors should wrap Exceptions in MailPreparationException.
	 * 
	 * @param name the name or location of template
	 * @param values the Map that contains model names as keys and model objects as values
	 * @return the result as String
	 * @throws MailPreparationException if the template wasn't found or rendering failed
	 */
	public String mergeIntoString(String name, Map<String,?> values) throws MailPreparationException;
	
	/**
	 * Merge the given template with the given model into a String.
	 * 
	 * Class implementors should wrap Exceptions in MailPreparationException.
	 * 
	 * @param name the name or location of template
	 * @param values the Map that contains model names as keys and model objects as values
	 * @param encoding the encoding of the template file
	 * @return the result as String
	 * @throws MailPreparationException if the template wasn't found or rendering failed
	 */
	public String mergeIntoString(String name, Map<String,?> values, String encoding) throws MailPreparationException;
}
