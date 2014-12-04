/**
 * Copyright 2014 LibreWorks contributors
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

import java.io.Serializable;

import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

/**
 * A "denying" immutable {@link org.springframework.security.acls.model.AccessControlEntry}.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class DenyAccessControlEntry extends AbstractAccessControlEntry
{
	private static final long serialVersionUID = 1L;

	DenyAccessControlEntry(Acl acl, Sid sid, Permission permission)
	{
		super(acl, sid, permission);
	}

	public Serializable getId()
	{
		return new StringBuilder(getSid().toString()).append(" cannot ")
			.append(getPermission().getPattern()).append(" on ")
			.append(getAcl().getObjectIdentity().toString());
	}

	public boolean isGranting()
	{
		return false;
	}
}