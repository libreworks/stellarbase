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
package com.libreworks.stellarbase.web.upload;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.web.multipart.MultipartFile;

import com.libreworks.stellarbase.text.Characters;
import com.libreworks.stellarbase.text.Strings;

/**
 * The default naming strategy for uploads.
 * 
 * A filename is prepended by the current time in milliseconds, then the whole
 * thing is hashed using a specified MessageDigest. This hash is what is used to
 * represent the original file on the filesystem.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class DefaultNamingStrategy implements NamingStrategy
{
	public static final String DEFAULT_DIGEST = "MD5";
	protected String digest = DEFAULT_DIGEST;
	protected boolean hashFolders = false;
	
	protected MessageDigest getDigest()
	{
		try {
			return MessageDigest.getInstance(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Unable to create message digest", e);
		}
	}
	
	protected String doDigest(String in)
	{
		MessageDigest m = getDigest();
		m.update(in.getBytes(), 0, in.length());
		String hash = new BigInteger(1, m.digest()).toString(16);
		// MD5 in Java leaves out a leading zero. Add it, when applicable.
		if ( DEFAULT_DIGEST.equals(digest) && hash.length() == 31 ) {
			hash = Strings.ZERO + hash;
		}
		return hash;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.libreworks.stellarbase.web.upload.NamingStrategy#getName(java.lang.String, org.springframework.web.multipart.MultipartFile)
	 */
	public String getName(String destination, MultipartFile file)
	{
		String fname = file.getOriginalFilename();
		String extension = Strings.getExtension(fname);
		String hash = doDigest(System.currentTimeMillis() + Strings.UNDERSCORE + fname);
		StringBuilder sb = new StringBuilder(destination)
			.append(File.separatorChar);
		if ( hashFolders ) {
			sb.append(hash.substring(0, 1)).append(File.separatorChar)
				.append(hash.substring(0, 2)).append(File.separatorChar);
		}
		return sb.append(hash).append(Characters.DOT).append(extension).toString();
	}

	/**
	 * Sets the digest to be used to hash the filename ("MD5" by default).
	 * 
	 * @param digest The message digest name
	 */
	public void setDigest(String digest)
	{
		this.digest = digest;
	}
	
	/**
	 * Whether folders will be created for the first two characters of the hash.
	 * 
	 * For instance, suppose a filename is hashed to 01df72h... The folders
	 * would be 0\01\01df72h...
	 *  
	 * @param flag Whether to hash folders
	 */
	public void setHashFolders(boolean flag)
	{
		this.hashFolders = flag;
	}
}
