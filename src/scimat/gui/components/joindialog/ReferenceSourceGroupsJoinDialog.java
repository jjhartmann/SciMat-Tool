/*
 * ReferenceSourceGroupsJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinReferenceSourceGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.ReferenceSourceGroupsTableModel;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;

/**
 *
 * @author mjcobo
 */
public class ReferenceSourceGroupsJoinDialog extends GenericJoinEntitiesDialog<ReferenceSourceGroup> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public ReferenceSourceGroupsJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<ReferenceSourceGroup>(new ReferenceSourceGroupsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<ReferenceSourceGroup> sourceItems, ReferenceSourceGroup targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinReferenceSourceGroupEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
