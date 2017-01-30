/*
 * AddWordsToDocumentDialog.java
 *
 * Created on 26-may-2011, 19:12:26
 */
package scimat.gui.components.adddialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.add.AddWordsToDocumentEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericDynamicItemsListPanel;
import scimat.gui.components.itemslist.GenericSelectManyItemsPanel;
import scimat.gui.components.tablemodel.WordsTableModel;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.Word;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddWordsToDocumentDialog extends GenericAddItemsDialog<Document, Word> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  public AddWordsToDocumentDialog(JFrame frame) {
    super(frame, 
          new GenericSelectManyItemsPanel<Word>(new GenericDynamicItemsListPanel<Word>(new WordsTableModel()), 
                                                    new GenericDynamicItemsListPanel<Word>(new WordsTableModel())));
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
  public void addAction(Document masterItem, ArrayList<Word> itemsToAdd) {
    
    (new PerformKnowledgeBaseEditTask(new AddWordsToDocumentEdit(masterItem, itemsToAdd), rootPane)).execute();
    
    dispose();
  }

  /**
   * 
   */
  @Override
  public void addNewItemAction() {
    AddDialogManager.getInstance().showAddWordDialog();
  }

  /**
   * 
   * @param enabled 
   */
  @Override
  public void setEntityObserver(boolean enabled) {
    
    if (enabled) {
    
      CurrentProject.getInstance().getKbObserver().addWordObserver(this);
      
    } else {
    
      CurrentProject.getInstance().getKbObserver().removeWordObserver(this);
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
