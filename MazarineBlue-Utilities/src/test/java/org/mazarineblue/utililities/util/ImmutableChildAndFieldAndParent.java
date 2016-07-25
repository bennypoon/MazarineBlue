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
package org.mazarineblue.utililities.util;

import java.util.Objects;
import org.mazarineblue.utililities.Immutable;

public class ImmutableChildAndFieldAndParent
        extends ImmutableObjectWithImmutableField {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("FieldMayBeFinal")
    private String s = "abc";
    private final int value = 3;
    private final Integer value2 = 4;
    private final ImmutableObject finalField = new ImmutableObject();
    private final transient ImmutableObject finalTransientField = new ImmutableObject();

    @Immutable
    @SuppressWarnings("FieldMayBeFinal")
    private transient ImmutableObject immutableField = new ImmutableObject();

    @Override
    public int hashCode() {
        return 6388711
                + 912673 * Objects.hashCode(this.s)
                + 9409 * this.value + 97 * Objects.hashCode(this.value2)
                + super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && this.value == ((ImmutableChildAndFieldAndParent) obj).value
                && Objects.equals(this.s, ((ImmutableChildAndFieldAndParent) obj).s)
                && Objects.equals(this.value2, ((ImmutableChildAndFieldAndParent) obj).value2)
                && super.equals(obj);
    }
}
