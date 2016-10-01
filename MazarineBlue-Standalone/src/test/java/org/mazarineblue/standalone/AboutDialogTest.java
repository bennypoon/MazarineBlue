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

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.pictures.ImageUtil;
import org.mazarineblue.pictures.Picture;
import org.mazarineblue.standalone.config.Config;
import org.mazarineblue.standalone.registry.FileSupportRegistry;
import org.mazarineblue.standalone.screens.about.AboutDialog;
import org.mazarineblue.standalone.screens.about.AboutModel;
import org.mazarineblue.standalone.screens.about.ImageFetcher;
import org.mazarineblue.standalone.screens.about.ImagePanel;
import org.mazarineblue.standalone.screens.main.MainFrame;
import org.mazarineblue.standalone.screens.main.MainModel;
import org.mazarineblue.standalone.util.ExceptionReporter;
import org.mazarineblue.standalone.util.GraphicalTextImageFetcher;
import org.mazarineblue.standalone.util.NullExceptionReporter;
import org.mazarineblue.standalone.util.TestListener;
import org.mazarineblue.standalone.util.URLImageFetcher;
import org.mazarineblue.swing.SwingUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AboutDialogTest {

    private AboutDialog dialog;
    private Picture mazarineBlue;

    @Before
    public void setupClass()
            throws IOException {
        URL url = getClass().getClassLoader().getResource(new File("logo.png").getPath());
        mazarineBlue = ImageUtil.createPicture(ImageIO.read(url));
    }

    @After
    public void teardownClass() {
        dialog.dispose();
        dialog = null;
        mazarineBlue = null;
    }

    @Test
    public void testIT()
            throws IOException {
        FileSystem fs = new MemoryFileSystem();
        Config config = new Config(fs);
        FileSupportRegistry registry = new FileSupportRegistry(fs, Collections.emptyList());
        MainModel model = new MainModel(config, registry, null, new TestExceptionHandler());
        model.addListener(new TestListener(fs));
        MainFrame frame = new MainFrame(model);

        SwingUtil.clickButton(frame, "aboutMenuItem");
        dialog = SwingUtil.fetchWindowTitled(frame, "About MazarineBlue", AboutDialog.class);
        assertEquals("MazarineBlue", getTextFromLabel(dialog, "titleLabel"));
        assertEquals("Version", getTextFromLabel(dialog, "versionLabel"));
        assertEquals("Build date", getTextFromLabel(dialog, "buildDateLabel"));
        assertEquals("Lead developer", getTextFromLabel(dialog, "leadDeveloperLabel"));
        assertEquals("Graphical design", getTextFromLabel(dialog, "graphicalDesignLabel"));
        assertEquals("Alex de Kruijff", getTextFromLabel(dialog, "leadDeveloperName"));
        assertEquals("Daan Verbiest", getTextFromLabel(dialog, "graphicalDesignName"));

        Image image = SwingUtil.fetchChildNamed(dialog, "logoPanel", ImagePanel.class).getImage();
        Picture actual = ImageUtil.createPicture(image);
        assertEquals(mazarineBlue, actual);
    }

    @Test
    public void defaultLogo()
            throws IOException {
        AboutModel model = new AboutModel(new NullExceptionReporter());
        ImageFetcher fetcher = new GraphicalTextImageFetcher(150, 150);
        dialog = new AboutDialog(null, model, fetcher);
        assertEquals("MazarineBlue", getTextFromLabel(dialog, "titleLabel"));
        assertEquals("Version", getTextFromLabel(dialog, "versionLabel"));
        assertEquals("Build date", getTextFromLabel(dialog, "buildDateLabel"));
        assertEquals("Lead developer", getTextFromLabel(dialog, "leadDeveloperLabel"));
        assertEquals("Graphical design", getTextFromLabel(dialog, "graphicalDesignLabel"));
        assertEquals("Alex de Kruijff", getTextFromLabel(dialog, "leadDeveloperName"));
        assertEquals("Daan Verbiest", getTextFromLabel(dialog, "graphicalDesignName"));

        Image image = SwingUtil.fetchChildNamed(dialog, "logoPanel", ImagePanel.class).getImage();
        Picture actual = ImageUtil.createPicture(image);
        assertNotEquals(mazarineBlue, actual);
    }

    @Test
    public void logoURL_NonExistingFile()
            throws IOException {
        AboutModel model = new AboutModel(new NullExceptionReporter());
        URL url = new URL("file://foo");
        ImageFetcher backup = new GraphicalTextImageFetcher(150, 150);
        ExceptionReporter reporter = new NullExceptionReporter();
        ImageFetcher fetcher = new URLImageFetcher(url, backup, reporter);
        dialog = new AboutDialog(null, model, fetcher);
        assertEquals("MazarineBlue", getTextFromLabel(dialog, "titleLabel"));
        assertEquals("Version", getTextFromLabel(dialog, "versionLabel"));
        assertEquals("Build date", getTextFromLabel(dialog, "buildDateLabel"));
        assertEquals("Lead developer", getTextFromLabel(dialog, "leadDeveloperLabel"));
        assertEquals("Graphical design", getTextFromLabel(dialog, "graphicalDesignLabel"));
        assertEquals("Alex de Kruijff", getTextFromLabel(dialog, "leadDeveloperName"));
        assertEquals("Daan Verbiest", getTextFromLabel(dialog, "graphicalDesignName"));

        Image image = SwingUtil.fetchChildNamed(dialog, "logoPanel", ImagePanel.class).getImage();
        Picture picture = ImageUtil.createPicture(image);
    }

    private static String getTextFromLabel(Component parent, String name) {
        return SwingUtil.fetchChildNamed(parent, name, JLabel.class).getText();
    }
}
