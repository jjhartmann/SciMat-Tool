/*
 * ReferenceGroupManager.java
 *
 * Created on 13-mar-2011, 17:16:40
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteReferenceGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.ReferenceGroupGlobalSlavePanel;
import scimat.gui.components.itemslist.ReferenceGroupsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.ReferenceGroup;

/**
 *
 * @author mjcobo
 */
public class ReferenceGroupManager extends GenericItemManagerPanel<ReferenceGroup> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public ReferenceGroupManager() {
    super(new ReferenceGroupsListPanel(),
          new ReferenceGroupGlobalSlavePanel());

    setMasterPanelTitle("Reference groups list");
    setSlavePanelTitle("Reference group detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddReferenceGroupDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<ReferenceGroup> items) {
    JoinEntitiesDialogManager.getInstance().showReferenceGroupsJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<ReferenceGroup> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteReferenceGroupEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
