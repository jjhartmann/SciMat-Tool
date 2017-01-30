/*
 * DeleteReferenceGroupEdit.java
 *
 * Created on 14-mar-2011, 17:40:14
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.dao.ReferenceGroupDAO;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeleteReferenceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<ReferenceGroup> referenceGroupsToDelete;
  private ArrayList<ArrayList<Reference>> references = new ArrayList<ArrayList<Reference>>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param groupName
   * @param stopGroup
   */
  public DeleteReferenceGroupEdit(ArrayList<ReferenceGroup> referenceGroupsToDelete) {
    super();
    
    this.referenceGroupsToDelete = referenceGroupsToDelete;
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
    ReferenceGroupDAO referenceGroupDAO;
    ReferenceGroup referenceGroup;

    try {

      i = 0;
      referenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO();

      while ((i < this.referenceGroupsToDelete.size()) && (successful)) {

        referenceGroup = this.referenceGroupsToDelete.get(i);

        // Retrieve its relation
        this.references.add(referenceGroupDAO.getReferences(referenceGroup.getReferenceGroupID()));

        successful = referenceGroupDAO.removeReferenceGroup(referenceGroup.getReferenceGroupID(), true);

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
    ReferenceGroupDAO referenceGroupDAO;
    ReferenceDAO referenceDAO;
    ReferenceGroup referenceGroup;
    Reference reference;

    try {

      referenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO();
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();

      i = 0;

      while ((i < this.referenceGroupsToDelete.size()) && (successful)) {

        referenceGroup = this.referenceGroupsToDelete.get(i);

        successful = referenceGroupDAO.addReferenceGroup(referenceGroup, true);

        j = 0;

        while ((j < this.references.get(i).size()) && (successful)) {

          reference = this.references.get(i).get(j);

          successful = referenceDAO.setReferenceGroup(reference.getReferenceID(),
                                                referenceGroup.getReferenceGroupID(), true);

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
    ReferenceGroupDAO referenceGroupDAO;
    ReferenceGroup referenceGroup;

    try {

      i = 0;
      referenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO();

      while ((i < this.referenceGroupsToDelete.size()) && (successful)) {

        referenceGroup = this.referenceGroupsToDelete.get(i);

        successful = referenceGroupDAO.removeReferenceGroup(referenceGroup.getReferenceGroupID(), true);

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
