/*
 * AuthorGroupManager.java
 *
 * Created on 13-mar-2011, 17:15:32
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteAuthorGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.AuthorGroupGlobalSlavePanel;
import scimat.gui.components.itemslist.AuthorGroupsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.AuthorGroup;

/**
 *
 * @author mjcobo
 */
public class AuthorGroupManager extends GenericItemManagerPanel<AuthorGroup> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public AuthorGroupManager() {
    super(new AuthorGroupsListPanel(),
          new AuthorGroupGlobalSlavePanel());

    setMasterPanelTitle("Author groups list");
    setSlavePanelTitle("Author group detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddAuthorGroupDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<AuthorGroup> items) {
    JoinEntitiesDialogManager.getInstance().showAuthorGroupsJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<AuthorGroup> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteAuthorGroupEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
