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
package org.mazarineblue.mbt;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class StepPattern {

    private StateConfiguration c;
    private Set<Event> e;
    private ConditionValueAssignement cva;
    private Set<Transition> t;

    // 1. Visit c
    // 2. Trigger 1 e -> sattisfying 1 cvs
    // 3. Traversing 1 t
    public StepPattern(StateConfiguration c, Set<Event> e, ConditionValueAssignement cva, Collection<Transition> t) {
        this.c = c;
        this.e = e;
        this.cva = cva;
        this.t = new TreeSet<>(t);
    }
}
