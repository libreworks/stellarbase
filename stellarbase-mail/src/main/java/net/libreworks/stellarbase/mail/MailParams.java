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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.util.Assert;

/**
 * A simple Data Transfer Object to ease e-mail sending.
 * 
 * @author Jonathan Hawk
 * @version $Id: MailParams.java 56 2010-03-10 00:22:55Z jonathanhawk $
 */
public class MailParams implements Serializable
{
    private static final long serialVersionUID = 1L;
    
	protected InternetAddress from;
	protected InternetAddress replyTo;
	protected Collection<InternetAddress> to = new ArrayList<InternetAddress>();
	protected Collection<InternetAddress> cc = new ArrayList<InternetAddress>();
	protected Collection<InternetAddress> bcc = new ArrayList<InternetAddress>();
	protected String subject;
	protected String encoding;
	protected int priority;
	protected String textTemplate;
	protected String htmlTemplate;
	protected Map<String,Object> values = new HashMap<String,Object>();

	/**
	 * Creates a new MailParams object.
	 * 
	 * @param from The from address
	 */
	public MailParams(InternetAddress from)
	{
		Assert.notNull(from);
		this.from = from;
	}
	
	/**
	 * Creates a new MailParams object.
	 * 
	 * @param from
	 * @throws AddressException if {@code from} cannot be parsed into an address
	 */
	public MailParams(String from) throws AddressException
	{
		this(new InternetAddress(from));
	}
	
	/**
	 * Adds a collection of BCC addresses to the params.
	 * 
	 * @param bcc Addresses as {@link InternetAddress} or Strings
	 * @return provides a fluent interface
	 * @throws AddressException if {@code bcc} cannot be parsed into addresses
	 */
	public MailParams addBcc(Collection<?> bcc) throws AddressException
	{
		for (Object addr : bcc) {
			this.bcc
			    .add(addr instanceof InternetAddress ? (InternetAddress) addr
			        : new InternetAddress(ObjectUtils.toString(addr)));
		}
		return this;
	}

	/**
	 * Adds one or more BCC addresses to the params.
	 * 
	 * @param bcc Addresses
	 * @return provides a fluent interface
	 */
	public MailParams addBcc(InternetAddress... bcc)
	{
		for(InternetAddress addr : bcc) {
			this.bcc.add(addr);
		}
		return this;
	}

	/**
	 * Adds one or more BCC addresses to the params.
	 * 
	 * @param bcc Addresses
	 * @return provides a fluent interface
	 * @throws AddressException if {@code bcc} cannot be parsed into addresses
	 */
	public MailParams addBcc(String... bcc) throws AddressException
	{
		for (String addr : bcc) {
			this.bcc.add(new InternetAddress(addr));
		}
		return this;
	}

	/**
	 * Adds a collection of CC addresses to the params.
	 * 
	 * @param cc Addresses as {@link InternetAddress} or Strings
	 * @return provides a fluent interface
	 * @throws AddressException if {@code cc} cannot be parsed into addresses
	 */
	public MailParams addCc(Collection<?> cc) throws AddressException
	{
		for (Object addr : cc) {
			this.cc
			    .add(addr instanceof InternetAddress ? (InternetAddress) addr
			        : new InternetAddress(ObjectUtils.toString(addr)));
		}
		return this;
	}
	
	/**
	 * Adds one or more CC addresses to the params.
	 * 
	 * @param cc Addresses
	 * @return provides a fluent interface
	 */
	public MailParams addCc(InternetAddress... cc)
	{
		for(InternetAddress addr : cc) {
			this.cc.add(addr);
		}
		return this;
	}
	
	/**
	 * Adds one or more CC addresses to the params.
	 * 
	 * @param cc Addresses
	 * @return provides a fluent interface
	 * @throws AddressException if {@code cc} cannot be parsed into addresses
	 */
	public MailParams addCc(String... cc) throws AddressException
	{
		for (String addr : cc) {
			this.cc.add(new InternetAddress(addr));
		}
		return this;
	}
	
