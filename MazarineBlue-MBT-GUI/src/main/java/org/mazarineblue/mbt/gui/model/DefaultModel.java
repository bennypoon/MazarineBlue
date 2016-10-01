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
import java.util.Set;
import java.util.TreeSet;
import javax.swing.event.EventListenerList;

class DefaultModel
        implements GraphModel {

    private static final long serialVersionUID = 1L;

    private final List<State> states = new ArrayList<>(16);
    private final List<Transition> transitions = new ArrayList<>(16);
    private final EventListenerList listeners = new EventListenerList();

    @Override
    public String toString() {
        return "states=" + states.size() + ", transitions=" + transitions.size();
    }

    @Override
    public Collection<String> getViews() {
        Set<String> views = new TreeSet<>();
        states.stream().forEach(s -> views.addAll(s.getViews()));
        return Collections.unmodifiableCollection(views);
    }

    @Override
    public List<State> getStatesByName(String name) {
        return states.stream()
                .filter(t -> t.getName().equals(name))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    @Override
    public List<State> getStatesByView(String view) {
        return view == null || view.isEmpty()
                ? Collections.unmodifiableList(states)
                : findStatesWithView(view);
    }

    private List<State> findStatesWithView(String view) {
        List<State> list = new ArrayList<>(4);
        states.stream().filter(s -> s.containsView(view)).forEach(list::add);
        return list;
    }

    @Override
    public void addState(State... arr) {
        List<State> list = asList(arr);
        list.stream().forEach(State::verify);
        states.addAll(list);
        fireAddedStates(Collections.unmodifiableList(list));
    }

    private void fireAddedStates(List<State> list) {
        asList(listeners.getListeners(ModelListener.class)).stream()
                .forEach(l -> l.addedStates(list));
    }

    @Override
    public List<Transition> getTransitionByName(String name) {
        return transitions.stream()
                .filter(t -> t.getName().equals(name))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    @Override
    public void addTransition(Transition... arr) {
        List<Transition> list = asList(arr);
        list.stream().forEach(Transition::verify);
        transitions.addAll(list);
        fireAddedTransitions(Collections.unmodifiableList(list));
    }

    private void fireAddedTransitions(List<Transition> list) {
        asList(listeners.getListeners(ModelListener.class)).stream()
                .forEach(l -> l.addedTransitions(list));
    }

    @Override
    public List<Transition> getTransitions(String view) {
        return Collections.unmodifiableList(view == null || view.isEmpty() ? transitions : findTransitionsWithView(view));
    }

    @Override
    public List<Transition> getTransitionsByView(String view) {
        return view == null || view.isEmpty()
                ? Collections.unmodifiableList(transitions)
                : findTransitionsWithView(view);
    }

    private List<Transition> findTransitionsWithView(String view) {
        List<Transition> list = new ArrayList<>(4);
        transitions.stream().filter(t -> t.containsView(view)).forEach(list::add);
        return list;
    }

    @Override
    public void addModelListener(ModelListener l) {
        listeners.add(ModelListener.class, l);
    }

    @Override
    public void removeModelListener(ModelListener l) {
        listeners.remove(ModelListener.class, l);
    }
}
