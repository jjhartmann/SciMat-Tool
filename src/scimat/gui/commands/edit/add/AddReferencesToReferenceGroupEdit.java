/*
 * AddReferencesToReferenceGroupEdit.java
 *
 * Created on 10-abr-2011, 18:37:43
 */
package scimat.gui.commands.edit.add;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddReferencesToReferenceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<Reference> referencesToAdd;
  private ArrayList<ReferenceGroup> oldReferenceGroupOfReferences;
  private ReferenceGroup targetReferenceGroup;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param referencesToAdd
   * @param targetReferenceGroup
   */
  public AddReferencesToReferenceGroupEdit(ArrayList<Reference> referencesToAdd, ReferenceGroup targetReferenceGroup) {
    this.referencesToAdd = referencesToAdd;
    this.targetReferenceGroup = targetReferenceGroup;

    this.oldReferenceGroupOfReferences = new ArrayList<ReferenceGroup>();
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
    Reference reference;
    boolean successful = false;

    try {

      for (i = 0; i < this.referencesToAdd.size(); i++) {

        reference = this.referencesToAdd.get(i);

        this.oldReferenceGroupOfReferences.add(CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().getReferenceGroup(reference.getReferenceID()));

        successful = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().setReferenceGroup(reference.getReferenceID(),
                this.targetReferenceGroup.getReferenceGroupID(), true);
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
    ReferenceGroup referenceGroup;
    boolean successful = false;

    try {

      for (i = 0; i < this.referencesToAdd.size(); i++) {

        referenceGroup = this.oldReferenceGroupOfReferences.get(i);

        if (referenceGroup != null) {

          successful = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().setReferenceGroup(this.referencesToAdd.get(i).getReferenceID(),
                  referenceGroup.getReferenceGroupID(), true);

        } else {

          successful = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().setReferenceGroup(this.referencesToAdd.get(i).getReferenceID(), null, true);
        }
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

      for (i = 0; i < this.referencesToAdd.size(); i++) {

        successful = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().setReferenceGroup(this.referencesToAdd.get(i).getReferenceID(),
                this.targetReferenceGroup.getReferenceGroupID(), true);
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
