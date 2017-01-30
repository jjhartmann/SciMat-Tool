/*
 * DeleteAffiliationEdit.java
 *
 * Created on 14-mar-2011, 17:39:41
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.AffiliationDAO;
import scimat.model.knowledgebase.dao.AuthorAffiliationDAO;
import scimat.model.knowledgebase.dao.DocumentAffiliationDAO;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeleteAffiliationEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<Affiliation> affiliationsToDelete;
  private ArrayList<ArrayList<Author>> authors = new ArrayList<ArrayList<Author>>();
  private ArrayList<ArrayList<Document>> documents = new ArrayList<ArrayList<Document>>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param fullAffilliation
   */
  public DeleteAffiliationEdit(ArrayList<Affiliation> affiliationsToDelete) {
    super();
    
    this.affiliationsToDelete = affiliationsToDelete;
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
    AffiliationDAO affiliationDAO;
    Affiliation affiliation;

    try {

      i = 0;
      affiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAffiliationDAO();

      while ((i < this.affiliationsToDelete.size()) && (successful)) {

        affiliation = this.affiliationsToDelete.get(i);

        // Retrieve its relation
        this.authors.add(affiliationDAO.getAuthors(affiliation.getAffiliationID()));
        this.documents.add(affiliationDAO.getDocuments(affiliation.getAffiliationID()));

        successful = affiliationDAO.removeAffiliation(affiliation.getAffiliationID(), true);

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
    AffiliationDAO affiliationDAO;
    Affiliation affiliation;
    AuthorAffiliationDAO authorAffiliationDAO;
    DocumentAffiliationDAO documentAffiliationDAO;

    try {

      
      affiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAffiliationDAO();
      authorAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorAffiliationDAO();
      documentAffiliationDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentAffiliationDAO();

      i = 0;

      while ((i < this.affiliationsToDelete.size()) && (successful)) {

        affiliation = this.affiliationsToDelete.get(i);

        successful = affiliationDAO.addAffiliation(affiliation, true);

        j = 0;

        while ((j < this.documents.get(i).size()) && (successful)) {

          successful = documentAffiliationDAO.addDocumentAffiliation(this.documents.get(i).get(j).getDocumentID(),
                                                                    affiliation.getAffiliationID(), true);

          j++;
        }

        j = 0;

        while ((j < this.authors.get(i).size()) && (successful)) {

          successful = authorAffiliationDAO.addAuthorAffiliation(this.authors.get(i).get(j).getAuthorID(),
                                                                 affiliation.getAffiliationID(), true);

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
    AffiliationDAO affiliationDAO;
    Affiliation affiliation;

    try {

      i = 0;
      affiliationDAO = CurrentProject.getInstance().getFactoryDAO().getAffiliationDAO();

      while ((i < this.affiliationsToDelete.size()) && (successful)) {

        affiliation = this.affiliationsToDelete.get(i);

        successful = affiliationDAO.removeAffiliation(affiliation.getAffiliationID(), true);

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
