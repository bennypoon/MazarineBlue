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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static java.awt.EventQueue.invokeLater;
import java.awt.Point;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import static javax.swing.SwingUtilities.convertPointFromScreen;
import static javax.swing.SwingUtilities.convertPointToScreen;
import javax.swing.table.TableColumnModel;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mazarineblue.mbt.gui.StringConstants.BUSINESS_VALUE_MAX;
import org.mazarineblue.mbt.gui.model.GraphModel;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;
import org.mazarineblue.mbt.gui.pages.ModelEditorPage;
import org.mazarineblue.mbt.gui.pages.StatePage;
import org.mazarineblue.mbt.gui.pages.TransitionPage;

@RunWith(HierarchicalContextRunner.class)
public class ModelEditorTest {

    private static final String FIRST_HEADER = "Transitions";
    private static final String NA = "N/A";

    private static final String VIEW1 = "Test View1";
    private static final String VIEW2 = "Test View2";
    private static final String NAME_STATE_A = "State A";
    private static final String NAME_STATE_B = "State B";
    private static final String NAME_STATE_C = "State C";
    private static final String NAME_TRANSITION1 = "Transition 1";
    private static final String NAME_TRANSITION2 = "Transition 2";

    private GraphModel model;
    private ModelEditorFrame frame;
    private ModelEditorPage mainPage;

    private static void assertTable(JTable table, Object[][] expected) {
        assertEquals(expected.length - 1, table.getRowCount());
        assertEquals(expected[0].length, table.getColumnCount());
        for (int row = 0; row < expected.length; ++row)
            for (int column = 0; column < expected[row].length; ++column)
                assertEquals(expected[row][column], getValue(table, row, column));
    }
    
    private static Object getValue(JTable table, int row, int column) {
        if (row == 0)
            return table.getColumn(table.getColumnName(column)).getHeaderValue();
        return table.getValueAt(row - 1, column).toString();
    }

    @Before
    public void setup() {
        model = GraphModel.createDefault();
        frame = new ModelEditorFrame(model);
        mainPage = new ModelEditorPage(frame);
    }

