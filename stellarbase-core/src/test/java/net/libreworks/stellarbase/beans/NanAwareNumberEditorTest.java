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
package net.libreworks.stellarbase.beans;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Jonathan Hawk
 * @version $Id$
 */
public class NanAwareNumberEditorTest
{
	/**
	 * Test method for {@link net.libreworks.stellarbase.beans.NanAwareNumberEditor#setAsText(java.lang.String)}.
	 */
	@Test
	public void testSetAsTextString()
	{
		NanAwareNumberEditor object = new NanAwareNumberEditor(Integer.class, true);
		object.setAsText("NaN");
		assertNull(object.getValue());
		object.setAsText("123");
		assertEquals(123, object.getValue());
	}
}
