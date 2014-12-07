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
package com.libreworks.stellarbase.security.acl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class AccessDeciderTest
{
	private Authentication authentication;
	
	/**
	 * Sets up the test
	 */
	@Before
	public void setUp()
	{
		authentication = new UsernamePasswordAuthenticationToken("alice", "aoeuhtns");
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	/**
	 * Tears down the test
	 */
	@After
	public void tearDown()
	{
		SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContextHolder.clearContext();
	}
	
	/**
	 * Tests the simple constructor
	 */
	@Test
	public void testBasicConstructor()
	{
		SimpleBean bean = new SimpleBean();
		bean.setId(987654L);
		AccessDecider object = new AccessDecider(new AclPermissionEvaluator(new StubAclService()), bean, authentication);
		assertTrue(object.isAllowed(BasePermission.ADMINISTRATION));
		assertFalse(object.isAllowed(BasePermission.WRITE));
	}

	/**
	 * Tests usage with an objectidentity
	 */
	@Test
	public void testObjIdConstructor()
	{
		SimpleBean bean = new SimpleBean();
		bean.setId(987654L);
		AccessDecider object = new AccessDecider(new AclPermissionEvaluator(new StubAclService()), new ObjectIdentityImpl(bean));
		assertTrue(object.isAllowed(BasePermission.ADMINISTRATION));
		assertFalse(object.isAllowed(BasePermission.WRITE));
	}	
	
	protected class SimpleBean
	{
		private Long id;

		/**
         * @return the id
         */
        public Long getId()
        {
        	return id;
        }

		/**
         * @param id the id to set
         */
        public void setId(Long id)
        {
        	this.id = id;
        }
	}
	
	private class StubAclService extends AbstractAclService
	{
		public Map<ObjectIdentity,Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) throws NotFoundException
		{
			HashMap<ObjectIdentity,Acl> acls = new HashMap<ObjectIdentity,Acl>();
			for(ObjectIdentity oi : objects) {
				acls.put(oi, AccessControlList.builder(oi, null, sids)
					.allow(sids.get(0), BasePermission.ADMINISTRATION)
					.build());
			}
			return acls;
		}
	}
}
