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

import java.util.function.Function;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.util.TestLibraryStub;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FetchLibrariesEventTest {

    @Test
    public void message() {
        Function<Library, Boolean> filter = lib -> true;
        FetchLibrariesEvent e = new FetchLibrariesEvent(filter);
        assertEquals("matcher=" + filter, e.message());
    }

    @Test
    public void responce() {
        FetchLibrariesEvent e = new FetchLibrariesEvent();
        e.addLibrary(new TestLibraryStub(""));
        assertEquals("count=1", e.responce());
    }

    @Test
    public void equals_Null() {
        FetchLibrariesEvent a = new FetchLibrariesEvent();
        a.addLibrary(new TestLibraryStub(""));
        assertNotEquals(a, null);
    }

    @Test
    public void equals_DifferentClasses() {
        FetchLibrariesEvent a = new FetchLibrariesEvent();
        a.addLibrary(new TestLibraryStub(""));
        assertNotEquals(a, "");
    }

    @Test
    public void hashCode_DifferentLibraries() {
        FetchLibrariesEvent a = new FetchLibrariesEvent();
        FetchLibrariesEvent b = new FetchLibrariesEvent();
        a.addLibrary(new TestLibraryStub(""));
        b.addLibrary(new TestLibraryStub(""));
        b.addLibrary(new TestLibraryStub("foo"));
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentLibraries() {
        FetchLibrariesEvent a = new FetchLibrariesEvent();
        FetchLibrariesEvent b = new FetchLibrariesEvent();
        a.addLibrary(new TestLibraryStub(""));
        b.addLibrary(new TestLibraryStub(""));
        b.addLibrary(new TestLibraryStub("foo"));
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentFilters() {
        FetchLibrariesEvent a = new FetchLibrariesEvent();
        FetchLibrariesEvent b = new FetchLibrariesEvent(x -> false);
        a.addLibrary(new TestLibraryStub(""));
        b.addLibrary(new TestLibraryStub(""));
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentFilters() {
        FetchLibrariesEvent a = new FetchLibrariesEvent();
        FetchLibrariesEvent b = new FetchLibrariesEvent(x -> false);
        a.addLibrary(new TestLibraryStub(""));
        b.addLibrary(new TestLibraryStub(""));
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalEvents() {
        FetchLibrariesEvent a = new FetchLibrariesEvent();
        FetchLibrariesEvent b = new FetchLibrariesEvent();
        a.addLibrary(new TestLibraryStub(""));
        b.addLibrary(new TestLibraryStub(""));
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_IdenticalEvents() {
        FetchLibrariesEvent a = new FetchLibrariesEvent();
        FetchLibrariesEvent b = new FetchLibrariesEvent();
        a.addLibrary(new TestLibraryStub(""));
        b.addLibrary(new TestLibraryStub(""));
        assertNotEquals(a, b);
    }
}
