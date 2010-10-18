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
