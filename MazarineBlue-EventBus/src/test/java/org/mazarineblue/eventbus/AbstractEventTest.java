/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.eventbus;

import java.util.Date;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.Event.Status;
import org.mazarineblue.eventbus.events.AbstractEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AbstractEventTest {

    private AbstractEvent event;

    @Before
    public void setup() {
        event = new TestAbstractEvent();
    }

    @After
    public void teardown() {
        event = null;
    }

    @Test
    public void status_ReturnsOK() {
        assertEquals(Status.OK, event.status());
    }

    @Test
    public void message_ReturnsEmptyString() {
        assertEquals("", event.message());
    }

    @Test
    public void responce_ReturnsEmptyString() {
        assertEquals("", event.responce());
    }

    @Test
    public void dateConsumed_Unconsumed_ReturnsNull() {
        assertEquals(null, event.dateConsumed());
    }

    @Test
    public void dateConsumed_Consumed_ReturnsADate() {
        event.setConsumed();
        Assert.assertNotEquals(null, event.dateConsumed());
        Assert.assertTrue("Expected a Date but got something else", event.dateConsumed() instanceof Date);
    }

    public class TestAbstractEvent
            extends AbstractEvent {
    }
}
