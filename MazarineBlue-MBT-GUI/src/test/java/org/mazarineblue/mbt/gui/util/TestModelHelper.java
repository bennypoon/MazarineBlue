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
package org.mazarineblue.mbt.gui.util;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.mazarineblue.mbt.gui.ModelEditorFrame;
import org.mazarineblue.mbt.gui.StateDialog;
import org.mazarineblue.mbt.gui.TransitionDialog;
import org.mazarineblue.mbt.gui.list.JListPanel;
import org.mazarineblue.mbt.gui.model.GraphModel;
import org.mazarineblue.swing.SwingUtil;

public class TestModelHelper {

    private final GraphModel model;

    public TestModelHelper(GraphModel model) {
        this.model = model;
    }

    public void addState(ModelEditorFrame frame, String name, String view) {
        SwingUtil.fetchChildNamed(frame, "newStateMenuItem", JMenuItem.class).doClick();
        StateDialog dialog = SwingUtil.fetchWindowTitled(frame, "New State", StateDialog.class);
        @SuppressWarnings(value = "unchecked")
        JListPanel<String> viewPanel = SwingUtil.fetchChildNamed(dialog, "viewListPanel", JListPanel.class);
        SwingUtil.fetchChildNamed(dialog, "nameTextField", JTextField.class).setText(name);
        SwingUtil.fetchChildNamed(viewPanel, "optionsComboBox", JComboBox.class).setSelectedItem(view);
        SwingUtil.fetchChildNamed(viewPanel, "addButton", JButton.class).doClick();
        SwingUtil.fetchChildNamed(dialog, "actionTextArea", JTextArea.class).setText("Test Action");
        SwingUtil.fetchChildNamed(dialog, "acceptButton", JButton.class).doClick();
    }

    public void addTransition(ModelEditorFrame frame, String name, String from, String to) {
        SwingUtil.fetchChildNamed(frame, "newTransitionMenuItem", JMenuItem.class).doClick();
        TransitionDialog dialog = SwingUtil.fetchWindowTitled(frame, "New Transition", TransitionDialog.class);
        @SuppressWarnings(value = "unchecked")
        JListPanel<String> panel = SwingUtil.fetchChildNamed(dialog, "beforeStateListPanel", JListPanel.class);
        SwingUtil.fetchChildNamed(dialog, "nameTextField", JTextField.class).setText(name);
        SwingUtil.fetchChildNamed(dialog, "businessValueSlider", JSlider.class).setValue(TransitionDialog.BUSINESS_VALUE_MAX);
        SwingUtil.fetchChildNamed(panel, "optionsComboBox", JComboBox.class).setSelectedItem(model.getStatesByName(from).get(0));
        SwingUtil.fetchChildNamed(panel, "addButton", JButton.class).doClick();
        SwingUtil.fetchChildNamed(dialog, "afterStateComboBox", JComboBox.class).setSelectedItem(model.getStatesByName(to).get(0));
        SwingUtil.fetchChildNamed(dialog, "acceptButton", JButton.class).doClick();
    }
}
