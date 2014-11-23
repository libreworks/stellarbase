/**
 * Copyright 2011 LibreWorks contributors
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
package com.libreworks.stellarbase.web;

import javax.servlet.http.HttpServletResponse;

/**
 * A base class for controllers.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public abstract class ControllerSupport
{
	private static final String HEADER_EXPIRES = "Expires";
	private static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
	private static final String HEADER_CACHE_CONTROL = "Cache-Control";
	private static final String CACHE_CONTROL_PRIVATE = "private, must-revalidate, max-age=0, pre-check=0";

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
		setCachePrivate(response);
	}
	
	/**
	 * Sends a private "Cache-Control" header.
	 * 
	 * Specifically, this sends "private, must-revalidate, max-age=0,
	 * pre-check=0".
	 * 
	 * @param response The HTTP response
	 */
	protected void setCachePrivate(HttpServletResponse response)
	{
		response.setHeader(HEADER_CACHE_CONTROL, CACHE_CONTROL_PRIVATE);
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
