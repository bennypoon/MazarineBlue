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

import java.io.File;

/**
 * A {@code MailModelListener} listens for user interaction with the
 * {@link MainModel}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface MainModelListener {

    /**
     * This method is called when a file was selected.
     *
     * @param file        the most recent file the user selected.
     * @param recentFiles an array of files that the user looked at recently.
     */
    public void feedSelected(File file, File[] recentFiles);

    /**
     * This method is called when a {@link Feed} was executed.
     *
     * @param file  the most recent file the user selected.
     * @param sheet the sheet to start the feed from.
     */
    public void executeFeed(File file, String sheet);
}
