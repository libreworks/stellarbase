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
package com.libreworks.stellarbase.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.Callable;

import org.junit.Test;

import com.libreworks.stellarbase.text.Strings;

public class ConditionalRunnableTest
{
	@Test
	public void testTrue()
	{
		CalledRunnable r1 = new CalledRunnable();
		CalledRunnable r2 = new CalledRunnable();
		ConditionalRunnable object = new ConditionalRunnable(new Conditional(true), r1, r2);
		assertFalse(r1.called);
		assertFalse(r2.called);
		object.run();
		assertTrue(r1.called);
		assertFalse(r2.called);
	}

	@Test
	public void testFalse()
	{
		CalledRunnable r1 = new CalledRunnable();
		CalledRunnable r2 = new CalledRunnable();
		ConditionalRunnable object = new ConditionalRunnable(new Conditional(false), r1, r2);
		assertFalse(r1.called);
		assertFalse(r2.called);
		object.run();
		assertTrue(r2.called);
		assertFalse(r1.called);
	}
	
	@Test()
	public void testException()
	{
		CalledRunnable r1 = new CalledRunnable();
		CalledRunnable r2 = new CalledRunnable();
		ConditionalRunnable object = new ConditionalRunnable(new Conditional(null), r1, r2);
		assertFalse(r1.called);
		assertFalse(r2.called);
		object.run();
		assertFalse(r1.called);
		assertTrue(r2.called);
	}

	private class Conditional implements Callable<Boolean>
	{
		private Boolean result;
		
		Conditional(Boolean result)
		{
			this.result = result;
		}
		
		public Boolean call() throws Exception
		{
			if (result == null) {
				throw new Exception(Strings.POO);
			}
			return result;
		}
	}
	
	private class CalledRunnable implements Runnable
	{
		boolean called = false;
		
		public void run()
		{
			called = true;
		}
	}	
}
