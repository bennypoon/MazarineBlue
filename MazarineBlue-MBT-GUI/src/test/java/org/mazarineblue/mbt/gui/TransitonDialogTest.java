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
import static org.mazarineblue.mbt.gui.StringConstants.BUSINESS_VALUE_MAX;
import static org.mazarineblue.mbt.gui.StringConstants.CANT_BE_BLANK;
import static org.mazarineblue.mbt.gui.StringConstants.INVALID_CHARACTERS_USED;
import static org.mazarineblue.mbt.gui.StringConstants.IS_ALREADY_ADDED;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;
import org.mazarineblue.mbt.gui.util.TransitonDialogTestHelper;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;
import static org.mazarineblue.swing.SwingUtil.waitFor;
import static org.mazarineblue.swing.SwingUtil.waitUntilFalse;
import static org.mazarineblue.swing.SwingUtil.waitUntilTrue;

public class TransitonDialogTest
        extends TransitonDialogTestHelper {

    private void addBeforeState(int index) {
        transitionPage.beforeStateComboBox.setSelectedIndex(index);
        transitionPage.beforeStateAddButton.doClick();
    }

    private void assertItem(String expected, int index) {
        @SuppressWarnings("unchecked")
        JPanel panel = transitionPage.beforeStatesPanel.getContentComponent(index, JPanel.class);
        JLabel label = fetchChildNamed(panel, "itemLabel", JLabel.class);
        assertEquals(expected, label.getText());
    }

    @Test
    public void initialDialog() {
        assertTrue(transitionPage.nameTextField.getText().isEmpty());
        assertFalse(transitionPage.nameValidationLabel.isVisible());
        assertFalse(transitionPage.beforeStateComboBox.isEditable());
        assertEquals(-1, transitionPage.beforeStateComboBox.getSelectedIndex());
        assertFalse(transitionPage.beforeStateValidationLabel.isVisible());
        assertEquals(0, transitionPage.beforeStatesPanel.getContentCount());
        assertFalse(transitionPage.afterStateComboBox.isEditable());
        assertEquals(-1, transitionPage.afterStateComboBox.getSelectedIndex());
        assertFalse(transitionPage.afterStateValidationLabel.isVisible());
        assertTrue(transitionPage.actionTextArea.getText().isEmpty());
    }

    @Test
    public void name_EmptyInput()
            throws TimeoutException {
        transitionPage.actionTextArea.requestFocus();
        waitUntilTrue(transitionPage.nameValidationLabel::isVisible, 500);
        assertTrue(transitionPage.nameTextField.getText().isEmpty());
        assertEquals(CANT_BE_BLANK, transitionPage.nameValidationLabel.getText());
    }

    @Test
    public void name_InvalidInput()
            throws TimeoutException {
        transitionPage.nameValidationLabel.setVisible(false);
        waitUntilFalse(transitionPage.nameValidationLabel::isVisible, 500);

        transitionPage.nameTextField.requestFocus();
        transitionPage.nameTextField.setText(INVALID_CHARACTERS_REGULAR);
        transitionPage.actionTextArea.requestFocus();

        waitUntilTrue(transitionPage.nameValidationLabel::isVisible, 500);
        assertEquals(INVALID_CHARACTERS_USED, transitionPage.nameValidationLabel.getText());
    }

    @Test
    public void name_ValidInput() {
        transitionPage.nameTextField.requestFocus();
        transitionPage.nameTextField.setText("_State 1-2");
        transitionPage.actionTextArea.requestFocus();
        assertFalse(transitionPage.nameValidationLabel.isVisible());
    }

    @Test
    public void guard_Invalid()
            throws TimeoutException {
        transitionPage.nameTextField.setText("Test Name");
        transitionPage.guardValidationLabel.setVisible(false);
        waitUntilFalse(transitionPage.guardValidationLabel::isVisible, 500);

        transitionPage.guardTextField.requestFocus();
        transitionPage.guardTextField.setText(INVALID_CHARACTERS_VARIABLE);
        transitionPage.actionTextArea.requestFocus();

        waitUntilTrue(transitionPage.guardValidationLabel::isVisible, 500);
        assertEquals(INVALID_CHARACTERS_USED, transitionPage.guardValidationLabel.getText());
    }

    @Test
    public void guard_Valid() {
        transitionPage.guardTextField.requestFocus();
        transitionPage.guardTextField.setText(VALID_CHARACTERS_VARIABLE);
        transitionPage.actionTextArea.requestFocus();
        assertFalse(transitionPage.guardValidationLabel.isVisible());
    }

    @Test
    public void beforeState_AddOnce() {
        addBeforeState(STATE_D_INDEX);
        assertEquals(1, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_D, 0);
        assertFalse(transitionPage.beforeStateValidationLabel.isVisible());
    }

    @Test
    public void beforeState_AddTwice()
            throws TimeoutException {
        addBeforeState(STATE_D_INDEX);
        transitionPage.beforeStateAddButton.doClick();

        waitUntilTrue(transitionPage.beforeStateValidationLabel::isVisible, 500);
        assertEquals(1, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_D, 0);
        assertEquals(IS_ALREADY_ADDED, transitionPage.beforeStateValidationLabel.getText());
    }

    @Test
    public void beforeState_StateB_StateA_Happy() {
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_A_INDEX);
        addBeforeState(STATE_B_INDEX);

        assertEquals(1, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_B, 0);
        assertFalse(transitionPage.beforeStateValidationLabel.isVisible());
    }

    @Test
    public void beforeState_StateB_StateB_Happy() {
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_B_INDEX);
        addBeforeState(STATE_B_INDEX);

        assertEquals(1, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_B, 0);
        assertFalse(transitionPage.beforeStateValidationLabel.isVisible());
    }

    @Test
    public void beforeState_StateC_StateB_Rainy()
            throws TimeoutException {
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_B_INDEX);
        addBeforeState(STATE_E_INDEX);

        waitUntilTrue(transitionPage.beforeStateValidationLabel::isVisible, 500);
        assertEquals(0, transitionPage.beforeStatesPanel.getContentCount());
        assertEquals(BEFORE_STATE_DOESNT_SHARE_VIEW, transitionPage.beforeStateValidationLabel.getText());
    }

    @Test
    public void beforeState_StateE_StateB_Rainy()
            throws TimeoutException {
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_B_INDEX);
        addBeforeState(STATE_E_INDEX);

        waitUntilTrue(transitionPage.beforeStateValidationLabel::isVisible, 500);
        assertEquals(0, transitionPage.beforeStatesPanel.getContentCount());
        assertEquals(BEFORE_STATE_DOESNT_SHARE_VIEW, transitionPage.beforeStateValidationLabel.getText());
    }

    @Test
    public void beforeState_StateE_StateE_Happy() {
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_E_INDEX);
        addBeforeState(STATE_E_INDEX);

        assertEquals(1, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_E, 0);
        assertFalse(transitionPage.beforeStateValidationLabel.isVisible());
    }

    @Test
    public void afterState_StateA_StateA_Happy() {
        addBeforeState(STATE_A_INDEX);
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_A_INDEX);

        assertEquals(1, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_A, 0);
        assertFalse(transitionPage.afterStateValidationLabel.isVisible());
    }

    @Test
    public void afterState_StateABC_StateA_Happy() {
        addBeforeState(STATE_A_INDEX);
        addBeforeState(STATE_B_INDEX);
        addBeforeState(STATE_C_INDEX);
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_A_INDEX);

        assertEquals(3, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_A, 0);
        assertItem(STATE_B, 1);
        assertItem(STATE_C, 2);
        assertFalse(transitionPage.afterStateValidationLabel.isVisible());
    }

    @Test
    public void afterState_StateBC_StateA_Happy() {
        addBeforeState(STATE_B_INDEX);
        addBeforeState(STATE_C_INDEX);
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_A_INDEX);

        assertEquals(2, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_B, 0);
        assertItem(STATE_C, 1);
        assertFalse(transitionPage.afterStateValidationLabel.isVisible());
    }

    @Test
    public void afterState_StateABC_StateB_Rainy()
            throws TimeoutException {
        addBeforeState(STATE_A_INDEX);
        addBeforeState(STATE_B_INDEX);
        addBeforeState(STATE_C_INDEX);
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_B_INDEX);

        waitUntilTrue(transitionPage.afterStateValidationLabel::isVisible, 500);
        assertEquals(3, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_A, 0);
        assertItem(STATE_B, 1);
        assertItem(STATE_C, 2);
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, transitionPage.afterStateValidationLabel.getText());
    }

    @Test
    public void afterState_StateBC_StateB_Rainy()
            throws TimeoutException {
        addBeforeState(STATE_B_INDEX);
        addBeforeState(STATE_C_INDEX);
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_B_INDEX);

        waitUntilTrue(transitionPage.afterStateValidationLabel::isVisible, 500);
        assertEquals(2, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_B, 0);
        assertItem(STATE_C, 1);
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, transitionPage.afterStateValidationLabel.getText());
    }

    @Test
    public void afterState_StateC_StateB_Rainy()
            throws TimeoutException {
        addBeforeState(STATE_C_INDEX);
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_B_INDEX);

        waitUntilTrue(transitionPage.afterStateValidationLabel::isVisible, 500);
        assertEquals(1, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_C, 0);
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, transitionPage.afterStateValidationLabel.getText());
    }

    @Test
    public void afterState_StateE_StateE_Happy() {
        addBeforeState(STATE_E_INDEX);
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_E_INDEX);

        assertEquals(1, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_E, 0);
        assertFalse(transitionPage.afterStateValidationLabel.isVisible());
    }

    @Test
    public void afterState_StateE_StateB_Rainy()
            throws TimeoutException {
        addBeforeState(STATE_E_INDEX);
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_B_INDEX);

        waitUntilTrue(transitionPage.afterStateValidationLabel::isVisible, 500);
        assertEquals(1, transitionPage.beforeStatesPanel.getContentCount());
        assertItem(STATE_E, 0);
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, transitionPage.afterStateValidationLabel.getText());
    }

    @Test
    public void accept_WrongInput1()
            throws TimeoutException {
        transitionPage.nameTextField.setText(INVALID_CHARACTERS_REGULAR);
        transitionPage.guardTextField.setText(INVALID_CHARACTERS_VARIABLE);
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_D_INDEX);
        addBeforeState(STATE_A_INDEX);
        transitionPage.actionTextArea.setText("Test Actions");
        transitionPage.acceptButton.doClick();

        waitUntilTrue(transitionPage.nameValidationLabel::isVisible, 500);
        waitUntilTrue(transitionPage.guardValidationLabel::isVisible, 500);
        waitUntilTrue(transitionPage.beforeStateValidationLabel::isVisible, 500);
        assertFalse(transitionPage.afterStateValidationLabel.isVisible());
        assertEquals(INVALID_CHARACTERS_USED, transitionPage.nameValidationLabel.getText());
        assertEquals(INVALID_CHARACTERS_USED, transitionPage.guardValidationLabel.getText());
        assertEquals(BEFORE_STATE_DOESNT_SHARE_VIEW, transitionPage.beforeStateValidationLabel.getText());
    }

    @Test
    public void accept_WrongInput2()
            throws TimeoutException {
        transitionPage.nameTextField.setText(INVALID_CHARACTERS_REGULAR);
        transitionPage.guardTextField.setText(INVALID_CHARACTERS_VARIABLE);
        addBeforeState(STATE_A_INDEX);
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_D_INDEX);
        transitionPage.actionTextArea.setText("Test Actions");
        waitFor(() -> fetchChildNamed(dialog, "itemLabel", JLabel.class), 500);
        transitionPage.acceptButton.doClick();

        waitUntilTrue(transitionPage.nameValidationLabel::isVisible, 500);
        waitUntilTrue(transitionPage.guardValidationLabel::isVisible, 500);
        waitUntilTrue(transitionPage.afterStateValidationLabel::isVisible, 500);
        assertFalse(transitionPage.beforeStateValidationLabel.isVisible());
        assertEquals(INVALID_CHARACTERS_USED, transitionPage.nameValidationLabel.getText());
        assertEquals(INVALID_CHARACTERS_USED, transitionPage.guardValidationLabel.getText());
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, transitionPage.afterStateValidationLabel.getText());
    }

    @Test
    public void accept()
            throws TimeoutException {
        transitionPage.nameTextField.setText("Test Transition");
        transitionPage.guardTextField.setText(VALID_CHARACTERS_VARIABLE);
        transitionPage.businessValueSlider.setValue(BUSINESS_VALUE_MAX);
        addBeforeState(STATE_A_INDEX);
        transitionPage.afterStateComboBox.setSelectedIndex(STATE_B_INDEX);
        transitionPage.beforeStateAddButton.doClick();
        transitionPage.actionTextArea.setText("Test Action");
        waitFor(() -> fetchChildNamed(dialog, "itemLabel", JLabel.class), 500);
        transitionPage.acceptButton.doClick();

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
        transitionPage.nameValidationLabel.setVisible(true);
        transitionPage.guardValidationLabel.setVisible(true);
        transitionPage.beforeStateValidationLabel.setVisible(true);
        transitionPage.afterStateValidationLabel.setVisible(true);

        State stateA = State.createDefault("State A").addViews("View 1", "View 2").setAction("State Action");
        State stateB = State.createDefault("State B").addViews("View 1", "View 2").setAction("State Action");
        Transition transition = Transition.createDefault("Name").setSources(stateA, stateB).setDestination(stateA);
        transition.setGuard("Guard").setBusinessValue(BUSINESS_VALUE_MAX).setAction("Transition Action");
        dialog.setOptions(asList(stateA, stateB));
        dialog.setOld(transition);

        assertFalse(transitionPage.nameValidationLabel.isVisible());
        assertFalse(transitionPage.guardValidationLabel.isVisible());
        assertFalse(transitionPage.beforeStateValidationLabel.isVisible());
        assertFalse(transitionPage.afterStateValidationLabel.isVisible());
        assertEquals("Name", transitionPage.nameTextField.getText());
        assertEquals("Guard", transitionPage.guardTextField.getText());
        assertEquals(BUSINESS_VALUE_MAX, transitionPage.businessValueSlider.getValue());
        assertEquals(2, transitionPage.beforeStateComboBox.getItemCount());
        assertEquals(stateA, transitionPage.beforeStateComboBox.getItemAt(0));
        assertEquals(stateB, transitionPage.beforeStateComboBox.getItemAt(1));
        assertEquals(2, transitionPage.beforeStatesPanel.getContentCount());
        assertEquals(stateA, transitionPage.beforeStatesPanel.getItem(0));
        assertEquals(stateB, transitionPage.beforeStatesPanel.getItem(1));
        assertEquals(stateA, transitionPage.afterStateComboBox.getSelectedItem());
        assertEquals("Transition Action", transitionPage.actionTextArea.getText());
    }

    @Test
    public void setOptions() {
        State stateA = State.createDefault("State A").addViews("View 1", "View 2").setAction("State Action");
        State stateB = State.createDefault("State B").addViews("View 1", "View 2").setAction("State Action");
        dialog.setOptions(asList(stateA, stateB));

        assertEquals(2, transitionPage.beforeStateComboBox.getItemCount());
        assertEquals(stateA, transitionPage.beforeStateComboBox.getItemAt(0));
        assertEquals(stateB, transitionPage.beforeStateComboBox.getItemAt(1));
        assertEquals(0, transitionPage.beforeStatesPanel.getContentCount());
    }
}
