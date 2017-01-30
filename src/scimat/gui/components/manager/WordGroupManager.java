/*
 * WordGroupManager.java
 *
 * Created on 13-mar-2011, 17:15:54
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteWordGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.WordGroupGlobalSlavePanel;
import scimat.gui.components.itemslist.WordGroupsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.WordGroup;

/**
 *
 * @author mjcobo
 */
public class WordGroupManager extends GenericItemManagerPanel<WordGroup> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public WordGroupManager() {
    super(new WordGroupsListPanel(),
          new WordGroupGlobalSlavePanel());

    setMasterPanelTitle("Word groups list");
    setSlavePanelTitle("Word groups detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddWordGroupDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<WordGroup> items) {
    JoinEntitiesDialogManager.getInstance().showWordGroupsJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<WordGroup> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteWordGroupEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
