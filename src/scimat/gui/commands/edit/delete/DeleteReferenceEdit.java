/*
 * DeleteReferenceEdit.java
 *
 * Created on 14-mar-2011, 17:38:58
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.AuthorReferenceReferenceDAO;
import scimat.model.knowledgebase.dao.DocumentReferenceDAO;
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.entity.AuthorReferenceReference;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DeleteReferenceEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<Reference> referencesToDelete;
  private ArrayList<ArrayList<Document>> documents = new ArrayList<ArrayList<Document>>();
  private ArrayList<ArrayList<AuthorReferenceReference>> authorReferenceReferences = new ArrayList<ArrayList<AuthorReferenceReference>>();
  private ArrayList<ReferenceGroup> referenceGroups = new ArrayList<ReferenceGroup>();
  private ArrayList<ReferenceSource> referenceSources = new ArrayList<ReferenceSource>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public DeleteReferenceEdit(ArrayList<Reference> referencesToDelete) {
    super();
    
    this.referencesToDelete = referencesToDelete;
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
    ReferenceDAO referenceDAO;
    Reference reference;

    try {

      i = 0;
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();

      while ((i < this.referencesToDelete.size()) && (successful)) {

        reference = this.referencesToDelete.get(i);

        // Retrieve its relation
        this.documents.add(referenceDAO.getDocuments(reference.getReferenceID()));
        this.authorReferenceReferences.add(referenceDAO.getAuthorReferenceReferences(reference.getReferenceID()));
        this.referenceGroups.add(referenceDAO.getReferenceGroup(reference.getReferenceID()));
        this.referenceSources.add(referenceDAO.getReferenceSource(reference.getReferenceID()));

        successful = referenceDAO.removeReference(reference.getReferenceID(), true);

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
    ReferenceDAO referenceDAO;
    Reference reference;
    AuthorReferenceReference authorReferenceReference;
    ReferenceGroup referenceGroup;
    ReferenceSource referenceSource;
    DocumentReferenceDAO documentReferenceDAO;
    AuthorReferenceReferenceDAO authorReferenceReferenceDAO;

    try {


      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      authorReferenceReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceReferenceDAO();
      documentReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentReferenceDAO();

      i = 0;

      while ((i < this.referencesToDelete.size()) && (successful)) {

        reference = this.referencesToDelete.get(i);

        successful = referenceDAO.addReference(reference, true);

        j = 0;

        while ((j < this.authorReferenceReferences.get(i).size()) && (successful)) {

          authorReferenceReference = this.authorReferenceReferences.get(i).get(j);

          successful = authorReferenceReferenceDAO.addAuthorReferenceReference(reference.getReferenceID(),
                                                                               authorReferenceReference.getAuthorReference().getAuthorReferenceID(),
                                                                               authorReferenceReference.getPosition(), true);

          j++;
        }

        j = 0;

        while ((j < this.documents.get(i).size()) && (successful)) {

          successful = documentReferenceDAO.addDocumentReference(this.documents.get(i).get(j).getDocumentID(),
                                                                 reference.getReferenceID(), true);

          j++;
        }

        referenceGroup = this.referenceGroups.get(i);

        if ((referenceGroup != null) && (successful)) {

          successful = referenceDAO.setReferenceGroup(reference.getReferenceID(),
                                                      referenceGroup.getReferenceGroupID(), true);
        }

        referenceSource = this.referenceSources.get(i);

        if ((referenceSource != null) && (successful)) {

          successful = referenceDAO.setReferenceSource(reference.getReferenceID(),
                                                       referenceSource.getReferenceSourceID(), true);
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
    ReferenceDAO referenceDAO;
    Reference reference;

    try {

      i = 0;
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();

      while ((i < this.referencesToDelete.size()) && (successful)) {

        reference = this.referencesToDelete.get(i);

        successful = referenceDAO.removeReference(reference.getReferenceID(), true);

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
