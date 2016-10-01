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
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mazarineblue.mbt.gui.StringConstants.AFTER_STATE_DOESNT_SHARE_VIEW;
import static org.mazarineblue.mbt.gui.StringConstants.BEFORE_STATE_DOESNT_SHARE_VIEW;
import static org.mazarineblue.mbt.gui.StringConstants.CANT_BE_BLANK;
import static org.mazarineblue.mbt.gui.StringConstants.INVALID_CHARACTERS_USED;
import static org.mazarineblue.mbt.gui.StringConstants.IS_ALREADY_ADDED;
import static org.mazarineblue.mbt.gui.TransitionDialog.BUSINESS_VALUE_MAX;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;
import static org.mazarineblue.swing.SwingUtil.waitFor;
import static org.mazarineblue.swing.SwingUtil.waitUntilFalse;
import static org.mazarineblue.swing.SwingUtil.waitUntilTrue;

public class TransitonDialogTest
        extends TransitonDialogTestHelper {

    private void addBeforeState(int index) {
        beforeStateComboBox.setSelectedIndex(index);
        addBeforeStateButton.doClick();
    }

    private void assertItem(String expected, int index) {
        @SuppressWarnings("unchecked")
        JPanel panel = beforeStatesPanel.getContentComponent(index, JPanel.class);
        JLabel label = fetchChildNamed(panel, "itemLabel", JLabel.class);
        assertEquals(expected, label.getText());
    }

    @Test
    public void initialDialog() {
        assertTrue(nameTextField.getText().isEmpty());
        assertFalse(nameValidationLabel.isVisible());
        assertFalse(beforeStateComboBox.isEditable());
        assertEquals(-1, beforeStateComboBox.getSelectedIndex());
        assertFalse(beforeStateValidationLabel.isVisible());
        assertEquals(0, beforeStatesPanel.getContentCount());
        assertFalse(afterStateComboBox.isEditable());
        assertEquals(-1, afterStateComboBox.getSelectedIndex());
        assertFalse(afterStateValidationLabel.isVisible());
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
        nameTextField.setText(INVALID_CHARACTERS_REGULAR);
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
    public void guard_Invalid()
            throws TimeoutException {
        nameTextField.setText("Test Name");
        guardValidationLabel.setVisible(false);
        waitUntilFalse(guardValidationLabel::isVisible, 500);

        guardTextField.requestFocus();
        guardTextField.setText(INVALID_CHARACTERS_VARIABLE);
        actionTextArea.requestFocus();

        waitUntilTrue(guardValidationLabel::isVisible, 500);
        assertEquals(INVALID_CHARACTERS_USED, guardValidationLabel.getText());
    }

    @Test
    public void guard_Valid() {
        guardTextField.requestFocus();
        guardTextField.setText(VALID_CHARACTERS_VARIABLE);
        actionTextArea.requestFocus();
        assertFalse(guardValidationLabel.isVisible());
    }

    @Test
    public void beforeState_AddOnce() {
        addBeforeState(STATE_D_INDEX);
        assertEquals(1, beforeStatesPanel.getContentCount());
        assertItem(STATE_D, 0);
        assertFalse(beforeStateValidationLabel.isVisible());
    }

    @Test
    public void beforeState_AddTwice()
            throws TimeoutException {
        addBeforeState(STATE_D_INDEX);
        addBeforeStateButton.doClick();

        waitUntilTrue(beforeStateValidationLabel::isVisible, 500);
        assertEquals(1, beforeStatesPanel.getContentCount());
        assertItem(STATE_D, 0);
        assertEquals(IS_ALREADY_ADDED, beforeStateValidationLabel.getText());
    }

    @Test
    public void beforeState_StateB_StateA_Happy() {
        afterStateComboBox.setSelectedIndex(STATE_A_INDEX);
        addBeforeState(STATE_B_INDEX);

        assertEquals(1, beforeStatesPanel.getContentCount());
        assertItem(STATE_B, 0);
        assertFalse(beforeStateValidationLabel.isVisible());
    }

    @Test
    public void beforeState_StateB_StateB_Happy() {
        afterStateComboBox.setSelectedIndex(STATE_B_INDEX);
        addBeforeState(STATE_B_INDEX);

        assertEquals(1, beforeStatesPanel.getContentCount());
        assertItem(STATE_B, 0);
        assertFalse(beforeStateValidationLabel.isVisible());
    }

    @Test
    public void beforeState_StateC_StateB_Rainy()
            throws TimeoutException {
        afterStateComboBox.setSelectedIndex(STATE_B_INDEX);
        addBeforeState(STATE_E_INDEX);

        waitUntilTrue(beforeStateValidationLabel::isVisible, 500);
        assertEquals(0, beforeStatesPanel.getContentCount());
        assertEquals(BEFORE_STATE_DOESNT_SHARE_VIEW, beforeStateValidationLabel.getText());
    }

    @Test
    public void beforeState_StateE_StateB_Rainy()
            throws TimeoutException {
        afterStateComboBox.setSelectedIndex(STATE_B_INDEX);
        addBeforeState(STATE_E_INDEX);

        waitUntilTrue(beforeStateValidationLabel::isVisible, 500);
        assertEquals(0, beforeStatesPanel.getContentCount());
        assertEquals(BEFORE_STATE_DOESNT_SHARE_VIEW, beforeStateValidationLabel.getText());
    }

    @Test
    public void beforeState_StateE_StateE_Happy() {
        afterStateComboBox.setSelectedIndex(STATE_E_INDEX);
        addBeforeState(STATE_E_INDEX);

        assertEquals(1, beforeStatesPanel.getContentCount());
        assertItem(STATE_E, 0);
        assertFalse(beforeStateValidationLabel.isVisible());
    }

    @Test
    public void afterState_StateA_StateA_Happy() {
        addBeforeState(STATE_A_INDEX);
        afterStateComboBox.setSelectedIndex(STATE_A_INDEX);

        assertEquals(1, beforeStatesPanel.getContentCount());
        assertItem(STATE_A, 0);
        assertFalse(afterStateValidationLabel.isVisible());
    }

    @Test
    public void afterState_StateABC_StateA_Happy() {
        addBeforeState(STATE_A_INDEX);
        addBeforeState(STATE_B_INDEX);
        addBeforeState(STATE_C_INDEX);
        afterStateComboBox.setSelectedIndex(STATE_A_INDEX);

        assertEquals(3, beforeStatesPanel.getContentCount());
        assertItem(STATE_A, 0);
        assertItem(STATE_B, 1);
        assertItem(STATE_C, 2);
        assertFalse(afterStateValidationLabel.isVisible());
    }

    @Test
    public void afterState_StateBC_StateA_Happy() {
        addBeforeState(STATE_B_INDEX);
        addBeforeState(STATE_C_INDEX);
        afterStateComboBox.setSelectedIndex(STATE_A_INDEX);

        assertEquals(2, beforeStatesPanel.getContentCount());
        assertItem(STATE_B, 0);
        assertItem(STATE_C, 1);
        assertFalse(afterStateValidationLabel.isVisible());
    }

    @Test
    public void afterState_StateABC_StateB_Rainy()
            throws TimeoutException {
        addBeforeState(STATE_A_INDEX);
        addBeforeState(STATE_B_INDEX);
        addBeforeState(STATE_C_INDEX);
        afterStateComboBox.setSelectedIndex(STATE_B_INDEX);

        waitUntilTrue(afterStateValidationLabel::isVisible, 500);
        assertEquals(3, beforeStatesPanel.getContentCount());
        assertItem(STATE_A, 0);
        assertItem(STATE_B, 1);
        assertItem(STATE_C, 2);
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, afterStateValidationLabel.getText());
    }

    @Test
    public void afterState_StateBC_StateB_Rainy()
            throws TimeoutException {
        addBeforeState(STATE_B_INDEX);
        addBeforeState(STATE_C_INDEX);
        afterStateComboBox.setSelectedIndex(STATE_B_INDEX);

        waitUntilTrue(afterStateValidationLabel::isVisible, 500);
        assertEquals(2, beforeStatesPanel.getContentCount());
        assertItem(STATE_B, 0);
        assertItem(STATE_C, 1);
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, afterStateValidationLabel.getText());
    }

    @Test
    public void afterState_StateC_StateB_Rainy()
            throws TimeoutException {
        addBeforeState(STATE_C_INDEX);
        afterStateComboBox.setSelectedIndex(STATE_B_INDEX);

        waitUntilTrue(afterStateValidationLabel::isVisible, 500);
        assertEquals(1, beforeStatesPanel.getContentCount());
        assertItem(STATE_C, 0);
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, afterStateValidationLabel.getText());
    }

    @Test
    public void afterState_StateE_StateE_Happy() {
        addBeforeState(STATE_E_INDEX);
        afterStateComboBox.setSelectedIndex(STATE_E_INDEX);

        assertEquals(1, beforeStatesPanel.getContentCount());
        assertItem(STATE_E, 0);
        assertFalse(afterStateValidationLabel.isVisible());
    }

    @Test
    public void afterState_StateE_StateB_Rainy()
            throws TimeoutException {
        addBeforeState(STATE_E_INDEX);
        afterStateComboBox.setSelectedIndex(STATE_B_INDEX);

        waitUntilTrue(afterStateValidationLabel::isVisible, 500);
        assertEquals(1, beforeStatesPanel.getContentCount());
        assertItem(STATE_E, 0);
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, afterStateValidationLabel.getText());
    }

    @Test
    public void accept_WrongInput1()
            throws TimeoutException {
        nameTextField.setText(INVALID_CHARACTERS_REGULAR);
        guardTextField.setText(INVALID_CHARACTERS_VARIABLE);
        afterStateComboBox.setSelectedIndex(STATE_D_INDEX);
        addBeforeState(STATE_A_INDEX);
        actionTextArea.setText("Test Actions");
        acceptButton.doClick();

        waitUntilTrue(nameValidationLabel::isVisible, 500);
        waitUntilTrue(guardValidationLabel::isVisible, 500);
        waitUntilTrue(beforeStateValidationLabel::isVisible, 500);
        assertFalse(afterStateValidationLabel.isVisible());
        assertEquals(INVALID_CHARACTERS_USED, nameValidationLabel.getText());
        assertEquals(INVALID_CHARACTERS_USED, guardValidationLabel.getText());
        assertEquals(BEFORE_STATE_DOESNT_SHARE_VIEW, beforeStateValidationLabel.getText());
    }

    @Test
    public void accept_WrongInput2()
            throws TimeoutException {
        nameTextField.setText(INVALID_CHARACTERS_REGULAR);
        guardTextField.setText(INVALID_CHARACTERS_VARIABLE);
        addBeforeState(STATE_A_INDEX);
        afterStateComboBox.setSelectedIndex(STATE_D_INDEX);
        actionTextArea.setText("Test Actions");
        waitFor(() -> fetchChildNamed(dialog, "itemLabel", JLabel.class), 500);
        acceptButton.doClick();

        waitUntilTrue(nameValidationLabel::isVisible, 500);
        waitUntilTrue(guardValidationLabel::isVisible, 500);
        waitUntilTrue(afterStateValidationLabel::isVisible, 500);
        assertFalse(beforeStateValidationLabel.isVisible());
        assertEquals(INVALID_CHARACTERS_USED, nameValidationLabel.getText());
        assertEquals(INVALID_CHARACTERS_USED, guardValidationLabel.getText());
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, afterStateValidationLabel.getText());
    }

    @Test
    public void accept()
            throws TimeoutException {
        nameTextField.setText("Test Transition");
        guardTextField.setText(VALID_CHARACTERS_VARIABLE);
        businessValueSlider.setValue(BUSINESS_VALUE_MAX);
        addBeforeState(STATE_A_INDEX);
        afterStateComboBox.setSelectedIndex(STATE_B_INDEX);
        addBeforeStateButton.doClick();
        actionTextArea.setText("Test Action");
        waitFor(() -> fetchChildNamed(dialog, "itemLabel", JLabel.class), 500);
        acceptButton.doClick();

        Transition newTransition = okSpy.getNewState();
        List<State> sources = newTransition.getSources();
        assertNull(okSpy.getOldState());
        assertEquals("Test Transition", newTransition.getName());
        assertEquals(VALID_CHARACTERS_VARIABLE, newTransition.getGuard());
        assertEquals(BUSINESS_VALUE_MAX, newTransition.getBusinessValue());
        assertEquals(1, sources.size());
        assertEquals(STATE_A, sources.iterator().next().getName());
        assertEquals(STATE_B, newTransition.getDestination().getName());
        assertEquals("Test Action", newTransition.getAction());
    }

    @Test
    public void setOld() {
        nameValidationLabel.setVisible(true);
        guardValidationLabel.setVisible(true);
        beforeStateValidationLabel.setVisible(true);
        afterStateValidationLabel.setVisible(true);

        State stateA = new State("State A").addViews("View 1", "View 2").setAction("State Action");
        State stateB = new State("State B").addViews("View 1", "View 2").setAction("State Action");
        Transition transition = new Transition("Name").setSources(stateA, stateB).setDestination(stateA);
        transition.setGuard("Guard").setBusinessValue(BUSINESS_VALUE_MAX).setAction("Transition Action");
        dialog.setOptions(asList(stateA, stateB));
        dialog.setOld(transition);

        assertFalse(nameValidationLabel.isVisible());
        assertFalse(guardValidationLabel.isVisible());
        assertFalse(beforeStateValidationLabel.isVisible());
        assertFalse(afterStateValidationLabel.isVisible());
        assertEquals("Name", nameTextField.getText());
        assertEquals("Guard", guardTextField.getText());
        assertEquals(BUSINESS_VALUE_MAX, businessValueSlider.getValue());
        assertEquals(2, beforeStateComboBox.getItemCount());
        assertEquals(stateA, beforeStateComboBox.getItemAt(0));
        assertEquals(stateB, beforeStateComboBox.getItemAt(1));
        assertEquals(2, beforeStatesPanel.getContentCount());
        assertEquals(stateA, beforeStatesPanel.getItem(0));
        assertEquals(stateB, beforeStatesPanel.getItem(1));
        assertEquals(stateA, afterStateComboBox.getSelectedItem());
        assertEquals("Transition Action", actionTextArea.getText());
    }

    @Test
    public void setOptions() {
        State stateA = new State("State A").addViews("View 1", "View 2").setAction("State Action");
        State stateB = new State("State B").addViews("View 1", "View 2").setAction("State Action");
        dialog.setOptions(asList(stateA, stateB));

        assertEquals(2, beforeStateComboBox.getItemCount());
        assertEquals(stateA, beforeStateComboBox.getItemAt(0));
        assertEquals(stateB, beforeStateComboBox.getItemAt(1));
        assertEquals(0, beforeStatesPanel.getContentCount());
    }
}
