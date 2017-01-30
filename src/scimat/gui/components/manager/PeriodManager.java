/*
 * PeriodManager.java
 *
 * Created on 13-mar-2011, 17:16:14
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeletePeriodEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.PeriodGlobalSlavePanel;
import scimat.gui.components.itemslist.PeriodsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.Period;

/**
 *
 * @author mjcobo
 */
public class PeriodManager extends GenericItemManagerPanel<Period> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public PeriodManager() {
    super(new PeriodsListPanel(),
          new PeriodGlobalSlavePanel());

    setMasterPanelTitle("Periods list");
    setSlavePanelTitle("Period detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddPeriodDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<Period> items) {
    JoinEntitiesDialogManager.getInstance().showPeriodsJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<Period> items) {
    (new PerformKnowledgeBaseEditTask(new DeletePeriodEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
