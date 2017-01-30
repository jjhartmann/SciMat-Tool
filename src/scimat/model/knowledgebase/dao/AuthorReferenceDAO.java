/*
 * AuthorReferenceDAO.java
 *
 * Created on 04-nov-2010, 13:43:02
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.add.AddAuthorReferenceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.relation.AuthorReferenceGroupRelationAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.relation.AuthorRelationAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.relation.ReferenceRelationAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.remove.RemoveAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.remove.RemoveAuthorReferenceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceWithoutGroupEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceReference;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;
  /**
   * <pre>
   * INSERT INTO AuthorReference(authorName) VALUES(?);
   * </pre>
   */
  private static final String __INSERT_AUTHROR_REFERENCE = "INSERT INTO AuthorReference(authorName) VALUES(?);";
  /**
   * <pre>
   * INSERT INTO AuthorReference(idAuthorRefernce, authorName) VALUES(?,?);
   * </pre>
   */
  private static final String __INSERT_AUTHROR_REFERENCE_WITH_ID = "INSERT INTO AuthorReference(idAuthorReference, authorName) VALUES(?,?);";
  /**
   * <pre>
   * DELETE AuthorReference
   * WHERE idAuthorReference = ?;
   * </pre>
   */
  private final static String __REMOVE_AUTHORREFERENCE = "DELETE FROM AuthorReference "
          + "WHERE idAuthorReference = ?;";
  /**
   * <pre>
   * UPDATE AuthorReference "
   * SET authorName = ? "
   * WHERE idAuthorReference = ?;
   * </pre>
   */
  private static final String __UPDATE_AUTHORNAME = "UPDATE AuthorReference "
          + "SET authorName = ? "
          + "WHERE idAuthorReference = ?;";
  /**
   * <pre>
   * UPDATE AuthorReference "
   * SET AuthorReferenceGroup_idAuthorReferenceGroup = ? "
   * WHERE idAuthorReference = ?;
   * </pre>
   */
  private static final String __UPDATE_AUTHORREFERENCEGROUP = "UPDATE AuthorReference "
          + "SET AuthorReferenceGroup_idAuthorReferenceGroup = ? "
          + "WHERE idAuthorReference = ?;";
  /**
   * <pre>
   * UPDATE AuthorReference "
   * SET Author_idAuthor = ? "
   * WHERE idAuthorReference = ?;
   * </pre>
   */
  private static final String __UPDATE_AUTHOR = "UPDATE AuthorReference "
          + "SET Author_idAuthor = ? "
          + "WHERE idAuthorReference = ?;";
  /**
   * <pre>
   * SELECT arg.idAuthorReferenceGroup, arg.groupName, arg.stopGroup
   * FROM AuthorReference ar, AuthorReferenceGroup arg
   * WHERE ar.idAuthorReference = ? AND 
   *       ar.AuthorReferenceGroup_idAuthorReferenceGroup = arg.idAuthorReferenceGroup;
   * </pre>
   */
  private final static String __SELECT_AUTHORREFERENCEGROUP = "SELECT arg.* "
          + "FROM AuthorReference ar, AuthorReferenceGroup arg "
          + "WHERE ar.idAuthorReference = ? AND "
          + "      ar.AuthorReferenceGroup_idAuthorReferenceGroup = arg.idAuthorReferenceGroup;";
  private final static String __SELECT_AUTHORREFERENCEGROUP_ID = "SELECT arg.idAuthorReferenceGroup "
          + "FROM AuthorReference ar, AuthorReferenceGroup arg "
          + "WHERE ar.idAuthorReference = ? AND "
          + "      ar.AuthorReferenceGroup_idAuthorReferenceGroup = arg.idAuthorReferenceGroup;";
  /**
   * <pre>
   * ELECT a.idAuthor, a.authorName, a.fullAuthorName
   * FROM AuthorReference ar, Author a
   * WHERE ar.idAuthorReference = ? AND ar.Author_idAuthor = a.idAuthor;
   * </pre>
   */
  private final static String __SELECT_AUTHOR = "SELECT a.* "
          + "FROM AuthorReference ar, Author a "
          + "WHERE ar.idAuthorReference = ? AND "
          + "      ar.Author_idAuthor = a.idAuthor;";
  private final static String __SELECT_AUTHOR_ID = "SELECT a.idAuthor "
          + "FROM AuthorReference ar, Author a "
          + "WHERE ar.idAuthorReference = ? AND "
          + "      ar.Author_idAuthor = a.idAuthor;";
  /*private final static String __SELECT_AUTHOR_WITHOUTGROUP = "SELECT a.* "
          + "FROM AuthorReference ar, Author a "
          + "WHERE ar.idAuthorReference = ? AND "
          + "      ar.Author_idAuthor = a.idAuthor AND"
          + "      ar.AuthorReferenceGroup_idAuthorReferenceGroup ISNULL;";
  private final static String __SELECT_AUTHOR_WITHOUTGROUP_ID = "SELECT a.idAuthor "
          + "FROM AuthorReference ar, Author a "
          + "WHERE ar.idAuthorReference = ? AND "
          + "      ar.Author_idAuthor = a.idAuthor AND"
          + "      ar.AuthorReferenceGroup_idAuthorReferenceGroup ISNULL;";*/
  /**
   * <pre>
   * SELECT r.idReference, r.fullReference, r.volume, r.issue, r.page, r.doi, r.format, r.year,
   *        ar.idAuthorReference, ar.authorName,
   *        arr.position
   * FROM AuthorReference_Reference arr, AuthorReference ar, Reference r
   * WHERE ar.idAuthorReference = ? AND
   *       ar.AuthorReference_idAuthorReference = arr.AuthorReference_idAuthorReference AND
   *       arr.Reference_idReference = r.idReference;
   * </pre>
   */
  private final static String __SELECT_AUTHORREFERENCEREFERENCE = "SELECT r.*, ar.*, arr.position "
          + "FROM AuthorReference_Reference arr, AuthorReference ar, Reference r "
          + "WHERE ar.idAuthorReference = ? AND "
          + "      ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND "
          + "      arr.Reference_idReference = r.idReference;";
  private final static String __SELECT_AUTHORREFERENCE_BY_ID = "SELECT * FROM AuthorReference WHERE idAuthorReference = ?;";
  private final static String __SELECT_AUTHORREFERENCE_BY_NAME = "SELECT * FROM AuthorReference WHERE authorName = ?;";
  private final static String __SELECT_AUTHORREFERENCES = "SELECT * FROM AuthorReference;";
  private final static String __SELECT_AUTHORREFERENCE_IDS = "SELECT idAuthorReference FROM AuthorReference;";
  private final static String __SELECT_AUTHORREFERENCES_WITHOUTGROUP = "SELECT * FROM AuthorReference WHERE AuthorReferenceGroup_idAuthorReferenceGroup IS NULL;";
  private final static String __SELECT_AUTHORREFERENCE_WITHOUTGROUP_IDS = "SELECT idAuthorReference FROM AuthorReference WHERE AuthorReferenceGroup_idAuthorReferenceGroup IS NULL;";
  private final static String __CHECK_AUTHORREFEFENCE_BY_NAME = "SELECT idAuthorReference FROM AuthorReference WHERE authorName = ?;";
  private final static String __CHECK_AUTHORREFERENCE_BY_ID = "SELECT idAuthorReference FROM AuthorReference WHERE idAuthorReference = ?;";
  
  private PreparedStatement statCheckAuthorReferenceByName;
  private PreparedStatement statCheckAuthorReferenceById;
  private PreparedStatement statAddAuthorReference;
  private PreparedStatement statAddAuthorReferenceWithId;
  private PreparedStatement statRemoveAuthorReference;
  private PreparedStatement statSelectAuthor;
  private PreparedStatement statSelectAuthorId;
  private PreparedStatement statSelectAuthorReferenceGroup;
  private PreparedStatement statSelectAuthorReferenceGroupId;
  private PreparedStatement statSelectAuthorReferenceReference;
  private PreparedStatement statSelectAuthorReferences;
  private PreparedStatement statSelectAuthorReferencesWithoutGroup;
  private PreparedStatement statSelectAuthorReferenceById;
  private PreparedStatement statSelectAuthorReferenceByName;
  private PreparedStatement statSelectAuthorReferenceIds;
  private PreparedStatement statSelectAuthorReferenceWithoutGroupIds;
  private PreparedStatement statUpdateAuthorName;
  private PreparedStatement statUpdateAuthor;
  private PreparedStatement statUpdateAuthorReferenceGroup;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  /**
   * 
   * @param kbm
   */
  public AuthorReferenceDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException{

    this.kbm = kbm;

    try {

      this.statCheckAuthorReferenceByName = this.kbm.getConnection().prepareStatement(__CHECK_AUTHORREFEFENCE_BY_NAME);
      this.statCheckAuthorReferenceById = this.kbm.getConnection().prepareStatement(__CHECK_AUTHORREFERENCE_BY_ID);
      this.statAddAuthorReference = this.kbm.getConnection().prepareStatement(__INSERT_AUTHROR_REFERENCE, Statement.RETURN_GENERATED_KEYS);
      this.statAddAuthorReferenceWithId = this.kbm.getConnection().prepareStatement(__INSERT_AUTHROR_REFERENCE_WITH_ID);
      this.statRemoveAuthorReference = this.kbm.getConnection().prepareStatement(__REMOVE_AUTHORREFERENCE);
      this.statSelectAuthor = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR);
      this.statSelectAuthorId = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR_ID);
      this.statSelectAuthorReferenceGroup = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCEGROUP);
      this.statSelectAuthorReferenceGroupId = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCEGROUP_ID);
      this.statSelectAuthorReferenceReference = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCEREFERENCE);
      this.statSelectAuthorReferences = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCES);
      this.statSelectAuthorReferencesWithoutGroup = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCES_WITHOUTGROUP);
      this.statSelectAuthorReferenceById = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCE_BY_ID);
      this.statSelectAuthorReferenceByName = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCE_BY_NAME);
      this.statSelectAuthorReferenceIds = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCE_IDS);
      this.statSelectAuthorReferenceWithoutGroupIds = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCE_WITHOUTGROUP_IDS);
      this.statUpdateAuthorName = this.kbm.getConnection().prepareStatement(__UPDATE_AUTHORNAME);
      this.statUpdateAuthor = this.kbm.getConnection().prepareStatement(__UPDATE_AUTHOR);
      this.statUpdateAuthorReferenceGroup = this.kbm.getConnection().prepareStatement(__UPDATE_AUTHORREFERENCEGROUP);

    } catch (SQLException e) {
      
      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  /**
   *
   * @param authorName
   * @return
   * @throws KnowledgeBaseException
   */
  public Integer addAuthorReference(String authorName, boolean notifyObservers)
          throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddAuthorReference.clearParameters();

      this.statAddAuthorReference.setString(1, authorName);

      if (this.statAddAuthorReference.executeUpdate() == 1) {

        id = this.statAddAuthorReference.getGeneratedKeys().getInt(1);
        this.statAddAuthorReference.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAuthorReferenceEvent(getAuthorReference(id)));
    }

    return id;
  }

  /**
   * 
   * @param authorReferenceID
   * @param authorName
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addAuthorReference(Integer authorReferenceID, String authorName, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statAddAuthorReferenceWithId.clearParameters();

      this.statAddAuthorReferenceWithId.setInt(1, authorReferenceID);
      this.statAddAuthorReferenceWithId.setString(2, authorName);

      result = this.statAddAuthorReferenceWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAuthorReferenceEvent(getAuthorReference(authorReferenceID)));
    }

    return result;
  }

  /**
   * 
   * @param authorReference
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addAuthorReference(AuthorReference authorReference, boolean notifyObservers)
          throws KnowledgeBaseException {

    return addAuthorReference(authorReference.getAuthorReferenceID(),
            authorReference.getAuthorName(),
            notifyObservers);
  }

  /**
   *
   * @param authorReferenceID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeAuthorReference(Integer authorReferenceID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    AuthorReference authorReference = null;
    AuthorReferenceGroup authorReferenceGroup = null;

    // Save the information before remove
    if (notifyObservers) {

      authorReference = getAuthorReference(authorReferenceID);
      authorReferenceGroup = getAuthorReferenceGroup(authorReferenceID);
    }

    try {

      this.statRemoveAuthorReference.clearParameters();

      this.statRemoveAuthorReference.setInt(1, authorReferenceID);

      result = this.statRemoveAuthorReference.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveAuthorReferenceEvent(authorReference));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceRelationAuthorReferenceEvent());

      if (authorReferenceGroup != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceGroupEvent(CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().refreshAuthorReferenceGroup(authorReferenceGroup)));
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new AuthorReferenceGroupRelationAuthorReferenceEvent());

      } else {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveAuthorReferenceWithoutGroupEvent(authorReference));
      }
    }

    return result;
  }

  /**
   *
   * @param idAuthorReference the author reference's ID
   *
   * @return an <ocde>AuthorReference</code> or null if there is not any author
   *         reference with this ID
   *
   * @throws KnowledgeBaseException
   */
  public AuthorReference getAuthorReference(Integer idAuthorReference)
          throws KnowledgeBaseException {

    ResultSet rs;
    AuthorReference authorReference = null;

    try {

      this.statSelectAuthorReferenceById.clearParameters();

      this.statSelectAuthorReferenceById.setInt(1, idAuthorReference);

      rs = this.statSelectAuthorReferenceById.executeQuery();

      if (rs.next()) {

        authorReference = UtilsDAO.getInstance().getAuthorReference(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReference;
  }

  /**
   *
   * @param authorName the author reference's name
   *
   * @return an <ocde>AuthorReference</code> or null if there is not any author
   *         reference with this author's name
   *
   * @throws KnowledgeBaseException
   */
  public AuthorReference getAuthorReference(String authorName)
          throws KnowledgeBaseException {

    ResultSet rs;
    AuthorReference authorReference = null;

    try {

      this.statSelectAuthorReferenceByName.clearParameters();

      this.statSelectAuthorReferenceByName.setString(1, authorName);

      rs = this.statSelectAuthorReferenceByName.executeQuery();

      if (rs.next()) {

        authorReference = UtilsDAO.getInstance().getAuthorReference(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReference;
  }

  /**
   *
   * @return an <ocde>AuthorReference</code> or null if there is not any author
   *         reference with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<AuthorReference> getAuthorReferences()
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<AuthorReference> authorReferenceList = new ArrayList<AuthorReference>();

    try {

      this.statSelectAuthorReferences.clearParameters();

      rs = this.statSelectAuthorReferences.executeQuery();

      while (rs.next()) {

        authorReferenceList.add(UtilsDAO.getInstance().getAuthorReference(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferenceList;
  }

  /**
   * 
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Integer> getAuthorReferenceIDs()
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Integer> authorReferenceList = new ArrayList<Integer>();

    try {

      this.statSelectAuthorReferenceIds.clearParameters();

      rs = this.statSelectAuthorReferenceIds.executeQuery();

      while (rs.next()) {

        authorReferenceList.add(UtilsDAO.getInstance().getAuthorReferenceID(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferenceList;
  }

  /**
   *
   * @return an <ocde>AuthorReference</code> or null if there is not any author
   *         reference with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<AuthorReference> getAuthorReferencesWithoutGroup()
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<AuthorReference> authorReferenceList = new ArrayList<AuthorReference>();

    try {

      this.statSelectAuthorReferencesWithoutGroup.clearParameters();

      rs = this.statSelectAuthorReferencesWithoutGroup.executeQuery();

      while (rs.next()) {

        authorReferenceList.add(UtilsDAO.getInstance().getAuthorReference(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferenceList;
  }

  /**
   * 
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Integer> getAuthorReferenceIDsWithoutGroup()
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Integer> authorReferenceList = new ArrayList<Integer>();

    try {

      this.statSelectAuthorReferenceWithoutGroupIds.clearParameters();

      rs = this.statSelectAuthorReferenceWithoutGroupIds.executeQuery();

      while (rs.next()) {

        authorReferenceList.add(UtilsDAO.getInstance().getAuthorReferenceID(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferenceList;
  }

  /**
   * @param authorName the authorName to set
   */
  public boolean setAuthorName(Integer authorReferenceID, String authorName, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    AuthorReference authorReference;

    try {

      this.statUpdateAuthorName.clearParameters();

      this.statUpdateAuthorName.setString(1, authorName);
      this.statUpdateAuthorName.setInt(2, authorReferenceID);

      result = this.statUpdateAuthorName.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      authorReference = getAuthorReference(authorReferenceID);

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceEvent(authorReference));

      if (getAuthorReferenceGroup(authorReferenceID) == null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceWithoutGroupEvent(authorReference));
      }
    }

    return result;
  }

  /**
   * 
   * @param authorReferenceID
   * @param authorReferenceGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setAuthorReferenceGroup(Integer authorReferenceID, Integer authorReferenceGroupID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    AuthorReferenceGroup oldAuthorReferenceGroup = null;

    // Notify to the observer
    if (notifyObservers) {

      oldAuthorReferenceGroup = getAuthorReferenceGroup(authorReferenceID);
    }

    try {

      this.statUpdateAuthorReferenceGroup.clearParameters();

      if (authorReferenceGroupID != null) {

        this.statUpdateAuthorReferenceGroup.setInt(1, authorReferenceGroupID);

      } else {

        this.statUpdateAuthorReferenceGroup.setNull(1, Types.NULL);
      }

      this.statUpdateAuthorReferenceGroup.setInt(2, authorReferenceID);

      result = this.statUpdateAuthorReferenceGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      if (oldAuthorReferenceGroup != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceGroupEvent(CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO().refreshAuthorReferenceGroup(oldAuthorReferenceGroup)));

        if (authorReferenceGroupID == null) {

          KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAuthorReferenceWithoutGroupEvent(getAuthorReference(authorReferenceID)));
        }

      } else {

        if (authorReferenceGroupID != null) {

          KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveAuthorReferenceWithoutGroupEvent(getAuthorReference(authorReferenceID)));
        }
      }

      if (authorReferenceGroupID != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceGroupEvent(getAuthorReferenceGroup(authorReferenceID)));
      }

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AuthorReferenceGroupRelationAuthorReferenceEvent());
    }

    return result;
  }

  /**
   * 
   * @param authorReferenceID
   * @param authorID
   * @throws KnowledgeBaseException
   */
  public boolean setAuthor(Integer authorReferenceID, Integer authorID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statUpdateAuthor.clearParameters();

      this.statUpdateAuthor.setInt(1, authorID);
      this.statUpdateAuthor.setInt(2, authorReferenceID);

      result = this.statUpdateAuthor.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorEvent(getAuthor(authorReferenceID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AuthorRelationAuthorReferenceEvent());
    }

    return result;
  }

  /**
   * @return the idAuthorReferenceGroup
   */
  public AuthorReferenceGroup getAuthorReferenceGroup(Integer authorReferenceID)
          throws KnowledgeBaseException {

    ResultSet rs;
    AuthorReferenceGroup authorReferenceGroup = null;

    try {

      this.statSelectAuthorReferenceGroup.clearParameters();

      this.statSelectAuthorReferenceGroup.setInt(1, authorReferenceID);

      rs = this.statSelectAuthorReferenceGroup.executeQuery();

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
   * @return the idAuthorReferenceGroup
   */
  public Integer getAuthorReferenceGroupID(Integer authorReferenceID)
          throws KnowledgeBaseException {

    ResultSet rs;
    Integer authorReferenceGroupID = null;

    try {

      this.statSelectAuthorReferenceGroupId.clearParameters();

      this.statSelectAuthorReferenceGroupId.setInt(1, authorReferenceID);

      rs = this.statSelectAuthorReferenceGroupId.executeQuery();

      if (rs.next()) {

        authorReferenceGroupID = UtilsDAO.getInstance().getAuthorReferenceGroupID(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferenceGroupID;
  }

  /**
   * 
   * @param authorReferenceID
   * @return
   * @throws KnowledgeBaseException
   */
  public Author getAuthor(Integer authorReferenceID) throws KnowledgeBaseException {

    ResultSet rs;
    Author author = null;

    try {

      this.statSelectAuthor.clearParameters();

      this.statSelectAuthor.setInt(1, authorReferenceID);

      rs = this.statSelectAuthor.executeQuery();

      if (rs.next()) {



        author = UtilsDAO.getInstance().getAuthor(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return author;
  }

  /**
   * 
   * @param authorReferenceID
   * @return
   * @throws KnowledgeBaseException
   */
  public Integer getAuthorID(Integer authorReferenceID) throws KnowledgeBaseException {

    ResultSet rs;
    Integer authorID = null;

    try {

      this.statSelectAuthorId.clearParameters();

      this.statSelectAuthorId.setInt(1, authorReferenceID);

      rs = this.statSelectAuthorId.executeQuery();

      if (rs.next()) {

        authorID = UtilsDAO.getInstance().getAuthorID(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorID;
  }

  /**
   * 
   * @param authorReferenceID
   * @return
   * @throws KnowledgeBaseException
   */
  public ArrayList<AuthorReferenceReference> getReferences(Integer authorReferenceID)
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<AuthorReferenceReference> referencesList = new ArrayList<AuthorReferenceReference>();

    try {

      this.statSelectAuthorReferenceReference.clearParameters();

      this.statSelectAuthorReferenceReference.setInt(1, authorReferenceID);

      rs = this.statSelectAuthorReferenceReference.executeQuery();

      while (rs.next()) {

        referencesList.add(UtilsDAO.getInstance().getAuthorReferenceReference(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referencesList;
  }

  /**
   * <p>Check if there is an <code>AuthorReference</code> with this
   * author reference's name.</p>
   *
   * @param authorName a string with the author reference's name
   *
   * @return true if there is an <code>AuthorReference</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkAuthorReference(String authorName)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckAuthorReferenceByName.clearParameters();

      this.statCheckAuthorReferenceByName.setString(1, authorName);

      rs = this.statCheckAuthorReferenceByName.executeQuery();

      result = rs.next();

      rs.close();

      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>AuthorReference</code> with this ID.</p>
   *
   * @param idAuthorReference the author reference's ID
   *
   * @return true if there is an <code>AuthorReference</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkAuthorReference(Integer idAuthorReference)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckAuthorReferenceById.clearParameters();

      this.statCheckAuthorReferenceById.setInt(1, idAuthorReference);

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
   * @param authorReferencesToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<AuthorReference> refreshAuthorReferences(ArrayList<AuthorReference> authorReferencesToRefresh) throws KnowledgeBaseException {

    int i;
    String query;
    ResultSet rs;
    ArrayList<AuthorReference> authorReferences = new ArrayList<AuthorReference>();

    i = 0;

    if (!authorReferencesToRefresh.isEmpty()) {

      query = "SELECT * FROM AuthorReference WHERE idAuthorReference IN (" + authorReferencesToRefresh.get(i).getAuthorReferenceID();

      for (i = 1; i < authorReferencesToRefresh.size(); i++) {

        query += ", " + authorReferencesToRefresh.get(i).getAuthorReferenceID();
      }

      query += ");";

      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          authorReferences.add(UtilsDAO.getInstance().getAuthorReference(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }

    return authorReferences;
  }

  /**
   * 
   * @param authorReferenceToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public AuthorReference refreshAuthorReference(AuthorReference authorReferenceToRefresh) throws KnowledgeBaseException {

    return getAuthorReference(authorReferenceToRefresh.getAuthorReferenceID());
  }
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
