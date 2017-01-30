/*
 * AuthorReferenceGroupManager.java
 *
 * Created on 13-mar-2011, 17:16:59
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteAuthorReferenceGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.AuthorReferenceGroupGlobalSlavePanel;
import scimat.gui.components.itemslist.AuthorReferenceGroupsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceGroupManager extends GenericItemManagerPanel<AuthorReferenceGroup> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public AuthorReferenceGroupManager() {
    super(new AuthorReferenceGroupsListPanel(),
          new AuthorReferenceGroupGlobalSlavePanel());

    setMasterPanelTitle("Author reference groups list");
    setSlavePanelTitle("Author reference group detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddAuthorReferenceGroupDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<AuthorReferenceGroup> items) {
    JoinEntitiesDialogManager.getInstance().showAuthorReferenceGroupsJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<AuthorReferenceGroup> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteAuthorReferenceGroupEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
