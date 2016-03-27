/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class Primatives {

    private static Primatives singleton;

    public static Primatives getDefaultInstance() {
        if (singleton == null)
            singleton = new Primatives();
        return singleton;
    }

    private final Map<Class<?>, Class<?>> primatives = new HashMap<>(16);

    private Primatives() {
        primatives.put(boolean.class, Boolean.class);
        primatives.put(byte.class, Byte.class);
        primatives.put(char.class, Character.class);
        primatives.put(short.class, Short.class);
        primatives.put(int.class, Integer.class);
        primatives.put(long.class, Long.class);
        primatives.put(float.class, Float.class);
        primatives.put(double.class, Double.class);
    }

    public boolean isPrimative(Class<?> type) {
        return primatives.containsKey(type);
    }

    public Class<?> getEquivalentType(Class<?> type) {
        return primatives.get(type);
    }
}
