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
import org.mazarineblue.mbt.gui.util.StateDialogTestHelper;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;
import static org.mazarineblue.swing.SwingUtil.waitFor;
import static org.mazarineblue.swing.SwingUtil.waitUntilFalse;
import static org.mazarineblue.swing.SwingUtil.waitUntilTrue;

public class StateDialogTest
        extends StateDialogTestHelper {

    @Test
    public void initialDialog() {
        assertTrue(statePage.nameTextField.getText().isEmpty());
        assertFalse(statePage.nameValidationLabel.isVisible());
        assertTrue(statePage.viewComboBox.isEditable());
        assertEquals(-1, statePage.viewComboBox.getSelectedIndex());
        assertFalse(statePage.viewValidationLabel.isVisible());
        assertEquals(0, statePage.viewPanel.getContentCount());
        assertTrue(statePage.actionTextArea.getText().isEmpty());
    }

    @Test
    public void name_EmptyInput()
            throws TimeoutException {
        statePage.actionTextArea.requestFocus();
        waitUntilTrue(statePage.nameValidationLabel::isVisible, 500);
        assertTrue(statePage.nameTextField.getText().isEmpty());
        assertEquals(CANT_BE_BLANK, statePage.nameValidationLabel.getText());
    }

    @Test
    public void name_InvalidInput()
            throws TimeoutException {
        statePage.nameValidationLabel.setVisible(false);
        waitUntilFalse(statePage.nameValidationLabel::isVisible, 500);

        statePage.nameTextField.requestFocus();
        statePage.nameTextField.setText(INVALID_CHARACTERS);
        statePage.actionTextArea.requestFocus();

        waitUntilTrue(statePage.nameValidationLabel::isVisible, 500);
        assertEquals(INVALID_CHARACTERS_USED, statePage.nameValidationLabel.getText());
    }

    @Test
    public void name_ValidInput() {
        statePage.nameTextField.requestFocus();
        statePage.nameTextField.setText("_State 1-2");
        statePage.actionTextArea.requestFocus();
        assertFalse(statePage.nameValidationLabel.isVisible());
    }

    @Test
    public void views_EmptyInput() {
        statePage.viewComboBox.setSelectedItem("");
        statePage.addViewButton.doClick();

        assertEquals(0, statePage.viewComboBox.getItemCount());
        assertEquals(0, statePage.viewPanel.getContentCount());
        assertTrue(statePage.viewValidationLabel.isVisible());
        assertEquals(CANT_BE_BLANK, statePage.viewValidationLabel.getText());
    }

    @Test
    public void views_InvalidInput()
            throws TimeoutException {
        statePage.viewValidationLabel.setVisible(false);
        waitUntilFalse(statePage.viewValidationLabel::isVisible, 500);

        statePage.viewComboBox.requestFocus();
        statePage.viewComboBox.setSelectedItem(INVALID_CHARACTERS);
        statePage.addViewButton.doClick();
        waitUntilTrue(statePage.viewValidationLabel::isVisible, 500);

        assertEquals(0, statePage.viewComboBox.getItemCount());
        assertEquals(0, statePage.viewPanel.getContentCount());
        assertEquals(INVALID_CHARACTERS_USED, statePage.viewValidationLabel.getText());
    }

    @Test
    public void views_AddOnce() {
        statePage.viewComboBox.setSelectedItem("Test view");
        statePage.addViewButton.doClick();

        @SuppressWarnings("unchecked")
        JPanel panel = statePage.viewPanel.getContentComponent(0, JPanel.class);
        JLabel label = fetchChildNamed(panel, "itemLabel", JLabel.class);
        assertEquals(1, statePage.viewComboBox.getItemCount());
        assertEquals(1, statePage.viewPanel.getContentCount());
        assertEquals("Test view", statePage.viewComboBox.getItemAt(0));
        assertEquals("Test view", label.getText());
        assertFalse(statePage.viewValidationLabel.isVisible());
    }

    @Test
    public void views_AddTwice()
            throws TimeoutException {
        statePage.viewComboBox.setSelectedItem("Test view");
        statePage.addViewButton.doClick();
        statePage.addViewButton.doClick();

        waitUntilTrue(statePage.viewValidationLabel::isVisible, 500);
        @SuppressWarnings("unchecked")
        JPanel panel = statePage.viewPanel.getContentComponent(0, JPanel.class);
        JLabel label = fetchChildNamed(panel, "itemLabel", JLabel.class);
        assertEquals(1, statePage.viewComboBox.getItemCount());
        assertEquals(1, statePage.viewPanel.getContentCount());
        assertEquals("Test view", statePage.viewComboBox.getItemAt(0));
        assertEquals("Test view", label.getText());
        assertEquals(IS_ALREADY_ADDED, statePage.viewValidationLabel.getText());
    }

    @Test
    public void accept_WrongInput()
            throws TimeoutException {
        statePage.nameTextField.setText(INVALID_CHARACTERS);
        statePage.viewComboBox.setSelectedItem("Test View");
        statePage.addViewButton.doClick();
        statePage.actionTextArea.setText("Test Actions");
        waitFor(() -> fetchChildNamed(statePage.dialog, "itemLabel", JLabel.class), 500);
        statePage.acceptButton.doClick();

        waitUntilTrue(statePage.nameValidationLabel::isVisible, 500);
        assertEquals(INVALID_CHARACTERS_USED, statePage.nameValidationLabel.getText());
    }

    @Test
    public void accept()
            throws TimeoutException {
        statePage.nameTextField.setText("Test State");
        statePage.viewComboBox.setSelectedItem("Test View");
        statePage.addViewButton.doClick();
        statePage.actionTextArea.setText("Test Action");
        waitFor(() -> fetchChildNamed(statePage.dialog, "itemLabel", JLabel.class), 500);
        statePage.acceptButton.doClick();

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
        statePage.dialog.setOld(State.createDefault("Name").addViews("View 1", "View 2").setAction("Action"));
        assertEquals("Name", statePage.nameTextField.getText());
        assertFalse(statePage.nameValidationLabel.isVisible());
        assertFalse(statePage.viewValidationLabel.isVisible());
        assertEquals(-1, statePage.viewComboBox.getSelectedIndex());
        assertEquals(null, statePage.viewComboBox.getSelectedItem());
        assertEquals(2, statePage.viewComboBox.getItemCount());
        assertEquals("View 1", statePage.viewComboBox.getItemAt(0));
        assertEquals("View 2", statePage.viewComboBox.getItemAt(1));
        assertEquals(2, statePage.viewPanel.getContentCount());
        assertEquals("View 1", statePage.viewPanel.getItem(0));
        assertEquals("View 2", statePage.viewPanel.getItem(1));
        assertEquals("Action", statePage.actionTextArea.getText());
    }

    @Test
    public void setOptions() {
        statePage.dialog.setOptions(asList("View 1", "View 2"));
        assertEquals(0, statePage.viewPanel.getContentCount());
        assertEquals(-1, statePage.viewComboBox.getSelectedIndex());
        assertEquals(null, statePage.viewComboBox.getSelectedItem());
        assertEquals(2, statePage.viewComboBox.getItemCount());
        assertEquals("View 1", statePage.viewComboBox.getItemAt(0));
        assertEquals("View 2", statePage.viewComboBox.getItemAt(1));
        assertEquals(0, statePage.viewPanel.getContentCount());
    }
}
