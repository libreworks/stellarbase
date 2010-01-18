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
package net.libreworks.stellarbase.util;

import java.text.NumberFormat;
import org.springframework.util.NumberUtils;

/**
 * File utilities
 * 
 * @author Jonathan Hawk
 * @version $Id$
 */
public class FileUtil
{
	private static final String GB = " GB";
	private static final String MB = " MB";
	private static final String KB = " KB";
	private static final String B = " B";
	
	/**
	 * Gets the file extension of the filename provided.
	 * 
	 * @param name The file name
	 * @return The file extension
	 */
	public static String getExtension(String name)
	{
		return name == null ? null : name.substring(name.lastIndexOf('.') + 1);
	}
	
	/**
	 * Formats a file size with trailing units and 2 decimal places.
	 * 
	 * @param size The file size
	 * @return The pretty-formatted version
	 */
	public static String getPrettySize(Number size)
	{
		NumberFormat fmt = NumberFormat.getInstance();
		fmt.setMaximumFractionDigits(2);
		fmt.setMinimumFractionDigits(2);
		return getPrettySize(size, fmt);
	}
	
	/**
	 * Formats a file size in decimal format with trailing units.
	 *  
	 * @param size The file size
	 * @param fmt The number format
	 * @return The pretty-formatted version
	 */
	public static String getPrettySize(Number size, NumberFormat fmt)
	{
		double dsize = size == null ?
				0 : NumberUtils.convertNumberToTargetClass(size, Double.class);
		if ( dsize < 1024 ) {
			return fmt.format(dsize).concat(B);
		} else if ( dsize < Math.pow(1024, 2) ) {
			return fmt.format(dsize / 1024).concat(KB);
		} else if ( dsize < Math.pow(1024, 3) ) {
			return fmt.format(dsize / 1024 / 1024).concat(MB);
		} else if ( dsize < Math.pow(1024, 4) ) {
			return fmt.format(dsize / 1024 / 1024 / 1024).concat(GB);
		}
		return null;
	}
}
