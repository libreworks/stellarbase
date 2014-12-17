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

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.libreworks.stellarbase.text.Strings;
import com.libreworks.stellarbase.util.Arguments;

/**
 * 
 * @author Jonathan Hawk
 * @version $Id: MailTask.java 50 2010-01-21 03:04:26Z jonathanhawk $
 */
public class MailTask implements Runnable, MimeMessagePreparator
{
	protected JavaMailSender mailSender;
	protected TemplateEngine engine;
	protected MailParams params;
	
	/**
	 * Creates a new MailTask
	 * 
	 * @param mailSender The mailSender
	 * @param engine The template engine
	 * @param params The mail parameters
	 */
	public MailTask(JavaMailSender mailSender, TemplateEngine engine, MailParams params)
	{
		this.mailSender = Arguments.checkNull(mailSender);
		this.engine = Arguments.checkNull(engine);
		this.params = Arguments.checkNull(params);
	}
	
	public void prepare(MimeMessage mimeMessage) throws Exception
    {
		boolean hasMultipart = !Strings.isBlank(params.getTextTemplate()) &&
			!Strings.isBlank(params.getHtmlTemplate());
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, hasMultipart, params.getEncoding());
		final InternetAddress[] EMPTY = new InternetAddress[0];
		helper.setFrom(params.getFrom());
		helper.setBcc(params.getBcc().toArray(EMPTY));
		helper.setTo(params.getTo().toArray(EMPTY));
		helper.setCc(params.getCc().toArray(EMPTY));
		if ( params.getSubject() != null ) {
			helper.setSubject(params.getSubject());
		}
		if ( params.getPriority() > 0 ) {
			helper.setPriority(params.getPriority());
		}
		if ( params.getReplyTo() != null ) {
			helper.setReplyTo(params.getReplyTo());
		}
		if ( hasMultipart ) {
			String text = engine.mergeIntoString(params.getTextTemplate(),
				params.getValues(), params.getEncoding());
			String html = engine.mergeIntoString(params.getHtmlTemplate(),
				params.getValues(), params.getEncoding());
			helper.setText(text, html);
		} else if ( !Strings.isBlank(params.getTextTemplate()) ) {
			String text = engine.mergeIntoString(params.getTextTemplate(),
				params.getValues(), params.getEncoding());
			helper.setText(text);
		} else {
			String html = engine.mergeIntoString(params.getHtmlTemplate(),
				params.getValues(), params.getEncoding());
			helper.setText(html, true);
		}
    }

	public void run()
    {
		mailSender.send(this);
    }
}
