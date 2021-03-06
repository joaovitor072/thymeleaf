/*
 * =============================================================================
 * 
 *   Copyright (c) 2011, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.thymeleaf.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class NumberUtils {

    
    
    public static String format(final Number target, final Integer minIntegerDigits, final Locale locale) {
        Validate.notNull(target, "Cannot apply format on null");
        Validate.notNull(minIntegerDigits, "Minimum integer digits cannot be null");
        return formatNumber(target, minIntegerDigits, NumberPointType.NONE, Integer.valueOf(0), NumberPointType.NONE, locale);
    }
    
    public static String format(final Number target, final Integer minIntegerDigits, final NumberPointType thousandsPointType, final Locale locale) {
        Validate.notNull(target, "Cannot apply format on null");
        Validate.notNull(minIntegerDigits, "Minimum integer digits cannot be null");
        Validate.notNull(thousandsPointType, "Thousands point type cannot be null");
        return formatNumber(target, minIntegerDigits, thousandsPointType, Integer.valueOf(0), NumberPointType.NONE, locale);
    }
    
    
    
    public static String format(final Number target, final Integer minIntegerDigits, final Integer decimalDigits, final Locale locale) {
        Validate.notNull(target, "Cannot apply format on null");
        Validate.notNull(minIntegerDigits, "Minimum integer digits cannot be null");
        Validate.notNull(decimalDigits, "Decimal digits cannot be null");
        return formatNumber(target, minIntegerDigits, NumberPointType.NONE, decimalDigits, NumberPointType.DEFAULT, locale);
    }

    
    
    public static String format(final Number target, final Integer minIntegerDigits, final Integer decimalDigits, final NumberPointType decimalPointType, final Locale locale) {
        Validate.notNull(target, "Cannot apply format on null");
        Validate.notNull(minIntegerDigits, "Minimum integer digits cannot be null");
        Validate.notNull(decimalDigits, "Decimal digits cannot be null");
        Validate.notNull(decimalPointType, "Decimal point type cannot be null");
        return formatNumber(target, minIntegerDigits, NumberPointType.NONE, decimalDigits, decimalPointType, locale);
    }

    
    
    public static String format(final Number target, final Integer minIntegerDigits, final NumberPointType thousandsPointType, final Integer decimalDigits, final Locale locale) {
        Validate.notNull(target, "Cannot apply format on null");
        Validate.notNull(minIntegerDigits, "Minimum integer digits cannot be null");
        Validate.notNull(thousandsPointType, "Thousands point type cannot be null");
        Validate.notNull(decimalDigits, "Decimal digits cannot be null");
        return formatNumber(target, minIntegerDigits, thousandsPointType, decimalDigits, NumberPointType.DEFAULT, locale);
    }

    
    
    public static String format(final Number target, final Integer minIntegerDigits, final NumberPointType thousandsPointType, final Integer decimalDigits, final NumberPointType decimalPointType, final Locale locale) {
        Validate.notNull(target, "Cannot apply format on null");
        Validate.notNull(minIntegerDigits, "Minimum integer digits cannot be null");
        Validate.notNull(thousandsPointType, "Thousands point type cannot be null");
        Validate.notNull(decimalDigits, "Decimal digits cannot be null");
        Validate.notNull(decimalPointType, "Decimal point type cannot be null");
        return formatNumber(target, minIntegerDigits, thousandsPointType, decimalDigits, decimalPointType, locale);
    }
    

    
    
    
    /**
     * <p>
     *   Produces an array with a sequence of integer numbers.
     * </p>
     * 
     * @param from value to start the sequence from
     * @param to value to produce the sequence to
     * @return the Integer[] sequence
     * 
     * @since 1.1.2
     */
    public static Integer[] sequence(final Integer from, final Integer to) {
        
        Validate.notNull(from, "Value to start the sequence from cannot be null");
        Validate.notNull(to, "Value to generate the sequence up to cannot be null");
        
        final int iFrom = from.intValue();
        final int iTo = to.intValue();
        
        if (iFrom == iTo) {
            return new Integer[] {Integer.valueOf(iFrom)};
        }
        
        final List<Integer> values = new ArrayList<Integer>();
        if (iFrom < iTo) {
            int i = iFrom;
            while (i <= iTo) {
                values.add(Integer.valueOf(i++));
            }
        } else {
            // iFrom > iTo
            int i = iFrom;
            while (i >= iTo) {
                values.add(Integer.valueOf(i--));
            }
        }
        
        return values.toArray(new Integer[values.size()]);
        
    }
    
    
    
    
    
    
    
    
    
    
    private static String formatNumber(
            final Number target, final Integer minIntegerDigits, final NumberPointType thousandsPointType, final Integer fractionDigits, final NumberPointType decimalPointType, final Locale locale) {

        Validate.notNull(target, "Cannot apply format on null");
        Validate.notNull(fractionDigits, "Fraction digits cannot be null");
        Validate.notNull(decimalPointType, "Decimal point type cannot be null");
        Validate.notNull(thousandsPointType, "Thousands point type cannot be null");
        Validate.notNull(locale, "Locale cannot be null");

        DecimalFormat format = null;
        
        if (target instanceof Double || target instanceof Float || target instanceof BigDecimal ||
            target instanceof Integer || target instanceof Long || target instanceof Byte || target instanceof Short || target instanceof BigInteger) {

            format = (DecimalFormat) NumberFormat.getNumberInstance();
            format.setMinimumFractionDigits(fractionDigits.intValue());
            format.setMaximumFractionDigits(fractionDigits.intValue());
            if (minIntegerDigits != null) {
                format.setMinimumIntegerDigits(minIntegerDigits.intValue());
            }
            format.setRoundingMode(RoundingMode.FLOOR);
            format.setDecimalSeparatorAlwaysShown(decimalPointType != NumberPointType.NONE && fractionDigits.intValue() > 0);
            format.setGroupingUsed(thousandsPointType != NumberPointType.NONE);
            format.setDecimalFormatSymbols(new NumberDecimalFormatSymbols(decimalPointType, thousandsPointType, locale));
                
        } else {
            throw new IllegalArgumentException(
                    "Cannot format object of class \"" + target.getClass().getName() + "\" as a decimal number");
        }
        
        return format.format(target);
        
    }
    

    
    
    
    
    
    
    
    
    
    
    private NumberUtils() {
        super();
    }
    
    
    
}
