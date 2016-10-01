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
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.standalone.config.Config;

public class ConfigTest {

    @Test
    public void getMostRecentDirectory_FreshConfig()
            throws IOException {
        FileSystem fs = new MemoryFileSystem();
        Config config = new Config(fs);
        assertEquals(".", config.getMostRecentDirectory().getPath());
    }

    @Test
    public void getMostRecentDirectory_EmptyConfig()
            throws IOException {
        MemoryFileSystem fs = new MemoryFileSystem();
        fs.mkfile(Config.recentFilesLocation(), new ArrayList<>(0));
        Config config = new Config(fs);
        assertEquals(".", config.getMostRecentDirectory().getPath());
    }

    @Test
    public void getMostRecentDirectory_FilledConfig()
            throws IOException {
        MemoryFileSystem fs = new MemoryFileSystem();
        fs.mkdir(new File("dir"));
        fs.mkfile(Config.recentFilesLocation(), asList(new File("dir", "file")));
        Config config = new Config(fs);
        assertEquals("dir", config.getMostRecentDirectory().getPath());
    }
}
