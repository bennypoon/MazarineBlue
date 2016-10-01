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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * A data model for graph, with {@link State states} and
 * {@link Transition transitions}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface GraphModel
        extends Serializable {

    public static GraphModel createDefault() {
        return new DefaultModel();
    }

    /**
     * Returns all views that
     *
     * @return a collection of views
     */
    public Collection<String> getViews();

    /**
     * Returns a selection of states of those who have the specified name.
     *
     * @param name the name to filter on.
     * @return a list of all the states with the specified name.
     */
    public List<State> getStatesByName(String name);

    /**
     * Add the specified {@code states} to the model.
     *
     * @param states the states to add to the model.
     */
    public void addState(State... states);

    public List<State> getStatesByView(String view);

    /**
     * Returns a selection of transitions of those who have the specified name.
     *
     * @param name the name to filter on.
     * @return a list of all the states with the specified name.
     */
    public List<Transition> getTransitionByName(String name);

    public List<Transition> getTransitionsByView(String view);

    public void addTransition(Transition... transition);

    public List<Transition> getTransitions(String view);

    public void addModelListener(ModelListener l);

    public void removeModelListener(ModelListener l);
}