/*
 * MoveAuthorReferencesToNewAuthorReferenceGroupEdit.java
 *
 * Created on 24-may-2011, 17:54:23
 */
package scimat.gui.commands.edit.move;

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
public class MoveAuthorReferencesToNewAuthorReferenceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private ArrayList<AuthorReference> authorReferencesToMove;
  private String groupName;
  private AuthorReferenceGroup authorReferenceGroup;
  private boolean groupNew; // true if the group has to be created.
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param authorReferencesToMove
   * @param groupName 
   */
  public MoveAuthorReferencesToNewAuthorReferenceGroupEdit(ArrayList<AuthorReference> authorReferencesToMove, String groupName) {
    this.authorReferencesToMove = authorReferencesToMove;
    this.groupName = groupName;
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
    
    boolean successful = true;
    int i;
    Integer authorReferenceGroupID;
    AuthorReference authorReference;
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    AuthorReferenceDAO authorReferenceDAO;
    
    try {
      
      authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
      
      authorReferenceGroup = authorReferenceGroupDAO.getAuthorReferenceGroup(this.groupName);

      if (authorReferenceGroup == null) {

        this.groupNew = true;

        authorReferenceGroupID = authorReferenceGroupDAO.addAuthorReferenceGroup(this.groupName, false, true);

        authorReferenceGroup = authorReferenceGroupDAO.getAuthorReferenceGroup(authorReferenceGroupID);

      } else {

        this.groupNew = false;
      }
      
      for (i = 0; i < this.authorReferencesToMove.size(); i++) {
      
        authorReference = this.authorReferencesToMove.get(i);
        
        successful = authorReferenceDAO.setAuthorReferenceGroup(authorReference.getAuthorReferenceID(), 
                authorReferenceGroup.getAuthorReferenceGroupID(), true);
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
    
    boolean successful = true;
    int i;
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    AuthorReferenceDAO authorReferenceDAO;
    
    try {
      
      authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
      
      for (i = 0; i < this.authorReferencesToMove.size(); i++) {
        
        successful = authorReferenceDAO.setAuthorReferenceGroup(this.authorReferencesToMove.get(i).getAuthorReferenceID(), null, true);
      }
      
      if (this.groupNew) {
        
        successful = authorReferenceGroupDAO.removeAuthorReferenceGroup(this.authorReferenceGroup.getAuthorReferenceGroupID(), true);
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
    int i;
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    AuthorReferenceDAO authorReferenceDAO;
    
    try {
      
      authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
      
      if (groupNew) {
        
        successful = authorReferenceGroupDAO.addAuthorReferenceGroup(this.authorReferenceGroup, true);
      }
      
      for (i = 0; i < this.authorReferencesToMove.size(); i++) {
        
        successful = authorReferenceDAO.setAuthorReferenceGroup(this.authorReferencesToMove.get(i).getAuthorReferenceID(), 
                authorReferenceGroup.getAuthorReferenceGroupID(), true);
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
