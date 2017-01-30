/*
 * JoinReferenceGroupEdit.java
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
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.dao.ReferenceGroupDAO;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @reference mjcobo
 */
public class JoinReferenceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<ReferenceGroup> referenceGroupsToMove;
  private ReferenceGroup targetReferenceGroup;

  private ArrayList<ArrayList<Reference>> referencesOfSources = new ArrayList<ArrayList<Reference>>();

  private TreeSet<Reference> referencesOfTarget = new TreeSet<Reference>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param referenceGroupsToMove
   * @param targetReferenceGroup
   */
  public JoinReferenceGroupEdit(ArrayList<ReferenceGroup> referenceGroupsToMove, ReferenceGroup targetReferenceGroup) {

    this.referenceGroupsToMove = referenceGroupsToMove;
    this.targetReferenceGroup = targetReferenceGroup;
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
    ReferenceGroupDAO referenceGroupDAO;
    ReferenceGroup referenceGroup;
    ArrayList<Reference> references;

    try {

      i = 0;

      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      referenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO();

      // Retrieve the realations of the target item.
      this.referencesOfTarget = new TreeSet<Reference>(referenceGroupDAO.getReferences(this.targetReferenceGroup.getReferenceGroupID()));

      while ((i < this.referenceGroupsToMove.size()) && (successful)) {

        referenceGroup = this.referenceGroupsToMove.get(i);

        // Retrieve the relations of the source items
        references = referenceGroupDAO.getReferences(referenceGroup.getReferenceGroupID());
        this.referencesOfSources.add(references);

        // Do the join
        j = 0;

        successful = referenceGroupDAO.removeReferenceGroup(referenceGroup.getReferenceGroupID(), true);

        while ((j < references.size()) && (successful)) {
          
          successful = referenceDAO.setReferenceGroup(references.get(j).getReferenceID(), 
                  this.targetReferenceGroup.getReferenceGroupID(), true);

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
    TreeSet<Reference> tmpReferences;
    ReferenceGroupDAO referenceGroupDAO;
    ReferenceDAO referenceDAO;
    ReferenceGroup referenceGroup;
    Reference reference;
    Iterator<Reference> itReference;

    try {

      referenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO();
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      

      tmpReferences = new TreeSet<Reference>(referenceGroupDAO.getReferences(this.targetReferenceGroup.getReferenceGroupID()));
      tmpReferences.removeAll(this.referencesOfTarget);

      itReference = tmpReferences.iterator();

      while ((itReference.hasNext()) && (successful)) {

        successful = referenceDAO.setReferenceGroup(itReference.next().getReferenceID(), null, true);
      }

      i = 0;

      while ((i < this.referenceGroupsToMove.size()) && (successful)) {

        referenceGroup = this.referenceGroupsToMove.get(i);

        successful = referenceGroupDAO.addReferenceGroup(referenceGroup, true);

        j = 0;

        while ((j < this.referencesOfSources.get(i).size()) && (successful)) {

          reference = this.referencesOfSources.get(i).get(j);

          successful = referenceDAO.setReferenceGroup(reference.getReferenceID(),
                                                referenceGroup.getReferenceGroupID(), true);

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
    ReferenceDAO referenceDAO;
    ReferenceGroupDAO referenceGroupDAO;
    ReferenceGroup referenceGroup;
    ArrayList<Reference> references;

    try {

      i = 0;

      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      referenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO();

      while ((i < this.referenceGroupsToMove.size()) && (successful)) {

        referenceGroup = this.referenceGroupsToMove.get(i);

        // Retrieve the relations of the source items
        references = this.referencesOfSources.get(i) ;

        // Do the join
        j = 0;

        successful = referenceGroupDAO.removeReferenceGroup(referenceGroup.getReferenceGroupID(), true);

        while ((j < references.size()) && (successful)) {
          
          successful = referenceDAO.setReferenceGroup(references.get(j).getReferenceID(), 
                  this.targetReferenceGroup.getReferenceGroupID(), true);

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
