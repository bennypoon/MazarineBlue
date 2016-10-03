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
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.swing.JTable;
import static javax.swing.SwingUtilities.convertPointToScreen;
import javax.swing.table.TableColumnModel;
import org.junit.After;
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
import static org.mazarineblue.swing.SwingUtil.waitUntilFalse;
import static org.mazarineblue.swing.SwingUtil.waitUntilTrue;
import static java.awt.EventQueue.invokeAndWait;
import static org.junit.Assert.assertEquals;

@RunWith(HierarchicalContextRunner.class)
public class ModelEditorTest {

    private static final int TIMEOUT = 1000;
    private static final String FIRST_HEADER = "Transitions";
    private static final String NA = "N/A";

    private static final String VIEW1 = "Test View1";
    private static final String VIEW2 = "Test View2";
    private static final String NAME_STATE_A = "State A";
    private static final String NAME_STATE_B = "State B";
    private static final String NAME_STATE_C = "State C";
    private static final String NAME_STATE_D = "State D";
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
    public class View {

        @Test
        public void modelWithState() {
            model.addState(State.createDefault(NAME_STATE_A).addViews(VIEW1));

            assertEquals(1, mainPage.viewComboBox.getItemCount());
            assertEquals(0, mainPage.viewComboBox.getSelectedIndex());
            assertEquals(VIEW1, mainPage.viewComboBox.getSelectedItem());
            assertTable(mainPage.table, new Object[][]{{FIRST_HEADER, NAME_STATE_A}});
        }

