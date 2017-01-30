/*
 * AffiliationsJoinDialog.java
 *
 * Created on 24-may-2011, 12:40:45
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.gui.commands.edit.join.JoinAffiliationEdit;
import scimat.gui.commands.task.PerformKnowledgeBaseEditTask;
import scimat.gui.components.itemslist.GenericItemsListPanel;
import scimat.gui.components.tablemodel.AffiliationsTableModel;
import scimat.model.knowledgebase.entity.Affiliation;

/**
 *
 * @author mjcobo
 */
public class AffiliationsJoinDialog extends GenericJoinEntitiesDialog<Affiliation> {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param frame 
   */
  public AffiliationsJoinDialog(JFrame frame) {
    super(frame, 
          new GenericItemsListPanel<Affiliation>(new AffiliationsTableModel()));
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public void moveToAction(ArrayList<Affiliation> sourceItems, Affiliation targetItem) {
    (new PerformKnowledgeBaseEditTask(new JoinAffiliationEdit(sourceItems, targetItem), rootPane)).execute();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
