/*
 * AddReferenceSourceToReferenceDialog.java
 *
 * Created on 26-may-2011, 19:12:26
 */
package scimat.gui.components.adddialog;

import javax.swing.JFrame;
import scimat.gui.commands.edit.add.AddReferenceSourceToReferenceEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.detailspanel.ReferenceSourceDetailPanel;
import scimat.gui.components.itemslist.GenericDynamicItemsListPanel;
import scimat.gui.components.itemslist.GenericSelectOneItemPanel;
import scimat.gui.components.tablemodel.ReferenceSourcesTableModel;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddReferenceSourceToReferenceDialog extends GenericAddItemDialog<Reference, ReferenceSource> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   * @param frame 
   */
  public AddReferenceSourceToReferenceDialog(JFrame frame) {
    super(frame, 
          new GenericSelectOneItemPanel<ReferenceSource>(new GenericDynamicItemsListPanel<ReferenceSource>(new ReferenceSourcesTableModel()), 
                                                                              new ReferenceSourceDetailPanel()));
  }
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param masterItem
   * @param itemsToAdd 
   */
  @Override
  public void addAction(Reference masterItem, ReferenceSource itemsToAdd) {
    
    (new PerformKnowledgeBaseEditTask(new AddReferenceSourceToReferenceEdit(masterItem, itemsToAdd), rootPane)).execute();
    
    dispose();
  }

  /**
   * 
   */
  @Override
  public void addNewItemAction() {
    AddDialogManager.getInstance().showAddReferenceSourceDialog();
    
  }

  /**
   * 
   * @param enabled 
   */
  @Override
  public void setEntityObserver(boolean enabled) {
    if (enabled) {
    
      CurrentProject.getInstance().getKbObserver().addReferenceSourceObserver(this);
      
    } else {
    
      CurrentProject.getInstance().getKbObserver().removeReferenceSourceObserver(this);
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
