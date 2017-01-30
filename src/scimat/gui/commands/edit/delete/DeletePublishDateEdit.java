/*
 * DeletePublishDateEdit.java
 *
 * Created on 14-mar-2011, 17:38:33
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.DocumentDAO;
import scimat.model.knowledgebase.dao.PublishDateDAO;
import scimat.model.knowledgebase.dao.PublishDatePeriodDAO;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeletePublishDateEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<PublishDate> publishDatesToDelete;
  private ArrayList<ArrayList<Period>> periods = new ArrayList<ArrayList<Period>>();
  private ArrayList<ArrayList<Document>> documents = new ArrayList<ArrayList<Document>>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param publishDatesToDelete
   */
  public DeletePublishDateEdit(ArrayList<PublishDate> publishDatesToDelete) {
    super();
    
    this.publishDatesToDelete = publishDatesToDelete;
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
    PublishDateDAO publishDateDAO;
    PublishDate publishDate;

    try {

      i = 0;
      publishDateDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO();

      while ((i < this.publishDatesToDelete.size()) && (successful)) {

        publishDate = this.publishDatesToDelete.get(i);

        // Retrieve its relation
        this.documents.add(publishDateDAO.getDocuments(publishDate.getPublishDateID()));
        this.periods.add(publishDateDAO.getPeriods(publishDate.getPublishDateID()));

        successful = publishDateDAO.removePublishDate(publishDate.getPublishDateID(), true);

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
    PublishDateDAO publishDateDAO;
    PublishDatePeriodDAO publishDatePeriodDAO;
    DocumentDAO documentDAO;
    PublishDate publishDate;
    Document document;

    try {

      publishDateDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO();
      publishDatePeriodDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDatePeriodDAO();
      documentDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO();

      i = 0;

      while ((i < this.publishDatesToDelete.size()) && (successful)) {

        publishDate = this.publishDatesToDelete.get(i);

        successful = publishDateDAO.addPublishDate(publishDate, true);

        j = 0;

        while ((j < this.documents.get(i).size()) && (successful)) {

          document = this.documents.get(i).get(j);

          successful = documentDAO.setPublishDate(document.getDocumentID(),
                                                  publishDate.getPublishDateID(), true);

          j++;
        }

        j = 0;

        while ((j < this.periods.get(i).size()) && (successful)) {

          successful = publishDatePeriodDAO.addPublishDatePeriod(this.periods.get(i).get(j).getPeriodID(),
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

    int i;
    boolean successful = true;
    PublishDateDAO publishDateDAO;
    PublishDate publishDate;

    try {

      i = 0;
      publishDateDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO();

      while ((i < this.publishDatesToDelete.size()) && (successful)) {

        publishDate = this.publishDatesToDelete.get(i);

        successful = publishDateDAO.removePublishDate(publishDate.getPublishDateID(), true);

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
