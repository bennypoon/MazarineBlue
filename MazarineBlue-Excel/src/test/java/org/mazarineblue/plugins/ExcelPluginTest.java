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
package org.mazarineblue.plugins;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import static java.util.Arrays.copyOf;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.exceptions.NoEventsLeftException;
import org.mazarineblue.plugins.exceptions.CorruptWorkbookException;
import org.mazarineblue.plugins.exceptions.SheetNotFoundException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ExcelPluginTest {

    private ExcelPlugin plugin;

    @Before
    public void setup() {
        plugin = new ExcelPlugin();
    }

    @After
    public void teardown() {
        plugin = null;
    }

    @Test
    public void canProcess_NonSupported_ReturnsTrue() {
        assertFalse(plugin.canProcess(new File("foo")));
    }

    @Test
    public void canProcess_XLS_ReturnsTrue() {
        assertTrue(plugin.canProcess(new File("foo.xls")));
    }

    @Test
    public void canProcess_XLSX_ReturnsTrue() {
        assertTrue(plugin.canProcess(new File("foo.xlsx")));
    }

    @Test(expected = NullPointerException.class)
    public void readSheetNames_Null_ThrowsNullPointerException() {
        plugin.readSheetNames(null);
    }

    @SuppressWarnings("PublicInnerClass")
    public class EmptyWorkbookAsBytes {

        private byte[] bytes;

        @Before
        public void setup()
                throws IOException {
            try (Workbook workbook = new SXSSFWorkbook()) {
                ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
                workbook.write(output);
                bytes = output.toByteArray();
            }
        }

        @After
        public void teardown() {
        }

        @Test(expected = CorruptWorkbookException.class)
        public void readSheetNames_CurruptHeader_ThrowsIllegalArgumentException()
                throws IOException {
            plugin.readSheetNames(new ByteArrayInputStream(copyOf(bytes, 2)));
        }

        @Test(expected = CorruptWorkbookException.class)
        public void createFeed_CurruptHeader_ThrowsIllegalArgumentException()
                throws IOException {
            plugin.createFeed(new ByteArrayInputStream(copyOf(bytes, 2)), null);

        }

        @Test(expected = CorruptWorkbookException.class)
        public void readSheetNames_CurruptBody_ThrowsCorruptWorkbookException()
                throws IOException {
            plugin.readSheetNames(new ByteArrayInputStream(copyOf(bytes, 200)));
        }

        @Test(expected = CorruptWorkbookException.class)
        public void createFeed_CurruptBody_ThrowsCorruptWorkbookException()
                throws IOException {
            plugin.createFeed(new ByteArrayInputStream(copyOf(bytes, 200)), "foo");
        }

        @Test
        public void readSheetNames_EmptyWorkbook_ReturnsEmptyArray()
                throws IOException {
            assertArrayEquals(new String[0], plugin.readSheetNames(new ByteArrayInputStream(bytes)));
        }

        @Test(expected = SheetNotFoundException.class)
        public void createFeed_EmptyWorkbook_ReturnsEmptyArray()
                throws IOException {
            plugin.createFeed(new ByteArrayInputStream(bytes), "foo");
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class WorkbookWithEmptySheet {

        private ByteArrayInputStream input;

        @Before
        public void setup()
                throws IOException {
            try (Workbook workbook = new SXSSFWorkbook()) {
                Sheet sheet = workbook.createSheet();
                workbook.setSheetName(0, "foo");
                ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
                workbook.write(output);
                input = new ByteArrayInputStream(output.toByteArray());
            }
        }

        @After
        public void teardown() {
            input = null;
        }

        @Test
        public void readSheetNames_WorkbookWithSheets_ReturnsSheetNames()
                throws IOException {
            assertArrayEquals(new String[]{"foo"}, plugin.readSheetNames(input));
        }

        @Test(expected = NoEventsLeftException.class)
        public void createFeed_WorkbookWithSheets_ReturnsSheetNames()
                throws IOException {
            Feed feed = plugin.createFeed(input, "foo");
            assertFalse(feed.hasNext());
            feed.next();
        }
    }
}
