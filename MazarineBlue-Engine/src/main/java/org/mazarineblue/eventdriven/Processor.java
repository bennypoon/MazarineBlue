/*
 * Copyright (c) 2015-2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;

/**
 * A {@code Processor} is a concrete implementation of {@link Interpreter}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see ProcessorFactory
 * @see Interpreter
 */
public class Processor
        extends ChainManager
        implements Interpreter {

    private static final long serialVersionUID = 1L;

    Processor(ChainImpl chain) {
        super(new ChainImpl(chain));
    }

    @Override
    public void execute(Feed feed) {
        while (feed.hasNext()) {
            Event event = feed.next();
            try {
                publish(event);
            } catch (RuntimeException ex) {
                setException(event, ex);
                publishExceptionThrown(new ExceptionThrownEvent(event, ex), ex);
                break;
            } finally {
                feed.done(event);
            }
        }
        feed.reset();
    }

    private void publish(Event event) {
        if (event instanceof InvokerEvent)
            ((InvokerEvent) event).setInvoker(new InvokerImpl(this, getChain()));
        getChain().publish(event);
    }

    private void setException(Event e, RuntimeException ex) {
        if (e instanceof InvokerEvent)
            ((InvokerEvent) e).setException(ex);
    }

    private void publishExceptionThrown(Event e, RuntimeException ex) {
        publish(e);
        if (!e.isConsumed())
            throw ex;
    }
}
