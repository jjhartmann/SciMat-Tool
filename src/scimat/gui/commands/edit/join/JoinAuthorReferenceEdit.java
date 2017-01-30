/*
 * JoinAuthorReferenceEdit.java
 *
 * Created on 26-abr-2011, 18:06:00
 */
package scimat.gui.commands.edit.join;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.AuthorReferenceDAO;
import scimat.model.knowledgebase.dao.AuthorReferenceReferenceDAO;
import scimat.model.knowledgebase.entity.AuthorReferenceReference;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class JoinAuthorReferenceEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<AuthorReference> authorReferencesToMove;
  private AuthorReference targetAuthorReference;
  
  private ArrayList<ArrayList<AuthorReferenceReference>> authorReferenceReferenceOfSources = new ArrayList<ArrayList<AuthorReferenceReference>>();
  private ArrayList<AuthorReferenceGroup> authorReferenceGroupOfSources = new ArrayList<AuthorReferenceGroup>();
  private ArrayList<Author> authorOfSources = new ArrayList<Author>();
  
  private TreeSet<AuthorReferenceReference> authorReferenceReferenceOfTarget = new TreeSet<AuthorReferenceReference>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param authorReferencesToMove
   * @param targetAuthorReference
   */
  public JoinAuthorReferenceEdit(ArrayList<AuthorReference> authorReferencesToMove, AuthorReference targetAuthorReference) {

    this.authorReferencesToMove = authorReferencesToMove;
    this.targetAuthorReference = targetAuthorReference;
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
    AuthorReferenceReferenceDAO authorReferenceReferenceDAO;
    AuthorReferenceDAO authorReferenceDAO;
    AuthorReference authorReference;
    AuthorReferenceReference authorReferenceReference;
    ArrayList<AuthorReferenceReference> authorReferenceReferences;

    try {

      i = 0;
      
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
      authorReferenceReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceReferenceDAO();

      // Retrieve the realations of the target item.
      this.authorReferenceReferenceOfTarget = new TreeSet<AuthorReferenceReference>(authorReferenceDAO.getReferences(this.targetAuthorReference.getAuthorReferenceID()));

      while ((i < this.authorReferencesToMove.size()) && (successful)) {

        authorReference = this.authorReferencesToMove.get(i);

        // Retrieve the relations of the source items
        authorReferenceReferences = authorReferenceDAO.getReferences(authorReference.getAuthorReferenceID());
        this.authorReferenceReferenceOfSources.add(authorReferenceReferences);
        
        this.authorReferenceGroupOfSources.add(authorReferenceDAO.getAuthorReferenceGroup(authorReference.getAuthorReferenceID()));
        
        this.authorOfSources.add(authorReferenceDAO.getAuthor(authorReference.getAuthorReferenceID()));

        // Do the join
        j = 0;

        successful = authorReferenceDAO.removeAuthorReference(authorReference.getAuthorReferenceID(), true);

        while ((j < authorReferenceReferences.size()) && (successful)) {

          authorReferenceReference = authorReferenceReferences.get(j);

          // If the target element is not associated with this element we perform the association.
          if (! authorReferenceReferenceDAO.checkAuthorReferenceReference(this.targetAuthorReference.getAuthorReferenceID(), authorReferenceReference.getReference().getReferenceID())) {

            successful = authorReferenceReferenceDAO.addAuthorReferenceReference(authorReferenceReference.getReference().getReferenceID(), 
                    this.targetAuthorReference.getAuthorReferenceID(), 
                    authorReferenceReference.getPosition(), true);
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
    AuthorReference authorReference;
    AuthorReferenceReferenceDAO authorReferenceReferenceDAO;
    AuthorReferenceReference authorReferenceReference;
    AuthorReferenceDAO authorReferenceDAO;
    Iterator<AuthorReferenceReference> itAuthorReferenceReference;

    try {
      
      authorReferenceReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceReferenceDAO();
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();

      // Restore the target entity

      tmpAuthorReferenceReferences = new TreeSet<AuthorReferenceReference>(authorReferenceDAO.getReferences(this.targetAuthorReference.getAuthorReferenceID()));
      tmpAuthorReferenceReferences.removeAll(this.authorReferenceReferenceOfTarget);

      itAuthorReferenceReference = tmpAuthorReferenceReferences.iterator();

      while ((itAuthorReferenceReference.hasNext()) && (successful)) {
        
        successful = authorReferenceReferenceDAO.removeAuthorReferenceReference(itAuthorReferenceReference.next().getReference().getReferenceID(), 
                this.targetAuthorReference.getAuthorReferenceID(), true);
      }

      i = 0;
      
      // Restore the source entities

      while ((i < this.authorReferencesToMove.size()) && (successful)) {

        authorReference = this.authorReferencesToMove.get(i);

        successful = authorReferenceDAO.addAuthorReference(authorReference, true);

        j = 0;

        while ((j < this.authorReferenceReferenceOfSources.get(i).size()) && (successful)) {

          authorReferenceReference = this.authorReferenceReferenceOfSources.get(i).get(j);

          successful = authorReferenceReferenceDAO.addAuthorReferenceReference(authorReferenceReference.getReference().getReferenceID(),
                  authorReference.getAuthorReferenceID(), 
                  authorReferenceReference.getPosition(), true);

          j++;
        }

        if ((this.authorReferenceGroupOfSources.get(i) != null) && (successful)) {

          successful = authorReferenceDAO.setAuthorReferenceGroup(authorReference.getAuthorReferenceID(), 
                  this.authorReferenceGroupOfSources.get(i).getAuthorReferenceGroupID(), true);
        }

        if ((this.authorOfSources.get(i) != null) && (successful)) {

          successful = authorReferenceDAO.setAuthor(authorReference.getAuthorReferenceID(), 
                  this.authorOfSources.get(i).getAuthorID(), true);
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
    AuthorReferenceReferenceDAO authorReferenceReferenceDAO;
    AuthorReferenceDAO authorReferenceDAO;
    AuthorReference authorReference;
    AuthorReferenceReference authorReferenceReference;
    ArrayList<AuthorReferenceReference> authorReferenceReferences;

    try {

      i = 0;
      
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
      authorReferenceReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceReferenceDAO();

      while ((i < this.authorReferencesToMove.size()) && (successful)) {

        authorReference = this.authorReferencesToMove.get(i);

        // Retrieve the relations of the source items
        authorReferenceReferences = this.authorReferenceReferenceOfSources.get(i);

        // Do the join
        j = 0;

        successful = authorReferenceDAO.removeAuthorReference(authorReference.getAuthorReferenceID(), true);

        while ((j < authorReferenceReferences.size()) && (successful)) {

          authorReferenceReference = authorReferenceReferences.get(j);

          // If the target element is not associated with this element we perform the association.
          if (! authorReferenceReferenceDAO.checkAuthorReferenceReference(this.targetAuthorReference.getAuthorReferenceID(), authorReferenceReference.getReference().getReferenceID())) {

            successful = authorReferenceReferenceDAO.addAuthorReferenceReference(authorReferenceReference.getReference().getReferenceID(), 
                    this.targetAuthorReference.getAuthorReferenceID(), 
                    authorReferenceReference.getPosition(), true);
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
