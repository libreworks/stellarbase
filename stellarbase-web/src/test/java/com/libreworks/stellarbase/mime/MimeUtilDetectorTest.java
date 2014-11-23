/**
 * 
 */
package com.libreworks.stellarbase.mime;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map.Entry;

import com.libreworks.stellarbase.collections.FluentValues;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class MimeUtilDetectorTest
{
	private MimeUtilDetector object;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		object = new MimeUtilDetector();
		object.afterPropertiesSet();
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.mime.MimeUtilDetector#getMimeType(java.io.File)}.
	 * @throws URISyntaxException 
	 */
	@Test
	public void testGetMimeTypeFile() throws URISyntaxException
	{
		FluentValues types = new FluentValues()
			.set("/mime/LICENSE.html", "text/html")
			.set("/mime/butterfly.svg", "image/svg+xml")
			.set("/mime/book1.xls", "application/excel")
			.set("/mime/test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
			.set("/mime/Example.jpg", "image/jpeg");
			//.set("/mime/automaticTextIndent.odt", "application/vnd.oasis.opendocument.text"); doesn't work on ubuntu 14.04 anymore
		for(Entry<String,Object> entry : types.entrySet()){
			File file = new File(getClass().getResource(entry.getKey()).toURI());
			assertEquals(entry.getValue(), object.getMimeType(file).toString());
		}
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.mime.MimeUtilDetector#getMimeType(java.lang.String)}.
	 */
	@Test
	public void testGetMimeTypeString()
	{
		FluentValues types = new FluentValues()
			.set("/mime/license.html", "text/html")
			.set("/mime/butterfly.svg", "image/svg+xml")
			.set("/mime/book1.xls", "application/excel")
			.set("/mime/test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
			.set("/mime/Example.jpg", "image/jpeg")
			.set("/mime/automaticTextIndent.odt", "application/vnd.oasis.opendocument.text");
		for(Entry<String,Object> entry : types.entrySet()){
			assertEquals(entry.getValue(), object.getMimeType(entry.getKey()).toString());
		}
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.mime.MimeUtilDetector#getMimeType(java.io.InputStream)}.
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testGetMimeTypeInputStream() throws FileNotFoundException
	{
		FluentValues types = new FluentValues()
			.set("/mime/automaticTextIndent.odt", "application/zip") // yeah, that's what it shows up as
			.set("/mime/butterfly.svg", "text/xml")
			.set("/mime/Example.jpg", "image/jpeg")
			.set("/mime/book1.xls", "application/msword"); // different because of byte reading
		for(Entry<String,Object> entry : types.entrySet()){
			InputStream file = getClass().getResourceAsStream(entry.getKey());
			assertEquals(entry.getValue(), object.getMimeType(file).toString());
		}
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.mime.MimeUtilDetector#getMimeType(org.springframework.web.multipart.MultipartFile)}.
	 * @throws URISyntaxException 
	 */
	@Test
	public void testGetMimeTypeMultipartFile() throws URISyntaxException
	{
		FluentValues types = new FluentValues()
			//.set("/mime/license.html", "text/html")
			.set("/mime/book1.xls", "application/excel")
			.set("/mime/Example.jpg", "image/jpeg")
			.set("/mime/butterfly.svg", "image/svg+xml")
			.set("/mime/test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
			.set("/mime/automaticTextIndent.odt", "application/vnd.oasis.opendocument.text");
		for(final Entry<String,Object> entry : types.entrySet()){
			final File file = new File(getClass().getResource(entry.getKey()).toURI());
			MultipartFile mf = new MultipartFile() {
				public void transferTo(File dest) throws IOException, IllegalStateException {
				}
				public boolean isEmpty() {
					return false;
				}
				public long getSize() {
					return file.length();
				}
				public String getOriginalFilename() {
					return file.getName();
				}
				public String getName() {
					return "test";
				}
				public InputStream getInputStream() throws IOException {
					return new FileInputStream(file);
				}
				public String getContentType() {
					return entry.getValue().toString();
				}
				public byte[] getBytes() throws IOException {
					byte[] fileBArray = new byte[(int)file.length()];
					FileInputStream fis = new FileInputStream(file);
					fis.read(fileBArray);
					return fileBArray;
				}
			};
			assertEquals(entry.getValue(), object.getMimeType(mf).toString());
		}
	}
}
