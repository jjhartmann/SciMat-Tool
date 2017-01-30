/*
 * AddAuthorReferencesToReferenceDialog.java
 *
 * Created on 26-may-2011, 19:12:26
 */
package scimat.gui.components.adddialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.add.AddAuthorReferencesToReferenceEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericDynamicItemsListPanel;
import scimat.gui.components.itemslist.GenericSelectManyItemsPanel;
import scimat.gui.components.tablemodel.AuthorReferencesTableModel;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.Reference;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddAuthorReferencesToReferenceDialog extends GenericAddItemsDialog<Reference, AuthorReference> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  public AddAuthorReferencesToReferenceDialog(JFrame frame) {
    super(frame, 
          new GenericSelectManyItemsPanel<AuthorReference>(new GenericDynamicItemsListPanel<AuthorReference>(new AuthorReferencesTableModel()), 
                                                               new GenericDynamicItemsListPanel<AuthorReference>(new AuthorReferencesTableModel())));
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
  public void addAction(Reference masterItem, ArrayList<AuthorReference> itemsToAdd) {
    
    (new PerformKnowledgeBaseEditTask(new AddAuthorReferencesToReferenceEdit(masterItem, itemsToAdd), rootPane)).execute();
    
    dispose();
  }

  /**
   * 
   */
  @Override
  public void addNewItemAction() {
    AddDialogManager.getInstance().showAddAuthorReferenceDialog();
  }

  /**
   * 
   * @param enabled 
   */
  @Override
  public void setEntityObserver(boolean enabled) {
    
    if (enabled) {
    
      CurrentProject.getInstance().getKbObserver().addAuthorReferenceObserver(this);
      
    } else {
    
      CurrentProject.getInstance().getKbObserver().removeAuthorReferenceObserver(this);
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
