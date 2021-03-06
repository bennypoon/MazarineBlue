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
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.eventbus.link;

import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventbus.EventService;
import org.mazarineblue.eventbus.ReflectionSubscriber;
import org.mazarineblue.eventbus.Subscriber;
import org.mazarineblue.eventdriven.Interpreter;

/**
 * An {@code EventBusLinkSubscriber} is a {@link Subscriber} that can process
 * {@link SubscribeEvent} and {@link UnsubscribeEvent} events so that a
 * {@link EventService} gains or loses a {@code Subscriber}.
 * <p>
 * The {@code EventService} is typically shared with {@link EventBusLink}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class EventBusLinkSubscriber
        extends ReflectionSubscriber<Event> {

    private final EventService<Event> service;

    EventBusLinkSubscriber(EventService<Event> service) {
        this.service = service;
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     */
    @EventHandler
    public void eventHandler(SubscribeEvent event) {
        service.subscribe(event.getType(), event.getFilter(), event.getSubscriber());
        event.setConsumed();
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     */
    @EventHandler
    public void eventHandler(UnsubscribeEvent event) {
        service.unsubscribe(event.getType(), event.getFilter(), event.getSubscriber());
        event.setConsumed();
    }
}
