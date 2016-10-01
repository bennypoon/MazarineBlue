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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.junit.After;
import org.junit.Before;
import org.mazarineblue.mbt.gui.list.JListPanel;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.util.FormActionSpy;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;

public abstract class StateDialogTestHelper {

    protected static final String INVALID_CHARACTERS = "~!@#$%^&";

    @SuppressWarnings("ProtectedField")
    protected FormActionSpy<State> okSpy;
    @SuppressWarnings("ProtectedField")
    protected StateDialog dialog;
    @SuppressWarnings("ProtectedField")
    protected JTextField nameTextField;
    @SuppressWarnings("ProtectedField")
    protected JLabel nameValidationLabel;
    @SuppressWarnings("ProtectedField")
    protected JListPanel<String> viewPanel;
    @SuppressWarnings("ProtectedField")
    protected JComboBox<?> viewComboBox;
    @SuppressWarnings("ProtectedField")
    protected JButton addViewButton;
    @SuppressWarnings("ProtectedField")
    protected JLabel viewValidationLabel;
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
        dialog = new StateDialog(new JFrame(), "Test State Dialog");
        dialog.setAcceptAction(okSpy);
        nameTextField = fetchChildNamed(dialog, "nameTextField", JTextField.class);
        nameValidationLabel = fetchChildNamed(dialog, "nameValidationLabel", JLabel.class);

        viewPanel = fetchChildNamed(dialog, "viewListPanel", JListPanel.class);
        viewComboBox = fetchChildNamed(viewPanel, "optionsComboBox", JComboBox.class);
        addViewButton = fetchChildNamed(viewPanel, "addButton", JButton.class);
        viewValidationLabel = fetchChildNamed(viewPanel, "validationLabel", JLabel.class);

        actionTextArea = fetchChildNamed(dialog, "actionTextArea", JTextArea.class);
        acceptButton = fetchChildNamed(dialog, "acceptButton", JButton.class);
        rejectButton = fetchChildNamed(dialog, "rejectButton", JButton.class);
        nameTextField.requestFocus();
    }

    @After
    public void teardown() {
        dialog.dispose();
        dialog = null;
        nameTextField = null;
        nameValidationLabel = null;
        viewComboBox = null;
        viewValidationLabel = null;
        viewPanel = null;
        actionTextArea = null;
    }
}
