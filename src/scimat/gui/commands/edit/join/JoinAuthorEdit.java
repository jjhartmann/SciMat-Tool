/*
 * JoinAuthorEdit.java
 *
 * Created on 26-abr-2011, 18:06:00
 */
package scimat.gui.commands.edit.join;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
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
public class JoinAuthorEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<Author> authorsToMove;
  private Author targetAuthor;

  private ArrayList<ArrayList<Affiliation>> affiliationOfSources = new ArrayList<ArrayList<Affiliation>>();
  private ArrayList<ArrayList<DocumentAuthor>> documentAuthorOfSources = new ArrayList<ArrayList<DocumentAuthor>>();
  private ArrayList<AuthorGroup> authorGroupOfSources = new ArrayList<AuthorGroup>();
  private ArrayList<AuthorReference> authorReferenceOfSources = new ArrayList<AuthorReference>();

  private TreeSet<Affiliation> affiliationOfTarget = new TreeSet<Affiliation>();
  private TreeSet<DocumentAuthor> documentAuthorOfTarget = new TreeSet<DocumentAuthor>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param authorsToMove
   * @param targetAuthor
   */
  public JoinAuthorEdit(ArrayList<Author> authorsToMove, Author targetAuthor) {

    this.authorsToMove = authorsToMove;
    this.targetAuthor = targetAuthor;
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
    AuthorAffiliationDAO authorAffiliationDAO;
    DocumentAuthorDAO documentAuthorDAO;
    AuthorReferenceDAO authorReferenceDAO;
    Author author;
    AuthorReference authorReference;
    DocumentAuthor documentAuthor;
    ArrayList<Affiliation> affiliations;
    ArrayList<DocumentAuthor> documentAuthors;

    try {

      i = 0;

      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      authorAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorAffiliationDAO();
      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();

      // Retrieve the realations of the target item.
      this.affiliationOfTarget = new TreeSet<Affiliation>(authorDAO.getAffiliations(this.targetAuthor.getAuthorID()));
      this.documentAuthorOfTarget = new TreeSet<DocumentAuthor>(authorDAO.getDocumentAuthors(this.targetAuthor.getAuthorID()));

      while ((i < this.authorsToMove.size()) && (successful)) {

        author = this.authorsToMove.get(i);

        // Retrieve the relations of the source items
        affiliations = authorDAO.getAffiliations(author.getAuthorID());
        this.affiliationOfSources.add(affiliations);

        documentAuthors = authorDAO.getDocumentAuthors(author.getAuthorID());
        this.documentAuthorOfSources.add(documentAuthors);
        
        this.authorGroupOfSources.add(authorDAO.getAuthorGroup(author.getAuthorID()));

        authorReference = authorDAO.getAuthorReference(author.getAuthorID());
        this.authorReferenceOfSources.add(authorReference);

        // Do the join
        j = 0;

        successful = authorDAO.removeAuthor(author.getAuthorID(), true);

        while ((j < affiliations.size()) && (successful)) {

          // If the target element is not associated with this element we perform the association.
          if (! authorAffiliationDAO.checkAuthorAffiliation(this.targetAuthor.getAuthorID(), 
                  affiliations.get(j).getAffiliationID())) {

            successful = authorAffiliationDAO.addAuthorAffiliation(this.targetAuthor.getAuthorID(), 
                    affiliations.get(j).getAffiliationID(), true);
          }

          j ++;
        }

        j = 0;

        while ((j < documentAuthors.size()) && (successful)) {

          documentAuthor = documentAuthors.get(j);

          // If the target element is not associated with this element we perform the association.
          if (! documentAuthorDAO.checkDocumentAuthor(documentAuthor.getDocument().getDocumentID(), 
                  this.targetAuthor.getAuthorID())) {

            successful = documentAuthorDAO.addDocumentAuthor(documentAuthor.getDocument().getDocumentID(), 
                    this.targetAuthor.getAuthorID(), documentAuthor.getPosition(), true);
          }

          j ++;
        }
        
        if ((authorReference != null) && (successful)) {

          successful = authorReferenceDAO.setAuthor(authorReference.getAuthorReferenceID(), 
                  this.targetAuthor.getAuthorID(), true);
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
    TreeSet<Affiliation> tmpAffiliations;
    TreeSet<DocumentAuthor> tmpDocumentAuthors;
    AuthorDAO authorDAO;
    Author author;
    AuthorAffiliationDAO authorAffiliationDAO;
    DocumentAuthorDAO documentAuthorDAO;
    DocumentAuthor documentAuthor;
    AuthorReferenceDAO authorReferenceDAO;
    Iterator<Affiliation> itAffiliation;
    Iterator<DocumentAuthor> itDocumentAuthor;

    try {

      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      authorAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorAffiliationDAO();
      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();

      // Restore the target entity
      
      tmpAffiliations = new TreeSet<Affiliation>(authorDAO.getAffiliations(this.targetAuthor.getAuthorID()));
      tmpAffiliations.removeAll(this.affiliationOfTarget);

      itAffiliation = tmpAffiliations.iterator();

      while ((itAffiliation.hasNext()) && (successful)) {

        successful = authorAffiliationDAO.removeAuthorAffiliation(this.targetAuthor.getAuthorID(), itAffiliation.next().getAffiliationID(), true);
      }

      tmpDocumentAuthors = new TreeSet<DocumentAuthor>(authorDAO.getDocumentAuthors(this.targetAuthor.getAuthorID()));
      tmpDocumentAuthors.removeAll(this.documentAuthorOfTarget);

      itDocumentAuthor = tmpDocumentAuthors.iterator();

      while ((itDocumentAuthor.hasNext()) && (successful)) {
        
        successful = documentAuthorDAO.removeDocumentAuthor(itDocumentAuthor.next().getDocument().getDocumentID(), 
                this.targetAuthor.getAuthorID(), true);
      }

      i = 0;
      
      // Restore the source entities

      while ((i < this.authorsToMove.size()) && (successful)) {

        author = this.authorsToMove.get(i);

        successful = authorDAO.addAuthor(author, true);

        j = 0;

        while ((j < this.documentAuthorOfSources.get(i).size()) && (successful)) {

          documentAuthor = this.documentAuthorOfSources.get(i).get(j);

          successful = documentAuthorDAO.addDocumentAuthor(documentAuthor.getDocument().getDocumentID(),
                                                           author.getAuthorID(), documentAuthor.getPosition(), true);

          j++;
        }

        j = 0;

        while ((j < this.affiliationOfSources.get(i).size()) && (successful)) {

          successful = authorAffiliationDAO.addAuthorAffiliation(author.getAuthorID(), 
                  this.affiliationOfSources.get(i).get(j).getAffiliationID(), true);

          j++;
        }

        if ((this.authorGroupOfSources.get(i) != null) && (successful)) {

          successful = authorDAO.setAuthorGroup(author.getAuthorID(), 
                  this.authorGroupOfSources.get(i).getAuthorGroupID(), true);
        }

        if ((this.authorReferenceOfSources.get(i) != null) && (successful)) {

          successful = authorReferenceDAO.setAuthor(this.authorReferenceOfSources.get(i).getAuthorReferenceID(), 
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

    boolean successful = true;
    int i, j;
    AuthorDAO authorDAO;
    AuthorAffiliationDAO authorAffiliationDAO;
    DocumentAuthorDAO documentAuthorDAO;
    AuthorReferenceDAO authorReferenceDAO;
    Author author;
    DocumentAuthor documentAuthor;
    ArrayList<Affiliation> affiliations;
    ArrayList<DocumentAuthor> documentAuthors;
    AuthorReference authorReference;

    try {

      i = 0;

      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      authorAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorAffiliationDAO();
      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();

      while ((i < this.authorsToMove.size()) && (successful)) {

        author = this.authorsToMove.get(i);

        // Retrieve the relations of the source items
        affiliations = this.affiliationOfSources.get(i);
        documentAuthors = this.documentAuthorOfSources.get(i);
        authorReference = this.authorReferenceOfSources.get(i);

        // Do the join
        j = 0;

        successful = authorDAO.removeAuthor(author.getAuthorID(), true);

        while ((j < affiliations.size()) && (successful)) {

          // If the target element is not associated with this element we perform the association.
          if (! authorAffiliationDAO.checkAuthorAffiliation(this.targetAuthor.getAuthorID(), 
                  affiliations.get(j).getAffiliationID())) {

            successful = authorAffiliationDAO.addAuthorAffiliation(this.targetAuthor.getAuthorID(), 
                    affiliations.get(j).getAffiliationID(), true);
          }

          j ++;
        }

        j = 0;

        while ((j < documentAuthors.size()) && (successful)) {

          documentAuthor = documentAuthors.get(j);

          // If the target element is not associated with this element we perform the association.
          if (! documentAuthorDAO.checkDocumentAuthor(documentAuthor.getDocument().getDocumentID(), 
                  this.targetAuthor.getAuthorID())) {

            successful = documentAuthorDAO.addDocumentAuthor(documentAuthor.getDocument().getDocumentID(), 
                    this.targetAuthor.getAuthorID(), documentAuthor.getPosition(), true);
          }

          j ++;
        }

        if ((authorReference != null) && (successful)) {

          successful = authorReferenceDAO.setAuthor(authorReference.getAuthorReferenceID(), 
                  this.targetAuthor.getAuthorID(), true);
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
