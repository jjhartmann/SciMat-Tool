/*
 * AddJournalToDocumentDialog.java
 *
 * Created on 26-may-2011, 23:52:40
 */
package scimat.gui.components.adddialog;

import javax.swing.JFrame;
import scimat.gui.commands.edit.add.AddJournalToDocumentEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.detailspanel.JournalDetailPanel;
import scimat.gui.components.itemslist.GenericDynamicItemsListPanel;
import scimat.gui.components.itemslist.GenericSelectOneItemPanel;
import scimat.gui.components.tablemodel.JournalsTableModel;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.Journal;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddJournalToDocumentDialog extends GenericAddItemDialog<Document, Journal> {

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
  public AddJournalToDocumentDialog(JFrame frame) {
    super(frame,
          new GenericSelectOneItemPanel<Journal>(new GenericDynamicItemsListPanel<Journal>(new JournalsTableModel()), 
                                                              new JournalDetailPanel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   */
  @Override
  public void addAction(Document masterItem, Journal itemsToAdd) {
    
    (new PerformKnowledgeBaseEditTask(new AddJournalToDocumentEdit(masterItem, itemsToAdd), rootPane)).execute();
    
    dispose();
  }

  /**
   * 
   */
  @Override
  public void addNewItemAction() {
    AddDialogManager.getInstance().showAddJournalDialog();
    
  }

  /**
   * 
   * @param enabled 
   */
  @Override
  public void setEntityObserver(boolean enabled) {
    
    if (enabled) {
    
      CurrentProject.getInstance().getKbObserver().addJournalObserver(this);
      
    } else {
    
      CurrentProject.getInstance().getKbObserver().removeJournalObserver(this);
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
