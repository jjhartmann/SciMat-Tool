/*
 * AddPublishDateToDocumentDialog.java
 *
 * Created on 26-may-2011, 23:52:40
 */
package scimat.gui.components.adddialog;

import javax.swing.JFrame;
import scimat.gui.commands.edit.add.AddPublishDateToDocumentEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.detailspanel.PublishDateDetailPanel;
import scimat.gui.components.itemslist.GenericDynamicItemsListPanel;
import scimat.gui.components.itemslist.GenericSelectOneItemPanel;
import scimat.gui.components.tablemodel.PublishDatesTableModel;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddPublishDateToDocumentDialog extends GenericAddItemDialog<Document, PublishDate> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param frame 
   */
  public AddPublishDateToDocumentDialog(JFrame frame) {
    super(frame, 
          new GenericSelectOneItemPanel<PublishDate>(new GenericDynamicItemsListPanel<PublishDate>(new PublishDatesTableModel()), 
                                                                      new PublishDateDetailPanel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   */
  @Override
  public void addAction(Document masterItem, PublishDate itemsToAdd) {
    
    (new PerformKnowledgeBaseEditTask(new AddPublishDateToDocumentEdit(masterItem, itemsToAdd), rootPane)).execute();
    
    dispose();
  }

  /**
   * 
   */
  @Override
  public void addNewItemAction() {
    AddDialogManager.getInstance().showAddPublishDateDialog();
    
  }

  /**
   * 
   * @param enabled 
   */
  @Override
  public void setEntityObserver(boolean enabled) {
    if (enabled) {
    
      CurrentProject.getInstance().getKbObserver().addPublishDateObserver(this);
      
    } else {
    
      CurrentProject.getInstance().getKbObserver().removePublishDateObserver(this);
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
