/*
 * JoinDocumentEdit.java
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
 * @author mjcobo
 */
public class JoinDocumentEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<Document> documentsToMove;
  private Document targetDocument;

  private ArrayList<ArrayList<DocumentAuthor>> documentAuthorsOfSources = new ArrayList<ArrayList<DocumentAuthor>>();
  private ArrayList<ArrayList<DocumentWord>> documentWordsOfSources = new ArrayList<ArrayList<DocumentWord>>();
  private ArrayList<ArrayList<Affiliation>> affiliationsOfSources = new ArrayList<ArrayList<Affiliation>>();
  private ArrayList<ArrayList<Reference>> referencesOfSources = new ArrayList<ArrayList<Reference>>();
  private ArrayList<Journal> journalOfSources = new ArrayList<Journal>();
  private ArrayList<PublishDate> publishDateOfSources = new ArrayList<PublishDate>();

  private TreeSet<DocumentAuthor> documentAuthorsOfTarget = new TreeSet<DocumentAuthor>();
  private TreeSet<DocumentWord> documentWordsOfTarget = new TreeSet<DocumentWord>();
  private TreeSet<Affiliation> affiliationsOfTarget = new TreeSet<Affiliation>();
  private TreeSet<Reference> referencesOfTarget = new TreeSet<Reference>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param documentsToMove
   * @param targetDocument
   */
  public JoinDocumentEdit(ArrayList<Document> documentsToMove, Document targetDocument) {

    this.documentsToMove = documentsToMove;
    this.targetDocument = targetDocument;
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
    DocumentDAO documentDAO;
    DocumentAuthorDAO documentAuthorDAO;
    DocumentAffiliationDAO documentAffiliationDAO;
    DocumentWordDAO documentWordDAO;
    DocumentReferenceDAO documentReferenceDAO;
    Document document;
    DocumentWord documentWord;
    DocumentAuthor documentAuthor;
    ArrayList<DocumentAuthor> documentAuthors;
    ArrayList<DocumentWord> documentWords;
    ArrayList<Affiliation> affiliations;
    ArrayList<Reference> references;

    try {

      i = 0;
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      documentAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAffiliationDAO();
      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();
      documentReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentReferenceDAO();

      // Retrieve the realations of the target item.
      this.documentAuthorsOfTarget = new TreeSet<DocumentAuthor>(documentDAO.getDocumentAuthors(this.targetDocument.getDocumentID()));
      this.documentWordsOfTarget = new TreeSet<DocumentWord>(documentDAO.getDocumentWords(this.targetDocument.getDocumentID()));
      this.affiliationsOfTarget = new TreeSet<Affiliation>(documentDAO.getAffiliations(this.targetDocument.getDocumentID()));
      this.referencesOfTarget = new TreeSet<Reference>(documentDAO.getReferences(this.targetDocument.getDocumentID()));

      while ((i < this.documentsToMove.size()) && (successful)) {

        document = this.documentsToMove.get(i);

        // Retrieve the relations of the source items
        documentWords = documentDAO.getDocumentWords(document.getDocumentID());
        this.documentWordsOfSources.add(documentWords);

        documentAuthors = documentDAO.getDocumentAuthors(document.getDocumentID());
        this.documentAuthorsOfSources.add(documentAuthors);
        
        affiliations = documentDAO.getAffiliations(document.getDocumentID());
        this.affiliationsOfSources.add(affiliations);
        
        references = documentDAO.getReferences(document.getDocumentID());
        this.referencesOfSources.add(references);
        
        this.journalOfSources.add(documentDAO.getJournal(document.getDocumentID()));
        
        this.publishDateOfSources.add(documentDAO.getPublishDate(document.getDocumentID()));

        // Do the join
        j = 0;

        successful = documentDAO.removeDocument(document.getDocumentID(), true);

        while ((j < documentWords.size()) && (successful)) {

          documentWord = documentWords.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! documentWordDAO.checkDocumentWord(this.targetDocument.getDocumentID(), 
                  documentWord.getWord().getWordID())) {

            successful = documentWordDAO.addDocumentWord(this.targetDocument.getDocumentID(), 
                    documentWord.getWord().getWordID(),
                    documentWord.isAuthorKeyword(),
                    documentWord.isSourceKeyword(),
                    documentWord.isAddedKeyword(), true);
          }

          j ++;
        }

        j = 0;
        
        while ((j < documentAuthors.size()) && (successful)) {

          documentAuthor = documentAuthors.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! documentAuthorDAO.checkDocumentAuthor(this.targetDocument.getDocumentID(), 
                  documentAuthor.getAuthor().getAuthorID())) {

            successful = documentAuthorDAO.addDocumentAuthor(this.targetDocument.getDocumentID(), 
                    documentAuthor.getAuthor().getAuthorID(),
                    documentAuthor.getPosition(), true);
          }

          j ++;
        }

        j = 0;

        while ((j < affiliations.size()) && (successful)) {

          // If the target element is not associated with this element we perform the association.
          if (! documentAffiliationDAO.checkDocumentAffiliation(this.targetDocument.getDocumentID(), 
                  affiliations.get(j).getAffiliationID())) {

            successful = documentAffiliationDAO.addDocumentAffiliation(this.targetDocument.getDocumentID(), 
                    affiliations.get(j).getAffiliationID(), true);
          }

          j ++;
        }
        
        j = 0;

        while ((j < references.size()) && (successful)) {

          // If the target element is not associated with this element we perform the association.
          if (! documentReferenceDAO.checkDocumentReference(this.targetDocument.getDocumentID(), 
                  references.get(j).getReferenceID())) {

            successful = documentReferenceDAO.addDocumentReference(this.targetDocument.getDocumentID(), 
                    references.get(j).getReferenceID(), true);
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
    TreeSet<DocumentAuthor> tmpDocumentAuthors;
    TreeSet<DocumentWord> tmpDocumentWords;
    TreeSet<Affiliation> tmpAffiliations;
    TreeSet<Reference> tmpReferences;
    DocumentDAO documentDAO;
    DocumentAffiliationDAO documentAffiliationDAO;
    DocumentReferenceDAO documentReferenceDAO;
    Document document;
    DocumentWord documentWord;
    DocumentAuthor documentAuthor;
    DocumentAuthorDAO documentAuthorDAO;
    DocumentWordDAO documentWordDAO;
    Iterator<DocumentAuthor> itDocumentAuthor;
    Iterator<DocumentWord> itDocumentWord;
    Iterator<Affiliation> itAffiliation;
    Iterator<Reference> itReference;

    try {

      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      documentAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAffiliationDAO();
      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();
      documentReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentReferenceDAO();

      tmpDocumentAuthors = new TreeSet<DocumentAuthor>(documentDAO.getDocumentAuthors(this.targetDocument.getDocumentID()));
      tmpDocumentAuthors.removeAll(this.documentAuthorsOfTarget);

      itDocumentAuthor = tmpDocumentAuthors.iterator();

      while ((itDocumentAuthor.hasNext()) && (successful)) {

        successful = documentAuthorDAO.removeDocumentAuthor(this.targetDocument.getDocumentID(), 
                itDocumentAuthor.next().getAuthor().getAuthorID(), true);
      }

      tmpDocumentWords = new TreeSet<DocumentWord>(documentDAO.getDocumentWords(this.targetDocument.getDocumentID()));
      tmpDocumentWords.removeAll(this.documentWordsOfTarget);

      itDocumentWord = tmpDocumentWords.iterator();

      while ((itDocumentWord.hasNext()) && (successful)) {

        successful = documentWordDAO.removeDocumentWord(this.targetDocument.getDocumentID(), 
                itDocumentWord.next().getWord().getWordID(), true);
      }
      
      tmpAffiliations = new TreeSet<Affiliation>(documentDAO.getAffiliations(this.targetDocument.getDocumentID()));
      tmpAffiliations.removeAll(this.affiliationsOfTarget);

      itAffiliation = tmpAffiliations.iterator();

      while ((itAffiliation.hasNext()) && (successful)) {

        successful = documentAffiliationDAO.removeDocumentAffiliation(this.targetDocument.getDocumentID(), 
                itAffiliation.next().getAffiliationID(), true);
      }
      
      tmpReferences = new TreeSet<Reference>(documentDAO.getReferences(this.targetDocument.getDocumentID()));
      tmpReferences.removeAll(this.referencesOfTarget);

      itReference = tmpReferences.iterator();

      while ((itReference.hasNext()) && (successful)) {

        successful = documentReferenceDAO.removeDocumentReference(this.targetDocument.getDocumentID(),
                itReference.next().getReferenceID(), true);
      }

      i = 0;

      while ((i < this.documentsToMove.size()) && (successful)) {

        document = this.documentsToMove.get(i);

        successful = documentDAO.addDocument(document, true);

        j = 0;

        while ((j < this.documentWordsOfSources.get(i).size()) && (successful)) {

          documentWord = this.documentWordsOfSources.get(i).get(j);
          
          successful = documentWordDAO.addDocumentWord(document.getDocumentID(),
                  documentWord.getWord().getWordID(),
                  documentWord.isAuthorKeyword(),
                  documentWord.isSourceKeyword(),
                  documentWord.isAddedKeyword(), true);

          j++;
        }

        j = 0;

        while ((j < this.documentAuthorsOfSources.get(i).size()) && (successful)) {

          documentAuthor = this.documentAuthorsOfSources.get(i).get(j);
          
          successful = documentAuthorDAO.addDocumentAuthor(document.getDocumentID(),
                  documentAuthor.getAuthor().getAuthorID(),
                  documentAuthor.getPosition(), true);

          j++;
        }
        
        j = 0;

        while ((j < this.affiliationsOfSources.get(i).size()) && (successful)) {
          
          successful = documentAffiliationDAO.addDocumentAffiliation(document.getDocumentID(),
                  this.affiliationsOfSources.get(i).get(j).getAffiliationID(), true);

          j++;
        }
        
        j = 0;

        while ((j < this.referencesOfSources.get(i).size()) && (successful)) {
          
          successful = documentReferenceDAO.addDocumentReference(document.getDocumentID(),
                  this.referencesOfSources.get(i).get(j).getReferenceID(), true);

          j++;
        }
        
        if ((this.journalOfSources.get(i) != null) && (successful)) {

          successful = documentDAO.setJournal(document.getDocumentID(), 
                  this.journalOfSources.get(i).getJournalID(), true);
        }
        
        if ((this.publishDateOfSources.get(i) != null) && (successful)) {

          successful = documentDAO.setPublishDate(document.getDocumentID(), 
                  this.publishDateOfSources.get(i).getPublishDateID(), true);
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
    DocumentDAO documentDAO;
    DocumentAuthorDAO documentAuthorDAO;
    DocumentAffiliationDAO documentAffiliationDAO;
    DocumentWordDAO documentWordDAO;
    DocumentReferenceDAO documentReferenceDAO;
    Document document;
    DocumentWord documentWord;
    DocumentAuthor documentAuthor;
    ArrayList<DocumentAuthor> documentAuthors;
    ArrayList<DocumentWord> documentWords;
    ArrayList<Affiliation> affiliations;
    ArrayList<Reference> references;

    try {

      i = 0;
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      documentAuthorDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAuthorDAO();
      documentAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAffiliationDAO();
      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();
      documentReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentReferenceDAO();

      while ((i < this.documentsToMove.size()) && (successful)) {

        document = this.documentsToMove.get(i);

        // Retrieve the relations of the source items
        documentWords = this.documentWordsOfSources.get(i);
        documentAuthors = this.documentAuthorsOfSources.get(i);
        affiliations = this.affiliationsOfSources.get(i);
        references = this.referencesOfSources.get(i);

        // Do the join
        j = 0;

        successful = documentDAO.removeDocument(document.getDocumentID(), true);

        while ((j < documentWords.size()) && (successful)) {

          documentWord = documentWords.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! documentWordDAO.checkDocumentWord(this.targetDocument.getDocumentID(), documentWord.getWord().getWordID())) {

            successful = documentWordDAO.addDocumentWord(this.targetDocument.getDocumentID(), 
                    documentWord.getWord().getWordID(),
                    documentWord.isAuthorKeyword(),
                    documentWord.isSourceKeyword(),
                    documentWord.isAddedKeyword(), true);
          }

          j ++;
        }

        j = 0;
        
        while ((j < documentAuthors.size()) && (successful)) {

          documentAuthor = documentAuthors.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! documentAuthorDAO.checkDocumentAuthor(this.targetDocument.getDocumentID(), documentAuthor.getAuthor().getAuthorID())) {

            successful = documentAuthorDAO.addDocumentAuthor(this.targetDocument.getDocumentID(), 
                    documentAuthor.getAuthor().getAuthorID(),
                    documentAuthor.getPosition(), true);
          }

          j ++;
        }

        j = 0;

        while ((j < affiliations.size()) && (successful)) {

          // If the target element is not associated with this element we perform the association.
          if (! documentAffiliationDAO.checkDocumentAffiliation(this.targetDocument.getDocumentID(), 
                  affiliations.get(j).getAffiliationID())) {

            successful = documentAffiliationDAO.addDocumentAffiliation(this.targetDocument.getDocumentID(), 
                    affiliations.get(j).getAffiliationID(), true);
          }

          j ++;
        }
        
        j = 0;

        while ((j < references.size()) && (successful)) {

          // If the target element is not associated with this element we perform the association.
          if (! documentReferenceDAO.checkDocumentReference(this.targetDocument.getDocumentID(), 
                  references.get(j).getReferenceID())) {

            successful = documentReferenceDAO.addDocumentReference(this.targetDocument.getDocumentID(), 
                    references.get(j).getReferenceID(), true);
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
