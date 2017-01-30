/*
 * AddAuthorReferencesToAuthorReferenceGroupEdit.java
 *
 * Created on 10-abr-2011, 18:37:43
 */
package scimat.gui.commands.edit.add;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AddAuthorReferencesToAuthorReferenceGroupEdit extends KnowledgeBaseEdit {



  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<AuthorReference> authorReferencesToAdd;
  private ArrayList<AuthorReferenceGroup> oldAuthorReferenceGroupOfAuthorReferences;
  private AuthorReferenceGroup targetAuthorReferenceGroup;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param authorReferencesToAdd
   * @param targetAuthorReferenceGroup
   */
  public AddAuthorReferencesToAuthorReferenceGroupEdit(ArrayList<AuthorReference> authorReferencesToAdd,
          AuthorReferenceGroup targetAuthorReferenceGroup) {
    
    this.authorReferencesToAdd = authorReferencesToAdd;
    this.targetAuthorReferenceGroup = targetAuthorReferenceGroup;

    this.oldAuthorReferenceGroupOfAuthorReferences = new ArrayList<AuthorReferenceGroup>();
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   * @throws KnowledgeBaseException
   */
  @Override
  public boolean execute() throws KnowledgeBaseException {

    int i;
    AuthorReference authorReference;
    boolean successful = false;

    try {

      for (i = 0; i < this.authorReferencesToAdd.size(); i++) {

        authorReference = this.authorReferencesToAdd.get(i);

        this.oldAuthorReferenceGroupOfAuthorReferences.add(CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().getAuthorReferenceGroup(authorReference.getAuthorReferenceID()));

        successful = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().setAuthorReferenceGroup(authorReference.getAuthorReferenceID(),
                this.targetAuthorReferenceGroup.getAuthorReferenceGroupID(), true);
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

    int i;
    AuthorReferenceGroup authorReferenceGroup;
    boolean successful = false;

    try {

      for (i = 0; i < this.authorReferencesToAdd.size(); i++) {

        authorReferenceGroup = this.oldAuthorReferenceGroupOfAuthorReferences.get(i);

        if (authorReferenceGroup != null) {

          successful = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().setAuthorReferenceGroup(this.authorReferencesToAdd.get(i).getAuthorReferenceID(),
                  authorReferenceGroup.getAuthorReferenceGroupID(), true);

        } else {

          successful = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().setAuthorReferenceGroup(this.authorReferencesToAdd.get(i).getAuthorReferenceID(), null, true);
        }
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
    boolean successful = false;

    try {

      for (i = 0; i < this.authorReferencesToAdd.size(); i++) {

        successful = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().setAuthorReferenceGroup(this.authorReferencesToAdd.get(i).getAuthorReferenceID(),
                this.targetAuthorReferenceGroup.getAuthorReferenceGroupID(), true);
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
