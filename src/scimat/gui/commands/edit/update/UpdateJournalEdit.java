/*
 * UpdateJournalEdit.java
 *
 * Created on 14-mar-2011, 17:38:03
 */
package scimat.gui.commands.edit.update;

import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdateJournalEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer journalID;

  /**
   *
   */
  private String source;

  /**
   *
   */
  private String conferenceInformation;

  /**
   * The elements added
   */
  private Journal journalsOld;
  
  private Journal journalsUpdated;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public UpdateJournalEdit(Integer journalID, String source, String conferenceInformation) {
    super();

    this.journalID = journalID;
    this.source = source;
    this.conferenceInformation = conferenceInformation;
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

    boolean successful = false;

    try {

      this.journalsOld = CurrentProject.getInstance().getFactoryDAO().getJournalDAO().getJournal(journalID);
      
      if (this.journalsOld.getSource().equals(this.source)) {
      
        successful = CurrentProject.getInstance().getFactoryDAO().getJournalDAO().setConferenceInformation(journalID, conferenceInformation, true);
        
        this.journalsUpdated = CurrentProject.getInstance().getFactoryDAO().getJournalDAO().getJournal(journalID);
        
      } else if (this.source == null) {

        successful = false;
        this.errorMessage = "The source can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getJournalDAO().checkJournal(source)) {

        successful = false;
        this.errorMessage = "A journal yet exists with this source.";

      } else {

        successful = CurrentProject.getInstance().getFactoryDAO().getJournalDAO().updateJournal(journalID, source, conferenceInformation, true);
        
        this.journalsUpdated = CurrentProject.getInstance().getFactoryDAO().getJournalDAO().getJournal(journalID);

      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();

        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

        successful = true;

        UndoStack.addEdit(this);

      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();
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

    boolean flag;

    try {

      if (journalsOld.getSource().equals(journalsUpdated.getSource())) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getJournalDAO().setConferenceInformation(journalsOld.getJournalID(), journalsOld.getConferenceInformation(), true);
        
      } else {

        flag = CurrentProject.getInstance().getFactoryDAO().getJournalDAO().updateJournal(journalsOld.getJournalID(),
              journalsOld.getSource(), journalsOld.getConferenceInformation(), true);
      
      }

      if (flag) {

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

    boolean flag;

    try {

      if (journalsOld.getSource().equals(this.source)) {
        
        flag = CurrentProject.getInstance().getFactoryDAO().getJournalDAO().setConferenceInformation(journalID, conferenceInformation, true);
        
      } else {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getJournalDAO().updateJournal(journalID, source, conferenceInformation, true);
      }

      if (flag) {

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
