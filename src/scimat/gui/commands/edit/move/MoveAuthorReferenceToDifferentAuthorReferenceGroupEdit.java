/*
 * MoveAuthorReferenceToDifferentAuthorReferenceGroupEdit.java
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
public class MoveAuthorReferenceToDifferentAuthorReferenceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private ArrayList<AuthorReference> authorReferencesToMove;
  private AuthorReferenceGroup[] authorReferenceGroups;
  private ArrayList<AuthorReferenceGroup> authorReferenceGroupsAdded;
  private boolean[] groupNew; // true if the group i has to be created.
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param authorReferencesToMove 
   */
  public MoveAuthorReferenceToDifferentAuthorReferenceGroupEdit(ArrayList<AuthorReference> authorReferencesToMove) {
    this.authorReferencesToMove = authorReferencesToMove;
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
    AuthorReferenceGroup authorReferenceGroup;
    AuthorReference authorReference;
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    AuthorReferenceDAO authorReferenceDAO;
    
    try {
      
      authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
      this.authorReferenceGroupsAdded = new ArrayList<AuthorReferenceGroup>();
      
      this.authorReferenceGroups = new AuthorReferenceGroup[this.authorReferencesToMove.size()];
      this.groupNew = new boolean[this.authorReferencesToMove.size()];
      
      for (i = 0; i < this.authorReferencesToMove.size(); i++) {
      
        authorReference = this.authorReferencesToMove.get(i);
        
        authorReferenceGroup = authorReferenceGroupDAO.getAuthorReferenceGroup(authorReference.getAuthorName());
        
        if (authorReferenceGroup == null) {
        
          this.groupNew[i] = true;
          
          authorReferenceGroupID = authorReferenceGroupDAO.addAuthorReferenceGroup(authorReference.getAuthorName(), false, true);
          
          authorReferenceGroup = authorReferenceGroupDAO.getAuthorReferenceGroup(authorReferenceGroupID);
          this.authorReferenceGroupsAdded.add(authorReferenceGroup);
          
        } else {
        
          this.groupNew[i] = false;
        }
        
        this.authorReferenceGroups[i] = authorReferenceGroup;
        
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
        
        if (this.groupNew[i]) {
        
          successful = authorReferenceGroupDAO.removeAuthorReferenceGroup(this.authorReferenceGroups[i].getAuthorReferenceGroupID(), true);
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
    
    boolean successful = true;
    int i;
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    AuthorReferenceDAO authorReferenceDAO;
    
    try {
      
      authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();
      authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
      
      for (i = 0; i < this.authorReferencesToMove.size(); i++) {
        
        if (groupNew[i]) {
        
          successful = authorReferenceGroupDAO.addAuthorReferenceGroup(this.authorReferenceGroups[i], true);
        }
        
        authorReferenceDAO.setAuthorReferenceGroup(this.authorReferencesToMove.get(i).getAuthorReferenceID(), 
                authorReferenceGroups[i].getAuthorReferenceGroupID(), true);
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