	/**
	 * Adds a collection of to addresses to the params.
	 * 
	 * @param to Addresses as {@link InternetAddress} or Strings
	 * @return provides a fluent interface
	 * @throws AddressException if {@code to} cannot be parsed into addresses
	 */
	public MailParams addTo(Collection<?> to) throws AddressException
	{
		for (Object addr : to) {
			this.to
			    .add(addr instanceof InternetAddress ? (InternetAddress) addr
			        : new InternetAddress(ObjectUtils.toString(addr)));
		}
		return this;
	}

	/**
	 * Adds one or more to addresses to the params.
	 * 
	 * @param to Addresses
	 * @return provides a fluent interface
	 */
	public MailParams addTo(InternetAddress... to)
	{
		for(InternetAddress addr : to) {
			this.to.add(addr);
		}
		return this;
	}
	
	/**
	 * Adds one or more to addresses to the params.
	 * 
	 * @param to Addresses
	 * @return provides a fluent interface
	 * @throws AddressException if {@code to} cannot be parsed into addresses
	 */
	public MailParams addTo(String... to) throws AddressException
	{
		for (String addr : to) {
			this.to.add(new InternetAddress(addr));
		}
		return this;
	}	
	
	/**
	 * @return the bcc
	 */
	public Collection<InternetAddress> getBcc()
	{
		return Collections.unmodifiableCollection(bcc);
	}

	/**
	 * @return the cc
	 */
	public Collection<InternetAddress> getCc()
	{
		return Collections.unmodifiableCollection(cc);
	}

	/**
     * @return the encoding
     */
    public String getEncoding()
    {
    	return encoding;
    }

	/**
	 * @return the from
	 */
	public InternetAddress getFrom()
	{
		return from;
	}

	/**
	 * @return the htmlTemplate
	 */
	public String getHtmlTemplate()
	{
		return htmlTemplate;
	}

	/**
	 * @return the priority
	 */
	public int getPriority()
	{
		return priority;
	}

	/**
	 * @return the replyTo
	 */
	public InternetAddress getReplyTo()
	{
		return replyTo;
	}

	/**
	 * @return the subject
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * @return the textTemplate
	 */
	public String getTextTemplate()
	{
		return textTemplate;
	}

	/**
	 * @return the to
	 */
	public Collection<InternetAddress> getTo()
	{
		return Collections.unmodifiableCollection(to);
	}

	/**
     * @return the values
     */
    public Map<String,?> getValues()
    {
    	return Collections.unmodifiableMap(values);
    }

	/**
     * @param encoding the encoding to set
	 * @return provides a fluent interface
     */
    public MailParams setEncoding(String encoding)
    {
    	this.encoding = encoding;
    	return this;
    }

	/**
	 * @param from the from to set
	 * @return provides a fluent interface
	 */
	public MailParams setFrom(InternetAddress from)
	{
		this.from = from;
		return this;
	}

	/**
	 * @param htmlTemplate the htmlTemplate to set
	 * @return provides a fluent interface
	 */
	public MailParams setHtmlTemplate(String htmlTemplate)
	{
		this.htmlTemplate = htmlTemplate;
		return this;
	}

	/**
	 * @param priority the priority to set
	 * @return provides a fluent interface
	 */
	public MailParams setPriority(int priority)
	{
		this.priority = priority;
		return this;
	}

	/**
	 * @param replyTo the replyTo to set
	 * @return provides a fluent interface
	 */
	public MailParams setReplyTo(InternetAddress replyTo)
	{
		this.replyTo = replyTo;
		return this;
	}

	/**
	 * @param subject the subject to set
	 * @return provides a fluent interface
	 */
	public MailParams setSubject(String subject)
	{
		this.subject = subject;
		return this;
	}

	/**
	 * @param textTemplate the textTemplate to set
	 * @return provides a fluent interface
	 */
	public MailParams setTextTemplate(String textTemplate)
	{
		this.textTemplate = textTemplate;
		return this;
	}

	/**
     * @param values the values to set
	 * @return provides a fluent interface
     */
    public MailParams setValues(Map<String,?> values)
    {
    	this.values.putAll(values);
    	return this;
    }
}
