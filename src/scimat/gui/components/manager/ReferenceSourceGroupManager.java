/*
 * ReferenceSourceGroupManager.java
 *
 * Created on 13-mar-2011, 17:17:26
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteReferenceSourceGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.ReferenceSourceGroupGlobalSlavePanel;
import scimat.gui.components.itemslist.ReferenceSourceGroupsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;

/**
 *
 * @author mjcobo
 */
public class ReferenceSourceGroupManager extends GenericItemManagerPanel<ReferenceSourceGroup> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public ReferenceSourceGroupManager() {
    super(new ReferenceSourceGroupsListPanel(),
          new ReferenceSourceGroupGlobalSlavePanel());

    setMasterPanelTitle("Sources-reference group list");
    setSlavePanelTitle("Sources-reference group detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddReferenceSourceGroupDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<ReferenceSourceGroup> items) {
    JoinEntitiesDialogManager.getInstance().showReferenceSourceGroupsJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<ReferenceSourceGroup> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteReferenceSourceGroupEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
