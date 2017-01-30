/*
 * AddReferenceEdit.java
 *
 * Created on 14-mar-2011, 17:38:58
 */
package scimat.gui.commands.edit.add;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddReferenceEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer referenceID;

  /**
   *
   */
  private String fullReference;

  /**
   *
   */
  private String volume;

  /**
   *
   */
  private String issue;

  /**
   *
   */
  private String page;

  /**
   *
   */
  private String year;

  /**
   *
   */
  private String doi;

  /**
   *
   */
  private String format;

  /**
   * The elements added
   */
  private ArrayList<Reference> referencesAdded;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public AddReferenceEdit(String fullReference, String volume, String issue, 
          String page, String year, String doi, String format) {
    super();
    
    this.fullReference = fullReference;
    this.volume = volume;
    this.issue = issue;
    this.page = page;
    this.year = year;
    this.doi = doi;
    this.format = format;
    this.referencesAdded = new ArrayList<Reference>();
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

      if (this.fullReference == null) {
        
        successful = false;
        this.errorMessage = "The full reference can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().checkReference(fullReference)) {

        successful = false;
        this.errorMessage = "A reference yet exists with this full reference.";

      } else {

        this.referenceID = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().addReference(fullReference,
                volume, issue, page, year, doi, format, true);

        if (this.referenceID != null) {

          CurrentProject.getInstance().getKnowledgeBase().commit();

          this.referencesAdded.add(CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().getReference(referenceID));

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

    try {

      flag = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().removeReference(referenceID, true);

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

      flag = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().addReference(referenceID,
              fullReference, volume, issue, page, year, doi, format, true);

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
