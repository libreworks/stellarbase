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
package net.libreworks.stellarbase.mail;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.MailPreparationException;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;

/**
 * A simple front-end to Velocity for e-mail content.
 * 
 * @author Jonathan Hawk
 * @version $Id: VelocityTemplateEngine.java 50 2010-01-21 03:04:26Z jonathanhawk $
 */
public class VelocityTemplateEngine implements TemplateEngine, InitializingBean
{
	protected VelocityEngine engine;
	
	public void afterPropertiesSet() throws Exception
    {
		Assert.notNull(engine, "VelocityEngine must not be null");
    }
	
	/* (non-Javadoc)
	 * @see net.libreworks.stellarbase.mail.TemplateEngine#mergeIntoString(java.lang.String, java.util.Map)
	 */
	@Deprecated
	public String mergeIntoString(String name, Map<String,?> values) throws MailPreparationException
	{
		try {
			return VelocityEngineUtils.mergeTemplateIntoString(engine, name, (Map<String, Object>) values);
		} catch ( VelocityException e ) {
			throw new MailPreparationException(e);
		}
	}

	/* (non-Javadoc)
	 * @see net.libreworks.stellarbase.mail.TemplateEngine#mergeIntoString(java.lang.String, java.util.Map, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public String mergeIntoString(String name, Map<String,?> values, String encoding) throws MailPreparationException
	{
		try {
			return VelocityEngineUtils.mergeTemplateIntoString(engine, name, encoding, (Map<String,Object>)values);
		} catch ( VelocityException e ) {
			throw new MailPreparationException(e);
		}
	}

	/**
	 * @param engine The VelocityEngine to set 
	 */
	public void setVelocityEngine(VelocityEngine engine)
	{
		this.engine = engine;
	}
}
