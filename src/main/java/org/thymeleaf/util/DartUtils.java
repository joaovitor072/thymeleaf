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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1.2
 *
 */
public final class DartUtils {





    public static String escapeString(final String str) {
        final StringBuilder output = new StringBuilder();
        escapeString(output, str);
        return output.toString();
    }




    public static String print(final Object object) {
        final StringBuilder output = new StringBuilder();
        print(output, object);
        return output.toString();
    }


    public static String printString(final String str) {
        final StringBuilder output = new StringBuilder();
        if (str == null) {
            printNull(output);
        } else {
            printString(output, str);
        }
        return output.toString();
    }

    public static String printNumber(final Number number) {
        final StringBuilder output = new StringBuilder();
        if (number == null) {
            printNull(output);
        } else {
            printNumber(output, number);
        }
        return output.toString();
    }

    public static String printBoolean(final Boolean bool) {
        final StringBuilder output = new StringBuilder();
        if (bool == null) {
            printNull(output);
        } else {
            printBoolean(output, bool);
        }
        return output.toString();
    }

    public static String printCollection(final Collection<?> collection) {
        final StringBuilder output = new StringBuilder();
        if (collection == null) {
            printNull(output);
        } else {
            printCollection(output, collection);
        }
        return output.toString();
    }

    public static String printArray(final Object array) {
        final StringBuilder output = new StringBuilder();
        if (array == null) {
            printNull(output);
        } else {
            printArray(output, array);
        }
        return output.toString();
    }


    public static String printMap(final Map<?,?> map) {
        final StringBuilder output = new StringBuilder();
        if (map == null) {
            printNull(output);
        } else {
            printMap(output, map);
        }
        return output.toString();
    }


    public static String printObject(final Object object) {
        final StringBuilder output = new StringBuilder();
        if (object == null) {
            printNull(output);
        } else {
            printObject(output, object);
        }
        return output.toString();
    }







    private static void print(final StringBuilder output, final Object object) {
        if (object == null) {
            printNull(output);
            return;
        }
        if (object instanceof String) {
            printString(output, (String) object);
            return;
        }
        if (object instanceof Number) {
            printNumber(output, (Number) object);
            return;
        }
        if (object instanceof Boolean) {
            printBoolean(output, (Boolean) object);
            return;
        }
        if (object.getClass().isArray()) {
            printArray(output, object);
            return;
        }
        if (object instanceof Collection<?>) {
            printCollection(output, (Collection<?>) object);
            return;
        }
        if (object instanceof Map<?,?>) {
            printMap(output, (Map<?,?>) object);
            return;
        }
        if (object.getClass().isEnum()) {
            printEnum(output, object);
            return;
        }
        printObject(output, object);
    }


    private static void printNull(final StringBuilder output) {
        output.append("null");
    }


    private static void printString(final StringBuilder output, final String str) {
        output.append('\'');
        escapeString(output, str);
        output.append('\'');
    }


    private static void printNumber(final StringBuilder output, final Number number) {
        output.append(number.toString());
    }


    private static void printBoolean(final StringBuilder output, final Boolean bool) {
        output.append(bool.toString());
    }


    private static void printArray(final StringBuilder output, final Object arrayObj) {
        output.append('[');
        if (arrayObj instanceof Object[]) {
            final Object[] array = (Object[]) arrayObj;
            boolean first = true;
            for (final Object element: array) {
                if (first) {
                    first = false;
                } else {
                    output.append(',');
                }
                print(output, element);
            }
        } else if (arrayObj instanceof boolean[]) {
            final boolean[] array = (boolean[]) arrayObj;
            boolean first = true;
            for (final boolean element: array) {
                if (first) {
                    first = false;
                } else {
                    output.append(',');
                }
                print(output, Boolean.valueOf(element));
            }
        } else if (arrayObj instanceof byte[]) {
            final byte[] array = (byte[]) arrayObj;
            boolean first = true;
            for (final byte element: array) {
                if (first) {
                    first = false;
                } else {
                    output.append(',');
                }
                print(output, Byte.valueOf(element));
            }
        } else if (arrayObj instanceof short[]) {
            final short[] array = (short[]) arrayObj;
            boolean first = true;
            for (final short element: array) {
                if (first) {
                    first = false;
                } else {
                    output.append(',');
                }
                print(output, Short.valueOf(element));
            }
        } else if (arrayObj instanceof int[]) {
            final int[] array = (int[]) arrayObj;
            boolean first = true;
            for (final int element: array) {
                if (first) {
                    first = false;
                } else {
                    output.append(',');
                }
                print(output, Integer.valueOf(element));
            }
        } else if (arrayObj instanceof long[]) {
            final long[] array = (long[]) arrayObj;
            boolean first = true;
            for (final long element: array) {
                if (first) {
                    first = false;
                } else {
                    output.append(',');
                }
                print(output, Long.valueOf(element));
            }
        } else if (arrayObj instanceof float[]) {
            final float[] array = (float[]) arrayObj;
            boolean first = true;
            for (final float element: array) {
                if (first) {
                    first = false;
                } else {
                    output.append(',');
                }
                print(output, Float.valueOf(element));
            }
        } else if (arrayObj instanceof double[]) {
            final double[] array = (double[]) arrayObj;
            boolean first = true;
            for (final double element: array) {
                if (first) {
                    first = false;
                } else {
                    output.append(',');
                }
                print(output, Double.valueOf(element));
            }
        } else {
            throw new IllegalArgumentException("Cannot print \"" + arrayObj + "\" of class " + arrayObj.getClass().getName() + " as an array");
        }
        output.append(']');
    }


