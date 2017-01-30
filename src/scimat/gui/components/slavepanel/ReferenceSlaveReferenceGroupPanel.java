/*
 * ReferenceSlaveReferenceGroupPanel.java
 *
 * Created on 21-mar-2011, 11:34:59
 */
package scimat.gui.components.slavepanel;

import java.util.ArrayList;
import scimat.gui.components.ErrorDialogManager;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;
import scimat.project.observer.EntityObserver;
import scimat.project.observer.ReferenceGroupRelationReferenceObserver;

/**
 *
 * @author mjcobo
 */
public class ReferenceSlaveReferenceGroupPanel extends GenericOneSlaveItemPanel
        implements ReferenceGroupRelationReferenceObserver, EntityObserver<ReferenceGroup> {

  /** Creates new form WordSlaveWordGroupPanel */
  public ReferenceSlaveReferenceGroupPanel() {
    initComponents();

    CurrentProject.getInstance().getKbObserver().addReferenceGroupRelationReferencesObserver(this);
    CurrentProject.getInstance().getKbObserver().addReferenceGroupObserver(this);
  }

  /**
   *
   */
  private void refresh() {

    if (this.referenceGroup != null) {

      this.groupNameTextField.setText(this.referenceGroup.getGroupName());
      this.stopGroupCheckBox.setSelected(this.referenceGroup.isStopGroup());
      fireSlaveItemObserver(true);

    } else {

      this.groupNameTextField.setText("");
      this.stopGroupCheckBox.setSelected(false);
    }
  }

  /**
   *
   */
  public void setMasterItem(Reference reference) {

    this.reference = reference;

    try {

      if (this.reference != null) {

        relationChanged();

      } else {

        this.referenceGroup = null;
        refresh();

      }

    } catch (KnowledgeBaseException e) {
    
      ErrorDialogManager.getInstance().showException(e);

    }
  }

  /**
   *
   */
  public void relationChanged() throws KnowledgeBaseException {

    if (this.reference != null) {
      
      this.referenceGroup = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().getReferenceGroup(this.reference.getReferenceID());
      
    } else {
    
      this.referenceGroup = null;
    }
    
    refresh();
  }

  /**
   *
   * @param items
   * @throws KnowledgeBaseException
   */
  public void entityAdded(ArrayList<ReferenceGroup> items) throws KnowledgeBaseException {
    // Do not do nothing
  }

  /**
   *
   * @param entity
   * @throws KnowledgeBaseException
   */
  public void entityRefresh() throws KnowledgeBaseException {

    if (this.referenceGroup != null) {

      this.referenceGroup = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().getReferenceGroup(this.referenceGroup.getReferenceGroupID());
      refresh();
    }
  }

  /**
   *
   * @param items
   * @throws KnowledgeBaseException
   */
  public void entityRemoved(ArrayList<ReferenceGroup> items) throws KnowledgeBaseException {
    // Do not do nothing
  }

  /**
   *
   * @param items
   * @throws KnowledgeBaseException
   */
  public void entityUpdated(ArrayList<ReferenceGroup> items) throws KnowledgeBaseException {

    int position;

    if (this.referenceGroup != null) {

      position = items.indexOf(this.referenceGroup);

      if (position != -1) {

        this.referenceGroup = items.get(position);
      }
    }
  }

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
    stopGroupCheckBox = new javax.swing.JCheckBox();

    groupNameDescriptionLabel.setText("Group name:");

    groupNameTextField.setEditable(false);

    stopGroupCheckBox.setText("is stop group?");
    stopGroupCheckBox.setEnabled(false);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(groupNameDescriptionLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(groupNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
      .addGroup(layout.createSequentialGroup()
        .addComponent(stopGroupCheckBox)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(groupNameDescriptionLabel)
          .addComponent(groupNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(stopGroupCheckBox))
    );
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel groupNameDescriptionLabel;
  private javax.swing.JTextField groupNameTextField;
  private javax.swing.JCheckBox stopGroupCheckBox;
  // End of variables declaration//GEN-END:variables
  private Reference reference = null;
  private ReferenceGroup referenceGroup = null;
}
