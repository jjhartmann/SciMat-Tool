/*
 * AuthorReferenceGroupManualSetManager.java
 *
 * Created on 23-mar-2011, 21:49:34
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.add.AddAuthorReferencesToAuthorReferenceGroupEdit;
import scimat.gui.commands.edit.delete.DeleteAuthorReferenceEdit;
import scimat.gui.commands.edit.delete.DeleteAuthorReferenceGroupEdit;
import scimat.gui.commands.edit.delete.DeleteAuthorReferencesFromAuthorReferenceGroupEdit;
import scimat.gui.commands.edit.move.MoveAuthorReferenceToDifferentAuthorReferenceGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.editdialog.EditDialogManager;
import scimat.gui.components.itemslist.AuthorReferenceGroupsListPanel;
import scimat.gui.components.itemslist.AuthorReferenceWithoutGroupsListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.gui.components.movetogroup.MoveToGroupDialogManager;
import scimat.gui.components.slavepanel.AuthorReferenceGroupSlaveAuthorReferencesPanel;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceGroupManualSetManager extends GenericManualSetGroupPanel<AuthorReferenceGroup, AuthorReference>{

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public AuthorReferenceGroupManualSetManager() {
    super(new AuthorReferenceGroupsListPanel(),
          new AuthorReferenceWithoutGroupsListPanel(),
          new AuthorReferenceGroupSlaveAuthorReferencesPanel());

    setDescription("Authors-reference groups", "Authors-reference of the group", "Authors-reference without group");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  public void addGroupAction() {

    AddDialogManager.getInstance().showAddAuthorReferenceGroupDialog();
  }

  /**
   * 
   * @param group 
   */
  public void editGroupAction(AuthorReferenceGroup group) {

    EditDialogManager.getInstance().showEditAuthorReferenceGroupDialog(group);
  }

  /**
   * 
   * @param groups 
   */
  public void moveGroupToAction(ArrayList<AuthorReferenceGroup> groups) {

    JoinEntitiesDialogManager.getInstance().showAuthorReferenceGroupsJoinDialog(groups);
  }

  /**
   * 
   * @param groups 
   */
  public void deleteGroupAction(ArrayList<AuthorReferenceGroup> groups) {

    (new PerformKnowledgeBaseEditTask(new DeleteAuthorReferenceGroupEdit(groups), this)).execute();
  }

  /**
   * 
   * @param itemsWithoutGroup 
   */
  public void toNewGroupAction(ArrayList<AuthorReference> itemsWithoutGroup) {

    MoveToGroupDialogManager.getInstance().showMoveAuthorReferencesToNewGroupDialog(itemsWithoutGroup);
  }

  /**
   * 
   * @param itemsWithoutGroup 
   */
  public void toDifferentGroupAction(ArrayList<AuthorReference> itemsWithoutGroup) {

    (new PerformKnowledgeBaseEditTask(new MoveAuthorReferenceToDifferentAuthorReferenceGroupEdit(itemsWithoutGroup), this)).execute();
  }

  /**
   * 
   * @param itemsFromGroup
   * @param group 
   */
  public void removeItemFromGroupAction(ArrayList<AuthorReference> itemsFromGroup, AuthorReferenceGroup group) {

    (new PerformKnowledgeBaseEditTask(new DeleteAuthorReferencesFromAuthorReferenceGroupEdit(itemsFromGroup, group), this)).execute();
  }

  /**
   * 
   * @param group
   * @param itemsWithoutGroup 
   */
  public void addItemToGroupAction(AuthorReferenceGroup group, ArrayList<AuthorReference> itemsWithoutGroup) {

    (new PerformKnowledgeBaseEditTask(new AddAuthorReferencesToAuthorReferenceGroupEdit(itemsWithoutGroup, group), this)).execute();
  }

  /**
   * 
   * @param itemsWithoutGroup 
   */
  @Override
  public void removeItemsWithoutGroup(ArrayList<AuthorReference> itemsWithoutGroup) {
    (new PerformKnowledgeBaseEditTask(new DeleteAuthorReferenceEdit(itemsWithoutGroup), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
