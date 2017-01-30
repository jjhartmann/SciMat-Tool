/*
 * JoinAuthorReferenceGroupEdit.java
 *
 * Created on 18-may-2011, 13:43:52
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
import scimat.model.knowledgebase.dao.AuthorReferenceGroupDAO;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @authorReference mjcobo
 */
public class JoinAuthorReferenceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<AuthorReferenceGroup> authorReferenceGroupsToMove;
  private AuthorReferenceGroup targetAuthorReferenceGroup;

  private ArrayList<ArrayList<AuthorReference>> authorReferencesOfSources = new ArrayList<ArrayList<AuthorReference>>();

  private TreeSet<AuthorReference> authorReferencesOfTarget = new TreeSet<AuthorReference>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param authorReferenceGroupsToMove
   * @param targetAuthorReferenceGroup
   */
  public JoinAuthorReferenceGroupEdit(ArrayList<AuthorReferenceGroup> authorReferenceGroupsToMove, AuthorReferenceGroup targetAuthorReferenceGroup) {

    this.authorReferenceGroupsToMove = authorReferenceGroupsToMove;
    this.targetAuthorReferenceGroup = targetAuthorReferenceGroup;
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
    AuthorReferenceDAO authorReferenceDAO;
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    AuthorReferenceGroup authorReferenceGroup;
    ArrayList<AuthorReference> authorReferences;

    try {

      i = 0;

      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
      authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();

      // Retrieve the realations of the target item.
      this.authorReferencesOfTarget = new TreeSet<AuthorReference>(authorReferenceGroupDAO.getAuthorReferences(this.targetAuthorReferenceGroup.getAuthorReferenceGroupID()));

      while ((i < this.authorReferenceGroupsToMove.size()) && (successful)) {

        authorReferenceGroup = this.authorReferenceGroupsToMove.get(i);

        // Retrieve the relations of the source items
        authorReferences = authorReferenceGroupDAO.getAuthorReferences(authorReferenceGroup.getAuthorReferenceGroupID());
        this.authorReferencesOfSources.add(authorReferences);

        // Do the join
        j = 0;

        successful = authorReferenceGroupDAO.removeAuthorReferenceGroup(authorReferenceGroup.getAuthorReferenceGroupID(), true);

        while ((j < authorReferences.size()) && (successful)) {
          
          successful = authorReferenceDAO.setAuthorReferenceGroup(authorReferences.get(j).getAuthorReferenceID(), 
                  this.targetAuthorReferenceGroup.getAuthorReferenceGroupID(), true);

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
    TreeSet<AuthorReference> tmpAuthorReferences;
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    AuthorReferenceDAO authorReferenceDAO;
    AuthorReferenceGroup authorReferenceGroup;
    AuthorReference authorReference;
    Iterator<AuthorReference> itAuthorReference;

    try {

      authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
      

      tmpAuthorReferences = new TreeSet<AuthorReference>(authorReferenceGroupDAO.getAuthorReferences(this.targetAuthorReferenceGroup.getAuthorReferenceGroupID()));
      tmpAuthorReferences.removeAll(this.authorReferencesOfTarget);

      itAuthorReference = tmpAuthorReferences.iterator();

      while ((itAuthorReference.hasNext()) && (successful)) {

        successful = authorReferenceDAO.setAuthorReferenceGroup(itAuthorReference.next().getAuthorReferenceID(), null, true);
      }

      i = 0;

      while ((i < this.authorReferenceGroupsToMove.size()) && (successful)) {

        authorReferenceGroup = this.authorReferenceGroupsToMove.get(i);

        successful = authorReferenceGroupDAO.addAuthorReferenceGroup(authorReferenceGroup, true);

        j = 0;

        while ((j < this.authorReferencesOfSources.get(i).size()) && (successful)) {

          authorReference = this.authorReferencesOfSources.get(i).get(j);

          successful = authorReferenceDAO.setAuthorReferenceGroup(authorReference.getAuthorReferenceID(),
                                                authorReferenceGroup.getAuthorReferenceGroupID(), true);

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
    AuthorReferenceDAO authorReferenceDAO;
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    AuthorReferenceGroup authorReferenceGroup;
    ArrayList<AuthorReference> authorReferences;

    try {

      i = 0;

      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
      authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();

      while ((i < this.authorReferenceGroupsToMove.size()) && (successful)) {

        authorReferenceGroup = this.authorReferenceGroupsToMove.get(i);

        // Retrieve the relations of the source items
        authorReferences = this.authorReferencesOfSources.get(i) ;

        // Do the join
        j = 0;

        successful = authorReferenceGroupDAO.removeAuthorReferenceGroup(authorReferenceGroup.getAuthorReferenceGroupID(), true);

        while ((j < authorReferences.size()) && (successful)) {
          
          successful = authorReferenceDAO.setAuthorReferenceGroup(authorReferences.get(j).getAuthorReferenceID(), 
                  this.targetAuthorReferenceGroup.getAuthorReferenceGroupID(), true);

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
