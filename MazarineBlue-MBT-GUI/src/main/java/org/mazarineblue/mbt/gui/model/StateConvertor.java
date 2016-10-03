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
package org.mazarineblue.mbt.gui.model;

import java.util.ArrayList;
import java.util.List;

public class StateConvertor {

    private final List<State> states;
    private final State oldState;
    private final State newState;

    StateConvertor(List<State> states) {
        this.states = states;
        this.oldState = null;
        this.newState = null;
    }

    StateConvertor(List<State> states, State oldState, State newState) {
        this.states = states;
        this.oldState = oldState;
        this.newState = newState;
    }

    List<State> convert(List<State> list) {
        return list.stream()
                .map(s -> s.equals(oldState) ? newState : convertHelper(s))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    State convert(State s) {
        return s.equals(oldState) ? newState : convertHelper(s);
    }

    private State convertHelper(State s) {
        return states.get(states.indexOf(s));
    }
}
