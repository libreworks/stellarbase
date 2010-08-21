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

import static org.junit.Assert.*;

import java.text.DecimalFormat;

import org.junit.Test;

public class FileUtilTest
{
	@Test
	public void testGetExtension()
	{
		assertEquals("gz", FileUtil.getExtension("thing.tar.gz"));
		assertEquals("txt", FileUtil.getExtension("what.txt"));
		assertNull(FileUtil.getExtension(null));
		assertEquals("everything", FileUtil.getExtension("everything"));
	}

	@Test
	public void testGetPrettySizeNumber()
	{
		assertEquals("1.20 MB", FileUtil.getPrettySize(1024 * 1024 * 1.2));
		assertEquals("1.30 KB", FileUtil.getPrettySize(1024 * 1.3));
		assertEquals("657.00 B", FileUtil.getPrettySize(657));
		assertEquals("1.25 GB", FileUtil.getPrettySize(1024 * 1024 * 1024 * 1.25));
	}

	@Test
	public void testGetPrettySizeNumberNumberFormat()
	{
		assertEquals("1.6 MB", FileUtil.getPrettySize(1024 * 1024 * 1.6, new DecimalFormat("0.#")));
	}
}
