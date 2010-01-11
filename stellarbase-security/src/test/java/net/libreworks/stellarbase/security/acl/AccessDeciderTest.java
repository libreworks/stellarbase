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
package net.libreworks.stellarbase.security.acl;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.libreworks.stellarbase.test.SimpleBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class AccessDeciderTest
{
	private AccessDecider object;
	
	/**
	 * Sets up the test
	 */
	@Before
	public void setUp()
	{
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken("alice", "aoeuhtns");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		SimpleBean bean = new SimpleBean();
		bean.setId((long)123456);
		object = AccessDecider.factory(new StubAclService(), bean);
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
		bean.setId((long)987654);		
		StubAclService aclService = new StubAclService();
		List<ObjectIdentity> ois = new ArrayList<ObjectIdentity>();
		ois.add(new ObjectIdentityImpl(bean));
		List<Sid> sids = new ArrayList<Sid>();
		sids.add(new PrincipalSid("alice"));
		AccessDecider ad = new AccessDecider(aclService.readAclsById(ois, sids).get(ois.get(0)));
		assertTrue(ad.isAllowed(BasePermission.ADMINISTRATION));
	}
	
	/**
	 * Test method for {@link net.libreworks.stellarbase.security.acl.AccessDecider#isAllowed(org.springframework.security.acls.model.Permission[])}.
	 */
	@Test
	public void testIsAllowed()
	{
		assertTrue(object.isAllowed(BasePermission.ADMINISTRATION));
		assertFalse(object.isAllowed(BasePermission.WRITE));
	}
	
	private class StubAclService extends AbstractAclService
	{
		public Map<ObjectIdentity,Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) throws NotFoundException
		{
			HashMap<ObjectIdentity,Acl> acls = new HashMap<ObjectIdentity,Acl>();
			for(ObjectIdentity oi : objects) {
				AclImpl acl = new AclImpl(oi, null, sids);
				List<AccessControlEntry> aces = new ArrayList<AccessControlEntry>();
				aces.add(new AccessControlEntryImpl(1, acl, sids.get(0), BasePermission.ADMINISTRATION, true, false, false));
				acl.setEntries(aces);
				acls.put(oi, acl);
			}
			return acls;
		}
		public List<ObjectIdentity> findChildren(ObjectIdentity parentIdentity)
		{
			return Collections.emptyList();
		}
	}
}
