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

import java.awt.Window;
import java.io.File;
import java.util.concurrent.TimeoutException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.standalone.exceptions.CorrupedFileException;
import org.mazarineblue.standalone.exceptions.FileAccessException;
import org.mazarineblue.standalone.exceptions.NoFileSelectedException;
import org.mazarineblue.standalone.exceptions.NoSheetSelectedException;
import org.mazarineblue.standalone.exceptions.UnableToWriteToFileException;
import org.mazarineblue.standalone.exceptions.UserException;
import org.mazarineblue.standalone.util.NullExceptionReporter;
import org.mazarineblue.swing.SwingUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExceptionHandlerTest {

    private JDialog dialog;
    private String title;
    private Window parent;
    private ExceptionHandler handler;

    @Before
    public void setup() {
        title = "Error";
        parent = new JFrame();
        handler = new ExceptionHandler(title, parent, new NullExceptionReporter());
    }

    @After
    public void teardown() {
        dialog.dispose();
        dialog = null;
        handler = null;
    }

    @Test
    public void nullPointer()
            throws TimeoutException {
        dialog = openErrorDialog(new NullPointerException(), 1000);
        assertErrorMessage("Unknow error occurred.", dialog);
    }

    @Test
    public void NoFileSelectedException()
            throws TimeoutException {
        dialog = openErrorDialog(new NoFileSelectedException(), 1000);
        assertErrorMessage("A file needs to be selected.", dialog);
    }

    @Test
    public void NoSheetSelectedException()
            throws TimeoutException {
        dialog = openErrorDialog(new NoSheetSelectedException(), 1000);
        assertErrorMessage("A sheet needs to be selected.", dialog);
    }

    @Test
    public void SwingException()
            throws TimeoutException {
        dialog = openErrorDialog(new UserException(), 1000);
        assertErrorMessage("Unknow user error occurred.", dialog);
    }

    @Test
    public void CorrupedFileException()
            throws TimeoutException {
        File file = new File("404");
        dialog = openErrorDialog(new CorrupedFileException(file, null), 1000);
        assertErrorMessage("Corruped file: " + file, dialog);
    }

    @Test
    public void UnableToWriteToFileException()
            throws TimeoutException {
        File file = new File("404");
        dialog = openErrorDialog(new UnableToWriteToFileException(file, null), 1000);
        assertErrorMessage("Unable to write to file: " + file, dialog);
    }

    @Test
    public void FileAccessException()
            throws TimeoutException {
        File file = new File("404");
        dialog = openErrorDialog(new FileAccessException(file, null), 1000);
        assertErrorMessage("Unknow error occurred with file: " + file, dialog);
    }

    private JDialog openErrorDialog(Throwable ex, long timeout)
            throws TimeoutException {
        SwingUtilities.invokeLater(() -> handler.uncaughtException(Thread.currentThread(), ex));
        return SwingUtil.waitFor(() -> SwingUtil.fetchWindowTitled(parent, title, JDialog.class), timeout);
    }

    private void assertErrorMessage(String errorMessage, JDialog dialog) {
        assertEquals(errorMessage, SwingUtil.fetchChildIndexed(dialog, 0, JLabel.class).getText());
    }
}
