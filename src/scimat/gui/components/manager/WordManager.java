/*
 * WordManager.java
 *
 * Created on 13-mar-2011, 17:16:00
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteWordEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.WordGlobalSlavePanel;
import scimat.gui.components.itemslist.WordsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.Word;

/**
 *
 * @author mjcobo
 */
public class WordManager extends GenericItemManagerPanel<Word> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public WordManager() {
    super(new WordsListPanel(),
          new WordGlobalSlavePanel());

    setMasterPanelTitle("Words list");
    setSlavePanelTitle("Word detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddWordDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<Word> items) {
    JoinEntitiesDialogManager.getInstance().showWordsJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<Word> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteWordEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
