/*
 * AddAuthorEdit.java
 *
 * Created on 14-mar-2011, 17:39:07
 */
package scimat.gui.commands.edit.add;

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
public class AddAuthorEdit extends KnowledgeBaseEdit {

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
  private ArrayList<Author> authorsAdded;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param authorName
   * @param fullAuthorName
   */
  public AddAuthorEdit(String authorName, String fullAuthorName) {
    super();
    
    this.authorName = authorName;
    this.fullAuthorName = fullAuthorName;
    this.authorsAdded = new ArrayList<Author>();
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

        this.authorID = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().addAuthor(authorName, fullAuthorName, true);

        if (this.authorID != null) {

          CurrentProject.getInstance().getKnowledgeBase().commit();

          this.authorsAdded.add(CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().getAuthor(this.authorID));

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

      flag = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().removeAuthor(this.authorID, true);

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

      flag = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().addAuthor(authorID, authorName, fullAuthorName, true);

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
