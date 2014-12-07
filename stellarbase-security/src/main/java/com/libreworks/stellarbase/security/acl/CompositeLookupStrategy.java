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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * A LookupStrategy that internally acts as a Chain-of-responsibility.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class CompositeLookupStrategy implements RecursiveLookupStrategy
{
	private final List<RecursiveLookupStrategy> strategies;

	public CompositeLookupStrategy()
	{
		strategies = new ArrayList<RecursiveLookupStrategy>();
	}
	
	public CompositeLookupStrategy(RecursiveLookupStrategy... strategies)
	{
		this.strategies = new ArrayList<RecursiveLookupStrategy>(ImmutableList.copyOf(strategies));
	}
	
	/**
	 * Specify a list of strategies.
	 * 
	 * @param strategies the strategies
	 */
	public void setStrategies(RecursiveLookupStrategy... strategies)
	{
		this.strategies.clear();
		this.strategies.addAll(ImmutableList.copyOf(strategies));
	}

	/**
	 * Add one or more strategies to the list of delegates.
	 * 
	 * @param strategies the strategies
	 */
	public void addStrategies(RecursiveLookupStrategy... strategies)
	{
		this.strategies.addAll(ImmutableList.copyOf(strategies));
	}
	
	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids, AclService aclService)
	{
		ImmutableMap.Builder<ObjectIdentity,Acl> b = ImmutableMap.builder();
		for (RecursiveLookupStrategy strategy : strategies) {
			b.putAll(strategy.readAclsById(objects, sids, aclService));
		}
		return b.build();
	}
}
