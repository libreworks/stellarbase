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

import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import com.google.common.base.Objects;
import com.libreworks.stellarbase.util.Arguments;

/**
 * Abstract super class for immutable AccessControlEntry rules.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractAccessControlEntry implements AccessControlEntry
{
	final private Acl acl;
	final private Sid sid;
	final private Permission permission;
	
	protected AbstractAccessControlEntry(Acl acl, Sid sid, Permission permission)
	{
		this.acl = Arguments.checkNull(acl);
		this.sid = Arguments.checkNull(sid);
		this.permission = Arguments.checkNull(permission);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		} else if (obj instanceof AbstractAccessControlEntry) {
			AbstractAccessControlEntry other = (AbstractAccessControlEntry)obj;
			return isGranting() == other.isGranting() &&
				Objects.equal(sid, other.sid) &&
				Objects.equal(permission, other.permission) &&
				Objects.equal(acl.getObjectIdentity(),
					other.getAcl() == null ? null : other.getAcl().getObjectIdentity());
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(Boolean.valueOf(isGranting()), sid, permission, acl.getObjectIdentity());
	}
	
	public Acl getAcl()
	{
		return acl;
	}

	public Permission getPermission()
	{
		return permission;
	}

	public Sid getSid()
	{
		return sid;
	}
	
	public String toString()
	{
		return getId().toString();
	}
}
