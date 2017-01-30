/*
 * JoinReferenceEdit.java
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
import scimat.model.knowledgebase.dao.AuthorReferenceReferenceDAO;
import scimat.model.knowledgebase.dao.DocumentReferenceDAO;
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.entity.AuthorReferenceReference;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @authorReference mjcobo
 */
public class JoinReferenceEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<Reference> referencesToMove;
  private Reference targetReference;

  private ArrayList<ArrayList<AuthorReferenceReference>> authorReferenceReferencesOfSources = new ArrayList<ArrayList<AuthorReferenceReference>>();
  private ArrayList<ArrayList<Document>> documentsOfSources = new ArrayList<ArrayList<Document>>();
  private ArrayList<ReferenceGroup> referenceGroupOfSources = new ArrayList<ReferenceGroup>();
  private ArrayList<ReferenceSource> referenceSourceOfSources = new ArrayList<ReferenceSource>();

  private TreeSet<AuthorReferenceReference> authorReferenceReferencesOfTarget = new TreeSet<AuthorReferenceReference>();
  private TreeSet<Document> documentsOfTarget = new TreeSet<Document>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param referencesToMove
   * @param targetReference
   */
  public JoinReferenceEdit(ArrayList<Reference> referencesToMove, Reference targetReference) {

    this.referencesToMove = referencesToMove;
    this.targetReference = targetReference;
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
    ReferenceDAO referenceDAO;
    AuthorReferenceReferenceDAO authorReferenceReferenceDAO;
    DocumentReferenceDAO documentReferenceDAO;
    Reference reference;
    ArrayList<Document> documents;
    Document document;
    AuthorReferenceReference authorReferenceReference;
    ArrayList<AuthorReferenceReference> authorReferenceReferences;

    try {

      i = 0;
      
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      authorReferenceReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceReferenceDAO();
      documentReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentReferenceDAO();

      // Retrieve the realations of the target item.
      this.authorReferenceReferencesOfTarget = new TreeSet<AuthorReferenceReference>(referenceDAO.getAuthorReferenceReferences(this.targetReference.getReferenceID()));
      this.documentsOfTarget = new TreeSet<Document>(referenceDAO.getDocuments(this.targetReference.getReferenceID()));

      while ((i < this.referencesToMove.size()) && (successful)) {

        reference = this.referencesToMove.get(i);

        // Retrieve the relations of the source items
        authorReferenceReferences = referenceDAO.getAuthorReferenceReferences(reference.getReferenceID());
        this.authorReferenceReferencesOfSources.add(authorReferenceReferences);
                
        documents = referenceDAO.getDocuments(reference.getReferenceID());
        this.documentsOfSources.add(documents);
        
        this.referenceGroupOfSources.add(referenceDAO.getReferenceGroup(reference.getReferenceID()));
        
        this.referenceSourceOfSources.add(referenceDAO.getReferenceSource(reference.getReferenceID()));

        // Do the join
        j = 0;

        successful = referenceDAO.removeReference(reference.getReferenceID(), true);
        
        while ((j < authorReferenceReferences.size()) && (successful)) {

          authorReferenceReference = authorReferenceReferences.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! authorReferenceReferenceDAO.checkAuthorReferenceReference(authorReferenceReference.getAuthorReference().getAuthorReferenceID(), 
                  this.targetReference.getReferenceID())) {

            successful = authorReferenceReferenceDAO.addAuthorReferenceReference(this.targetReference.getReferenceID(), 
                    authorReferenceReference.getAuthorReference().getAuthorReferenceID(),
                    authorReferenceReference.getPosition(), true);
          }

          j ++;
        }
        
        j = 0;
        
        while ((j < documents.size()) && (successful)) {

          document = documents.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! documentReferenceDAO.checkDocumentReference(document.getDocumentID(), 
                  this.targetReference.getReferenceID())) {

            successful = documentReferenceDAO.addDocumentReference(document.getDocumentID(), 
                    this.targetReference.getReferenceID(), true);
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
    TreeSet<AuthorReferenceReference> tmpAuthorReferenceReferences;
    TreeSet<Document> tmpDocuments;
    ReferenceDAO referenceDAO;
    DocumentReferenceDAO documentReferenceDAO;
    Reference reference;
    AuthorReferenceReference authorReferenceReference;
    AuthorReferenceReferenceDAO authorReferenceReferenceDAO;
    Iterator<AuthorReferenceReference> itAuthorReferenceReference;
    Iterator<Document> itDocument;

    try {

      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      authorReferenceReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceReferenceDAO();
      documentReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentReferenceDAO();

      tmpAuthorReferenceReferences = new TreeSet<AuthorReferenceReference>(referenceDAO.getAuthorReferenceReferences(this.targetReference.getReferenceID()));
      tmpAuthorReferenceReferences.removeAll(this.authorReferenceReferencesOfTarget);

      itAuthorReferenceReference = tmpAuthorReferenceReferences.iterator();

      while ((itAuthorReferenceReference.hasNext()) && (successful)) {

        successful = authorReferenceReferenceDAO.removeAuthorReferenceReference(this.targetReference.getReferenceID(), 
                itAuthorReferenceReference.next().getAuthorReference().getAuthorReferenceID(), true);
      }
      
      tmpDocuments = new TreeSet<Document>(referenceDAO.getDocuments(this.targetReference.getReferenceID()));
      tmpDocuments.removeAll(this.documentsOfTarget);

      itDocument = tmpDocuments.iterator();

      while ((itDocument.hasNext()) && (successful)) {

        successful = documentReferenceDAO.removeDocumentReference(itDocument.next().getDocumentID(),
                this.targetReference.getReferenceID(), true);
      }

      i = 0;

      while ((i < this.referencesToMove.size()) && (successful)) {

        reference = this.referencesToMove.get(i);

        successful = referenceDAO.addReference(reference, true);

        j = 0;
        
        while ((j < this.authorReferenceReferencesOfSources.get(i).size()) && (successful)) {

          authorReferenceReference = this.authorReferenceReferencesOfSources.get(i).get(j);
          
          successful = authorReferenceReferenceDAO.addAuthorReferenceReference(reference.getReferenceID(),
                  authorReferenceReference.getAuthorReference().getAuthorReferenceID(),
                  authorReferenceReference.getPosition(), true);

          j++;
        }
        
        j = 0;
        
        while ((j < this.documentsOfSources.get(i).size()) && (successful)) {

          // = this.authorReferenceReferencesOfSources.get(i).get(j);
          
          successful = documentReferenceDAO.addDocumentReference(this.documentsOfSources.get(i).get(j).getDocumentID(),
                  reference.getReferenceID(), true);

          j++;
        }
        
        if ((this.referenceGroupOfSources.get(i) != null) && (successful)) {

          successful = referenceDAO.setReferenceGroup(reference.getReferenceID(), 
                  this.referenceGroupOfSources.get(i).getReferenceGroupID(), true);
        }
        
        if ((this.referenceSourceOfSources.get(i) != null) && (successful)) {

          successful = referenceDAO.setReferenceSource(reference.getReferenceID(), 
                  this.referenceSourceOfSources.get(i).getReferenceSourceID(), true);
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
    ReferenceDAO referenceDAO;
    AuthorReferenceReferenceDAO authorReferenceReferenceDAO;
    DocumentReferenceDAO documentReferenceDAO;
    Reference reference;
    ArrayList<Document> documents;
    Document document;
    AuthorReferenceReference authorReferenceReference;
    ArrayList<AuthorReferenceReference> authorReferenceReferences;

    try {

      i = 0;
      
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      authorReferenceReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceReferenceDAO();
      documentReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentReferenceDAO();

      while ((i < this.referencesToMove.size()) && (successful)) {

        reference = this.referencesToMove.get(i);

        // Retrieve the relations of the source items
        authorReferenceReferences = this.authorReferenceReferencesOfSources.get(i);
        documents = this.documentsOfSources.get(i);

        // Do the join
        j = 0;

        successful = referenceDAO.removeReference(reference.getReferenceID(), true);
        
        while ((j < authorReferenceReferences.size()) && (successful)) {

          authorReferenceReference = authorReferenceReferences.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! authorReferenceReferenceDAO.checkAuthorReferenceReference(authorReferenceReference.getAuthorReference().getAuthorReferenceID(), 
                  this.targetReference.getReferenceID())) {

            successful = authorReferenceReferenceDAO.addAuthorReferenceReference(this.targetReference.getReferenceID(), 
                    authorReferenceReference.getAuthorReference().getAuthorReferenceID(),
                    authorReferenceReference.getPosition(), true);
          }

          j ++;
        }
        
        j = 0;
        
        while ((j < documents.size()) && (successful)) {

          document = documents.get(j);
          
          // If the target element is not associated with this element we perform the association.
          if (! documentReferenceDAO.checkDocumentReference(document.getDocumentID(), 
                  this.targetReference.getReferenceID())) {

            successful = documentReferenceDAO.addDocumentReference(document.getDocumentID(), 
                    this.targetReference.getReferenceID(), true);
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
