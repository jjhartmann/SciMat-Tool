/*
 * AuthorsJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinAuthorEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.AuthorsTableModel;
import scimat.model.knowledgebase.entity.Author;

/**
 *
 * @author mjcobo
 */
public class AuthorsJoinDialog extends GenericJoinEntitiesDialog<Author> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public AuthorsJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<Author>(new AuthorsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<Author> sourceItems, Author targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinAuthorEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
