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
package com.libreworks.stellarbase.mime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil2;

/**
 * A MimeDetector that uses mime-util.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class MimeUtilDetector implements MimeDetector, InitializingBean
{
	final Logger logger = LoggerFactory.getLogger(getClass());
	protected MimeUtil2 detector = new MimeUtil2();

	/**
	 * Sets up the class to work
	 */
	public void afterPropertiesSet() throws Exception
	{
		detector.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector");
		detector.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
		detector.registerMimeDetector("eu.medsea.mimeutil.detector.WindowsRegistryMimeDetector");
		if ( new File("/usr/share/mime/mime.cache").exists() ) {
			detector.registerMimeDetector("eu.medsea.mimeutil.detector.OpendesktopMimeDetector");
		}
	}
	
	@SuppressWarnings("unchecked")
	public MediaType getMimeType(File file)
	{
		Collection<MimeType> mimeTypes = detector.getMimeTypes(file);
		logger.debug("MimeType for File: " + file.getName());
		return convert(mimeTypes);
	}

	@SuppressWarnings("unchecked")
	public MediaType getMimeType(String filename)
	{
		Collection<MimeType> mimeTypes = detector.getMimeTypes(filename);
		logger.debug("MimeType for String: " + filename);
		return convert(mimeTypes);
	}

	@SuppressWarnings("unchecked")
	public MediaType getMimeType(InputStream inputStream)
	{
		Collection<MimeType> mimeTypes = detector.getMimeTypes(inputStream);
		logger.debug("InputStream");
		return convert(mimeTypes);
	}

	@SuppressWarnings("unchecked")
	public MediaType getMimeType(MultipartFile multipartFile)
	{
		logger.debug("MimeType for MultipartFile: " + multipartFile.getOriginalFilename());
		Collection<MimeType> types = detector.getMimeTypes(multipartFile.getOriginalFilename());
		if ( multipartFile.getContentType() != null ) {
			types.add(new MimeType(multipartFile.getContentType()));
		}
		try {
			byte[] bytes = multipartFile.getBytes();
			types.addAll(detector.getMimeTypes(bytes));
		} catch (IOException e) {
			// just try the filename
		}
		return convert(types);
	}
	
	/**
	 * Converts a MimeUtil MimeType into a Spring MediaType.
	 * 
	 * @param mimeTypes The MimeUtil MimeTypes
	 * @return The Spring MediaType
	 */
	protected MediaType convert(Collection<MimeType> mimeTypes)
	{
		if(logger.isDebugEnabled()){
			System.out.println("=====");
			for(MimeType type : mimeTypes) {
				System.out.println(type.toString() + " (" + type.getSpecificity() + ")");
				logger.debug(type.toString() + " (" + type.getSpecificity() + ")");
			}			
		}
		MimeType from = MimeUtil2.getMostSpecificMimeType(mimeTypes);
		return new MediaType(from.getMediaType(), from.getSubType());
	}
}
