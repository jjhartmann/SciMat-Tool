/*
 * JournalSlaveJournalSubjectCategoryPublishDatePanel.java
 *
 * Created on 21-mar-2011, 19:19:37
 */
package scimat.gui.components.slavepanel;

import java.util.ArrayList;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.tablemodel.JournalSlaveJournalSubjectCategoryPublishDateTableModel;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.entity.JournalSubjectCategoryPublishDate;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.entity.SubjectCategory;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;
import scimat.project.observer.EntityObserver;
import scimat.project.observer.JournalRelationSubjectCategoryRelationPublishDateObserver;

/**
 *
 * @author mjcobo
 */
public class JournalSlaveJournalSubjectCategoryPublishDatePanel 
        extends GenericSlaveListPanel<Journal, JournalSubjectCategoryPublishDate>
        implements JournalRelationSubjectCategoryRelationPublishDateObserver  {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   */
  public JournalSlaveJournalSubjectCategoryPublishDatePanel() {
    super(new JournalSlaveJournalSubjectCategoryPublishDateTableModel());
    
    CurrentProject.getInstance().getKbObserver().addJournalSubjectCategoryPublishDate(this);
    CurrentProject.getInstance().getKbObserver().addSubjectCategoryObserver(new SubjectCategoryWrapper());
    CurrentProject.getInstance().getKbObserver().addPublishDateObserver(new PublishDateWrapper());
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  public void setMasterItem(Journal journal) {

    this.masterItem = journal;

    try {

      if (this.masterItem != null) {

        relationChanged();

      } else {

        this.refreshData(new ArrayList<JournalSubjectCategoryPublishDate>());

      }

    } catch (KnowledgeBaseException e) {
    
      ErrorDialogManager.getInstance().showException(e);

    }
  }

  

  /**
   * 
   * @throws KnowledgeBaseException
   */
  public void relationChanged() throws KnowledgeBaseException {

    if (this.masterItem != null) {
      refreshData(CurrentProject.getInstance().getFactoryDAO().getJournalDAO().getJournalSubjectCategoryPublishDates(this.masterItem.getJournalID()));
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                           Private Classes                               */
  /***************************************************************************/

  /**
   *
   */
  private class SubjectCategoryWrapper implements EntityObserver<SubjectCategory> {

    /**
     *
     * @param items
     * @throws KnowledgeBaseException
     */
    public void entityAdded(ArrayList<SubjectCategory> items) throws KnowledgeBaseException {
      // Do not do nothing
    }

    /**
     *
     * @param entity
     * @throws KnowledgeBaseException
     */
    public void entityRefresh() throws KnowledgeBaseException {

      relationChanged();
    }

    /**
     *
     * @param items
     * @throws KnowledgeBaseException
     */
    public void entityRemoved(ArrayList<SubjectCategory> items) throws KnowledgeBaseException {
      // Do not do nothing
    }

    /**
     *
     * @param items
     * @throws KnowledgeBaseException
     */
    public void entityUpdated(ArrayList<SubjectCategory> items) throws KnowledgeBaseException {
      // Do not do nothing
    }
  }

  /**
   * 
   */
  private class PublishDateWrapper implements EntityObserver<PublishDate> {

    /**
     *
     * @param items
     * @throws KnowledgeBaseException
     */
    public void entityAdded(ArrayList<PublishDate> items) throws KnowledgeBaseException {
      // Do not do nothing
    }

    /**
     *
     * @param entity
     * @throws KnowledgeBaseException
     */
    public void entityRefresh() throws KnowledgeBaseException {

      relationChanged();
    }

    /**
     *
     * @param items
     * @throws KnowledgeBaseException
     */
    public void entityRemoved(ArrayList<PublishDate> items) throws KnowledgeBaseException {
      // Do not do nothing
    }

    /**
     *
     * @param items
     * @throws KnowledgeBaseException
     */
    public void entityUpdated(ArrayList<PublishDate> items) throws KnowledgeBaseException {
      // Do not do nothing
    }
  }
}
