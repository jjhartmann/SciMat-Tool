
/*
 * JoinPublishDateEdit.java
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
import scimat.model.knowledgebase.dao.JournalSubjectCategoryPublishDateDAO;
import scimat.model.knowledgebase.dao.PublishDateDAO;
import scimat.model.knowledgebase.dao.PublishDatePeriodDAO;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.JournalSubjectCategoryPublishDate;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class JoinPublishDateEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<PublishDate> publishDatesToMove;
  private PublishDate targetPublishDate;

  private ArrayList<ArrayList<Document>> documentsOfSources = new ArrayList<ArrayList<Document>>();
  private ArrayList<ArrayList<Period>> periodsOfSources = new ArrayList<ArrayList<Period>>();
  private ArrayList<ArrayList<JournalSubjectCategoryPublishDate>> journalSubjectCategoryPublishDatesOfSources = new ArrayList<ArrayList<JournalSubjectCategoryPublishDate>>();

  private TreeSet<Document> documentsOfTarget = new TreeSet<Document>();
  private TreeSet<Period> periodsOfTarget = new TreeSet<Period>();
  private TreeSet<JournalSubjectCategoryPublishDate> journalSubjectCategoryPublishDatesOfTarget = new TreeSet<JournalSubjectCategoryPublishDate>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param publishDatesToMove
   * @param targetPublishDate
   */
  public JoinPublishDateEdit(ArrayList<PublishDate> publishDatesToMove, PublishDate targetPublishDate) {

    this.publishDatesToMove = publishDatesToMove;
    this.targetPublishDate = targetPublishDate;
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
    PublishDateDAO publishDateDAO;
    PublishDate publishDate;
    DocumentDAO documentDAO;
    PublishDatePeriodDAO publishDatePeriodDAO;
    Period period;
    ArrayList<Period> periods;
    JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO;
    JournalSubjectCategoryPublishDate journalSubjectCategoryPublishDate;
    ArrayList<Document> documents;
    ArrayList<JournalSubjectCategoryPublishDate> journalSubjectCategoryPublishDates;

    try {

      i = 0;
      publishDateDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO();
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      journalSubjectCategoryPublishDateDAO = CurrentProject.getInstance().getFactoryDAO().getJournalSubjectCategoryPublishDateDAO();
      publishDatePeriodDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDatePeriodDAO();

      // Retrieve the realations of the target item.
      this.documentsOfTarget = new TreeSet<Document>(publishDateDAO.getDocuments(this.targetPublishDate.getPublishDateID()));
      this.journalSubjectCategoryPublishDatesOfTarget = new TreeSet<JournalSubjectCategoryPublishDate>(publishDateDAO.getSubjectCategories(this.targetPublishDate.getPublishDateID()));
      this.periodsOfTarget = new TreeSet<Period>(publishDateDAO.getPeriods(this.targetPublishDate.getPublishDateID()));

      while ((i < this.publishDatesToMove.size()) && (successful)) {

        publishDate = this.publishDatesToMove.get(i);

        // Retrieve the relations of the source items
        journalSubjectCategoryPublishDates = publishDateDAO.getSubjectCategories(publishDate.getPublishDateID());
        this.journalSubjectCategoryPublishDatesOfSources.add(journalSubjectCategoryPublishDates);

        documents = publishDateDAO.getDocuments(publishDate.getPublishDateID());
        this.documentsOfSources.add(documents);
        
        periods = publishDateDAO.getPeriods(publishDate.getPublishDateID());
        this.documentsOfSources.add(documents);

        // Do the join
        j = 0;

        successful = publishDateDAO.removePublishDate(publishDate.getPublishDateID(), true);

        while ((j < journalSubjectCategoryPublishDates.size()) && (successful)) {

          journalSubjectCategoryPublishDate = journalSubjectCategoryPublishDates.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! journalSubjectCategoryPublishDateDAO.checkJournalSubjectCategoryPublishDate(journalSubjectCategoryPublishDate.getJournal().getJournalID(), 
                  journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(),
                    this.targetPublishDate.getPublishDateID())) {

            successful = journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(journalSubjectCategoryPublishDate.getJournal().getJournalID(),
                    journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(), 
                    this.targetPublishDate.getPublishDateID(), true);
          }

          j ++;
        }

        j = 0;
        
        while ((j < documents.size()) && (successful)) {

          successful = documentDAO.setPublishDate(documents.get(j).getDocumentID(), 
                  this.targetPublishDate.getPublishDateID(), true);

          j ++;
        }
        
        j = 0;
        
        while ((j < periods.size()) && (successful)) {

          period = periods.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! publishDatePeriodDAO.checkPublishDatePeriod(this.targetPublishDate.getPublishDateID(), 
                  period.getPeriodID())) {

            successful = publishDatePeriodDAO.addPublishDatePeriod(period.getPeriodID(), 
                    this.targetPublishDate.getPublishDateID(), true);
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
    TreeSet<Document> tmpDocuments;
    TreeSet<JournalSubjectCategoryPublishDate> tmpJournalSubjectCategoryPublishDates;
    TreeSet<Period> tmpPeriods;
    PublishDateDAO publishDateDAO;
    PublishDatePeriodDAO publishDatePeriodDAO;
    PublishDate publishDate;
    JournalSubjectCategoryPublishDate journalSubjectCategoryPublishDate;
    Document document;
    DocumentDAO documentDAO;
    JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO;
    Iterator<Document> itDocument;
    Iterator<Period> itPeriod;
    Iterator<JournalSubjectCategoryPublishDate> itJournalSubjectCategoryPublishDate;

    try {

      publishDateDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO();
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      journalSubjectCategoryPublishDateDAO = CurrentProject.getInstance().getFactoryDAO().getJournalSubjectCategoryPublishDateDAO();
      publishDatePeriodDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDatePeriodDAO();

      tmpDocuments = new TreeSet<Document>(publishDateDAO.getDocuments(this.targetPublishDate.getPublishDateID()));
      tmpDocuments.removeAll(this.documentsOfTarget);

      itDocument = tmpDocuments.iterator();

      while ((itDocument.hasNext()) && (successful)) {

        successful = documentDAO.setPublishDate(itDocument.next().getDocumentID(), null, true);
      }

      tmpJournalSubjectCategoryPublishDates = new TreeSet<JournalSubjectCategoryPublishDate>(publishDateDAO.getSubjectCategories(this.targetPublishDate.getPublishDateID()));
      tmpJournalSubjectCategoryPublishDates.removeAll(this.journalSubjectCategoryPublishDatesOfTarget);

      itJournalSubjectCategoryPublishDate = tmpJournalSubjectCategoryPublishDates.iterator();

      while ((itJournalSubjectCategoryPublishDate.hasNext()) && (successful)) {

        journalSubjectCategoryPublishDate = itJournalSubjectCategoryPublishDate.next();
        
        successful = journalSubjectCategoryPublishDateDAO.removeSubjectCategoryFromJournal(journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(), 
                journalSubjectCategoryPublishDate.getJournal().getJournalID(),
                this.targetPublishDate.getPublishDateID(), true);
      }
      
      tmpPeriods = new TreeSet<Period>(publishDateDAO.getPeriods(this.targetPublishDate.getPublishDateID()));
      tmpPeriods.removeAll(this.periodsOfTarget);

      itPeriod = tmpPeriods.iterator();

      while ((itPeriod.hasNext()) && (successful)) {
        
        successful = publishDatePeriodDAO.removePublishDatePeriod(itPeriod.next().getPeriodID(),
                this.targetPublishDate.getPublishDateID(), true);
      }

      i = 0;

      while ((i < this.publishDatesToMove.size()) && (successful)) {

        publishDate = this.publishDatesToMove.get(i);

        successful = publishDateDAO.addPublishDate(publishDate, true);

        j = 0;

        while ((j < this.journalSubjectCategoryPublishDatesOfSources.get(i).size()) && (successful)) {

          journalSubjectCategoryPublishDate = this.journalSubjectCategoryPublishDatesOfSources.get(i).get(j);
          
          successful = journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(), 
                  journalSubjectCategoryPublishDate.getJournal().getJournalID(),
                  publishDate.getPublishDateID(), true);

          j++;
        }
        
        j = 0;

        while ((j < this.periodsOfSources.get(i).size()) && (successful)) {
          
          successful = publishDatePeriodDAO.addPublishDatePeriod(this.periodsOfSources.get(i).get(j).getPeriodID(), 
                  publishDate.getPublishDateID(), true);

          j++;
        }

        j = 0;

        while ((j < this.documentsOfSources.get(i).size()) && (successful)) {

          document = this.documentsOfSources.get(i).get(j);
          
          successful = documentDAO.setPublishDate(document.getDocumentID(), 
                  publishDate.getPublishDateID(), true);

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
    PublishDateDAO publishDateDAO;
    PublishDate publishDate;
    DocumentDAO documentDAO;
    PublishDatePeriodDAO publishDatePeriodDAO;
    Period period;
    ArrayList<Period> periods;
    JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO;
    JournalSubjectCategoryPublishDate journalSubjectCategoryPublishDate;
    ArrayList<Document> documents;
    ArrayList<JournalSubjectCategoryPublishDate> journalSubjectCategoryPublishDates;

    try {

      i = 0;
      publishDateDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO();
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();
      journalSubjectCategoryPublishDateDAO = CurrentProject.getInstance().getFactoryDAO().getJournalSubjectCategoryPublishDateDAO();
      publishDatePeriodDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDatePeriodDAO();
      
      while ((i < this.publishDatesToMove.size()) && (successful)) {

        publishDate = this.publishDatesToMove.get(i);

        // Retrieve the relations of the source items
        journalSubjectCategoryPublishDates = this.journalSubjectCategoryPublishDatesOfSources.get(i);
        documents = this.documentsOfSources.get(i);
        periods = this.periodsOfSources.get(i);

        // Do the join
        j = 0;

        successful = publishDateDAO.removePublishDate(publishDate.getPublishDateID(), true);

        while ((j < journalSubjectCategoryPublishDates.size()) && (successful)) {

          journalSubjectCategoryPublishDate = journalSubjectCategoryPublishDates.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! journalSubjectCategoryPublishDateDAO.checkJournalSubjectCategoryPublishDate(journalSubjectCategoryPublishDate.getJournal().getJournalID(),
                  journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(), 
                  this.targetPublishDate.getPublishDateID())) {

            successful = journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(journalSubjectCategoryPublishDate.getJournal().getJournalID(),
                  journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(), 
                  this.targetPublishDate.getPublishDateID(), true);
          }

          j ++;
        }

        j = 0;
        
        while ((j < documents.size()) && (successful)) {

          successful = documentDAO.setPublishDate(documents.get(j).getDocumentID(), this.targetPublishDate.getPublishDateID(), true);

          j ++;
        }
        
        j = 0;
        
        while ((j < periods.size()) && (successful)) {

          period = periods.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! publishDatePeriodDAO.checkPublishDatePeriod(this.targetPublishDate.getPublishDateID(), 
                  period.getPeriodID())) {

            successful = publishDatePeriodDAO.addPublishDatePeriod(period.getPeriodID(), 
                    this.targetPublishDate.getPublishDateID(), true);
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
