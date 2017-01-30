/*
 * PeriodsJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinPeriodEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.PeriodsTableModel;
import scimat.model.knowledgebase.entity.Period;

/**
 *
 * @author mjcobo
 */
public class PeriodsJoinDialog extends GenericJoinEntitiesDialog<Period> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public PeriodsJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<Period>(new PeriodsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<Period> sourceItems, Period targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinPeriodEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
