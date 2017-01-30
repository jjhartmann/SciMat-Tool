/*
 * DeleteReferenceSourcesFromReferenceSourceGroupEdit.java
 *
 * Created on 10-abr-2011, 18:37:43
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @referenceSource mjcobo
 */
public class DeleteReferenceSourcesFromReferenceSourceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<ReferenceSource> referenceSourcesToDeleteFromReferenceSourceGroup;
  private ReferenceSourceGroup referenceSourceGroupToDeleteFromReferenceSource;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param referenceSourcesToDeleteFromReferenceSourceGroup
   * @param referenceSourceGroupToDeleteFromReferenceSource
   */
  public DeleteReferenceSourcesFromReferenceSourceGroupEdit(ArrayList<ReferenceSource> referenceSourcesToDeleteFromReferenceSourceGroup,
          ReferenceSourceGroup referenceSourceGroupToDeleteFromReferenceSource) {
    
    this.referenceSourcesToDeleteFromReferenceSourceGroup = referenceSourcesToDeleteFromReferenceSourceGroup;
    this.referenceSourceGroupToDeleteFromReferenceSource = referenceSourceGroupToDeleteFromReferenceSource;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   * @throws KnowledgeBaseException
   */
  @Override
  public boolean execute() throws KnowledgeBaseException {

    int i;
    boolean successful = false;

    try {

      for (i = 0; i < this.referenceSourcesToDeleteFromReferenceSourceGroup.size(); i++) {

        successful = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().setReferenceSourceGroup(this.referenceSourcesToDeleteFromReferenceSourceGroup.get(i).getReferenceSourceID(),
                null, true);
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

    int i;
    boolean successful = false;

    try {

      for (i = 0; i < this.referenceSourcesToDeleteFromReferenceSourceGroup.size(); i++) {

        successful = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().setReferenceSourceGroup(this.referenceSourcesToDeleteFromReferenceSourceGroup.get(i).getReferenceSourceID(),
                this.referenceSourceGroupToDeleteFromReferenceSource.getReferenceSourceGroupID(), true);
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
    boolean successful = false;

    try {

      for (i = 0; i < this.referenceSourcesToDeleteFromReferenceSourceGroup.size(); i++) {

        successful = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().setReferenceSourceGroup(this.referenceSourcesToDeleteFromReferenceSourceGroup.get(i).getReferenceSourceID(), null, true);
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
