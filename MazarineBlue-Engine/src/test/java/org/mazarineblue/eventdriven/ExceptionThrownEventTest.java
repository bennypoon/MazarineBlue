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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExceptionThrownEventTest {

    @Test
    public void equals_Null() {
        ExceptionThrownEvent a = new ExceptionThrownEvent(new TestEvent(), new RuntimeException());
        assertNotEquals(a, null);
    }

    @Test
    public void equals_DifferentClasses() {
        ExceptionThrownEvent a = new ExceptionThrownEvent(new TestEvent(), new RuntimeException());
        assertNotEquals(a, "");
    }

    @Test
    public void equals_DifferentEvents() {
        RuntimeException ex = new RuntimeException();
        ExceptionThrownEvent a = new ExceptionThrownEvent(new TestEvent(), ex);
        ExceptionThrownEvent b = new ExceptionThrownEvent(new TestEvent(), ex);
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentEvents() {
        RuntimeException ex = new RuntimeException();
        ExceptionThrownEvent a = new ExceptionThrownEvent(new TestEvent(), ex);
        ExceptionThrownEvent b = new ExceptionThrownEvent(new TestEvent(), ex);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentExceptions() {
        TestEvent e = new TestEvent();
        ExceptionThrownEvent a = new ExceptionThrownEvent(e, new RuntimeException());
        ExceptionThrownEvent b = new ExceptionThrownEvent(e, new RuntimeException());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentExceptions() {
        TestEvent e = new TestEvent();
        ExceptionThrownEvent a = new ExceptionThrownEvent(e, new RuntimeException());
        ExceptionThrownEvent b = new ExceptionThrownEvent(e, new RuntimeException());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_IdenticalExceptionThrownEvents() {
        TestEvent e = new TestEvent();
        RuntimeException ex = new RuntimeException();
        ExceptionThrownEvent a = new ExceptionThrownEvent(e, ex);
        ExceptionThrownEvent b = new ExceptionThrownEvent(e, ex);
        assertEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalExceptionThrownEvents() {
        TestEvent e = new TestEvent();
        RuntimeException ex = new RuntimeException();
        ExceptionThrownEvent a = new ExceptionThrownEvent(e, ex);
        ExceptionThrownEvent b = new ExceptionThrownEvent(e, ex);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
