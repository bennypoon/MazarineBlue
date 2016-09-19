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
package org.mazarineblue.eventdriven;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
abstract class ChainManager
        implements Chain {

    private static final long serialVersionUID = 1L;

    private final ChainImpl chain;

    ChainManager() {
        chain = new ChainImpl();
    }

    ChainManager(ChainImpl chain) {
        this.chain = chain;
    }

    ChainImpl getChain() {
        return chain;
    }

    @Override
    public int countLinks() {
        return chain.countLinks();
    }

    @Override
    public void addLink(Link link) {
        chain.addLink(link);
    }

    @Override
    public void addLink(Link link, Link after) {
        chain.addLink(link, after);
    }

    @Override
    public void removeLink(Link link) {
        chain.removeLink(link);
    }
}
