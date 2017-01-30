/*
 * JoinAffiliationEdit.java
 *
 * Created on 11-abr-2011, 20:42:49
 */
package scimat.gui.commands.edit.join;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.AffiliationDAO;
import scimat.model.knowledgebase.dao.AuthorAffiliationDAO;
import scimat.model.knowledgebase.dao.DocumentAffiliationDAO;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class JoinAffiliationEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<Affiliation> affiliationsToMove;
  private Affiliation targetAffiliation;

  private ArrayList<ArrayList<Author>> authorOfSources = new ArrayList<ArrayList<Author>>();
  private ArrayList<ArrayList<Document>> documentOfSources = new ArrayList<ArrayList<Document>>();

  private TreeSet<Author> authorsOfTarget = new TreeSet<Author>();
  private TreeSet<Document> documentsOfTarget = new TreeSet<Document>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param affiliationsToMove
   * @param targetAffiliation
   */
  public JoinAffiliationEdit(ArrayList<Affiliation> affiliationsToMove, Affiliation targetAffiliation) {

    this.affiliationsToMove = affiliationsToMove;
    this.targetAffiliation = targetAffiliation;
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
    AffiliationDAO affiliationDAO;
    AuthorAffiliationDAO authorAffiliationDAO;
    DocumentAffiliationDAO documentAffiliationDAO;
    Affiliation affiliation;
    ArrayList<Author> authors;
    ArrayList<Document> documents;

    try {

      i = 0;
      affiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAffiliationDAO();
      authorAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorAffiliationDAO();
      documentAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAffiliationDAO();

      // Retrieve the realations of the target item.
      this.authorsOfTarget = new TreeSet<Author>(affiliationDAO.getAuthors(this.targetAffiliation.getAffiliationID()));
      this.documentsOfTarget = new TreeSet<Document>(affiliationDAO.getDocuments(this.targetAffiliation.getAffiliationID()));

      while ((i < this.affiliationsToMove.size()) && (successful)) {

        affiliation = this.affiliationsToMove.get(i);

        // Retrieve the relations of the source items
        authors = affiliationDAO.getAuthors(affiliation.getAffiliationID());
        this.authorOfSources.add(authors);

        documents = affiliationDAO.getDocuments(affiliation.getAffiliationID());
        this.documentOfSources.add(documents);

        // Do the join
        j = 0;

        successful = affiliationDAO.removeAffiliation(affiliation.getAffiliationID(), true);

        while ((j < authors.size()) && (successful)) {

          // If the target element is not associated with this element we perform the association.
          if (! authorAffiliationDAO.checkAuthorAffiliation(authors.get(j).getAuthorID(), 
                  this.targetAffiliation.getAffiliationID())) {

            successful = authorAffiliationDAO.addAuthorAffiliation(authors.get(j).getAuthorID(), 
                    this.targetAffiliation.getAffiliationID(), true);
          }

          j ++;
        }

        j = 0;

        while ((j < documents.size()) && (successful)) {

          // If the target element is not associated with this element we perform the association.
          if (! documentAffiliationDAO.checkDocumentAffiliation(documents.get(j).getDocumentID(), 
                  this.targetAffiliation.getAffiliationID())) {

            successful = documentAffiliationDAO.addDocumentAffiliation(documents.get(j).getDocumentID(), 
                    this.targetAffiliation.getAffiliationID(), true);
          }

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
    TreeSet<Document> tmpDocuments;
    AffiliationDAO affiliationDAO;
    Affiliation affiliation;
    AuthorAffiliationDAO authorAffiliationDAO;
    DocumentAffiliationDAO documentAffiliationDAO;
    Iterator<Author> itAuthor;
    Iterator<Document> itDocument;

    try {

      affiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAffiliationDAO();
      authorAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorAffiliationDAO();
      documentAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAffiliationDAO();

      tmpAuthors = new TreeSet<Author>(affiliationDAO.getAuthors(this.targetAffiliation.getAffiliationID()));
      tmpAuthors.removeAll(this.authorsOfTarget);

      itAuthor = tmpAuthors.iterator();

      while ((itAuthor.hasNext()) && (successful)) {

        successful = authorAffiliationDAO.removeAuthorAffiliation(itAuthor.next().getAuthorID(), this.targetAffiliation.getAffiliationID(), true);
      }

      tmpDocuments = new TreeSet<Document>(affiliationDAO.getDocuments(this.targetAffiliation.getAffiliationID()));
      tmpDocuments.removeAll(this.documentsOfTarget);

      itDocument = tmpDocuments.iterator();

      while ((itDocument.hasNext()) && (successful)) {

        successful = documentAffiliationDAO.removeDocumentAffiliation(itDocument.next().getDocumentID(), this.targetAffiliation.getAffiliationID(), true);
      }

      i = 0;

      while ((i < this.affiliationsToMove.size()) && (successful)) {

        affiliation = this.affiliationsToMove.get(i);

        successful = affiliationDAO.addAffiliation(affiliation, true);

        j = 0;

        while ((j < this.documentOfSources.get(i).size()) && (successful)) {

          successful = documentAffiliationDAO.addDocumentAffiliation(this.documentOfSources.get(i).get(j).getDocumentID(),
                                                                    affiliation.getAffiliationID(), true);

          j++;
        }

        j = 0;

        while ((j < this.authorOfSources.get(i).size()) && (successful)) {

          successful = authorAffiliationDAO.addAuthorAffiliation(this.authorOfSources.get(i).get(j).getAuthorID(),
                                                                 affiliation.getAffiliationID(), true);

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

    int i, j;
    boolean successful = true;
    AffiliationDAO affiliationDAO;
    Affiliation affiliation;
    ArrayList<Author> authors;
    ArrayList<Document> documents;

    AuthorAffiliationDAO authorAffiliationDAO;
    DocumentAffiliationDAO documentAffiliationDAO;

    try {

      i = 0;
      affiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAffiliationDAO();
      authorAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorAffiliationDAO();
      documentAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAffiliationDAO();

      while ((i < this.affiliationsToMove.size()) && (successful)) {

        affiliation = this.affiliationsToMove.get(i);

        authors = this.authorOfSources.get(i);
        documents = this.documentOfSources.get(i);

        // Do the join
        j = 0;

        successful = affiliationDAO.removeAffiliation(affiliation.getAffiliationID(), true);

        while ((j < authors.size()) && (successful)) {

          // If the target element is not associated with this element we perform the association.
          if (! authorAffiliationDAO.checkAuthorAffiliation(authors.get(j).getAuthorID(),
                  this.targetAffiliation.getAffiliationID())) {

            successful = authorAffiliationDAO.addAuthorAffiliation(authors.get(j).getAuthorID(), 
                    this.targetAffiliation.getAffiliationID(), true);
          }

          j ++;
        }

        j = 0;

        while ((j < documents.size()) && (successful)) {
          
          // If the target element is not associated with this element we perform the association.
          if (! documentAffiliationDAO.checkDocumentAffiliation(documents.get(j).getDocumentID(), 
                  this.targetAffiliation.getAffiliationID())) {

            successful = documentAffiliationDAO.addDocumentAffiliation(documents.get(j).getDocumentID(), 
                    this.targetAffiliation.getAffiliationID(), true);
          }

          j ++;
        }

        i ++;
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
