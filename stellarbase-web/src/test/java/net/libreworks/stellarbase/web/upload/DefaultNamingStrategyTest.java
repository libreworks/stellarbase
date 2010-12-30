package net.libreworks.stellarbase.web.upload;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

public class DefaultNamingStrategyTest
{
	private DefaultNamingStrategy object;
	private static final String DESTINATION = "/tmp/files";
	private MultipartFile file;
	
	@Before
	public void setUp() throws Exception
	{
		object = new DefaultNamingStrategy();
		file = new MultipartFile()
		{
			public void transferTo(File dest) throws IOException, IllegalStateException
			{
			}
			
			public boolean isEmpty()
			{
				return false;
			}
			
			public long getSize()
			{
				return 9001; // it's over 9000!!!
			}
			
			public String getOriginalFilename()
			{
				return "exampleFile.txt";
			}
			
			public String getName()
			{
				return "123456789";
			}
			
			public InputStream getInputStream() throws IOException
			{
				return null;
			}
			
			public String getContentType()
			{
				return null;
			}
			
			public byte[] getBytes() throws IOException
			{
				return null;
			}
		};
	}

	@Test
	public void testGetDigest()
	{
		MessageDigest md5 = object.getDigest();
		assertNotNull(md5);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testGetDigestBad()
	{
		object.setDigest("FOO");
		object.getDigest();
	}

	@Test
	public void testDoDigest()
	{
		String expected = "098f6bcd4621d373cade4e832627b4f6";
		assertEquals(expected, object.doDigest("test"));
	}
	
	@Test
	public void testGetName()
	{
		String name = object.getName(DESTINATION, file);
		assertTrue(name.startsWith(DESTINATION + File.separator));
		assertTrue(name.endsWith(".txt"));
		
	}
	@Test
	public void testGetNameHash()
	{
		object.setHashFolders(true);
		String name = object.getName(DESTINATION, file);
		assertTrue(name.startsWith(DESTINATION + File.separator));
		assertTrue(name.endsWith(".txt"));
	}
}
