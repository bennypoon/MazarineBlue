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

import javax.swing.JComboBox;
import javax.swing.JTable;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.mbt.gui.model.GraphModel;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;
import org.mazarineblue.mbt.gui.util.TestModelHelper;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;

// http://thebadprogrammer.com/swing-uimanager-keys/
// http://stackoverflow.com/questions/1951558/list-of-java-swing-ui-properties/25740576#25740576
//@RunWith(HierarchicalContextRunner.class)
public class ModelEditorTest {

    private static final String FIRST_HEADER = "Transitions";
    private static final String NA = "N/A";

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

    @SuppressWarnings("PublicInnerClass")
    public class View {

        @Test
        public void emptyModel() {
            GraphModel model = GraphModel.createDefault();
            ModelEditorFrame frame = new ModelEditorFrame(model);

            JComboBox<?> viewComboBox = fetchChildNamed(frame, "viewComboBox", JComboBox.class);
            assertEquals(0, viewComboBox.getItemCount());
            assertEquals(-1, viewComboBox.getSelectedIndex());
            assertEquals(null, viewComboBox.getSelectedItem());

            JTable table = fetchChildNamed(frame, "stateTransactionTable", JTable.class);
            assertTable(table, new Object[][]{{FIRST_HEADER}});
        }

        @Test
        public void modelWithState() {
            GraphModel model = GraphModel.createDefault();
            model.addState(new State("state").addViews("view"));
            ModelEditorFrame frame = new ModelEditorFrame(model);

            JComboBox<?> viewComboBox = fetchChildNamed(frame, "viewComboBox", JComboBox.class);
            assertEquals(1, viewComboBox.getItemCount());
            assertEquals(0, viewComboBox.getSelectedIndex());
            assertEquals("view", viewComboBox.getSelectedItem());

            JTable table = fetchChildNamed(frame, "stateTransactionTable", JTable.class);
            assertTable(table, new Object[][]{{FIRST_HEADER, "state"}});
        }

        @Test
        public void modelWithStateAndTransitions_WithIdenticalViews() {
            GraphModel model = GraphModel.createDefault();
            State from1 = new State("from 1").addViews("view");
            State from2 = new State("from 2").addViews("view");
            State to = new State("to").addViews("view");
            model.addState(from1, from2, to);
            model.addTransition(new Transition("transition").setSources(from1, from2).setDestination(to));
            ModelEditorFrame frame = new ModelEditorFrame(model);

            JComboBox<?> viewComboBox = fetchChildNamed(frame, "viewComboBox", JComboBox.class);
            assertEquals(1, viewComboBox.getItemCount());
            assertEquals(0, viewComboBox.getSelectedIndex());
            assertEquals("view", viewComboBox.getSelectedItem());

            JTable table = fetchChildNamed(frame, "stateTransactionTable", JTable.class);
            assertTable(table, new Object[][]{
                {FIRST_HEADER, "from 1", "from 2", "to"},
                {"transition", "to", "to", NA}
            });
        }

        @Test
        public void modelWithTwoViews_AndSwitchToDifferentView() {
            GraphModel model = GraphModel.createDefault();
            State a = new State("a").addViews("view 1");
            State b = new State("b").addViews("view 1").addViews("view 2");
            State c = new State("c").addViews("view 2");
            model.addState(a, b, c);
            model.addTransition(new Transition("transition 1").setSources(a).setDestination(b));
            model.addTransition(new Transition("transition 2").setSources(b).setDestination(c));
            ModelEditorFrame frame = new ModelEditorFrame(model);

            JComboBox<?> viewComboBox = fetchChildNamed(frame, "viewComboBox", JComboBox.class);
            viewComboBox.setSelectedItem("view 2");

            assertEquals(2, viewComboBox.getItemCount());
            assertEquals(1, viewComboBox.getSelectedIndex());
            assertEquals("view 2", viewComboBox.getSelectedItem());

            JTable table = fetchChildNamed(frame, "stateTransactionTable", JTable.class);
            assertTable(table, new Object[][]{
                {FIRST_HEADER, "b", "c"},
                {"transition 2", "c", NA}
            });
        }
    }

    @Test
    public void addNewState() {
        GraphModel model = GraphModel.createDefault();
        TestModelHelper helper = new TestModelHelper(model);
        ModelEditorFrame frame = new ModelEditorFrame(model);

        helper.addState(frame, "Test State", "Test View");

        @SuppressWarnings("unchecked")
        JComboBox<String> viewComboBox = fetchChildNamed(frame, "viewComboBox", JComboBox.class);
        JTable table = fetchChildNamed(frame, "stateTransactionTable", JTable.class);
        assertEquals("Test View", viewComboBox.getSelectedItem());
        assertTable(table, new Object[][]{{FIRST_HEADER, "Test State"}});
    }

    @Test
    public void addNewTransition() {
        GraphModel model = GraphModel.createDefault();
        TestModelHelper helper = new TestModelHelper(model);
        ModelEditorFrame frame = new ModelEditorFrame(model);

        helper.addState(frame, "a", "Test View");
        helper.addState(frame, "b", "Test View");
        helper.addTransition(frame, "Test Transition", "a", "b");

        @SuppressWarnings("unchecked")
        JComboBox<String> viewComboBox = fetchChildNamed(frame, "viewComboBox", JComboBox.class);
        JTable table = fetchChildNamed(frame, "stateTransactionTable", JTable.class);
        assertEquals("Test View", viewComboBox.getSelectedItem());
        assertTable(table, new Object[][]{
            {FIRST_HEADER, "a", "b"},
            {"Test Transition", "b", NA}
        });
    }
}
