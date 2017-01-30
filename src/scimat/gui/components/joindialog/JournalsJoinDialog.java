/*
 * JournalsJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinJournalEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.JournalsTableModel;
import scimat.model.knowledgebase.entity.Journal;

/**
 *
 * @author mjcobo
 */
public class JournalsJoinDialog extends GenericJoinEntitiesDialog<Journal>{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public JournalsJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<Journal>(new JournalsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<Journal> sourceItems, Journal targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinJournalEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
