/*
 * AffiliationDAO.java
 *
 * Created on 21-oct-2010, 17:48:24
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddAffiliationEvent;
import scimat.knowledgebaseevents.event.relation.AuthorRelationAffiliationEvent;
import scimat.knowledgebaseevents.event.relation.DocumentRelationAffiliationEvent;
import scimat.knowledgebaseevents.event.remove.RemoveAffiliationEvent;
import scimat.knowledgebaseevents.event.update.UpdateAffiliationEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorEvent;
import scimat.knowledgebaseevents.event.update.UpdateDocumentEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 * <p>This class represent an author's affiliation.</p>
 *
 * <p>Each <code>AffiliationDAO</code> contains one single Identifier and the
 * complete affiliation.</p>
 * 
 * @author mjcobo
 */
public class AffiliationDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO Affiliation(fullAffiliation) VALUES(?)"
   * </pre>
   */
  private final static String __INSERT_AFFILIATION = "INSERT INTO Affiliation(fullAffiliation) VALUES(?);";

  /**
   * <pre>
   * INSERT INTO Affiliation(idAffiliation,fullAffiliation) VALUES(?,?)"
   * </pre>
   */
  private final static String __INSERT_AFFILIATION_WITH_ID = "INSERT INTO Affiliation(idAffiliation,fullAffiliation) VALUES(?,?);";

  /**
   * <pre>
   * DELETE Affiliation
   * WHERE idAffiliation = ?;
   * </pre>
   */
  private final static String __REMOVE_AFFILIATION = "DELETE FROM Affiliation "
                                                   + "WHERE idAffiliation = ?;";

  /**
   * <pre>
   * UPDATE Affiliation
   * SET fullAffiliation = ?
   * WHERE idAffiliation = ?;
   * </pre>
   */
  private final static String __UPDATE_FULLAFILLILIATION = "UPDATE Affiliation "
                                                         + "SET fullAffiliation = ? "
                                                         + "WHERE idAffiliation = ?;";
 
   
  /**
   * <pre>
   * SELECT au.idAuthor, au.authorName, au.fullAuthorName
   * FROM Author_Affiliation aa, Author au
   * WHERE aa.Affiliation_idAffiliation = ? AND au.idAuthor = aa.Author_idAuthor;
   * </pre>
   */
  private final static String __SELECT_AUTHOR = "SELECT au.* "
                                              + "FROM Author_Affiliation aa, Author au "
                                              + "WHERE aa.Affiliation_idAffiliation = ? AND au.idAuthor = aa.Author_idAuthor;";
  
  private final static String __SELECT_AUTHOR_IDs = "SELECT au.idAuthor "
                                              + "FROM Author_Affiliation aa, Author au "
                                              + "WHERE aa.Affiliation_idAffiliation = ? AND au.idAuthor = aa.Author_idAuthor;";
  
  private final static String __SELECT_AUTHOR_WITHOUTGROUP = "SELECT au.* "
                                              + "FROM Author_Affiliation aa, Author au "
                                              + "WHERE aa.Affiliation_idAffiliation = ? AND au.idAuthor = aa.Author_idAuthor AND au.AuthorGroup_idAuthorGroup ISNULL;";
  
  private final static String __SELECT_AUTHOR_ID_WITHOUTGROUP = "SELECT au.idAuthor "
                                              + "FROM Author_Affiliation aa, Author au "
                                              + "WHERE aa.Affiliation_idAffiliation = ? AND au.idAuthor = aa.Author_idAuthor AND au.AuthorGroup_idAuthorGroup ISNULL;";

  /**
   * <pre>
   * SELECT d.*
   * FROM Document_Affiliation da, Document d
   * WHERE da.Affiliation_idAffiliation = ? AND d.idDocument = da.Document_idDocument;
   * </pre>
   */
  private final static String __SELECT_DOCUMENTS = "SELECT d.* "
                                                + "FROM Document_Affiliation da, Document d "
                                                + "WHERE da.Affiliation_idAffiliation = ? AND d.idDocument = da.Document_idDocument;";
  
  private final static String __SELECT_DOCUMENT_ID = "SELECT d.idDocument "
                                                   + "FROM Document_Affiliation da, Document d "
                                                   + "WHERE da.Affiliation_idAffiliation = ? AND d.idDocument = da.Document_idDocument;";
  
  private final static String __SELECT_AFFILIATION_BY_ID = "SELECT * FROM Affiliation WHERE idAffiliation = ?;";
  private final static String __SELECT_AFFILIATION_BY_FULLAFFILIATION = "SELECT * FROM Affiliation WHERE fullAffiliation = ?;";
  private final static String __SELECT_AFFILIATIONS = "SELECT * FROM Affiliation;";
  private final static String __SELECT_AFFILIATION_IDS = "SELECT idAffiliation FROM Affiliation;";
  private final static String __CHECK_AFFILIATION_BY_FULLAFFILIATION = "SELECT idAffiliation FROM Affiliation WHERE fullAffiliation = ?;";
  private final static String __CHECK_AFFILIATION_BY_ID = "SELECT idAffiliation FROM Affiliation WHERE idAffiliation = ?;";
  
  private PreparedStatement statAddAffiliation;
  private PreparedStatement statAddAffiliationWithId;
  private PreparedStatement statRemoveAffiliation;
  private PreparedStatement statSelectAffiliationById;
  private PreparedStatement statSelectAffiliationByFullAffiliation;
  private PreparedStatement statSelectAffiliations;
  private PreparedStatement statSelectAffiliationIds;
  private PreparedStatement statCheckAffiliationById;
  private PreparedStatement statCheckAffiliationByFullAffiliation;
  private PreparedStatement statSelectAuthors;
  private PreparedStatement statSelectAuthorIds;
  private PreparedStatement statSelectAuthorWithoutGroup;
  private PreparedStatement statSelectAuthorWithoutGroupID;
  private PreparedStatement statSelectDocuments;
  private PreparedStatement statSelectDocumentId;
  private PreparedStatement statUpdateFullAffiliation;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * Create a new AffiliationDAO.
   * 
   * @param idAffiliation the ID
   * @param fullAffilliation the AffiliationDAO
   * @param kbm 
   */
  public AffiliationDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    
    this.kbm = kbm;
    
    try {
      
      this.statAddAffiliation = this.kbm.getConnection().prepareStatement(__INSERT_AFFILIATION, Statement.RETURN_GENERATED_KEYS);
      this.statAddAffiliationWithId = this.kbm.getConnection().prepareStatement(__INSERT_AFFILIATION_WITH_ID);
      this.statRemoveAffiliation = this.kbm.getConnection().prepareStatement(__REMOVE_AFFILIATION);
      this.statSelectAffiliationById = this.kbm.getConnection().prepareStatement(__SELECT_AFFILIATION_BY_ID);
      this.statSelectAffiliationByFullAffiliation = this.kbm.getConnection().prepareStatement(__SELECT_AFFILIATION_BY_FULLAFFILIATION);
      this.statSelectAffiliations = this.kbm.getConnection().prepareStatement(__SELECT_AFFILIATIONS);
      this.statSelectAffiliationIds = this.kbm.getConnection().prepareStatement(__SELECT_AFFILIATION_IDS);
      this.statCheckAffiliationById = this.kbm.getConnection().prepareStatement(__CHECK_AFFILIATION_BY_ID);
      this.statCheckAffiliationByFullAffiliation = this.kbm.getConnection().prepareStatement(__CHECK_AFFILIATION_BY_FULLAFFILIATION);
      this.statSelectAuthors = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR);
      this.statSelectAuthorIds = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR_IDs);
      this.statSelectAuthorWithoutGroup = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR_WITHOUTGROUP);
      this.statSelectAuthorWithoutGroupID = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR_ID_WITHOUTGROUP);
      this.statSelectDocuments = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENTS);
      this.statSelectDocumentId = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENT_ID);
      this.statUpdateFullAffiliation = this.kbm.getConnection().prepareStatement(__UPDATE_FULLAFILLILIATION);
      
    } catch (SQLException e) {
      
      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param fullAffiliation
   *
   * @return the affiliation's ID or null if the affiliation was not inserted.
   *
   * @throws KnowledgeBaseException
   */
  public Integer addAffiliation(String fullAffiliation, boolean notifyObservers)
          throws KnowledgeBaseException{

    Integer id;

    try {

      this.statAddAffiliation.clearParameters();

      this.statAddAffiliation.setString(1, fullAffiliation);

      if (this.statAddAffiliation.executeUpdate() == 1 ) {

        id = this.statAddAffiliation.getGeneratedKeys().getInt(1);
        this.statAddAffiliation.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAffiliationEvent(getAffiliation(id)));
    }

    return id;
  }

  /**
   * 
   * @param affiliationID
   * @param fullAffiliation
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addAffiliation(Integer affiliationID, String fullAffiliation, boolean notifyObservers)
          throws KnowledgeBaseException{
    
    boolean result = false;

    try {

      this.statAddAffiliationWithId.clearParameters();
      
      this.statAddAffiliationWithId.setInt(1, affiliationID);
      this.statAddAffiliationWithId.setString(2, fullAffiliation);

      result = this.statAddAffiliationWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddAffiliationEvent(getAffiliation(affiliationID)));
    }

    return result;
  }

  /**
   *
   * @param affiliation
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addAffiliation(Affiliation affiliation, boolean notifyObservers)
          throws KnowledgeBaseException{

    return addAffiliation(affiliation.getAffiliationID(),
                          affiliation.getFullAffiliation(),
                          notifyObservers);
  }

  /**
   * 
   * @param affiliationID
   * @param notifyObservers
   * @return
   * @throws KnowledgeBaseException 
   */
  public boolean removeAffiliation(Integer affiliationID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    Affiliation affiliation = null;
    ArrayList<Document> documents = null;
    ArrayList<Author> authors = null;
    ArrayList<Author> authorsWithoutGroup = null;
    
    // Save the information before remove
    if (notifyObservers) {
      
      affiliation = getAffiliation(affiliationID);
      documents = getDocuments(affiliationID);
      authors = getAuthors(affiliationID);
      authorsWithoutGroup = getAuthorWithoutGroups(affiliationID);
    }
    
    try {

      this.statRemoveAffiliation.clearParameters();

      this.statRemoveAffiliation.setInt(1, affiliationID);

      result = this.statRemoveAffiliation.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveAffiliationEvent(affiliation));

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().refreshDocuments(documents)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationAffiliationEvent());

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorEvent(CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().refreshAuthors(authors)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorEvent(CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().refreshAuthors(authorsWithoutGroup)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AuthorRelationAffiliationEvent());
    }

    return result;
  }

  /**
   *
   * @param idAffiliation the affiliation's ID
   *
   * @return an <ocde>Affilation</code> or null if there is not any affiliation
   *         with this ID
   *
   * @throws KnowledgeBaseException
   */
  public Affiliation getAffiliation(Integer idAffiliation)
          throws KnowledgeBaseException {

    ResultSet rs;
    Affiliation affiliation = null;

    try {

      this.statSelectAffiliationById.clearParameters();

      this.statSelectAffiliationById.setInt(1, idAffiliation);

      rs = this.statSelectAffiliationById.executeQuery();

      if (rs.next()) {

        affiliation = UtilsDAO.getInstance().getAffiliation(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return affiliation;
  }

  /**
   *
   * @param fullAffiliation the affiliation's ID
   *
   * @return an <ocde>Affilation</code> or null if there is not any affiliation
   *         with this full affiliation
   *
   * @throws KnowledgeBaseException
   */
  public Affiliation getAffiliation(String fullAffiliation)
          throws KnowledgeBaseException {

    ResultSet rs;
    Affiliation affiliation = null;

    try {

      this.statSelectAffiliationByFullAffiliation.clearParameters();

      this.statSelectAffiliationByFullAffiliation.setString(1, fullAffiliation);

      rs = this.statSelectAffiliationByFullAffiliation.executeQuery();

      if (rs.next()) {

        affiliation = UtilsDAO.getInstance().getAffiliation(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return affiliation;
  }

  /**
   *
   * @return an array with all the <ocde>Affilation</code>
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Affiliation> getAffiliations() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Affiliation> affiliationsList = new ArrayList<Affiliation>();

    try {

      this.statSelectAffiliations.clearParameters();

      rs = this.statSelectAffiliations.executeQuery();

      while (rs.next()) {

        affiliationsList.add(UtilsDAO.getInstance().getAffiliation(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return affiliationsList;
  }
  
  /**
   *
   * @return an array with all the <ocde>Affilation</code>
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Integer> getAffiliationIDs() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Integer> affiliationIDs = new ArrayList<Integer>();

    try {

      this.statSelectAffiliationIds.clearParameters();

      rs = this.statSelectAffiliationIds.executeQuery();

      while (rs.next()) {

        affiliationIDs.add(UtilsDAO.getInstance().getAffiliationID(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return affiliationIDs;
  }

  /**
   * 
   * @param affiliationID
   * @param fullAffilliation
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setFullAffiliation(Integer affiliationID, String fullAffilliation, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;
    
    try {

      this.statUpdateFullAffiliation.clearParameters();
      
      this.statUpdateFullAffiliation.setString(1, fullAffilliation);
      this.statUpdateFullAffiliation.setInt(2, affiliationID);
      
      result = this.statUpdateFullAffiliation.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAffiliationEvent(getAffiliation(affiliationID)));
    }

    return result;
  }

  /**
   *
   * @return an array with the authors associated with this affiliation
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Author> getAuthors(Integer affiliationID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Author> authorsList = new ArrayList<Author>();

    try {

      this.statSelectAuthors.clearParameters();

      this.statSelectAuthors.setInt(1, affiliationID);

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
   * @return an array with the authors associated with this affiliation
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Integer> getAuthorIDs(Integer affiliationID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Integer> authorsList = new ArrayList<Integer>();

    try {

      this.statSelectAuthorIds.clearParameters();

      this.statSelectAuthorIds.setInt(1, affiliationID);

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
   *
   * @return an array with the authors associated with this affiliation
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Author> getAuthorWithoutGroups(Integer affiliationID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Author> authorsList = new ArrayList<Author>();

    try {

      this.statSelectAuthorWithoutGroup.clearParameters();

      this.statSelectAuthorWithoutGroup.setInt(1, affiliationID);

      rs = this.statSelectAuthorWithoutGroup.executeQuery();

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
   * @return an array with the authors associated with this affiliation
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Integer> getAuthorIDsWithoutGroup(Integer affiliationID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Integer> authorsList = new ArrayList<Integer>();

    try {

      this.statSelectAuthorWithoutGroupID.clearParameters();

      this.statSelectAuthorWithoutGroupID.setInt(1, affiliationID);

      rs = this.statSelectAuthorWithoutGroupID.executeQuery();

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
   *
   * @return an array with the documents associated with this affiliation
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Document> getDocuments(Integer affiliationID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Document> documentsList = new ArrayList<Document>();

    try {

      this.statSelectDocuments.clearParameters();

      this.statSelectDocuments.setInt(1, affiliationID);

      rs = this.statSelectDocuments.executeQuery();

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
   */
  public ArrayList<Integer> getDocumentIDs(Integer affiliationID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Integer> documentsList = new ArrayList<Integer>();

    try {

      this.statSelectDocumentId.clearParameters();

      this.statSelectDocumentId.setInt(1, affiliationID);

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
   * <p>Check if there is an <code>Affiliation</code> with this
   * fullAffiliation.</p>
   *
   * @param fullAffiliation a string with the full affiliation
   *
   * @return true if there is an <code>Affiliation</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkAffiliation(String fullAffiliation)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckAffiliationByFullAffiliation.clearParameters();

      this.statCheckAffiliationByFullAffiliation.setString(1, fullAffiliation);

      rs = this.statCheckAffiliationByFullAffiliation.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>Affiliation</code> with this ID.</p>
   *
   * @param idAffiliation the affiliation's ID
   *
   * @return true if there is an <code>Affiliation</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkAffiliation(Integer idAffiliation)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckAffiliationById.clearParameters();

      this.statCheckAffiliationById.setInt(1, idAffiliation);

      rs = this.statCheckAffiliationById.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param affiliationsToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Affiliation> refreshAffiliations(ArrayList<Affiliation> affiliationsToRefresh) throws KnowledgeBaseException {
  
    int i;
    String query;
    ResultSet rs;
    ArrayList<Affiliation> affiliations = new ArrayList<Affiliation>();
    
    i = 0;
    
    if (!affiliationsToRefresh.isEmpty()) {

      query = "SELECT * FROM Affiliation WHERE idAffiliation IN (" + affiliationsToRefresh.get(i).getAffiliationID();
      
      for (i = 1; i < affiliationsToRefresh.size(); i++) {
        
        query += ", " + affiliationsToRefresh.get(i).getAffiliationID();
      }
      
      query += ");";
      
      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          affiliations.add(UtilsDAO.getInstance().getAffiliation(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }
    
    return affiliations;
  }
  
  /**
   * 
   * @param affiliationToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public Affiliation refreshAffiliation(Affiliation affiliationToRefresh) throws KnowledgeBaseException {
  
    return getAffiliation(affiliationToRefresh.getAffiliationID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
