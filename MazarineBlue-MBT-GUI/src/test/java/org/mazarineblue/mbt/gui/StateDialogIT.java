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
import static org.mazarineblue.swing.SwingUtil.waitUntilFalse;
import static org.mazarineblue.swing.SwingUtil.waitUntilTrue;

public class StateDialogIT
        extends StateDialogTestHelper {

    @Test
    public void name_InvalidInput()
            throws TimeoutException {
        CharSequence invalid = INVALID_CHARACTERS;
        for (int i = 0; i < invalid.length(); ++i) {
            nameValidationLabel.setVisible(false);
            waitUntilFalse(nameValidationLabel::isVisible, 500);

            nameTextField.requestFocus();
            nameTextField.setText(Character.toString(invalid.charAt(i)));
            actionTextArea.requestFocus();

            waitUntilTrue(nameValidationLabel::isVisible, 500);
            assertEquals(INVALID_CHARACTERS_USED, nameValidationLabel.getText());
        }
    }

    @Test
    public void views_InvalidInput()
            throws TimeoutException {
        CharSequence invalid = INVALID_CHARACTERS;
        for (int i = 0; i < invalid.length(); ++i) {
            viewValidationLabel.setVisible(false);
            waitUntilFalse(viewValidationLabel::isVisible, 500);

            viewComboBox.requestFocus();
            viewComboBox.setSelectedItem(Character.toString(invalid.charAt(i)));
            addViewButton.doClick();
            waitUntilTrue(viewValidationLabel::isVisible, 500);

            assertEquals(0, viewComboBox.getItemCount());
            assertEquals(0, viewPanel.getContentCount());
            assertEquals(INVALID_CHARACTERS_USED, viewValidationLabel.getText());
        }
    }
}
