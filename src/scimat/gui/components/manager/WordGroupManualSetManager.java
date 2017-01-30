/*
 * WordGroupManualSetManager.java
 *
 * Created on 23-mar-2011, 21:49:34
 */
package scimat.gui.components.manager;

import java.util.ArrayList;
import scimat.gui.commands.edit.add.AddWordsToWordGroupEdit;
import scimat.gui.commands.edit.delete.DeleteWordEdit;
import scimat.gui.commands.edit.delete.DeleteWordGroupEdit;
import scimat.gui.commands.edit.delete.DeleteWordsFromWordGroupEdit;
import scimat.gui.commands.edit.move.MoveWordToDifferentWordGroupEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.adddialog.AddDialogManager;
import scimat.gui.components.editdialog.EditDialogManager;
import scimat.gui.components.itemslist.WordGroupsListPanel;
import scimat.gui.components.itemslist.WordsWithoutGroupListPanel;
import scimat.gui.components.joindialog.JoinEntitiesDialogManager;
import scimat.gui.components.movetogroup.MoveToGroupDialogManager;
import scimat.gui.components.slavepanel.WordGroupSlaveWordsPanel;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.entity.WordGroup;

/**
 *
 * @author mjcobo
 */
public class WordGroupManualSetManager extends GenericManualSetGroupPanel<WordGroup, Word> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public WordGroupManualSetManager() {
    super(new WordGroupsListPanel(),
          new WordsWithoutGroupListPanel(),
          new WordGroupSlaveWordsPanel());

    setDescription("Word groups", "Words of the group", "Words without group");
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  public void addGroupAction() {

    AddDialogManager.getInstance().showAddWordGroupDialog();
  }

  /**
   * 
   * @param group 
   */
  public void editGroupAction(WordGroup group) {

    EditDialogManager.getInstance().showEditWordGroupDialog(group);
  }

  /**
   * 
   * @param groups 
   */
  public void moveGroupToAction(ArrayList<WordGroup> groups) {

    JoinEntitiesDialogManager.getInstance().showWordGroupsJoinDialog(groups);
  }

  /**
   * 
   * @param groups 
   */
  public void deleteGroupAction(ArrayList<WordGroup> groups) {

    (new PerformKnowledgeBaseEditTask(new DeleteWordGroupEdit(groups), this)).execute();
  }

  /**
   * 
   * @param itemsWithoutGroup 
   */
  public void toNewGroupAction(ArrayList<Word> itemsWithoutGroup) {

    MoveToGroupDialogManager.getInstance().showMoveWordsToNewGroupDialog(itemsWithoutGroup);
  }

  /**
   * 
   * @param itemsWithoutGroup 
   */
  public void toDifferentGroupAction(ArrayList<Word> itemsWithoutGroup) {

    (new PerformKnowledgeBaseEditTask(new MoveWordToDifferentWordGroupEdit(itemsWithoutGroup), this)).execute();
  }

  /**
   * 
   * @param itemsFromGroup
   * @param group 
   */
  public void removeItemFromGroupAction(ArrayList<Word> itemsFromGroup, WordGroup group) {

    (new PerformKnowledgeBaseEditTask(new DeleteWordsFromWordGroupEdit(itemsFromGroup, group), this)).execute();
  }

  /**
   * 
   * @param group
   * @param itemsWithoutGroup 
   */
  public void addItemToGroupAction(WordGroup group, ArrayList<Word> itemsWithoutGroup) {

    (new PerformKnowledgeBaseEditTask(new AddWordsToWordGroupEdit(itemsWithoutGroup, group), this)).execute();
  }

  /**
   * 
   * @param itemsWithoutGroup 
   */
  @Override
  public void removeItemsWithoutGroup(ArrayList<Word> itemsWithoutGroup) {
    (new PerformKnowledgeBaseEditTask(new DeleteWordEdit(itemsWithoutGroup), this)).execute();
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
