/*
 * ReferenceSourceGroupDAO.java
 *
 * Created on 28-oct-2010, 20:10:57
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddReferenceSourceGroupEvent;
import scimat.knowledgebaseevents.event.add.AddReferenceSourceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.relation.ReferenceSourceGroupRelationReferenceSourceEvent;
import scimat.knowledgebaseevents.event.remove.RemoveReferenceSourceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceSourceGroupEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class ReferenceSourceGroupDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO ReferenceSourceGroup(groupName,stopGroup) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_REFERENCESOURCEGROUP = "INSERT INTO ReferenceSourceGroup(groupName,stopGroup) VALUES(?,?);";

  /**
   * <pre>
   * INSERT INTO ReferenceSourceGroup(idReferenceSourceGroup,groupName,stopGroup) VALUES(?,?,?);
   * </pre>
   */
  private final static String __INSERT_REFERENCESOURCEGROUP_WITH_ID = "INSERT INTO ReferenceSourceGroup(idReferenceSourceGroup,groupName,stopGroup) VALUES(?,?,?);";

  /**
   * <pre>
   * DELETE ReferenceSourceGroup
   * WHERE idReferenceSourceGroup = ?;
   * </pre>
   */
  private final static String __REMOVE_REFERENCESOURCEGROUP = "DELETE FROM ReferenceSourceGroup "
                                                            + "WHERE idReferenceSourceGroup = ?;";

  /**
   * <pre>
   * UPDATE ReferenceSourceGroup
   * SET groupName = ?
   * WHERE idReferenceSourceGroup = ?;
   * </pre>
   */
  private final static String __UPDATE_GROUPNAME = "UPDATE ReferenceSourceGroup "
                                                 + "SET groupName = ? "
                                                 + "WHERE idReferenceSourceGroup = ?;";

  /**
   * <pre>
   * UPDATE ReferenceSourceGroup 
   * SET stopGroup = ?
   * WHERE idReferenceSourceGroup = ?;
   * </pre>
   */
  private final static String __UPDATE_STOPGROUP = "UPDATE ReferenceSourceGroup "
                                                 + "SET stopGroup = ? "
                                                 + "WHERE idReferenceSourceGroup = ?;";

  /**
   * <pre>
   * UPDATE ReferenceSourceGroup
   * SET groupName = ?,
   *     stopGroup = ?
   * WHERE idReferenceSourceGroup = ?;
   * </pre>
   */
  private final static String __UPDATE_REFERENCESOURCEGROUP = "UPDATE ReferenceSourceGroup "
                                                            + "SET groupName = ?, "
                                                            + "    stopGroup = ? "
                                                            + "WHERE idReferenceSourceGroup = ?;";

  /**
   * <pre>
   * </pre>
   */
  private final static String __SELECT_REFERENCESOURCES = "SELECT rs.* "
                                                       + "FROM ReferenceSource rs, ReferenceSourceGroup rsg "
                                                       + "WHERE rsg.idReferenceSourceGroup = ? AND "
                                                       + "      rs.ReferenceSourceGroup_idReferenceSourceGroup = rsg.idReferenceSourceGroup;";
  
  private final static String __SELECT_REFERENCESOURCEGROUP_BY_ID = "SELECT * FROM ReferenceSourceGroup WHERE idReferenceSourceGroup = ?;";
  private final static String __SELECT_REFERENCESOURCEGROUP_BY_GROUPNAME = "SELECT * FROM ReferenceSourceGroup WHERE groupName = ?;";
  private final static String __SELECT_REFERENCESOURCEGROUP_BY_STOPGROUP = "SELECT idReferenceSourceGroup, groupName, stopGroup FROM ReferenceSourceGroup WHERE stopGroup = ?;";
  private final static String __SELECT_REFERENCESOURCEGROUPS = "SELECT * FROM ReferenceSourceGroup;";
  private final static String __CHECK_REFERENCESOURCEGROUP_BY_GROUPNAME = "SELECT idReferenceSourceGroup FROM ReferenceSourceGroup WHERE groupName = ?;";
  private final static String __CHECK_REFERENCESOURCEGROUP_BY_ID = "SELECT idReferenceSourceGroup FROM ReferenceSourceGroup WHERE idReferenceSourceGroup = ?;";
  
  private PreparedStatement statCheckReferenceSourceGroupByGroupName;
  private PreparedStatement statCheckReferenceSourceGroupById;
  private PreparedStatement statAddReferenceSourceGroup;
  private PreparedStatement statAddReferenceSourceGroupWithId;
  private PreparedStatement statRemoveReferenceSourceGroup;
  private PreparedStatement statSelectReferenceSources;
  private PreparedStatement statSelectReferenceSourceGroups;
  private PreparedStatement statSelectReferenceSourceGroupByGroupName;
  private PreparedStatement statSelectReferenceSourceGroupById;
  private PreparedStatement statSelectReferenceSourceGroupByStopGroup;
  private PreparedStatement statUpdateGroupName;
  private PreparedStatement statUpdateStopGroup;
  private PreparedStatement statUpdateReferenceSourceGroup;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param kbm
   */
  public ReferenceSourceGroupDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    
    this.kbm = kbm;
    
    try {
    
      this.statCheckReferenceSourceGroupByGroupName = this.kbm.getConnection().prepareStatement(__CHECK_REFERENCESOURCEGROUP_BY_GROUPNAME);
      this.statCheckReferenceSourceGroupById = this.kbm.getConnection().prepareStatement(__CHECK_REFERENCESOURCEGROUP_BY_ID);
      this.statAddReferenceSourceGroup = this.kbm.getConnection().prepareStatement(__INSERT_REFERENCESOURCEGROUP, Statement.RETURN_GENERATED_KEYS);
      this.statAddReferenceSourceGroupWithId = this.kbm.getConnection().prepareStatement(__INSERT_REFERENCESOURCEGROUP_WITH_ID);
      this.statRemoveReferenceSourceGroup = this.kbm.getConnection().prepareStatement(__REMOVE_REFERENCESOURCEGROUP);
      this.statSelectReferenceSources = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCESOURCES);
      this.statSelectReferenceSourceGroups = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCESOURCEGROUPS);
      this.statSelectReferenceSourceGroupByGroupName = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCESOURCEGROUP_BY_GROUPNAME);
      this.statSelectReferenceSourceGroupById = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCESOURCEGROUP_BY_ID);
      this.statSelectReferenceSourceGroupByStopGroup = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCESOURCEGROUP_BY_STOPGROUP);
      this.statUpdateGroupName = this.kbm.getConnection().prepareStatement(__UPDATE_GROUPNAME);
      this.statUpdateStopGroup = this.kbm.getConnection().prepareStatement(__UPDATE_STOPGROUP);
      this.statUpdateReferenceSourceGroup = this.kbm.getConnection().prepareStatement(__UPDATE_REFERENCESOURCEGROUP);
      
     } catch (SQLException e) {
     
       throw new KnowledgeBaseException(e.getMessage(), e.getCause());
     }
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param groupName
   * @param stopGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public Integer addReferenceSourceGroup(String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException{

    Integer id;

    try {

      this.statAddReferenceSourceGroup.clearParameters();

      this.statAddReferenceSourceGroup.setString(1, groupName);
      this.statAddReferenceSourceGroup.setBoolean(2, stopGroup);

      if (this.statAddReferenceSourceGroup.executeUpdate() == 1 ) {

        id = this.statAddReferenceSourceGroup.getGeneratedKeys().getInt(1);
        this.statAddReferenceSourceGroup.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddReferenceSourceGroupEvent(getReferenceSourceGroup(id)));
    }

    return id;
  }

  /**
   *
   * @param referenceSourceGroupID
   * @param groupName
   * @param stopGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addReferenceSourceGroup(Integer referenceSourceGroupID, String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statAddReferenceSourceGroupWithId.clearParameters();

      this.statAddReferenceSourceGroupWithId.setInt(1, referenceSourceGroupID);
      this.statAddReferenceSourceGroupWithId.setString(2, groupName);
      this.statAddReferenceSourceGroupWithId.setBoolean(3, stopGroup);

      result = this.statAddReferenceSourceGroupWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddReferenceSourceGroupEvent(getReferenceSourceGroup(referenceSourceGroupID)));
    }

    return result;
  }

  /**
   * 
   * @param referenceSourceGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addReferenceSourceGroup(ReferenceSourceGroup referenceSourceGroup, boolean notifyObservers)
          throws KnowledgeBaseException {

    return addReferenceSourceGroup(referenceSourceGroup.getReferenceSourceGroupID(), 
                                   referenceSourceGroup.getGroupName(),
                                   referenceSourceGroup.isStopGroup(),
                                   notifyObservers);
  }

  /**
   *
   * @param referenceSourceGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeReferenceSourceGroup(Integer referenceSourceGroupID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    ReferenceSourceGroup referenceSourceGroup = null;
    ArrayList<ReferenceSource> referenceSources = null;
    
    // Save the information before remove
    if (notifyObservers) {
      
      referenceSourceGroup = getReferenceSourceGroup(referenceSourceGroupID);
      referenceSources = getReferenceSources(referenceSourceGroupID);
    }
    
    try {

      this.statRemoveReferenceSourceGroup.clearParameters();

      this.statRemoveReferenceSourceGroup.setInt(1, referenceSourceGroupID);

      result = this.statRemoveReferenceSourceGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveReferenceSourceGroupEvent(referenceSourceGroup));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceSourceGroupRelationReferenceSourceEvent());
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddReferenceSourceWithoutGroupEvent(referenceSources));
    }

    return result;
  }

  /**
   *
   * @param idReferenceSourceGroup the references source group's ID
   *
   * @return a <ocde>ReferenceSourceGroup</code> or null if there is not any
   *         references source group with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ReferenceSourceGroup getReferenceSourceGroup(Integer idReferenceSourceGroup)
          throws KnowledgeBaseException {

    ResultSet rs;
    ReferenceSourceGroup referenceSourceGroup = null;

    try {

      this.statSelectReferenceSourceGroupById.clearParameters();

      this.statSelectReferenceSourceGroupById.setInt(1, idReferenceSourceGroup);

      rs = this.statSelectReferenceSourceGroupById.executeQuery();

      if (rs.next()) {

        referenceSourceGroup = UtilsDAO.getInstance().getReferenceSourceGroup(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceSourceGroup;
  }

  /**
   *
   * @param groupName the reference source group's name
   *
   * @return a <ocde>ReferenceSourceGroup</code> or null if there is not any
   *         referene source group with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ReferenceSourceGroup getReferenceSourceGroup(String groupName)
          throws KnowledgeBaseException {

    ResultSet rs;
    ReferenceSourceGroup referenceSourceGroup = null;

    try {

      this.statSelectReferenceSourceGroupByGroupName.clearParameters();

      this.statSelectReferenceSourceGroupByGroupName.setString(1, groupName);

      rs = this.statSelectReferenceSourceGroupByGroupName.executeQuery();

      if (rs.next()) {

        referenceSourceGroup = UtilsDAO.getInstance().getReferenceSourceGroup(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceSourceGroup;

  }

  /**
   *
   * @param stopGroup
   * @return
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<ReferenceSourceGroup> getReferenceSourceGroups(boolean stopGroup)
          throws KnowledgeBaseException {

    ArrayList<ReferenceSourceGroup> referenceSourceGroupList = new ArrayList<ReferenceSourceGroup>();
    ResultSet rs;
    ReferenceSourceGroup referenceSourceGroup = null;

    try {

      this.statSelectReferenceSourceGroupByStopGroup.clearParameters();

      this.statSelectReferenceSourceGroupByStopGroup.setBoolean(1, stopGroup);

      rs = this.statSelectReferenceSourceGroupByStopGroup.executeQuery();

      while (rs.next()) {

        referenceSourceGroup = UtilsDAO.getInstance().getReferenceSourceGroup(rs);

        referenceSourceGroupList.add(referenceSourceGroup);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceSourceGroupList;

  }

  /**
   *
   * @return
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<ReferenceSourceGroup> getReferenceSourceGroups()
          throws KnowledgeBaseException {

    ArrayList<ReferenceSourceGroup> referenceSourceGroupList = new ArrayList<ReferenceSourceGroup>();
    ResultSet rs;
    ReferenceSourceGroup referenceSourceGroup = null;

    try {

      this.statSelectReferenceSourceGroups.clearParameters();

      rs = this.statSelectReferenceSourceGroups.executeQuery();

      while (rs.next()) {

        referenceSourceGroup = UtilsDAO.getInstance().getReferenceSourceGroup(rs);

        referenceSourceGroupList.add(referenceSourceGroup);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceSourceGroupList;

  }
  
  /**
   *
   * @param referenceSourceGroupID
   * @param goupName
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setGroupName(Integer referenceSourceGroupID, String goupName, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateGroupName.clearParameters();

      this.statUpdateGroupName.setString(1, goupName);
      this.statUpdateGroupName.setInt(2, referenceSourceGroupID);

      result = this.statUpdateGroupName.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceGroupEvent(getReferenceSourceGroup(referenceSourceGroupID)));
    }

    return result;
  }

  /**
   *
   * @param referenceSourceGroupID
   * @param stopGroup true if the group if a stop group. Else otherwise.
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setStopGroup(Integer referenceSourceGroupID, boolean stopGroup, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateStopGroup.clearParameters();

      this.statUpdateStopGroup.setBoolean(1, stopGroup);
      this.statUpdateStopGroup.setInt(2, referenceSourceGroupID);

      result = this.statUpdateStopGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceGroupEvent(getReferenceSourceGroup(referenceSourceGroupID)));
    }

    return result;
  }

  /**
   * 
   * @param referenceSourceGroupID
   * @param groupName
   * @param stopGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean updateReferenceSourceGroup(Integer referenceSourceGroupID, String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateReferenceSourceGroup.clearParameters();

      this.statUpdateReferenceSourceGroup.setString(1, groupName);
      this.statUpdateReferenceSourceGroup.setBoolean(2, stopGroup);
      this.statUpdateReferenceSourceGroup.setInt(3, referenceSourceGroupID);

      result = this.statUpdateReferenceSourceGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceGroupEvent(getReferenceSourceGroup(referenceSourceGroupID)));
    }

    return result;
  }

  /**
   *
   * @param referenceSourceGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public ArrayList<ReferenceSource> getReferenceSources(Integer referenceSourceGroupID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<ReferenceSource> referencesList = new ArrayList<ReferenceSource>();

    try {

      this.statSelectReferenceSources.clearParameters();

      this.statSelectReferenceSources.setInt(1, referenceSourceGroupID);

      rs = this.statSelectReferenceSources.executeQuery();

      while (rs.next()) {

        referencesList.add(UtilsDAO.getInstance().getReferenceSource(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referencesList;
  }

  /**
   * <p>Check if there is an <code>ReferenceSourceGroup</code> with this
   * group's name.</p>
   *
   * @param groupName a string with the group's name
   *
   * @return true if there is an <code>ReferenceSourceGroup</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkReferenceSourceGroup(String groupName)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckReferenceSourceGroupByGroupName.clearParameters();

      this.statCheckReferenceSourceGroupByGroupName.setString(1, groupName);

      rs = this.statCheckReferenceSourceGroupByGroupName.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>ReferenceSourceGroup</code> with this ID.</p>
   *
   * @param idReferenceSourceGroup a string with the group's ID
   *
   * @return true if there is an <code>ReferenceSourceGroup</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkReferenceSourceGroup(Integer idReferenceSourceGroup)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckReferenceSourceGroupById.clearParameters();

      this.statCheckReferenceSourceGroupById.setInt(1, idReferenceSourceGroup);

      rs = this.statCheckReferenceSourceGroupById.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param referenceSourceGroupsToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<ReferenceSourceGroup> refreshReferenceSourceGroups(ArrayList<ReferenceSourceGroup> referenceSourceGroupsToRefresh) throws KnowledgeBaseException {
  
    int i;
    String query;
    ResultSet rs;
    ArrayList<ReferenceSourceGroup> referenceSourceGroups = new ArrayList<ReferenceSourceGroup>();
    
    i = 0;
    
    if (!referenceSourceGroupsToRefresh.isEmpty()) {

      query = "SELECT * FROM ReferenceSourceGroup WHERE idReferenceSourceGroup IN (" + referenceSourceGroupsToRefresh.get(i).getReferenceSourceGroupID();
      
      for (i = 1; i < referenceSourceGroupsToRefresh.size(); i++) {
        
        query += ", " + referenceSourceGroupsToRefresh.get(i).getReferenceSourceGroupID();
      }
      
      query += ");";
      
      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          referenceSourceGroups.add(UtilsDAO.getInstance().getReferenceSourceGroup(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }
    
    return referenceSourceGroups;
  }
  
  /**
   * 
   * @param referenceSourceGroupToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ReferenceSourceGroup refreshReferenceSourceGroup(ReferenceSourceGroup referenceSourceGroupToRefresh) throws KnowledgeBaseException {
  
    return getReferenceSourceGroup(referenceSourceGroupToRefresh.getReferenceSourceGroupID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
