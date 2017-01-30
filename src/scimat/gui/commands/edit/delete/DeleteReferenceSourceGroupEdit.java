/*
 * DeleteReferenceSourceGroupEdit.java
 *
 * Created on 14-mar-2011, 17:40:35
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.ReferenceSourceDAO;
import scimat.model.knowledgebase.dao.ReferenceSourceGroupDAO;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeleteReferenceSourceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<ReferenceSourceGroup> referenceSourceGroupsToDelete;
  private ArrayList<ArrayList<ReferenceSource>> referenceSources = new ArrayList<ArrayList<ReferenceSource>>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public DeleteReferenceSourceGroupEdit(ArrayList<ReferenceSourceGroup> referenceSourceGroupsToDelete) {
    super();
    
    this.referenceSourceGroupsToDelete = referenceSourceGroupsToDelete;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @throws KnowledgeBaseException
   */
  @Override
  public boolean execute() throws KnowledgeBaseException {

    boolean successful = true;
    int i;
    ReferenceSourceGroupDAO referenceSourceGroupDAO;
    ReferenceSourceGroup referenceSourceGroup;

    try {

      i = 0;
      referenceSourceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO();

      while ((i < this.referenceSourceGroupsToDelete.size()) && (successful)) {

        referenceSourceGroup = this.referenceSourceGroupsToDelete.get(i);

        // Retrieve its relation
        this.referenceSources.add(referenceSourceGroupDAO.getReferenceSources(referenceSourceGroup.getReferenceSourceGroupID()));

        successful = referenceSourceGroupDAO.removeReferenceSourceGroup(referenceSourceGroup.getReferenceSourceGroupID(), true);

        i++;
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();
        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

        UndoStack.addEdit(this);


      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();

        this.errorMessage = "An error happened";

      }


    } catch (KnowledgeBaseException e) {

      CurrentProject.getInstance().getKnowledgeBase().rollback();

      successful = false;
      this.errorMessage = "An exception happened.";

      throw e;
    }

    return successful;

  }

  /**
   *
   * @throws CannotUndoException
   */
  @Override
  public void undo() throws CannotUndoException {
    super.undo();

    int i, j;
    boolean successful = true;
    ReferenceSourceGroupDAO referenceSourceGroupDAO;
    ReferenceSourceDAO referenceSourceDAO;
    ReferenceSourceGroup referenceSourceGroup;
    ReferenceSource referenceSource;

    try {

      referenceSourceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO();
      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();

      i = 0;

      while ((i < this.referenceSourceGroupsToDelete.size()) && (successful)) {

        referenceSourceGroup = this.referenceSourceGroupsToDelete.get(i);

        successful = referenceSourceGroupDAO.addReferenceSourceGroup(referenceSourceGroup, true);

        j = 0;

        while ((j < this.referenceSources.get(i).size()) && (successful)) {

          referenceSource = this.referenceSources.get(i).get(j);

          successful = referenceSourceDAO.setReferenceSourceGroup(referenceSource.getReferenceSourceID(),
                                                referenceSourceGroup.getReferenceSourceGroupID(), true);

          j++;
        }

        i++;
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();

        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();
      }

    } catch (KnowledgeBaseException e) {

      e.printStackTrace(System.err);

      try{

        CurrentProject.getInstance().getKnowledgeBase().rollback();

      } catch (KnowledgeBaseException e2) {

        e2.printStackTrace(System.err);

      }
    }
  }

  /**
   *
   * @throws CannotUndoException
   */
  @Override
  public void redo() throws CannotUndoException {
    super.redo();

    int i;
    boolean successful = true;
    ReferenceSourceGroupDAO referenceSourceGroupDAO;
    ReferenceSourceGroup referenceSourceGroup;

    try {

      i = 0;
      referenceSourceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO();

      while ((i < this.referenceSourceGroupsToDelete.size()) && (successful)) {

        referenceSourceGroup = this.referenceSourceGroupsToDelete.get(i);

        successful = referenceSourceGroupDAO.removeReferenceSourceGroup(referenceSourceGroup.getReferenceSourceGroupID(), true);

        i++;
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();

        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();
      }

    } catch (KnowledgeBaseException e) {

      e.printStackTrace(System.err);

      try{

        CurrentProject.getInstance().getKnowledgeBase().rollback();

      } catch (KnowledgeBaseException e2) {

        e2.printStackTrace(System.err);

      }
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
