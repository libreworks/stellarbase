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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Test for aclentryvoter
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class EntityAclEntryVoterTest
{
	private EntityAclEntryVoter object;
	
	/**
	 * Sets up the test
	 */
	@Before
	public void setUp()
	{
		AclService aclService = new AbstractAclService()
		{
			public Map<ObjectIdentity,Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) throws NotFoundException
			{
				HashMap<ObjectIdentity,Acl> acls = new HashMap<ObjectIdentity,Acl>();
				for(ObjectIdentity oi : objects) {
					AclImpl acl = new AclImpl(oi, null, sids);
					List<AccessControlEntry> aces = new ArrayList<AccessControlEntry>();
					aces.add(new AccessControlEntryImpl(1, acl, sids.get(0), BasePermission.READ, true, false, false));
					acl.setEntries(aces);
					acls.put(oi, acl);
				}
				return acls;
			}
			public List<ObjectIdentity> findChildren(ObjectIdentity parentIdentity)
			{
				return null;
			}
		};
		object = new EntityAclEntryVoter(aclService, "ACL_READ",
			new Permission[]{BasePermission.READ}, InnerBean.class);
	}

	/**
	 * Tests the supports method
	 */
	@Test
	public void testSupportsClassOfQ()
	{
		assertTrue(object.supports(MethodInvocation.class));
	}

	
	/**
	 * @author Jonathan Hawk
	 * @version $Id$
	 */
	public class InnerBean implements Serializable
	{
        private static final long serialVersionUID = 1L;

		public Serializable getId()
        {
            return 1;
        }
	}
}
