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

import java.util.Arrays;

/**
 * Allows multiple {@link Runnable}s to be executed one after the other.
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class CompositeRunnable implements Runnable {
	private final Runnable[] runnables;
	
	/**
	 * Creates a new CompositeRunnable.
	 * 
	 * @param runnables The Runnable objects to consolidate.
	 */
	public CompositeRunnable(Runnable... runnables){
		this.runnables = Arrays.copyOf(runnables, runnables.length);
	}
	
	@Override
	public void run() {
		for (Runnable r : runnables) {
			r.run();
		}
	}
}
