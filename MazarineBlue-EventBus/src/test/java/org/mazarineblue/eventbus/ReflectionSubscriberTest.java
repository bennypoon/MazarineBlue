/*
 * Copyright (c) 2015 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventbus.exceptions.EventHandlerRequiresOneParameterException;
import org.mazarineblue.eventbus.exceptions.SubscriberClassRequiresPublicDeclarationException;
import org.mazarineblue.eventbus.subscribers.ReflectionSubscriberSpy;
import org.mazarineblue.eventbus.subscribers.SubscriberFactory;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ReflectionSubscriberTest {

    @Test
    public void eventHandler_TestEvent_Passes() {
        ReflectionSubscriberSpy subscriber = new ReflectionSubscriberSpy();
        subscriber.eventHandler(new TestEvent());
        assertEquals(1, subscriber.receivedEvents());
    }

    @Test(expected = EventHandlerRequiresOneParameterException.class)
    public void eventHandler_ReflectionWithTwoParameters_ThrowsException() {
        Subscriber<Event> subscriber = new ReflectionSubscriber<Event>() {

            @EventHandler
            public void eventHandler(Event event, Event other) {
                throw new Error();
            }
        };
        subscriber.eventHandler(new TestEvent());
    }

    @Test
    public void eventHandler_ReflectionWithStringParameter_DoesNothing() {
        Subscriber<Event> subscriber = new ReflectionSubscriber<Event>() {

            @EventHandler
            public void eventHandler(String other) {
                throw new Error();
            }
        };
        subscriber.eventHandler(new TestEvent());
    }

    @Test(expected = SubscriberClassRequiresPublicDeclarationException.class)
    public void eventHandler_ProtectedSubscriber_ThrowsSubscriberClassRequiresPublicDeclarationException() {
        new SubscriberFactory().createProtectedSubscriber().eventHandler(new TestEvent());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void eventHandler_ThrowExceptionSubscriber_ExceptionThrown() {
        Subscriber<Event> subscriber = new ReflectionSubscriber<Event>() {

            @EventHandler
            public void eventHandler(TestEvent event) {
                throw new IndexOutOfBoundsException();
            }
        };
        subscriber.eventHandler(new TestEvent());
    }
}
