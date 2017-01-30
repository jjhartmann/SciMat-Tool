/*
 * SubjectCategorySlaveJournalSubjectCategoryPublishDatePanel.java
 *
 * Created on 21-mar-2011, 19:19:37
 */
package scimat.gui.components.slavepanel;

import java.util.ArrayList;
import scimat.gui.components.ErrorDialogManager;
import scimat.gui.components.tablemodel.SubjectCategorySlaveJournalSubjectCategoryPublishDateTableModel;
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
public class SubjectCategorySlaveJournalSubjectCategoryPublishDatePanel 
        extends GenericSlaveListPanel<SubjectCategory,
                                      JournalSubjectCategoryPublishDate>
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
  public SubjectCategorySlaveJournalSubjectCategoryPublishDatePanel() {
    super(new SubjectCategorySlaveJournalSubjectCategoryPublishDateTableModel());
    
    CurrentProject.getInstance().getKbObserver().addJournalSubjectCategoryPublishDate(this);
    CurrentProject.getInstance().getKbObserver().addJournalObserver(new JournalWrapper());
    CurrentProject.getInstance().getKbObserver().addPublishDateObserver(new PublishDateWrapper());
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   */
  public void setMasterItem(SubjectCategory subjectCategory) {

    this.masterItem = subjectCategory;

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
      refreshData(CurrentProject.getInstance().getFactoryDAO().getSubjectCategoryDAO().getJournals(this.masterItem.getSubjectCategoryID()));
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
  private class JournalWrapper implements EntityObserver<Journal> {

    /**
     *
     * @param items
     * @throws KnowledgeBaseException
     */
    public void entityAdded(ArrayList<Journal> items) throws KnowledgeBaseException {
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
    public void entityRemoved(ArrayList<Journal> items) throws KnowledgeBaseException {
      // Do not do nothing
    }

    /**
     *
     * @param items
     * @throws KnowledgeBaseException
     */
    public void entityUpdated(ArrayList<Journal> items) throws KnowledgeBaseException {
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
