/*
 * JoinAuthorGroupEdit.java
 *
 * Created on 18-may-2011, 13:43:52
 */
package scimat.gui.commands.edit.join;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
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
public class JoinAuthorGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<AuthorGroup> authorGroupsToMove;
  private AuthorGroup targetAuthorGroup;

  private ArrayList<ArrayList<Author>> authorsOfSources = new ArrayList<ArrayList<Author>>();

  private TreeSet<Author> authorsOfTarget = new TreeSet<Author>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param authorGroupsToMove
   * @param targetAuthorGroup
   */
  public JoinAuthorGroupEdit(ArrayList<AuthorGroup> authorGroupsToMove, AuthorGroup targetAuthorGroup) {

    this.authorGroupsToMove = authorGroupsToMove;
    this.targetAuthorGroup = targetAuthorGroup;
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
    int i, j;
    AuthorDAO authorDAO;
    AuthorGroupDAO authorGroupDAO;
    AuthorGroup authorGroup;
    ArrayList<Author> authors;

    try {

      i = 0;

      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      authorGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO();

      // Retrieve the realations of the target item.
      this.authorsOfTarget = new TreeSet<Author>(authorGroupDAO.getAuthors(this.targetAuthorGroup.getAuthorGroupID()));

      while ((i < this.authorGroupsToMove.size()) && (successful)) {

        authorGroup = this.authorGroupsToMove.get(i);

        // Retrieve the relations of the source items
        authors = authorGroupDAO.getAuthors(authorGroup.getAuthorGroupID());
        this.authorsOfSources.add(authors);

        // Do the join
        j = 0;

        successful = authorGroupDAO.removeAuthorGroup(authorGroup.getAuthorGroupID(), true);

        while ((j < authors.size()) && (successful)) {
          
          successful = authorDAO.setAuthorGroup(authors.get(j).getAuthorID(), 
                  this.targetAuthorGroup.getAuthorGroupID(), true);

          j ++;
        }

        i ++;
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
    TreeSet<Author> tmpAuthors;
    AuthorGroupDAO authorGroupDAO;
    AuthorDAO authorDAO;
    AuthorGroup authorGroup;
    Author author;
    Iterator<Author> itAuthor;

    try {

      authorGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO();
      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      

      tmpAuthors = new TreeSet<Author>(authorGroupDAO.getAuthors(this.targetAuthorGroup.getAuthorGroupID()));
      tmpAuthors.removeAll(this.authorsOfTarget);

      itAuthor = tmpAuthors.iterator();

      while ((itAuthor.hasNext()) && (successful)) {

        successful = authorDAO.setAuthorGroup(itAuthor.next().getAuthorID(), null, true);
      }

      i = 0;

      while ((i < this.authorGroupsToMove.size()) && (successful)) {

        authorGroup = this.authorGroupsToMove.get(i);

        successful = authorGroupDAO.addAuthorGroup(authorGroup, true);

        j = 0;

        while ((j < this.authorsOfSources.get(i).size()) && (successful)) {

          author = this.authorsOfSources.get(i).get(j);

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

    boolean successful = true;
    int i, j;
    AuthorDAO authorDAO;
    AuthorGroupDAO authorGroupDAO;
    AuthorGroup authorGroup;
    ArrayList<Author> authors;

    try {

      i = 0;

      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      authorGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO();

      while ((i < this.authorGroupsToMove.size()) && (successful)) {

        authorGroup = this.authorGroupsToMove.get(i);

        // Retrieve the relations of the source items
        authors = this.authorsOfSources.get(i) ;

        // Do the join
        j = 0;

        successful = authorGroupDAO.removeAuthorGroup(authorGroup.getAuthorGroupID(), true);

        while ((j < authors.size()) && (successful)) {
          
          successful = authorDAO.setAuthorGroup(authors.get(j).getAuthorID(), this.targetAuthorGroup.getAuthorGroupID(), true);

          j ++;
        }

        i ++;
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();
        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

        UndoStack.addEdit(this);

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
