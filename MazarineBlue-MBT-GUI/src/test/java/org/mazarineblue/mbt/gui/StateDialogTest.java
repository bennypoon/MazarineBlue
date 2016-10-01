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

import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mazarineblue.mbt.gui.StringConstants.CANT_BE_BLANK;
import static org.mazarineblue.mbt.gui.StringConstants.INVALID_CHARACTERS_USED;
import static org.mazarineblue.mbt.gui.StringConstants.IS_ALREADY_ADDED;
import org.mazarineblue.mbt.gui.model.State;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;
import static org.mazarineblue.swing.SwingUtil.waitFor;
import static org.mazarineblue.swing.SwingUtil.waitUntilFalse;
import static org.mazarineblue.swing.SwingUtil.waitUntilTrue;

public class StateDialogTest
        extends StateDialogTestHelper {

    @Test
    public void initialDialog() {
        assertTrue(nameTextField.getText().isEmpty());
        assertFalse(nameValidationLabel.isVisible());
        assertTrue(viewComboBox.isEditable());
        assertEquals(-1, viewComboBox.getSelectedIndex());
        assertFalse(viewValidationLabel.isVisible());
        assertEquals(0, viewPanel.getContentCount());
        assertTrue(actionTextArea.getText().isEmpty());
    }

    @Test
    public void name_EmptyInput()
            throws TimeoutException {
        actionTextArea.requestFocus();
        waitUntilTrue(nameValidationLabel::isVisible, 500);
        assertTrue(nameTextField.getText().isEmpty());
        assertEquals(CANT_BE_BLANK, nameValidationLabel.getText());
    }

    @Test
    public void name_InvalidInput()
            throws TimeoutException {
        nameValidationLabel.setVisible(false);
        waitUntilFalse(nameValidationLabel::isVisible, 500);

        nameTextField.requestFocus();
        nameTextField.setText(INVALID_CHARACTERS);
        actionTextArea.requestFocus();

        waitUntilTrue(nameValidationLabel::isVisible, 500);
        assertEquals(INVALID_CHARACTERS_USED, nameValidationLabel.getText());
    }

    @Test
    public void name_ValidInput() {
        nameTextField.requestFocus();
        nameTextField.setText("_State 1-2");
        actionTextArea.requestFocus();
        assertFalse(nameValidationLabel.isVisible());
    }

    @Test
    public void views_EmptyInput() {
        viewComboBox.setSelectedItem("");
        addViewButton.doClick();

        assertEquals(0, viewComboBox.getItemCount());
        assertEquals(0, viewPanel.getContentCount());
        assertTrue(viewValidationLabel.isVisible());
        assertEquals(CANT_BE_BLANK, viewValidationLabel.getText());
    }

    @Test
    public void views_InvalidInput()
            throws TimeoutException {
        viewValidationLabel.setVisible(false);
        waitUntilFalse(viewValidationLabel::isVisible, 500);

        viewComboBox.requestFocus();
        viewComboBox.setSelectedItem(INVALID_CHARACTERS);
        addViewButton.doClick();
        waitUntilTrue(viewValidationLabel::isVisible, 500);

        assertEquals(0, viewComboBox.getItemCount());
        assertEquals(0, viewPanel.getContentCount());
        assertEquals(INVALID_CHARACTERS_USED, viewValidationLabel.getText());
    }

    @Test
    public void views_AddOnce() {
        viewComboBox.setSelectedItem("Test view");
        addViewButton.doClick();

        @SuppressWarnings("unchecked")
        JPanel panel = viewPanel.getContentComponent(0, JPanel.class);
        JLabel label = fetchChildNamed(panel, "itemLabel", JLabel.class);
        assertEquals(1, viewComboBox.getItemCount());
        assertEquals(1, viewPanel.getContentCount());
        assertEquals("Test view", viewComboBox.getItemAt(0));
        assertEquals("Test view", label.getText());
        assertFalse(viewValidationLabel.isVisible());
    }

    @Test
    public void views_AddTwice()
            throws TimeoutException {
        viewComboBox.setSelectedItem("Test view");
        addViewButton.doClick();
        addViewButton.doClick();

        waitUntilTrue(viewValidationLabel::isVisible, 500);
        @SuppressWarnings("unchecked")
        JPanel panel = viewPanel.getContentComponent(0, JPanel.class);
        JLabel label = fetchChildNamed(panel, "itemLabel", JLabel.class);
        assertEquals(1, viewComboBox.getItemCount());
        assertEquals(1, viewPanel.getContentCount());
        assertEquals("Test view", viewComboBox.getItemAt(0));
        assertEquals("Test view", label.getText());
        assertEquals(IS_ALREADY_ADDED, viewValidationLabel.getText());
    }

    @Test
    public void accept_WrongInput()
            throws TimeoutException {
        nameTextField.setText(INVALID_CHARACTERS);
        viewComboBox.setSelectedItem("Test View");
        addViewButton.doClick();
        actionTextArea.setText("Test Actions");
        waitFor(() -> fetchChildNamed(dialog, "itemLabel", JLabel.class), 500);
        acceptButton.doClick();

        waitUntilTrue(nameValidationLabel::isVisible, 500);
        assertEquals(INVALID_CHARACTERS_USED, nameValidationLabel.getText());
    }

    @Test
    public void accept()
            throws TimeoutException {
        nameTextField.setText("Test State");
        viewComboBox.setSelectedItem("Test View");
        addViewButton.doClick();
        actionTextArea.setText("Test Action");
        waitFor(() -> fetchChildNamed(dialog, "itemLabel", JLabel.class), 500);
        acceptButton.doClick();

        State newState = okSpy.getNewState();
        Collection<String> views = newState.getViews();
        assertNull(okSpy.getOldState());
        assertEquals(1, views.size());
        assertEquals("Test State", newState.getName());
        assertEquals("Test View", views.iterator().next());
        assertEquals("Test Action", newState.getAction());
    }

    @Test
    public void setOld() {
        dialog.setOld(new State("Name").addViews("View 1", "View 2").setAction("Action"));
        assertEquals("Name", nameTextField.getText());
        assertFalse(nameValidationLabel.isVisible());
        assertFalse(viewValidationLabel.isVisible());
        assertEquals(-1, viewComboBox.getSelectedIndex());
        assertEquals(null, viewComboBox.getSelectedItem());
        assertEquals(2, viewComboBox.getItemCount());
        assertEquals("View 1", viewComboBox.getItemAt(0));
        assertEquals("View 2", viewComboBox.getItemAt(1));
        assertEquals(2, viewPanel.getContentCount());
        assertEquals("View 1", viewPanel.getItem(0));
        assertEquals("View 2", viewPanel.getItem(1));
        assertEquals("Action", actionTextArea.getText());
    }

    @Test
    public void setOptions() {
        dialog.setOptions(asList("View 1", "View 2"));
        assertEquals(0, viewPanel.getContentCount());
        assertEquals(-1, viewComboBox.getSelectedIndex());
        assertEquals(null, viewComboBox.getSelectedItem());
        assertEquals(2, viewComboBox.getItemCount());
        assertEquals("View 1", viewComboBox.getItemAt(0));
        assertEquals("View 2", viewComboBox.getItemAt(1));
        assertEquals(0, viewPanel.getContentCount());
    }
}
