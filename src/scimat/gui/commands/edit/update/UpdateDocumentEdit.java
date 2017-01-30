/*
 * UpdateDocumentEdit.java
 *
 * Created on 14-mar-2011, 17:38:12
 */
package scimat.gui.commands.edit.update;

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
public class UpdateDocumentEdit extends KnowledgeBaseEdit {

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
   * The old elements
   */
  private ArrayList<Document> documentsOld;
  
  private ArrayList<Document> documentsUpdated;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public UpdateDocumentEdit(Integer documentID, 
          String title, String docAbstract, String type, int citationsCount, String doi,
          String sourceIdentifier, String volume, String issue, String beginPage,
          String endPage) {
    super();

    this.documentID = documentID;
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
    this.documentsOld = new ArrayList<Document>();
    this.documentsUpdated = new ArrayList<Document>();
            
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

      this.documentsOld.add(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID));

      successful = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().updateDocument(documentID,
              title, docAbstract, type, citationsCount, doi,
              sourceIdentifier, volume, issue, beginPage, endPage, true);
      
      this.documentsUpdated.add(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID));

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();

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
    Document document;

    try {

      document = this.documentsOld.get(0);

      flag = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().updateDocument(document.getDocumentID(),
              document.getTitle(), document.getDocAbstract(),
              document.getType(), document.getCitationsCount(),
              document.getDoi(), document.getSourceIdentifier(),
              document.getVolume(), document.getIssue(),
              document.getBeginPage(), document.getEndPage(), true);

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

      flag = CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().updateDocument(documentID,
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
