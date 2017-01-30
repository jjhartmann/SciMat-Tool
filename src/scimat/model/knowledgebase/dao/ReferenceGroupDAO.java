/*
 * ReferenceGroupDAO.java
 *
 * Created on 21-oct-2010, 17:48:58
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddReferenceGroupEvent;
import scimat.knowledgebaseevents.event.add.AddReferenceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.relation.ReferenceGroupRelationReferenceEvent;
import scimat.knowledgebaseevents.event.remove.RemoveReferenceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceGroupEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class ReferenceGroupDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

 /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO ReferenceGroup(groupName,stopGroup) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_REFERENCEGROUP = "INSERT INTO ReferenceGroup(groupName,stopGroup) VALUES(?,?);";

  /**
   * <pre>
   * INSERT INTO ReferenceGroup(idReferenceGroup,groupName,stopGroup) VALUES(?,?,?);
   * </pre>
   */
  private final static String __INSERT_REFERENCEGROUP_WITH_ID = "INSERT INTO ReferenceGroup(idReferenceGroup,groupName,stopGroup) VALUES(?,?,?);";

  /**
   * <pre>
   * DELETE ReferenceGroup
   * WHERE idReferenceGroup = ?;
   * </pre>
   */
  private final static String __REMOVE_REFERENCEGROUP = "DELETE FROM ReferenceGroup "
                                                      + "WHERE idReferenceGroup = ?;";

  /**
   * <pre>
   * UPDATE ReferenceGroup 
   * SET groupName = ?
   * WHERE idReferenceGroup = ?
   * </pre>
   */
  private final static String __UPDATE_GROUPNAME = "UPDATE ReferenceGroup SET groupName = ? WHERE idReferenceGroup = ?;";

  /**
   * <pre>
   * UPDATE ReferenceGroup 
   * SET stopGroup = ?
   * WHERE idReferenceGroup = ?;
   * </pre>
   */
  private final static String __UPDATE_STOPGROUP = "UPDATE ReferenceGroup SET stopGroup = ? WHERE idReferenceGroup = ?;";

  /**
   * <pre>
   * UPDATE ReferenceGroup
   * SET groupName = ?,
   *     stopGroup = ?
   * WHERE idReferenceGroup = ?;
   * </pre>
   */
  private final static String __UPDATE_REFERENCEGROUP = "UPDATE ReferenceGroup "
                                                      + "SET groupName = ?, "
                                                      + "    stopGroup = ? "
                                                      + "WHERE idReferenceGroup = ?;";

  /**
   * <pre>
   * SELECT r.idReference, r.fullReference, r.volume, r.issue, r.page, r.doi, r.format, r.year
   * FROM Reference r, ReferenceGroup rg
   * WHERE rg.idReferenceGroup = ? AND
   *       r.ReferenceGroup_idReferenceGroup = rg.idReferenceGroup;
   * </pre>
   */
  private final static String __SELECT_REFERENCES = "SELECT r.* "
                                                 + "FROM Reference r, ReferenceGroup rg "
                                                 + "WHERE rg.idReferenceGroup = ? AND "
                                                 + "      r.ReferenceGroup_idReferenceGroup = rg.idReferenceGroup;";

  private final static String __SELECT_REFERENCEGROUP_BY_ID = "SELECT * FROM ReferenceGroup WHERE idReferenceGroup = ?;";
  private final static String __SELECT_REFERENCEGROUP_BY_NAME ="SELECT * FROM ReferenceGroup WHERE groupName = ?;";
  private final static String __SELECT_REFERENCEGROUP_BY_STOPGROUP = "SELECT * FROM ReferenceGroup WHERE stopGroup = ?;";
  private final static String __SELECT_REFERENCEGROUPS = "SELECT * FROM ReferenceGroup;";
  private final static String __CHECK_REFERENCEGROUP_BY_NAME = "SELECT idReferenceGroup FROM ReferenceGroup WHERE groupName = ?;";
  private final static String __CHECK_REFERENCEGROUP_BY_ID = "SELECT idReferenceGroup FROM ReferenceGroup WHERE idReferenceGroup = ?;";
  
  private PreparedStatement statCheckReferenceGroupByName;
  private PreparedStatement statCheckReferenceGroupById;
  private PreparedStatement statAddReferenceGroup;
  private PreparedStatement statAddReferenceGroupWithId;
  private PreparedStatement statRemoveReferenceGroup;
  private PreparedStatement statSelectReferences;
  private PreparedStatement statSelectReferenceGroups;
  private PreparedStatement statSelectReferenceGroupById;
  private PreparedStatement statSelectReferenceGroupByName;
  private PreparedStatement statSelectReferenceGroupByStopGroup;
  private PreparedStatement statUpdateGroupName;
  private PreparedStatement statUpdateReferenceGroup;
  private PreparedStatement statUpdateStopGroup;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param idReferenceGroup
   * @param groupName
   * @param stopGroup
   * @param kbm
   */
  public ReferenceGroupDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    
    this.kbm = kbm;
    
    try {
      
      this.statCheckReferenceGroupByName = this.kbm.getConnection().prepareStatement(__CHECK_REFERENCEGROUP_BY_NAME);
      this.statCheckReferenceGroupById = this.kbm.getConnection().prepareStatement(__CHECK_REFERENCEGROUP_BY_ID);
      this.statAddReferenceGroup = this.kbm.getConnection().prepareStatement(__INSERT_REFERENCEGROUP, Statement.RETURN_GENERATED_KEYS);
      this.statAddReferenceGroupWithId = this.kbm.getConnection().prepareStatement(__INSERT_REFERENCEGROUP_WITH_ID);
      this.statRemoveReferenceGroup = this.kbm.getConnection().prepareStatement(__REMOVE_REFERENCEGROUP);
      this.statSelectReferences = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCES);
      this.statSelectReferenceGroups = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCEGROUPS);
      this.statSelectReferenceGroupById = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCEGROUP_BY_ID);
      this.statSelectReferenceGroupByName = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCEGROUP_BY_NAME);
      this.statSelectReferenceGroupByStopGroup = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCEGROUP_BY_STOPGROUP);
      this.statUpdateGroupName = this.kbm.getConnection().prepareStatement(__UPDATE_GROUPNAME);
      this.statUpdateReferenceGroup = this.kbm.getConnection().prepareStatement(__UPDATE_REFERENCEGROUP);
      this.statUpdateStopGroup = this.kbm.getConnection().prepareStatement(__UPDATE_STOPGROUP);
    
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
  public Integer addReferenceGroup(String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException{

    Integer id;

    try {

      this.statAddReferenceGroup.clearParameters();

      this.statAddReferenceGroup.setString(1, groupName);
      this.statAddReferenceGroup.setBoolean(2, stopGroup);

      if (this.statAddReferenceGroup.executeUpdate() == 1 ) {

        id = this.statAddReferenceGroup.getGeneratedKeys().getInt(1);
        this.statAddReferenceGroup.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddReferenceGroupEvent(getReferenceGroup(id)));
    }

    return id;
  }

  /**
   *
   * @param referenceGroupID
   * @param groupName
   * @param stopGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addReferenceGroup(Integer referenceGroupID, String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statAddReferenceGroupWithId.clearParameters();

      this.statAddReferenceGroupWithId.setInt(1, referenceGroupID);
      this.statAddReferenceGroupWithId.setString(2, groupName);
      this.statAddReferenceGroupWithId.setBoolean(3, stopGroup);

      result = this.statAddReferenceGroupWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddReferenceGroupEvent(getReferenceGroup(referenceGroupID)));
    }

    return result;
  }

  /**
   * 
   * @param referenceGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addReferenceGroup(ReferenceGroup referenceGroup, boolean notifyObservers)
          throws KnowledgeBaseException {

    return addReferenceGroup(referenceGroup.getReferenceGroupID(),
                             referenceGroup.getGroupName(),
                             referenceGroup.isStopGroup(),
                             notifyObservers);
  }

  /**
   *
   * @param referenceGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeReferenceGroup(Integer referenceGroupID, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;
    ReferenceGroup referenceGroup = null;
    ArrayList<Reference> references = null;
    
    // Save the information before remove
    if (notifyObservers) {
      
      referenceGroup = getReferenceGroup(referenceGroupID);
      references = getReferences(referenceGroupID);
    }

    try {

      this.statRemoveReferenceGroup.clearParameters();

      this.statRemoveReferenceGroup.setInt(1, referenceGroupID);

      result = this.statRemoveReferenceGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveReferenceGroupEvent(referenceGroup));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceGroupRelationReferenceEvent());
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddReferenceWithoutGroupEvent(references));
    }

    return result;
  }

  /**
   *
   * @param idReferenceGroup the references group's ID
   *
   * @return a <ocde>ReferenceGroup</code> or null if there is not any
   *         references group with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ReferenceGroup getReferenceGroup(Integer idReferenceGroup)
          throws KnowledgeBaseException {

    ResultSet rs;
    ReferenceGroup referenceGroup = null;

    try {

      this.statSelectReferenceGroupById.clearParameters();

      this.statSelectReferenceGroupById.setInt(1, idReferenceGroup);

      rs = this.statSelectReferenceGroupById.executeQuery();

      if (rs.next()) {

        referenceGroup = UtilsDAO.getInstance().getReferenceGroup(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceGroup;
  }

  /**
   *
   * @param groupName the reference group's name
   *
   * @return a <ocde>ReferenceGroup</code> or null if there is not any
   *         referene group with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ReferenceGroup getReferenceGroup(String groupName)
          throws KnowledgeBaseException {

    ResultSet rs;
    ReferenceGroup referenceGroup = null;

    try {

      this.statSelectReferenceGroupByName.clearParameters();

      this.statSelectReferenceGroupByName.setString(1, groupName);

      rs = this.statSelectReferenceGroupByName.executeQuery();

      if (rs.next()) {

        referenceGroup = UtilsDAO.getInstance().getReferenceGroup(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceGroup;

  }

  /**
   *
   * @param stopGroup
   * @return
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<ReferenceGroup> getReferenceGroups(boolean stopGroup)
          throws KnowledgeBaseException {

    ArrayList<ReferenceGroup> referenceGroupList = new ArrayList<ReferenceGroup>();
    ResultSet rs;
    ReferenceGroup referenceGroup = null;

    try {

      this.statSelectReferenceGroupByStopGroup.clearParameters();

      this.statSelectReferenceGroupByStopGroup.setBoolean(1, stopGroup);

      rs = this.statSelectReferenceGroupByStopGroup.executeQuery();

      while (rs.next()) {

        referenceGroup = UtilsDAO.getInstance().getReferenceGroup(rs);

        referenceGroupList.add(referenceGroup);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceGroupList;

  }

  /**
   *
   * @return
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<ReferenceGroup> getReferenceGroups()
          throws KnowledgeBaseException {

    ArrayList<ReferenceGroup> referenceGroupList = new ArrayList<ReferenceGroup>();
    ResultSet rs;
    ReferenceGroup referenceGroup = null;

    try {

      this.statSelectReferenceGroups.clearParameters();

      rs = this.statSelectReferenceGroups.executeQuery();

      while (rs.next()) {

        referenceGroup = UtilsDAO.getInstance().getReferenceGroup(rs);

        referenceGroupList.add(referenceGroup);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceGroupList;

  }

  /**
   * 
   * @param referenceGroupID
   * @param goupName
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setGroupName(Integer referenceGroupID, String goupName, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateGroupName.clearParameters();

      this.statUpdateGroupName.setString(1, goupName);
      this.statUpdateGroupName.setInt(2, referenceGroupID);

      result = this.statUpdateGroupName.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceGroupEvent(getReferenceGroup(referenceGroupID)));
    }

    return result;
  }

  /**
   *
   * @param referenceGroupID
   * @param stopGroup true if the group if a stop group. Else otherwise.
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setStopGroup(Integer referenceGroupID, boolean stopGroup, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateStopGroup.clearParameters();

      this.statUpdateStopGroup.setBoolean(1, stopGroup);
      this.statUpdateStopGroup.setInt(2, referenceGroupID);

      result = this.statUpdateStopGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceGroupEvent(getReferenceGroup(referenceGroupID)));
    }

    return result;
  }

  /**
   *
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean updateReferenceGroup(Integer referenceGroupID, String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateReferenceGroup.clearParameters();

      this.statUpdateReferenceGroup.setString(1, groupName);
      this.statUpdateReferenceGroup.setBoolean(2, stopGroup);
      this.statUpdateReferenceGroup.setInt(3, referenceGroupID);

      result = this.statUpdateReferenceGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceGroupEvent(getReferenceGroup(referenceGroupID)));
    }

    return result;
  }

  /**
   * 
   * @param referenceGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public ArrayList<Reference> getReferences(Integer referenceGroupID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Reference> referencesList = new ArrayList<Reference>();

    try {

      this.statSelectReferences.clearParameters();

      this.statSelectReferences.setInt(1, referenceGroupID);

      rs = this.statSelectReferences.executeQuery();

      while (rs.next()) {

        referencesList.add(UtilsDAO.getInstance().getReference(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referencesList;
  }

  /**
   * <p>Check if there is an <code>ReferenceGroup</code> with this group's
   * name.</p>
   *
   * @param groupName a string with the group's name
   *
   * @return true if there is an <code>ReferenceGroup</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkReferenceGroup(String groupName)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckReferenceGroupByName.clearParameters();

      this.statCheckReferenceGroupByName.setString(1, groupName);

      rs = this.statCheckReferenceGroupByName.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>ReferenceGroup</code> with this ID.</p>
   *
   * @param idReferenceGroup the group's ID
   *
   * @return true if there is an <code>ReferenceGroup</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkReferenceGroup(Integer idReferenceGroup)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckReferenceGroupById.clearParameters();

      this.statCheckReferenceGroupById.setInt(1, idReferenceGroup);

      rs = this.statCheckReferenceGroupById.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param referenceGroupsToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<ReferenceGroup> refreshReferenceGroups(ArrayList<ReferenceGroup> referenceGroupsToRefresh) throws KnowledgeBaseException {
  
    int i;
    String query;
    ResultSet rs;
    ArrayList<ReferenceGroup> referenceGroups = new ArrayList<ReferenceGroup>();
    
    i = 0;
    
    if (!referenceGroupsToRefresh.isEmpty()) {

      query = "SELECT * FROM ReferenceGroup WHERE idReferenceGroup IN (" + referenceGroupsToRefresh.get(i).getReferenceGroupID();
      
      for (i = 1; i < referenceGroupsToRefresh.size(); i++) {
        
        query += ", " + referenceGroupsToRefresh.get(i).getReferenceGroupID();
      }
      
      query += ");";
      
      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          referenceGroups.add(UtilsDAO.getInstance().getReferenceGroup(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }
    
    return referenceGroups;
  }
  
  /**
   * 
   * @param referenceGroupToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ReferenceGroup refreshReferenceGroup(ReferenceGroup referenceGroupToRefresh) throws KnowledgeBaseException {
  
    return getReferenceGroup(referenceGroupToRefresh.getReferenceGroupID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
