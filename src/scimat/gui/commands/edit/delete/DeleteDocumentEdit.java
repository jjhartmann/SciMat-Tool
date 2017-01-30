/*
 * DeleteDocumentEdit.java
 *
 * Created on 14-mar-2011, 17:38:12
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.DocumentAffiliationDAO;
import scimat.model.knowledgebase.dao.DocumentAuthorDAO;
import scimat.model.knowledgebase.dao.DocumentDAO;
import scimat.model.knowledgebase.dao.DocumentReferenceDAO;
import scimat.model.knowledgebase.dao.DocumentWordDAO;
import scimat.model.knowledgebase.entity.DocumentAuthor;
import scimat.model.knowledgebase.entity.DocumentWord;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @document mjcobo
 */
public class DeleteDocumentEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<Document> documentsToDelete;
  private ArrayList<ArrayList<Affiliation>> affiliations = new ArrayList<ArrayList<Affiliation>>();
  private ArrayList<ArrayList<DocumentAuthor>> documentAuthors = new ArrayList<ArrayList<DocumentAuthor>>();
  private ArrayList<ArrayList<Reference>> references = new ArrayList<ArrayList<Reference>>();
  private ArrayList<ArrayList<DocumentWord>> documentWords = new ArrayList<ArrayList<DocumentWord>>();
  private ArrayList<PublishDate> publishDates = new ArrayList<PublishDate>();
  private ArrayList<Journal> journals = new ArrayList<Journal>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public DeleteDocumentEdit(ArrayList<Document> documentsToDelete) {
    super();
    
    this.documentsToDelete = documentsToDelete;
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
    DocumentDAO documentDAO;
    Document document;

    try {

      i = 0;
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();

      while ((i < this.documentsToDelete.size()) && (successful)) {

        document = this.documentsToDelete.get(i);

        // Retrieve its relation
        this.affiliations.add(documentDAO.getAffiliations(document.getDocumentID()));
        this.documentAuthors.add(documentDAO.getDocumentAuthors(document.getDocumentID()));
        this.documentWords.add(documentDAO.getDocumentWords(document.getDocumentID()));
        this.publishDates.add(documentDAO.getPublishDate(document.getDocumentID()));
        this.journals.add(documentDAO.getJournal(document.getDocumentID()));
        this.references.add(documentDAO.getReferences(document.getDocumentID()));

        successful = documentDAO.removeDocument(document.getDocumentID(), true);

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
    DocumentDAO documentDAO;
    Document document;
    DocumentAuthor documentAuthor;
    DocumentWord documentWord;
    PublishDate publishDate;
    Journal journal;
    DocumentAffiliationDAO documentAffiliationDAO;
    DocumentReferenceDAO documentReferenceDAO;
    DocumentAuthorDAO documentAuthorDAO;
    DocumentWordDAO documentWordDAO;

    try {


      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      documentAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAffiliationDAO();
      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      documentReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentReferenceDAO();
      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();

      i = 0;

      while ((i < this.documentsToDelete.size()) && (successful)) {

        document = this.documentsToDelete.get(i);

        successful = documentDAO.addDocument(document, true);

        j = 0;

        while ((j < this.documentAuthors.get(i).size()) && (successful)) {

          documentAuthor = this.documentAuthors.get(i).get(j);

          successful = documentAuthorDAO.addDocumentAuthor(document.getDocumentID(),
                                                           documentAuthor.getAuthor().getAuthorID(),
                                                           documentAuthor.getPosition(), true);

          j++;
        }

        j = 0;

        while ((j < this.affiliations.get(i).size()) && (successful)) {

          successful = documentAffiliationDAO.addDocumentAffiliation(document.getDocumentID(),
                                                                    this.affiliations.get(i).get(j).getAffiliationID(), true);

          j++;
        }

        j = 0;

        while ((j < this.documentWords.get(i).size()) && (successful)) {

          documentWord = this.documentWords.get(i).get(j);

          successful = documentWordDAO.addDocumentWord(document.getDocumentID(),
                                                       documentWord.getWord().getWordID(),
                                                       documentWord.isAuthorKeyword(),
                                                       documentWord.isSourceKeyword(),
                                                       documentWord.isAddedKeyword(), true);

          j++;
        }

        j = 0;

        while ((j < this.references.get(i).size()) && (successful)) {

          successful = documentReferenceDAO.addDocumentReference(document.getDocumentID(),
                                                                 this.references.get(i).get(j).getReferenceID(), true);

          j++;
        }

        publishDate = this.publishDates.get(i);

        if ((publishDate != null) && (successful)) {

          successful = documentDAO.setPublishDate(document.getDocumentID(),
                                                  publishDate.getPublishDateID(), true);
        }

        journal = this.journals.get(i);

        if ((journal != null) && (successful)) {

          successful = documentDAO.setJournal(document.getDocumentID(),
                                              journal.getJournalID(), true);
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
    DocumentDAO documentDAO;
    Document document;

    try {

      i = 0;
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();

      while ((i < this.documentsToDelete.size()) && (successful)) {

        document = this.documentsToDelete.get(i);

        successful = documentDAO.removeDocument(document.getDocumentID(), true);

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
