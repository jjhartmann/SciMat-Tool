/*
 * DeleteAuthorReferenceEdit.java
 *
 * Created on 14-mar-2011, 17:40:03
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.AuthorReferenceDAO;
import scimat.model.knowledgebase.dao.AuthorReferenceReferenceDAO;
import scimat.model.knowledgebase.entity.AuthorReferenceReference;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeleteAuthorReferenceEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<AuthorReference> authorReferencesToDelete;
  private ArrayList<ArrayList<AuthorReferenceReference>> authorReferenceReferences = new ArrayList<ArrayList<AuthorReferenceReference>>();
  private ArrayList<Author> authors = new ArrayList<Author>();
  private ArrayList<AuthorReferenceGroup> authorReferenceGroups = new ArrayList<AuthorReferenceGroup>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public DeleteAuthorReferenceEdit(ArrayList<AuthorReference> authorReferencesToDelete) {
    super();
    
    this.authorReferencesToDelete = authorReferencesToDelete;
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
    AuthorReferenceDAO authorReferenceDAO;
    AuthorReference authorReference;

    try {

      i = 0;
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();

      while ((i < this.authorReferencesToDelete.size()) && (successful)) {

        authorReference = this.authorReferencesToDelete.get(i);

        // Retrieve its relation
        this.authorReferenceReferences.add(authorReferenceDAO.getReferences(authorReference.getAuthorReferenceID()));
        this.authors.add(authorReferenceDAO.getAuthor(authorReference.getAuthorReferenceID()));
        this.authorReferenceGroups.add(authorReferenceDAO.getAuthorReferenceGroup(authorReference.getAuthorReferenceID()));

        successful = authorReferenceDAO.removeAuthorReference(authorReference.getAuthorReferenceID(), true);

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
    AuthorReferenceDAO authorReferenceDAO;
    AuthorReferenceReferenceDAO authorReferenceReferenceDAO;
    AuthorReference authorReference;
    Author author;
    AuthorReferenceReference authorReferenceReference;
    AuthorReferenceGroup authorReferenceGroup;
    

    try {

      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
      authorReferenceReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceReferenceDAO();

      i = 0;

      while ((i < this.authorReferencesToDelete.size()) && (successful)) {

        authorReference = this.authorReferencesToDelete.get(i);

        successful = authorReferenceDAO.addAuthorReference(authorReference, true);

        j = 0;

        while ((j < this.authorReferenceReferences.get(i).size()) && (successful)) {

          authorReferenceReference = this.authorReferenceReferences.get(i).get(j);

          successful = authorReferenceReferenceDAO.addAuthorReferenceReference(authorReferenceReference.getAuthorReference().getAuthorReferenceID(),
                                                                               authorReference.getAuthorReferenceID(),
                                                                               authorReferenceReference.getPosition(), true);

          j++;
        }

        author = authorReferenceDAO.getAuthor(authorReference.getAuthorReferenceID());

        if ((author != null) && (successful)) {

          successful = authorReferenceDAO.setAuthor(authorReference.getAuthorReferenceID(),
                                                    author.getAuthorID(), true);
        }

        authorReferenceGroup = authorReferenceDAO.getAuthorReferenceGroup(authorReference.getAuthorReferenceID());

        if ((authorReferenceGroup != null) && (successful)) {

          successful = authorReferenceDAO.setAuthorReferenceGroup(authorReference.getAuthorReferenceID(),
                                                                  authorReferenceGroup.getAuthorReferenceGroupID(), true);
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
    AuthorReferenceDAO authorReferenceDAO;
    AuthorReference authorReference;

    try {

      i = 0;
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();

      while ((i < this.authorReferencesToDelete.size()) && (successful)) {

        authorReference = this.authorReferencesToDelete.get(i);

        successful = authorReferenceDAO.removeAuthorReference(authorReference.getAuthorReferenceID(), true);

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
