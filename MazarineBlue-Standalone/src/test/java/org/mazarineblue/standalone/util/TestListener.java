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
import java.util.Arrays;
import java.util.Date;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.standalone.screens.main.MainModelListener;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestListener
        implements MainModelListener {

    private final FileSystem fs;
    private File[] selectableOptions;
    private File selectedFile, executedFile;
    private String executedSheet;
    private Date modified;

    public TestListener(FileSystem fs) {
        this.fs = fs;
    }

    public boolean isModifiedAfther(Date date) {
        return modified == null ? false : modified.after(date);
    }

    @Override
    public void feedSelected(File file, File[] options) {
        this.selectedFile = file;
        this.selectableOptions = Arrays.copyOf(options, options.length);
    }

    @Override
    public void executeFeed(File file, String sheet) {
        this.executedFile = file;
        this.executedSheet = sheet;
        modified = new Date();
    }

    public File[] getSelectableOptions() {
        return Arrays.copyOf(selectableOptions, selectableOptions.length);
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public File getExecutedFile() {
        return executedFile;
    }

    public String getExecutedSheet() {
        return executedSheet;
    }
}
