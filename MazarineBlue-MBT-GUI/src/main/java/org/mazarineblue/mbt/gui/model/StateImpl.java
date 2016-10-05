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
import java.util.Objects;

class StateImpl
        extends ModelElementImpl<State>
        implements State {

    private static final long serialVersionUID = 1L;

    private List<String> views = new ArrayList<>(4);

    StateImpl(String name) {
        super(name);
    }

    @Override
    public void verify() {
        // @TODO implement verification here
    }

    @Override
    public void copy(State newState) {
        super.copy(newState);
        views = newState.getViews();
    }

    @Override
    public StateImpl addViews(String... views) {
        this.views.addAll(asList(views));
        return this;
    }

    @Override
    public StateImpl addViews(Collection<String> views) {
        this.views.addAll(views);
        return this;
    }

    @Override
    public List<String> getViews() {
        return Collections.unmodifiableList(views);
    }

    @Override
    public boolean containsView(String view) {
        return views.contains(view);
    }

    @Override
    public int hashCode() {
        return 2523 + 29 * Objects.hashCode(views) + super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof State
                && super.equals(obj)
                && Objects.equals(views, ((State) obj).getViews());
    }
}
