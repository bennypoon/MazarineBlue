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
package org.mazarineblue.fitnesse;

import fitnesse.testsystems.Descriptor;
import fitnesse.testsystems.TestSystem;
import fitnesse.testsystems.TestSystemFactory;
import fitnesse.testsystems.slim.CustomComparatorRegistry;
import fitnesse.testsystems.slim.HtmlSlimTestSystem;
import fitnesse.testsystems.slim.SlimClient;
import fitnesse.testsystems.slim.tables.SlimTableFactory;
import java.io.IOException;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.runner.RunnerFactory;
import org.mazarineblue.utililities.TwoWayPipeFactory;

class MazarineBlueTestSystemFactory
        implements TestSystemFactory {

    private final TwoWayPipeFactory<Event> pipeFactory;
    private final RunnerFactory runnerFactory;

    MazarineBlueTestSystemFactory(TwoWayPipeFactory<Event> pipeFactory, RunnerFactory runnerFactory) {
        this.pipeFactory = pipeFactory;
        this.runnerFactory = runnerFactory;
    }

    @Override
    public TestSystem create(Descriptor descriptor)
            throws IOException {
        SlimClient client = new MazarineBlueSlimClient(pipeFactory, runnerFactory);
        SlimTableFactory factory = new SlimTableFactory();
        CustomComparatorRegistry registry = new CustomComparatorRegistry();
        return new HtmlSlimTestSystem(descriptor.getTestSystem(), client, factory, registry);
    }
}
