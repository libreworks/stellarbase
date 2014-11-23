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
package com.libreworks.stellarbase.mail;

import static org.junit.Assert.*;

import java.util.Map;

import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.libreworks.stellarbase.collections.FluentValues;

import org.junit.Test;
import org.springframework.mail.MailPreparationException;

public class MailTaskTest
{
	@Test(expected=IllegalArgumentException.class)
	public void testNull1()
	{
		new MailTask(null, null, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNull2()
	{
		new MailTask(new StubJavaMailSender(), null, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNull3()
	{
		new MailTask(new StubJavaMailSender(), new StubTemplateEngine(), null);
	}
	
	@Test
	public void testPrepare() throws Exception
	{
		MailParams mp = new MailParams(new InternetAddress("foo1@example.com"))
			.addBcc(new InternetAddress("bcc1@example.com"), new InternetAddress("bcc2@example.com"))
			.addCc(new InternetAddress("cc1@example.com"), new InternetAddress("cc2@example.com"))
			.addTo(new InternetAddress("to1@example.com"), new InternetAddress("to2@example.com"))
			.setPriority(5)
			.setEncoding("UTF-8")
			.setReplyTo(new InternetAddress("reply1@example.com"))
			.setSubject("The subject")
			.setTextTemplate("aoeu")
			.setHtmlTemplate("htns")
			.setValues(new FluentValues()
				.set("foo", "bar")
				.set("up", "down")
				.set("yes", "no"));
		MailTask object = new MailTask(new StubJavaMailSender(), new StubTemplateEngine(), mp);
		MimeMessage mm = new MimeMessage((Session)null);
		object.prepare(mm);
		assertEquals("5", mm.getHeader("X-Priority", ""));
		assertEquals("The subject", mm.getSubject());
		assertArrayEquals(new InternetAddress[]{new InternetAddress("reply1@example.com")}, mm.getReplyTo());
		assertArrayEquals(new InternetAddress[]{new InternetAddress("foo1@example.com")}, mm.getFrom());
		assertArrayEquals(new InternetAddress[]{new InternetAddress("to1@example.com"), new InternetAddress("to2@example.com")},
				mm.getRecipients(RecipientType.TO));
		assertArrayEquals(new InternetAddress[]{new InternetAddress("cc1@example.com"), new InternetAddress("cc2@example.com")},
				mm.getRecipients(RecipientType.CC));
		assertArrayEquals(new InternetAddress[]{new InternetAddress("bcc1@example.com"), new InternetAddress("bcc2@example.com")},
				mm.getRecipients(RecipientType.BCC));
	}
	
	public class StubTemplateEngine implements TemplateEngine
	{
		public String mergeIntoString(String name, Map<String,?> values, String encoding) throws MailPreparationException
		{
			return name + "," + values;
		}
		
		public String mergeIntoString(String name, Map<String,?> values) throws MailPreparationException
		{
			return name + "," + values;
		}
	}
}
