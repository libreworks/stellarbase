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

import org.springframework.web.multipart.MultipartFile;

/**
 * Allows customization of the final filename and location given to uploaded files.
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public interface NamingStrategy
{
	/**
	 * Constructs a final destination filename for a file.
	 * 
	 * The filename returned should be an absolute path, usually prepended
	 * by the destination.
	 * 
	 * @param destination The directory into which the file will be moved 
	 * @param file The file uploaded
	 * @return The constructed filename
	 */
	public String getName(String destination, MultipartFile file);
}

