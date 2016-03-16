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

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.util.LinkSpy;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ProcessorFactoryTest {

    private InterpreterFactory factory;

    public ProcessorFactoryTest() {
    }

    @Before
    public void setup() {
        factory = new ProcessorFactory();
    }

    @After
    public void teardown() {
        factory = null;
    }

    @Test
    public void createInterpretor_Accepted() {
        Interpreter interpretor = factory.create();
        assertNotEquals(null, interpretor);
        assertEquals(0, factory.countLinks());
    }

    @Test
    public void addLink_Accepted() {
        factory.addLink(new LinkSpy());
        Interpreter interpretor = factory.create();
        interpretor.addLink(new LinkSpy());
        assertEquals(1, factory.countLinks());
        assertEquals(2, interpretor.countLinks());
    }

    @Test
    public void addLinkInsertAfter_Accepted() {
        Link first = new LinkSpy();
        factory.addLink(first);
        factory.addLink(new LinkSpy(), first);
        Interpreter interpretor = factory.create();
        interpretor.addLink(new LinkSpy(), first);
        assertEquals(2, factory.countLinks());
        assertEquals(3, interpretor.countLinks());
    }

    @Test
    public void removeLink_Accepted() {
        Link link = new LinkSpy();
        factory.addLink(link);
        Interpreter interpretor = factory.create();
        factory.removeLink(link);
        interpretor.removeLink(link);
    }
}
