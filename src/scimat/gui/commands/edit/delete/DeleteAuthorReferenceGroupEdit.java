/*
 * DeleteAuthorReferenceGroupEdit.java
 *
 * Created on 14-mar-2011, 17:39:51
 */
package scimat.gui.commands.edit.delete;

import java.util.ArrayList;
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
 * @author mjcobo
 */
public class DeleteAuthorReferenceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The elements delete
   */
  private ArrayList<AuthorReferenceGroup> authorReferenceGroupsToDelete;
  private ArrayList<ArrayList<AuthorReference>> authorReferences = new ArrayList<ArrayList<AuthorReference>>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param authorReferenceGroupsToDelete
   */
  public DeleteAuthorReferenceGroupEdit(ArrayList<AuthorReferenceGroup> authorReferenceGroupsToDelete) {
    super();
    
    this.authorReferenceGroupsToDelete = authorReferenceGroupsToDelete;
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
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    AuthorReferenceGroup authorReferenceGroup;

    try {

      i = 0;
      authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();

      while ((i < this.authorReferenceGroupsToDelete.size()) && (successful)) {

        authorReferenceGroup = this.authorReferenceGroupsToDelete.get(i);

        // Retrieve its relation
        this.authorReferences.add(authorReferenceGroupDAO.getAuthorReferences(authorReferenceGroup.getAuthorReferenceGroupID()));

        successful = authorReferenceGroupDAO.removeAuthorReferenceGroup(authorReferenceGroup.getAuthorReferenceGroupID(), true);

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
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    AuthorReferenceDAO authorReferenceDAO;
    AuthorReferenceGroup authorReferenceGroup;
    AuthorReference authorReference;

    try {

      authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();

      i = 0;

      while ((i < this.authorReferenceGroupsToDelete.size()) && (successful)) {

        authorReferenceGroup = this.authorReferenceGroupsToDelete.get(i);

        successful = authorReferenceGroupDAO.addAuthorReferenceGroup(authorReferenceGroup, true);

        j = 0;

        while ((j < this.authorReferences.get(i).size()) && (successful)) {

          authorReference = this.authorReferences.get(i).get(j);

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

    int i;
    boolean successful = true;
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    AuthorReferenceGroup authorReferenceGroup;

    try {

      i = 0;
      authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();

      while ((i < this.authorReferenceGroupsToDelete.size()) && (successful)) {

        authorReferenceGroup = this.authorReferenceGroupsToDelete.get(i);

        successful = authorReferenceGroupDAO.removeAuthorReferenceGroup(authorReferenceGroup.getAuthorReferenceGroupID(), true);

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
