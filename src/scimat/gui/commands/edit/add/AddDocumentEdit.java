/*
 * AddDocumentEdit.java
 *
 * Created on 14-mar-2011, 17:38:12
 */
package scimat.gui.commands.edit.add;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddDocumentEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer documentID;

  /**
   *
   */
  private String type;

  /**
   *
   */
  private String title;

  /**
   *
   */
  private String docAbstract;

  /**
   *
   */
  private int citationsCount;

  /**
   *
   */
  private String doi;

  /**
   *
   */
  private String sourceIdentifier;

  /**
   *
   */
  private String volume;

  /**
   *
   */
  private String issue;

  /**
   *
   */
  private String beginPage;

  /**
   *
   */
  private String endPage;

  /**
   * The elements added
   */
  private ArrayList<Document> documentsAdded;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public AddDocumentEdit(String type, String title, String docAbstract,
          int citationsCount, String doi, String sourceIdentifier,
          String volume, String issue, String beginPage, String endPage) {
    super();
    
    this.type = type;
    this.title = title;
    this.docAbstract = docAbstract;
    this.citationsCount = citationsCount;
    this.doi = doi;
    this.sourceIdentifier = sourceIdentifier;
    this.volume = volume;
    this.issue = issue;
    this.beginPage = beginPage;
    this.endPage = endPage;
    this.documentsAdded = new ArrayList<Document>();
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

      this.documentID = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().addDocument(title,
              docAbstract, type, citationsCount, doi, sourceIdentifier,
              volume, issue, beginPage, endPage, true);

      if (this.documentID != null) {

        CurrentProject.getInstance().getKnowledgeBase().commit();

        this.documentsAdded.add(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID));

        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

        successful = true;

        UndoStack.addEdit(this);

      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();

        successful = false;
        this.errorMessage = "An error happened.";
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

      flag = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().removeDocument(documentID, true);

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

      flag = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().addDocument(documentID,
              title, docAbstract, type, citationsCount, doi,
              sourceIdentifier, volume, issue, beginPage, endPage, true);

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
