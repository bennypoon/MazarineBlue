/*
 * Copyright (c) 2012 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.eventbus.exceptions;

import org.mazarineblue.eventbus.Filter;
import org.mazarineblue.eventbus.SimpleEventService;
import org.mazarineblue.eventbus.Subscriber;

/**
 * The exception is thrown by {@link SimpleEventService} when an type is passed
 * that is not an {@link Event} or is not assignable to the base type of the
 * {@code SimpleEventService} instance.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see SimpleEventService#SimpleEventService(Class)
 * @see SimpleEventService#subscribe(Class, Filter, Subscriber)
 */
public class IllegalEventTypeException
        extends EventServiceException {

    private static final String FORMAT = "Expected instance of %s but found %s";
    private static final long serialVersionUID = 1L;

    public IllegalEventTypeException(Class<?> expected, Class<?> actual) {
        super(String.format(FORMAT, expected, actual));
    }
}
