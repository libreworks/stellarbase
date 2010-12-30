package net.libreworks.stellarbase.mail;

import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class StubJavaMailSender implements JavaMailSender
{
	public void send(SimpleMailMessage simpleMessage) throws MailException
	{
	}

	public void send(SimpleMailMessage[] simpleMessages) throws MailException
	{
	}

	public MimeMessage createMimeMessage()
	{
		return null;
	}

	public MimeMessage createMimeMessage(InputStream contentStream) throws MailException
	{
		return null;
	}

	public void send(MimeMessage mimeMessage) throws MailException
	{
	}

	public void send(MimeMessage[] mimeMessages) throws MailException
	{
	}

	public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException
	{
	}

	public void send(MimeMessagePreparator[] mimeMessagePreparators) throws MailException
	{
	}
}
