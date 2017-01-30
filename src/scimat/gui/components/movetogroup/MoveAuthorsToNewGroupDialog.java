/*
 * MoveAuthorsToNewGroupDialog.java
 *
 * Created on 25-may-2011, 17:12:02
 */
package scimat.gui.components.movetogroup;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.move.MoveAuthorsToNewAuthorGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.AuthorsTableModel;
import scimat.model.knowledgebase.entity.Author;

/**
 *
 * @author mjcobo
 */
public class MoveAuthorsToNewGroupDialog extends GenericMoveToGroupDialog<Author> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public MoveAuthorsToNewGroupDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<Author>(new AuthorsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param item 
   */
  @Override
  public void setGroupNameFromItem(Author item) {
    
    if (item != null) {
    
      super.setGroupNameText(item.getAuthorName());
      
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
  public void moveAction(ArrayList<Author> items, String groupName) {
    
    (new PerformKnowledgeBaseEditTask(new MoveAuthorsToNewAuthorGroupEdit(items, groupName), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
