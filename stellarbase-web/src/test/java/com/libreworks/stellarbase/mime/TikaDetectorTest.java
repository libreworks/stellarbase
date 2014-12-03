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
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class TikaDetectorTest
{
	private TikaDetector object;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		object = new TikaDetector();
		object.afterPropertiesSet();
	}

	/**
	 * Test method for {@link com.libreworks.stellarbase.mime.MimeUtilDetector#getMimeType(java.io.File)}.
	 * @throws URISyntaxException 
	 */
	@Test
	public void testGetMimeTypeFile() throws URISyntaxException
	{
		Map<String,String> types = ImmutableMap.<String,String>builder()
			.put("/mime/LICENSE.html", "text/html")
			.put("/mime/butterfly.svg", "image/svg+xml")
			.put("/mime/book1.xls", "application/vnd.ms-excel")
			.put("/mime/test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
			.put("/mime/Example.jpg", "image/jpeg")
			.put("/mime/ODF_text_reference_v3.odt", "application/vnd.oasis.opendocument.text")
			.build();
		for(Entry<String,String> entry : types.entrySet()){
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
		Map<String,String> types = ImmutableMap.<String,String>builder()
			.put("/mime/license.html", "text/html")
			.put("/mime/butterfly.svg", "image/svg+xml")
			.put("/mime/book1.xls", "application/vnd.ms-excel")
			.put("/mime/test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
			.put("/mime/Example.jpg", "image/jpeg")
			.put("/mime/ODF_text_reference_v3.odt", "application/vnd.oasis.opendocument.text")
			.build();
		for(Entry<String,String> entry : types.entrySet()){
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
		Map<String,String> types = ImmutableMap.<String,String>builder()
			.put("/mime/ODF_text_reference_v3.odt", "application/vnd.oasis.opendocument.text")
			.put("/mime/butterfly.svg", "image/svg+xml")
			.put("/mime/Example.jpg", "image/jpeg")
			.put("/mime/book1.xls", "application/vnd.ms-excel")
			.build();
		for(Entry<String,String> entry : types.entrySet()){
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
		Map<String,String> types = ImmutableMap.<String,String>builder()
			.put("/mime/LICENSE.html", "text/html")
			.put("/mime/book1.xls", "application/vnd.ms-excel")
			.put("/mime/Example.jpg", "image/jpeg")
			.put("/mime/butterfly.svg", "image/svg+xml")
			.put("/mime/test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
			.put("/mime/ODF_text_reference_v3.odt", "application/vnd.oasis.opendocument.text")
			.build();
		for(final Entry<String,String> entry : types.entrySet()){
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
