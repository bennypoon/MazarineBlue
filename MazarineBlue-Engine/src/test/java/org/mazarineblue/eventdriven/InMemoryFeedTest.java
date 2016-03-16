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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventdriven.exceptions.FeedClassRequiresPublicDeclarationException;
import org.mazarineblue.eventdriven.exceptions.FeedTargetException;
import org.mazarineblue.eventdriven.exceptions.NoEventsLeftException;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.ExceptionThrowingFeed;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class InMemoryFeedTest {

    @SuppressWarnings("PublicInnerClass")
    public class EmptyFeed {

        private MemoryFeed feed;

        @Before
        public void setup() {
            feed = new MemoryFeed(4);
        }

        @After
        public void teardown() {
            feed = null;
        }

        @Test
        public void hasNext_HasNoNext() {
            assertEquals(false, feed.hasNext());
        }

        @Test(expected = NoEventsLeftException.class)
        public void next_ExceptionThrown() {
            feed.next();
        }

        @Test
        public void reset_Accepted() {
            feed.reset();
            assertEquals(false, feed.hasNext());
        }

        @SuppressWarnings("PublicInnerClass")
        public class FilledFeed {

            private static final int n = 3;
            private List<Event> list;

            @Before
            public void setup() {
                list = new ArrayList<>(3);
                for (int i = 0; i < n; ++i) {
                    Event e = new TestEvent();
                    list.add(e);
                    feed.add(e);
                }
            }

            @After
            public void teardown() {
                list = null;
            }

            @Test
            public void hasNext_Accepted() {
                for (int i = 0; i < n; ++i) {
                    assertEquals(true, feed.hasNext());
                    feed.next();
                }
                assertEquals(false, feed.hasNext());
            }

            @Test
            public void next_Accepted() {
                for (Event e : list)
                    assertEquals(e, feed.next());
            }

            @Test(expected = NoEventsLeftException.class)
            public void next_ExceptionThrown() {
                for (int i = 0; i < n; ++i)
                    feed.next();
                feed.next();
            }

            @Test
            public void reset_Accepted() {
                feed.reset();
                for (int i = 0; i < n; ++i)
                    feed.next();
                feed.reset();
                assertEquals(true, feed.hasNext());
                for (int i = 0; i < n; ++i)
                    feed.next();
            }
        }
    }

    @Test(expected = FeedClassRequiresPublicDeclarationException.class)
    public void done_PrivateFeed_ThrowsFeedClassRequiresPublicDeclarationException() {
        new PrivateFeed().done(new TestEvent());
    }

    @Test(expected = FeedTargetException.class)
    public void done_ExceptionThrowingFeed_ThrowsFeedTargetException() {
        new ExceptionThrowingFeed().done(new TestEvent());
    }

    private class PrivateFeed
            extends MemoryFeed {

        /**
         * Event handlers are not meant to be called directly, instead publish an
         * event to an {@link Interpreter}; please see the specified event for more
         * information about this event handler.
         *
         * @param event the event this {@code EventHandler} processes.
         */
        @EventHandler
        public void eventHandler(TestEvent event) {
        }
    }
}
