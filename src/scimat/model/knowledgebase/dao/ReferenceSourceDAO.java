/*
 * ReferenceSourceDAO.java
 *
 * Created on 28-oct-2010, 20:10:46
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddReferenceSourceEvent;
import scimat.knowledgebaseevents.event.add.AddReferenceSourceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.relation.ReferenceSourceGroupRelationReferenceSourceEvent;
import scimat.knowledgebaseevents.event.relation.ReferenceSourceRelationReferenceEvent;
import scimat.knowledgebaseevents.event.remove.RemoveReferenceSourceEvent;
import scimat.knowledgebaseevents.event.remove.RemoveReferenceSourceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceSourceEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceSourceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceSourceWithoutGroupEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class ReferenceSourceDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO ReferenceSource(source) VALUES(?);
   * </pre>
   */
  private final static String __INSERT_REFERENCESOURCE = "INSERT INTO ReferenceSource(source) VALUES(?);";

  /**
   * <pre>
   * INSERT INTO ReferenceSource(idReferenceSource,source) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_REFERENCESOURCE_WITH_ID = "INSERT INTO ReferenceSource(idReferenceSource,source) VALUES(?,?);";

  /**
   * <pre>
   * DELETE ReferenceSource
   * WHERE idReferenceSource = ?;
   * </pre>
   */
  private final static String __REMOVE_REFERENCESOURCE = "DELETE FROM ReferenceSource "
                                                       + "WHERE idReferenceSource = ?;";

  /**
   * <pre>
   * UPDATE ReferenceSource 
   * SET source = ?
   * WHERE idReferenceSource = ?;
   * </pre>
   */
  private final static String __UPDATE_SOURCE = "UPDATE ReferenceSource SET source = ? WHERE idReferenceSource = ?;";

  /**
   * <pre>
   * UPDATE ReferenceSource SET 
   * ReferenceSourceGroup_idReferenceSourceGroup = ?
   * WHERE idReferenceSource = ?;
   * </pre>
   */
  private final static String __UPDATE_REFERENCESOURCEGROUP = "UPDATE ReferenceSource "
                                                            + "SET ReferenceSourceGroup_idReferenceSourceGroup = ? "
                                                            + "WHERE idReferenceSource = ?;";

  /**
   * <pre>
   * SELECT rg.idReferenceSourceGroup, rg.groupName, rg.stopGroup
   * FROM ReferenceSource r, ReferenceSourceGroup rg
   * WHERE r.idReferenceSource = ? AND
   *       r.ReferenceSourceGroup_idReferenceSourceGroup = rg.idReferenceSourceGroup;
   * </pre>
   */
  private final static String __SELECT_REFERENCESOURCEGROUP = "SELECT rsg.* "
                                                      + "FROM ReferenceSource rs, ReferenceSourceGroup rsg "
                                                      + "WHERE rs.idReferenceSource = ? AND "
                                                      + "      rs.ReferenceSourceGroup_idReferenceSourceGroup = rsg.idReferenceSourceGroup;";
  
  /**
   * <pre>
   * SELECT r.idReference, r.fullReference, r.volume, r.issue, r.page, r.doi, r.format, r.year "
   * FROM ReferenceSource rs, Reference r "
   * WHERE rs.ReferenceSource_idReferenceSource = ? AND
   *       rs.Reference_idReference = r.idReference;
   * </pre>
   */
  private final static String __SELECT_REFERENCES = "SELECT r.* "
                                                 + "FROM ReferenceSource rs, Reference r "
                                                 + "WHERE rs.idReferenceSource = ? AND "
                                                 + "      rs.idReferenceSource = r.ReferenceSource_idReferenceSource;";
  
  private final static String __SELECT_REFERENCESOURCE_BY_ID = "SELECT * FROM ReferenceSource WHERE idReferenceSource = ?;";
  private final static String __SELECT_REFERENCESOURCE_BY_SOURCE = "SELECT * FROM ReferenceSource WHERE source = ?;";
  private final static String __SELECT_REFERENCESOURCES = "SELECT * FROM ReferenceSource;";
  private final static String __SELECT_REFERENCESOURCES_WITHOUT_GROUP = "SELECT * FROM ReferenceSource WHERE ReferenceSourceGroup_idReferenceSourceGroup IS NULL;";
  private final static String __CHECK_REFERENCESOURCE_BY_SOURCE = "SELECT idReferenceSource FROM ReferenceSource WHERE source = ?;";
  private final static String __CHECK_REFERENCESOURCE_BY_ID = "SELECT idReferenceSource FROM ReferenceSource WHERE idReferenceSource = ?;";
  
  private PreparedStatement statCheckReferenceSourceById;
  private PreparedStatement statCheckReferenceSourceBySource;
  private PreparedStatement statAddReferenceSource;
  private PreparedStatement statAddReferenceSourceWithId;
  private PreparedStatement statRemoveReferenceSource;
  private PreparedStatement statSelectReferences;
  private PreparedStatement statSelectReferenceSourceGroup;
  private PreparedStatement statSelectReferenceSources;
  private PreparedStatement statSelectReferenceSourcesWithoutGroup;
  private PreparedStatement statSelectReferenceSourceById;
  private PreparedStatement statSelectReferenceSourceBySource;
  private PreparedStatement statUpdateReferenceSourceGroup;
  private PreparedStatement statUpdateSource;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param kbm
   */
  public ReferenceSourceDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    
    this.kbm = kbm;
    
    try {
    
      this.statCheckReferenceSourceById = this.kbm.getConnection().prepareStatement(__CHECK_REFERENCESOURCE_BY_ID);
      this.statCheckReferenceSourceBySource = this.kbm.getConnection().prepareStatement(__CHECK_REFERENCESOURCE_BY_SOURCE);
      this.statAddReferenceSource = this.kbm.getConnection().prepareStatement(__INSERT_REFERENCESOURCE, Statement.RETURN_GENERATED_KEYS);
      this.statAddReferenceSourceWithId = this.kbm.getConnection().prepareStatement(__INSERT_REFERENCESOURCE_WITH_ID);
      this.statRemoveReferenceSource = this.kbm.getConnection().prepareStatement(__REMOVE_REFERENCESOURCE);
      this.statSelectReferences = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCES);
      this.statSelectReferenceSourceGroup = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCESOURCEGROUP);
      this.statSelectReferenceSources = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCESOURCES);
      this.statSelectReferenceSourcesWithoutGroup = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCESOURCES_WITHOUT_GROUP);
      this.statSelectReferenceSourceById = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCESOURCE_BY_ID);
      this.statSelectReferenceSourceBySource = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCESOURCE_BY_SOURCE);
      this.statUpdateReferenceSourceGroup = this.kbm.getConnection().prepareStatement(__UPDATE_REFERENCESOURCEGROUP);
      this.statUpdateSource = this.kbm.getConnection().prepareStatement(__UPDATE_SOURCE);
      
     } catch (SQLException e) {
     
       throw new KnowledgeBaseException(e.getMessage(), e.getCause());
     }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param source
   * @return
   * @throws KnowledgeBaseException
   */
  public Integer addReferenceSource(String source, boolean notifyObservers)
          throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddReferenceSource.clearParameters();

      this.statAddReferenceSource.setString(1, source);

      if (this.statAddReferenceSource.executeUpdate() == 1 ) {

        id = this.statAddReferenceSource.getGeneratedKeys().getInt(1);
        this.statAddReferenceSource.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddReferenceSourceEvent(getReferenceSource(id)));
    }
    
    return id;
  }

  /**
   * 
   * @param referenceSourceID
   * @param source
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addReferenceSource(Integer referenceSourceID, String source, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statAddReferenceSourceWithId.clearParameters();

      this.statAddReferenceSourceWithId.setInt(1, referenceSourceID);
      this.statAddReferenceSourceWithId.setString(2, source);

      result = this.statAddReferenceSourceWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddReferenceSourceEvent(getReferenceSource(referenceSourceID)));
    }
    
    return result;
  }

  /**
   * 
   * @param referenceSource
   * @return
   * @throws KnowledgeBaseException 
   */
  public boolean addReferenceSource(ReferenceSource referenceSource, boolean notifyObservers)
          throws KnowledgeBaseException {

    return addReferenceSource(referenceSource.getReferenceSourceID(),
                              referenceSource.getSource(),
                              notifyObservers);
  }

  /**
   *
   * @param referenceSourceID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeReferenceSource(Integer referenceSourceID, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;
    ReferenceSource referenceSource = null;
    ReferenceSourceGroup referenceSourceGroup = null;
    
    // Save the information before remove
    if (notifyObservers) {
      
      referenceSource = getReferenceSource(referenceSourceID);
      referenceSourceGroup = getReferenceSourceGroup(referenceSourceID);
    }

    try {

      this.statRemoveReferenceSource.clearParameters();

      this.statRemoveReferenceSource.setInt(1, referenceSourceID);

      result = this.statRemoveReferenceSource.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveReferenceSourceEvent(referenceSource));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceSourceRelationReferenceEvent());
      
      if (referenceSourceGroup != null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceGroupEvent(CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO().refreshReferenceSourceGroup(referenceSourceGroup)));
        
      } else {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveReferenceSourceWithoutGroupEvent(referenceSource));
      }
    }
      
    return result;  
  }

  /**
   *
   * @param idReferenceSource the reference source's ID
   *
   * @return a <ocde>ReferenceSource</code> or null if there is not any
   *         reference source with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ReferenceSource getReferenceSource(Integer idReferenceSource)
          throws KnowledgeBaseException {

    ResultSet rs;
    ReferenceSource referenceSource = null;

    try {

      this.statSelectReferenceSourceById.clearParameters();

      this.statSelectReferenceSourceById.setInt(1, idReferenceSource);

      rs = this.statSelectReferenceSourceById.executeQuery();

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
   * @param source the reference source's source
   *
   * @return a <ocde>ReferenceSource</code> or null if there is not any
   *         reference source with this source
   *
   * @throws KnowledgeBaseException
   */
  public ReferenceSource getReferenceSource(String source)
          throws KnowledgeBaseException {

    ResultSet rs;
    ReferenceSource referenceSource = null;

    try {

      this.statSelectReferenceSourceBySource.clearParameters();

      this.statSelectReferenceSourceBySource.setString(1, source);

      rs = this.statSelectReferenceSourceBySource.executeQuery();

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
   * @return a <ocde>ReferenceSource</code> or null if there is not any
   *         reference source with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<ReferenceSource> getReferenceSources()
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<ReferenceSource> referenceSourceList = new ArrayList<ReferenceSource>();

    try {

      this.statSelectReferenceSources.clearParameters();

      rs = this.statSelectReferenceSources.executeQuery();

      while (rs.next()) {

        referenceSourceList.add(UtilsDAO.getInstance().getReferenceSource(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceSourceList;
  }
  
  /**
   *
   * @return a <ocde>ReferenceSource</code> or null if there is not any
   *         reference source with this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<ReferenceSource> getReferenceSourcesWithoutGroup()
          throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<ReferenceSource> referenceSourceList = new ArrayList<ReferenceSource>();

    try {

      this.statSelectReferenceSourcesWithoutGroup.clearParameters();

      rs = this.statSelectReferenceSourcesWithoutGroup.executeQuery();

      while (rs.next()) {

        referenceSourceList.add(UtilsDAO.getInstance().getReferenceSource(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return referenceSourceList;
  }

  /**
   * @param source the source to set
   */
  public boolean setSource(Integer referenceSourceID, String source, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;
    ReferenceSource referenceSource;

    try {

      this.statUpdateSource.clearParameters();

      this.statUpdateSource.setString(1, source);
      this.statUpdateSource.setInt(2, referenceSourceID);

      result = this.statUpdateSource.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
      
      referenceSource = getReferenceSource(referenceSourceID);
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceEvent(referenceSource));
      
      if (getReferenceSourceGroup(referenceSourceID) == null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceWithoutGroupEvent(referenceSource));
      }
    }
    
    return result;
  }

  /**
   * @param referenceSourceGroupID the idReferenceSourceGroup to set
   */
  public boolean setReferenceSourceGroup(Integer referenceSourceID, Integer referenceSourceGroupID, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;
    ReferenceSourceGroup oldReferenceSourceGroup = null;
    
    // Save the information before remove
    if (notifyObservers) {
    
      oldReferenceSourceGroup = getReferenceSourceGroup(referenceSourceID);
    }

    try {

      this.statUpdateReferenceSourceGroup.clearParameters();

      if (referenceSourceGroupID != null) {
        
      this.statUpdateReferenceSourceGroup.setInt(1, referenceSourceGroupID);
        
      } else {
      
        this.statUpdateReferenceSourceGroup.setNull(1, Types.NULL);
      }
      
      this.statUpdateReferenceSourceGroup.setInt(2, referenceSourceID);

      result = this.statUpdateReferenceSourceGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      if (oldReferenceSourceGroup != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceGroupEvent(CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO().refreshReferenceSourceGroup(oldReferenceSourceGroup)));
        
        if (referenceSourceGroupID == null) {
          
          KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddReferenceSourceWithoutGroupEvent(getReferenceSource(referenceSourceID)));
        }
        
      } else {
      
        if (referenceSourceGroupID != null) {
          
          KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveReferenceSourceWithoutGroupEvent(getReferenceSource(referenceSourceID)));
        }
      }

      if (referenceSourceGroupID != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceGroupEvent(getReferenceSourceGroup(referenceSourceID)));
      }
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceSourceGroupRelationReferenceSourceEvent());
    }
      
    return result;
  }

  /**
   * 
   * @param referenceSourceID
   * @return
   * @throws KnowledgeBaseException
   */
  public ReferenceSourceGroup getReferenceSourceGroup(Integer referenceSourceID)
          throws KnowledgeBaseException {

    ResultSet rs;
    ReferenceSourceGroup referenceSourceGroup = null;

    try {

      this.statSelectReferenceSourceGroup.clearParameters();

      this.statSelectReferenceSourceGroup.setInt(1, referenceSourceID);

      rs = this.statSelectReferenceSourceGroup.executeQuery();

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
   * @return an array with the references associated with this reference source
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Reference> getReferences(Integer referenceSourceID) throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Reference> referenceList = new ArrayList<Reference>();

    try {

      this.statSelectReferences.clearParameters();

      this.statSelectReferences.setInt(1, referenceSourceID);

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
   * <p>Check if there is an <code>ReferenceSource</code> with this
   * reference's source.</p>
   *
   * @param source a string with the reference's source
   *
   * @return true if there is an <code>ReferenceSource</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkReferenceSource(String source)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckReferenceSourceBySource.clearParameters();

      this.statCheckReferenceSourceBySource.setString(1, source);

      rs = this.statCheckReferenceSourceBySource.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>ReferenceSource</code> with this ID.</p>
   *
   * @param idReferenceSource the reference source's ID
   *
   * @return true if there is an <code>ReferenceSource</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkReferenceSource(Integer idReferenceSource)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckReferenceSourceById.clearParameters();

      this.statCheckReferenceSourceById.setInt(1, idReferenceSource);

      rs = this.statCheckReferenceSourceById.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param referenceSourcesToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<ReferenceSource> refreshReferenceSources(ArrayList<ReferenceSource> referenceSourcesToRefresh) throws KnowledgeBaseException {
  
    int i;
    String query;
    ResultSet rs;
    ArrayList<ReferenceSource> referenceSources = new ArrayList<ReferenceSource>();
    
    i = 0;
    
    if (!referenceSourcesToRefresh.isEmpty()) {

      query = "SELECT * FROM ReferenceSource WHERE idReferenceSource IN (" + referenceSourcesToRefresh.get(i).getReferenceSourceID();
      
      for (i = 1; i < referenceSourcesToRefresh.size(); i++) {
        
        query += ", " + referenceSourcesToRefresh.get(i).getReferenceSourceID();
      }
      
      query += ");";
      
      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          referenceSources.add(UtilsDAO.getInstance().getReferenceSource(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }
    
    return referenceSources;
  }
  
  /**
   * 
   * @param referenceSourceToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ReferenceSource refreshReferenceSource(ReferenceSource referenceSourceToRefresh) throws KnowledgeBaseException {
  
    return getReferenceSource(referenceSourceToRefresh.getReferenceSourceID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
