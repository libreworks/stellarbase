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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.web.multipart.MultipartFile;
import net.libreworks.stellarbase.util.FileUtil;

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
	protected String digest = "MD5";
	protected boolean hashFolders = false;
	
	protected MessageDigest getDigest()
	{
		try {
			return MessageDigest.getInstance(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Unable to create message digest", e);
		}
	}
	
	public String getName(String destination, MultipartFile file)
	{
		String fname = file.getName();
		String extension = FileUtil.getExtension(fname);
		String nameKey = System.currentTimeMillis() + "_" + fname;
		MessageDigest m = getDigest();
		m.update(nameKey.getBytes(), 0, nameKey.length());
		String hash = new BigInteger(1, m.digest()).toString(16);
		if ( hash.length() == 31 ) {
			hash = "0" + hash;
		}
		StringBuilder sb = new StringBuilder(destination)
			.append(File.pathSeparatorChar);
		if ( hashFolders ) {
			sb.append(hash.substring(0, 1)).append(File.pathSeparatorChar)
				.append(hash.substring(0, 2)).append(File.pathSeparatorChar);
		}
		return sb.append(hash).append('.').append(extension).toString();
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
