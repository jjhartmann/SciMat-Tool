/*
 * AuthorReferenceGroupsJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinAuthorReferenceGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.AuthorReferenceGroupsTableModel;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceGroupsJoinDialog extends GenericJoinEntitiesDialog<AuthorReferenceGroup> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public AuthorReferenceGroupsJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<AuthorReferenceGroup>(new AuthorReferenceGroupsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<AuthorReferenceGroup> sourceItems, AuthorReferenceGroup targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinAuthorReferenceGroupEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
