/*
 * SubjectCategoryManager.java
 *
 * Created on 13-mar-2011, 17:16:33
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.delete.DeleteSubjectCategoryEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.globalslavepanel.SubjectCategoryGlobalSlavePanel;
import scimat.gui.components.itemslist.SubjectCategoriesListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.model.knowledgebase.entity.SubjectCategory;

/**
 *
 * @author mjcobo
 */
public class SubjectCategoryManager extends GenericItemManagerPanel<SubjectCategory> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public SubjectCategoryManager() {
    super(new SubjectCategoriesListPanel(),
          new SubjectCategoryGlobalSlavePanel());

    setMasterPanelTitle("Subject categories list");
    setSlavePanelTitle("Subject category detail");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  @Override
  public void addAction() {
    AddDialogManager.getInstance().showAddSubjectCategoryDialog();
  }

  /**
   *
   */
  @Override
  public void moveToAction(ArrayList<SubjectCategory> items) {
    JoinEntitiesDialogManager.getInstance().showSubjectCategoriesJoinDialog(items);
  }

  /**
   *
   */
  @Override
  public void removeAction(ArrayList<SubjectCategory> items) {
    (new PerformKnowledgeBaseEditTask(new DeleteSubjectCategoryEdit(items), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
