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
package org.mazarineblue.standalone;

import java.io.File;
import org.mazarineblue.eventdriven.InterpreterFactory;
import org.mazarineblue.standalone.registry.FileSupportRegistry;
import org.mazarineblue.standalone.screens.main.MainModelListener;

/**
 * A {@code FeedExecutor} is a {@code MainModelListener} with the capability to
 * execute a {@code Feed}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FeedExecutor
        implements MainModelListener {

    private final FileSupportRegistry registry;
    private final InterpreterFactory factory;

    /**
     * Constructs a {@code FeedExecutor} with an {@code InterpreterFactory},
     * used to execute a {@code Feed}, and a, {@code FileSupportRegistry},
     * used to convert a file and sheet name into a {@code Feed}.
     *
     * @param factory  the factory used to execute the feed.
     * @param registry the registry used to get the feed.
     */
    public FeedExecutor(InterpreterFactory factory, FileSupportRegistry registry) {
        this.factory = factory;
        this.registry = registry;
    }

    @Override
    public void feedSelected(File file, File[] recentFiles) {
        // Nothing to do here
    }

    @Override
    public void executeFeed(File file, String sheet) {
        factory.create().execute(registry.createFeed(file, sheet));
    }
}
