/*
 * Copyright (c) 2012-2014 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * Copyright (c) 2014-2015 Specialisterren
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.util;

import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.util.exceptions.InputConvertException;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TypeConvertor {

    private Primatives primatives = Primatives.getDefaultInstance();
    private final Map<Tupel<Class<?>>, ConvertingMethod> methods = new HashMap<>(128);

    private interface ConvertingMethod {

        Object doConvert(Object input);
    }

    @SuppressWarnings("cast")
    public TypeConvertor() {
        methods.put(new Tupel<>(Byte.class, Byte.class),           (Object input) -> { return input; });
        methods.put(new Tupel<>(Byte.class, Short.class),          (Object input) -> { return (byte) (short) input; });
        methods.put(new Tupel<>(Byte.class, Integer.class),        (Object input) -> { return (byte) (int) input; });
        methods.put(new Tupel<>(Byte.class, Long.class),           (Object input) -> { return (byte) (long) input; });
        methods.put(new Tupel<>(Byte.class, Float.class),          (Object input) -> { return (byte) (float) input; });
        methods.put(new Tupel<>(Byte.class, Double.class),         (Object input) -> { return (byte) (double) input; });
        methods.put(new Tupel<>(Byte.class, Character.class),      (Object input) -> { return Byte.parseByte(input.toString()); });
        methods.put(new Tupel<>(Byte.class, String.class),         (Object input) -> { return Byte.parseByte((String) input); });

        methods.put(new Tupel<>(Short.class, Byte.class),          (Object input) -> { return (short) (byte) input; });
        methods.put(new Tupel<>(Short.class, Short.class),         (Object input) -> { return input; });
        methods.put(new Tupel<>(Short.class, Integer.class),       (Object input) -> { return (short) (int) input; });
        methods.put(new Tupel<>(Short.class, Long.class),          (Object input) -> { return (short) (long) input; });
        methods.put(new Tupel<>(Short.class, Float.class),         (Object input) -> { return (short) (float) input; });
        methods.put(new Tupel<>(Short.class, Double.class),        (Object input) -> { return (short) (double) input; });
        methods.put(new Tupel<>(Short.class, Character.class),     (Object input) -> { return Short.parseShort(input.toString()); });
        methods.put(new Tupel<>(Short.class, String.class),        (Object input) -> { return Short.parseShort((String)input); });

        methods.put(new Tupel<>(Integer.class, Byte.class),        (Object input) -> { return (int) (byte) input; });
        methods.put(new Tupel<>(Integer.class, Short.class),       (Object input) -> { return (int) (short) input; });
        methods.put(new Tupel<>(Integer.class, Integer.class),     (Object input) -> { return input; });
        methods.put(new Tupel<>(Integer.class, Long.class),        (Object input) -> { return (int) (long) input; });
        methods.put(new Tupel<>(Integer.class, Float.class),       (Object input) -> { return (int) (float) input; });
        methods.put(new Tupel<>(Integer.class, Double.class),      (Object input) -> { return (int) (double) input; });
        methods.put(new Tupel<>(Integer.class, Character.class),   (Object input) -> { return Integer.parseInt(input.toString()); });
        methods.put(new Tupel<>(Integer.class, String.class),      (Object input) -> { return Integer.parseInt((String)input); });

        methods.put(new Tupel<>(Long.class, Byte.class),           (Object input) -> { return (long) (byte) input; });
        methods.put(new Tupel<>(Long.class, Short.class),          (Object input) -> { return (long) (short) input; });
        methods.put(new Tupel<>(Long.class, Integer.class),        (Object input) -> { return (long) (int) input; });
        methods.put(new Tupel<>(Long.class, Long.class),           (Object input) -> { return input; });
        methods.put(new Tupel<>(Long.class, Float.class),          (Object input) -> { return (long) (float) input; });
        methods.put(new Tupel<>(Long.class, Double.class),         (Object input) -> { return (long) (double) input; });
        methods.put(new Tupel<>(Long.class, Character.class),      (Object input) -> { return Long.parseLong(input.toString()); });
        methods.put(new Tupel<>(Long.class, String.class),         (Object input) -> { return Long.parseLong((String)input); });

        methods.put(new Tupel<>(Float.class, Byte.class),          (Object input) -> { return (float) Math.floor((byte) input); });
        methods.put(new Tupel<>(Float.class, Short.class),         (Object input) -> { return (float) Math.floor((short) input); });
        methods.put(new Tupel<>(Float.class, Integer.class),       (Object input) -> { return (float) Math.floor((int) input); });
        methods.put(new Tupel<>(Float.class, Long.class),          (Object input) -> { return (float) Math.floor((long) input); });
        methods.put(new Tupel<>(Float.class, Float.class),         (Object input) -> { return input; });
        methods.put(new Tupel<>(Float.class, Double.class),        (Object input) -> { return (float) (double) input; });
        methods.put(new Tupel<>(Float.class, Character.class),     (Object input) -> { return (float) Float.parseFloat(input.toString()); });
        methods.put(new Tupel<>(Float.class, String.class),        (Object input) -> { return (float) Float.parseFloat((String) input); });

        methods.put(new Tupel<>(Double.class, Byte.class),         (Object input) -> { return Math.floor((byte) input); });
        methods.put(new Tupel<>(Double.class, Short.class),        (Object input) -> { return Math.floor((short) input); });
        methods.put(new Tupel<>(Double.class, Integer.class),      (Object input) -> { return Math.floor((int) input); });
        methods.put(new Tupel<>(Double.class, Long.class),         (Object input) -> { return Math.floor((long) input); });
        methods.put(new Tupel<>(Double.class, Float.class),        (Object input) -> { return (double) (float) input; });
        methods.put(new Tupel<>(Double.class, Double.class),       (Object input) -> { return input; });
        methods.put(new Tupel<>(Double.class, Character.class),    (Object input) -> { return (double) Double.parseDouble(input.toString()); });
        methods.put(new Tupel<>(Double.class, String.class),       (Object input) -> { return (double) Double.parseDouble((String) input); });

        methods.put(new Tupel<>(Boolean.class, Boolean.class),     (Object input) -> { return input; });
        methods.put(new Tupel<>(Boolean.class, Character.class),   (Object input) -> { return ((Character) input) != 0; });
        methods.put(new Tupel<>(Boolean.class, String.class),      (Object input) -> { return Boolean.parseBoolean((String)input); });

        methods.put(new Tupel<>(Character.class, Character.class), (Object input) -> { return input; });
        methods.put(new Tupel<>(Character.class, String.class),    (Object input) -> {
            String str = (String) input;
            if (str.length() == 1)
                return str.charAt(0);
            throw new InputConvertException();
        });

        methods.put(new Tupel<>(String.class, Byte.class),         (Object input) -> { return Byte.toString((byte) input); });
        methods.put(new Tupel<>(String.class, Short.class),        (Object input) -> { return Short.toString((short) input); });
        methods.put(new Tupel<>(String.class, Integer.class),      (Object input) -> { return Integer.toString((int) input); });
        methods.put(new Tupel<>(String.class, Long.class),         (Object input) -> { return Long.toString((long) input); });
        methods.put(new Tupel<>(String.class, Float.class),        (Object input) -> { return Float.toString((float) input); });
        methods.put(new Tupel<>(String.class, Double.class),       (Object input) -> { return Double.toString((double) input); });
        methods.put(new Tupel<>(String.class, Boolean.class),      (Object input) -> { return Boolean.toString((boolean) input); });
        methods.put(new Tupel<>(String.class, Character.class),    (Object input) -> { return Character.toString((char) input); });
        methods.put(new Tupel<>(String.class, String.class),       (Object input) -> { return input; });
    }

    public boolean isConvertable(Class<?> outputType, Object input) {
        Class<?> type = convertType(outputType);
        return conversionMethodIsAvailable(type, input)
                ? tryConverting(type, input)
                : type.isInstance(input);
    }

    private Class<?> convertType(Class<?> type) {
        return primatives.isPrimative(type) ? primatives.getEquivalentType(type) : type;
    }

    private boolean conversionMethodIsAvailable(Class<?> outputType, Object input) {
        Tupel<Class<?>> tupel = createTupel(outputType, input);
        return methods.containsKey(tupel);
    }

    private boolean tryConverting(Class<?> outputType, Object input) {
        try {
            ConvertingMethod method = getMethod(outputType, input);
            method.doConvert(input);
        } catch (RuntimeException ex) {
            return false;
        }
        return true;
    }

    public Object convert(Class<?> outputType, Object input) {
        ConvertingMethod method = getMethod(outputType, input);
        return method.doConvert(input);
    }

    private ConvertingMethod getMethod(Class<?> outputType, Object input) {
        Tupel<Class<?>> tupel = createTupel(outputType, input);
        if (methods.containsKey(tupel))
            return methods.get(tupel);
        throw new InputConvertException();
    }

    private Tupel<Class<?>> createTupel(Class<?> outputType, Object input) {
        return new Tupel<>(outputType, input.getClass());
    }
}
