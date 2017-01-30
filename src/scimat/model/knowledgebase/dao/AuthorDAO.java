/*
 * AuthorDAO.java
 *
 * Created on 21-oct-2010, 17:48:42
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddAuthorEvent;
import scimat.knowledgebaseevents.event.add.AddAuthorWithoutGroupEvent;
import scimat.knowledgebaseevents.event.relation.AuthorGroupRelationAuthorEvent;
import scimat.knowledgebaseevents.event.relation.AuthorRelationAffiliationEvent;
import scimat.knowledgebaseevents.event.relation.DocumentRelationAuthorEvent;
import scimat.knowledgebaseevents.event.remove.RemoveAuthorEvent;
import scimat.knowledgebaseevents.event.remove.RemoveAuthorWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateAffiliationEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateDocumentEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.DocumentAuthor;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AuthorDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;
  /**
   * <pre>
   * INSERT INTO Author(authorName,fullAuthorName) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_AUTHOR = "INSERT INTO Author(authorName,fullAuthorName) VALUES(?,?);";
  /**
   * <pre>
   * INSERT INTO Author(idAuthor,authorName,fullAuthorName) VALUES(?,?,?);
   * </pre>
   */
  private final static String __INSERT_AUTHOR_WITH_ID = "INSERT INTO Author(idAuthor,authorName,fullAuthorName) VALUES(?,?,?);";
  /**
   * <pre>
   * DELETE Author
   * WHERE idAuthor = ?;
   * </pre>
   */
  private final static String __REMOVE_AUTHOR = "DELETE FROM Author "
          + "WHERE idAuthor = ?;";
  /**
   * <pre>
   * UPDATE Author
   * SET authorName = ?
   * WHERE idAuthor = ?;
   * </pre>
   */
  private final static String __UPDATE_AUTHORNAME = "UPDATE Author "
          + "SET authorName = ? "
          + "WHERE idAuthor = ?;";
  /**
   * <pre>
   * UPDATE Author
   * SET fullAuthorName = ?
   * WHERE idAuthor = ?;
   * </pre>
   */
  private final static String __UPDATE_FULLAUTHORNAME = "UPDATE Author "
          + "SET fullAuthorName = ? "
          + "WHERE idAuthor = ?;";
  /**
   * <pre>
   * UPDATE Author
   * SET authorName = ?,
   *     fullAuthorName = ?
   * WHERE idAuthor = ?;
   * </pre>
   */
  private final static String __UPDATE_AUTHOR = "UPDATE Author "
          + "SET authorName = ?,"
          + "    fullAuthorName = ? "
          + "WHERE idAuthor = ?;";
  /**
   * <pre>
   * UPDATE Author
   * SET AuthorGroup_idAuthorGroup = ?
   * WHERE idAuthor = ?;
   * </pre>
   */
  private final static String __UPDATE_AUTHORGROUP = "UPDATE Author "
          + "SET AuthorGroup_idAuthorGroup = ? "
          + "WHERE idAuthor = ?;";
  /**
   * <pre>
   * SELECT af.idAffiliation, af.fullAffiliation
   * FROM Author_Affiliation aa, Affiliation af
   * WHERE aa.Author_idAuthor = ? AND
   *       aa.Affiliation_idAffiliation = af.idAffiliation;
   * </pre>
   */
  private final static String __SELECT_AFFILIATION = "SELECT af.* "
          + "FROM Author_Affiliation aa, Affiliation af "
          + "WHERE aa.Author_idAuthor = ? AND "
          + "      aa.Affiliation_idAffiliation = af.idAffiliation;";
  private final static String __SELECT_AFFILIATION_ID = "SELECT af.idAffiliation "
          + "FROM Author_Affiliation aa, Affiliation af "
          + "WHERE aa.Author_idAuthor = ? AND "
          + "      aa.Affiliation_idAffiliation = af.idAffiliation;";
  /**
   * <pre>
   * SELECT ag.idAuthorGroup, ag.groupName, ag.stopGroup
   * FROM Author a, AuthorGroup ag
   * WHERE a.idAuthor = ? AND
   *       a.AuthorGroup_idAuthorGroup = ag.idAuthorGroup;
   * </pre>
   */
  private final static String __SELECT_AUTHORGROUP = "SELECT ag.* "
          + "FROM Author a, AuthorGroup ag "
          + "WHERE a.idAuthor = ? AND "
          + "     a.AuthorGroup_idAuthorGroup = ag.idAuthorGroup;";
  private final static String __SELECT_AUTHORGROUP_ID = "SELECT ag.idAuthorGroup "
          + "FROM Author a, AuthorGroup ag "
          + "WHERE a.idAuthor = ? AND "
          + "     a.AuthorGroup_idAuthorGroup = ag.idAuthorGroup;";
  /**
   * <pre>
   * </pre>
   */
  private final static String __SELECT_DOCUMENT = "SELECT d.* "
          + "FROM Document_Author da, Document d, Author a "
          + "WHERE a.idAuthor = ? AND "
          + "      a.idAuthor = da.Author_idAuthor AND "
          + "      da.Document_idDocument = d.idDocument;";
  private final static String __SELECT_DOCUMENT_ID = "SELECT d.idDocument "
          + "FROM Document_Author da, Document d, Author a "
          + "WHERE a.idAuthor = ? AND "
          + "      a.idAuthor = da.Author_idAuthor AND "
          + "      da.Document_idDocument = d.idDocument;";
  private final static String __SELECT_DOCUMENTAUTHOR = "SELECT d.*, a.*, da.position "
          + "FROM Document_Author da, Document d, Author a "
          + "WHERE a.idAuthor = ? AND "
          + "      a.idAuthor = da.Author_idAuthor AND "
          + "      da.Document_idDocument = d.idDocument;";
  /**
   * <pre>
   * SELECT ar.idAuthorReference, ar.authorName
   * FROM Author a, AuthorReference ar
   * WHERE a.idAuthor = ? AND
   *       ar.Author_idAuthor = a.idAuthor;
   * </pre>
   */
  private final static String __SELECT_AUTHORREFERENCE = "SELECT ar.* "
          + "FROM Author a, AuthorReference ar "
          + "WHERE a.idAuthor = ? AND "
          + "      ar.Author_idAuthor = a.idAuthor;";
  private final static String __SELECT_AUTHORREFERENCE_ID = "SELECT ar.idAuthorReference "
          + "FROM Author a, AuthorReference ar "
          + "WHERE a.idAuthor = ? AND "
          + "      ar.Author_idAuthor = a.idAuthor;";
  
  private final static String __SELECT_AUTHOR_BY_ID = "SELECT * FROM Author WHERE idAuthor = ?";
  private final static String __SELECT_AUTHOR_BY_NAME = "SELECT * FROM Author WHERE authorName = ? "
              + "AND fullAuthorName =?;";
  private final static String __SELECT_AUTHORS = "SELECT * FROM Author;";
  private final static String __SELECT_AUTHOR_IDS = "SELECT idAuthor FROM Author;";
  private final static String __SELECT_AUTHORS_WITHOUTGROUP = "SELECT * FROM Author WHERE AuthorGroup_idAuthorGroup IS NULL;";
  private final static String __SELECT_AUTHOR_WITHOUTGROUP_IDS = "SELECT idAuthor FROM Author WHERE AuthorGroup_idAuthorGroup IS NULL;";
  private final static String __CHECK_AUTHOR_BY_ID = "SELECT idAuthor FROM Author WHERE authorName = ? AND fullAuthorName = ?;";
  private final static String __CHECK_AUTHOR_BY_NAME = "SELECT idAuthor FROM Author WHERE idAuthor = ?;";
  
  private PreparedStatement statAddAuthor;
  private PreparedStatement statAddAuthorWithId;
  private PreparedStatement statRemoveAuthor;
  private PreparedStatement statSelectAffiliation;
  private PreparedStatement statSelectAffiliationId;
  private PreparedStatement statSelectAuthorGroup;
  private PreparedStatement statSelectAuthorGroupId;
  private PreparedStatement statSelectAuthorReference;
  private PreparedStatement statSelectAuthorReferenceId;
  private PreparedStatement statSelectDocument;
  private PreparedStatement statSelectDocumentAuthor;
  private PreparedStatement statSelectDocumentId;
  private PreparedStatement statUpdateAuthor;
  private PreparedStatement statUpdateAuthorGroup;
  private PreparedStatement statUpdateAuthorName;
  private PreparedStatement statUpdateFullAuthorName;
  private PreparedStatement statSelectAuthorById;
  private PreparedStatement statSelectAuthorByName;
  private PreparedStatement statSelectAuthors;
  private PreparedStatement statSelectAuthorIds;
  private PreparedStatement statSelectAuthorsWithoutGroup;
  private PreparedStatement statSelectAuthorWithoutGroupIds;
  private PreparedStatement statCheckAuthorById;
  private PreparedStatement statCheckAuthorByName;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   *
   * @param kbm
   */
  public AuthorDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {

    this.kbm = kbm;

    try {
      this.statAddAuthor = this.kbm.getConnection().prepareStatement(__INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS);
      this.statAddAuthorWithId = this.kbm.getConnection().prepareStatement(__INSERT_AUTHOR_WITH_ID);
      this.statRemoveAuthor = this.kbm.getConnection().prepareStatement(__REMOVE_AUTHOR);
      this.statSelectAffiliation = this.kbm.getConnection().prepareStatement(__SELECT_AFFILIATION);
      this.statSelectAffiliationId = this.kbm.getConnection().prepareStatement(__SELECT_AFFILIATION_ID);
      this.statSelectAuthorGroup = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORGROUP);
      this.statSelectAuthorGroupId = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORGROUP_ID);
      this.statSelectAuthorReference = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCE);
      this.statSelectAuthorReferenceId = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCE_ID);
      this.statSelectDocument = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENT);
      this.statSelectDocumentAuthor = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENTAUTHOR);
      this.statSelectDocumentId = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENT_ID);
      this.statUpdateAuthor = this.kbm.getConnection().prepareStatement(__UPDATE_AUTHOR);
      this.statUpdateAuthorGroup = this.kbm.getConnection().prepareStatement(__UPDATE_AUTHORGROUP);
      this.statUpdateAuthorName = this.kbm.getConnection().prepareStatement(__UPDATE_AUTHORNAME);
      this.statUpdateFullAuthorName = this.kbm.getConnection().prepareStatement(__UPDATE_FULLAUTHORNAME);
      this.statSelectAuthorById = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR_BY_ID);
      this.statSelectAuthorByName = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR_BY_NAME);
      this.statSelectAuthors = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORS);
      this.statSelectAuthorIds = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR_IDS);
      this.statSelectAuthorsWithoutGroup = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORS_WITHOUTGROUP);
      this.statSelectAuthorWithoutGroupIds = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR_WITHOUTGROUP_IDS);
      this.statCheckAuthorById = this.kbm.getConnection().prepareStatement(__CHECK_AUTHOR_BY_ID);
      this.statCheckAuthorByName = this.kbm.getConnection().prepareStatement(__CHECK_AUTHOR_BY_NAME);
    
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
   * @param fullAuthorName
   * @return
   * @throws KnowledgeBaseException
   */
  public Integer addAuthor(String authorName, String fullAuthorName, boolean notifyObservers)
          throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddAuthor.clearParameters();
      
      this.statAddAuthor.setString(1, authorName);
      this.statAddAuthor.setString(2, fullAuthorName);

      if (this.statAddAuthor.executeUpdate() == 1) {

        id = this.statAddAuthor.getGeneratedKeys().getInt(1);
        this.statAddAuthor.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAuthorEvent(getAuthor(id)));
    }

    return id;
  }

  /**
   *
   * @param authorID
   * @param authorName
   * @param fullAuthorName
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addAuthor(Integer authorID, String authorName, String fullAuthorName, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statAddAuthorWithId.clearParameters();

      this.statAddAuthorWithId.setInt(1, authorID);
      this.statAddAuthorWithId.setString(2, authorName);
      this.statAddAuthorWithId.setString(3, fullAuthorName);

      result = this.statAddAuthorWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAuthorEvent(getAuthor(authorID)));
    }

    return result;
  }

  /**
   * 
   * @param author
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addAuthor(Author author, boolean notifyObservers)
          throws KnowledgeBaseException {

    return addAuthor(author.getAuthorID(),
            author.getAuthorName(),
            author.getFullAuthorName(),
            notifyObservers);
  }

  /**
   *
   * @param authorID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeAuthor(Integer authorID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    Author author = null;
    AuthorGroup authorGroup = null;
    ArrayList<Document> documents = null;
    ArrayList<Affiliation> affiliations = null;

    // Save the information before remove
    if (notifyObservers) {

      author = getAuthor(authorID);
      authorGroup = getAuthorGroup(authorID);
      documents = getDocuments(authorID);
      affiliations = getAffiliations(authorID);
    }

    try {

      this.statRemoveAuthor.clearParameters();

      this.statRemoveAuthor.setInt(1, authorID);

      result = this.statRemoveAuthor.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveAuthorEvent(author));

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAffiliationEvent(CurrentProject.getInstance().getFactoryDAO().getAffiliationDAO().refreshAffiliations(affiliations)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AuthorRelationAffiliationEvent());

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().refreshDocuments(documents)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationAuthorEvent());

      if (authorGroup != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorGroupEvent(CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO().refreshAuthorGroup(authorGroup)));
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new AuthorGroupRelationAuthorEvent());

      } else {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveAuthorWithoutGroupEvent(author));
      }
    }

    return result;
  }

  /**
   *
   * @param idAuthor the author's ID
   *
   * @return an <ocde>Author</code> or null if there is not any author with
   *         this ID
   *
   * @throws KnowledgeBaseException
   */
  public Author getAuthor(Integer idAuthor) throws KnowledgeBaseException {

    ResultSet rs;
    Author author = null;

    try {

      this.statSelectAuthorById.clearParameters();

      this.statSelectAuthorById.setInt(1, idAuthor);

      rs = this.statSelectAuthorById.executeQuery();

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
   * @param authorName the author's name
   * @param fullAuthorName the full author's name
   *
   * @return an <ocde>Author</code> or null if there is not any author with
   *         this attribute
   *
   * @throws KnowledgeBaseException
   */
  public Author getAuthor(String authorName, String fullAuthorName)
          throws KnowledgeBaseException {

    ResultSet rs;
    Author author = null;

    try {

      this.statSelectAuthorByName.clearParameters();

      this.statSelectAuthorByName.setString(1, authorName);
      this.statSelectAuthorByName.setString(2, fullAuthorName);

      rs = this.statSelectAuthorByName.executeQuery();

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
   * @return an <ocde>Author</code> or null if there is not any author with
   *         this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Author> getAuthors() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Author> authorList = new ArrayList<Author>();

    try {

      this.statSelectAuthors.clearParameters();

      rs = this.statSelectAuthors.executeQuery();

      while (rs.next()) {

        authorList.add(UtilsDAO.getInstance().getAuthor(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorList;
  }

  /**
   * 
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Integer> getAuthorIDs() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Integer> authorList = new ArrayList<Integer>();

    try {

      this.statSelectAuthorIds.clearParameters();

      rs = this.statSelectAuthorIds.executeQuery();

      while (rs.next()) {

        authorList.add(UtilsDAO.getInstance().getAuthorID(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorList;
  }

  /**
   * 
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Author> getAuthorsWithoutGroup() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Author> authorList = new ArrayList<Author>();

    try {

      this.statSelectAuthorsWithoutGroup.clearParameters();

      rs = this.statSelectAuthorsWithoutGroup.executeQuery();

      while (rs.next()) {

        authorList.add(UtilsDAO.getInstance().getAuthor(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorList;
  }

  /**
   * 
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Integer> getAuthorIDsWithoutGroup() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Integer> authorList = new ArrayList<Integer>();

    try {

      this.statSelectAuthorWithoutGroupIds.clearParameters();

      rs = this.statSelectAuthorWithoutGroupIds.executeQuery();

      while (rs.next()) {

        authorList.add(UtilsDAO.getInstance().getAuthorID(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorList;
  }

  /**
   *
   * @param authorName the author's name to set
   *
   * @return 
   * @throws KnowledgeBaseException
   */
  public boolean setAuthorName(Integer authorID, String authorName, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    Author author;

    try {

      this.statUpdateAuthorName.clearParameters();

      this.statUpdateAuthorName.setString(1, authorName);
      this.statUpdateAuthorName.setInt(2, authorID);

      result = this.statUpdateAuthorName.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      author = getAuthor(authorID);

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorEvent(author));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocuments(authorID)));

      if (getAuthorGroup(authorID) == null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorWithoutGroupEvent(author));
      }
    }

    return result;
  }

  /**
   *
   * @param authorID
   * @param fullAuthorName the author's full name to set
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setFullAuthorName(Integer authorID, String fullAuthorName, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    Author author;

    try {

      this.statUpdateFullAuthorName.clearParameters();

      this.statUpdateFullAuthorName.setString(1, fullAuthorName);
      this.statUpdateFullAuthorName.setInt(2, authorID);

      result = this.statUpdateFullAuthorName.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      author = getAuthor(authorID);

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorEvent(author));

      if (getAuthorGroup(authorID) == null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorWithoutGroupEvent(author));
      }
    }

    return result;
  }

  /**
   * 
   * @param authorID
   * @param authorGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setAuthorGroup(Integer authorID, Integer authorGroupID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    AuthorGroup oldAuthorGroup = null;

    // Save the information before remove
    if (notifyObservers) {

      oldAuthorGroup = getAuthorGroup(authorID);
    }

    try {

      this.statUpdateAuthorGroup.clearParameters();

      if (authorGroupID != null) {

        this.statUpdateAuthorGroup.setInt(1, authorGroupID);

      } else {

        this.statUpdateAuthorGroup.setNull(1, Types.NULL);
      }

      this.statUpdateAuthorGroup.setInt(2, authorID);

      result = this.statUpdateAuthorGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      if (oldAuthorGroup != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorGroupEvent(CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO().refreshAuthorGroup(oldAuthorGroup)));

        if (authorGroupID == null) {

          KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAuthorWithoutGroupEvent(getAuthor(authorID)));
        }

      } else {

        if (authorGroupID != null) {

          KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveAuthorWithoutGroupEvent(getAuthor(authorID)));
        }
      }

      if (authorGroupID != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorGroupEvent(getAuthorGroup(authorID)));
      }

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AuthorGroupRelationAuthorEvent());
    }

    return result;
  }

  /**
   *
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean updateAuthor(Integer authorID, String authorName, String fullAuthorName, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    Author author;

    try {

      this.statUpdateAuthor.clearParameters();

      this.statUpdateAuthor.setString(1, authorName);
      this.statUpdateAuthor.setString(2, fullAuthorName);
      this.statUpdateAuthor.setInt(3, authorID);

      result = this.statUpdateAuthor.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      author = getAuthor(authorID);

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorEvent(author));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocuments(authorID)));

      if (getAuthorGroup(authorID) == null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorWithoutGroupEvent(author));
      }
    }

    return result;
  }

  /**
   * @return the AuthorGroup's ID associated with the author
   *
   * @throws KnowledgeBaseException
   */
  public AuthorGroup getAuthorGroup(Integer authorID) throws KnowledgeBaseException {

    ResultSet rs;
    AuthorGroup authorGroup = null;

    try {

      this.statSelectAuthorGroup.clearParameters();

      this.statSelectAuthorGroup.setInt(1, authorID);

      rs = this.statSelectAuthorGroup.executeQuery();

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
   * @param authorID
   * @return
   * @throws KnowledgeBaseException 
   */
  public Integer getAuthorGroupID(Integer authorID) throws KnowledgeBaseException {

    ResultSet rs;
    Integer authorGroupID = null;

    try {

      this.statSelectAuthorGroupId.clearParameters();

      this.statSelectAuthorGroupId.setInt(1, authorID);

      rs = this.statSelectAuthorGroupId.executeQuery();

      if (rs.next()) {

        authorGroupID = UtilsDAO.getInstance().getAuthorGroupID(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorGroupID;
  }

  /**
   *
   * @return an array with the affiliations associated with this author
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Affiliation> getAffiliations(Integer authorID)
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Affiliation> affiliationList = new ArrayList<Affiliation>();

    try {

      this.statSelectAffiliation.clearParameters();

      this.statSelectAffiliation.setInt(1, authorID);

      rs = this.statSelectAffiliation.executeQuery();

      while (rs.next()) {

        affiliationList.add(UtilsDAO.getInstance().getAffiliation(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return affiliationList;
  }

  /**
   * 
   * @param authorID
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Integer> getAffiliationIDs(Integer authorID)
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Integer> affiliationList = new ArrayList<Integer>();

    try {

      this.statSelectAffiliationId.clearParameters();

      this.statSelectAffiliationId.setInt(1, authorID);

      rs = this.statSelectAffiliationId.executeQuery();

      while (rs.next()) {

        affiliationList.add(UtilsDAO.getInstance().getAffiliationID(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return affiliationList;
  }

  /**
   *
   * @return an array with the documents associated with this author
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<DocumentAuthor> getDocumentAuthors(Integer authorID)
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<DocumentAuthor> documentsList = new ArrayList<DocumentAuthor>();

    try {

      this.statSelectDocumentAuthor.clearParameters();

      this.statSelectDocumentAuthor.setInt(1, authorID);

      rs = this.statSelectDocumentAuthor.executeQuery();

      while (rs.next()) {

        documentsList.add(UtilsDAO.getInstance().getDocumentAuthor(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return documentsList;
  }

  /**
   *
   * @return an array with the documents associated with this author
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Document> getDocuments(Integer authorID)
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Document> documentsList = new ArrayList<Document>();

    try {

      this.statSelectDocument.clearParameters();

      this.statSelectDocument.setInt(1, authorID);

      rs = this.statSelectDocument.executeQuery();

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
   * @param authorID
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Integer> getDocumentIDs(Integer authorID)
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Integer> documentsList = new ArrayList<Integer>();

    try {

      this.statSelectDocumentId.clearParameters();

      this.statSelectDocumentId.setInt(1, authorID);

      rs = this.statSelectDocumentId.executeQuery();

      while (rs.next()) {

        documentsList.add(UtilsDAO.getInstance().getDocumentID(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return documentsList;
  }

  /**
   * 
   * @param authorID
   * @return
   * @throws KnowledgeBaseException 
   */
  public AuthorReference getAuthorReference(Integer authorID) throws KnowledgeBaseException {

    ResultSet rs;
    AuthorReference authorReference = null;

    try {

      this.statSelectAuthorReference.clearParameters();

      this.statSelectAuthorReference.setInt(1, authorID);

      rs = this.statSelectAuthorReference.executeQuery();

      if (rs.next()) {

        authorReference = UtilsDAO.getInstance().getAuthorReference(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReference;
  }

  public Integer getAuthorReferenceID(Integer authorID) throws KnowledgeBaseException {

    ResultSet rs;
    Integer authorReferenceID = null;

    try {

      this.statSelectAuthorReferenceId.clearParameters();

      this.statSelectAuthorReferenceId.setInt(1, authorID);

      rs = this.statSelectAuthorReferenceId.executeQuery();

      if (rs.next()) {

        authorReferenceID = UtilsDAO.getInstance().getAuthorReferenceID(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferenceID;
  }

  /**
   * <p>Check if there is an <code>Author</code> with this
   * authorName and fullAuthorName.</p>
   *
   * @param authorName a string with the author's name
   * @param fullAthorName a string with the full author's name
   *
   * @return true if there is an <code>Author</code> with these attributes
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkAuthor(String authorName, String fullAthorName)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckAuthorById.clearParameters();

      this.statCheckAuthorById.setString(1, authorName);
      this.statCheckAuthorById.setString(2, fullAthorName);

      rs = this.statCheckAuthorById.executeQuery();

      result = rs.next();

      rs.close();

      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>Author</code> with this ID.</p>
   *
   * @param idAuthor the author's ID
   *
   * @return true if there is an <code>Author</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkAuthor(Integer idAuthor) throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckAuthorByName.clearParameters();

      this.statCheckAuthorByName.setInt(1, idAuthor);

      rs = this.statCheckAuthorByName.executeQuery();

      result = rs.next();

      rs.close();

      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * 
   * @param authorToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Author> refreshAuthors(ArrayList<Author> authorToRefresh) throws KnowledgeBaseException {

    int i;
    String query;
    ResultSet rs;
    ArrayList<Author> authors = new ArrayList<Author>();

    i = 0;

    if (!authorToRefresh.isEmpty()) {

      query = "SELECT * FROM Author WHERE idAuthor IN (" + authorToRefresh.get(i).getAuthorID();

      for (i = 1; i < authorToRefresh.size(); i++) {

        query += ", " + authorToRefresh.get(i).getAuthorID();
      }

      query += ");";

      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          authors.add(UtilsDAO.getInstance().getAuthor(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }

    return authors;
  }

  /**
   * 
   * @param authorToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public Author refreshAuthor(Author authorToRefresh) throws KnowledgeBaseException {

    return getAuthor(authorToRefresh.getAuthorID());
  }
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
