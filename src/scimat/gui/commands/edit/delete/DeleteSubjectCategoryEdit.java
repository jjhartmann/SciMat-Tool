/*
 * DeleteSubjectCategoryEdit.java
 *
 * Created on 14-mar-2011, 17:38:49
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.JournalSubjectCategoryPublishDateDAO;
import scimat.model.knowledgebase.dao.SubjectCategoryDAO;
import scimat.model.knowledgebase.entity.JournalSubjectCategoryPublishDate;
import scimat.model.knowledgebase.entity.SubjectCategory;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeleteSubjectCategoryEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<SubjectCategory> subjectCategoriesToDelete;
  private ArrayList<ArrayList<JournalSubjectCategoryPublishDate>> journalSubjectCategoryPublishDates = new ArrayList<ArrayList<JournalSubjectCategoryPublishDate>>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public DeleteSubjectCategoryEdit(ArrayList<SubjectCategory> subjectCategoriesToDelete) {
    super();
    
    this.subjectCategoriesToDelete = subjectCategoriesToDelete;
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
    SubjectCategoryDAO subjectCategoryDAO;
    SubjectCategory subjectCategory;

    try {

      i = 0;
      subjectCategoryDAO = CurrentProject.getInstance().getFactoryDAO().getSubjectCategoryDAO();

      while ((i < this.subjectCategoriesToDelete.size()) && (successful)) {

        subjectCategory = this.subjectCategoriesToDelete.get(i);

        // Retrieve its relation
        this.journalSubjectCategoryPublishDates.add(subjectCategoryDAO.getJournals(subjectCategory.getSubjectCategoryID()));

        successful = subjectCategoryDAO.removeSubjectCategory(subjectCategory.getSubjectCategoryID(), true);

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
    SubjectCategoryDAO subjectCategoryDAO;
    JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO;
    SubjectCategory subjectCategory;
    JournalSubjectCategoryPublishDate jscp;

    try {

      subjectCategoryDAO = CurrentProject.getInstance().getFactoryDAO().getSubjectCategoryDAO();
      journalSubjectCategoryPublishDateDAO = CurrentProject.getInstance().getFactoryDAO().getJournalSubjectCategoryPublishDateDAO();

      i = 0;

      while ((i < this.subjectCategoriesToDelete.size()) && (successful)) {

        subjectCategory = this.subjectCategoriesToDelete.get(i);

        successful = subjectCategoryDAO.addSubjectCategory(subjectCategory, true);

        j = 0;

        while ((j < this.journalSubjectCategoryPublishDates.get(i).size()) && (successful)) {

          jscp = this.journalSubjectCategoryPublishDates.get(i).get(j);

          successful = journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(subjectCategory.getSubjectCategoryID(),
                                                                                        jscp.getJournal().getJournalID(),
                                                                                        jscp.getPublishDate().getPublishDateID(), true);

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
    SubjectCategoryDAO subjectCategoryDAO;
    SubjectCategory subjectCategory;

    try {

      i = 0;
      subjectCategoryDAO = CurrentProject.getInstance().getFactoryDAO().getSubjectCategoryDAO();

      while ((i < this.subjectCategoriesToDelete.size()) && (successful)) {

        subjectCategory = this.subjectCategoriesToDelete.get(i);

        successful = subjectCategoryDAO.removeSubjectCategory(subjectCategory.getSubjectCategoryID(), true);

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
