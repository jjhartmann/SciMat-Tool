/*
 * WordsJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinWordEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.WordsTableModel;
import scimat.model.knowledgebase.entity.Word;

/**
 *
 * @author mjcobo
 */
public class WordsJoinDialog extends GenericJoinEntitiesDialog<Word> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public WordsJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<Word>(new WordsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<Word> sourceItems, Word targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinWordEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
