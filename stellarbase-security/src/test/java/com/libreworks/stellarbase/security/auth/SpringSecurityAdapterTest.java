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
package com.libreworks.stellarbase.security.auth;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class SpringSecurityAdapterTest
{
	/**
	 * Test method for {@link com.libreworks.stellarbase.security.auth.SpringSecurityAdapter#getPrincipal()}.
	 */
	@Test
	public void testGetPrincipal()
	{
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken("alice", "aoeuhtns");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		SpringSecurityAdapter object = new SpringSecurityAdapter();
		assertSame(authentication, object.getPrincipal());
		SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContextHolder.clearContext();
	}
}