    @After
    public void teardown() {
        frame.dispose();
        frame = null;
        model = null;
        mainPage = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class EmptyModel {

        @Test
        public void emptyModel() {
            assertEquals(0, mainPage.viewComboBox.getItemCount());
            assertEquals(-1, mainPage.viewComboBox.getSelectedIndex());
            assertEquals(null, mainPage.viewComboBox.getSelectedItem());
            assertTable(mainPage.table, new Object[][]{{FIRST_HEADER}});
        }

        @Test
        public void addNewState() {
            addNewState(NAME_STATE_A, VIEW1);

            assertEquals(VIEW1, mainPage.viewComboBox.getSelectedItem());
            assertTable(mainPage.table, new Object[][]{{FIRST_HEADER, NAME_STATE_A}});
        }

        @Test
        public void addNewTransition() {
            addNewState(NAME_STATE_A, VIEW1);
            addNewState(NAME_STATE_B, VIEW1);
            addNewTransition(NAME_TRANSITION1, NAME_STATE_A, NAME_STATE_B);

            assertEquals(VIEW1, mainPage.viewComboBox.getSelectedItem());
            assertTable(mainPage.table, new Object[][]{
                {FIRST_HEADER, NAME_STATE_A, NAME_STATE_B},
                {NAME_TRANSITION1, NAME_STATE_B, NA}
            });
        }

        private void addNewState(String nameStateA, String view) {
            StatePage stateA = mainPage.addState();
            stateA.nameTextField.setText(nameStateA);
            stateA.viewComboBox.setSelectedItem(view);
            stateA.addViewButton.doClick();
            stateA.actionTextArea.setText("Test Action");
            stateA.acceptButton.doClick();
        }

        private void addNewTransition(String nameTransition, String source, String destination) {
            TransitionPage transitionPage = mainPage.addTransition();
            transitionPage.nameTextField.setText(nameTransition);
            transitionPage.businessValueSlider.setValue(BUSINESS_VALUE_MAX);
            transitionPage.beforeStateComboBox.setSelectedItem(model.getStatesByName(source).get(0));
            transitionPage.beforeStateAddButton.doClick();
            transitionPage.afterStateComboBox.setSelectedItem(model.getStatesByName(destination).get(0));
            transitionPage.acceptButton.doClick();
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class View {

        @Test
        public void modelWithState() {
            model.addState(new State(NAME_STATE_A).addViews(VIEW1));

            assertEquals(1, mainPage.viewComboBox.getItemCount());
            assertEquals(0, mainPage.viewComboBox.getSelectedIndex());
            assertEquals(VIEW1, mainPage.viewComboBox.getSelectedItem());
            assertTable(mainPage.table, new Object[][]{{FIRST_HEADER, NAME_STATE_A}});
        }

        @Test
        public void modelWithStateAndTransitions_WithIdenticalViews() {
            State stateA = new State(NAME_STATE_A).addViews(VIEW1);
            State stateB = new State(NAME_STATE_B).addViews(VIEW1);
            State stateC = new State(NAME_STATE_C).addViews(VIEW1);
            model.addState(stateA, stateB, stateC);
            model.addTransition(new Transition(NAME_TRANSITION1).setSources(stateA, stateB).setDestination(stateC));

            assertEquals(1, mainPage.viewComboBox.getItemCount());
            assertEquals(0, mainPage.viewComboBox.getSelectedIndex());
            assertEquals(VIEW1, mainPage.viewComboBox.getSelectedItem());
            assertTable(mainPage.table, new Object[][]{
                {FIRST_HEADER, NAME_STATE_A, NAME_STATE_B, NAME_STATE_C},
                {NAME_TRANSITION1, NAME_STATE_C, NAME_STATE_C, NA}
            });
        }

        @Test
        public void modelWithTwoViews_AndSwitchToDifferentView() {
            State stateA = new State(NAME_STATE_A).addViews(VIEW1);
            State stateB = new State(NAME_STATE_B).addViews(VIEW1).addViews(VIEW2);
            State stateC = new State(NAME_STATE_C).addViews(VIEW2);
            model.addState(stateA, stateB, stateC);
            model.addTransition(new Transition(NAME_TRANSITION1).setSources(stateA).setDestination(stateB));
            model.addTransition(new Transition(NAME_TRANSITION2).setSources(stateB).setDestination(stateC));
            mainPage.viewComboBox.setSelectedItem(VIEW2);

            assertEquals(2, mainPage.viewComboBox.getItemCount());
            assertEquals(1, mainPage.viewComboBox.getSelectedIndex());
            assertEquals(VIEW2, mainPage.viewComboBox.getSelectedItem());
            assertTable(mainPage.table, new Object[][]{
                {FIRST_HEADER, NAME_STATE_B, NAME_STATE_C},
                {NAME_TRANSITION2, NAME_STATE_C, NA}
            });
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class InitializedModel {

        @Before
        public void setup() {
            State stateA = new State(NAME_STATE_A).addViews(VIEW1);
            State stateB = new State(NAME_STATE_B).addViews(VIEW1);
            State stateC = new State(NAME_STATE_C).addViews(VIEW1);
            Transition t = new Transition("Transition 1").setSources(stateA).setDestination(stateB);
            model.addState(stateA, stateB, stateC);
            model.addTransition(t);
        }

        @Test
        public void addTransition() {
            invokeLater(() -> frame.setVisible(true));
            
            JPopupMenu popupMenu = mainPage.table.getComponentPopupMenu();
            popupMenu.setLocation(convertTableCoordinates(mainPage.table, 0, 0));
            Point location = popupMenu.getLocation();
            popupMenu.setVisible(true);
            
            Point p1 = convertTableCoordinates(mainPage.table, -1, 0); // Transitions
            Point p2 = convertTableCoordinates(mainPage.table, -1, 1); // StateA
            Point p3 = convertTableCoordinates(mainPage.table, -1, 2); // StateB
            Point p4 = convertTableCoordinates(mainPage.table, -1, 3); // StateC
            Point p5 = convertTableCoordinates(mainPage.table, 0, 0); // Transition 1
            Point p6 = convertTableCoordinates(mainPage.table, 0, 1); // StateB
            Point p7 = convertTableCoordinates(mainPage.table, 0, 2); // N/A
            Point p8 = convertTableCoordinates(mainPage.table, 0, 3); // N/A
            fail();
        }
        
        @Test
        public void editTransition() {
            invokeLater(() -> frame.setVisible(true));
        }
        
        @Test
        public void removeTransition() {
            invokeLater(() -> frame.setVisible(true));
        }

        private Point convertTableCoordinates(JTable table, int row, int column) {
            int x = 0;
            int y = row * table.getRowHeight();
            TableColumnModel columnModel = table.getColumnModel();
            for (int i = 0; i < column; ++i)
                x += columnModel.getColumn(i).getWidth();
            Point p = new Point(x + 5, y + 5);
            convertPointToScreen(p, table);
            convertPointFromScreen(p, mainPage.table.getComponentPopupMenu());
            return p;
        }
    }
}
