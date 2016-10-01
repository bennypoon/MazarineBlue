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

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import static org.apache.poi.ss.usermodel.WorkbookFactory.create;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.excel.ExcelFeed;
import org.mazarineblue.plugins.exceptions.CorruptWorkbookException;
import org.mazarineblue.plugins.exceptions.SheetNotFoundException;
import org.mazarineblue.utililities.Immutable;

/**
 * An {@code ExcelPlugin} is a {@code FeedPlugin} that support excel files.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@Immutable
public class ExcelPlugin
        implements FeedPlugin {

    @Override
    public boolean canProcess(File file) {
        String path = file.getName();
        return path.endsWith(".xls") || path.endsWith(".xlsx");
    }

    @Override
    public String[] readSheetNames(InputStream input) {
        try (Workbook workbook = create(input)) {
            return getAllSheetNames(workbook);
        } catch (IOException | InvalidFormatException ex) {
            throw new CorruptWorkbookException(ex);
        }
    }

    private String[] getAllSheetNames(Workbook workbook) {
        int n = workbook.getNumberOfSheets();
        String[] names = new String[n];
        for (int i = 0; i < n; ++i)
            names[i] = workbook.getSheetName(i);
        return names;
    }

    @Override
    public Feed createFeed(InputStream input, String sheet)
            throws IOException {
        try (Workbook workbook = create(input)) {
            Sheet s = workbook.getSheet(sheet);
            if (s == null)
                throw new SheetNotFoundException();
            return new ExcelFeed(s);
        } catch (EOFException | InvalidFormatException ex) {
            throw new CorruptWorkbookException(ex);
        }
    }
}
