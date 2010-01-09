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

import java.util.HashSet;
import java.util.List;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.UnloadedSidException;

/**
 * A simple implementation of ACL.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class AclImpl implements Acl
{
	private static final long serialVersionUID = 1L;

	private ObjectIdentity oi;
	private List<AccessControlEntry> aces;
	private Acl parent;
	final private HashSet<Sid> loadedSids = new HashSet<Sid>();
	
	/**
	 * Creates a new ACL.
	 * 
	 * @param oi the objectidentity
	 */
	public AclImpl(ObjectIdentity oi)
	{
		this(oi, null, null);
	}
	
	/**
	 * Creates a new ACL.
	 * 
	 * @param oi the objectidentity
	 * @param parent the parent ACL
	 */
	public AclImpl(ObjectIdentity oi, Acl parent)
	{
		this(oi, parent, null);
	}
	
	/**
	 * Creates a new ACL.
	 * 
	 * @param oi the objectidentity
	 * @param parent the parent ACL 
	 * @param sids the Sids
	 */
	public AclImpl(ObjectIdentity oi, Acl parent, List<Sid> sids)
	{
		this.oi = oi;
		this.parent = parent;
		if ( sids != null ) {
			loadedSids.addAll(sids);
		}
	}
	
	public List<AccessControlEntry> getEntries()
	{
		return aces;
	}

	public ObjectIdentity getObjectIdentity()
	{
		return oi;
	}

	public Sid getOwner()
	{
		return null;
	}

	public Acl getParentAcl()
	{
		return parent;
	}

	public boolean isEntriesInheriting()
	{
		return parent != null;
	}

	public boolean isGranted(List<Permission> permission, List<Sid> sids, boolean adminMode) throws NotFoundException, UnloadedSidException
	{
		if ( !isSidLoaded(sids) ) {
			throw new UnloadedSidException("ACL was not loaded for one or more Sids");
		}
		
		AccessControlEntry firstRejection = null;
		
		for(Permission perm : permission) {
			for(Sid sid : sids) {
				boolean scanNextSid = true;
				for(AccessControlEntry ace : aces) {
					if ( ace.getPermission().getMask() == perm.getMask() &&
							ace.getSid().equals(sid) ) {
						// found a matching ACE
						if ( ace.isGranting() ) {
							return true;
						} else {
							// failure for this permission, so stop search
							// we will see if they have a different permission
							// (this permission is 100% rejected for this sid)
							if ( firstRejection != null ) {
								firstRejection = ace;
							}
							scanNextSid = false;
							break;
						}
					}
				}
				if ( !scanNextSid ) {
					break;
				}
			}
		}
		
		if ( firstRejection != null ) {
			return false;
		}
		
		if ( isEntriesInheriting() && parent != null ) {
			return parent.isGranted(permission, sids, false);
		} else {
			throw new NotFoundException("Unable to locate a matching ACE for passed permissions and Sids");
		}
	}

	public boolean isSidLoaded(List<Sid> sids)
	{
		if (loadedSids == null || loadedSids.isEmpty()) {
			return false;
		}
		if (sids == null || sids.isEmpty()) {
			return true;
		}
		
		for(Sid sid : sids){
			boolean found = false;
			for(Sid loaded : loadedSids){
				if (sid.equals(loaded)) {
					found = true;
					break;
				}
			}
			if ( !found ) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Sets the access control entries
	 * 
	 * @param aces The aces to set
	 */
	public void setEntries(List<AccessControlEntry> aces)
	{
		this.aces = aces;
		for(AccessControlEntry ace : aces){
			loadedSids.add(ace.getSid());
		}
	}
}
