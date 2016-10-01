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
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.mazarineblue.mbt.gui.exceptions.BusinessValueOutOfRangeException;
import org.mazarineblue.mbt.gui.exceptions.DestinationStateRequiredException;
import org.mazarineblue.mbt.gui.exceptions.IncompatibleViewsException;
import org.mazarineblue.mbt.gui.exceptions.SourceStateRequiredException;
import org.mazarineblue.mbt.gui.verifiers.StatesShareViewChecker;

public class Transition
        extends ModelElement<Transition> {

    private static final long serialVersionUID = 1L;

    private String guard;
    private int businessValue;
    private ArrayList<State> sources;
    private State destination;

    public Transition(String name) {
        super(name);
    }

    public void verify() {
        if (sources == null)
            throw new SourceStateRequiredException();
        if (destination == null)
            throw new DestinationStateRequiredException();
        if (!StatesShareViewChecker.doStatePairsShareAView(destination, () -> sources))
            throw new IncompatibleViewsException(sources, destination);
        // @TODO add verification implementation here
    }

    boolean containsView(String view) {
        return sources.stream().noneMatch(s -> !s.containsView(view)) && destination.containsView(view);
    }

    public String getGuard() {
        return guard;
    }

    public Transition setGuard(String guard) {
        this.guard = guard;
        return this;
    }

    public int getBusinessValue() {
        return businessValue;
    }

    public Transition setBusinessValue(int value) {
        if (value < 0 || value > 100)
            throw new BusinessValueOutOfRangeException(value);
        this.businessValue = value;
        return this;
    }

    public List<State> getSources() {
        return Collections.unmodifiableList(sources);
    }

    public Transition setSources(State... states) {
        sources = new ArrayList<>(asList(states));
        return this;
    }

    public Transition setSources(Collection<State> collection) {
        sources = new ArrayList<>(collection);
        return this;
    }

    public State getDestination() {
        return destination;
    }

    public Transition setDestination(State state) {
        destination = state;
        return this;
    }

    public boolean contains(State state) {
        return getDestination().equals(state) || getSources().stream().anyMatch(s -> s.equals(state));
    }
}
