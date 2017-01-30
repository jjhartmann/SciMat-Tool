/*
 * GenericMoveToGroupDialog.java
 *
 * Created on 24-may-2011, 19:01:50
 */
package scimat.gui.components.movetogroup;

import java.util.ArrayList;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.observer.SelectionObserver;

/**
 *
 * @author mjcobo
 * 
 * @param <G> Group
 */
public abstract class GenericMoveToGroupDialog<E> 
extends javax.swing.JDialog implements SelectionObserver {

  /** Creates new form GenericMoveToGroupDialog */
  public GenericMoveToGroupDialog(java.awt.Frame parent, GenericItemsListPanel<E> itemsPanelList) {
    super(parent, true);
    
    this.itemsPanelList = itemsPanelList;
    
    initComponents();
    
    this.itemsPanelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    this.groupNameTextField.getDocument().addDocumentListener(new DocumentListener() {

      public void insertUpdate(DocumentEvent e) {
        enableMoveButton();
      }

      public void removeUpdate(DocumentEvent e) {
        enableMoveButton();
      }

      public void changedUpdate(DocumentEvent e) {
        enableMoveButton();
      }
    });
    
    this.itemsPanelList.addSelectionObserver(this);
    
    this.itemsPanel.add(this.itemsPanelList);
  }
  
  /**
   * 
   */
  public void reset() {
  
    this.groupNameTextField.setText("");
    this.moveButton.setEnabled(false);
    this.itemsPanelList.refreshItems(new ArrayList<E>());
  }
  
  /**
   * 
   * @param items 
   */
  public void refreshData(ArrayList<E> items) {
  
    this.itemsPanelList.refreshItems(items);
  }

  /**
   * 
   * @param selection 
   */
  public void selectionChangeHappened(int[] selection) {
    
    if (selection.length == 1) {
      
      setGroupNameFromItem(this.itemsPanelList.getSelectedItems().get(0));
      
    } else {
    
      setGroupNameFromItem(null);
    }
  }
  
  public void setGroupNameText(String groupName) {
  
    this.groupNameTextField.setText(groupName);
  }
  
  /**
   * 
   */
  private void enableMoveButton() {
    
    this.moveButton.setEnabled(! this.groupNameTextField.getText().isEmpty());
  }
  
  public abstract void setGroupNameFromItem(E item);
  
  public abstract void moveAction(ArrayList<E> items, String groupName);

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    groupNameDescriptionLabel = new javax.swing.JLabel();
    groupNameTextField = new javax.swing.JTextField();
    itemsPanel = new javax.swing.JPanel();
    jSeparator1 = new javax.swing.JSeparator();
    moveButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Move to group");

    groupNameDescriptionLabel.setText("Group name:");

    itemsPanel.setLayout(new javax.swing.BoxLayout(itemsPanel, javax.swing.BoxLayout.LINE_AXIS));

    moveButton.setText("Move");
    moveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        moveButtonActionPerformed(evt);
      }
    });

    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(itemsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
          .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .add(groupNameDescriptionLabel)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(groupNameTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
          .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
          .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .add(moveButton)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(cancelButton)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(groupNameDescriptionLabel)
          .add(groupNameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(itemsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(cancelButton)
          .add(moveButton))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  /**
   * 
   * @param evt 
   */
  private void moveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveButtonActionPerformed
    
   moveAction(this.itemsPanelList.getItems(), this.groupNameTextField.getText());
    
    dispose();
  }//GEN-LAST:event_moveButtonActionPerformed

  /**
   * 
   * @param evt 
   */
  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    dispose();
  }//GEN-LAST:event_cancelButtonActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton cancelButton;
  private javax.swing.JLabel groupNameDescriptionLabel;
  private javax.swing.JTextField groupNameTextField;
  private javax.swing.JPanel itemsPanel;
  private javax.swing.JSeparator jSeparator1;
  private javax.swing.JButton moveButton;
  // End of variables declaration//GEN-END:variables
  private GenericItemsListPanel<E> itemsPanelList;
}
