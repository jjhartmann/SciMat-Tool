/*
 * UpdateAuthorEdit.java
 *
 * Created on 14-mar-2011, 17:39:07
 */
package scimat.gui.commands.edit.update;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateAuthorEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The author's ID
   */
  private Integer authorID;

  /**
   * The author's name.
   */
  private String authorName;

  /**
   * The author's full name when available
   */
  private String fullAuthorName;

  /**
   * The elements added
   */
  private ArrayList<Author> authorsUpdated;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param authorName
   * @param fullAuthorName
   */
  public UpdateAuthorEdit(Integer authorID, String authorName, String fullAuthorName) {
    super();

    this.authorID = authorID;
    this.authorName = authorName;
    this.fullAuthorName = fullAuthorName;
    this.authorsUpdated = new ArrayList<Author>();
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

      if ((this.authorName == null) || (this.fullAuthorName == null)) {

        successful = false;
        this.errorMessage = "The name and full name can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().checkAuthor(authorName, fullAuthorName)) {

        successful = false;
        this.errorMessage = "An Author yet exists with this name and full name.";

      } else {

        this.authorsUpdated.add(CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().getAuthor(this.authorID));

        successful = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().updateAuthor(authorID, authorName, fullAuthorName, true);

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
    Author author;

    try {

      author = this.authorsUpdated.get(0);

      flag = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().updateAuthor(author.getAuthorID(),
              author.getAuthorName(), author.getFullAuthorName(), true);

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

      flag = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().updateAuthor(authorID, authorName, fullAuthorName, true);

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
