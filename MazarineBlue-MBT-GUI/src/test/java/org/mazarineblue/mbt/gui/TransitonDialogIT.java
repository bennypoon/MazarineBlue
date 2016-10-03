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
package org.mazarineblue.mbt.gui;

import java.util.concurrent.TimeoutException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mazarineblue.mbt.gui.StringConstants.INVALID_CHARACTERS_USED;
import org.mazarineblue.mbt.gui.util.TransitonDialogTestHelper;
import static org.mazarineblue.swing.SwingUtil.waitUntilFalse;
import static org.mazarineblue.swing.SwingUtil.waitUntilTrue;

public class TransitonDialogIT
        extends TransitonDialogTestHelper {

    @Test
    public void name_InvalidInput()
            throws TimeoutException {
        CharSequence invalid = INVALID_CHARACTERS_REGULAR;
        for (int i = 0; i < invalid.length(); ++i) {
            transitionPage.nameValidationLabel.setVisible(false);
            waitUntilFalse(transitionPage.nameValidationLabel::isVisible, 500);

            transitionPage.nameTextField.requestFocus();
            transitionPage.nameTextField.setText(Character.toString(invalid.charAt(i)));
            transitionPage.actionTextArea.requestFocus();

            waitUntilTrue(transitionPage.nameValidationLabel::isVisible, 500);
            assertEquals(INVALID_CHARACTERS_USED, transitionPage.nameValidationLabel.getText());
        }
    }

    @Test
    public void guard_Invalid()
            throws TimeoutException {
        transitionPage.nameTextField.setText("Test Name");
        CharSequence invalid = INVALID_CHARACTERS_VARIABLE;
        for (int i = 0; i < invalid.length(); ++i) {
            transitionPage.guardValidationLabel.setVisible(false);
            waitUntilFalse(transitionPage.guardValidationLabel::isVisible, 500);

            transitionPage.guardTextField.requestFocus();
            transitionPage.guardTextField.setText(Character.toString(invalid.charAt(i)));
            transitionPage.actionTextArea.requestFocus();

            waitUntilTrue(transitionPage.guardValidationLabel::isVisible, 500);
            assertEquals(INVALID_CHARACTERS_USED, transitionPage.guardValidationLabel.getText());
        }
    }
}
