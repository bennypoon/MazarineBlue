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

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import org.mazarineblue.eventdriven.ProcessorFactory;
import org.mazarineblue.fs.DiskFileSystem;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.plugins.FeedPlugin;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.standalone.config.Config;
import org.mazarineblue.standalone.registry.FileSupportRegistry;
import org.mazarineblue.standalone.screens.main.FileSelector;
import org.mazarineblue.standalone.screens.main.MainFrame;
import org.mazarineblue.standalone.screens.main.MainModel;
import org.mazarineblue.standalone.screens.main.SheetFilter;
import org.mazarineblue.standalone.screens.selector.DiskFileSelector;
import org.mazarineblue.standalone.util.LoggerExceptionReporter;

/**
 * {@code StandaloneGUI} is an application that starts a graphical user
 * interface allowing the user to select and execute a {@code Feed}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class StandaloneGUI {

    private StandaloneGUI() {
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler("Error", null, new LoggerExceptionReporter()));
        setNimbusLookAndFeel();
        startApplication();
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for main()">
    private static void setNimbusLookAndFeel() {
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // </editor-fold>

    private static void startApplication() {
        FileSystem fs = new DiskFileSystem();
        Config config = new Config(fs);
        PluginLoader<FeedPlugin> loader = new PluginLoader<>(FeedPlugin.class);
        FileSupportRegistry registry = new FileSupportRegistry(fs, loader.getSupportedFeeds());
        FileFilter filter = new SheetFilter(registry, fs);
        FileSelector selector = new DiskFileSelector(getMostRecentDirectory(config), filter);
        MainModel model = new MainModel(config, registry, selector);
        model.addListener(new FeedExecutor(new ProcessorFactory(), registry));
        MainFrame frame = new MainFrame(model);
        EventQueue.invokeLater(() -> frame.setVisible(true));
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for startApplication()">
    private static File getMostRecentDirectory(Config config) {
        try {
            return config.getMostRecentDirectory();
        } catch (IOException ex) {
            return new File(".");
        }
    }
    // </editor-fold>
}
