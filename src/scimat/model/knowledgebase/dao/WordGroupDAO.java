/*
 * WordGroupDAO.java
 *
 * Created on 21-oct-2010, 17:48:16
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddWordGroupEvent;
import scimat.knowledgebaseevents.event.add.AddWordWithoutGroupEvent;
import scimat.knowledgebaseevents.event.relation.WordGroupRelationWordEvent;
import scimat.knowledgebaseevents.event.remove.RemoveWordGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateWordGroupEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class WordGroupDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

 /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO WordGroup(groupName,stopGroup) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_WORDGROUP = "INSERT INTO WordGroup(groupName,stopGroup) VALUES(?,?);";

  /**
   * <pre>
   * INSERT INTO WordGroup(idWordGroup,groupName,stopGroup) VALUES(?,?,?);
   * </pre>
   */
  private final static String __INSERT_WORDGROUP_WITH_ID = "INSERT INTO WordGroup(idWordGroup,groupName,stopGroup) VALUES(?,?,?);";

  /**
   * <pre>
   * DELETE WordGroup
   * WHERE idWordGroup = ?;
   * </pre>
   */
  private final static String __REMOVE_WORDGROUP = "DELETE FROM WordGroup "
                                                 + "WHERE idWordGroup = ?;";

  /**
   * <pre>
   * UPDATE WordGroup
   * SET groupName = ?
   * WHERE idWordGroup = ?;
   * </pre>
   */
  private final static String __UPDATE_GROUPNAME = "UPDATE WordGroup "
                                                 + "SET groupName = ? "
                                                 + "WHERE idWordGroup = ?;";

  /**
   * <pre>
   * UPDATE WordGroup
   * SET groupName = ?,
   *     stopGroup = ?
   * WHERE idWordGroup = ?;
   * </pre>
   */
  private final static String __UPDATE_WORDGROUP = "UPDATE WordGroup "
                                                 + "SET groupName = ?, "
                                                 + "    stopGroup = ? "
                                                 + "WHERE idWordGroup = ?;";

  /**
   * <pre>
   * UPDATE WordGroup 
   * SET stopGroup = ?
   * WHERE idWordGroup = ?;
   * </pre>
   */
  private final static String __UPDATE_STOPGROUP = "UPDATE WordGroup "
                                                 + "SET stopGroup = ? "
                                                 + "WHERE idWordGroup = ?;";

  /**
   * <pre>
   * SELECT w.idWord, w.wordName
   * FROM Word w, WordGroup wg
   * WHERE wg.idWordGroup = ? AND w.WordGroup_idWordGroup = wg.idWordGroup;
   * </pre>
   */
  private final static String __SELECT_WORDS = "SELECT w.* "
                                            + "FROM Word w, WordGroup wg "
                                            + "WHERE wg.idWordGroup = ? AND w.WordGroup_idWordGroup = wg.idWordGroup;";
  
  private final static String __SELECT_WORDGROUP_BY_ID = "SELECT * FROM WordGroup WHERE idWordGroup = ?;";
  private final static String __SELECT_WORDGROUP_BY_GROUPNAME = "SELECT * FROM WordGroup WHERE groupName = ?;";
  private final static String __SELECT_WORDGROUP_BY_STOPGROUP = "SELECT * FROM WordGroup WHERE stopGroup = ?;";
  private final static String __SELECT_WORDGROUUPS = "SELECT * FROM WordGroup;";
  private final static String __CHECK_WORDGROUP_BY_GROUPNAME = "SELECT idWordGroup FROM WordGroup WHERE groupName = ?;";
  private final static String __CHECK_WORDGROUP_BY_ID = "SELECT idWordGroup FROM WordGroup WHERE idWordGroup = ?;";
  
  private PreparedStatement statCheckWordGroupByGroupName;
  private PreparedStatement statCheckWordGroupById;
  private PreparedStatement statAddWordGroup;
  private PreparedStatement statAddWordGroupWithId;
  private PreparedStatement statRemoveWordGroup;
  private PreparedStatement statSelectWords;
  private PreparedStatement statSelectWordGroupByGroupName;
  private PreparedStatement statSelectWordGroupById;
  private PreparedStatement statSelectWordGroupByStopGroup;
  private PreparedStatement statSelectWordGroups;
  private PreparedStatement statUpdateGroupName;
  private PreparedStatement statUpdateStopGroup;
  private PreparedStatement statUpdateWordGroup;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param kbm
   */
  public WordGroupDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    
    this.kbm = kbm;
    
    try {
    
      this.statCheckWordGroupByGroupName = this.kbm.getConnection().prepareStatement(__CHECK_WORDGROUP_BY_GROUPNAME);
      this.statCheckWordGroupById = this.kbm.getConnection().prepareStatement(__CHECK_WORDGROUP_BY_ID);
      this.statAddWordGroup = this.kbm.getConnection().prepareStatement(__INSERT_WORDGROUP, Statement.RETURN_GENERATED_KEYS);
      this.statAddWordGroupWithId = this.kbm.getConnection().prepareStatement(__INSERT_WORDGROUP_WITH_ID);
      this.statRemoveWordGroup = this.kbm.getConnection().prepareStatement(__REMOVE_WORDGROUP);
      this.statSelectWords = this.kbm.getConnection().prepareStatement(__SELECT_WORDS);
      this.statSelectWordGroupByGroupName = this.kbm.getConnection().prepareStatement(__SELECT_WORDGROUP_BY_GROUPNAME);
      this.statSelectWordGroupById = this.kbm.getConnection().prepareStatement(__SELECT_WORDGROUP_BY_ID);
      this.statSelectWordGroupByStopGroup = this.kbm.getConnection().prepareStatement(__SELECT_WORDGROUP_BY_STOPGROUP);
      this.statSelectWordGroups = this.kbm.getConnection().prepareStatement(__SELECT_WORDGROUUPS);
      this.statUpdateGroupName = this.kbm.getConnection().prepareStatement(__UPDATE_GROUPNAME);
      this.statUpdateStopGroup = this.kbm.getConnection().prepareStatement(__UPDATE_STOPGROUP);
      this.statUpdateWordGroup = this.kbm.getConnection().prepareStatement(__UPDATE_WORDGROUP);
      
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
  public Integer addWordGroup(String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException{

    Integer id;

    try {

      this.statAddWordGroup.clearParameters();

      this.statAddWordGroup.setString(1, groupName);
      this.statAddWordGroup.setBoolean(2, stopGroup);

      if (this.statAddWordGroup.executeUpdate() == 1 ) {

        id = this.statAddWordGroup.getGeneratedKeys().getInt(1);
        this.statAddWordGroup.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddWordGroupEvent(getWordGroup(id)));
    }

    return id;
  }

  /**
   *
   * @param wordGroupID
   * @param groupName
   * @param stopGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addWordGroup(Integer wordGroupID, String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statAddWordGroupWithId.clearParameters();

      this.statAddWordGroupWithId.setInt(1, wordGroupID);
      this.statAddWordGroupWithId.setString(2, groupName);
      this.statAddWordGroupWithId.setBoolean(3, stopGroup);

      result = this.statAddWordGroupWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddWordGroupEvent(getWordGroup(wordGroupID)));
    }

    return result;
  }

  /**
   * 
   * @param wordGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addWordGroup(WordGroup wordGroup, boolean notifyObservers) throws KnowledgeBaseException {

    return addWordGroup(wordGroup.getWordGroupID(),
                        wordGroup.getGroupName(),
                        wordGroup.isStopGroup(),
                        notifyObservers);
  }

  /**
   *
   * @param idWordGroup the words group's ID
   *
   * @return a <ocde>WordGroup</code> or null if there is not any words
   *         group with this ID
   *
   * @throws KnowledgeBaseException
   */
  public WordGroup getWordGroup(Integer idWordGroup)
          throws KnowledgeBaseException {

    ResultSet rs;
    WordGroup wordGroup = null;

    try {

      this.statSelectWordGroupById.clearParameters();

      this.statSelectWordGroupById.setInt(1, idWordGroup);

      rs = this.statSelectWordGroupById.executeQuery();

      if (rs.next()) {

        wordGroup = UtilsDAO.getInstance().getWordGroup(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return wordGroup;
  }

  /**
   *
   * @param groupName the words group's name
   *
   * @return a <ocde>WordGroup</code> or null if there is not any words
   *         group with this ID
   *
   * @throws KnowledgeBaseException
   */
  public WordGroup getWordGroup(String groupName)
          throws KnowledgeBaseException {

    ResultSet rs;
    WordGroup wordGroup = null;

    try {

      this.statSelectWordGroupByGroupName.clearParameters();

      this.statSelectWordGroupByGroupName.setString(1, groupName);

      rs = this.statSelectWordGroupByGroupName.executeQuery();

      if (rs.next()) {

        wordGroup = UtilsDAO.getInstance().getWordGroup(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return wordGroup;

  }

  /**
   *
   * @param stopGroup
   * @return
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<WordGroup> getWordGroups(boolean stopGroup)
          throws KnowledgeBaseException {

    ArrayList<WordGroup> wordGroupList = new ArrayList<WordGroup>();
    ResultSet rs;
    WordGroup wordGroup = null;

    try {

      this.statSelectWordGroupByStopGroup.clearParameters();

      this.statSelectWordGroupByStopGroup.setBoolean(1, stopGroup);

      rs = this.statSelectWordGroupByStopGroup.executeQuery();

      while (rs.next()) {

        wordGroup = UtilsDAO.getInstance().getWordGroup(rs);

        wordGroupList.add(wordGroup);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return wordGroupList;

  }

  /**
   *
   * @return
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<WordGroup> getWordGroups() throws KnowledgeBaseException {

    ArrayList<WordGroup> wordGroupList = new ArrayList<WordGroup>();
    ResultSet rs;
    WordGroup wordGroup = null;

    try {

      this.statSelectWordGroups.clearParameters();

      rs = this.statSelectWordGroups.executeQuery();

      while (rs.next()) {

        wordGroup = UtilsDAO.getInstance().getWordGroup(rs);

        wordGroupList.add(wordGroup);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return wordGroupList;

  }

  /**
   *
   * @param wordGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeWordGroup(Integer wordGroupID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    WordGroup wordGroup = null;
    ArrayList<Word> words = null;
    
    // Save the information before remove
    if (notifyObservers) {
      
      wordGroup = getWordGroup(wordGroupID);
      words = getWords(wordGroupID);
    }
    
    try {

      this.statRemoveWordGroup.clearParameters();

      this.statRemoveWordGroup.setInt(1, wordGroupID);

      result = this.statRemoveWordGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveWordGroupEvent(wordGroup));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new WordGroupRelationWordEvent());
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddWordWithoutGroupEvent(words));
    }

    return result;
  }

  /**
   *
   * @param wordGroupID
   * @param goupName
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setGroupName(Integer wordGroupID, String goupName, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statUpdateGroupName.clearParameters();

      this.statUpdateGroupName.setString(1, goupName);
      this.statUpdateGroupName.setInt(2, wordGroupID);

      result = this.statUpdateGroupName.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordGroupEvent(getWordGroup(wordGroupID)));
    }

    return result;
  }

  /**
   * 
   * @param wordGroupID
   * @param groupName
   * @param stopGroup
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean updateWordGroup(Integer wordGroupID, String groupName, boolean stopGroup, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statUpdateWordGroup.clearParameters();

      this.statUpdateWordGroup.setString(1, groupName);
      this.statUpdateWordGroup.setBoolean(2, stopGroup);
      this.statUpdateWordGroup.setInt(3, wordGroupID);

      result = this.statUpdateWordGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordGroupEvent(getWordGroup(wordGroupID)));
    }

    return result;
  }

  /**
   *
   * @param wordGroupID
   * @param stopGroup true if the group if a stop group. Else otherwise.
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setStopGroup(Integer wordGroupID, boolean stopGroup, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statUpdateStopGroup.clearParameters();

      this.statUpdateStopGroup.setBoolean(1, stopGroup);
      this.statUpdateStopGroup.setInt(2, wordGroupID);

      result = this.statUpdateStopGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordGroupEvent(getWordGroup(wordGroupID)));
    }

    return result;
  }

  /**
   *
   * @param referenceGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public ArrayList<Word> getWords(Integer wordGroupID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Word> wordsList = new ArrayList<Word>();

    try {

      this.statSelectWords.clearParameters();

      this.statSelectWords.setInt(1, wordGroupID);

      rs = this.statSelectWords.executeQuery();

      while (rs.next()) {

        wordsList.add(UtilsDAO.getInstance().getWord(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return wordsList;
  }

  /**
   * <p>Check if there is an <code>WordGroup</code> with this group's name.</p>
   *
   * @param groupName a string with the group's name
   *
   * @return true if there is an <code>WordGroup</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkWordGroup(String groupName) throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckWordGroupByGroupName.clearParameters();
      
      this.statCheckWordGroupByGroupName.setString(1, groupName);

      rs = this.statCheckWordGroupByGroupName.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>WordGroup</code> with this ID.</p>
   *
   * @param idWordGroup the group's ID
   *
   * @return true if there is an <code>WordGroup</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkWordGroup(Integer idWordGroup) throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckWordGroupById.clearParameters();

      this.statCheckWordGroupById.setInt(1, idWordGroup);

      rs = this.statCheckWordGroupById.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param wordGroupsToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<WordGroup> refreshWordGroups(ArrayList<WordGroup> wordGroupsToRefresh) throws KnowledgeBaseException {
  
    int i;
    String query;
    ResultSet rs;
    ArrayList<WordGroup> wordGroups = new ArrayList<WordGroup>();
    
    i = 0;
    
    if (!wordGroupsToRefresh.isEmpty()) {

      query = "SELECT * FROM WordGroup WHERE idWordGroup IN (" + wordGroupsToRefresh.get(i).getWordGroupID();
      
      for (i = 1; i < wordGroupsToRefresh.size(); i++) {
        
        query += ", " + wordGroupsToRefresh.get(i).getWordGroupID();
      }
      
      query += ");";
      
      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          wordGroups.add(UtilsDAO.getInstance().getWordGroup(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }
    
    return wordGroups;
  }
  
  /**
   * 
   * @param wordGroupToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public WordGroup refreshWordGroup(WordGroup wordGroupToRefresh) throws KnowledgeBaseException {
  
    return getWordGroup(wordGroupToRefresh.getWordGroupID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
