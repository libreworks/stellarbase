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
package net.libreworks.stellarbase.web;

import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.libreworks.stellarbase.collections.FluentValues;
import net.libreworks.stellarbase.security.auth.PrincipalAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 * A base class for controllers.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public abstract class ControllerSupport
{
	@Autowired
	protected PrincipalAdapter principalAdapter;
	private static final String HEADER_EXPIRES = "Expires";
	private static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
	
	/**
	 * Returns a new ModelAndView with some common values and no view name.
	 * 
	 * @see ControllerSupport#getCommonModelAttributes()
	 * @return The ModelAndView
	 */
	protected ModelAndView createModelAndView()
	{
		return createModelAndView(null);
	}

	/**
	 * Returns a new ModelAndView with some common values.
	 * 
	 * @param name The view name
	 * @return The ModelAndView
	 */
	protected ModelAndView createModelAndView(String name)
	{
		return new ModelAndView(name).addAllObjects(getCommonModelAttributes());
	}

	/**
	 * Gets some common values that are put in every view from
	 * {@link #createModelAndView()}.
	 * 
	 * Override this method to add the same attributes to all methods in a
	 * single controller, but <em>be sure to mix-in those returned by
	 * {@code super.getCommonModelAttributes()}!</em>.
	 * 
	 * @return The common attributes
	 */
	protected Map<String,?> getCommonModelAttributes()
	{
		return new FluentValues().set("principal", principalAdapter
		    .getPrincipal());
	}

	/**
	 * Gets the currently authenticated Principal. Note that the {@code
	 * principalAdapter} property must be set.
	 * 
	 * @return The currently authenticated Principal
	 */
	protected Principal getPrincipal()
	{
		return principalAdapter == null ? null : principalAdapter
		    .getPrincipal();
	}

	/**
	 * Sends the {@code Content-Disposition: attachment; filename="x"} header.
	 *
	 * MSIE has a problem downloading files over SSL sent in this manner if 
	 * certain cache headers have been sent, so we set the headers that will get
	 * around this. 
	 * 
	 * @param filename The filename
	 * @param response The HTTP response
	 */
	protected void setDispositionAttachment(String filename, HttpServletResponse response)
	{
		response.setHeader(HEADER_CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"");
		response.setIntHeader(HEADER_EXPIRES, 0);
		response.setHeader("Cache-Control", "private");
	}
	
	/**
	 * Sets the "Expires" HTTP Header
	 * 
	 * @param seconds The number of seconds in the future to set the header
	 * @param response The HTTP response
	 */
	protected void setExpires(int seconds, HttpServletResponse response)
	{
		response.setDateHeader(HEADER_EXPIRES, System.currentTimeMillis()
		    + (seconds * 1000));
	}
}
