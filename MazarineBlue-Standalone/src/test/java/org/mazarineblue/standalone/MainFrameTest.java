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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import java.io.IOException;
import static java.util.Arrays.asList;
import java.util.Collections;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.fs.CorrupedFileSystem;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.fs.ReadOnlyFileSystem;
import org.mazarineblue.standalone.config.Config;
import org.mazarineblue.standalone.exceptions.CorrupedFileException;
import org.mazarineblue.standalone.exceptions.NoFileSelectedException;
import org.mazarineblue.standalone.exceptions.NoSheetSelectedException;
import org.mazarineblue.standalone.exceptions.UnableToWriteToFileException;
import org.mazarineblue.standalone.registry.FileSupportRegistry;
import org.mazarineblue.standalone.screens.main.FileSelector;
import org.mazarineblue.standalone.screens.main.MainFrame;
import org.mazarineblue.standalone.screens.main.MainModel;
import org.mazarineblue.standalone.util.SupportAllFilesPlugin;
import org.mazarineblue.standalone.util.SupportNoneFilesPlugin;
import org.mazarineblue.standalone.util.TestFileSelector;
import org.mazarineblue.standalone.util.TestListener;
import org.mazarineblue.swing.SwingUtil;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class MainFrameTest {

    private MainFrame frame;
    private Config config;
    private TestListener listener;
    private FileSystem fs;
    private File[] recentFiles;
    private FileSupportRegistry registry;

    private static File[] createRecentFiles(FileSystem filesystem, int length)
            throws IOException {
        return createRecentFiles(filesystem, length, length);
    }

    private static File[] createRecentFiles(FileSystem filesystem, int length, int amount)
            throws IOException {
        File[] files = new File[length];
        if (length == 0)
            return files;

        int sheet = 0;
        for (int i = 0; i < files.length; ++i)
            files[i] = new File("file" + i + ".xml");
        filesystem.mkfile(Config.recentFilesLocation(), (Object[]) files);
        for (int i = 0; i < files.length && i < amount; ++i)
            filesystem.mkfile(files[0], "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                              + "<sheet name=\"sheet " + ++sheet + "\"></sheet>"
                              + "<sheet name=\"sheet " + ++sheet + "\"></sheet>");
        return files;
    }

    @After
    public void teardown() {
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
        config = null;
        listener = null;
        fs = null;
        recentFiles = null;
        registry = null;
    }

    @Test(expected = CorrupedFileException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void initialize_NonExistingFeed_ThrowsCorrupedFileException()
            throws IOException {
        fs = new CorrupedFileSystem(new MemoryFileSystem());
        createRecentFiles(fs, 1, 0);
        config = new Config(fs);
        listener = new TestListener(fs);
        registry = new FileSupportRegistry(fs, asList(new SupportAllFilesPlugin()));

        MainModel model = new MainModel(config, registry, null, new TestExceptionHandler());
        model.addListener(listener);
        new MainFrame(model);
    }

    @Test(expected = NoFileSelectedException.class)
    public void execute_NonExistingRecentFile_ThrowsNoFileSelectedException()
            throws IOException {
        fs = new CorrupedFileSystem(new MemoryFileSystem());
        config = new Config(fs);
        listener = new TestListener(fs);
        registry = new FileSupportRegistry(fs, asList(new SupportAllFilesPlugin()));
        registry.add(new SupportAllFilesPlugin(new String[0]));

        MainModel model = new MainModel(config, registry, null);
        model.addListener(listener);
        frame = new MainFrame(model);
        SwingUtil.clickButton(frame, "executeButton");
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestingWithMemoryFileSystem {

        @Before
        public void setup() {
            fs = new MemoryFileSystem();
            listener = new TestListener(fs);
            registry = new FileSupportRegistry(fs, Collections.emptyList());
        }

        @Test(expected = NoFileSelectedException.class)
        public void execute_NoSelectedFile_NoFileSelectedException()
                throws IOException {
            createRecentFiles(fs, 0);
            config = new Config(fs);
            registry.add(new SupportAllFilesPlugin());
            MainModel model = new MainModel(config, registry, null, new TestExceptionHandler());
            model.addListener(listener);
            frame = new MainFrame(model);
            SwingUtil.clickButton(frame, "executeButton");
        }

        @Test(expected = NoSheetSelectedException.class)
        public void execute_UnsuppertedSelectedFile_ReturnsEmptyArray()
                throws IOException {
            createRecentFiles(fs, 1);
            config = new Config(fs);
            registry.add(new SupportNoneFilesPlugin());
            MainModel model = new MainModel(config, registry, null, new TestExceptionHandler());
            model.addListener(listener);
            frame = new MainFrame(model);
            SwingUtil.clickButton(frame, "executeButton");
        }

        @Test
        public void execute_SelectedFile_Returned()
                throws IOException {
            recentFiles = createRecentFiles(fs, 3);
            config = new Config(fs);
            registry.add(new SupportAllFilesPlugin("sheet 1", "sheet 2"));

            MainModel model = new MainModel(config, registry, null, new TestExceptionHandler());
            model.addListener(listener);
            frame = new MainFrame(model);
            SwingUtil.clickButton(frame, "executeButton");
            assertEquals(recentFiles[0], listener.getExecutedFile());
            assertEquals("sheet 1", listener.getExecutedSheet());
        }

        @Test
        public void selectFile_SelectedFiles_IsAdded()
                throws IOException {
            recentFiles = createRecentFiles(fs, 3);
            config = new Config(fs);
            registry.add(new SupportAllFilesPlugin("sheet 1", "sheet 2"));

            File file = new File("oke");
            FileSelector selector = new TestFileSelector(file);
            MainModel model = new MainModel(config, registry, selector, new TestExceptionHandler());
            model.addListener(listener);
            frame = new MainFrame(model);
            SwingUtil.clickButton(frame, "selectFileButton");
            SwingUtil.clickButton(frame, "executeButton");

            File[] files = config.getRecentFiles();
            assertEquals(file, listener.getExecutedFile());
            assertEquals(recentFiles.length + 1, files.length);
            assertEquals(file, files[0]);
            for (int i = 1; i < files.length; ++i)
                assertEquals(recentFiles[i - 1], files[i]);
        }

        @Test
        public void selectFile_FullRecentFiles_RecentFilesIsCapped()
                throws IOException {
            recentFiles = createRecentFiles(fs, Config.maxRecentFiles());
            config = new Config(fs);
            registry.add(new SupportAllFilesPlugin("sheet 1", "sheet 2"));

            File file = new File("oke");
            FileSelector selector = new TestFileSelector(file);
            MainModel model = new MainModel(config, registry, selector, new TestExceptionHandler());
            model.addListener(listener);
            frame = new MainFrame(model);
            SwingUtil.clickButton(frame, "selectFileButton");
            SwingUtil.clickButton(frame, "executeButton");

            File[] files = config.getRecentFiles();
            assertEquals(file, listener.getExecutedFile());
            assertEquals(recentFiles.length, files.length);
            assertEquals(file, files[0]);
            for (int i = 1; i < files.length; ++i)
                assertEquals(recentFiles[i - 1], files[i]);
        }
    }

    @Test
    public void selectFile_IgnoresNull_Accepts()
            throws IOException {
        fs = new ReadOnlyFileSystem(new MemoryFileSystem());
        listener = new TestListener(fs);
        registry = new FileSupportRegistry(fs, asList(new SupportAllFilesPlugin("sheet 1", "sheet 2")));
        config = new Config(fs);

        FileSelector selector = new TestFileSelector(null);
        MainModel model = new MainModel(config, registry, selector, new TestExceptionHandler());
        model.addListener(listener);
        frame = new MainFrame(model);
        SwingUtil.clickButton(frame, "selectFileButton");
        assertEquals(null, listener.getSelectedFile());
    }

    @Test(expected = UnableToWriteToFileException.class)
    public void selectFile_ReadOnlyFileSystem_UnableToWriteToFileException()
            throws IOException {
        MemoryFileSystem mfs = new MemoryFileSystem();
        createRecentFiles(mfs, Config.maxRecentFiles());
        fs = new ReadOnlyFileSystem(mfs);
        listener = new TestListener(fs);
        registry = new FileSupportRegistry(fs, asList(new SupportAllFilesPlugin("sheet 1", "sheet 2")));
        config = new Config(fs);

        File file = new File("oke");
        FileSelector selector = new TestFileSelector(file);
        MainModel model = new MainModel(config, registry, selector, new TestExceptionHandler());
        model.addListener(listener);
        frame = new MainFrame(model);
        SwingUtil.clickButton(frame, "selectFileButton");
    }

    @Test
    public void select_DoubleFiles_Filtered()
            throws IOException {
        fs = new MemoryFileSystem();
        listener = new TestListener(fs);
        registry = new FileSupportRegistry(fs, asList(new SupportAllFilesPlugin("sheet 1", "sheet 2")));
        config = new Config(fs);

        File file = new File("oke");
        FileSelector selector = new TestFileSelector(file);
        MainModel model = new MainModel(config, registry, selector, new TestExceptionHandler());
        model.addListener(listener);
        frame = new MainFrame(model);
        SwingUtil.clickButton(frame, "selectFileButton");
        SwingUtil.clickButton(frame, "selectFileButton");

        assertEquals(file, listener.getSelectedFile());
        assertArrayEquals(new File[]{file}, listener.getSelectableOptions());
    }
}
