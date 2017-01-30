/*
 * AddReferenceSourcesToReferenceSourceGroupEdit.java
 *
 * Created on 10-abr-2011, 18:37:43
 */
package scimat.gui.commands.edit.add;

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
 * @author mjcobo
 */
public class AddReferenceSourcesToReferenceSourceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<ReferenceSource> referenceSourcesToAdd;
  private ArrayList<ReferenceSourceGroup> oldReferenceSourceGroupOfReferenceSources;
  private ReferenceSourceGroup targetReferenceSourceGroup;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param referenceSourcesToAdd
   * @param targetReferenceSourceGroup
   */
  public AddReferenceSourcesToReferenceSourceGroupEdit(ArrayList<ReferenceSource> referenceSourcesToAdd,
          ReferenceSourceGroup targetReferenceSourceGroup) {
    
    this.referenceSourcesToAdd = referenceSourcesToAdd;
    this.targetReferenceSourceGroup = targetReferenceSourceGroup;

    this.oldReferenceSourceGroupOfReferenceSources = new ArrayList<ReferenceSourceGroup>();
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
    ReferenceSource referenceSource;
    boolean successful = false;

    try {

      for (i = 0; i < this.referenceSourcesToAdd.size(); i++) {

        referenceSource = this.referenceSourcesToAdd.get(i);

        this.oldReferenceSourceGroupOfReferenceSources.add(CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().getReferenceSourceGroup(referenceSource.getReferenceSourceID()));

        successful = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().setReferenceSourceGroup(referenceSource.getReferenceSourceID(),
                this.targetReferenceSourceGroup.getReferenceSourceGroupID(), true);
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
    ReferenceSourceGroup referenceSourceGroup;
    boolean successful = false;

    try {

      for (i = 0; i < this.referenceSourcesToAdd.size(); i++) {

        referenceSourceGroup = this.oldReferenceSourceGroupOfReferenceSources.get(i);

        if (referenceSourceGroup != null) {

          successful = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().setReferenceSourceGroup(this.referenceSourcesToAdd.get(i).getReferenceSourceID(),
                  referenceSourceGroup.getReferenceSourceGroupID(), true);

        } else {

          successful = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().setReferenceSourceGroup(this.referenceSourcesToAdd.get(i).getReferenceSourceID(), null, true);
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

      for (i = 0; i < this.referenceSourcesToAdd.size(); i++) {

        successful = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().setReferenceSourceGroup(this.referenceSourcesToAdd.get(i).getReferenceSourceID(),
                this.targetReferenceSourceGroup.getReferenceSourceGroupID(), true);
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
