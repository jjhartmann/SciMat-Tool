/*
 * ReferenceManager.java
 *
 * Created on 13-mar-2011, 17:16:46
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteReferenceEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.ReferenceGlobalSlavePanel;
import scimat.gui.components.itemslist.ReferencesListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.Reference;

/**
 *
 * @author mjcobo
 */
public class ReferenceManager extends GenericItemManagerPanel<Reference> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public ReferenceManager() {
    super(new ReferencesListPanel(), 
          new ReferenceGlobalSlavePanel());

    setMasterPanelTitle("References list");
    setSlavePanelTitle("Reference detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddReferenceDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<Reference> items) {
    JoinEntitiesDialogManager.getInstance().showReferencesJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<Reference> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteReferenceEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
