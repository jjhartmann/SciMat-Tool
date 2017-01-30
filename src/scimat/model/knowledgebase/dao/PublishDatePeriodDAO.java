/*
 * PublishDatePeriodDAO.java
 *
 * Created on 02-mar-2011, 18:16:15
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.relation.PeriodRelationPublishDateEvent;
import scimat.knowledgebaseevents.event.update.UpdatePeriodEvent;
import scimat.knowledgebaseevents.event.update.UpdatePublishDateEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class PublishDatePeriodDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO PublishDate_Period(PublishDate_idPublishDate,Period_idPeriod) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_PUBLISHDATE_PERIOD = "INSERT INTO PublishDate_Period(PublishDate_idPublishDate,Period_idPeriod) VALUES(?,?);";

  /**
   * <pre>
   * DELETE PublishDate_Period
   * WHERE PublishDate_idPublishDate = ? AND
   *       Period_idPeriod = ?;
   * </pre>
   */
  private final static String __DELETE_PUBLISHDATE_PERIOD = "DELETE FROM PublishDate_Period "
                                                           + "WHERE PublishDate_idPublishDate = ? AND Period_idPeriod = ?;";
  
  private final static String __CHECK_PUBLISHDATE_PERIOD = "SELECT PublishDate_idPublishDate "
          + "FROM PublishDate_Period WHERE "
          + "PublishDate_idPublishDate = ? AND Period_idPeriod = ?;";
  
  private PreparedStatement statAddPublishDatePeriod;
  private PreparedStatement statRemovePublishDatePeriod;
  private PreparedStatement statCheckPublishDatePeriod;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param kbm
   */
  public PublishDatePeriodDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    this.kbm = kbm;
    
    try {
    
      this.statAddPublishDatePeriod = this.kbm.getConnection().prepareStatement(__INSERT_PUBLISHDATE_PERIOD);
      this.statRemovePublishDatePeriod = this.kbm.getConnection().prepareStatement(__DELETE_PUBLISHDATE_PERIOD);
      this.statCheckPublishDatePeriod = this.kbm.getConnection().prepareStatement(__CHECK_PUBLISHDATE_PERIOD);
      
    } catch (SQLException e) {
      
      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param periodID
   * @param publishDateID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addPublishDatePeriod(Integer periodID, Integer publishDateID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statAddPublishDatePeriod.clearParameters();

      this.statAddPublishDatePeriod.setInt(1, publishDateID);
      this.statAddPublishDatePeriod.setInt(2, periodID);

      result = this.statAddPublishDatePeriod.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePublishDateEvent(CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO().getPublishDate(publishDateID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePeriodEvent(CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().getPeriod(periodID)));
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new PeriodRelationPublishDateEvent());
    }

    return result;
  }

  /**
   *
   * @param periodID
   * @param publishDateID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removePublishDatePeriod(Integer periodID, Integer publishDateID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statRemovePublishDatePeriod.clearParameters();

      this.statRemovePublishDatePeriod.setInt(1, publishDateID);
      this.statRemovePublishDatePeriod.setInt(2, periodID);

      result = this.statRemovePublishDatePeriod.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePublishDateEvent(CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO().getPublishDate(publishDateID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePeriodEvent(CurrentProject.getInstance().getFactoryDAO().getPeriodDAO().getPeriod(periodID)));
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new PeriodRelationPublishDateEvent());
    }

    return result;
  }

  /**
   * <p>Check if the <code>PublishDate</code> and <Code>Period<Code>
   * are associated.</p>
   *
   * @param idPublishDate the journal's ID
   * @param idPeriod the subject category's ID
   *
   * @return true if there is an association between both items.
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkPublishDatePeriod(Integer idPublishDate, Integer idPeriod)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckPublishDatePeriod.clearParameters();

      this.statCheckPublishDatePeriod.setInt(1, idPublishDate);
      this.statCheckPublishDatePeriod.setInt(2, idPeriod);

      rs = this.statCheckPublishDatePeriod.executeQuery();
      
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
