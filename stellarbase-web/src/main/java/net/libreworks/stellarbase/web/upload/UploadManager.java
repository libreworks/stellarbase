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
package net.libreworks.stellarbase.web.upload;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.libreworks.stellarbase.mime.MimeDetector;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.multipart.MultipartFile;

/**
 * Transfers uploads to a target directory and restricts based on MIME type.
 * 
 * This class requires a {@link MimeDetector} either assigned or in the bean
 * factory which instantiated this one. 
 * 
 * It also uses a {@link NamingStrategy}, so if one is not provided, the default
 * is {@link DefaultNamingStrategy}.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class UploadManager implements BeanFactoryAware, InitializingBean
{
	private BeanFactory beanFactory;
	private NamingStrategy namingStrategy;
	private String destination;
	private MimeDetector mimeDetector;
	private HashSet<String> types = new HashSet<String>();
	
	public void afterPropertiesSet() throws Exception
	{
		if ( mimeDetector == null ) {
			beanFactory.getBean(MimeDetector.class);
		}
	}

	/**
	 * Gets the destination directory into which files will be uploaded
	 * @return
	 */
	public String getDestination()
	{
		return destination;
	}

	/**
	 * @return the mimeDetector
	 */
	public MimeDetector getMimeDetector()
	{
		return mimeDetector;
	}
	
	/**
	 * @return the namingStrategy
	 */
	public NamingStrategy getNamingStrategy()
	{
		if ( namingStrategy == null ) {
			namingStrategy = new DefaultNamingStrategy();
		}
		return namingStrategy;
	}
	
	/**
	 * Gets the MIME types allowed by this uploader
	 * 
	 * @return The MIME types
	 */
	public Set<String> getTypes()
	{
		return Collections.unmodifiableSet(types);
	}
	
	public void setBeanFactory(BeanFactory arg0) throws BeansException
	{
		this.beanFactory = arg0;
	}
	
	/**
	 * Sets the destination directory into which files will be transferred.
	 * 
	 * @param destination
	 */
	public void setDestination(String destination)
	{
		File dest = new File(destination);
		if ( !dest.exists() || !dest.canWrite() ) {
			throw new IllegalArgumentException("Target directory does not exist or is not writable");
		}
		this.destination = destination;
	}

	/**
	 * @param mimeDetector the mimeDetector to set
	 */
	public void setMimeDetector(MimeDetector mimeDetector)
	{
		this.mimeDetector = mimeDetector;
	}

	/**
	 * @param namingStrategy the namingStrategy to set
	 */
	public void setNamingStrategy(NamingStrategy namingStrategy)
	{
		this.namingStrategy = namingStrategy;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(Collection<String> types)
	{
		this.types.addAll(types);
	}

	/**
	 * Uploads a file to the destination directory (rejecting invalid files).
	 * 
	 * This method uses the default MIME types to determine if a file is valid.
	 *  
	 * @param file The file to upload
	 * @return The file uploaded and validated
	 * @throws IOException If the file cannot be moved to the destination
	 * @throws IllegalArgumentException if the MIME type is not acceptable
	 */
	public UploadedFile upload(MultipartFile file) throws IOException
	{
		return upload(file, types);
	}
	
	/**
	 * Uploads a file to the destination directory (rejecting invalid files).
	 * 
	 * This method skips the default MIME types and uses the specific ones you
	 * provide. If {@code mimeTypes} is null or empty, all MIME types will be
	 * accepted.
	 * 
	 * @param file The file to upload
	 * @param mimeTypes The acceptable MIME types (can be null or empty for all types)
	 * @return The file uploaded and validated
	 * @throws IOException If the file cannot be moved to the destination
	 * @throws IllegalArgumentException if the MIME type is not acceptable
	 */
	public UploadedFile upload(MultipartFile file, Collection<String> mimeTypes) throws IOException
	{
		String mimeType = getMimeDetector().getMimeType(file);
		if ( mimeTypes != null && !mimeTypes.isEmpty() && !mimeTypes.contains(mimeType) ) {
			throw new IllegalArgumentException("MIME type not allowed: " + mimeType);
		}
		File dest = new File(getNamingStrategy().getName(destination, file));
		file.transferTo(dest);
		return new UploadedFile(dest, mimeType);
	}
}
