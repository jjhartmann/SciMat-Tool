/*
 * WordGroupsJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinWordGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.WordGroupsTableModel;
import scimat.model.knowledgebase.entity.WordGroup;

/**
 *
 * @author mjcobo
 */
public class WordGroupsJoinDialog extends GenericJoinEntitiesDialog<WordGroup> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public WordGroupsJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<WordGroup>(new WordGroupsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<WordGroup> sourceItems, WordGroup targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinWordGroupEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
