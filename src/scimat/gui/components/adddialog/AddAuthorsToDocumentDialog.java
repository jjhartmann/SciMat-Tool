/*
 * AddAuthorsToDocumentDialog.java
 *
 * Created on 26-may-2011, 19:12:26
 */
package scimat.gui.components.adddialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.add.AddAuthorsToDocumentEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericDynamicItemsListPanel;
import scimat.gui.components.itemslist.GenericSelectManyItemsPanel;
import scimat.gui.components.tablemodel.AuthorsTableModel;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.Document;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddAuthorsToDocumentDialog extends GenericAddItemsDialog<Document, Author> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  public AddAuthorsToDocumentDialog(JFrame frame) {
    super(frame, 
          new GenericSelectManyItemsPanel<Author>(new GenericDynamicItemsListPanel<Author>(new AuthorsTableModel()), 
                                                      new GenericDynamicItemsListPanel<Author>(new AuthorsTableModel())));
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
  public void addAction(Document masterItem, ArrayList<Author> itemsToAdd) {
    
    (new PerformKnowledgeBaseEditTask(new AddAuthorsToDocumentEdit(masterItem, itemsToAdd), rootPane)).execute();
    
    dispose();
  }

  /**
   * 
   */
  @Override
  public void addNewItemAction() {
    AddDialogManager.getInstance().showAddAuthorDialog();
  }

  /**
   * 
   * @param enabled 
   */
  @Override
  public void setEntityObserver(boolean enabled) {
    
    if (enabled) {
    
      CurrentProject.getInstance().getKbObserver().addAuthorObserver(this);
      
    } else {
    
      CurrentProject.getInstance().getKbObserver().removeAuthorObserver(this);
    }
  }
  
  
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
