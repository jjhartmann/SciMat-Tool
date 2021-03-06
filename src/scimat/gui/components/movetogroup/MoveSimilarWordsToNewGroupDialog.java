/*
 * MoveSimilarWordsToNewGroupDialog.java
 *
 * Created on 25-may-2011, 17:12:02
 */
package scimat.gui.components.movetogroup;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.move.MoveWordsToNewWordGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericDynamicItemsListPanel;
import scimat.gui.components.tablemodel.WordsTableModel;
import scimat.model.knowledgebase.entity.Word;

/**
 *
 * @author mjcobo
 */
public class MoveSimilarWordsToNewGroupDialog extends GenericMoveSimilarItemToGroupDialog<Word> {

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
  public MoveSimilarWordsToNewGroupDialog(JFrame frame) {
    super(frame, 
          new GenericDynamicItemsListPanel<Word>(new WordsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param item 
   */
  @Override
  public void setGroupNameFromItem(Word item) {
    
    if (item != null) {
    
      super.setGroupNameText(item.getWordName());
      
    } else {
    
      super.setGroupNameText("");
    }
  }

  /**
   * 
   * @param items
   * @param groupName 
   */
  @Override
  public void moveAction(ArrayList<Word> items, String groupName) {
    
    (new PerformKnowledgeBaseEditTask(new MoveWordsToNewWordGroupEdit(items, groupName), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
