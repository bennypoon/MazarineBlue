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

import fitnesse.slim.instructions.Instruction;
import fitnesse.slim.instructions.InstructionExecutor;
import fitnesse.testsystems.slim.SlimClient;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.runner.Runner;
import org.mazarineblue.runner.RunnerFactory;
import org.mazarineblue.runner.events.ByeEvent;
import org.mazarineblue.runner.events.KillEvent;
import org.mazarineblue.utililities.TwoWayPipe;
import org.mazarineblue.utililities.TwoWayPipeFactory;

/**
 * A {@code MazarineBlueSlimClient} connects to FitNesse using
 * {@link EventCreator}, a {@link InstructionExecutor}, that converts each
 * method call into {@link Event events} and sends them to
 * {@link FitnesseSubscriber}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class MazarineBlueSlimClient
        implements SlimClient {

    private static final int QUEUE_CAPACITY = 16;
    private final TwoWayPipeFactory<Event> pipeFactory;
    private final RunnerFactory runnerFactory;
    private TwoWayPipe<Event> pipe;
    private InstructionExecutor executor;
    private Runner runner;

    MazarineBlueSlimClient(TwoWayPipeFactory<Event> pipeFactory, RunnerFactory runnerFactory) {
        this.pipeFactory = pipeFactory;
        this.runnerFactory = runnerFactory;
    }

    @Override
    public void start()
            throws IOException {
        pipe = pipeFactory.createPipe(QUEUE_CAPACITY);
        executor = new EventCreator(pipe);
        runner = runnerFactory.create(pipe.redirect());
        runner.start();
    }

    @Override
    public Map<String, Object> invokeAndGetResponse(List<Instruction> list)
            throws IOException {
        Map<String, Object> map = new HashMap<>(list.size() * 4 / 3);
        list.stream().forEach(instruction -> map.put(instruction.getId(), executeLine(instruction)));
        return map;
    }

    private Object executeLine(Instruction instruction) {
        return instruction.execute(executor).getResult();
    }

    @Override
    public void connect()
            throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void bye()
            throws IOException {
        try {
            pipe.write(new ByeEvent());
            runner.bye();
        } catch (InterruptedException ex) {
            pipe.clear();
            Thread.currentThread().interrupt();
            throw new IOException(ex);
        } finally {
            clean();
        }
    }

    @Override
    public void kill()
            throws IOException {
        if (pipe == null)
            return;
        try {
            pipe.write(new KillEvent());
            runner.kill();
        } catch (InterruptedException ex) {
            pipe.clear();
            Thread.currentThread().interrupt();
            throw new IOException(ex);
        } finally {
            runner.bye();
            clean();
        }

    }

    private void clean() {
        pipe = null;
        executor = null;
        runner = null;
    }
}
