/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventbus.link.SubscribeEvent;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.InvokerEvent;
import org.mazarineblue.eventdriven.ProcessorFactory;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.keyworddriven.util.TestLibraryStub;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LibraryRegistryTest {

    private MemoryFeed feed;
    private Interpreter interpreter;

    @Before
    public void setup() {
        feed = new MemoryFeed(4);
        feed.add(new SubscribeEvent(new LibraryRegistry()));
        interpreter = new ProcessorFactory().create();
        interpreter.addLink(new EventBusLink());
    }

    @After
    public void teardown() {
        feed = null;
        interpreter = null;
    }

    @Test
    public void addLibrary_Accepted() {
        InvokerEvent addEvent = new AddLibraryEvent(new TestLibraryStub(Library.NO_NAMESPACE));
        CountLibrariesEvent countEvent = new CountLibrariesEvent();
        feed.add(addEvent);
        feed.add(countEvent);
        interpreter.execute(feed);
        assertEquals(true, addEvent.isConsumed());
        assertEquals(1, countEvent.getCount());
    }

    @Test(expected = LibraryNotFoundException.class)
    public void removeLibrary_LibraryNotAdded_ExceptionThrown() {
        feed.add(new RemoveLibraryEvent(new TestLibraryStub(Library.NO_NAMESPACE)));
        feed.add(new CountLibrariesEvent());
        interpreter.execute(feed);
    }

    @Test
    public void removeLibrary_Accepted() {
        Library library = new TestLibraryStub(Library.NO_NAMESPACE);
        RemoveLibraryEvent removeEvent = new RemoveLibraryEvent(library);
        CountLibrariesEvent countEvent = new CountLibrariesEvent();
        feed.add(new AddLibraryEvent(library));
        feed.add(removeEvent);
        feed.add(countEvent);
        interpreter.execute(feed);
        assertEquals(0, countEvent.getCount());
        assertEquals(true, removeEvent.isConsumed());
        assertEquals(false, countEvent.isConsumed());
    }
}
