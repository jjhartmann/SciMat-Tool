/*
 * PublishDateDAO.java
 *
 * Created on 21-oct-2010, 17:49:37
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddPublishDateEvent;
import scimat.knowledgebaseevents.event.relation.PeriodRelationPublishDateEvent;
import scimat.knowledgebaseevents.event.relation.PublishDateRelationDocumentEvent;
import scimat.knowledgebaseevents.event.remove.RemovePublishDateEvent;
import scimat.knowledgebaseevents.event.update.UpdateDocumentEvent;
import scimat.knowledgebaseevents.event.update.UpdatePeriodEvent;
import scimat.knowledgebaseevents.event.update.UpdatePublishDateEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.JournalSubjectCategoryPublishDate;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class PublishDateDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO PublishDate(year,date) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_PUBLISHDATE = "INSERT INTO PublishDate(year,date) VALUES(?,?);";

  /**
   * <pre>
   * INSERT INTO PublishDate(idPublishDate,year,date) VALUES(?,?,?);
   * </pre>
   */
  private final static String __INSERT_PUBLISHDATE_WITH_ID = "INSERT INTO PublishDate(idPublishDate,year,date) VALUES(?,?,?);";

  /**
   * <pre>
   * DELETE PublishDate
   * WHERE idPublishDate = ?;
   * </pre>
   */
  private final static String __REMOVE_PUBLISHDATE = "DELETE FROM PublishDate "
                                                   + "WHERE idPublishDate = ?;";

  /**
   * <pre>
   * UPDATE PublishDate
   * SET year = ?
   * WHERE idPublishDate = ?;
   * </pre>
   */
  private final static String __UPDATE_YEAR = "UPDATE PublishDate "
                                            + "SET year = ? "
                                            + "WHERE idPublishDate = ?;";

  /**
   * UPDATE PublishDate
   * SET year = ?,
   *     date = ?
   * WHERE idPublishDate = ?;
   */
  private final static String __UPDATE_PUBLISHDATE = "UPDATE PublishDate "
                                                   + "SET year = ?, "
                                                   + "    date = ? "
                                                   + "WHERE idPublishDate = ?;";

  /**
   * <pre>
   * UPDATE PublishDate
   * SET date = ?
   * WHERE idPublishDate = ?;
   * </pre>
   */
  private final static String __UPDATE_DATE = "UPDATE PublishDate "
                                            + "SET date = ? "
                                            + "WHERE idPublishDate = ?;";

  /**
   * <pre>
   * SELECT pe.idPeriod, pe.name "
   * FROM PublishDate_Period pdp, Period pe
   * WHERE pdp.PublishDate_idPublishDate = ? AND
   *       pdp.Period_idPeriod = pe.idPeriod;
   * </pre>
   */
  private final static String __SELECT_PERIODS = "SELECT pe.* "
                                              + "FROM PublishDate_Period pdp, Period pe "
                                              + "WHERE pdp.PublishDate_idPublishDate = ? AND "
                                              + "      pdp.Period_idPeriod = pe.idPeriod;";

  /**
   * <pre>
   * </pre>
   */
  private final static String __SELECT_DOCUMENT = "SELECT d.* "
                                                + "FROM Document d, PublishDate p "
                                                + "WHERE p.idPublishDate = ? AND "
                                                + "      p.idPublishDate = d.PublishDate_idPublishDate;";

  /**
   * <pre>
   * SELECT j.idJournal, j.source, j.conferenceInformation,
   *        s.idSubjectCategory, s.subjectCategoryName
   *        p.idPublishDate, p.year, p.date
   * FROM PublishDate p, Journal j, SubjectCategory s, Journal_SubjectCategory_PublishDate jsp
   * WHERE p.idPublishDate = ? AND
   *       p.idPublishDate = jsp.PublishDate_idPublishDate AND
   *       jsp.SubjectCategory_idSubjectCategory = s.idSubjectCategory AND
   *       jsp.Journal_idJournal = j.idJournal;
   * </pre>
   */
  private final static String __SELECT_JOURNAL_SUBJECTCATEGORIES_PUBLISHDATE = "SELECT j.*, s.*, p.* "
                                                                             + "FROM PublishDate p, Journal j, SubjectCategory s, Journal_SubjectCategory_PublishDate jsp "
                                                                             + "WHERE p.idPublishDate = ? AND "
                                                                             + "      p.idPublishDate = jsp.PublishDate_idPublishDate AND "
                                                                             + "      jsp.SubjectCategory_idSubjectCategory = s.idSubjectCategory AND "
                                                                             + "      jsp.Journal_idJournal = j.idJournal;";

  private final static String __SELECT_PUBLISHDATE_BY_ID = "SELECT * FROM PublishDate WHERE idPublishDate = ?;";
  private final static String __SELECT_PUBLISHDATE_BY_YEAR = "SELECT * FROM PublishDate WHERE year = ? AND date = ?;";
  private final static String __SELECT_PUBLISHDATES = "SELECT * FROM PublishDate;";
  private final static String __CHECK_PUBLISHDATES_BY_YEAR = "SELECT idPublishDate FROM PublishDate WHERE year = ? AND date = ?;";
  private final static String __CHECK_PUBLISHDATES_BY_ID = "SELECT idPublishDate FROM PublishDate WHERE idPublishDate = ?;";
  
  private PreparedStatement statCheckPublishDateByYear;
  private PreparedStatement statCheckPublishDateById;
  private PreparedStatement statAddPublishDate;
  private PreparedStatement statAddPublishDateWithId;
  private PreparedStatement statRemovePublishDate;
  private PreparedStatement statSelectDocuments;
  private PreparedStatement statSelectJournalSubjectCategoryPublishDate;
  private PreparedStatement statSelectPeriods;
  private PreparedStatement statSelectPublishDates;
  private PreparedStatement statSelectPublishDateById;
  private PreparedStatement statSelectPublishDateByYear;
  private PreparedStatement statUpdateDate;
  private PreparedStatement statUpdateYear;
  private PreparedStatement statUpdatePublishDate;
    
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param kbm
   */
  public PublishDateDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException{
    
    this.kbm = kbm;
    
    try {
    
      this.statCheckPublishDateByYear = this.kbm.getConnection().prepareStatement(__CHECK_PUBLISHDATES_BY_YEAR);
      this.statCheckPublishDateById = this.kbm.getConnection().prepareStatement(__CHECK_PUBLISHDATES_BY_ID);
      this.statAddPublishDate = this.kbm.getConnection().prepareStatement(__INSERT_PUBLISHDATE, Statement.RETURN_GENERATED_KEYS);
      this.statAddPublishDateWithId = this.kbm.getConnection().prepareStatement(__INSERT_PUBLISHDATE_WITH_ID);
      this.statRemovePublishDate = this.kbm.getConnection().prepareStatement(__REMOVE_PUBLISHDATE);
      this.statSelectDocuments = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENT);
      this.statSelectJournalSubjectCategoryPublishDate = this.kbm.getConnection().prepareStatement(__SELECT_JOURNAL_SUBJECTCATEGORIES_PUBLISHDATE);
      this.statSelectPeriods = this.kbm.getConnection().prepareStatement(__SELECT_PERIODS);
      this.statSelectPublishDates = this.kbm.getConnection().prepareStatement(__SELECT_PUBLISHDATES);
      this.statSelectPublishDateById = this.kbm.getConnection().prepareStatement(__SELECT_PUBLISHDATE_BY_ID);
      this.statSelectPublishDateByYear = this.kbm.getConnection().prepareStatement(__SELECT_PUBLISHDATE_BY_YEAR);
      this.statUpdateDate = this.kbm.getConnection().prepareStatement(__UPDATE_DATE);
      this.statUpdateYear = this.kbm.getConnection().prepareStatement(__UPDATE_YEAR);
      this.statUpdatePublishDate = this.kbm.getConnection().prepareStatement(__UPDATE_PUBLISHDATE);
      
    } catch (SQLException e) {
    
      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public Integer addPublishDate(String year, String date, boolean notifyObservers)
          throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddPublishDate.clearParameters();

      this.statAddPublishDate.setString(1, year);
      this.statAddPublishDate.setString(2, date);

      if (this.statAddPublishDate.executeUpdate() == 1 ) {

        id = this.statAddPublishDate.getGeneratedKeys().getInt(1);
        statAddPublishDate.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddPublishDateEvent(getPublishDate(id)));
    }

    return id;
  }

  /**
   * 
   * @param publishDateID
   * @param year
   * @param date
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addPublishDate(Integer publishDateID, String year, String date, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statAddPublishDateWithId.clearParameters();

      this.statAddPublishDateWithId.setInt(1, publishDateID);
      this.statAddPublishDateWithId.setString(2, year);
      this.statAddPublishDateWithId.setString(3, date);

      result = this.statAddPublishDateWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddPublishDateEvent(getPublishDate(publishDateID)));
    }

    return result;
  }

  /**
   * 
   * @param publishDate
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addPublishDate(PublishDate publishDate, boolean notifyObservers) throws KnowledgeBaseException {

    return addPublishDate(publishDate.getPublishDateID(), 
            publishDate.getYear(), 
            publishDate.getDate(),
            notifyObservers);
  }

  /**
   *
   * @param publishDateID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removePublishDate(Integer publishDateID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    PublishDate publishDate = null;
    ArrayList<Document> documents = null;
    ArrayList<Period> periods = null;
    
    // Save the information before remove
    if (notifyObservers) {
      
      publishDate = getPublishDate(publishDateID);
      documents = getDocuments(publishDateID);
      periods = getPeriods(publishDateID);
    }
    
    try {

      this.statRemovePublishDate.clearParameters();

      this.statRemovePublishDate.setInt(1, publishDateID);

      result = this.statRemovePublishDate.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemovePublishDateEvent(publishDate));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().refreshDocuments(documents)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePeriodEvent(CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().refreshPeriods(periods)));
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new PublishDateRelationDocumentEvent());
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new PeriodRelationPublishDateEvent());
    }
      
    return result;
  }

  /**
   *
   * @param idPublishDate the publish date's ID
   *
   * @return a <ocde>PublishDate</code> or null if there is not any publish date
   *         with this ID
   *
   * @throws KnowledgeBaseException
   */
  public PublishDate getPublishDate(Integer idPublishDate)
          throws KnowledgeBaseException {

    ResultSet rs;
    PublishDate publishDate = null;

    try {

      this.statSelectPublishDateById.clearParameters();

      this.statSelectPublishDateById.setInt(1, idPublishDate);

      rs = this.statSelectPublishDateById.executeQuery();

      if (rs.next()) {

        publishDate = UtilsDAO.getInstance().getPublishDate(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return publishDate;
  }

  /**
   *
   * @param year the publish date's year
   *
   * @return a <ocde>PublishDate</code> or null if there is not any publish date
   *         with this year
   *
   * @throws KnowledgeBaseException
   */
  public PublishDate getPublishDate(String year, String date)
          throws KnowledgeBaseException {

    ResultSet rs;
    PublishDate publishDate = null;

    try {

      this.statSelectPublishDateByYear.clearParameters();

      this.statSelectPublishDateByYear.setString(1, year);
      this.statSelectPublishDateByYear.setString(2, date);

      rs = this.statSelectPublishDateByYear.executeQuery();

      if (rs.next()) {

        publishDate = UtilsDAO.getInstance().getPublishDate(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return publishDate;
  }

  /**
   *
   * @return a <ocde>PublishDate</code> or null if there is not any publish date
   *         with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<PublishDate> getPublishDates() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<PublishDate> publishDateList = new ArrayList<PublishDate>();

    try {

      this.statSelectPublishDates.clearParameters();

      rs = this.statSelectPublishDates.executeQuery();

      while (rs.next()) {

        publishDateList.add(UtilsDAO.getInstance().getPublishDate(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return publishDateList;
  }

  /**
   * @param year the year to set
   */
  public boolean setYear(Integer publishDateID, String year, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statUpdateYear.clearParameters();

      this.statUpdateYear.setString(1, year);
      this.statUpdateYear.setInt(2, publishDateID);

      result = this.statUpdateYear.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePublishDateEvent(getPublishDate(publishDateID)));
    }

    return result;
  }

  /**
   * 
   * @param publishDateID
   * @param year
   * @param date
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean updatePublishDate(Integer publishDateID, String year, String date, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statUpdatePublishDate.clearParameters();

      this.statUpdatePublishDate.setString(1, year);
      this.statUpdatePublishDate.setString(2, date);
      this.statUpdatePublishDate.setInt(3, publishDateID);

      result = this.statUpdatePublishDate.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePublishDateEvent(getPublishDate(publishDateID)));
    }

    return result;
  }

  /**
   * @param date the date to set
   */
  public boolean setDate(Integer publishDateID, String date, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statUpdateDate.clearParameters();

      this.statUpdateDate.setString(1, date);
      this.statUpdateDate.setInt(2, publishDateID);

      result = this.statUpdateDate.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePublishDateEvent(getPublishDate(publishDateID)));
    }

    return result;
  }

  /**
   *
   * @return an array with the periods associated with this publish date
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Period> getPeriods(Integer publishDateID) throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Period> periodsList = new ArrayList<Period>();

    try {

      this.statSelectPeriods.clearParameters();

      this.statSelectPeriods.setInt(1, publishDateID);

      rs = this.statSelectPeriods.executeQuery();

      while (rs.next()) {

        periodsList.add(UtilsDAO.getInstance().getPeriod(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return periodsList;
  }

  /**
   *
   * @return an array with the documents associated with this publish date
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Document> getDocuments(Integer publishDateID) throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Document> authorsList = new ArrayList<Document>();

    try {

      this.statSelectDocuments.clearParameters();

      this.statSelectDocuments.setInt(1, publishDateID);

      rs = this.statSelectDocuments.executeQuery();

      while (rs.next()) {

        authorsList.add(UtilsDAO.getInstance().getDocument(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorsList;
  }

  /**
   *
   * @return an array with the authors associated with this document
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<JournalSubjectCategoryPublishDate> getSubjectCategories(Integer publishDateID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<JournalSubjectCategoryPublishDate> jspList = new ArrayList<JournalSubjectCategoryPublishDate>();

    try {

      this.statSelectJournalSubjectCategoryPublishDate.clearParameters();

      this.statSelectJournalSubjectCategoryPublishDate.setInt(1, publishDateID);

      rs = this.statSelectJournalSubjectCategoryPublishDate.executeQuery();

      while (rs.next()) {

        jspList.add(UtilsDAO.getInstance().getJournalSubjectCategoryPublishDate(rs));
      }

      rs.close();

    } catch (SQLException e) {

      e.printStackTrace(System.err);

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return jspList;
  }

  /**
   * <p>Check if there is an <code>PublishDate</code> with this year.</p>
   *
   * @param year a string with the publish date's year
   * @param date a string with the date.
   *
   * @return true if there is an <code>PublishDate</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkPublishDate(String year, String date)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckPublishDateByYear.clearParameters();

      this.statCheckPublishDateByYear.setString(1, year);
      this.statCheckPublishDateByYear.setString(2, date);

      rs = this.statCheckPublishDateByYear.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>PublishDate</code> with this ID.</p>
   *
   * @param idPublishDate the publish date's ID
   *
   * @return true if there is an <code>PublishDate</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkPublishDate(Integer idPublishDate)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckPublishDateById.clearParameters();

      this.statCheckPublishDateById.setInt(1, idPublishDate);

      rs = this.statCheckPublishDateById.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param publishDatesToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<PublishDate> refreshPublishDates(ArrayList<PublishDate> publishDatesToRefresh) throws KnowledgeBaseException {
  
    int i;
    String query;
    ResultSet rs;
    ArrayList<PublishDate> publishDates = new ArrayList<PublishDate>();
    
    i = 0;
    
    if (!publishDatesToRefresh.isEmpty()) {

      query = "SELECT * FROM PublishDate WHERE idPublishDate IN (" + publishDatesToRefresh.get(i).getPublishDateID();
      
      for (i = 1; i < publishDatesToRefresh.size(); i++) {
        
        query += ", " + publishDatesToRefresh.get(i).getPublishDateID();
      }
      
      query += ");";
      
      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          publishDates.add(UtilsDAO.getInstance().getPublishDate(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }
    
    return publishDates;
  }
  
  /**
   * 
   * @param publishDateToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public PublishDate refreshPublishDate(PublishDate publishDateToRefresh) throws KnowledgeBaseException {
  
    return getPublishDate(publishDateToRefresh.getPublishDateID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
