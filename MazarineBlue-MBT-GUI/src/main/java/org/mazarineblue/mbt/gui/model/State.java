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

public class State
        extends ModelElement<State> {

    private static final long serialVersionUID = 1L;

    private final ArrayList<String> views = new ArrayList<>(4);

    public State(String name) {
        super(name);
    }

    public void verify() {
        // @TODO implement verification here
    }

    public State addViews(String view) {
        views.add(view);
        return this;
    }

    public State addViews(String... views) {
        this.views.addAll(asList(views));
        return this;
    }

    public State addViews(Collection<String> views) {
        this.views.addAll(views);
        return this;
    }

    public Collection<String> getViews() {
        return Collections.unmodifiableCollection(views);
    }

    boolean containsView(String view) {
        return views.contains(view);
    }
}
