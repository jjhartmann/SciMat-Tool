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
public class JoinSubjectCategoryEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<SubjectCategory> subjectCategoriesToMove;
  private SubjectCategory targetSubjectCategory;
  
  private ArrayList<ArrayList<JournalSubjectCategoryPublishDate>> journalSubjectCategoryPublishDatesOfSources = new ArrayList<ArrayList<JournalSubjectCategoryPublishDate>>();
  
  private TreeSet<JournalSubjectCategoryPublishDate> journalSubjectCategoryPublishDatesOfTarget = new TreeSet<JournalSubjectCategoryPublishDate>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param subjectCategoriesToMove
   * @param targetSubjectCategory
   */
  public JoinSubjectCategoryEdit(ArrayList<SubjectCategory> subjectCategoriesToMove, SubjectCategory targetSubjectCategory) {

    this.subjectCategoriesToMove = subjectCategoriesToMove;
    this.targetSubjectCategory = targetSubjectCategory;
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
    SubjectCategory subjectCategory;
    SubjectCategoryDAO subjectCategoryDAO;
    JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO;
    JournalSubjectCategoryPublishDate journalSubjectCategoryPublishDate;
    ArrayList<JournalSubjectCategoryPublishDate> journalSubjectCategoryPublishDates;

    try {

      i = 0;
      subjectCategoryDAO = CurrentProject.getInstance().getFactoryDAO().getSubjectCategoryDAO();
      journalSubjectCategoryPublishDateDAO = CurrentProject.getInstance().getFactoryDAO().getJournalSubjectCategoryPublishDateDAO();

      // Retrieve the realations of the target item.
      this.journalSubjectCategoryPublishDatesOfTarget = new TreeSet<JournalSubjectCategoryPublishDate>(subjectCategoryDAO.getJournals(this.targetSubjectCategory.getSubjectCategoryID()));

      while ((i < this.subjectCategoriesToMove.size()) && (successful)) {

        subjectCategory = this.subjectCategoriesToMove.get(i);

        // Retrieve the relations of the source items
        journalSubjectCategoryPublishDates = subjectCategoryDAO.getJournals(subjectCategory.getSubjectCategoryID());
        this.journalSubjectCategoryPublishDatesOfSources.add(journalSubjectCategoryPublishDates);

        // Do the join
        j = 0;

        successful = subjectCategoryDAO.removeSubjectCategory(subjectCategory.getSubjectCategoryID(), true);

        while ((j < journalSubjectCategoryPublishDates.size()) && (successful)) {

          journalSubjectCategoryPublishDate = journalSubjectCategoryPublishDates.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! journalSubjectCategoryPublishDateDAO.checkJournalSubjectCategoryPublishDate(journalSubjectCategoryPublishDate.getJournal().getJournalID(), 
                  this.targetSubjectCategory.getSubjectCategoryID(), 
                  journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID())) {

            successful = journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(journalSubjectCategoryPublishDate.getJournal().getJournalID(), 
                    this.targetSubjectCategory.getSubjectCategoryID(), 
                    journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID(), true);
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
    SubjectCategory subjectCategory;
    SubjectCategoryDAO subjectCategoryDAO;
    TreeSet<JournalSubjectCategoryPublishDate> tmpJournalSubjectCategoryPublishDates;
    JournalSubjectCategoryPublishDate journalSubjectCategoryPublishDate;
    JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO;
    Iterator<JournalSubjectCategoryPublishDate> itJournalSubjectCategoryPublishDate;

    try {

      subjectCategoryDAO = CurrentProject.getInstance().getFactoryDAO().getSubjectCategoryDAO();
      journalSubjectCategoryPublishDateDAO = CurrentProject.getInstance().getFactoryDAO().getJournalSubjectCategoryPublishDateDAO();

      tmpJournalSubjectCategoryPublishDates = new TreeSet<JournalSubjectCategoryPublishDate>(subjectCategoryDAO.getJournals(this.targetSubjectCategory.getSubjectCategoryID()));
      tmpJournalSubjectCategoryPublishDates.removeAll(this.journalSubjectCategoryPublishDatesOfTarget);

      itJournalSubjectCategoryPublishDate = tmpJournalSubjectCategoryPublishDates.iterator();

      while ((itJournalSubjectCategoryPublishDate.hasNext()) && (successful)) {

        journalSubjectCategoryPublishDate = itJournalSubjectCategoryPublishDate.next();
        
        successful = journalSubjectCategoryPublishDateDAO.removeSubjectCategoryFromJournal(this.targetSubjectCategory.getSubjectCategoryID(), 
                journalSubjectCategoryPublishDate.getJournal().getJournalID(),
                journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID(), true);
      }

      i = 0;

      while ((i < this.subjectCategoriesToMove.size()) && (successful)) {

        subjectCategory = this.subjectCategoriesToMove.get(i);

        successful = subjectCategoryDAO.addSubjectCategory(subjectCategory, true);

        j = 0;

        while ((j < this.journalSubjectCategoryPublishDatesOfSources.get(i).size()) && (successful)) {

          journalSubjectCategoryPublishDate = this.journalSubjectCategoryPublishDatesOfSources.get(i).get(j);
          
          successful = journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(subjectCategory.getSubjectCategoryID(),
                  journalSubjectCategoryPublishDate.getJournal().getJournalID(), 
                  journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID(), true);

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
    SubjectCategory subjectCategory;
    SubjectCategoryDAO subjectCategoryDAO;
    JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO;
    JournalSubjectCategoryPublishDate journalSubjectCategoryPublishDate;
    ArrayList<JournalSubjectCategoryPublishDate> journalSubjectCategoryPublishDates;

    try {

      i = 0;
      subjectCategoryDAO = CurrentProject.getInstance().getFactoryDAO().getSubjectCategoryDAO();
      journalSubjectCategoryPublishDateDAO = CurrentProject.getInstance().getFactoryDAO().getJournalSubjectCategoryPublishDateDAO();
      
      while ((i < this.subjectCategoriesToMove.size()) && (successful)) {

        subjectCategory = this.subjectCategoriesToMove.get(i);

        // Retrieve the relations of the source items
        journalSubjectCategoryPublishDates = this.journalSubjectCategoryPublishDatesOfSources.get(i);

        // Do the join
        j = 0;

        successful = subjectCategoryDAO.removeSubjectCategory(subjectCategory.getSubjectCategoryID(), true);

        while ((j < journalSubjectCategoryPublishDates.size()) && (successful)) {

          journalSubjectCategoryPublishDate = journalSubjectCategoryPublishDates.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! journalSubjectCategoryPublishDateDAO.checkJournalSubjectCategoryPublishDate(journalSubjectCategoryPublishDate.getJournal().getJournalID(), 
                  this.targetSubjectCategory.getSubjectCategoryID(), 
                  journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID())) {

            successful = journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(journalSubjectCategoryPublishDate.getJournal().getJournalID(), 
                    this.targetSubjectCategory.getSubjectCategoryID(), 
                    journalSubjectCategoryPublishDate.getPublishDate().getPublishDateID(), true);
          }

          j ++;
        }

        i ++;
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();

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
