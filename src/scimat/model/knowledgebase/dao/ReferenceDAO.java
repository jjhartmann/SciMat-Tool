/*
 * ReferenceDAO.java
 *
 * Created on 21-oct-2010, 17:48:49
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.TreeSet;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddReferenceEvent;
import scimat.knowledgebaseevents.event.add.AddReferenceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.relation.DocumentRelationReferenceEvent;
import scimat.knowledgebaseevents.event.relation.ReferenceGroupRelationReferenceEvent;
import scimat.knowledgebaseevents.event.relation.ReferenceRelationAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.relation.ReferenceSourceRelationReferenceEvent;
import scimat.knowledgebaseevents.event.remove.RemoveReferenceEvent;
import scimat.knowledgebaseevents.event.remove.RemoveReferenceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateDocumentEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceSourceEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceSourceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceSourceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceWithoutGroupEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.entity.AuthorReferenceReference;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class ReferenceDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO Reference(fullReference,volume,issue,page,year,doi,format) VALUES(?,?,?,?,?,?,?);
   * </pre>
   */
  private final static String __INSERT_REFERENCE = "INSERT INTO Reference(fullReference,volume,issue,page,year,doi,format) VALUES(?,?,?,?,?,?,?);";

  /**
   * <pre>
   * INSERT INTO Reference(idReference,fullReference,volume,issue,page,year,doi,format) VALUES(?,?,?,?,?,?,?,?);
   * </pre>
   */
  private final static String __INSERT_REFERENCE_WITH_ID = "INSERT INTO Reference(idReference,fullReference,volume,issue,page,year,doi,format) VALUES(?,?,?,?,?,?,?,?);";

  /**
   * <pre>
   * DELETE Reference
   * WHERE idReference = ?;
   * </pre>
   */
  private final static String __REMOVE_REFERENCE = "DELETE FROM Reference "
                                                 + "WHERE idReference = ?;";

  /**
   * <pre>
   * UPDATE Reference 
   * SET fullReference = ?
   * WHERE idReference = ?;
   * </pre>
   */
  private final static String __UPDATE_FULLREFERENCE = "UPDATE Reference "
                                                     + "SET fullReference = ? "
                                                     + "WHERE idReference = ?;";
  
  /**
   * <pre>
   * UPDATE Reference 
   * SET volume = ?
   * WHERE idReference = ?;
   * </pre>
   */
  private final static String __UPDATE_VOLUME = "UPDATE Reference "
                                              + "SET volume = ?"
                                              + " WHERE idReference = ?;";

  /**
   * <pre>
   * UPDATE Reference 
   * SET issue = ?
   * WHERE idReference = ?;
   * </pre>
   */
  private final static String __UPDATE_ISSUE = "UPDATE Reference "
                                             + "SET issue = ? "
                                             + "WHERE idReference = ?;";

  /**
   * <pre>
   * UPDATE Reference 
   * SET page = ?
   * WHERE idReference = ?;
   * </pre>
   */
  private final static String __UPDATE_PAGE = "UPDATE Reference "
                                            + "SET page = ? "
                                            + "WHERE idReference = ?;";

  /**
   * <pre>
   * UPDATE Reference 
   * SET doi = ?
   * WHERE idReference = ?;
   * </pre>
   */
  private final static String __UPDATE_DOI = "UPDATE Reference "
                                           + "SET doi = ? "
                                           + "WHERE idReference = ?;";

  /**
   * <pre>
   * UPDATE Reference 
   * SET format = ?
   * WHERE idReference = ?;
   * </pre>
   */
  private final static String __UPDATE_FORMAT = "UPDATE Reference "
                                              + "SET format = ? "
                                              + "WHERE idReference = ?;";

  /**
   * <pre>
   * UPDATE Reference 
   * SET year = ?
   * WHERE idReference = ?;
   * </pre>
   */
  private final static String __UPDATE_YEAR = "UPDATE Reference "
                                            + "SET year = ? "
                                            + "WHERE idReference = ?;";

  /**
   * <pre>
   * UPDATE Reference
   * SET fullReference = ?,
   *     volume = ?,
   *     issue = ?,
   *     page = ?,
   *     year = ?,
   *     doi = ?,
   *     format = ?
   * WHERE idReference = ?;
   * </pre>
   */
  private final static String __UPDATE_REFERENCE = "UPDATE Reference "
                                                 + "SET fullReference = ?, "
                                                 + "    volume = ?, "
                                                 + "    issue = ?, "
                                                 + "    page = ?, "
                                                 + "    year = ?, "
                                                 + "    doi = ?, "
                                                 + "    format = ? "
                                                 + "WHERE idReference = ?;";

  /**
   * <pre>
   * UPDATE Reference 
   * SET ReferenceGroup_idReferenceGroup = ?
   * WHERE idReference = ?;
   * </pre>
   */
  private final static String __UPDATE_REFERENCEGROUP = "UPDATE Reference "
                                                      + "SET ReferenceGroup_idReferenceGroup = ? "
                                                      + "WHERE idReference = ?;";

  /**
   * <pre>
   * UPDATE Reference 
   * SET ReferenceSource_idReferenceSource = ?
   * WHERE idReference = ?;
   * </pre>
   */
  private final static String __UPDATE_REFERENCESOURCE = "UPDATE Reference "
                                                       + "SET ReferenceSource_idReferenceSource = ? "
                                                       + "WHERE idReference = ?;";

  /**
   * <pre>
   * SELECT rg.idReferenceGroup, rg.groupName, rg.stopGroup
   * FROM Reference r, ReferenceGroup rg
   * WHERE r.idReference = ? AND r.ReferenceGroup_idReferenceGroup = rg.idReferenceGroup;
   * </pre>
   */
  private final static String __SELECT_REFERENCEGROUP = "SELECT rg.* "
                                                      + "FROM Reference r, ReferenceGroup rg "
                                                      + "WHERE r.idReference = ? AND r.ReferenceGroup_idReferenceGroup = rg.idReferenceGroup;";

  /**
   * <pre>
   * SELECT rs.idReferenceSource, rs.source "
   * FROM Reference r, ReferenceSource rs
   * WHERE r.idReference = ? AND r.ReferenceSource_idReferenceSource = rs.idReferenceSource;
   * </pre>
   */
  private final static String __SELECT_REFERENCESOURCE = "SELECT rs.* "
                                                       + "FROM Reference r, ReferenceSource rs "
                                                       + "WHERE r.idReference = ? AND r.ReferenceSource_idReferenceSource = rs.idReferenceSource;";
  
  private final static String __SELECT_REFERENCESOURCE_WITHOUT_GROUP = "SELECT rs.* "
                                                       + "FROM Reference r, ReferenceSource rs "
                                                       + "WHERE r.idReference = ? AND r.ReferenceSource_idReferenceSource = rs.idReferenceSource AND rs.ReferenceSourceGroup_idReferenceSourceGroup;";

  /**
   * <pre>
   * </pre>
   */
  private final static String __SELECT_DOCUMENTS = "SELECT d.* "
                                                 + "FROM Document_Reference dr, Document d "
                                                 + "WHERE dr.Reference_idReference = ? and dr.Document_idDocument = d.idDocument;";

  /**
   * <pre>
   * SELECT r.idReference, r.fullReference, r.volume, r.issue, r.page, r.doi, r.format, r.year,
   *        ar.idAuthorReference, ar.authorName
   *        arr.position
   * FROM AuthorReference_Reference arr, AuthorReference ar, Reference r
   * WHERE r.idReference = ? AND
   *       r.Reference_idReference = arr.Reference_idReference AND
   *       arr.AuthorReference_idAuthorReference = ar.idAuthorReference;
   * </pre>
   */
  private final static String __SELECT_AUTHORREFERENCE_REFERENCE = "SELECT r.*, ar.*, arr.position "
                                                        + "FROM AuthorReference_Reference arr, AuthorReference ar, Reference r "
                                                        + "WHERE r.idReference = ? AND "
                                                        + "      r.idReference = arr.Reference_idReference AND "
                                                        + "      arr.AuthorReference_idAuthorReference = ar.idAuthorReference;";
  
  /** <pre>
   * SELECT r.idReference, r.fullReference, r.volume, r.issue, r.page, r.doi, r.format, r.year,
   *        ar.idAuthorReference, ar.authorName
   *        arr.position
   * FROM AuthorReference_Reference arr, AuthorReference ar, Reference r
   * WHERE r.idReference = ? AND
   *       r.Reference_idReference = arr.Reference_idReference AND
   *       arr.AuthorReference_idAuthorReference = ar.idAuthorReference;
   * </pre>
   */
  private final static String __SELECT_AUTHORREFERENCES = "SELECT ar.* "
                                                        + "FROM AuthorReference_Reference arr, AuthorReference ar "
                                                        + "WHERE arr.Reference_idReference = ? AND "
                                                        + "      arr.AuthorReference_idAuthorReference = ar.idAuthorReference;";
  
  private final static String __SELECT_AUTHORREFERENCES_WITHOUT_GROUP = "SELECT ar.* "
                                                        + "FROM AuthorReference_Reference arr, AuthorReference ar "
                                                        + "WHERE arr.Reference_idReference = ? AND "
                                                        + "      arr.AuthorReference_idAuthorReference = ar.idAuthorReference AND ar.AuthorReferenceGroup_idAuthorReferenceGroup ISNULL;";
  
  private final static String __SELECT_REFERENCE_BY_ID = "SELECT * FROM Reference WHERE idReference = ?;";
  private final static String __SELECT_REFERENCE_BY_FULLREFERENCE = "SELECT * FROM Reference WHERE fullReference = ?;";
  private final static String __SELECT_REFERENCES = "SELECT * FROM Reference;";
  private final static String __SELECT_REFERENCES_WITHOUTGROUP = "SELECT * FROM Reference WHERE ReferenceGRoup_idReferenceGroup IS NULL;";
  private final static String __CHECK_REFERENCE_BY_FULLREFERENCE = "SELECT idReference FROM Reference WHERE fullReference = ?;";
  private final static String __CHECK_REFERENCE_BY_ID = "SELECT idReference FROM Reference WHERE idReference = ?;";
  
  private PreparedStatement statCheckReferenceByFullReference;
  private PreparedStatement statCheckReferenceById;
  private PreparedStatement statAddReference;
  private PreparedStatement statAddReferenceWithId;
  private PreparedStatement statRemoveReference;
  private PreparedStatement statSelectAuthorReferences;
  private PreparedStatement statSelectAuthorReferencesWithoutGroup;
  private PreparedStatement statSelectAuthorReferenceReference;
  private PreparedStatement statSelectDocuments;
  private PreparedStatement statSelectReferenceGroup;
  private PreparedStatement statSelectReferences;
  private PreparedStatement statSelectReferenceSource;
  private PreparedStatement statSelectReferenceSourceWithoutGroup;
  private PreparedStatement statSelectReferencesWithoutGroup;
  private PreparedStatement statSelectReferenceByFullReference;
  private PreparedStatement statSelectReferenceById;
  private PreparedStatement statUpdateDoi;
  private PreparedStatement statUpdateFormat;
  private PreparedStatement statUpdateFullReference;
  private PreparedStatement statUpdateIssue;
  private PreparedStatement statUpdatePage;
  private PreparedStatement statUpdateReference;
  private PreparedStatement statUpdateRererenceGroup;
  private PreparedStatement statUpdateReferenceSource;
  private PreparedStatement statUpdateVolume;
  private PreparedStatement statUpdateYear;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param kbm
   */
  public ReferenceDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    
    this.kbm = kbm;
    
    try {
    
      this.statCheckReferenceByFullReference = this.kbm.getConnection().prepareStatement(__CHECK_REFERENCE_BY_FULLREFERENCE);
      this.statCheckReferenceById = this.kbm.getConnection().prepareStatement(__CHECK_REFERENCE_BY_ID);
      this.statAddReference = this.kbm.getConnection().prepareStatement(__INSERT_REFERENCE, Statement.RETURN_GENERATED_KEYS);
      this.statAddReferenceWithId = this.kbm.getConnection().prepareStatement(__INSERT_REFERENCE_WITH_ID);
      this.statRemoveReference = this.kbm.getConnection().prepareStatement(__REMOVE_REFERENCE);
      this.statSelectAuthorReferences = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCES);
      this.statSelectAuthorReferencesWithoutGroup = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCES_WITHOUT_GROUP);
      this.statSelectAuthorReferenceReference = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCE_REFERENCE);
      this.statSelectDocuments = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENTS);
      this.statSelectReferenceGroup = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCEGROUP);
      this.statSelectReferences = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCES);
      this.statSelectReferenceSource = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCESOURCE);
      this.statSelectReferenceSourceWithoutGroup = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCESOURCE_WITHOUT_GROUP);
      this.statSelectReferencesWithoutGroup = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCES_WITHOUTGROUP);
      this.statSelectReferenceByFullReference = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCE_BY_FULLREFERENCE);
      this.statSelectReferenceById = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCE_BY_ID);
      this.statUpdateDoi = this.kbm.getConnection().prepareStatement(__UPDATE_DOI);
      this.statUpdateFormat = this.kbm.getConnection().prepareStatement(__UPDATE_FORMAT);
      this.statUpdateFullReference = this.kbm.getConnection().prepareStatement(__UPDATE_FULLREFERENCE);
      this.statUpdateIssue = this.kbm.getConnection().prepareStatement(__UPDATE_ISSUE);
      this.statUpdatePage = this.kbm.getConnection().prepareStatement(__UPDATE_PAGE);
      this.statUpdateReference = this.kbm.getConnection().prepareStatement(__UPDATE_REFERENCE);
      this.statUpdateRererenceGroup = this.kbm.getConnection().prepareStatement(__UPDATE_REFERENCEGROUP);
      this.statUpdateReferenceSource = this.kbm.getConnection().prepareStatement(__UPDATE_REFERENCESOURCE);
      this.statUpdateVolume = this.kbm.getConnection().prepareStatement(__UPDATE_VOLUME);
      this.statUpdateYear = this.kbm.getConnection().prepareStatement(__UPDATE_YEAR);
      
    } catch (SQLException e) {
      
      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param fullReference
   * @param volume
   * @param issue
   * @param page
   * @param year
   * @param doi
   * @param format
   * @return
   * @throws KnowledgeBaseException
   */
  public Integer addReference(String fullReference, String volume,
          String issue, String page, String year, String doi, String format, boolean notifyObservers)
          throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddReference.clearParameters();

      this.statAddReference.setString(1, fullReference);
      this.statAddReference.setString(2, volume);
      this.statAddReference.setString(3, issue);
      this.statAddReference.setString(4, page);
      this.statAddReference.setString(5, year);
      this.statAddReference.setString(6, doi);
      this.statAddReference.setString(7, format);

      if (this.statAddReference.executeUpdate() == 1 ) {

        id = this.statAddReference.getGeneratedKeys().getInt(1);

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddReferenceEvent(getReference(id)));
    }

    return id;
  }

  /**
   * 
   * @param referenceID
   * @param fullReference
   * @param volume
   * @param issue
   * @param page
   * @param year
   * @param doi
   * @param format
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addReference(Integer referenceID, String fullReference, String volume,
          String issue, String page, String year, String doi, String format, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;

    try {

      this.statAddReferenceWithId.clearParameters();

      this.statAddReferenceWithId.setInt(1, referenceID);
      this.statAddReferenceWithId.setString(2, fullReference);
      this.statAddReferenceWithId.setString(3, volume);
      this.statAddReferenceWithId.setString(4, issue);
      this.statAddReferenceWithId.setString(5, page);
      this.statAddReferenceWithId.setString(6, year);
      this.statAddReferenceWithId.setString(7, doi);
      this.statAddReferenceWithId.setString(8, format);

      result = this.statAddReferenceWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddReferenceEvent(getReference(referenceID)));
    }
    
    return result;
  }

  /**
   * 
   * @param reference
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addReference(Reference reference, boolean notifyObservers) throws KnowledgeBaseException {

     return addReference(reference.getReferenceID(), 
                         reference.getFullReference(),
                         reference.getVolume(),
                         reference.getIssue(),
                         reference.getPage(),
                         reference.getYear(),
                         reference.getDoi(), 
                         reference.getFormat(),
                         notifyObservers);
  }

  /**
   *
   * @param referenceID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeReference(Integer referenceID, boolean notifyObservers)
          throws KnowledgeBaseException {

    int i;
    boolean result = false;
    Reference reference = null;
    ReferenceGroup referenceGroup = null;
    ArrayList<AuthorReference> authorReferences = null;
    ArrayList<AuthorReference> authorReferencesWithoutGroup = null;
    ReferenceSource referenceSource = null;
    ReferenceSourceGroup referenceSourceGroup = null;
    ArrayList<Document> documents = null;
    
    // Save the information before remove
    if (notifyObservers) {
      
      reference = getReference(referenceID);
      referenceGroup = getReferenceGroup(referenceID);
      authorReferences = getAuthorReferences(referenceID);
      referenceSource = getReferenceSource(referenceID);
      documents = getDocuments(referenceID);
    }

    try {

      this.statRemoveReference.clearParameters();

      this.statRemoveReference.setInt(1, referenceID);

      result = this.statRemoveReference.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveReferenceEvent(reference));
      
      if (referenceGroup != null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceGroupEvent(CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().refreshReferenceGroup(referenceGroup)));
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceGroupRelationReferenceEvent());
        
      } else {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveReferenceWithoutGroupEvent(reference));
      }
      
      if (referenceSource != null) {
        
        ReferenceSourceDAO referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      
        referenceSource = referenceSourceDAO.refreshReferenceSource(referenceSource);
        
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceEvent(referenceSource));
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceSourceRelationReferenceEvent());
        
        referenceSourceGroup = referenceSourceDAO.getReferenceSourceGroup(referenceSource.getReferenceSourceID());
        
        if (referenceSourceGroup != null) {
        
          KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceGroupEvent(referenceSourceGroup));
        
        } else {
        
          KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceWithoutGroupEvent(referenceSource));
        }
      }
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceEvent(authorReferences));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceRelationAuthorReferenceEvent());
      
      TreeSet<AuthorReferenceGroup> authorReferenceGroups = new TreeSet<AuthorReferenceGroup>();
      authorReferencesWithoutGroup = new ArrayList<AuthorReference>();
      AuthorReferenceGroup authorReferenceGroup;
              
      for (i = 0; i < authorReferences.size(); i++) {
      
        authorReferenceGroup = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().getAuthorReferenceGroup(authorReferences.get(i).getAuthorReferenceID());
        
        if (authorReferenceGroup != null) {
        
          authorReferenceGroups.add(authorReferenceGroup);
          
        } else {
        
          authorReferencesWithoutGroup.add(authorReferences.get(i));
        }
      }
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceWithoutGroupEvent(authorReferencesWithoutGroup));
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceGroupEvent(new ArrayList<AuthorReferenceGroup>(authorReferenceGroups)));
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(documents));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationReferenceEvent());
    }
      
    return result;
  }

  /**
   *
   * @param idReference the reference's ID
   *
   * @return a <ocde>Reference</code> or null if there is not any reference
   *         with this ID
   *
   * @throws KnowledgeBaseException
   */
  public Reference getReference(Integer idReference)
          throws KnowledgeBaseException {

    ResultSet rs;
    Reference reference = null;

    try {

      this.statSelectReferenceById.clearParameters();

      this.statSelectReferenceById.setInt(1, idReference);

      rs = this.statSelectReferenceById.executeQuery();

      if (rs.next()) {

        reference = UtilsDAO.getInstance().getReference(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return reference;
  }

  /**
   *
   * @param fullReference the reference's full reference
   *
   * @return a <ocde>Reference</code> or null if there is not any reference
   *         with this full reference
   *
   * @throws KnowledgeBaseException
   */
  public Reference getReference(String fullReference)
          throws KnowledgeBaseException {

    ResultSet rs;
    Reference reference = null;

    try {

      this.statSelectReferenceByFullReference.clearParameters();

      this.statSelectReferenceByFullReference.setString(1, fullReference);

      rs = this.statSelectReferenceByFullReference.executeQuery();

      if (rs.next()) {

        reference = UtilsDAO.getInstance().getReference(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return reference;
  }

  /**
   *
   * @return a <ocde>Reference</code> or null if there is not any reference
   *         with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Reference> getReferences() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Reference> referenceList = new ArrayList<Reference>();

    try {

      this.statSelectReferences.clearParameters();

      rs = this.statSelectReferences.executeQuery();

      while (rs.next()) {

        referenceList.add(UtilsDAO.getInstance().getReference(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceList;
  }

  /**
   *
   * @return a <ocde>Reference</code> or null if there is not any reference
   *         with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Reference> getReferencesWithoutGroup() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Reference> referenceList = new ArrayList<Reference>();

    try {

      this.statSelectReferencesWithoutGroup.clearParameters();

      rs = this.statSelectReferencesWithoutGroup.executeQuery();

      while (rs.next()) {

        referenceList.add(UtilsDAO.getInstance().getReference(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceList;
  }

  /**
   * @param fullReference the fullReference to set
   */
  public boolean setFullReference(Integer referenceID, String fullReference, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    Reference reference;

    try {

      this.statUpdateFullReference.clearParameters();

      this.statUpdateFullReference.setString(1, fullReference);
      this.statUpdateFullReference.setInt(2, referenceID);

      result = this.statUpdateFullReference.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      reference = getReference(referenceID);
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceEvent(reference));
      
      if (getReferenceGroup(referenceID) == null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceWithoutGroupEvent(reference));
      }
    }
    
    return result;
  }

  /**
   * @param volume the volume to set
   */
  public boolean setVolume(Integer referenceID, String volume, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;
    Reference reference;

    try {

      this.statUpdateVolume.clearParameters();

      this.statUpdateVolume.setString(1, volume);
      this.statUpdateVolume.setInt(2, referenceID);

      result = this.statUpdateVolume.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      reference = getReference(referenceID);
      
      if (getReferenceGroup(referenceID) == null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceWithoutGroupEvent(reference));
      }
    }
    
    return result;
  }

  /**
   * @param issue the issue to set
   */
  public boolean setIssue(Integer referenceID, String issue, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;
    Reference reference;

    try {

      this.statUpdateIssue.clearParameters();

      this.statUpdateIssue.setString(1, issue);
      this.statUpdateIssue.setInt(2, referenceID);

      result = this.statUpdateIssue.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      reference = getReference(referenceID);
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceEvent(reference));
      
      if (getReferenceGroup(referenceID) == null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceWithoutGroupEvent(reference));
      }
    }
    
    return result;
  }

  /**
   * @param page the page to set
   */
  public boolean setPage(Integer referenceID, String page, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;
    Reference reference;

    try {

      this.statUpdatePage.clearParameters();

      this.statUpdatePage.setString(1, page);
      this.statUpdatePage.setInt(2, referenceID);

      result = this.statUpdatePage.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      reference = getReference(referenceID);
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceEvent(reference));
      
      if (getReferenceGroup(referenceID) == null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceWithoutGroupEvent(reference));
      }
    }
    
    return result;
  }

  /**
   * @param year the year to set
   */
  public boolean setYear(Integer referenceID, String year, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;
    Reference reference;

    try {

      this.statUpdateYear.clearParameters();

      this.statUpdateYear.setString(1, year);
      this.statUpdateYear.setInt(2, referenceID);

      result = this.statUpdateYear.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      reference = getReference(referenceID);
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceEvent(reference));
      
      if (getReferenceGroup(referenceID) == null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceWithoutGroupEvent(reference));
      }
    }
    
    return result;
  }

  /**
   * @param doi the doi to set
   */
  public boolean setDoi(Integer referenceID, String doi, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;
    Reference reference;

    try {

      this.statUpdateDoi.clearParameters();

      this.statUpdateDoi.setString(1, doi);
      this.statUpdateDoi.setInt(2, referenceID);

      result = this.statUpdateDoi.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      reference = getReference(referenceID);
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceEvent(reference));
      
      if (getReferenceGroup(referenceID) == null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceWithoutGroupEvent(reference));
      }
    }
    
    return result;
  }

  /**
   * @param format the format to set
   */
  public boolean setFormat(Integer referenceID, String format, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;
    Reference reference;

    try {

      this.statUpdateFormat.clearParameters();

      this.statUpdateFormat.setString(1, format);
      this.statUpdateFormat.setInt(2, referenceID);

      result = this.statUpdateFormat.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      reference = getReference(referenceID);
      
      if (getReferenceGroup(referenceID) == null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceWithoutGroupEvent(reference));
      }
    }
    
    return result;
  }

  /**
   * 
   * @param referenceID
   * @param fullReference
   * @param volume
   * @param issue
   * @param page
   * @param year
   * @param doi
   * @param format
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean updateReference(Integer referenceID, String fullReference, String volume,
          String issue, String page, String year, String doi, String format, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    Reference reference;

    try {

      this.statUpdateReference.clearParameters();

      this.statUpdateReference.setString(1, fullReference);
      this.statUpdateReference.setString(2, volume);
      this.statUpdateReference.setString(3, issue);
      this.statUpdateReference.setString(4, page);
      this.statUpdateReference.setString(5, year);
      this.statUpdateReference.setString(6, doi);
      this.statUpdateReference.setString(7, format);
      this.statUpdateReference.setInt(8, referenceID);

      result = this.statUpdateReference.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      reference = getReference(referenceID);
      
      if (getReferenceGroup(referenceID) == null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceWithoutGroupEvent(reference));
      }
    }
    
    return result;
  }

  /**
   * @param referenceGroupID the idReferecenGroup to set
   */
  public boolean setReferenceGroup(Integer referenceID, Integer referenceGroupID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    ReferenceGroup oldReferenceGroup = null;
    
    // Save the information before remove
    if (notifyObservers) {
    
      oldReferenceGroup = getReferenceGroup(referenceID);
    }

    try {

      this.statUpdateRererenceGroup.clearParameters();

      if (referenceGroupID != null) {
      
        this.statUpdateRererenceGroup.setInt(1, referenceGroupID);
        
      } else {
      
        this.statUpdateRererenceGroup.setNull(1, Types.NULL);
      }
      
      this.statUpdateRererenceGroup.setInt(2, referenceID);

      result = this.statUpdateRererenceGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      if (oldReferenceGroup != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceGroupEvent(CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO().refreshReferenceGroup(oldReferenceGroup)));
        
        if (referenceGroupID == null) {
          
          KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddReferenceWithoutGroupEvent(getReference(referenceID)));
        }
        
      } else {
      
        if (referenceGroupID != null) {
          
          KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveReferenceWithoutGroupEvent(getReference(referenceID)));
        }
      }

      if (referenceGroupID != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceGroupEvent(getReferenceGroup(referenceID)));
      }
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceGroupRelationReferenceEvent());
    }
      
    return result;
  }

  /**
   * @param referenceSourceID the idJournal to set
   */
  public boolean setReferenceSource(Integer referenceID, Integer referenceSourceID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    ReferenceSource oldReferenceSource = null;
    ReferenceSourceGroup referenceSourceGroup;
    
    // Save the information before remove
    if (notifyObservers) {
    
      oldReferenceSource = getReferenceSource(referenceID);
    }

    try {

      this.statUpdateReferenceSource.clearParameters();

      if (referenceSourceID != null) {
      
        this.statUpdateReferenceSource.setInt(1, referenceSourceID);
        
      } else {
      
        this.statUpdateReferenceSource.setNull(1, Types.NULL);
      }
      
      this.statUpdateReferenceSource.setInt(2, referenceID);

      result = this.statUpdateReferenceSource.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      ReferenceSourceDAO referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
      
      if (oldReferenceSource != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceEvent(referenceSourceDAO.refreshReferenceSource(oldReferenceSource)));
        
        referenceSourceGroup = referenceSourceDAO.getReferenceSourceGroup(oldReferenceSource.getReferenceSourceID());
        
        if (referenceSourceGroup != null) {
        
          KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceGroupEvent(referenceSourceGroup));
        }
      }

      if (referenceSourceID != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceEvent(getReferenceSource(referenceID)));
        
        referenceSourceGroup = referenceSourceDAO.getReferenceSourceGroup(referenceSourceID);
        
        if (referenceSourceGroup != null) {
        
          KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceGroupEvent(referenceSourceGroup));
        }
      }
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceSourceRelationReferenceEvent());
    }
      
    return result;
  }

  /**
   * 
   * @param referenceID
   * @return
   * @throws KnowledgeBaseException
   */
  public ReferenceGroup getReferenceGroup(Integer referenceID) throws KnowledgeBaseException {

    ResultSet rs;
    ReferenceGroup referenceGroup = null;

    try {

      this.statSelectReferenceGroup.clearParameters();

      this.statSelectReferenceGroup.setInt(1, referenceID);

      rs = this.statSelectReferenceGroup.executeQuery();

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
   * @param referenceID
   * @return
   * @throws KnowledgeBaseException
   */
  public ReferenceSource getReferenceSource(Integer referenceID) throws KnowledgeBaseException {

    ResultSet rs;
    ReferenceSource referenceSource = null;

    try {

      this.statSelectReferenceSource.clearParameters();

      this.statSelectReferenceSource.setInt(1, referenceID);

      rs = this.statSelectReferenceSource.executeQuery();

      if (rs.next()) {

        referenceSource = UtilsDAO.getInstance().getReferenceSource(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceSource;
  }
  
  /**
   * 
   * @param referenceID
   * @return
   * @throws KnowledgeBaseException
   */
  public ReferenceSource getReferenceSourceWithoutGroup(Integer referenceID) throws KnowledgeBaseException {

    ResultSet rs;
    ReferenceSource referenceSource = null;

    try {

      this.statSelectReferenceSourceWithoutGroup.clearParameters();

      this.statSelectReferenceSourceWithoutGroup.setInt(1, referenceID);

      rs = this.statSelectReferenceSourceWithoutGroup.executeQuery();

      if (rs.next()) {

        referenceSource = UtilsDAO.getInstance().getReferenceSource(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceSource;
  }

  /**
   *
   * @return an array with the documnents associated with this reference
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Document> getDocuments(Integer referenceID) throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Document> documentList = new ArrayList<Document>();

    try {

      this.statSelectDocuments.clearParameters();

      this.statSelectDocuments.setInt(1, referenceID);

      rs = this.statSelectDocuments.executeQuery();

      while (rs.next()) {

        documentList.add(UtilsDAO.getInstance().getDocument(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return documentList;
  }

  /**
   *
   * @return an array with the author references associated with this reference
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<AuthorReferenceReference> getAuthorReferenceReferences(Integer referenceID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<AuthorReferenceReference> referencesList = new ArrayList<AuthorReferenceReference>();

    try {

      this.statSelectAuthorReferenceReference.clearParameters();

      this.statSelectAuthorReferenceReference.setInt(1, referenceID);

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
   *
   * @return an array with the author references associated with this reference
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<AuthorReference> getAuthorReferences(Integer referenceID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<AuthorReference> authorReferences = new ArrayList<AuthorReference>();

    try {

      this.statSelectAuthorReferences.clearParameters();

      this.statSelectAuthorReferences.setInt(1, referenceID);

      rs = this.statSelectAuthorReferences.executeQuery();

      while (rs.next()) {

        authorReferences.add(UtilsDAO.getInstance().getAuthorReference(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferences;
  }
  
  /**
   * 
   * @param referenceID
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<AuthorReference> getAuthorReferencesWithoutGroup(Integer referenceID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<AuthorReference> authorReferences = new ArrayList<AuthorReference>();

    try {

      this.statSelectAuthorReferencesWithoutGroup.clearParameters();

      this.statSelectAuthorReferencesWithoutGroup.setInt(1, referenceID);

      rs = this.statSelectAuthorReferencesWithoutGroup.executeQuery();

      while (rs.next()) {

        authorReferences.add(UtilsDAO.getInstance().getAuthorReference(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferences;
  }

  /**
   * <p>Check if there is an <code>Reference</code> with this full
   * reference.</p>
   *
   * @param fullReference a string with the full reference
   *
   * @return true if there is an <code>Reference</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkReference(String fullReference)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;
    
    try {

      this.statCheckReferenceByFullReference.clearParameters();

      this.statCheckReferenceByFullReference.setString(1, fullReference);

      rs = this.statCheckReferenceByFullReference.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>Reference</code> with this ID.</p>
   *
   * @param idReference the reference's ID
   *
   * @return true if there is an <code>Reference</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkReference(Integer idReference)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;
    
    try {

      this.statCheckReferenceById.clearParameters();

      this.statCheckReferenceById.setInt(1, idReference);

      rs = this.statCheckReferenceById.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param referencesToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Reference> refreshReferences(ArrayList<Reference> referencesToRefresh) throws KnowledgeBaseException {
  
    int i;
    String query;
    ResultSet rs;
    ArrayList<Reference> references = new ArrayList<Reference>();
    
    i = 0;
    
    if (!referencesToRefresh.isEmpty()) {

      query = "SELECT * FROM Reference WHERE idReference IN (" + referencesToRefresh.get(i).getReferenceID();
      
      for (i = 1; i < referencesToRefresh.size(); i++) {
        
        query += ", " + referencesToRefresh.get(i).getReferenceID();
      }
      
      query += ");";
      
      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          references.add(UtilsDAO.getInstance().getReference(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }
    
    return references;
  }
  
  /**
   * 
   * @param referenceToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public Reference refreshReference(Reference referenceToRefresh) throws KnowledgeBaseException {
  
    return getReference(referenceToRefresh.getReferenceID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
