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
package org.mazarineblue.standalone.screens.main;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.mazarineblue.standalone.config.Config;
import org.mazarineblue.standalone.config.ConfigListener;
import org.mazarineblue.standalone.exceptions.CorrupedFileException;
import org.mazarineblue.standalone.exceptions.NoFileSelectedException;
import org.mazarineblue.standalone.exceptions.NoSheetSelectedException;
import org.mazarineblue.standalone.registry.FileSupportRegistry;

/**
 * A {@code MainModel} is the model of the main form for which
 * {@link MainFrame} is the view and controller.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class MainModel
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private final DefaultComboBoxModel<File> files;
    private final DefaultComboBoxModel<String> sheets;

    private final transient CompoundedListener listener;
    private final SelectFileAction selectFileAction;
    private final ExecuteAction executeAction;
    private final ExceptionHandler exceptionHandler;

    /**
     * Constructs a {@code MainModel} that performs the user based actions for
     * the {@code MainFrame}.
     *
     * @param config           the object containing the configuration.
     * @param registry         the registry of all supported file types.
     * @param selector         the window allowing the user to select a file.
     * @param exceptionHandler the exception handler to handle uncaught
     *                         exceptions.
     * @see DummyExceptionHandler
     */
    public MainModel(Config config, FileSupportRegistry registry, FileSelector selector,
                     ExceptionHandler exceptionHandler) {
        this.listener = new CompoundedListener(new ConfigListener(config));

        this.exceptionHandler = exceptionHandler;
        files = new DefaultComboBoxModel<>(getRecentFiles(config));
        sheets = new DefaultComboBoxModel<>(readSheetNames((File) files.getSelectedItem(), registry));
        selectFileAction = new SelectFileAction(selector);
        executeAction = new ExecuteAction();
    }

    /**
     * Constructs a {@code MainModel} that performs the user based actions for
     * the {@code MainFrame}.
     *
     * @param config   the object containing the configuration.
     * @param registry the registry of all supported file types.
     * @param selector the window allowing the user to select a file.
     */
    public MainModel(Config config, FileSupportRegistry registry, FileSelector selector) {
        this.listener = new CompoundedListener(new ConfigListener(config));

        this.exceptionHandler = new DummyExceptionHandler();
        files = new DefaultComboBoxModel<>(getRecentFiles(config));
        sheets = new DefaultComboBoxModel<>(readSheetNames((File) files.getSelectedItem(), registry));
        selectFileAction = new SelectFileAction(selector);
        executeAction = new ExecuteAction();
    }

    /**
     * Adds a {@code MainModelListener} to this model.
     *
     * @param l the {@code MainModelListener|} to add.
     */
    public void addListener(MainModelListener l) {
        listener.addListener(l);
    }

    private File[] getRecentFiles(Config config) {
        try {
            return config.getRecentFiles();
        } catch (IOException ex) {
            exceptionHandler.exception(ex);
            return new File[0];
        }
    }

    private static String[] readSheetNames(File file, FileSupportRegistry registry) {
        try {
            return file == null ? new String[0] : registry.readSheetNames(file);
        } catch (IOException ex) {
            throw new CorrupedFileException(file, ex);
        }
    }

    public ComboBoxModel<File> getFileModel() {
        return files;
    }

    public ComboBoxModel<String> getSheetModel() {
        return sheets;
    }

    public Action getSelectFileAction() {
        return selectFileAction;
    }

    public Action getExecuteAction() {
        return executeAction;
    }

    private class SelectFileAction
            extends AbstractAction {

        private static final long serialVersionUID = 1L;

        private final FileSelector selector;

        SelectFileAction(FileSelector selector) {
            this.selector = selector;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selector.waitForUserInput();
            File file = selector.getSelectedFile();
            if (file == null)
                return;
            insertFile(file);
            listener.feedSelected(file, getAllFiles());
        }

        private void insertFile(File file) {
            files.removeElement(file);
            files.insertElementAt(file, 0);
            files.setSelectedItem(file);
        }

        private File[] getAllFiles() {
            int size = files.getSize();
            File[] arr = new File[size];
            for (int i = 0; i < size; ++i)
                arr[i] = files.getElementAt(i);
            return arr;
        }
    }

    private class ExecuteAction
            extends AbstractAction {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (files.getSelectedItem() == null)
                throw new NoFileSelectedException();
            if (sheets.getSelectedItem() == null)
                throw new NoSheetSelectedException();
            listener.executeFeed((File) files.getSelectedItem(), (String) sheets.getSelectedItem());
        }
    }
}
