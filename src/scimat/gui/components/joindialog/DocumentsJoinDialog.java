/*
 * DocumentsJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinDocumentEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.DocumentsTableModel;
import scimat.model.knowledgebase.entity.Document;

/**
 *
 * @author mjcobo
 */
public class DocumentsJoinDialog extends GenericJoinEntitiesDialog<Document> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public DocumentsJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<Document>(new DocumentsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<Document> sourceItems, Document targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinDocumentEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
