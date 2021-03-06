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
package org.mazarineblue.keyworddriven.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.util.TestLibrary;
import org.mazarineblue.keyworddriven.util.TestLibraryStub;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LibraryEventTest {

    @Test
    public void message_LibraryWithNoNamespace() {
        Event e = new TestLibraryEventStub(new TestLibraryStub(""));
        assertEquals("[]", e.message());
    }

    @Test
    public void message_LibraryWithNamespace() {
        Event e = new TestLibraryEventStub(new TestLibraryStub("namespace"));
        assertEquals("namespace []", e.message());
    }

    @Test
    public void message_LibraryWithNamespaceAndMethods() {
        Event e = new TestLibraryEventStub(new TestLibraryStub("namespace") {
            @Keyword("a")
            public void testA() {
            }

            @Keyword("b")
            public void testC() {
            }

            @Keyword("c")
            public void testB() {
            }
        });
        assertEquals("namespace [a, b, c]", e.message());
    }

    @Test
    public void equals_Null() {
        LibraryEvent a = new TestLibraryEventStub(new TestLibrary("foo"));
        assertNotEquals(a, null);
    }

    @Test
    public void equals_DifferentClasses() {
        LibraryEvent a = new TestLibraryEventStub(new TestLibrary("foo"));
        assertNotEquals(a, "");
    }

    @Test
    public void hashCode_DifferentLibraries() {
        LibraryEvent a = new TestLibraryEventStub(new TestLibrary("foo"));
        LibraryEvent b = new TestLibraryEventStub(new TestLibrary("oof"));
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentLibraries() {
        LibraryEvent a = new TestLibraryEventStub(new TestLibrary("foo"));
        LibraryEvent b = new TestLibraryEventStub(new TestLibrary("oof"));
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalEvents() {
        LibraryEvent a = new TestLibraryEventStub(new TestLibrary("foo"));
        LibraryEvent b = new TestLibraryEventStub(new TestLibrary("oof"));
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_IdenticalEvents() {
        LibraryEvent a = new TestLibraryEventStub(new TestLibrary("foo"));
        LibraryEvent b = new TestLibraryEventStub(new TestLibrary("foo"));
        assertNotEquals(a, b);
    }
}
