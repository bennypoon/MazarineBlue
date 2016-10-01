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
package org.mazarineblue.standalone.config;

import java.io.File;
import org.mazarineblue.standalone.screens.main.MainModelListener;

/**
 * A {@code ConfigListener} is a {@code MailModelListener} that processes
 * users interaction with the {@link MainModel} and process them to the
 * configuration, as needed.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ConfigListener
        implements MainModelListener {

    private final Config config;

    /**
     * Constructs a {@code ConfigListener} with a mapping to a {@code Config}
     * object.
     *
     * @param config the object with the configuration.
     */
    public ConfigListener(Config config) {
        this.config = config;
    }

    @Override
    public void feedSelected(File file, File[] recentFiles) {
        config.storeRecentFiles(recentFiles);
    }

    @Override
    public void executeFeed(File file, String sheet) {
        // We are only interested in the file array.
    }
}
