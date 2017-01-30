/*
 * ReferenceGroupsJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinReferenceGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.ReferenceGroupsTableModel;
import scimat.model.knowledgebase.entity.ReferenceGroup;

/**
 *
 * @author mjcobo
 */
public class ReferenceGroupsJoinDialog extends GenericJoinEntitiesDialog<ReferenceGroup> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public ReferenceGroupsJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<ReferenceGroup>(new ReferenceGroupsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<ReferenceGroup> sourceItems, ReferenceGroup targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinReferenceGroupEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
