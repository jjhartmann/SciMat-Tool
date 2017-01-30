/*
 * ReferenceSourceManager.java
 *
 * Created on 13-mar-2011, 17:17:19
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteReferenceSourceEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.ReferenceSourceGlobalSlavePanel;
import scimat.gui.components.itemslist.ReferenceSourcesListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.ReferenceSource;

/**
 *
 * @author mjcobo
 */
public class ReferenceSourceManager extends GenericItemManagerPanel<ReferenceSource> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public ReferenceSourceManager() {
    super(new ReferenceSourcesListPanel(),
          new ReferenceSourceGlobalSlavePanel());

    setMasterPanelTitle("Sources-reference list");
    setSlavePanelTitle("Sources-reference detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddReferenceSourceDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<ReferenceSource> items) {
    JoinEntitiesDialogManager.getInstance().showReferenceSourcesJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<ReferenceSource> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteReferenceSourceEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
