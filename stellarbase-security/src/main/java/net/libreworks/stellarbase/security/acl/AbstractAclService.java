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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

/**
 * Base ACL Service just to make implementing easier.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public abstract class AbstractAclService implements AclService
{
	public Acl readAclById(ObjectIdentity object) throws NotFoundException
	{
		return readAclById(object, null);
	}

	public Acl readAclById(ObjectIdentity object, List<Sid> sids) throws NotFoundException
	{
		Map<ObjectIdentity,Acl> map = readAclsById(Arrays.asList(object), sids);
		return map.get(object);
	}

	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects) throws NotFoundException
	{
		return readAclsById(objects, null);
	}
}
