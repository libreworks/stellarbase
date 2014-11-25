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

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will execute one of two {@link Runnable}s, depending on the result of a {@link Callable}. 
 * 
 * @author Jonathan Hawk
 * @since 1.0.0
 */
public class ConditionalRunnable implements Runnable {
	private final Callable<Boolean> condition;
	private final Runnable ifTrue;
	private final Runnable ifFalse;
	
	private static final Logger log = LoggerFactory.getLogger(ConditionalRunnable.class);
	
	/**
	 * Creates a new ConditionalRunnable.
	 * 
	 * Any exceptions that {@code condition} may throw are caught and logged. 
	 * 
	 * @param condition The Callable that provides the condition
	 * @param ifTrue The one to call on a {@code true} result
	 * @param ifFalse The one to call on a {@code false} result or if {@code condition} throws an Exception
	 */
	public ConditionalRunnable(Callable<Boolean> condition, Runnable ifTrue, Runnable ifFalse) {
		this.condition = condition;
		this.ifTrue = ifTrue;
		this.ifFalse = ifFalse;
	}

	@Override
	public void run() {
		Boolean result = null;
		try {
			result = condition.call();
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Exception in ConditionalRunnable", e);
			}
		}
		if (Boolean.TRUE.equals(result)) {
			ifTrue.run();
		} else {
			ifFalse.run();
		}
	}
}
