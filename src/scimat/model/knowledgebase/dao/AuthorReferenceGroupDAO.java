/*
 * AuthorReferenceGroupDAO.java
 *
 * Created on 04-nov-2010, 13:44:07
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.add.AddAuthorReferenceGroupEvent;
import scimat.knowledgebaseevents.event.add.AddAuthorReferenceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.relation.AuthorReferenceGroupRelationAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.remove.RemoveAuthorReferenceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceGroupEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceGroupDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;
  /**
   * <pre>
   * INSERT INTO AuthorReferenceGroup(groupName,stopGroup) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_AUTHORREFERENCEGROUP = "INSERT INTO AuthorReferenceGroup(groupName,stopGroup) VALUES(?,?);";
  /**
   * <pre>
   * INSERT INTO AuthorReferenceGroup(idAuthorReferenceGroup,groupName,stopGroup) VALUES(?,?,?);
   * </pre>
   */
  private final static String __INSERT_AUTHORREFERENCEGROUP_WITH_ID = "INSERT INTO AuthorReferenceGroup(idAuthorReferenceGroup,groupName,stopGroup) VALUES(?,?,?);";
  /**
   * <pre>
   * </pre>
   */
  private final static String __REMOVE_AUTHORREFERENCEGROUP = "DELETE FROM AuthorReferenceGroup WHERE idAuthorReferenceGroup = ?;";
  /**
   * <pre>
   * UPDATE AuthorReferenceGroup 
   * SET groupName = ?
   * WHERE idAuthorReferenceGroup = ?;
   * </pre>
   */
  private final static String __UPDATE_GROUPNAME = "UPDATE AuthorReferenceGroup SET groupName = ? WHERE idAuthorReferenceGroup = ?;";
  /**
   * <pre>
   * UPDATE AuthorReferenceGroup 
   * SET groupName = ?
   * WHERE idAuthorReferenceGroup = ?;
   * </pre>
   */
  private final static String __UPDATE_STOPGROUP = "UPDATE AuthorReferenceGroup SET stopGroup = ? WHERE idAuthorReferenceGroup = ?;";
  /**
   *
   */
  private final static String __UPDATE_AUTHORREFERENCEGROUP = "UPDATE AuthorReferenceGroup "
          + "SET groupName = ?, "
          + "    stopGroup = ? "
          + "WHERE idAuthorReferenceGroup = ?;";
  /**
   * <pre>
   * SELECT a.idAuthorReference, a.authorName"
   * FROM AuthorReference a, AuthorReferenceGroup ag
   * WHERE ag.idAuthorReferenceGroup = ? AND 
   * a.AuthorReferenceGroup_idAuthorReferenceGroup = ag.idAuthorReferenceGroup;
   * </pre>
   */
  private final static String __SELECT_AUTHORREFERENCE = "SELECT a.* "
          + "FROM AuthorReference a, AuthorReferenceGroup ag "
          + "WHERE ag.idAuthorReferenceGroup = ? AND "
          + "      a.AuthorReferenceGroup_idAuthorReferenceGroup = ag.idAuthorReferenceGroup;";
  private final static String __SELECT_AUTHORREFERENCE_ID = "SELECT a.idAuthorReference "
          + "FROM AuthorReference a, AuthorReferenceGroup ag "
          + "WHERE ag.idAuthorReferenceGroup = ? AND "
          + "      a.AuthorReferenceGroup_idAuthorReferenceGroup = ag.idAuthorReferenceGroup;";
  private final String __SELECT_AUTHORREFERENCEGROUP_BY_ID = "SELECT * FROM AuthorReferenceGroup WHERE idAuthorReferenceGroup = ?;";
  private final String __SELECT_AUTHORREFERENCEGROUP_BY_NAME = "SELECT * FROM AuthorReferenceGroup WHERE groupName = ?;";
  private final String __SELECT_AUTHORREFERENCEGROUP_BY_STOPGROUP = "SELECT * FROM AuthorReferenceGroup WHERE stopGroup = ?;";
  private final String __SELECT_AUTHORREFERENCEGROUPS = "SELECT * FROM AuthorReferenceGroup;";
  private final String __SELECT_AUTHORREFERENCEGROUP_IDS = "SELECT idAuthorReferenceGroup FROM AuthorReferenceGroup;";
  private final String __CHECK_AUTHORREFERENCEGROUP_BY_NAME = "SELECT idAuthorReferenceGroup FROM AuthorReferenceGroup WHERE groupName = ?;";
  private final String __CHECK_AUTHORREFERENCEGROUP_BY_ID = "SELECT idAuthorReferenceGroup FROM AuthorReferenceGroup WHERE idAuthorReferenceGroup = ?;";
  
  private PreparedStatement statCheckAuthorReferenceById;
  private PreparedStatement statCheckAuthorReferenceByName;
  private PreparedStatement statAddAuthorRefefenceGroup;
  private PreparedStatement statAddAuthorRefefenceGroupWithId;
  private PreparedStatement statRemoveAuthorRefefenceGroup;
  private PreparedStatement statSelectAuthorReferences;
  private PreparedStatement statSelectAuthorReferenceGroups;
  private PreparedStatement statSelectAuthorReferenceGroupById;
  private PreparedStatement statSelectAuthorReferenceGroupByName;
  private PreparedStatement statSelectAuthorReferenceGroupByStopGroup;
  private PreparedStatement statSelectAuthorReferenceGroupIds;
  private PreparedStatement statSelectAuthorReferenceIds;
  private PreparedStatement statUpdateAuthorReferenceGroup;
  private PreparedStatement statUpdateGroupName;
  private PreparedStatement statUpdateStopGroup;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  /**
   * 
   * @param kbm
   */
  public AuthorReferenceGroupDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException{

    this.kbm = kbm;

    try {

      this.statCheckAuthorReferenceById = this.kbm.getConnection().prepareStatement(__CHECK_AUTHORREFERENCEGROUP_BY_ID);
      this.statCheckAuthorReferenceByName = this.kbm.getConnection().prepareStatement(__CHECK_AUTHORREFERENCEGROUP_BY_NAME);
      this.statAddAuthorRefefenceGroup = this.kbm.getConnection().prepareStatement(__INSERT_AUTHORREFERENCEGROUP, Statement.RETURN_GENERATED_KEYS);
      this.statAddAuthorRefefenceGroupWithId = this.kbm.getConnection().prepareStatement(__INSERT_AUTHORREFERENCEGROUP_WITH_ID);
      this.statRemoveAuthorRefefenceGroup = this.kbm.getConnection().prepareStatement(__REMOVE_AUTHORREFERENCEGROUP);
      this.statSelectAuthorReferences = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCE);
      this.statSelectAuthorReferenceGroups = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCEGROUPS);
      this.statSelectAuthorReferenceGroupById = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCEGROUP_BY_ID);
      this.statSelectAuthorReferenceGroupByName = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCEGROUP_BY_NAME);
      this.statSelectAuthorReferenceGroupByStopGroup = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCEGROUP_BY_STOPGROUP);
      this.statSelectAuthorReferenceGroupIds = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCEGROUP_IDS);
      this.statSelectAuthorReferenceIds = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCE_ID);
      this.statUpdateAuthorReferenceGroup = this.kbm.getConnection().prepareStatement(__UPDATE_AUTHORREFERENCEGROUP);
      this.statUpdateGroupName = this.kbm.getConnection().prepareStatement(__UPDATE_GROUPNAME);
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
  public Integer addAuthorReferenceGroup(String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddAuthorRefefenceGroup.clearParameters();

      this.statAddAuthorRefefenceGroup.setString(1, groupName);
      this.statAddAuthorRefefenceGroup.setBoolean(2, stopGroup);

      if (this.statAddAuthorRefefenceGroup.executeUpdate() == 1) {

        id = this.statAddAuthorRefefenceGroup.getGeneratedKeys().getInt(1);
        this.statAddAuthorRefefenceGroup.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAuthorReferenceGroupEvent(getAuthorReferenceGroup(id)));
    }

    return id;
  }

  /**
   *
   * @param authorReferenceGroupID
   * @param groupName
   * @param stopGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addAuthorReferenceGroup(Integer authorReferenceGroupID, String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statAddAuthorRefefenceGroupWithId.clearParameters();

      this.statAddAuthorRefefenceGroupWithId.setInt(1, authorReferenceGroupID);
      this.statAddAuthorRefefenceGroupWithId.setString(2, groupName);
      this.statAddAuthorRefefenceGroupWithId.setBoolean(3, stopGroup);

      result = this.statAddAuthorRefefenceGroupWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAuthorReferenceGroupEvent(getAuthorReferenceGroup(authorReferenceGroupID)));
    }

    return result;
  }

  /**
   * 
   * @param authorReferenceGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addAuthorReferenceGroup(AuthorReferenceGroup authorReferenceGroup, boolean notifyObservers)
          throws KnowledgeBaseException {

    return addAuthorReferenceGroup(authorReferenceGroup.getAuthorReferenceGroupID(),
            authorReferenceGroup.getGroupName(),
            authorReferenceGroup.isStopGroup(),
            notifyObservers);
  }

  /**
   *
   * @param idAuthorReferenceGroup the author of references group's ID
   *
   * @return an <ocde>AuthorReferenceGroup</code> or null if there is not any
   *         author references group with this ID
   *
   * @throws KnowledgeBaseException
   */
  public AuthorReferenceGroup getAuthorReferenceGroup(Integer idAuthorReferenceGroup)
          throws KnowledgeBaseException {

    ResultSet rs;
    AuthorReferenceGroup authorReferenceGroup = null;

    try {

      this.statSelectAuthorReferenceGroupById.clearParameters();
      
      this.statSelectAuthorReferenceGroupById.setInt(1, idAuthorReferenceGroup);

      rs = this.statSelectAuthorReferenceGroupById.executeQuery();

      if (rs.next()) {

        authorReferenceGroup = UtilsDAO.getInstance().getAuthorReferenceGroup(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferenceGroup;
  }

  /**
   *
   * @param groupName the author reference group's name
   *
   * @return an <ocde>AuthorReferenceGroup</code> or null if there is not any
   *         author referene group with this ID
   *
   * @throws KnowledgeBaseException
   */
  public AuthorReferenceGroup getAuthorReferenceGroup(String groupName)
          throws KnowledgeBaseException {

    ResultSet rs;
    AuthorReferenceGroup authorReferenceGroup = null;

    try {

      this.statSelectAuthorReferenceGroupByName.clearParameters();

      this.statSelectAuthorReferenceGroupByName.setString(1, groupName);

      rs = this.statSelectAuthorReferenceGroupByName.executeQuery();

      if (rs.next()) {

        authorReferenceGroup = UtilsDAO.getInstance().getAuthorReferenceGroup(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferenceGroup;

  }

  /**
   *
   * @param stopGroup
   * @return
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<AuthorReferenceGroup> getAuthorReferenceGroups(boolean stopGroup)
          throws KnowledgeBaseException {

    ArrayList<AuthorReferenceGroup> authorReferenceGroupList = new ArrayList<AuthorReferenceGroup>();
    ResultSet rs;
    AuthorReferenceGroup authorReferenceGroup = null;

    try {


      this.statSelectAuthorReferenceGroupByStopGroup.clearParameters();
      this.statSelectAuthorReferenceGroupByStopGroup.setBoolean(1, stopGroup);

      rs = this.statSelectAuthorReferenceGroupByStopGroup.executeQuery();

      while (rs.next()) {

        authorReferenceGroup = UtilsDAO.getInstance().getAuthorReferenceGroup(rs);

        authorReferenceGroupList.add(authorReferenceGroup);
      }
      
      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferenceGroupList;

  }

  /**
   *
   * @return
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<AuthorReferenceGroup> getAuthorReferenceGroups()
          throws KnowledgeBaseException {

    ArrayList<AuthorReferenceGroup> authorReferenceGroupList = new ArrayList<AuthorReferenceGroup>();
    ResultSet rs;
    AuthorReferenceGroup authorReferenceGroup = null;

    try {

      this.statSelectAuthorReferenceGroups.clearParameters();

      rs = this.statSelectAuthorReferenceGroups.executeQuery();

      while (rs.next()) {

        authorReferenceGroup = UtilsDAO.getInstance().getAuthorReferenceGroup(rs);

        authorReferenceGroupList.add(authorReferenceGroup);
      }
      
      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferenceGroupList;

  }

  /**
   * 
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Integer> getAuthorReferenceGroupIDs()
          throws KnowledgeBaseException {

    ArrayList<Integer> authorReferenceGroupList = new ArrayList<Integer>();
    ResultSet rs;

    try {

      this.statSelectAuthorReferenceGroupIds.clearParameters();

      rs = this.statSelectAuthorReferenceGroupIds.executeQuery();

      while (rs.next()) {

        authorReferenceGroupList.add(UtilsDAO.getInstance().getAuthorReferenceGroupID(rs));
      }
      
      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferenceGroupList;

  }

  /**
   *
   * @param authorReferenceGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeAuthorReferenceGroup(Integer authorReferenceGroupID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    AuthorReferenceGroup authorReferenceGroup = null;
    ArrayList<AuthorReference> authorReferences = null;

    // Save the information before remove
    if (notifyObservers) {

      authorReferenceGroup = getAuthorReferenceGroup(authorReferenceGroupID);
      authorReferences = getAuthorReferences(authorReferenceGroupID);
    }

    try {

      this.statRemoveAuthorRefefenceGroup.clearParameters();

      this.statRemoveAuthorRefefenceGroup.setInt(1, authorReferenceGroupID);

      result = this.statRemoveAuthorRefefenceGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveAuthorReferenceGroupEvent(authorReferenceGroup));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AuthorReferenceGroupRelationAuthorReferenceEvent());
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAuthorReferenceWithoutGroupEvent(authorReferences));
    }

    return result;
  }

  /**
   *
   * @param goupName the authorGroup's name to set
   *
   * @throws KnowledgeBaseException
   */
  public boolean setGroupName(Integer authorReferenceGroupID, String goupName, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statUpdateGroupName.clearParameters();

      this.statUpdateGroupName.setString(1, goupName);
      this.statUpdateGroupName.setInt(2, authorReferenceGroupID);

      result = this.statUpdateGroupName.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceGroupEvent(getAuthorReferenceGroup(authorReferenceGroupID)));
    }

    return result;
  }

  /**
   *
   * @param stopGroup true if the group if a stop group. Else otherwise.
   *
   * @throws KnowledgeBaseException
   */
  public boolean setStopGroup(Integer authorReferenceGroupID, boolean stopGroup, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statUpdateStopGroup.clearParameters();

      this.statUpdateStopGroup.setBoolean(1, stopGroup);
      this.statUpdateStopGroup.setInt(2, authorReferenceGroupID);

      result = this.statUpdateStopGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceGroupEvent(getAuthorReferenceGroup(authorReferenceGroupID)));
    }

    return result;
  }

  /**
   *
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean updateAuthorReferenceGroup(Integer authorReferenceGroupID, String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statUpdateAuthorReferenceGroup.clearParameters();

      this.statUpdateAuthorReferenceGroup.setString(1, groupName);
      this.statUpdateAuthorReferenceGroup.setBoolean(2, stopGroup);
      this.statUpdateAuthorReferenceGroup.setInt(3, authorReferenceGroupID);

      result = this.statUpdateAuthorReferenceGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceGroupEvent(getAuthorReferenceGroup(authorReferenceGroupID)));
    }

    return result;
  }

  /**
   *
   * @return an array with the affiliations associated with this author group
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<AuthorReference> getAuthorReferences(Integer authorReferenceGroupID)
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<AuthorReference> authorsReferenceList = new ArrayList<AuthorReference>();

    try {

      this.statSelectAuthorReferences.clearParameters();

      this.statSelectAuthorReferences.setInt(1, authorReferenceGroupID);

      rs = this.statSelectAuthorReferences.executeQuery();

      while (rs.next()) {

        authorsReferenceList.add(UtilsDAO.getInstance().getAuthorReference(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorsReferenceList;
  }

  public ArrayList<Integer> getAuthorReferenceIDs(Integer authorReferenceGroupID)
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Integer> authorsReferenceList = new ArrayList<Integer>();

    try {

      this.statSelectAuthorReferenceIds.clearParameters();

      this.statSelectAuthorReferenceIds.setInt(1, authorReferenceGroupID);

      rs = this.statSelectAuthorReferenceIds.executeQuery();

      while (rs.next()) {

        authorsReferenceList.add(UtilsDAO.getInstance().getAuthorReferenceID(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorsReferenceList;
  }

  /**
   * <p>Check if there is an <code>AuthorReferencegroup</code> with this
   * group's name.</p>
   *
   * @param groupName a string with the group's name
   *
   * @return true if there is an <code>AuthorReferenceGroup</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkAuthorReferenceGroup(String groupName)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckAuthorReferenceByName.clearParameters();

      this.statCheckAuthorReferenceByName.setString(1, groupName);

      rs = this.statCheckAuthorReferenceByName.executeQuery();

      result = rs.next();

      rs.close();

      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>AuthorReferencegroup</code> with this ID.</p>
   *
   * @param idAuthorReferenceGroup a string with the group's ID
   *
   * @return true if there is an <code>AuthorReferenceGroup</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkAuthorReferenceGroup(Integer idAuthorReferenceGroup)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckAuthorReferenceById.clearParameters();

      this.statCheckAuthorReferenceById.setInt(1, idAuthorReferenceGroup);

      rs = this.statCheckAuthorReferenceById.executeQuery();

      result = rs.next();

      rs.close();

      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * 
   * @param authorReferenceGroupsToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<AuthorReferenceGroup> refreshAuthorReferenceGroups(ArrayList<AuthorReferenceGroup> authorReferenceGroupsToRefresh) throws KnowledgeBaseException {

    int i;
    String query;
    ResultSet rs;
    ArrayList<AuthorReferenceGroup> authorReferenceGroups = new ArrayList<AuthorReferenceGroup>();

    i = 0;

    if (!authorReferenceGroupsToRefresh.isEmpty()) {

      query = "SELECT * FROM AuthorReferenceGroup WHERE idAuthorReferenceGroup IN (" + authorReferenceGroupsToRefresh.get(i).getAuthorReferenceGroupID();

      for (i = 1; i < authorReferenceGroupsToRefresh.size(); i++) {

        query += ", " + authorReferenceGroupsToRefresh.get(i).getAuthorReferenceGroupID();
      }

      query += ");";

      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          authorReferenceGroups.add(UtilsDAO.getInstance().getAuthorReferenceGroup(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }

    return authorReferenceGroups;
  }

  /**
   * 
   * @param authorReferenceGroupToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public AuthorReferenceGroup refreshAuthorReferenceGroup(AuthorReferenceGroup authorReferenceGroupToRefresh) throws KnowledgeBaseException {

    return getAuthorReferenceGroup(authorReferenceGroupToRefresh.getAuthorReferenceGroupID());
  }
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
