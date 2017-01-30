/*
 * MoveAuthorsToNewAuthorGroupEdit.java
 *
 * Created on 24-may-2011, 17:54:23
 */
package scimat.gui.commands.edit.move;

import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.AuthorDAO;
import scimat.model.knowledgebase.dao.AuthorGroupDAO;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class MoveAuthorsToNewAuthorGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private ArrayList<Author> authorsToMove;
  private String groupName;
  private AuthorGroup authorGroup;
  private boolean groupNew; // true if the group has to be created.
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param authorsToMove 
   */
  public MoveAuthorsToNewAuthorGroupEdit(ArrayList<Author> authorsToMove, String groupName) {
    this.authorsToMove = authorsToMove;
    this.groupName = groupName;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  @Override
  public boolean execute() throws KnowledgeBaseException {
    
    boolean successful = true;
    int i;
    Integer authorGroupID;
    Author author;
    AuthorGroupDAO authorGroupDAO;
    AuthorDAO authorDAO;
    
    try {
      
      authorGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO();
      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      
      authorGroup = authorGroupDAO.getAuthorGroup(this.groupName);

      if (authorGroup == null) {

        this.groupNew = true;

        authorGroupID = authorGroupDAO.addAuthorGroup(this.groupName, false, true);

        authorGroup = authorGroupDAO.getAuthorGroup(authorGroupID);

      } else {

        this.groupNew = false;
      }
      
      for (i = 0; i < this.authorsToMove.size(); i++) {
      
        author = this.authorsToMove.get(i);
        
        successful = authorDAO.setAuthorGroup(author.getAuthorID(), authorGroup.getAuthorGroupID(), true);
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
    AuthorGroupDAO authorGroupDAO;
    AuthorDAO authorDAO;
    
    try {
      
      authorGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO();
      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      
      for (i = 0; i < this.authorsToMove.size(); i++) {
        
        successful = authorDAO.setAuthorGroup(this.authorsToMove.get(i).getAuthorID(), null, true);
        
      }
      
      if (this.groupNew) {
        
        successful = authorGroupDAO.removeAuthorGroup(this.authorGroup.getAuthorGroupID(), true);
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
    AuthorGroupDAO authorGroupDAO;
    AuthorDAO authorDAO;
    
    try {
      
      authorGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO();
      authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      
      if (groupNew) {
        
        successful = authorGroupDAO.addAuthorGroup(this.authorGroup, true);
      }
      
      for (i = 0; i < this.authorsToMove.size(); i++) {
        
        successful = authorDAO.setAuthorGroup(this.authorsToMove.get(i).getAuthorID(), 
                authorGroup.getAuthorGroupID(), true);
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
