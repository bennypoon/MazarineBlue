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

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.util.TestLibrary;
import org.mazarineblue.keyworddriven.util.TestLibraryStub;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class KeywordDrivenCopyConstructorEventTest {

    @Test
    public void addLibraryEvent() {
        Library library = new TestLibraryStub("");
        AddLibraryEvent e = new AddLibraryEvent(library);
        assertEquals(e, new AddLibraryEvent(e));
    }

    @Test
    public void commentInstructionLineEvent() {
        CommentInstructionLineEvent e = new CommentInstructionLineEvent("arg1", "arg2");
        assertEquals(e, new CommentInstructionLineEvent(e));
    }

    @Test
    public void countLibrariesEvent() {
        CountLibrariesEvent e = new CountLibrariesEvent();
        List<Library> list = new ArrayList<>(asList(new Library[]{new TestLibrary(""), new TestLibrary("")}));
        e.addToCount(list);
        assertEquals(e, new CountLibrariesEvent(e));
    }

    @Test
    public void executeInstructionLineEvent() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("path", "arg1", "arg2");
        assertEquals(e, new ExecuteInstructionLineEvent(e));
    }

    @Test
    public void fetchLibrariesEvent() {
        FetchLibrariesEvent e = new FetchLibrariesEvent();
        e.addLibrary(new TestLibrary(""));
        assertEquals(e, new FetchLibrariesEvent(e));
    }

    @Test
    public void removeLibraryEvent() {
        Library library = new TestLibraryStub("");
        RemoveLibraryEvent e = new RemoveLibraryEvent(library);
        assertEquals(e, new RemoveLibraryEvent(e));
    }

    @Test
    public void validateInstructionLineEvent() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("path", "arg1", "arg2", "arg3");
        assertEquals(e, new ValidateInstructionLineEvent(e));
    }
}
