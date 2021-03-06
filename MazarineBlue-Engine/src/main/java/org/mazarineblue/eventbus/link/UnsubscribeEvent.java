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

import org.mazarineblue.eventbus.Subscriber;

/**
 * An {@code UnsubscribeEvent} is an {@link Event} that instructs
 * {@link EventBusLink} to unsubscribe a {@link Subscriber}.
 * <p>
 * In order for this {@code Event} to be processed the {@link Interpreter} must
 * have a {@code EventBusLink} and a {@link EventBusLinkSubscriber}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see EventBusLink
 * @see EventBusLinkSubscriber
 * @see SubscribeEvent
 */
public class UnsubscribeEvent
        extends AbstractSubscriberEvent {

    /**
     * A copy constructor that copies all values from another event.
     *
     * @param e the event to copy all values from.
     */
    public UnsubscribeEvent(UnsubscribeEvent e) {
        super(e.getSubscriber());
    }

    /**
     * Constructs an {@code UnsubscribeEvent} to instruct the removal of the
     * specified subscriber.
     *
     * @param subscriber the subscriber to remove from {@link EventBusLink}.
     */
    public UnsubscribeEvent(Subscriber subscriber) {
        super(subscriber);
    }
}
