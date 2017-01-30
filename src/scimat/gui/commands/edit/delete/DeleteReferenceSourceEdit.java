/*
 * DeleteReferenceSourceEdit.java
 *
 * Created on 14-mar-2011, 17:40:25
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.dao.ReferenceSourceDAO;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeleteReferenceSourceEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<ReferenceSource> referenceSourcesToDelete;
  private ArrayList<ArrayList<Reference>> references = new ArrayList<ArrayList<Reference>>();
  private ArrayList<ReferenceSourceGroup> referenceSourceGroups = new ArrayList<ReferenceSourceGroup>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param referenceSourcesToDelete
   */
  public DeleteReferenceSourceEdit(ArrayList<ReferenceSource> referenceSourcesToDelete) {
    super();
    
    this.referenceSourcesToDelete = referenceSourcesToDelete;
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
    ReferenceSourceDAO referenceSourceDAO;
    ReferenceSource referenceSource;

    try {

      i = 0;
      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();

      while ((i < this.referenceSourcesToDelete.size()) && (successful)) {

        referenceSource = this.referenceSourcesToDelete.get(i);

        // Retrieve its relation
        this.references.add(referenceSourceDAO.getReferences(referenceSource.getReferenceSourceID()));
        this.referenceSourceGroups.add(referenceSourceDAO.getReferenceSourceGroup(referenceSource.getReferenceSourceID()));

        successful = referenceSourceDAO.removeReferenceSource(referenceSource.getReferenceSourceID(), true);

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
    ReferenceSourceDAO referenceSourceDAO;
    ReferenceDAO referenceDAO;
    ReferenceSource referenceSource;
    ReferenceSourceGroup referenceSourceGroup;

    try {

      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();

      i = 0;

      while ((i < this.referenceSourcesToDelete.size()) && (successful)) {

        referenceSource = this.referenceSourcesToDelete.get(i);

        successful = referenceSourceDAO.addReferenceSource(referenceSource, true);

        j = 0;

        while ((j < this.references.get(i).size()) && (successful)) {
          
          successful = referenceDAO.setReferenceSource(referenceSource.getReferenceSourceID(),
                                                       this.references.get(i).get(j).getReferenceID(), true);

          j++;
        }

        referenceSourceGroup = this.referenceSourceGroups.get(i);

        if ((referenceSourceGroup != null) && (successful)) {

          successful = referenceSourceDAO.setReferenceSourceGroup(referenceSource.getReferenceSourceID(),
                                            referenceSourceGroup.getReferenceSourceGroupID(), true);
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
    ReferenceSourceDAO referenceSourceDAO;
    ReferenceSource referenceSource;

    try {

      i = 0;
      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();

      while ((i < this.referenceSourcesToDelete.size()) && (successful)) {

        referenceSource = this.referenceSourcesToDelete.get(i);

        successful = referenceSourceDAO.removeReferenceSource(referenceSource.getReferenceSourceID(), true);

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
