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
package org.mazarineblue.standalone.screens.about;

import java.util.ResourceBundle;
import java.util.logging.Level;
import org.mazarineblue.standalone.util.ExceptionReporter;

/**
 * The {@code AboutModel} retrieves non-static information for the
 * {@code AboutDialog}
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AboutModel {

    @SuppressWarnings("NonConstantLogger")
    private final ExceptionReporter logger;
    private final ResourceBundle bundle;

    /**
     * Constructs a {@code AboutModel} that performs the user based actions for
     * a {@code AboutDialog}.
     *
     * @param logger the logger wrapper for exceptions.
     */
    public AboutModel(ExceptionReporter logger) {
        this.bundle = ResourceBundle.getBundle("MazarineBlue");
        this.logger = logger;
    }

    String getVersion() {
        try {
            return bundle.getString("MazarineBlue.version");
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, null, ex);
            return "Unable to fetch version";
        }
    }

    String getBuildDate() {
        try {
            return bundle.getString("MazarineBlue.buildDate");
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, null, ex);
            return "Unable to fetch release date";
        }
    }
}
