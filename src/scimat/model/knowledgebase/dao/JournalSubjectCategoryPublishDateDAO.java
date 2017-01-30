/*
 * JournalSubjectCategoryPublishDateDAO.java
 *
 * Created on 02-mar-2011, 17:54:26
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.relation.JournalRelationSubjectCategoryRelationPublishDateEvent;
import scimat.knowledgebaseevents.event.update.UpdateSubjectCategoryEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class JournalSubjectCategoryPublishDateDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO Journal_SubjectCategory_PublishDate(Journal_idJournal,SubjectCategory_idSubjectCategory,PublishDate_idPublishDate) VALUES(?,?,?);
   * </pre>
   */
  private final static String __INSERT_JOURNAL_SUBJECTCATEGORY_PUBLISHDATE = "INSERT INTO Journal_SubjectCategory_PublishDate(Journal_idJournal,SubjectCategory_idSubjectCategory,PublishDate_idPublishDate) VALUES(?,?,?);";

  /**
   * <pre>
   * DELETE Journal_SubjectCategory_PublishDate "
   * WHERE Journal_idJournal = ? AND
   *       SubjectCategory_idSubjectCategory = ? AND
   *       PublishDate_idPublishDate = ?;
   * </pre>
   */
  private final static String __REMOVE_JOURNAL_SUBJECTCATEGORY_PUBLISHDATE = "DELETE FROM Journal_SubjectCategory_PublishDate "
                                                                           + "WHERE Journal_idJournal = ? AND "
                                                                           + "      SubjectCategory_idSubjectCategory = ? AND "
                                                                           + "      PublishDate_idPublishDate = ?;";
  
  private final static String __CHECK_JOURNAL_SUBJECTCATEGORY_PUBLISHDATE = "SELECT Journal_idJournal "
          + "FROM Journal_SubjectCategory_PublishDate WHERE "
          + "Journal_idJournal = ? AND SubjectCategory_idSubjectCategory = ? "
          + "AND PublishDate_idPublishDate = ?;";
  
  private PreparedStatement statCheckJournalSubjectCategoryPublishDate;
  private PreparedStatement statAddJournalSubjectCategoryPublishDate;
  private PreparedStatement statRemoveJournalSubjectCategoryPublishDate;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param kbm
   */
  public JournalSubjectCategoryPublishDateDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    this.kbm = kbm;
    
    try {
      
      this.statAddJournalSubjectCategoryPublishDate = this.kbm.getConnection().prepareStatement(__INSERT_JOURNAL_SUBJECTCATEGORY_PUBLISHDATE);
      this.statRemoveJournalSubjectCategoryPublishDate = this.kbm.getConnection().prepareStatement(__REMOVE_JOURNAL_SUBJECTCATEGORY_PUBLISHDATE);
      this.statCheckJournalSubjectCategoryPublishDate = this.kbm.getConnection().prepareStatement(__CHECK_JOURNAL_SUBJECTCATEGORY_PUBLISHDATE);
      
    } catch (SQLException e) {
      
      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param subjectCategoryID
   * @param journalID
   * @param publishDateID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addSubjectCategoryToJournal(Integer subjectCategoryID,
          Integer journalID, Integer publishDateID, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statAddJournalSubjectCategoryPublishDate.clearParameters();

      this.statAddJournalSubjectCategoryPublishDate.setInt(1, journalID);
      this.statAddJournalSubjectCategoryPublishDate.setInt(2, subjectCategoryID);
      this.statAddJournalSubjectCategoryPublishDate.setInt(3, publishDateID);

      result = this.statAddJournalSubjectCategoryPublishDate.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateSubjectCategoryEvent(CurrentProject.getInstance().getFactoryDAO().getSubjectCategoryDAO().getSubjectCategory(subjectCategoryID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new JournalRelationSubjectCategoryRelationPublishDateEvent());
    }
      
    return result;
  }

  /**
   *
   * @param subjectCategoryID
   * @param journalID
   * @param publishDateID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeSubjectCategoryFromJournal(Integer subjectCategoryID,
          Integer journalID, Integer publishDateID, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statRemoveJournalSubjectCategoryPublishDate.clearParameters();

      this.statRemoveJournalSubjectCategoryPublishDate.setInt(1, journalID);
      this.statRemoveJournalSubjectCategoryPublishDate.setInt(2, subjectCategoryID);
      this.statRemoveJournalSubjectCategoryPublishDate.setInt(3, publishDateID);

      result = this.statRemoveJournalSubjectCategoryPublishDate.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateSubjectCategoryEvent(CurrentProject.getInstance().getFactoryDAO().getSubjectCategoryDAO().getSubjectCategory(subjectCategoryID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new JournalRelationSubjectCategoryRelationPublishDateEvent());
    }
      
    return result;
  }

  /**
   * <p>Check if the {@link Journal}, {@link SubjectCategory} and
   * {@link PublishDate} are associated.</p>
   *
   * @param journalID the journal's ID
   * @param subjectCategoryID the subject category's ID
   * @param publishDateID  the publish date's ID
   *
   * @return true if there is an association between both items.
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkJournalSubjectCategoryPublishDate(Integer journalID,
          Integer subjectCategoryID, Integer publishDateID)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckJournalSubjectCategoryPublishDate.clearParameters();

      this.statCheckJournalSubjectCategoryPublishDate.setInt(1, journalID);
      this.statCheckJournalSubjectCategoryPublishDate.setInt(2, subjectCategoryID);
      this.statCheckJournalSubjectCategoryPublishDate.setInt(3, publishDateID);

      rs = this.statCheckJournalSubjectCategoryPublishDate.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