        @Test
        public void modelWithStateAndTransitions_WithIdenticalViews() {
            State stateA = State.createDefault(NAME_STATE_A).addViews(VIEW1);
            State stateB = State.createDefault(NAME_STATE_B).addViews(VIEW1);
            State stateC = State.createDefault(NAME_STATE_C).addViews(VIEW1);
            model.addState(stateA, stateB, stateC);
            model.addTransition(Transition.createDefault(NAME_TRANSITION1).setSources(stateA, stateB).setDestination(stateC));

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
            State stateA = State.createDefault(NAME_STATE_A).addViews(VIEW1);
            State stateB = State.createDefault(NAME_STATE_B).addViews(VIEW1).addViews(VIEW2);
            State stateC = State.createDefault(NAME_STATE_C).addViews(VIEW2);
            model.addState(stateA, stateB, stateC);
            model.addTransition(Transition.createDefault(NAME_TRANSITION1).setSources(stateA).setDestination(stateB));
            model.addTransition(Transition.createDefault(NAME_TRANSITION2).setSources(stateB).setDestination(stateC));
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
    public class InitializedModel {

        @Before
        public void setup() {
            State stateA = State.createDefault(NAME_STATE_A).addViews(VIEW1);
            State stateB = State.createDefault(NAME_STATE_B).addViews(VIEW1);
            State stateC = State.createDefault(NAME_STATE_C).addViews(VIEW1);
            Transition t = Transition.createDefault("Transition 1").setSources(stateA).setDestination(stateB);
            model.addState(stateA, stateB, stateC);
            model.addTransition(t);
        }

        @Test
        public void popupMenu_FirstHeader()
                throws InterruptedException, InvocationTargetException, TimeoutException {
            mainPage.popupMenu.setLocation(convertTableCoordinates(mainPage.table, -1, 0));
            invokeAndWait(() -> mainPage.popupMenu.setVisible(true));
            waitUntilFalse(() -> mainPage.popupMenu.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.addMenuItem.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.editMenuItem.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.removeMenuItem.isVisible(), TIMEOUT);
        }

        @Test
        public void popupMenu_UsedHeaderStateAsSource()
                throws InterruptedException, InvocationTargetException, TimeoutException {
            mainPage.popupMenu.setLocation(convertTableCoordinates(mainPage.table, -1, 1));
            invokeAndWait(() -> mainPage.popupMenu.setVisible(true));

            waitUntilTrue(() -> mainPage.popupMenu.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.addMenuItem.isVisible(), TIMEOUT);
            waitUntilTrue(() -> mainPage.editMenuItem.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.removeMenuItem.isVisible(), TIMEOUT);
        }

        @Test
        public void popupMenu_UsedHeaderStateAsDestination()
                throws InterruptedException, InvocationTargetException, TimeoutException {
            mainPage.popupMenu.setLocation(convertTableCoordinates(mainPage.table, -1, 2));
            invokeAndWait(() -> mainPage.popupMenu.setVisible(true));

            waitUntilTrue(() -> mainPage.popupMenu.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.addMenuItem.isVisible(), TIMEOUT);
            waitUntilTrue(() -> mainPage.editMenuItem.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.removeMenuItem.isVisible(), TIMEOUT);
        }

        @Test
        public void popupMenu_UnusedHeaderState()
                throws InterruptedException, InvocationTargetException, TimeoutException {
            mainPage.popupMenu.setLocation(convertTableCoordinates(mainPage.table, -1, 3));
            invokeAndWait(() -> mainPage.popupMenu.setVisible(true));

            waitUntilTrue(() -> mainPage.popupMenu.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.addMenuItem.isVisible(), TIMEOUT);
            waitUntilTrue(() -> mainPage.editMenuItem.isVisible(), TIMEOUT);
            waitUntilTrue(() -> mainPage.removeMenuItem.isVisible(), TIMEOUT);
        }

        @Test
        public void popupMenu_Transition()
                throws InterruptedException, InvocationTargetException, TimeoutException {
            mainPage.popupMenu.setLocation(convertTableCoordinates(mainPage.table, 0, 0));
            invokeAndWait(() -> mainPage.popupMenu.setVisible(true));

            waitUntilTrue(() -> mainPage.popupMenu.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.addMenuItem.isVisible(), TIMEOUT);
            waitUntilTrue(() -> mainPage.editMenuItem.isVisible(), TIMEOUT);
            waitUntilTrue(() -> mainPage.removeMenuItem.isVisible(), TIMEOUT);
        }

        @Test
        public void popupMenu_DestinationState()
                throws InterruptedException, InvocationTargetException, TimeoutException {
            mainPage.popupMenu.setLocation(convertTableCoordinates(mainPage.table, 0, 1));
            invokeAndWait(() -> mainPage.popupMenu.setVisible(true));

            waitUntilTrue(() -> mainPage.popupMenu.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.addMenuItem.isVisible(), TIMEOUT);
            waitUntilTrue(() -> mainPage.editMenuItem.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.removeMenuItem.isVisible(), TIMEOUT);
        }

        @Test
        public void popupMenu_EmptySlot()
                throws InterruptedException, InvocationTargetException, TimeoutException {
            mainPage.popupMenu.setLocation(convertTableCoordinates(mainPage.table, 0, 2));
            invokeAndWait(() -> mainPage.popupMenu.setVisible(true));

            waitUntilTrue(() -> mainPage.popupMenu.isVisible(), TIMEOUT);
            waitUntilTrue(() -> mainPage.addMenuItem.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.editMenuItem.isVisible(), TIMEOUT);
            waitUntilFalse(() -> mainPage.removeMenuItem.isVisible(), TIMEOUT);
        }

        private Point convertTableCoordinates(JTable table, int row, int column) {
            Point p = new Point(5, 5);
            TableColumnModel columnModel = table.getColumnModel();
            for (int i = 0; i < column; ++i)
                p.x += columnModel.getColumn(i).getWidth();
            p.y += row * table.getRowHeight();
            convertPointToScreen(p, table);
            return p;
        }

        @Test
        public void editState()
                throws InterruptedException, InvocationTargetException {
            mainPage.popupMenu.setLocation(convertTableCoordinates(mainPage.table, -1, 2));
            invokeAndWait(() -> mainPage.popupMenu.setVisible(true));

            List<State> states = model.getStatesByName(NAME_STATE_B);
            StatePage page = mainPage.editStage();
            page.nameTextField.setText(NAME_STATE_D);
            page.acceptButton.doClick();

            assertEquals(0, model.getStatesByName(NAME_STATE_B).size());
            assertEquals(1, model.getStatesByName(NAME_STATE_D).size());
            assertEquals(states, model.getStatesByName(NAME_STATE_D));
            assertTable(mainPage.table, new Object[][]{
                {FIRST_HEADER, NAME_STATE_A, NAME_STATE_D, NAME_STATE_C},
                {NAME_TRANSITION1, NAME_STATE_D, NA, NA}
            });
        }
        
        @Test
        public void removeState()
                throws InterruptedException, InvocationTargetException {
            mainPage.popupMenu.setLocation(convertTableCoordinates(mainPage.table, -1, 3));
            invokeAndWait(() -> mainPage.popupMenu.setVisible(true));

            StatePage page = mainPage.removeStage();
            page.acceptButton.doClick();

            assertEquals(0, model.getStatesByName(NAME_STATE_C).size());
            assertTable(mainPage.table, new Object[][]{
                {FIRST_HEADER, NAME_STATE_A, NAME_STATE_B},
                {NAME_TRANSITION1, NAME_STATE_B, NA}
            });
        }

        @Test
        public void editTransition()
                throws InterruptedException, InvocationTargetException {
            mainPage.popupMenu.setLocation(convertTableCoordinates(mainPage.table, 0, 0));
            invokeAndWait(() -> mainPage.popupMenu.setVisible(true));

            List<Transition> transitions = model.getTransitionsByName(NAME_TRANSITION1);
            TransitionPage page = mainPage.editTransition();
            page.nameTextField.setText(NAME_TRANSITION2);
            page.acceptButton.doClick();

            assertEquals(0, model.getTransitionsByName(NAME_TRANSITION1).size());
            assertEquals(1, model.getTransitionsByName(NAME_TRANSITION2).size());
            assertEquals(transitions, model.getTransitionsByName(NAME_TRANSITION2));
            assertTable(mainPage.table, new Object[][]{
                {FIRST_HEADER, NAME_STATE_A, NAME_STATE_B, NAME_STATE_C},
                {NAME_TRANSITION2, NAME_STATE_B, NA, NA}
            });
        }

        @Test
        public void removeTransition()
                throws InterruptedException, InvocationTargetException {
            mainPage.popupMenu.setLocation(convertTableCoordinates(mainPage.table, 0, 0));
            invokeAndWait(() -> mainPage.popupMenu.setVisible(true));

            TransitionPage page = mainPage.removeTransition();
            page.acceptButton.doClick();

            assertEquals(0, model.getTransitionsByName(NAME_TRANSITION1).size());
            assertTable(mainPage.table, new Object[][]{
                {FIRST_HEADER, NAME_STATE_A, NAME_STATE_B, NAME_STATE_C}
            });
        }

        @Test
        public void addStateSource()
                throws InterruptedException, InvocationTargetException {
            invokeAndWait(() -> mainPage.frame.setVisible(true));

            mainPage.popupMenu.setLocation(convertTableCoordinates(mainPage.table, 0, 2));
            invokeAndWait(() -> mainPage.popupMenu.setVisible(true));

            List<Transition> transitions = model.getTransitionsByName(NAME_TRANSITION1);
            TransitionPage page = mainPage.addStateSource();
            page.acceptButton.doClick();

            assertEquals(transitions, model.getTransitionsByName(NAME_TRANSITION1));
            assertTable(mainPage.table, new Object[][]{
                {FIRST_HEADER, NAME_STATE_A, NAME_STATE_B, NAME_STATE_C},
                {NAME_TRANSITION1, NAME_STATE_B, NAME_STATE_B, NA}
            });
        }
    }
}
