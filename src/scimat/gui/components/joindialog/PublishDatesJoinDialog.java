/*
 * PublishDatesJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinPublishDateEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.PublishDatesTableModel;
import scimat.model.knowledgebase.entity.PublishDate;

/**
 *
 * @author mjcobo
 */
public class PublishDatesJoinDialog extends GenericJoinEntitiesDialog<PublishDate> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public PublishDatesJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<PublishDate>(new PublishDatesTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<PublishDate> sourceItems, PublishDate targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinPublishDateEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
