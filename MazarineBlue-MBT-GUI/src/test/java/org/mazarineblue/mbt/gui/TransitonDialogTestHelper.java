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

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.junit.After;
import org.junit.Before;
import org.mazarineblue.mbt.gui.list.JListPanel;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;
import org.mazarineblue.mbt.gui.util.FormActionSpy;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;

public abstract class TransitonDialogTestHelper {

    protected static final String INVALID_CHARACTERS_REGULAR = "~!@#$%^&";
    protected static final String INVALID_CHARACTERS_VARIABLE = "~!@#%^";
    protected static final String VALID_CHARACTERS_VARIABLE = "$a == $b || 0 <= ${c} && ${c} <= 100";

    protected static final String STATE_A = "State A";
    protected static final String STATE_B = "State B";
    protected static final String STATE_C = "State C";
    protected static final String STATE_D = "State D";
    protected static final String STATE_E = "State E";

    protected static final int STATE_A_INDEX = 0;
    protected static final int STATE_B_INDEX = 1;
    protected static final int STATE_C_INDEX = 2;
    protected static final int STATE_D_INDEX = 3;
    protected static final int STATE_E_INDEX = 4;

    @SuppressWarnings("ProtectedField")
    protected FormActionSpy<Transition> okSpy;
    @SuppressWarnings("ProtectedField")
    protected TransitionDialog dialog;
    @SuppressWarnings("ProtectedField")
    protected JTextField nameTextField;
    @SuppressWarnings("ProtectedField")
    protected JLabel nameValidationLabel;
    @SuppressWarnings("ProtectedField")
    protected JTextField guardTextField;
    @SuppressWarnings("ProtectedField")
    protected JLabel guardValidationLabel;
    @SuppressWarnings("ProtectedField")
    protected JSlider businessValueSlider;
    @SuppressWarnings("ProtectedField")
    protected JListPanel<State> beforeStatesPanel;
    @SuppressWarnings("ProtectedField")
    protected JComboBox<?> beforeStateComboBox;
    @SuppressWarnings("ProtectedField")
    protected JButton addBeforeStateButton;
    @SuppressWarnings("ProtectedField")
    protected JLabel beforeStateValidationLabel;
    @SuppressWarnings("ProtectedField")
    protected JComboBox<?> afterStateComboBox;
    @SuppressWarnings("ProtectedField")
    protected JLabel afterStateValidationLabel;
    @SuppressWarnings("ProtectedField")
    protected JTextArea actionTextArea;
    @SuppressWarnings("ProtectedField")
    protected JButton acceptButton;
    @SuppressWarnings("ProtectedField")
    protected JButton rejectButton;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        okSpy = new FormActionSpy<>();
        Collection<State> states = new ArrayList<>(4);
        states.add(new State(STATE_A).addViews("View 1", "View 2"));
        states.add(new State(STATE_B).addViews("View 1"));
        states.add(new State(STATE_C).addViews("View 2"));
        states.add(new State(STATE_D).addViews("View 3"));
        states.add(new State(STATE_E));

        dialog = new TransitionDialog(new JFrame(), "Test Transition Dialog");
        dialog.setAcceptAction(okSpy);
        dialog.setOptions(states);

        nameTextField = fetchChildNamed(dialog, "nameTextField", JTextField.class);
        nameValidationLabel = fetchChildNamed(dialog, "nameValidationLabel", JLabel.class);
        guardTextField = fetchChildNamed(dialog, "guardTextField", JTextField.class);
        guardValidationLabel = fetchChildNamed(dialog, "guardValidationLabel", JLabel.class);
        businessValueSlider = fetchChildNamed(dialog, "businessValueSlider", JSlider.class);

        beforeStatesPanel = fetchChildNamed(dialog, "beforeStateListPanel", JListPanel.class);
        beforeStateComboBox = fetchChildNamed(beforeStatesPanel, "optionsComboBox", JComboBox.class);
        addBeforeStateButton = fetchChildNamed(beforeStatesPanel, "addButton", JButton.class);
        beforeStateValidationLabel = fetchChildNamed(beforeStatesPanel, "validationLabel", JLabel.class);

        afterStateComboBox = fetchChildNamed(dialog, "afterStateComboBox", JComboBox.class);
        afterStateValidationLabel = fetchChildNamed(dialog, "afterStateValidationLabel", JLabel.class);
        actionTextArea = fetchChildNamed(dialog, "actionTextArea", JTextArea.class);
        acceptButton = fetchChildNamed(dialog, "acceptButton", JButton.class);
        rejectButton = fetchChildNamed(dialog, "rejectButton", JButton.class);
        nameTextField.requestFocus();
    }

    @After
    public void teardown() {
        dialog.dispose();
        dialog = null;
        okSpy = null;
        nameTextField = null;
        nameValidationLabel = null;
        beforeStateComboBox = null;
        addBeforeStateButton = null;
        beforeStateValidationLabel = null;
        beforeStatesPanel = null;
        afterStateComboBox = null;
        afterStateValidationLabel = null;
        actionTextArea = null;
        acceptButton = null;
        rejectButton = null;
    }
}
