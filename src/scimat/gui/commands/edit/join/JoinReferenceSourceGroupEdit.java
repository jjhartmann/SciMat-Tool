/*
 * JoinReferenceSourceGroupEdit.java
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
import scimat.model.knowledgebase.dao.ReferenceSourceDAO;
import scimat.model.knowledgebase.dao.ReferenceSourceGroupDAO;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @referenceSource mjcobo
 */
public class JoinReferenceSourceGroupEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<ReferenceSourceGroup> referenceSourceGroupsToMove;
  private ReferenceSourceGroup targetReferenceSourceGroup;

  private ArrayList<ArrayList<ReferenceSource>> referenceSourcesOfSources = new ArrayList<ArrayList<ReferenceSource>>();

  private TreeSet<ReferenceSource> referenceSourcesOfTarget = new TreeSet<ReferenceSource>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param referenceSourceGroupsToMove
   * @param targetReferenceSourceGroup
   */
  public JoinReferenceSourceGroupEdit(ArrayList<ReferenceSourceGroup> referenceSourceGroupsToMove, ReferenceSourceGroup targetReferenceSourceGroup) {

    this.referenceSourceGroupsToMove = referenceSourceGroupsToMove;
    this.targetReferenceSourceGroup = targetReferenceSourceGroup;
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
    ReferenceSourceGroupDAO referenceSourceGroupDAO;
    ReferenceSourceGroup referenceSourceGroup;
    ArrayList<ReferenceSource> referenceSources;

    try {

      i = 0;

      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      referenceSourceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO();

      // Retrieve the realations of the target item.
      this.referenceSourcesOfTarget = new TreeSet<ReferenceSource>(referenceSourceGroupDAO.getReferenceSources(this.targetReferenceSourceGroup.getReferenceSourceGroupID()));

      while ((i < this.referenceSourceGroupsToMove.size()) && (successful)) {

        referenceSourceGroup = this.referenceSourceGroupsToMove.get(i);

        // Retrieve the relations of the source items
        referenceSources = referenceSourceGroupDAO.getReferenceSources(referenceSourceGroup.getReferenceSourceGroupID());
        this.referenceSourcesOfSources.add(referenceSources);

        // Do the join
        j = 0;

        successful = referenceSourceGroupDAO.removeReferenceSourceGroup(referenceSourceGroup.getReferenceSourceGroupID(), true);

        while ((j < referenceSources.size()) && (successful)) {
          
          successful = referenceSourceDAO.setReferenceSourceGroup(referenceSources.get(j).getReferenceSourceID(), 
                  this.targetReferenceSourceGroup.getReferenceSourceGroupID(), true);

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
    TreeSet<ReferenceSource> tmpReferenceSources;
    ReferenceSourceGroupDAO referenceSourceGroupDAO;
    ReferenceSourceDAO referenceSourceDAO;
    ReferenceSourceGroup referenceSourceGroup;
    ReferenceSource referenceSource;
    Iterator<ReferenceSource> itReferenceSource;

    try {

      referenceSourceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO();
      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      

      tmpReferenceSources = new TreeSet<ReferenceSource>(referenceSourceGroupDAO.getReferenceSources(this.targetReferenceSourceGroup.getReferenceSourceGroupID()));
      tmpReferenceSources.removeAll(this.referenceSourcesOfTarget);

      itReferenceSource = tmpReferenceSources.iterator();

      while ((itReferenceSource.hasNext()) && (successful)) {

        successful = referenceSourceDAO.setReferenceSourceGroup(itReferenceSource.next().getReferenceSourceID(), null, true);
      }

      i = 0;

      while ((i < this.referenceSourceGroupsToMove.size()) && (successful)) {

        referenceSourceGroup = this.referenceSourceGroupsToMove.get(i);

        successful = referenceSourceGroupDAO.addReferenceSourceGroup(referenceSourceGroup, true);

        j = 0;

        while ((j < this.referenceSourcesOfSources.get(i).size()) && (successful)) {

          referenceSource = this.referenceSourcesOfSources.get(i).get(j);

          successful = referenceSourceDAO.setReferenceSourceGroup(referenceSource.getReferenceSourceID(),
                                                referenceSourceGroup.getReferenceSourceGroupID(), true);

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
    ReferenceSourceDAO referenceSourceDAO;
    ReferenceSourceGroupDAO referenceSourceGroupDAO;
    ReferenceSourceGroup referenceSourceGroup;
    ArrayList<ReferenceSource> referenceSources;

    try {

      i = 0;

      referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      referenceSourceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO();

      while ((i < this.referenceSourceGroupsToMove.size()) && (successful)) {

        referenceSourceGroup = this.referenceSourceGroupsToMove.get(i);

        // Retrieve the relations of the source items
        referenceSources = this.referenceSourcesOfSources.get(i) ;

        // Do the join
        j = 0;

        successful = referenceSourceGroupDAO.removeReferenceSourceGroup(referenceSourceGroup.getReferenceSourceGroupID(), true);

        while ((j < referenceSources.size()) && (successful)) {
          
          successful = referenceSourceDAO.setReferenceSourceGroup(referenceSources.get(j).getReferenceSourceID(), 
                  this.targetReferenceSourceGroup.getReferenceSourceGroupID(), true);

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
