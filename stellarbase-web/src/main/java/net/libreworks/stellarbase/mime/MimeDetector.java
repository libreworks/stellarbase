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
package net.libreworks.stellarbase.mime;

import java.io.File;
import java.io.InputStream;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

/**
 * Abstracts the libraries used to detect MIME types.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public interface MimeDetector
{
	/**
	 * Gets the most specific MIME type that applies to the file
	 * 
	 * @param file The file
	 * @return The MIME type
	 */
	public MediaType getMimeType(File file);

	/**
	 * Gets the most specific MIME type that applies to the file
	 * 
	 * @param inputStream
	 * @return The MIME type
	 */
	public MediaType getMimeType(InputStream inputStream);
	
	/**
	 * Gets the most specific MIME type that applies to the file
	 * 
	 * @param multipartFile
	 * @return The MIME type
	 */
	public MediaType getMimeType(MultipartFile multipartFile);
	
	/**
	 * Gets the most specific MIME type that applies to the file
	 * 
	 * @param filename The filename
	 * @return The MIME type
	 */
	public MediaType getMimeType(String filename);
}
