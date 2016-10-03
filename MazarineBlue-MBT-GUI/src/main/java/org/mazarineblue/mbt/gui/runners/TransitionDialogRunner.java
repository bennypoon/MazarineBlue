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
package org.mazarineblue.mbt.gui.runners;

import static java.awt.EventQueue.invokeLater;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.logging.Logger.getLogger;
import javax.swing.JFrame;
import org.mazarineblue.mbt.gui.TransitionDialog;
import org.mazarineblue.mbt.gui.model.State;

public class TransitionDialogRunner {

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
            getLogger(TransitionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            getLogger(TransitionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            getLogger(TransitionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            getLogger(TransitionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        Collection<State> states = new ArrayList<>(4);
        states.add(State.createDefault("State A").addViews("View 1", "View 2"));
        states.add(State.createDefault("State B").addViews("View 1"));
        states.add(State.createDefault("State C").addViews("View 2"));
        states.add(State.createDefault("State D").addViews("View 3"));
        states.add(State.createDefault("State E"));

        /* Create and display the dialog */
        TransitionDialog dialog = new TransitionDialog(new JFrame(), "Test Transtion Dialog");
        dialog.setOptions(states);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
        invokeLater(() -> dialog.setVisible(true));
    }

    private TransitionDialogRunner() {
    }
}
