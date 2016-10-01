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
package org.mazarineblue.mbt.gui.list;

import java.io.Serializable;
import javax.swing.Action;
import javax.swing.JPanel;

/**
 * A {@code TextItemPanel} is a {@code JPanel} that shows a line of text
 * that is optionally followed by an edit and remove button.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the type of item this panel contains.
 */
class JItemPanel<T extends Serializable>
        extends JPanel {

    private static final long serialVersionUID = 1L;

    private final T obj;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton removeButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Constructs a {@code JItemPanel} to show the specified object connects
     * the remove button to the specified action
     *
     * @param obj          the object to convert to a string using the
     *                     {@code toString()} method.
     * @param removeAction the action to perform when the remove button is
     *                     pushed.
     */
    JItemPanel(T obj, Action removeAction) {
        this.obj = obj;
        initComponents();
        removeButton.setAction(removeAction);
    }

    T getItem() {
        return obj;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel itemLabel = new javax.swing.JLabel();
        removeButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        itemLabel.setText(obj.toString());
        itemLabel.setName("itemLabel"); // NOI18N

        removeButton.setName("removeButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(removeButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeButton)
                    .addComponent(itemLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
}
