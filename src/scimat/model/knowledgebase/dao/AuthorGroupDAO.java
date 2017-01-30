/*
 * AuthorGroupDAO.java
 *
 * Created on 21-oct-2010, 17:48:36
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddAuthorGroupEvent;
import scimat.knowledgebaseevents.event.add.AddAuthorWithoutGroupEvent;
import scimat.knowledgebaseevents.event.relation.AuthorGroupRelationAuthorEvent;
import scimat.knowledgebaseevents.event.remove.RemoveAuthorGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorGroupEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 * <p>This class represents a set of {@link Author} that represent the same
 * author.</p>
 * 
 * <p>Each {@link AuthorGroupDAO} has one single one identifier, a name and
 * a boolean field which indicates if the group is a stop group. A value of
 * true indicates that it is a group that has no meaning and therefore should
 * not be taken into account in the analysis.<p>
 *
 * @author mjcobo
 */
public class AuthorGroupDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;
  /**
   * <pre>
   * INSERT INTO AuthorGroup(groupName,stopGroup) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_AUTHORGROUP = "INSERT INTO AuthorGroup(groupName,stopGroup) VALUES(?,?);";
  /**
   * <pre>
   * INSERT INTO AuthorGroup(idAuthorGroup,groupName,stopGroup) VALUES(?,?,?);
   * </pre>
   */
  private final static String __INSERT_AUTHORGROUP_WITH_ID = "INSERT INTO AuthorGroup(idAuthorGroup,groupName,stopGroup) VALUES(?,?,?);";
  /**
   * <pre>
   * DELETE AuthorGroup
   * WHERE idAuthorGroup = ?;
   * </pre>
   */
  private final static String __REMOVE_AUTHORGROUP = "DELETE FROM AuthorGroup WHERE idAuthorGroup = ?;";
  /**
   * <pre>
   * UPDATE AuthorGroup 
   * SET groupName = ?
   * WHERE idAuthorGroup = ?
   * </pre>
   */
  private final static String __UPDATE_GROUPNAME = "UPDATE AuthorGroup "
          + "SET groupName = ? "
          + "WHERE idAuthorGroup = ?";
  /**
   * <pre>
   * UPDATE AuthorGroup 
   * SET stopGroup = ?
   * WHERE idAuthorGroup = ?;
   * </pre>
   */
  private final static String __UPDATE_STOPGROUP = "UPDATE AuthorGroup "
          + "SET stopGroup = ? "
          + "WHERE idAuthorGroup = ?;";
  /**
   * <pre>
   * UPDATE AuthorGroup
   * SET groupName = ?,
   *     stopGroup = ?
   * WHERE idAuthorGroup = ?;
   * </pre>
   */
  private final static String __UPDATE_AUTHORGROUP = "UPDATE AuthorGroup "
          + "SET groupName = ?, "
          + "    stopGroup = ? "
          + "WHERE idAuthorGroup = ?;";
  /**
   * <pre>
   * SELECT a.idAuthor, a.authorName, a.fullAuthorName
   * FROM Author a, AuthorGroup ag
   * WHERE ag.idAuthorGroup = ? AND
   *       a.AuthorGroup_idAuthorGroup = ag.idAuthorGroup;
   * </pre>
   */
  private final static String __SELECT_AUTHORS = "SELECT a.* "
          + "FROM Author a, AuthorGroup ag "
          + "WHERE ag.idAuthorGroup = ? AND "
          + "      a.AuthorGroup_idAuthorGroup = ag.idAuthorGroup;";
  private final static String __SELECT_AUTHOR_IDS = "SELECT a.idAuthor "
          + "FROM Author a, AuthorGroup ag "
          + "WHERE ag.idAuthorGroup = ? AND "
          + "      a.AuthorGroup_idAuthorGroup = ag.idAuthorGroup;";
  
  private final static String __SELECT_AUTHORGROUP_BY_ID = "SELECT * FROM AuthorGroup WHERE idAuthorGroup = ?;";
  private final static String __SELECT_AUTHORGROUP_BY_GROUPNAME = "SELECT * FROM AuthorGroup WHERE groupName = ?;";
  private final static String __SELECT_AUTHORGROUP_BY_STOPGROUP = "SELECT * FROM AuthorGroup WHERE stopGroup = ?;";
  private final static String __SELECT_AUTHORGROUPS = "SELECT * FROM AuthorGroup;";
  private final static String __SELECT_AUTHORGROUP_IDS = "SELECT idAuthorGroup FROM AuthorGroup;";
  private final static String __CHECK_AUTHORGROUP_BY_GROUPNAME = "SELECT idAuthorGroup FROM AuthorGroup WHERE groupName = ?;";
  private final static String __CHECK_AUTHORGROUP_BY_ID = "SELECT idAuthorGroup FROM AuthorGroup WHERE idAuthorGroup = ?;";
  
  private PreparedStatement statCheckAuthorGroupByGroupName;
  private PreparedStatement statCheckAuthorGroupById;
  private PreparedStatement statAddAuthorGroup;
  private PreparedStatement statAddAuthorGroupWithId;
  private PreparedStatement statRemoveAuthorGroup;
  private PreparedStatement statSelectAuthors;
  private PreparedStatement statSelectAuthorGroups;
  private PreparedStatement statSelectAuthorGroupByGroupName;
  private PreparedStatement statSelectAuthorGroupById;
  private PreparedStatement statSelectAuthorGroupsByStopGroup;
  private PreparedStatement statSelectAuthorGroupIds;
  private PreparedStatement statSelectAuthorIds;
  private PreparedStatement statUpdateAuthorGroup;
  private PreparedStatement statUpdateGroupName;
  private PreparedStatement statUpdateStopGroup;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  /**
   *
   * @param kbm
   */
  public AuthorGroupDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {

    this.kbm = kbm;
    
    try {
    
      this.statCheckAuthorGroupByGroupName = this.kbm.getConnection().prepareStatement(__CHECK_AUTHORGROUP_BY_GROUPNAME);
      this.statCheckAuthorGroupById = this.kbm.getConnection().prepareStatement(__CHECK_AUTHORGROUP_BY_ID);
      this.statAddAuthorGroup = this.kbm.getConnection().prepareStatement(__INSERT_AUTHORGROUP, Statement.RETURN_GENERATED_KEYS);
      this.statAddAuthorGroupWithId = this.kbm.getConnection().prepareStatement(__INSERT_AUTHORGROUP_WITH_ID);
      this.statRemoveAuthorGroup = this.kbm.getConnection().prepareStatement(__REMOVE_AUTHORGROUP);
      this.statSelectAuthors = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORS);
      this.statSelectAuthorGroups = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORGROUPS);
      this.statSelectAuthorGroupByGroupName = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORGROUP_BY_GROUPNAME);
      this.statSelectAuthorGroupById = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORGROUP_BY_ID);
      this.statSelectAuthorGroupsByStopGroup = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORGROUP_BY_STOPGROUP);
      this.statSelectAuthorGroupIds = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORGROUP_IDS);
      this.statSelectAuthorIds = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR_IDS);
      this.statUpdateAuthorGroup = this.kbm.getConnection().prepareStatement(__UPDATE_AUTHORGROUP);
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
  public Integer addAuthorGroup(String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddAuthorGroup.clearParameters();

      this.statAddAuthorGroup.setString(1, groupName);
      this.statAddAuthorGroup.setBoolean(2, stopGroup);

      if (this.statAddAuthorGroup.executeUpdate() == 1) {

        id = this.statAddAuthorGroup.getGeneratedKeys().getInt(1);
        this.statAddAuthorGroup.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAuthorGroupEvent(getAuthorGroup(id)));
    }

    return id;
  }

  /**
   *
   * @param authorGroupID
   * @param groupName
   * @param stopGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addAuthorGroup(Integer authorGroupID, String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statAddAuthorGroupWithId.clearParameters();

      this.statAddAuthorGroupWithId.setInt(1, authorGroupID);
      this.statAddAuthorGroupWithId.setString(2, groupName);
      this.statAddAuthorGroupWithId.setBoolean(3, stopGroup);

      result = this.statAddAuthorGroupWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAuthorGroupEvent(getAuthorGroup(authorGroupID)));
    }

    return result;
  }

  /**
   * 
   * @param authorGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addAuthorGroup(AuthorGroup authorGroup, boolean notifyObservers) throws KnowledgeBaseException {

    return addAuthorGroup(authorGroup.getAuthorGroupID(),
            authorGroup.getGroupName(),
            authorGroup.isStopGroup(),
            notifyObservers);
  }

  /**
   *
   * @param authorGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeAuthorGroup(Integer authorGroupID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    AuthorGroup authorGroup = null;
    ArrayList<Author> authors = null;

    // Save the information before remove
    if (notifyObservers) {

      authorGroup = getAuthorGroup(authorGroupID);
      authors = getAuthors(authorGroupID);
    }

    try {

      this.statRemoveAuthorGroup.clearParameters();

      this.statRemoveAuthorGroup.setInt(1, authorGroupID);

      result = this.statRemoveAuthorGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveAuthorGroupEvent(authorGroup));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AuthorGroupRelationAuthorEvent());
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAuthorWithoutGroupEvent(authors));
    }

    return result;
  }

  /**
   *
   * @param idAuthorGroup the authors group's ID
   *
   * @return an <ocde>AuthorGroup</code> or null if there is not any authors
   *         group with this ID
   *
   * @throws KnowledgeBaseException
   */
  public AuthorGroup getAuthorGroup(Integer idAuthorGroup)
          throws KnowledgeBaseException {

    ResultSet rs;
    AuthorGroup authorGroup = null;

    try {

      this.statSelectAuthorGroupById.clearParameters();

      this.statSelectAuthorGroupById.setInt(1, idAuthorGroup);

      rs = this.statSelectAuthorGroupById.executeQuery();

      if (rs.next()) {

        authorGroup = UtilsDAO.getInstance().getAuthorGroup(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorGroup;
  }

  /**
   *
   * @param groupName the authors group's name
   *
   * @return an <ocde>AuthorGroup</code> or null if there is not any authors
   *         group with this ID
   *
   * @throws KnowledgeBaseException
   */
  public AuthorGroup getAuthorGroup(String groupName)
          throws KnowledgeBaseException {

    ResultSet rs;
    AuthorGroup authorGroup = null;

    try {

      this.statSelectAuthorGroupByGroupName.clearParameters();

      this.statSelectAuthorGroupByGroupName.setString(1, groupName);

      rs = this.statSelectAuthorGroupByGroupName.executeQuery();

      if (rs.next()) {

        authorGroup = UtilsDAO.getInstance().getAuthorGroup(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorGroup;

  }

  /**
   *
   * @param stopGroup
   * @return
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<AuthorGroup> getAuthorGroups(boolean stopGroup)
          throws KnowledgeBaseException {

    ArrayList<AuthorGroup> authorGroupList = new ArrayList<AuthorGroup>();
    ResultSet rs;
    AuthorGroup authorGroup = null;

    try {

      this.statSelectAuthorGroupsByStopGroup.clearParameters();

      this.statSelectAuthorGroupsByStopGroup.setBoolean(1, stopGroup);

      rs = this.statSelectAuthorGroupsByStopGroup.executeQuery();

      while (rs.next()) {

        authorGroup = UtilsDAO.getInstance().getAuthorGroup(rs);

        authorGroupList.add(authorGroup);
      }

      rs.close();
      

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorGroupList;

  }

  /**
   *
   * @return
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<AuthorGroup> getAuthorGroups() throws KnowledgeBaseException {

    ArrayList<AuthorGroup> authorGroupList = new ArrayList<AuthorGroup>();
    ResultSet rs;
    AuthorGroup authorGroup = null;

    try {

      this.statSelectAuthorGroups.clearParameters();

      rs = this.statSelectAuthorGroups.executeQuery();

      while (rs.next()) {

        authorGroup = UtilsDAO.getInstance().getAuthorGroup(rs);

        authorGroupList.add(authorGroup);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorGroupList;

  }

  /**
   * 
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Integer> getAuthorGroupIDs() throws KnowledgeBaseException {

    ArrayList<Integer> authorGroupList = new ArrayList<Integer>();
    ResultSet rs;

    try {

      this.statSelectAuthorGroupIds.clearParameters();

      rs = this.statSelectAuthorGroupIds.executeQuery();

      while (rs.next()) {

        authorGroupList.add(UtilsDAO.getInstance().getAuthorGroupID(rs));
      }

      rs.close();
      

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorGroupList;

  }

  /**
   * 
   * @param authorGroupID
   * @param goupName
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setGroupName(Integer authorGroupID, String goupName, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statUpdateGroupName.clearParameters();

      this.statUpdateGroupName.setString(1, goupName);
      this.statUpdateGroupName.setInt(2, authorGroupID);

      result = this.statUpdateGroupName.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorGroupEvent(getAuthorGroup(authorGroupID)));
    }

    return result;
  }

  /**
   * 
   * @param authorGroupID
   * @param stopGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setStopGroup(Integer authorGroupID, boolean stopGroup, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statUpdateStopGroup.clearParameters();

      this.statUpdateStopGroup.setBoolean(1, stopGroup);
      this.statUpdateStopGroup.setInt(2, authorGroupID);

      result = this.statUpdateStopGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorGroupEvent(getAuthorGroup(authorGroupID)));
    }

    return result;
  }

  /**
   *
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean updateAuthorGroup(Integer authorGroupID, String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statUpdateAuthorGroup.clearParameters();

      this.statUpdateAuthorGroup.setString(1, groupName);
      this.statUpdateAuthorGroup.setBoolean(2, stopGroup);
      this.statUpdateAuthorGroup.setInt(3, authorGroupID);

      result = this.statUpdateAuthorGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorGroupEvent(getAuthorGroup(authorGroupID)));
    }

    return result;
  }

  /**
   * 
   * @param authorGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public ArrayList<Author> getAuthors(Integer authorGroupID)
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Author> authorsList = new ArrayList<Author>();

    try {

      this.statSelectAuthors.clearParameters();

      this.statSelectAuthors.setInt(1, authorGroupID);

      rs = this.statSelectAuthors.executeQuery();

      while (rs.next()) {

        authorsList.add(UtilsDAO.getInstance().getAuthor(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorsList;
  }

  /**
   * 
   * @param authorGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public ArrayList<Integer> getAuthorIDs(Integer authorGroupID)
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Integer> authorsList = new ArrayList<Integer>();

    try {

      this.statSelectAuthorIds.clearParameters();

      this.statSelectAuthorIds.setInt(1, authorGroupID);

      rs = this.statSelectAuthorIds.executeQuery();

      while (rs.next()) {

        authorsList.add(UtilsDAO.getInstance().getAuthorID(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorsList;
  }

  /**
   * <p>Check if there is an <code>AuthorGroup</code> with this group's
   * name.</p>
   *
   * @param groupName a string with the group's name
   *
   * @return true if there is an <code>AuthorGroup</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkAuthorGroup(String groupName)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckAuthorGroupByGroupName.clearParameters();

      this.statCheckAuthorGroupByGroupName.setString(1, groupName);

      rs = this.statCheckAuthorGroupByGroupName.executeQuery();

      result = rs.next();

      rs.close();

      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>AuthorGroup</code> with this ID.</p>
   *
   * @param idAuthorsGroup the author's group ID
   *
   * @return true if there is an <code>Author</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkAuthorGroup(Integer idAuthorsGroup)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckAuthorGroupById.clearParameters();

      this.statCheckAuthorGroupById.setInt(1, idAuthorsGroup);

      rs = this.statCheckAuthorGroupById.executeQuery();

      result = rs.next();

      rs.close();

      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * 
   * @param authorGroupsToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<AuthorGroup> refreshAuthorGroups(ArrayList<AuthorGroup> authorGroupsToRefresh) throws KnowledgeBaseException {

    int i;
    String query;
    ResultSet rs;
    ArrayList<AuthorGroup> authorGroups = new ArrayList<AuthorGroup>();

    i = 0;

    if (!authorGroupsToRefresh.isEmpty()) {

      query = "SELECT * FROM AuthorGroup WHERE idAuthorGroup IN (" + authorGroupsToRefresh.get(i).getAuthorGroupID();

      for (i = 1; i < authorGroupsToRefresh.size(); i++) {

        query += ", " + authorGroupsToRefresh.get(i).getAuthorGroupID();
      }

      query += ");";

      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          authorGroups.add(UtilsDAO.getInstance().getAuthorGroup(rs));
        }

        rs.close();
        stat.close();
        

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }

    return authorGroups;
  }

  /**
   * 
   * @param authorGroupToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public AuthorGroup refreshAuthorGroup(AuthorGroup authorGroupToRefresh) throws KnowledgeBaseException {

    return getAuthorGroup(authorGroupToRefresh.getAuthorGroupID());
  }
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
