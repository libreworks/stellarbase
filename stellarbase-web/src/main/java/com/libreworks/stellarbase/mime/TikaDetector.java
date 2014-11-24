/**
 * Copyright 2014 LibreWorks contributors
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

/**
 * Detects MIME Types with Apache Tika.
 * 
 * The developer can supply a preconfigured Tika {@link Detector}. If not
 * supplied, the {@link DefaultDetector} is used with the MIME types provided by
 * {@link MimeTypes#getDefaultMimeTypes}.
 * 
 * @author Jonathan Hawk
 */
public class TikaDetector implements MimeDetector, InitializingBean {
	final Logger logger = LoggerFactory.getLogger(getClass());

	private Detector detector;
	
	private Detector tikaDetector;

	@Override
	public void afterPropertiesSet() throws Exception {
		detector = tikaDetector != null ? tikaDetector :
			new DefaultDetector(MimeTypes.getDefaultMimeTypes());
	}
	
	@Override
	public MediaType getMimeType(File file) {
		TikaInputStream tikaIS = null;
		try {
			tikaIS = TikaInputStream.get(file);
		} catch (FileNotFoundException e) {
		}
        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
        return convert(tikaIS, metadata);
	}

	@Override
	public MediaType getMimeType(InputStream inputStream) {
		TikaInputStream tikaIS = TikaInputStream.get(inputStream);
        Metadata metadata = new Metadata();
        return convert(tikaIS, metadata);
	}

	@Override
	public MediaType getMimeType(MultipartFile multipartFile) {
		TikaInputStream tikaIS = null;
		try {
			tikaIS = TikaInputStream.get(multipartFile.getInputStream());
		} catch (IOException e) {
		}
        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, multipartFile.getOriginalFilename());
        metadata.set(Metadata.CONTENT_TYPE, multipartFile.getContentType());
        return convert(tikaIS, metadata);
	}

	@Override
	public MediaType getMimeType(String filename) {
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, filename);
		return convert(null, metadata);
	}

	/**
	 * Allows the developer to supply a preconfigured Tika {@link Detector}.
	 * 
	 * @param tikaDetector the Detector to set
	 */
	public void setTikaDetector(Detector tikaDetector) {
		this.tikaDetector = tikaDetector;
	}
	
	/**
	 * Detects a Tika MediaType as a Spring MediaType.
	 * 
	 * @param mediaTypes The MediaTypes
	 * @return The Spring MediaType
	 */
	protected MediaType convert(TikaInputStream tis, Metadata metadata)
	{
		MediaType mediaType = null;
		try {
			org.apache.tika.mime.MediaType mt = detector.detect(tis, metadata);
			mediaType = new MediaType(mt.getType(), mt.getSubtype(), mt.getParameters());
		} catch (IOException e) {
			// do nothing, really
		} finally {
	        if (tis != null) {
	            try {
					tis.close();
				} catch (IOException e) {
				}
	        }
	    }
		return mediaType;
	}	
}
