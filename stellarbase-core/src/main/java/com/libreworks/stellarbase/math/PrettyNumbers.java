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
package com.libreworks.stellarbase.math;

import java.util.Currency;
import java.util.Locale;

/**
 * Just a static utility class for the {@link Multiplier} classes.
 */
public class PrettyNumbers
{
	private PrettyNumbers() {}
	
	/**
	 * Gets a base-2 Multiplier for a measure of bytes (e.g. "1.23 KiB", "4.57 MiB", "987 B").
	 * 
	 * @param size The amount of bytes, null will be treated as ZERO
	 * @return The Multiplier
	 */
	public static BinaryMultiplier bytes(Number size)
	{
		return BinaryMultiplier.create(size, "B", null);
	}

	/**
	 * Gets a base-2 Multiplier for any number (e.g. "1.23 Ki", "4.57 Mi", "987").
	 * 
	 * @param value The value, null will be treated as ZERO
	 * @return The Multiplier
	 * @see {@link BinaryMultiplier#from(Number)}
	 */
	public static BinaryMultiplier binary(Number value)
	{
		return BinaryMultiplier.from(value);
	}
	
	/**
	 * Gets a base-10 Multiplier for any number (e.g. "123.25", "2.78 M", "8.32 B").
	 * 
	 * @param value The value, null will be treated as ZERO
	 * @return The Multiplier
	 * @see {@link DecimalMultiplier#from(Number)}
	 */
	public static DecimalMultiplier decimal(Number value)
	{
		return DecimalMultiplier.from(value);
	}
	
	/**
	 * Gets a base-10 Multiplier for an amount of currency (e.g. "$123.45", "$2.78 M", "$8.32 B").
	 * 
	 * <p>The default locale will be used for currency symbol display format.
	 * 
	 * @param money The value, null will be treated as ZERO
	 * @return The Multiplier
	 */
	public static DecimalMultiplier currency(Number money)
	{
		return currency(money, null); 
	}
	
	/**
	 * Gets a base-10 Multiplier for an amount of currency (e.g. "$123.45", "$2.78 M", "$8.32 B").
	 * 
	 * <p>The {@code locale}'s currency will be used for currency symbol display
	 * format. If {@code locale} is null, the default locale will be used. 
	 * 
	 * @param money The value, null will be treated as ZERO
	 * @param locale The locale using and displaying the currency
	 * @return The Multiplier
	 */	
	public static DecimalMultiplier currency(Number money, Locale locale)
	{
		Locale l = locale == null ? Locale.getDefault() : locale;
		return currency(money, l, Currency.getInstance(l));
	}
	
	/**
	 * Gets a base-10 Multiplier for an amount of currency (e.g. "$123.45", "$2.78 M", "$8.32 B").
	 * 
	 * <p>If {@code locale} is null, the default locale will be used. If
	 * {@code currency} is null, the currency for {@code locale} will be used. 
	 *  
	 * @param money The value, null will be treated as ZERO
	 * @param locale The locale displaying the currency
	 * @param currency The currency type 
	 * @return The Multiplier
	 */
	public static DecimalMultiplier currency(Number money, Locale locale, Currency currency)
	{
		Locale l = locale == null ? Locale.getDefault() : locale;
		Currency c = currency == null ? Currency.getInstance(l) : currency;
		return DecimalMultiplier.create(money, null, c.getSymbol(l));
	}
}
