/*
 * DeleteJournalEdit.java
 *
 * Created on 14-mar-2011, 17:38:03
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
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
public class DeleteJournalEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<Journal> journalsToDelete;
  private ArrayList<ArrayList<Document>> documents = new ArrayList<ArrayList<Document>>();
  private ArrayList<ArrayList<JournalSubjectCategoryPublishDate>> journalSubjectCategoryPublishDates = new ArrayList<ArrayList<JournalSubjectCategoryPublishDate>>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param journalsToDelete
   */
  public DeleteJournalEdit(ArrayList<Journal> journalsToDelete) {
    super();
    
    this.journalsToDelete = journalsToDelete;
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
    JournalDAO journalDAO;
    Journal journal;

    try {

      i = 0;
      journalDAO = CurrentProject.getInstance().getFactoryDAO().getJournalDAO();

      while ((i < this.journalsToDelete.size()) && (successful)) {

        journal = this.journalsToDelete.get(i);

        // Retrieve its relation
        this.documents.add(journalDAO.getDocuments(journal.getJournalID()));
        this.journalSubjectCategoryPublishDates.add(journalDAO.getJournalSubjectCategoryPublishDates(journal.getJournalID()));

        successful = journalDAO.removeJournal(journal.getJournalID(), true);

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
    JournalDAO journalDAO;
    Journal journal;
    JournalSubjectCategoryPublishDate journalSubjectCategoryPublishDate;
    Document document;
    JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO;
    DocumentDAO documentDAO;

    try {


      journalDAO = CurrentProject.getInstance().getFactoryDAO().getJournalDAO();
      journalSubjectCategoryPublishDateDAO = CurrentProject.getInstance().getFactoryDAO().getJournalSubjectCategoryPublishDateDAO();
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();

      i = 0;

      while ((i < this.journalsToDelete.size()) && (successful)) {

        journal = this.journalsToDelete.get(i);

        successful = journalDAO.addJournal(journal, true);

        j = 0;

        while ((j < this.journalSubjectCategoryPublishDates.get(i).size()) && (successful)) {

          journalSubjectCategoryPublishDate = this.journalSubjectCategoryPublishDates.get(i).get(j);

          successful = journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(journalSubjectCategoryPublishDate.getSubjectCategory().getSubjectCategoryID(),
                                                           journalSubjectCategoryPublishDate.getJournal().getJournalID(),
                                                           journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID(), true);

          j++;
        }

        j = 0;

        while ((j < this.documents.get(i).size()) && (successful)) {

          successful = documentDAO.setJournal(this.documents.get(i).get(j).getDocumentID(),
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

    int i;
    boolean successful = true;
    JournalDAO journalDAO;
    Journal journal;

    try {

      i = 0;
      journalDAO = CurrentProject.getInstance().getFactoryDAO().getJournalDAO();

      while ((i < this.journalsToDelete.size()) && (successful)) {

        journal = this.journalsToDelete.get(i);

        successful = journalDAO.removeJournal(journal.getJournalID(), true);

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
