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
package org.mazarineblue.runner.util;

import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.InterpreterFactory;
import org.mazarineblue.eventdriven.Link;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class InterpreterFactoryAdopter
        implements InterpreterFactory {

    private static final long serialVersionUID = 1L;

    private final Interpreter adoptee;

    public InterpreterFactoryAdopter(Interpreter in) {
        this.adoptee = in;
    }

    @Override
    public Interpreter create() {
        return adoptee;
    }

    @Override
    public int countLinks() {
        return adoptee.countLinks();
    }

    @Override
    public void addLink(Link link) {
        adoptee.addLink(link);
    }

    @Override
    public void addLink(Link link, Link after) {
        adoptee.addLink(link, after);
    }

    @Override
    public void removeLink(Link link) {
        adoptee.removeLink(link);
    }
}
