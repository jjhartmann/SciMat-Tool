/*
 * JournalManager.java
 *
 * Created on 13-mar-2011, 17:16:25
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteJournalEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.JournalGlobalSlavePanel;
import scimat.gui.components.itemslist.JournalsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.Journal;

/**
 *
 * @author mjcobo
 */
public class JournalManager extends GenericItemManagerPanel<Journal> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public JournalManager() {
    super(new JournalsListPanel(), 
          new JournalGlobalSlavePanel());

    setMasterPanelTitle("Journals list");
    setSlavePanelTitle("Journal detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddJournalDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<Journal> items) {
    JoinEntitiesDialogManager.getInstance().showJournalsJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<Journal> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteJournalEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
