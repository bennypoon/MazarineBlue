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
package org.mazarineblue.standalone.registry;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.plugins.FeedPlugin;
import org.mazarineblue.standalone.exceptions.CorruptFeedException;

/**
 * A {@code FileSupportRegistry} is a register for {@code FileSupport} objects.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FileSupportRegistry
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private final transient FileSystem fs;
    private final List<FeedPlugin> supportList;

    /**
     * Constructs a {@code FileSupportRegistry} thats setup with the specified
     * {@code FileSystem} to read from.
     *
     * @param fs             the {@code FileSystem} to read from.
     * @param supportedFiles a collection of {@code FileSupport} objects.
     */
    public FileSupportRegistry(FileSystem fs, Collection<FeedPlugin> supportedFiles) {
        supportList = new ArrayList<>(supportedFiles);
        this.fs = fs;
    }

    /**
     * Add a {@code FileSupport} to extend the ability of the application to
     * read from a file.
     *
     * @param support the implementation of the file support.
     */
    public void add(FeedPlugin support) {
        supportList.add(support);
    }

    /**
     * Test if the specified file is supported.
     *
     * @param file the file to test.
     * @return {@code true} if the file is supported.
     */
    public boolean isSupported(File file) {
        return getSupportFor(file).canProcess(file);
    }

    /**
     * Read the sheet names from the specified file.
     *
     * @param file the file to read the sheet names from.
     * @return an empty array if the file is not supported.
     *
     * @throws IOException when the file couldn't be read.
     */
    public String[] readSheetNames(File file)
            throws IOException {
        return getSupportFor(file).readSheetNames(fs.getInputStream(file));
    }

    /**
     * Constructs a {@code Feed} from the specified sheet within the specified
     * file.
     *
     * @param file  the file to read.
     * @param sheet the sheet to read.
     * @return a {@code Feed} that reeds instructions from the sheet within the
     *         file.
     */
    public Feed createFeed(File file, String sheet) {
        try {
            return getSupportFor(file).createFeed(fs.getInputStream(file), sheet);
        } catch (IOException ex) {
            throw new CorruptFeedException(file, sheet, ex);
        }
    }

    private FeedPlugin getSupportFor(File file) {
        for (FeedPlugin s : supportList)
            if (s.canProcess(file))
                return s;
        return new DefaultFileSupport();
    }
}
