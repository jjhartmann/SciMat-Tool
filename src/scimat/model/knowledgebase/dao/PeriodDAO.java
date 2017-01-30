/*
 * PeriodDAO.java
 *
 * Created on 21-oct-2010, 17:49:30
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddPeriodEvent;
import scimat.knowledgebaseevents.event.relation.PeriodRelationPublishDateEvent;
import scimat.knowledgebaseevents.event.remove.RemovePeriodEvent;
import scimat.knowledgebaseevents.event.update.UpdatePeriodEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class PeriodDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO Period(name) VALUES(?);
   * </pre>
   */
  private final static String __INSERT_PERIOD = "INSERT INTO Period(name, position) VALUES(?,?);";

  /**
   * <pre>
   * INSERT INTO Period(idPeriod,name) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_PERIOD_WITH_ID = "INSERT INTO Period(idPeriod,name, position) VALUES(?,?, ?);";

  /**
   * <pre>
   * DELETE Period
   * WHERE idPeriod = ?;
   * </pre>
   */
  private final static String __REMOVE_PERIOD = "DELETE FROM Period "
                                              + "WHERE idPeriod = ?;";

  /**
   * <pre>
   * UPDATE Period 
   * SET name = ?
   * WHERE idPeriod = ?;
   * </pre>
   */
  private final static String __UPDATE_NAME = "UPDATE Period "
                                            + "SET name = ? "
                                            + "WHERE idPeriod = ?;";

  /**
   * <pre>
   * UPDATE Period
   * SET position = ?
   * WHERE idPeriod = ?;
   * </pre>
   */
  private final static String __UPDATE_POSITION = "UPDATE Period "
                                                + "SET position = ? "
                                                + "WHERE idPeriod = ?;";

  /**
   * <pre>
   * SELECT pu.idPublishDate, pu.year, pu.date
   * FROM PublishDate_Period pdp, PublishDate pu
   * WHERE pdp.Period_idPeriod = ? AND
   *       pdp.PublishDate_idPublishDate = pu.idPublishDate;
   * </pre>
   */
  private final static String __SELECT_PUBLISHDATES = "SELECT pu.* "
                                                    + "FROM PublishDate_Period pdp, PublishDate pu "
                                                    + "WHERE pdp.Period_idPeriod = ? AND "
                                                    + "      pdp.PublishDate_idPublishDate = pu.idPublishDate;";
  
  private final static String __SELECT_PERIOD_BY_ID = "SELECT * FROM Period WHERE idPeriod = ?;";
  private final static String __SELECT_PERIOD_BY_NAME = "SELECT * FROM Period WHERE name = ?;";
  private final static String __SELECT_PERIODS = "SELECT * FROM Period;";
  private final static String __SELECT_PERIODS_ORDERED_BY_POSITION = "SELECT * FROM Period ORDER BY position ASC;";
  private final static String __CHECK_PERIOD_BY_NAME = "SELECT idPeriod FROM Period WHERE name = ?;";
  private final static String __CHECK_PERIOD_BY_ID = "SELECT idPeriod FROM Period WHERE idPeriod = ?;";
  private final static String __SELECT_MAX_POSITION = "SELECT MAX(position) AS maxPosition FROM Period;";
  
  private PreparedStatement statCheckPeriodById;
  private PreparedStatement statCheckPeriodByName;
  private PreparedStatement statAddPeriod;
  private PreparedStatement statAddPeriodWithId;
  private PreparedStatement statRemovePeriod;
  private PreparedStatement statSelectMaxPosition;
  private PreparedStatement statSelectPeriods;
  private PreparedStatement statSelectPeriodsOrderedByPosition;
  private PreparedStatement statSelectPeriodById;
  private PreparedStatement statSelectPeriodByName;
  private PreparedStatement statSelectPublishDates;
  private PreparedStatement statUpdateName;
  private PreparedStatement statUpdatePosition;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param idPeriod
   * @param name
   * @param kbm
   */
  public PeriodDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    this.kbm = kbm;
    
    try {
    
      this.statCheckPeriodById = this.kbm.getConnection().prepareStatement(__CHECK_PERIOD_BY_ID);
      this.statCheckPeriodByName = this.kbm.getConnection().prepareStatement(__CHECK_PERIOD_BY_NAME);
      this.statAddPeriod = this.kbm.getConnection().prepareStatement(__INSERT_PERIOD, Statement.RETURN_GENERATED_KEYS);
      this.statAddPeriodWithId = this.kbm.getConnection().prepareStatement(__INSERT_PERIOD_WITH_ID);
      this.statRemovePeriod = this.kbm.getConnection().prepareStatement(__REMOVE_PERIOD);
      this.statSelectMaxPosition = this.kbm.getConnection().prepareStatement(__SELECT_MAX_POSITION);
      this.statSelectPeriods = this.kbm.getConnection().prepareStatement(__SELECT_PERIODS);
      this.statSelectPeriodsOrderedByPosition = this.kbm.getConnection().prepareStatement(__SELECT_PERIODS_ORDERED_BY_POSITION);
      this.statSelectPeriodById = this.kbm.getConnection().prepareStatement(__SELECT_PERIOD_BY_ID);
      this.statSelectPeriodByName = this.kbm.getConnection().prepareStatement(__SELECT_PERIOD_BY_NAME);
      this.statSelectPublishDates = this.kbm.getConnection().prepareStatement(__SELECT_PUBLISHDATES);
      this.statUpdateName = this.kbm.getConnection().prepareStatement(__UPDATE_NAME);
      this.statUpdatePosition = this.kbm.getConnection().prepareStatement(__UPDATE_POSITION);
      
    } catch (SQLException e) {
    
      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param name
   * @return
   * @throws KnowledgeBaseException
   */
  public Integer addPeriod(String name, int position, boolean notifyObservers) throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddPeriod.clearParameters();

      this.statAddPeriod.setString(1, name);
      this.statAddPeriod.setInt(2, position);

      if (this.statAddPeriod.executeUpdate() == 1 ) {

        id = this.statAddPeriod.getGeneratedKeys().getInt(1);
        this.statAddPeriod.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddPeriodEvent(getPeriod(id)));
    }

    return id;
  }

  /**
   * 
   * @param periodID
   * @param name
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addPeriod(Integer periodID, String name, int position, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statAddPeriodWithId.clearParameters();

      this.statAddPeriodWithId.setInt(1, periodID);
      this.statAddPeriodWithId.setString(2, name);
      this.statAddPeriodWithId.setInt(3, position);

      result = this.statAddPeriodWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddPeriodEvent(getPeriod(periodID)));
    }

    return result;
  }

  /**
   * 
   * @param period
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addPeriod(Period period, boolean notifyObservers) throws KnowledgeBaseException {

    return addPeriod(period.getPeriodID(), 
            period.getName(), 
            period.getPosition(),
            notifyObservers);
  }

  /**
   *
   * @param periodID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removePeriod(Integer periodID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    Period period = null;
    
    // Save the information before remove
    if (notifyObservers) {
      
      period = getPeriod(periodID);
    }

    try {

      this.statRemovePeriod.clearParameters();

      this.statRemovePeriod.setInt(1, periodID);

      result = this.statRemovePeriod.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemovePeriodEvent(period));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new PeriodRelationPublishDateEvent());
    }

    return result;
  }

  /**
   *
   * @param periodID the period's ID
   *
   * @return a <ocde>Period</code> or null if there is not any period with
   *         this ID
   *
   * @throws KnowledgeBaseException
   */
  public Period getPeriod(Integer periodID) throws KnowledgeBaseException {

    ResultSet rs;
    Period period = null;

    try {

      this.statSelectPeriodById.clearParameters();

      this.statSelectPeriodById.setInt(1, periodID);

      rs = this.statSelectPeriodById.executeQuery();

      if (rs.next()) {

        period = UtilsDAO.getInstance().getPeriod(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return period;
  }

  /**
   *
   * @param name the period's name
   *
   * @return a <ocde>Period</code> or null if there is not any period with
   *         this name
   *
   * @throws KnowledgeBaseException
   */
  public Period getPeriod(String name) throws KnowledgeBaseException {

    ResultSet rs;
    Period period = null;

    try {

      this.statSelectPeriodByName.clearParameters();

      this.statSelectPeriodByName.setString(1, name);

      rs = this.statSelectPeriodByName.executeQuery();

      if (rs.next()) {

        period = UtilsDAO.getInstance().getPeriod(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return period;
  }

  /**
   *
   * @return a <ocde>Period</code> or null if there is not any period with
   *         this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Period> getPeriods() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Period> periodList = new ArrayList<Period>();

    try {

      this.statSelectPeriods.clearParameters();

      rs = this.statSelectPeriods.executeQuery();

      while (rs.next()) {

        periodList.add(UtilsDAO.getInstance().getPeriod(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return periodList;
  }
  
  /**
   *
   * @return a <ocde>Period</code> or null if there is not any period with
   *         this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Period> getPeriodsOrderedByPosition() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Period> periodList = new ArrayList<Period>();

    try {

      this.statSelectPeriodsOrderedByPosition.clearParameters();

      rs = this.statSelectPeriodsOrderedByPosition.executeQuery();

      while (rs.next()) {

        periodList.add(UtilsDAO.getInstance().getPeriod(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return periodList;
  }

  /**
   * 
   * @param periodID
   * @param name
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setName(Integer periodID, String name, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statUpdateName.clearParameters();

      this.statUpdateName.setString(1, name);
      this.statUpdateName.setInt(2, periodID);

      result = this.statUpdateName.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePeriodEvent(getPeriod(periodID)));
    }
    
    return result;
  }

  /**
   *
   * @param periodID
   * @param name
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setPosition(Integer periodID, int position, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statUpdatePosition.clearParameters();

      this.statUpdatePosition.setInt(1, position);
      this.statUpdatePosition.setInt(2, periodID);

      result = this.statUpdatePosition.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePeriodEvent(getPeriod(periodID)));
    }
    
    return result;
  }

  /**
   *
   * @return an array with the publish dates associated with this period
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<PublishDate> getPublishDates(Integer periodID) throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<PublishDate> publishDateList = new ArrayList<PublishDate>();

    try {

      this.statSelectPublishDates.clearParameters();

      this.statSelectPublishDates.setInt(1, periodID);

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
   * <p>Check if there is an <code>Period</code> with this name.</p>
   *
   * @param name a string with the period's name
   *
   * @return true if there is an <code>Period</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkPeriod(String name) throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;
    
    try {

      this.statCheckPeriodByName.clearParameters();

      this.statCheckPeriodByName.setString(1, name);
      
      rs = this.statCheckPeriodByName.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>Period</code> with this ID.</p>
   *
   * @param idPeriod the period's ID
   *
   * @return true if there is an <code>Period</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkPeriod(Integer idPeriod) throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckPeriodById.clearParameters();

      this.statCheckPeriodById.setInt(1, idPeriod);

      rs = this.statCheckPeriodById.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * Retrieve the max position of the period.
   * 
   * @return
   * @throws KnowledgeBaseException 
   */
  public int getMaxPosition() throws KnowledgeBaseException {
  
    ResultSet rs;
    int position = 0;
    
    try {
      
      this.statSelectMaxPosition.clearParameters();
      
      rs = this.statSelectMaxPosition.executeQuery();
      
      if (rs.next()) {
      
        position = rs.getInt("maxPosition");
      }
      
      rs.close();
      
    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    return position;
  }
  
  /**
   * 
   * @param periodsToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Period> refreshPeriods(ArrayList<Period> periodsToRefresh) throws KnowledgeBaseException {
  
    int i;
    String query;
    ResultSet rs;
    ArrayList<Period> periods = new ArrayList<Period>();
    
    i = 0;
    
    if (!periodsToRefresh.isEmpty()) {

      query = "SELECT * FROM Period WHERE idPeriod IN (" + periodsToRefresh.get(i).getPeriodID();
      
      for (i = 1; i < periodsToRefresh.size(); i++) {
        
        query += ", " + periodsToRefresh.get(i).getPeriodID();
      }
      
      query += ");";
      
      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          periods.add(UtilsDAO.getInstance().getPeriod(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }
    
    return periods;
  }
  
  /**
   * 
   * @param periodToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public Period refreshPeriod(Period periodToRefresh) throws KnowledgeBaseException {
  
    return getPeriod(periodToRefresh.getPeriodID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
