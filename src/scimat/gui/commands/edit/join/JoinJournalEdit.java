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
import scimat.model.knowledgebase.dao.DocumentDAO;
import scimat.model.knowledgebase.dao.JournalDAO;
import scimat.model.knowledgebase.dao.JournalSubjectCategoryPublishDateDAO;
import scimat.model.knowledgebase.entity.JournalSubjectCategoryPublishDate;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class JoinJournalEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<Journal> journalsToMove;
  private Journal targetJournal;

  private ArrayList<ArrayList<Document>> documentsOfSources = new ArrayList<ArrayList<Document>>();
  private ArrayList<ArrayList<JournalSubjectCategoryPublishDate>> journalSubjectCategoryPublishDatesOfSources = new ArrayList<ArrayList<JournalSubjectCategoryPublishDate>>();

  private TreeSet<Document> documentsOfTarget = new TreeSet<Document>();
  private TreeSet<JournalSubjectCategoryPublishDate> journalSubjectCategoryPublishDatesOfTarget = new TreeSet<JournalSubjectCategoryPublishDate>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param journalsToMove
   * @param targetJournal
   */
  public JoinJournalEdit(ArrayList<Journal> journalsToMove, Journal targetJournal) {

    this.journalsToMove = journalsToMove;
    this.targetJournal = targetJournal;
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
    JournalDAO journalDAO;
    DocumentDAO documentDAO;
    JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO;
    JournalSubjectCategoryPublishDate journalSubjectCategoryPublishDate;
    Journal journal;
    ArrayList<Document> documents;
    ArrayList<JournalSubjectCategoryPublishDate> journalSubjectCategoryPublishDates;

    try {

      i = 0;
      journalDAO = CurrentProject.getInstance().getFactoryDAO().getJournalDAO();
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      journalSubjectCategoryPublishDateDAO = CurrentProject.getInstance().getFactoryDAO().getJournalSubjectCategoryPublishDateDAO();

      // Retrieve the realations of the target item.
      this.documentsOfTarget = new TreeSet<Document>(journalDAO.getDocuments(this.targetJournal.getJournalID()));
      this.journalSubjectCategoryPublishDatesOfTarget = new TreeSet<JournalSubjectCategoryPublishDate>(journalDAO.getJournalSubjectCategoryPublishDates(this.targetJournal.getJournalID()));

      while ((i < this.journalsToMove.size()) && (successful)) {

        journal = this.journalsToMove.get(i);

        // Retrieve the relations of the source items
        journalSubjectCategoryPublishDates = journalDAO.getJournalSubjectCategoryPublishDates(journal.getJournalID());
        this.journalSubjectCategoryPublishDatesOfSources.add(journalSubjectCategoryPublishDates);

        documents = journalDAO.getDocuments(journal.getJournalID());
        this.documentsOfSources.add(documents);

        // Do the join
        j = 0;

        successful = journalDAO.removeJournal(journal.getJournalID(), true);

        while ((j < journalSubjectCategoryPublishDates.size()) && (successful)) {

          journalSubjectCategoryPublishDate = journalSubjectCategoryPublishDates.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! journalSubjectCategoryPublishDateDAO.checkJournalSubjectCategoryPublishDate(this.targetJournal.getJournalID(), 
                  journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(), 
                  journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID())) {

            successful = journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(this.targetJournal.getJournalID(), 
                    journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(), 
                    journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID(), true);
          }

          j ++;
        }

        j = 0;
        
        while ((j < documents.size()) && (successful)) {

          successful = documentDAO.setJournal(documents.get(j).getDocumentID(), 
                  this.targetJournal.getJournalID(), true);

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
    TreeSet<Document> tmpDocuments;
    TreeSet<JournalSubjectCategoryPublishDate> tmpJournalSubjectCategoryPublishDates;
    JournalDAO journalDAO;
    Journal journal;
    JournalSubjectCategoryPublishDate journalSubjectCategoryPublishDate;
    Document document;
    DocumentDAO documentDAO;
    JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO;
    Iterator<Document> itDocument;
    Iterator<JournalSubjectCategoryPublishDate> itJournalSubjectCategoryPublishDate;

    try {

      journalDAO = CurrentProject.getInstance().getFactoryDAO().getJournalDAO();
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      journalSubjectCategoryPublishDateDAO = CurrentProject.getInstance().getFactoryDAO().getJournalSubjectCategoryPublishDateDAO();

      tmpDocuments = new TreeSet<Document>(journalDAO.getDocuments(this.targetJournal.getJournalID()));
      tmpDocuments.removeAll(this.documentsOfTarget);

      itDocument = tmpDocuments.iterator();

      while ((itDocument.hasNext()) && (successful)) {

        successful = documentDAO.setJournal(itDocument.next().getDocumentID(), null, true);
      }

      tmpJournalSubjectCategoryPublishDates = new TreeSet<JournalSubjectCategoryPublishDate>(journalDAO.getJournalSubjectCategoryPublishDates(this.targetJournal.getJournalID()));
      tmpJournalSubjectCategoryPublishDates.removeAll(this.journalSubjectCategoryPublishDatesOfTarget);

      itJournalSubjectCategoryPublishDate = tmpJournalSubjectCategoryPublishDates.iterator();

      while ((itJournalSubjectCategoryPublishDate.hasNext()) && (successful)) {

        journalSubjectCategoryPublishDate = itJournalSubjectCategoryPublishDate.next();
        
        successful = journalSubjectCategoryPublishDateDAO.removeSubjectCategoryFromJournal(journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(), 
                this.targetJournal.getJournalID(),
                journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID(), true);
      }

      i = 0;

      while ((i < this.journalsToMove.size()) && (successful)) {

        journal = this.journalsToMove.get(i);

        successful = journalDAO.addJournal(journal, true);

        j = 0;

        while ((j < this.journalSubjectCategoryPublishDatesOfSources.get(i).size()) && (successful)) {

          journalSubjectCategoryPublishDate = this.journalSubjectCategoryPublishDatesOfSources.get(i).get(j);
          
          successful = journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(), 
                  journal.getJournalID(),
                  journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID(), true);

          j++;
        }

        j = 0;

        while ((j < this.documentsOfSources.get(i).size()) && (successful)) {

          document = this.documentsOfSources.get(i).get(j);
          
          successful = documentDAO.setJournal(document.getDocumentID(), 
                  journal.getJournalID(), true);

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
    JournalDAO journalDAO;
    DocumentDAO documentDAO;
    JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO;
    JournalSubjectCategoryPublishDate journalSubjectCategoryPublishDate;
    Journal journal;
    ArrayList<Document> documents;
    ArrayList<JournalSubjectCategoryPublishDate> journalSubjectCategoryPublishDates;

    try {

      i = 0;
      journalDAO = CurrentProject.getInstance().getFactoryDAO().getJournalDAO();
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      journalSubjectCategoryPublishDateDAO = CurrentProject.getInstance().getFactoryDAO().getJournalSubjectCategoryPublishDateDAO();

      while ((i < this.journalsToMove.size()) && (successful)) {

        journal = this.journalsToMove.get(i);

        // Retrieve the relations of the source items
        journalSubjectCategoryPublishDates = this.journalSubjectCategoryPublishDatesOfSources.get(i);
        documents = this.documentsOfSources.get(i);

        // Do the join
        j = 0;

        successful = journalDAO.removeJournal(journal.getJournalID(), true);

        while ((j < journalSubjectCategoryPublishDates.size()) && (successful)) {

          journalSubjectCategoryPublishDate = journalSubjectCategoryPublishDates.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! journalSubjectCategoryPublishDateDAO.checkJournalSubjectCategoryPublishDate(this.targetJournal.getJournalID(), 
                  journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(), 
                  journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID())) {

            successful = journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(this.targetJournal.getJournalID(), 
                    journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(), 
                    journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID(), true);
          }

          j ++;
        }

        j = 0;
        
        while ((j < documents.size()) && (successful)) {

          successful = documentDAO.setJournal(documents.get(j).getDocumentID(), 
                  this.targetJournal.getJournalID(), true);

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
