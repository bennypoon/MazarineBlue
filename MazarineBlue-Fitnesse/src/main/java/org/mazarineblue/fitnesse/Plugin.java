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

import fitnesse.plugins.PluginFeatureFactoryBase;
import fitnesse.testrunner.TestSystemFactoryRegistry;
import fitnesse.testsystems.TestSystemFactory;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventdriven.InterpreterFactory;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.eventdriven.ProcessorFactory;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.fitnesse.events.FitnesseEvent;
import org.mazarineblue.fixtures.FixtureLoaderLink;
import org.mazarineblue.keyworddriven.LibraryRegistry;
import org.mazarineblue.keyworddriven.events.KeywordDrivenEvent;
import org.mazarineblue.links.ConsumeEventsLink;
import org.mazarineblue.links.UnconsumedExceptionThrowerLink;
import org.mazarineblue.runner.RunnerFactory;
import org.mazarineblue.runner.ThreadRunnerFactory;
import org.mazarineblue.runner.events.RunnerEvent;
import org.mazarineblue.utililities.BlockingTwoWayPipeFactory;
import org.mazarineblue.utililities.TwoWayPipeFactory;
import org.mazarineblue.variablestore.VariableStore;
import org.mazarineblue.variablestore.events.VariableStoreEvent;

/**
 * The {@code Plugin} is the MazarineBlue main class for the FitNesse point of
 * view.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class Plugin
        extends PluginFeatureFactoryBase {

    public static final String NAME = "mazarineblue";
    public static final int TIMEOUT = 4000; // Timeout in milliseconds

    @Override
    public void registerTestSystemFactories(TestSystemFactoryRegistry registry) {
        TwoWayPipeFactory<Event> pipeFactory = new BlockingTwoWayPipeFactory<>();
        RunnerFactory runningFactory = new ThreadRunnerFactory(createInterpreterFactory(), TIMEOUT);
        TestSystemFactory factory = new MazarineBlueTestSystemFactory(pipeFactory, runningFactory);
        registry.registerTestSystemFactory(NAME, factory);
    }

    private InterpreterFactory createInterpreterFactory() {
        InterpreterFactory factory = new ProcessorFactory();
        factory.addLink(new UnconsumedExceptionThrowerLink(RunnerEvent.class));
        factory.addLink(createEventBusLink());
        factory.addLink(new FixtureLoaderLink());
        factory.addLink(new ConsumeEventsLink(ExceptionThrownEvent.class));
        return factory;
    }

    private Link createEventBusLink() {
        EventBusLink link = new EventBusLink();
        link.subscribe(KeywordDrivenEvent.class, null, new LibraryRegistry());
        link.subscribe(VariableStoreEvent.class, null, new VariableStore());
        link.subscribe(FitnesseEvent.class, null, new FitnesseSubscriber());
        return link;
    }
}
