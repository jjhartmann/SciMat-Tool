/*
 * UpdateReferenceSourceEdit.java
 *
 * Created on 14-mar-2011, 17:40:25
 */
package scimat.gui.commands.edit.update;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateReferenceSourceEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer referenceSourceID;

  /**
   *
   */
  private String source;

  /**
   * The elements added
   */
  private ArrayList<ReferenceSource> referenceSourcesOld;
  
  private ArrayList<ReferenceSource> referenceSourcesUpdated;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public UpdateReferenceSourceEdit(Integer referenceSourceID, String source) {
    super();

    this.referenceSourceID = referenceSourceID;
    this.source = source;
    this.referenceSourcesOld = new ArrayList<ReferenceSource>();
    this.referenceSourcesUpdated = new ArrayList<ReferenceSource>();
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

    boolean successful = false;

    try {

      if (this.source == null) {

        successful = false;
        this.errorMessage = "The source can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().checkReferenceSource(source)) {

        successful = false;
        this.errorMessage = "A reference source yet exists with this source.";

      } else {

        this.referenceSourcesOld.add(CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().getReferenceSource(referenceSourceID));

        successful = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().setSource(referenceSourceID, source, true);
        
        this.referenceSourcesUpdated.add(CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().getReferenceSource(referenceSourceID));

        if (successful) {

          CurrentProject.getInstance().getKnowledgeBase().commit();

          KnowledgeBaseEventsReceiver.getInstance().fireEvents();

          successful = true;

          UndoStack.addEdit(this);

        } else {

          CurrentProject.getInstance().getKnowledgeBase().rollback();

          successful = false;
          this.errorMessage = "An error happened.";
        }
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

    boolean flag;
    ReferenceSource referenceSource;

    try {

      referenceSource = this.referenceSourcesOld.get(0);

      flag = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().setSource(referenceSource.getReferenceSourceID(),
              referenceSource.getSource(), true);

      if (flag) {

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

    boolean flag;

    try {

      flag = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO().setSource(referenceSourceID, source, true);

      if (flag) {

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
