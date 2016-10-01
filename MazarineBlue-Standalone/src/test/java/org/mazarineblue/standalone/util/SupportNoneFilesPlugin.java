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
package org.mazarineblue.standalone.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.plugins.FeedPlugin;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SupportNoneFilesPlugin
        implements FeedPlugin {

    public SupportNoneFilesPlugin(String... string) {
    }

    @Override
    public boolean canProcess(File file) {
        return false;
    }

    @Override
    public String[] readSheetNames(InputStream input) {
        return new String[0];
    }

    @Override
    public Feed createFeed(InputStream input, String sheet)
            throws IOException {
        throw new IOException();
    }
}