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

import static java.awt.EventQueue.invokeLater;
import java.util.Collection;
import java.util.List;
import static java.util.logging.Logger.getLogger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.mazarineblue.mbt.gui.model.GraphModel;
import org.mazarineblue.mbt.gui.model.ModelListener;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;
import org.mazarineblue.mbt.gui.model.ui.JModelEditorPopup;
import org.mazarineblue.mbt.gui.model.ui.TableModelConvertor;

public class ModelEditorFrame
        extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    private static final String NEW_STATE = "New State";
    private static final String NEW_TRANSITION = "New Transition";

    private final GraphModel model;
    private final TableModelConvertor convertor;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane graphScrollPane;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu modelMenu;
    private javax.swing.JTabbedPane modelTabbedPane;
    private javax.swing.JMenuItem newStateMenuItem;
    private javax.swing.JMenuItem newTransitionMenuItem;
    private org.jdesktop.swingx.JXTable stateTransactionTable;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JComboBox<String> viewComboBox;
    private javax.swing.JLabel viewLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            getLogger(ModelEditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            getLogger(ModelEditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            getLogger(ModelEditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            getLogger(ModelEditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        GraphModel model = GraphModel.createDefault();
        State start = new State("Start").addViews("Login");
        State l0 = new State("Level 0").addViews("Login", "Stairs");
        State l1 = new State("Level 1").addViews("Stairs");
        State l2 = new State("Level 2").addViews("Stairs");
        State l3 = new State("Level 3").addViews("Stairs");
        model.addState(start, l0, l1, l2, l3);

        model.addTransition(new Transition("Wrong username").setSources(start).setDestination(start));
        model.addTransition(new Transition("Wrong password").setSources(start).setDestination(start));
        model.addTransition(new Transition("Correct username & password").setSources(start).setDestination(l0));

        model.addTransition(new Transition("To Level 0").setSources(l1).setDestination(l0));
        model.addTransition(new Transition("To Level 1").setSources(l0, l2).setDestination(l1));
        model.addTransition(new Transition("To Level 2").setSources(l1).setDestination(l2));

        ModelEditorFrame frame = new ModelEditorFrame(model);
        invokeLater(() -> frame.setVisible(true));
        Thread.setDefaultUncaughtExceptionHandler((t, ex) -> {
            ExceptionDialog dialog = new ExceptionDialog(frame, ex);
            invokeLater(() -> dialog.setVisible(true));
        });
    }

    /**
     * Creates new form TestingModelEditor
     */
    public ModelEditorFrame(GraphModel model) {
        this.model = model;
        convertor = createConvertor(model);
        initComponents();
        addModelPopup();
    }

    private TableModelConvertor createConvertor(GraphModel model) {
        TableModelConvertor conv = new TableModelConvertor(model);
        model.addModelListener(conv);
        return conv;
    }

    private void addModelPopup() {
        JModelEditorPopup popup = new JModelEditorPopup(this, stateTransactionTable, convertor);
        stateTransactionTable.setComponentPopupMenu(popup);
        stateTransactionTable.getTableHeader().setComponentPopupMenu(popup);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewLabel = new javax.swing.JLabel();
        viewComboBox = new javax.swing.JComboBox<>();
        modelTabbedPane = new javax.swing.JTabbedPane();
        tableScrollPane = new javax.swing.JScrollPane();
        stateTransactionTable = new org.jdesktop.swingx.JXTable();
        graphScrollPane = new javax.swing.JScrollPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        modelMenu = new javax.swing.JMenu();
        newStateMenuItem = new javax.swing.JMenuItem();
        newTransitionMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        viewLabel.setText("View");

        viewComboBox.setModel(convert(model));
        viewComboBox.setMinimumSize(new java.awt.Dimension(100, 20));
        viewComboBox.setName("viewComboBox"); // NOI18N
        viewComboBox.setPreferredSize(new java.awt.Dimension(100, 20));
        viewComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewComboBoxActionPerformed(evt);
            }
        });

        stateTransactionTable.setModel(convertor);
        stateTransactionTable.setName("stateTransactionTable"); // NOI18N
        tableScrollPane.setViewportView(stateTransactionTable);
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        stateTransactionTable.setDefaultRenderer(Object.class, centerRenderer);

        modelTabbedPane.addTab("Table", tableScrollPane);
        modelTabbedPane.addTab("Graph", graphScrollPane);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        modelMenu.setText("Model");

        newStateMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, 0));
        newStateMenuItem.setMnemonic('s');
        newStateMenuItem.setText("New state");
        newStateMenuItem.setName("newStateMenuItem"); // NOI18N
        newStateMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newState(evt);
            }
        });
        modelMenu.add(newStateMenuItem);

        newTransitionMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, 0));
        newTransitionMenuItem.setText("New transition");
        newTransitionMenuItem.setName("newTransitionMenuItem"); // NOI18N
        newTransitionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTransition(evt);
            }
        });
        modelMenu.add(newTransitionMenuItem);

        jMenuBar1.add(modelMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(viewLabel)
                .addGap(18, 18, 18)
                .addComponent(viewComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(modelTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewLabel)
                    .addComponent(viewComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(modelTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //<editor-fold defaultstate="collapsed" desc="Helper methods for Generated Code">
    private ComboBoxModel<String> convert(GraphModel model) {
        Collection<String> views = model.getViews();
        DefaultComboBoxModel<String> swingModel = new DefaultComboBoxModel<>(views.toArray(new String[views.size()]));
        model.addModelListener(new ModelListener() {
            @Override
            public void addedStates(List<State> states) {
                Collection<String> views = model.getViews();
                swingModel.removeAllElements();
                views.stream().forEach(swingModel::addElement);
            }

            @Override
            public void addedTransitions(List<Transition> list) {
            }
        });
        return swingModel;
    }
    //</editor-fold>

    private void viewComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewComboBoxActionPerformed
        convertor.setView((String) viewComboBox.getSelectedItem());
    }//GEN-LAST:event_viewComboBoxActionPerformed

    private void newState(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newState
        StateDialog dialog = new StateDialog(this, NEW_STATE);
        dialog.setAcceptAction((oldState, newState) -> model.addState(newState));
        invokeLater(() -> dialog.setVisible(true));
    }//GEN-LAST:event_newState

    private void newTransition(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTransition
        TransitionDialog dialog = new TransitionDialog(this, NEW_TRANSITION);
        dialog.setAcceptAction((oldTransition, newTransition) -> model.addTransition(newTransition));
        dialog.setOptions(model.getStatesByView((String) viewComboBox.getSelectedItem()));
        invokeLater(() -> dialog.setVisible(true));
    }//GEN-LAST:event_newTransition
}
