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
package net.libreworks.stellarbase.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.libreworks.stellarbase.web.json.ErrorConverter;
import net.libreworks.stellarbase.web.json.JsonWrappedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;
import org.springframework.web.util.WebUtils;

/**
 * A pretty good default handler for Exceptions.
 * 
 * <p>By default, this handler will:</p>
 * <ul><li>Forward forbidden errors ({@link SecurityException} and {@link AccessDeniedException}) to the forbidden view (by default "_forbidden"), and set status code to 403 "Forbidden".</li>
 * <li>Set {@link BindException} to have a response of 422 "Unprocessable Entity"</li>
 * <li>Forward all other errors to the error view (by default "_error") with a response code of 500 "Internal Server Error"</li>
 * <li>Forward errors that are requested by XMLHttpRequest to a special ajax error view (by default "_ajaxError")</li>
 * <li>Turn {@link JsonWrappedException}s into a JSON object using the ErrorConverter and MappingJacksonJsonView properties</li></ul>
 * 
 * It also translates exceptions into JSON where appropriate (sort of). 
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class CustomExceptionResolver implements HandlerExceptionResolver
{
	private static final String DEFAULT_ERROR_VIEW_NAME = "_error";
	private static final String DEFAULT_FORBIDDEN_VIEW_NAME = "_forbidden";
	private static final String DEFAULT_AJAX_ERROR_VIEW_NAME = "_ajaxError";
	private MappingJacksonJsonView view;
	private ErrorConverter errorConverter;
	private String errorViewName = DEFAULT_ERROR_VIEW_NAME;
	private String forbiddenViewName = DEFAULT_FORBIDDEN_VIEW_NAME;
	private String ajaxErrorViewName = DEFAULT_AJAX_ERROR_VIEW_NAME;
	
	private static final String X_REQUESTED_WITH = "X-Requested-With";
	private static final String XMLHTTPREQUEST = "XMLHttpRequest";
	private static final Logger log = LoggerFactory.getLogger(CustomExceptionResolver.class);
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
	{
		int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		ModelAndView mav = new ModelAndView();
		if (ex instanceof JsonWrappedException) {
			Throwable inner = ex.getCause();
			if (inner instanceof BindException) {
				statusCode = 422;
			} else if (inner instanceof SecurityException
					|| inner instanceof AccessDeniedException) {
				statusCode = HttpServletResponse.SC_FORBIDDEN;
			}
			mav.addAllObjects(errorConverter.convert(inner)).setView(view);
		} else {
			String viewName = errorViewName;
			if (ex instanceof BindException) {
				statusCode = 422;
			} else if (ex instanceof SecurityException
					|| ex instanceof AccessDeniedException) {
				statusCode = HttpServletResponse.SC_FORBIDDEN;
				viewName = forbiddenViewName;
			}
			if (XMLHTTPREQUEST.equals(request.getHeader(X_REQUESTED_WITH))) {
				viewName = ajaxErrorViewName;
			}
			mav.addObject("exception", ex).setViewName(viewName);
		}
		if (statusCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
			log.error("Exception occurred during request", ex);
		}
		response.setStatus(statusCode);
		request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, new Integer(
				statusCode));
		return mav;
	}

	/**
	 * @param ajaxErrorViewName the ajaxErrorViewName to set
	 */
	public void setAjaxErrorViewName(String ajaxErrorViewName)
	{
		this.ajaxErrorViewName = ajaxErrorViewName;
	}

	/**
	 * @param errorConverter the errorConverter to set
	 */
	public void setErrorConverter(ErrorConverter errorConverter)
	{
		this.errorConverter = errorConverter;
	}

	/**
	 * @param errorViewName the errorViewName to set
	 */
	public void setErrorViewName(String errorViewName)
	{
		this.errorViewName = errorViewName;
	}

	/**
	 * @param forbiddenViewName the forbiddenViewName to set
	 */
	public void setForbiddenViewName(String forbiddenViewName)
	{
		this.forbiddenViewName = forbiddenViewName;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(MappingJacksonJsonView view)
	{
		this.view = view;
	}
}
