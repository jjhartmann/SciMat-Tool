/*
 * AddAffiliationsToDocumentDialog.java
 *
 * Created on 26-may-2011, 19:12:26
 */
package scimat.gui.components.adddialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.add.AddAffiliationsToDocumentEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericDynamicItemsListPanel;
import scimat.gui.components.itemslist.GenericSelectManyItemsPanel;
import scimat.gui.components.tablemodel.AffiliationsTableModel;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Document;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddAffiliationsToDocumentDialog extends GenericAddItemsDialog<Document, Affiliation> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   * @param frame 
   */
  public AddAffiliationsToDocumentDialog(JFrame frame) {
    super(frame, 
          new GenericSelectManyItemsPanel<Affiliation>(new GenericDynamicItemsListPanel<Affiliation>(new AffiliationsTableModel()), 
                                                           new GenericDynamicItemsListPanel<Affiliation>(new AffiliationsTableModel())));
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
  public void addAction(Document masterItem, ArrayList<Affiliation> itemsToAdd) {
    
    (new PerformKnowledgeBaseEditTask(new AddAffiliationsToDocumentEdit(masterItem, itemsToAdd), rootPane)).execute();
        
    dispose();
  }

  /**
   * 
   */
  @Override
  public void addNewItemAction() {
    AddDialogManager.getInstance().showAddAffiliationDialog();
  }

  /**
   * 
   * @param enabled 
   */
  @Override
  public void setEntityObserver(boolean enabled) {
    
    if (enabled) {
    
      CurrentProject.getInstance().getKbObserver().addAffiliationObserver(this);
      
    } else {
    
      CurrentProject.getInstance().getKbObserver().removeAffiliationObserver(this);
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