    private static void printCollection(final StringBuilder output, final Collection<?> collection) {
        output.append('[');
        boolean first = true;
        for (final Object element: collection) {
            if (first) {
                first = false;
            } else {
                output.append(',');
            }
            print(output, element);
        }
        output.append(']');
    }


    private static void printMap(final StringBuilder output, final Map<?,?> map) {
        output.append('{');
        boolean first = true;
        for (final Map.Entry<?,?> entry: map.entrySet()) {
            if (first) {
                first = false;
            } else {
                output.append(',');
            }
            printKeyValue(output, entry.getKey(), entry.getValue());
        }
        output.append('}');
    }


    private static void printKeyValue(final StringBuilder output, final Object key, final Object value) {
        print(output, key);
        output.append(':');
        print(output, value);
    }


    private static void printObject(final StringBuilder output, final Object object) {
        try {
            final Map<String,Object> properties = new LinkedHashMap<String, Object>();
            final PropertyDescriptor[] descriptors =
                Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors();
            for (final PropertyDescriptor descriptor : descriptors) {
                final Method readMethod =  descriptor.getReadMethod();
                if (readMethod != null) {
                    final String name = descriptor.getName();
                    if (!name.toLowerCase().equals("class")) {
                        final Object value = readMethod.invoke(object);
                        properties.put(name, value);
                    }
                }
            }
            printMap(output, properties);
        } catch (final IllegalAccessException e) {
            throw new IllegalArgumentException("Could not perform introspection on object of class " + object.getClass().getName(), e);
        } catch (final InvocationTargetException e) {
            throw new IllegalArgumentException("Could not perform introspection on object of class " + object.getClass().getName(), e);
        } catch (final IntrospectionException e) {
            throw new IllegalArgumentException("Could not perform introspection on object of class " + object.getClass().getName(), e);
        }
    }


    
    private static void printEnum(final StringBuilder output, final Object object) {
        
        final Enum<?> enumObject = (Enum<?>) object;
        final Class<?> enumClass = object.getClass();
        
		final Map<String,Object> properties = new LinkedHashMap<String, Object>();
		properties.put("$type", enumClass.getSimpleName());
		properties.put("$name", enumObject.name());

		printMap(output, properties);
        
    }







    private static void escapeString(final StringBuilder output, final String str) {

        if (str == null) {
            return;
        }


        final int length = str.length();
        for (int i = 0; i < length; i++) {

            char c = str.charAt(i);

            if (c > 0xfff) {
                output.append("\\u" + Integer.toHexString(c));
            } else if (c > 0xff) {
                output.append("\\u0" + Integer.toHexString(c));
            } else if (c > 0x7f) {
                output.append("\\u00" + Integer.toHexString(c));
            } else if (c < 32) {
                switch (c) {
                    case '\n':
                        output.append("\\n");
                        break;
                    case '\r':
                        output.append("\\r");
                        break;
                    case '\t':
                        output.append("\\t");
                        break;
                    case '\b':
                        output.append("\\b");
                        break;
                    case '\f':
                        output.append("\\f");
                        break;
                    default :
                        if (c > 0xf) {
                            output.append("\\u00" + Integer.toHexString(c));
                        } else {
                            output.append("\\u000" + Integer.toHexString(c));
                        }
                }
            } else {
                switch (c) {
                    case '\'':
                        output.append("\\'");
                        break;
                    case '"':
                        output.append("\\\"");
                        break;
                    case '\\':
                        output.append("\\\\");
                        break;
                    default :
                        output.append(c);
                }
            }
        }

    }




    private DartUtils() {
        super();
    }



}
