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
package org.mazarineblue.mbt.gui.pages;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.mazarineblue.mbt.gui.TransitionDialog;
import org.mazarineblue.mbt.gui.list.JListPanel;
import org.mazarineblue.mbt.gui.model.State;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;

public class TransitionPage
        implements AutoCloseable {

    public final TransitionDialog dialog;
    public final JTextField nameTextField;
    public final JLabel nameValidationLabel;
    public final JTextField guardTextField;
    public final JLabel guardValidationLabel;
    public final JSlider businessValueSlider;
    public final JListPanel<State> beforeStatesPanel;
    public final JComboBox<?> beforeStateComboBox;
    public final JButton beforeStateAddButton;
    public final JLabel beforeStateValidationLabel;
    public final JComboBox<?> afterStateComboBox;
    public final JLabel afterStateValidationLabel;
    public final JTextArea actionTextArea;
    public final JButton acceptButton;
    public final JButton rejectButton;

    public TransitionPage(TransitionDialog dialog) {
        this.dialog = dialog;

        nameTextField = fetchChildNamed(dialog, "nameTextField", JTextField.class);
        nameValidationLabel = fetchChildNamed(dialog, "nameValidationLabel", JLabel.class);
        guardTextField = fetchChildNamed(dialog, "guardTextField", JTextField.class);
        guardValidationLabel = fetchChildNamed(dialog, "guardValidationLabel", JLabel.class);
        businessValueSlider = fetchChildNamed(dialog, "businessValueSlider", JSlider.class);

        beforeStatesPanel = fetchChildNamed(dialog, "beforeStateListPanel", JListPanel.class);
        beforeStateComboBox = fetchChildNamed(beforeStatesPanel, "optionsComboBox", JComboBox.class);
        beforeStateAddButton = fetchChildNamed(beforeStatesPanel, "addButton", JButton.class);
        beforeStateValidationLabel = fetchChildNamed(beforeStatesPanel, "validationLabel", JLabel.class);

        afterStateComboBox = fetchChildNamed(dialog, "afterStateComboBox", JComboBox.class);
        afterStateValidationLabel = fetchChildNamed(dialog, "afterStateValidationLabel", JLabel.class);
        actionTextArea = fetchChildNamed(dialog, "actionTextArea", JTextArea.class);
        acceptButton = fetchChildNamed(dialog, "acceptButton", JButton.class);
        rejectButton = fetchChildNamed(dialog, "rejectButton", JButton.class);
        nameTextField.requestFocus();
    }

    @Override
    public void close()
            throws Exception {
        dialog.dispose();
    }
}
