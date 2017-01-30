/*
 * DeleteAuthorEdit.java
 *
 * Created on 14-mar-2011, 17:39:07
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.AuthorAffiliationDAO;
import scimat.model.knowledgebase.dao.AuthorDAO;
import scimat.model.knowledgebase.dao.AuthorReferenceDAO;
import scimat.model.knowledgebase.dao.DocumentAuthorDAO;
import scimat.model.knowledgebase.entity.DocumentAuthor;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeleteAuthorEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<Author> authorsToDelete;
  private ArrayList<ArrayList<Affiliation>> affiliations = new ArrayList<ArrayList<Affiliation>>();
  private ArrayList<ArrayList<DocumentAuthor>> documentAuthors = new ArrayList<ArrayList<DocumentAuthor>>();
  private ArrayList<AuthorGroup> authorGroups = new ArrayList<AuthorGroup>();
  private ArrayList<AuthorReference> authorReferences = new ArrayList<AuthorReference>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param authorName
   * @param fullAuthorName
   */
  public DeleteAuthorEdit(ArrayList<Author> authorsToDelete) {
    super();
    
    this.authorsToDelete = authorsToDelete;
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
    AuthorDAO authorDAO;
    Author author;

    try {

      i = 0;
      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();

      while ((i < this.authorsToDelete.size()) && (successful)) {

        author = this.authorsToDelete.get(i);

        // Retrieve its relation
        this.affiliations.add(authorDAO.getAffiliations(author.getAuthorID()));
        this.documentAuthors.add(authorDAO.getDocumentAuthors(author.getAuthorID()));
        this.authorGroups.add(authorDAO.getAuthorGroup(author.getAuthorID()));
        this.authorReferences.add(authorDAO.getAuthorReference(author.getAuthorID()));

        successful = authorDAO.removeAuthor(author.getAuthorID(), true);

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
    AuthorDAO authorDAO;
    Author author;
    DocumentAuthor documentAuthor;
    AuthorReference authorReference;
    AuthorGroup authorGroup;
    AuthorAffiliationDAO authorAffiliationDAO;
    AuthorReferenceDAO authorReferenceDAO;
    DocumentAuthorDAO documentAuthorDAO;

    try {
      
      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      authorAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorAffiliationDAO();
      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();

      i = 0;

      while ((i < this.authorsToDelete.size()) && (successful)) {

        author = this.authorsToDelete.get(i);

        successful = authorDAO.addAuthor(author, true);

        j = 0;

        while ((j < this.documentAuthors.get(i).size()) && (successful)) {

          documentAuthor = this.documentAuthors.get(i).get(j);

          successful = documentAuthorDAO.addDocumentAuthor(documentAuthor.getDocument().getDocumentID(),
                                                           author.getAuthorID(),
                                                           documentAuthor.getPosition(), true);

          j++;
        }

        j = 0;

        while ((j < this.affiliations.get(i).size()) && (successful)) {

          successful = authorAffiliationDAO.addAuthorAffiliation(author.getAuthorID(),
                                                                 this.affiliations.get(i).get(j).getAffiliationID(), true);

          j++;
        }

        authorGroup = this.authorGroups.get(i);

        if ((authorGroup != null) && (successful)) {

          successful = authorDAO.setAuthorGroup(author.getAuthorID(),
                                                authorGroup.getAuthorGroupID(), true);
        }

        authorReference = this.authorReferences.get(i);

        if ((authorReference != null) && (successful)) {

          successful = authorReferenceDAO.setAuthor(authorReference.getAuthorReferenceID(),
                                                    author.getAuthorID(), true);
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
    AuthorDAO authorDAO;
    Author author;

    try {

      i = 0;
      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();

      while ((i < this.authorsToDelete.size()) && (successful)) {

        author = this.authorsToDelete.get(i);

        successful = authorDAO.removeAuthor(author.getAuthorID(), true);

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
