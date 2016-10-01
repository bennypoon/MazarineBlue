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

import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.Link;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class WaitingInterpreterDummy
        implements Interpreter {

    private static final long serialVersionUID = 1L;
    private static final int time = 15000;

    private int interrupted;

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void execute(Feed feed) {
        try {
            while (true)
                Thread.sleep(time);
        } catch (InterruptedException ex) {
            ++interrupted;
            Thread.currentThread().interrupt();
        }
    }

    public int interrupted() {
        return interrupted;
    }

    @Override
    public int countLinks() {
        return 0;
    }

    @Override
    public void addLink(Link link) {
    }

    @Override
    public void addLink(Link link, Link after) {
    }

    @Override
    public void removeLink(Link link) {
    }
}
