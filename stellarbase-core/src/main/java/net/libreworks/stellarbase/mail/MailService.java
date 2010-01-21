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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.Assert;

/**
 * A service for sending email to which you pass a MailParams object. A task
 * executor can be chosen to allow e-mails to be sent asynchronously
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class MailService implements InitializingBean
{
	protected JavaMailSender mailSender;
	protected TemplateEngine templateEngine;
	protected TaskExecutor taskExecutor;

	public void afterPropertiesSet() throws Exception
    {
		Assert.notNull(mailSender, "mailSender must not be null");
		Assert.notNull(templateEngine, "templateEngine must not be null");
		Assert.notNull(taskExecutor, "taskExecutor must not be null");
    }

	/**
	 * Sends an email using the parameters laid out in {@code params}.
	 * 
	 * The email may or may not be sent asynchronously depending on the
	 * implementation of the {@code taskExecutor}.
	 * 
	 * @param params The mail parameters
	 */
	public void send(MailParams params)
	{
		taskExecutor.execute(getMailTask(params));
	}
	
	/**
	 * Gets the mail task to be executed.
	 * 
	 * @param params The mail parameters
	 * @return The runnable mail task
	 */
	protected Runnable getMailTask(MailParams params)
	{
		return new MailTask(mailSender, templateEngine, params);
	}
	
	/**
	 * Sets the JavaMailSender implementation to be used by this service.
	 * 
	 * @param mailSender the mailSender to set
	 */
	public void setMailSender(JavaMailSender mailSender)
	{
		this.mailSender = mailSender;
	}

	/**
	 * Sets the TaskExecutor to use for email sending.
	 * 
	 * @param taskExecutor the taskExecutor to set
	 */
	public void setTaskExecutor(TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}

	/**
	 * Sets the TemplateEngine to be used for template rendering.
	 * 
	 * @param templateEngine the templateEngine to set
	 */
	public void setTemplateEngine(TemplateEngine templateEngine)
	{
		this.templateEngine = templateEngine;
	}
}
