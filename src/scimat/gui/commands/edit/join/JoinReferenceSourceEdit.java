/*
 * JoinReferenceSourceEdit.java
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
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.dao.ReferenceSourceDAO;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class JoinReferenceSourceEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<ReferenceSource> referenceSourcesToMove;
  private ReferenceSource targetReferenceSource;
  
  private ArrayList<ReferenceSourceGroup> referenceSourceGroupOfSources = new ArrayList<ReferenceSourceGroup>();
  private ArrayList<ArrayList<Reference>> referencesOfSources = new ArrayList<ArrayList<Reference>>();

  private TreeSet<Reference> referencesOfTarget = new TreeSet<Reference>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param referenceSourcesToMove
   * @param target
   */
  public JoinReferenceSourceEdit(ArrayList<ReferenceSource> referenceSourcesToMove, ReferenceSource targetReferenceSource) {

    this.referenceSourcesToMove = referenceSourcesToMove;
    this.targetReferenceSource = targetReferenceSource;
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
    ReferenceSourceDAO referenceSourceDAO;
    ReferenceDAO referenceDAO;
    ReferenceSource referenceSource;
    ArrayList<Reference> references;

    try {

      i = 0;

      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();

      // Retrieve the realations of the target item.
      this.referencesOfTarget = new TreeSet<Reference>(referenceSourceDAO.getReferences(this.targetReferenceSource.getReferenceSourceID()));

      while ((i < this.referenceSourcesToMove.size()) && (successful)) {

        referenceSource = this.referenceSourcesToMove.get(i);

        // Retrieve the relations of the source items
        references = referenceSourceDAO.getReferences(referenceSource.getReferenceSourceID());
        this.referenceSourceGroupOfSources.add(referenceSourceDAO.getReferenceSourceGroup(referenceSource.getReferenceSourceID()));

        // Do the join
        j = 0;

        // Delete the items and its associations
        successful = referenceSourceDAO.removeReferenceSource(referenceSource.getReferenceSourceID(), true);

        while ((j < references.size()) && (successful)) {
          
          successful = referenceDAO.setReferenceSource(references.get(j).getReferenceID(), 
                  this.targetReferenceSource.getReferenceSourceID(), true);

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
    TreeSet<Reference> tmpReference;
    ReferenceSourceDAO referenceSourceDAO;
    ReferenceDAO referenceDAO;
    ReferenceSource referenceSource;
    Reference reference;
    Iterator<Reference> itReference;

    try {

      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();

      // Restore the target entity

      tmpReference = new TreeSet<Reference>(referenceSourceDAO.getReferences(this.targetReferenceSource.getReferenceSourceID()));
      tmpReference.removeAll(this.referencesOfTarget);

      itReference = tmpReference.iterator();

      while ((itReference.hasNext()) && (successful)) {

        successful = referenceDAO.setReferenceSource(itReference.next().getReferenceID(), null, true);
      }
      
      i = 0;
      
      // Restore the source entities

      while ((i < this.referenceSourcesToMove.size()) && (successful)) {

        referenceSource = this.referenceSourcesToMove.get(i);

        successful = referenceSourceDAO.addReferenceSource(referenceSource, true);

        j = 0;
        
        while ((j < this.referencesOfSources.get(i).size()) && (successful)) {

          reference = this.referencesOfSources.get(i).get(j);

          // FIX
          successful = referenceDAO.setReferenceSource(reference.getReferenceID(),
                  referenceSource.getReferenceSourceID(), true);

          j++;
        }
        

        if ((this.referenceSourceGroupOfSources.get(i) != null) && (successful)) {

          successful = referenceSourceDAO.setReferenceSourceGroup(referenceSource.getReferenceSourceID(), 
                  this.referenceSourceGroupOfSources.get(i).getReferenceSourceGroupID(), true);
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
    ReferenceSourceDAO referenceSourceDAO;
    ReferenceDAO referenceDAO;
    ReferenceSource referenceSource;
    ArrayList<Reference> references;

    try {

      i = 0;

      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();

      while ((i < this.referenceSourcesToMove.size()) && (successful)) {

        referenceSource = this.referenceSourcesToMove.get(i);

        // Retrieve the relations of the source items
        references = this.referencesOfSources.get(i);

        // Do the join
        j = 0;

        // Delete the items and its associations
        successful = referenceSourceDAO.removeReferenceSource(referenceSource.getReferenceSourceID(), true);

        while ((j < references.size()) && (successful)) {
          
          successful = referenceDAO.setReferenceSource(references.get(j).getReferenceID(), 
                  this.targetReferenceSource.getReferenceSourceID(), true);

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
