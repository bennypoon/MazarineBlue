/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.standalone.screens.main;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.standalone.registry.FileSupportRegistry;

/**
 * A {@code SheetFilter} is a {@code FilteFilter} that can be set on
 * {@link JFileChooser} to filters on directories and files that can be
 * turned into a {@link Feed}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SheetFilter
        extends FileFilter {

    private final FileSupportRegistry registry;
    private final FileSystem fs;

    /**
     * Constructs a {@code SheetFilter} that filters on directories and
     * supported files by asking the specified {@code FileSupportRegistry}.
     *
     * @param registry the registry to ask which files are supported.
     * @param fs       the {@code FileSystem} to query for files.
     */
    public SheetFilter(FileSupportRegistry registry, FileSystem fs) {
        this.registry = registry;
        this.fs = fs;
    }

    @Override
    public boolean accept(File file) {
        if (file == null || !fs.isReadable(file) || fs.isHidden(file))
            return false;
        return fs.isDirectory(file) || fs.isFile(file) && registry.isSupported(file);
    }

    @Override
    public String getDescription() {
        return "Feeds";
    }
}
