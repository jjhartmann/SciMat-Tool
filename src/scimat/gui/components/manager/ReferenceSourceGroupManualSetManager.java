/*
 * ReferenceSourceGroupManualSetManager.java
 *
 * Created on 23-mar-2011, 21:49:34
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.add.AddReferenceSourcesToReferenceSourceGroupEdit;
import scimat.gui.commands.edit.delete.DeleteReferenceSourceEdit;
import scimat.gui.commands.edit.delete.DeleteReferenceSourceGroupEdit;
import scimat.gui.commands.edit.delete.DeleteReferenceSourcesFromReferenceSourceGroupEdit;
import scimat.gui.commands.edit.move.MoveReferenceSourceToDifferentReferenceSourceGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.editdialog.EditDialogManager;
import scimat.gui.components.itemslist.ReferenceSourceGroupsListPanel;
import scimat.gui.components.itemslist.ReferenceSourcesWithoutGroupListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.gui.components.movetogroup.MoveToGroupDialogManager;
import scimat.gui.components.slavepanel.ReferenceSourceGroupSlaveReferenceSourcesPanel;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;

/**
 *
 * @author mjcobo
 */
public class ReferenceSourceGroupManualSetManager extends GenericManualSetGroupPanel<ReferenceSourceGroup, ReferenceSource> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public ReferenceSourceGroupManualSetManager() {
    super(new ReferenceSourceGroupsListPanel(),
          new ReferenceSourcesWithoutGroupListPanel(),
          new ReferenceSourceGroupSlaveReferenceSourcesPanel());

    setDescription("Sources-reference groups", "Sources-reference of the group", "Sources-reference without group");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  public void addGroupAction() {

    AddDialogManager.getInstance().showAddReferenceSourceGroupDialog();
  }

  /**
   * 
   * @param group 
   */
  public void editGroupAction(ReferenceSourceGroup group) {

    EditDialogManager.getInstance().showEditReferenceSourceGroupDialog(group);
  }

  /**
   * 
   * @param groups 
   */
  public void moveGroupToAction(ArrayList<ReferenceSourceGroup> groups) {

    JoinEntitiesDialogManager.getInstance().showReferenceSourceGroupsJoinDialog(groups);
  }

  /**
   * 
   * @param groups 
   */
  public void deleteGroupAction(ArrayList<ReferenceSourceGroup> groups) {

    (new PerformKnowledgeBaseEditTask(new DeleteReferenceSourceGroupEdit(groups), this)).execute();
  }

  /**
   * 
   * @param itemsWithoutGroup 
   */
  public void toNewGroupAction(ArrayList<ReferenceSource> itemsWithoutGroup) {

    MoveToGroupDialogManager.getInstance().showMoveReferenceSourcesToNewGroupDialog(itemsWithoutGroup);
  }

  /**
   * 
   * @param itemsWithoutGroup 
   */
  public void toDifferentGroupAction(ArrayList<ReferenceSource> itemsWithoutGroup) {

    (new PerformKnowledgeBaseEditTask(new MoveReferenceSourceToDifferentReferenceSourceGroupEdit(itemsWithoutGroup), this)).execute();
  }

  /**
   * 
   * @param itemsFromGroup
   * @param group 
   */
  public void removeItemFromGroupAction(ArrayList<ReferenceSource> itemsFromGroup, ReferenceSourceGroup group) {

    (new PerformKnowledgeBaseEditTask(new DeleteReferenceSourcesFromReferenceSourceGroupEdit(itemsFromGroup, group), this)).execute();
  }

  /**
   * 
   * @param group
   * @param itemsWithoutGroup 
   */
  public void addItemToGroupAction(ReferenceSourceGroup group, ArrayList<ReferenceSource> itemsWithoutGroup) {

    (new PerformKnowledgeBaseEditTask(new AddReferenceSourcesToReferenceSourceGroupEdit(itemsWithoutGroup, group), this)).execute();
  }

  /**
   * 
   * @param itemsWithoutGroup 
   */
  @Override
  public void removeItemsWithoutGroup(ArrayList<ReferenceSource> itemsWithoutGroup) {
    (new PerformKnowledgeBaseEditTask(new DeleteReferenceSourceEdit(itemsWithoutGroup), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
