/*
 * AuthorGroupsJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinAuthorGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.AuthorGroupsTableModel;
import scimat.model.knowledgebase.entity.AuthorGroup;

/**
 *
 * @author mjcobo
 */
public class AuthorGroupsJoinDialog extends GenericJoinEntitiesDialog<AuthorGroup> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public AuthorGroupsJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<AuthorGroup>(new AuthorGroupsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<AuthorGroup> sourceItems, AuthorGroup targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinAuthorGroupEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
