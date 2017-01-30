/*
 * AuthorReferencesJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinAuthorReferenceEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.AuthorReferencesTableModel;
import scimat.model.knowledgebase.entity.AuthorReference;

/**
 *
 * @author mjcobo
 */
public class AuthorReferencesJoinDialog extends GenericJoinEntitiesDialog<AuthorReference> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public AuthorReferencesJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<AuthorReference>(new AuthorReferencesTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<AuthorReference> sourceItems, AuthorReference targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinAuthorReferenceEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
