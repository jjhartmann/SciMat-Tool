/*
 * JournalDAO.java
 *
 * Created on 21-oct-2010, 17:49:15
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddJournalEvent;
import scimat.knowledgebaseevents.event.relation.JournalRelationDocumentEvent;
import scimat.knowledgebaseevents.event.remove.RemoveJournalEvent;
import scimat.knowledgebaseevents.event.update.UpdateDocumentEvent;
import scimat.knowledgebaseevents.event.update.UpdateJournalEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.entity.JournalSubjectCategoryPublishDate;
import scimat.model.knowledgebase.entity.SubjectCategory;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class JournalDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO Journal(source,conferenceInformation) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_JOURNAL = "INSERT INTO Journal(source,conferenceInformation) VALUES(?,?);";

  /**
   * <pre>
   * INSERT INTO Journal(idJournal,source,conferenceInformation) VALUES(?,?,?);
   * </pre>
   */
  private final static String __INSERT_JOURNAL_WITH_ID = "INSERT INTO Journal(idJournal,source,conferenceInformation) VALUES(?,?,?);";

  /**
   * <pre>
   * DELETE Journal
   * WHERE idJournal = ?;
   * </pre>
   */
  private final static String __REMOVE_JOURNAL = "DELETE FROM Journal "
                                               + "WHERE idJournal = ?;";

  /**
   * <pre>
   * UPDATE Journal SET source = ? WHERE idJournal = ?;
   * </pre>
   */
  private final static String __UPDATE_SOURCE = "UPDATE Journal "
                                              + "SET source = ? "
                                              + "WHERE idJournal = ?;";

  /**
   * <pre>
   * UPDATE Journal SET conferenceInformation = ? WHERE idJournal = ?;
   * </pre>
   */
  private final static String __UPDATE_CONFERENCE_INFORMATION = "UPDATE Journal "
                                                              + "SET conferenceInformation = ? "
                                                              + "WHERE idJournal = ?;";

  /**
   * <pre>
   * UPDATE Journal
   * SET source = ?,
   *     conferenceInformation = ?
   * WHERE idJournal = ?;
   * </pre>
   */
  private final static String __UPDATE_JOURNAL = "UPDATE Journal "
                                               + "SET source = ?, "
                                               + "    conferenceInformation = ? "
                                               + "WHERE idJournal = ?;";

  /**
   * <pre>
   * </pre>
   */
  private final static String __SELECT_DOCUMENT = "SELECT d.* "
                                                + "FROM Document d, Journal j "
                                                + "WHERE j.idJournal = ? AND "
                                                + "      d.Journal_idJournal = j.idJournal;";

  /**
   * <pre>
   * SELECT j.idJournal, j.source, j.conferenceInformation,
   *        s.idSubjectCategory, s.subjectCategoryName
   *        p.idPublishDate, p.year, p.date
   * FROM PublishDate p, Journal j, SubjectCategory s, Journal_SubjectCategory_PublishDate jsp
   * WHERE j.idJournal = ? AND
   *       j.idJournal = jsp.Journal_idJournal AND
   *       jsp.SubjectCategory_idSubjectCategory = s.idSubjectCategory AND
   *       jsp.PublishDate_idPublishDate = p.idPublishDate;
   * </pre>
   */
  private final static String __SELECT_JOURNAL_SUBJECTCATEGORIES_PUBLISHDATE = "SELECT j.*, s.*, p.* "
                                                                             + "FROM PublishDate p, Journal j, SubjectCategory s, Journal_SubjectCategory_PublishDate jsp "
                                                                             + "WHERE j.idJournal = ? AND "
                                                                             + "      j.idJournal = jsp.Journal_idJournal AND "
                                                                             + "      jsp.SubjectCategory_idSubjectCategory = s.idSubjectCategory AND "
                                                                             + "      jsp.PublishDate_idPublishDate = p.idPublishDate;";
  
  private final static String __SELECT_SUBJECTCATEGORIES = "SELECT s.* "
                                                         + "FROM SubjectCategory s, Journal_SubjectCategory_PublishDate jsp "
                                                         + "WHERE jsp.Journal_idJournal = ? AND "
                                                         + "      jsp.SubjectCategory_idSubjectCategory = s.idSubjectCategory;";
  
  private final static String __SELECT_JOURNAL_BY_ID = "SELECT * FROM Journal WHERE idJournal = ?;";
  private final static String __SELECT_JOURNAL_BY_SOURCE = "SELECT * FROM Journal WHERE source = ?;";
  private final static String __SELECT_JOURNALS = "SELECT * FROM Journal;";
  private final static String __CHECK_JOURNAL_BY_SOURCE = "SELECT idJournal FROM Journal WHERE source = ?;";
  private final static String __CHECK_JOURNAL_BY_ID = "SELECT idJournal FROM Journal WHERE idJournal = ?;";
  
  private PreparedStatement statCheckJournalById;
  private PreparedStatement statCheckJournalBySource;
  private PreparedStatement statAddJournal;
  private PreparedStatement statAddJournalWithId;
  private PreparedStatement statRemoveJournal;
  private PreparedStatement statSelectDocuments;
  private PreparedStatement statSelectJournals;
  private PreparedStatement statSelectJournalById;
  private PreparedStatement statSelectJournalBySource;
  private PreparedStatement statSelectJournalSubjectCategoryPublishDate;
  private PreparedStatement statSelectSubjectCategories;
  private PreparedStatement statUpdateConferenceInformation;
  private PreparedStatement statUpdateJournal;
  private PreparedStatement statUpdateSource;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param kbm
   */
  public JournalDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    
    this.kbm = kbm;
    
    try {
    
      this.statCheckJournalById = this.kbm.getConnection().prepareStatement(__CHECK_JOURNAL_BY_ID);
      this.statCheckJournalBySource = this.kbm.getConnection().prepareStatement(__CHECK_JOURNAL_BY_SOURCE);
      this.statAddJournal  = this.kbm.getConnection().prepareStatement(__INSERT_JOURNAL, Statement.RETURN_GENERATED_KEYS);
      this.statAddJournalWithId = this.kbm.getConnection().prepareStatement(__INSERT_JOURNAL_WITH_ID);
      this.statRemoveJournal = this.kbm.getConnection().prepareStatement(__REMOVE_JOURNAL);
      this.statSelectDocuments = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENT);
      this.statSelectJournals = this.kbm.getConnection().prepareStatement(__SELECT_JOURNALS);
      this.statSelectJournalById = this.kbm.getConnection().prepareStatement(__SELECT_JOURNAL_BY_ID);
      this.statSelectJournalBySource = this.kbm.getConnection().prepareStatement(__SELECT_JOURNAL_BY_SOURCE);
      this.statSelectJournalSubjectCategoryPublishDate = this.kbm.getConnection().prepareStatement(__SELECT_JOURNAL_SUBJECTCATEGORIES_PUBLISHDATE);
      this.statSelectSubjectCategories = this.kbm.getConnection().prepareStatement(__SELECT_SUBJECTCATEGORIES);
      this.statUpdateConferenceInformation = this.kbm.getConnection().prepareStatement(__UPDATE_CONFERENCE_INFORMATION);
      this.statUpdateJournal = this.kbm.getConnection().prepareStatement(__UPDATE_JOURNAL);
      this.statUpdateSource = this.kbm.getConnection().prepareStatement(__UPDATE_SOURCE);
      
    } catch (SQLException e) {
    
      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param source
   * @param conferenceInformation
   * @return
   * @throws KnowledgeBaseException
   */
  public Integer addJournal(String source, String conferenceInformation, boolean notifyObservers)
          throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddJournal.clearParameters();

      this.statAddJournal.setString(1, source);
      this.statAddJournal.setString(2, conferenceInformation);

      if (this.statAddJournal.executeUpdate() == 1 ) {

        id = this.statAddJournal.getGeneratedKeys().getInt(1);
        this.statAddJournal.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddJournalEvent(getJournal(id)));
    }

    return id;
  }

  /**
   *
   * @param source
   * @param conferenceInformation
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addJournal(Integer journalID, String source, String conferenceInformation, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statAddJournalWithId.clearParameters();

      this.statAddJournalWithId.setInt(1, journalID);
      this.statAddJournalWithId.setString(2, source);
      this.statAddJournalWithId.setString(3, conferenceInformation);

      result = this.statAddJournalWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddJournalEvent(getJournal(journalID)));
    }

    return result;
  }

  /**
   * 
   * @param journal
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addJournal(Journal journal, boolean notifyObservers) throws KnowledgeBaseException {

    return addJournal(journal.getJournalID(), 
                      journal.getSource(),
                      journal.getConferenceInformation(),
                      notifyObservers);
  }

  /**
   *
   * @param journalID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeJournal(Integer journalID, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;
    Journal journal = null;
    ArrayList<Document> documents = null;
    
    // Save the information before remove
    if (notifyObservers) { 
    
      journal = getJournal(journalID);
      documents = getDocuments(journalID);
    }

    try {

      this.statRemoveJournal.clearParameters();

      this.statRemoveJournal.setInt(1, journalID);

      result = this.statRemoveJournal.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveJournalEvent(journal));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().refreshDocuments(documents)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new JournalRelationDocumentEvent());
    }
      
    return result;
  }

  /**
   *
   * @param idJournal the journal's ID
   *
   * @return a <ocde>Journal</code> or null if there is not any journal with
   *         this ID
   *
   * @throws KnowledgeBaseException
   */
  public Journal getJournal(Integer idJournal) throws KnowledgeBaseException {

    ResultSet rs;
    Journal journal = null;

    try {

      this.statSelectJournalById.clearParameters();

      this.statSelectJournalById.setInt(1, idJournal);

      rs = this.statSelectJournalById.executeQuery();

      if (rs.next()) {

        journal = UtilsDAO.getInstance().getJournal(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return journal;
  }

  /**
   *
   * @param source the journal's suorce
   *
   * @return a <ocde>Journal</code> or null if there is not any journal with
   *         this source
   *
   * @throws KnowledgeBaseException
   */
  public Journal getJournal(String source) throws KnowledgeBaseException {

    ResultSet rs;
    Journal journal = null;

    try {

      this.statSelectJournalBySource.clearParameters();

      this.statSelectJournalBySource.setString(1, source);

      rs = this.statSelectJournalBySource.executeQuery();

      if (rs.next()) {

        journal = UtilsDAO.getInstance().getJournal(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return journal;
  }

  /**
   * 
   * @return a <ocde>Journal</code> or null if there is not any journal with
   *         this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Journal> getJournals() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Journal> journalList = new ArrayList<Journal>();

    try {

      this.statSelectJournals.clearParameters();

      rs = this.statSelectJournals.executeQuery();

      while (rs.next()) {

        journalList.add(UtilsDAO.getInstance().getJournal(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return journalList;
  }

  /**
   * 
   * @param journalID
   * @param source
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setSource(Integer journalID, String source, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateSource.clearParameters();

      this.statUpdateSource.setString(1, source);
      this.statUpdateSource.setInt(2, journalID);

      result = this.statUpdateSource.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateJournalEvent(getJournal(journalID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocuments(journalID)));
    }
    
    return result;
  }

  /**
   * 
   * @param journalID
   * @param conferenceInformation
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setConferenceInformation(Integer journalID, String conferenceInformation, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateConferenceInformation.clearParameters();

      this.statUpdateConferenceInformation.setString(1, conferenceInformation);
      this.statUpdateConferenceInformation.setInt(2, journalID);

      result = this.statUpdateConferenceInformation.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateJournalEvent(getJournal(journalID)));
    }
    
    return result;
  }

  /**
   * 
   * @param journalID
   * @param source
   * @param conferenceInformation
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean updateJournal(Integer journalID, String source, String conferenceInformation, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateJournal.clearParameters();

      this.statUpdateJournal.setString(1, source);
      this.statUpdateJournal.setString(2, conferenceInformation);
      this.statUpdateJournal.setInt(3, journalID);

      result = this.statUpdateJournal.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateJournalEvent(getJournal(journalID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocuments(journalID)));
    }
    
    return result;
  }

  /**
   *
   * @return an array with the documents associated with this journal
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Document> getDocuments(Integer journalID) throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Document> documentsList = new ArrayList<Document>();

    try {

      this.statSelectDocuments.clearParameters();

      this.statSelectDocuments.setInt(1, journalID);

      rs = this.statSelectDocuments.executeQuery();

      while (rs.next()) {

        documentsList.add(UtilsDAO.getInstance().getDocument(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return documentsList;
  }

  /**
   *
   * @return an array with the authors associated with this document
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<JournalSubjectCategoryPublishDate> getJournalSubjectCategoryPublishDates(Integer journalID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<JournalSubjectCategoryPublishDate> jspList = new ArrayList<JournalSubjectCategoryPublishDate>();

    try {

      this.statSelectJournalSubjectCategoryPublishDate.clearParameters();

      this.statSelectJournalSubjectCategoryPublishDate.setInt(1, journalID);

      rs = this.statSelectJournalSubjectCategoryPublishDate.executeQuery();

      while (rs.next()) {

        jspList.add(UtilsDAO.getInstance().getJournalSubjectCategoryPublishDate(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return jspList;
  }
  
  /**
   * 
   * @param journalID
   * @return
   * @throws KnowledgeBaseException 
   */
   public ArrayList<SubjectCategory> getSubjectCategories(Integer journalID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<SubjectCategory> subjectCategories = new ArrayList<SubjectCategory>();

    try {

      this.statSelectSubjectCategories.clearParameters();

      this.statSelectSubjectCategories.setInt(1, journalID);

      rs = this.statSelectSubjectCategories.executeQuery();

      while (rs.next()) {

        subjectCategories.add(UtilsDAO.getInstance().getSubjectCategory(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return subjectCategories;
  }

  /**
   * <p>Check if there is a <code>Journal</code> with this source.</p>
   *
   * @param source a string with the journal's source
   *
   * @return true if there is an <code>Journal</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkJournal(String source)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckJournalBySource.clearParameters();

      this.statCheckJournalBySource.setString(1, source);

      rs = this.statCheckJournalBySource.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is a <code>Journal</code> with this ID.</p>
   *
   * @param idJournal the journal's ID
   *
   * @return true if there is an <code>Journal</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkJournal(Integer idJournal)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckJournalById.clearParameters();

      this.statCheckJournalById.setInt(1, idJournal);

      rs = this.statCheckJournalById.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param journalsToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Journal> refreshJournals(ArrayList<Journal> journalsToRefresh) throws KnowledgeBaseException {
  
    int i;
    String query;
    ResultSet rs;
    ArrayList<Journal> journals = new ArrayList<Journal>();
    
    i = 0;
    
    if (!journalsToRefresh.isEmpty()) {

      query = "SELECT * FROM Journal WHERE idJournal IN (" + journalsToRefresh.get(i).getJournalID();
      
      for (i = 1; i < journalsToRefresh.size(); i++) {
        
        query += ", " + journalsToRefresh.get(i).getJournalID();
      }
      
      query += ");";
      
      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          journals.add(UtilsDAO.getInstance().getJournal(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }
    
    return journals;
  }
  
  /**
   * 
   * @param journalToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public Journal refreshJournal(Journal journalToRefresh) throws KnowledgeBaseException {
  
    return getJournal(journalToRefresh.getJournalID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
