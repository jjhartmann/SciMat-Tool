/*
 * UpdatePeriodEdit.java
 *
 * Created on 14-mar-2011, 17:38:24
 */
package scimat.gui.commands.edit.update;

import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class UpdatePeriodEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer periodID;

  /**
   *
   */
  private String name;
  
  /**
   * 
   */
  private int position;

  /**
   * The elements added
   */
  private Period periodOld;
  
  private Period periodUpdated;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public UpdatePeriodEdit(Integer periodID, String name, int position) {
    super();

    this.periodID = periodID;
    this.name = name;
    this.position = position;
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

    boolean successful = false;

    try {

      this.periodOld = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().getPeriod(periodID);

      if (this.periodOld.getName().equals(this.name)) {

        successful = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().setPosition(periodID, position, true);

        this.periodUpdated = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().getPeriod(periodID);

      } else if (this.name == null) {

        successful = false;
        this.errorMessage = "The name can not be null.";

      } else if (CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().checkPeriod(name)) {

        successful = false;
        this.errorMessage = "A period yet exists with this name.";

      } else {

        successful = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().setName(periodID, name, true);
        successful = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().setPosition(periodID, position, true);

        this.periodUpdated = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().getPeriod(periodID);

      }
      
      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();
        
        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

        successful = true;

        UndoStack.addEdit(this);

      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();
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

    boolean flag;

    try {
      
      if (this.periodOld.getName().equals(this.periodUpdated.getName())) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().setPosition(periodOld.getPeriodID(), periodOld.getPosition(), true);
        
      } else {

        flag = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().setName(periodOld.getPeriodID(), periodOld.getName(), true);
        flag = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().setPosition(periodOld.getPeriodID(), periodOld.getPosition(), true);
      }

      if (flag) {

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

    boolean flag;

    try {

      if (this.periodOld.getName().equals(this.name)) {
      
        flag = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().setPosition(this.periodID, this.position, true);
        
      } else {

        flag = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().setName(this.periodID, this.name, true);
        flag = CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().setPosition(this.periodID, this.position, true);
      }

      if (flag) {

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
