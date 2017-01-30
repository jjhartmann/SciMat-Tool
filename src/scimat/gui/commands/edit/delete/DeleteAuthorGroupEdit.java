/*
 * DeleteAuthorGroupEdit.java
 *
 * Created on 14-mar-2011, 17:42:43
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.AuthorDAO;
import scimat.model.knowledgebase.dao.AuthorGroupDAO;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeleteAuthorGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<AuthorGroup> authorGroupsToDelete;
  private ArrayList<ArrayList<Author>> authors = new ArrayList<ArrayList<Author>>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public DeleteAuthorGroupEdit(ArrayList<AuthorGroup> authorGroupsToDelete) {
    super();
    
    this.authorGroupsToDelete = authorGroupsToDelete;
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
    AuthorGroupDAO authorGroupDAO;
    AuthorGroup authorGroup;

    try {

      i = 0;
      authorGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO();

      while ((i < this.authorGroupsToDelete.size()) && (successful)) {

        authorGroup = this.authorGroupsToDelete.get(i);

        // Retrieve its relation
        this.authors.add(authorGroupDAO.getAuthors(authorGroup.getAuthorGroupID()));

        successful = authorGroupDAO.removeAuthorGroup(authorGroup.getAuthorGroupID(), true);

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
    AuthorGroupDAO authorGroupDAO;
    AuthorDAO authorDAO;
    AuthorGroup authorGroup;
    Author author;

    try {

      authorGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO();
      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();

      i = 0;

      while ((i < this.authorGroupsToDelete.size()) && (successful)) {

        authorGroup = this.authorGroupsToDelete.get(i);

        successful = authorGroupDAO.addAuthorGroup(authorGroup, true);

        j = 0;

        while ((j < this.authors.get(i).size()) && (successful)) {

          author = this.authors.get(i).get(j);

          successful = authorDAO.setAuthorGroup(author.getAuthorID(),
                                                authorGroup.getAuthorGroupID(), true);

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
    AuthorGroupDAO authorGroupDAO;
    AuthorGroup authorGroup;

    try {

      i = 0;
      authorGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO();

      while ((i < this.authorGroupsToDelete.size()) && (successful)) {

        authorGroup = this.authorGroupsToDelete.get(i);

        successful = authorGroupDAO.removeAuthorGroup(authorGroup.getAuthorGroupID(), true);

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
