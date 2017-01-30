/*
 * UpdateAuthorReferenceEdit.java
 *
 * Created on 14-mar-2011, 17:40:03
 */
package scimat.gui.commands.edit.update;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateAuthorReferenceEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer authorReferenceID;

  /**
   *
   */
  private String authorName;

  /**
   * The elements added
   */
  private ArrayList<AuthorReference> authorReferencesOld;
  
  private ArrayList<AuthorReference> authorReferencesUpdated;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public UpdateAuthorReferenceEdit(Integer authorReferenceID, String authorName) {
    super();

    this.authorReferenceID = authorReferenceID;
    this.authorName = authorName;
    this.authorReferencesOld = new ArrayList<AuthorReference>();
    this.authorReferencesUpdated = new ArrayList<AuthorReference>();
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

      if (this.authorName == null) {

        successful = false;
        this.errorMessage = "The name can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().checkAuthorReference(authorName)) {

        successful = false;
        this.errorMessage = "An Author reference yet exists with this name.";

      } else {

        this.authorReferencesOld.add(CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().getAuthorReference(authorReferenceID));

        successful = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().setAuthorName(authorReferenceID, authorName, true);
        
        this.authorReferencesUpdated.add(CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().getAuthorReference(authorReferenceID));

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
    AuthorReference authorReference;

    try {

      authorReference = this.authorReferencesOld.get(0);

      flag = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().setAuthorName(authorReference.getAuthorReferenceID(),
              authorReference.getAuthorName(), true);

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

      flag = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().setAuthorName(authorReferenceID, authorName, true);

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
