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

import java.util.Arrays;

/**
 * A tupel is a data structure that allows to store multiple variables in one record.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the type to store in the record.
 */
public class Tupel<T> {

    private final int capacity;
    private final T[] array;

    @SuppressWarnings("unchecked")
    public Tupel(T... array) {
        this.capacity = array.length;
        this.array = array;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(array);
    }

    @Override
    public int hashCode() {
        return 237 + Arrays.deepHashCode(this.array);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && equals((Tupel<?>) obj);
    }

    private boolean equals(Tupel<?> other) {
        return Arrays.deepEquals(this.array, other.array);
    }

    public T get(int index) {
        return array[index];
    }

    public int size() {
        return array.length;
    }
}
