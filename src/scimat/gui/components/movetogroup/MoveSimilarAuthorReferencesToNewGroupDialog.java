/*
 * MoveSimilarAuthorsToNewGroupDialog.java
 *
 * Created on 25-may-2011, 17:12:02
 */
package scimat.gui.components.movetogroup;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.move.MoveAuthorReferencesToNewAuthorReferenceGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericDynamicItemsListPanel;
import scimat.gui.components.tablemodel.AuthorReferencesTableModel;
import scimat.model.knowledgebase.entity.AuthorReference;

/**
 *
 * @author mjcobo
 */
public class MoveSimilarAuthorReferencesToNewGroupDialog extends GenericMoveSimilarItemToGroupDialog<AuthorReference> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public MoveSimilarAuthorReferencesToNewGroupDialog(JFrame frame) {
    super(frame, 
          new GenericDynamicItemsListPanel<AuthorReference>(new AuthorReferencesTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param item 
   */
  @Override
  public void setGroupNameFromItem(AuthorReference item) {
    
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
  public void moveAction(ArrayList<AuthorReference> items, String groupName) {
    
    (new PerformKnowledgeBaseEditTask(new MoveAuthorReferencesToNewAuthorReferenceGroupEdit(items, groupName), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
