/*
 * AuthorReferenceReferenceDAO.java
 *
 * Created on 02-mar-2011, 12:37:43
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.relation.ReferenceRelationAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.entity.AuthorReferenceReference;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AuthorReferenceReferenceDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO AuthorReference_Reference(AuthorReference_idAuthorReference,Reference_idReference,position) VALUES(?,?,?);
   * </pre>
   */
  private final static String __ADD_AUTHORREFERENCE_REFERENCE = "INSERT INTO AuthorReference_Reference(AuthorReference_idAuthorReference,Reference_idReference,position) VALUES(?,?,?);";

  /**
   * <pre>
   * DELETE AuthorReference_Reference 
   * WHERE AuthorReference_idAuthorReference = ? AND
   *       Reference_idReference = ?;
   * </pre>
   */
  private final static String __REMOVE_AUTHORREFERENCE_REFERENCE = "DELETE FROM AuthorReference_Reference "
                                                                 + "WHERE AuthorReference_idAuthorReference = ? AND "
                                                                 + "      Reference_idReference = ?;";

  /**
   * <pre>
   * UPDATE AuthorReference_Reference
   * SET position = ?
   * WHERE AuthorReference_idAuthorReference = ? AND
   *       Reference_idReference = ?;
   * </pre>
   */
  private final static String __UPDATE_POSITION = "UPDATE AuthorReference_Reference "
                                                + "SET position = ? "
                                                + "WHERE AuthorReference_idAuthorReference = ? AND "
                                                + "      Reference_idReference = ?;";
  
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
  private final static String __SELECT_AUTHORREFERENCES_REFERENCE = "SELECT r.*, ar.*, arr.position "
                                                        + "FROM AuthorReference_Reference arr, AuthorReference ar, Reference r "
                                                        + "WHERE r.idReference = ? AND "
                                                        + "      arr.AuthorReference_idAuthorReference = ? AND"
                                                        + "      r.idReference = arr.Reference_idReference AND "
                                                        + "      arr.AuthorReference_idAuthorReference = ar.idAuthorReference;";
  
  private final static String __CHECK_AUTHORREFERENCE_REFERENCE = "SELECT " +
              "AuthorReference_idAuthorReference FROM AuthorReference_Reference "
              + "WHERE AuthorReference_idAuthorReference = ? AND "
              + "Reference_idReference = ?;";
  
  private final static String __SELECT_MAX_POSITION = "SELECT MAX(position) AS maxPosition "
          + "FROM AuthorReference_Reference "
          + "WHERE Reference_idReference = ?;";
  
  private PreparedStatement statAddAuthorReferenceReference;
  private PreparedStatement statCheckAuthorReferenceReference;
  private PreparedStatement statRemoveAuthorReferenceReference;
  private PreparedStatement statSelectAuthorReferenceReference;
  private PreparedStatement statSelectMaxPosition;
  private PreparedStatement statUpdatePosition;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param kbm
   */
  public AuthorReferenceReferenceDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    this.kbm = kbm;
    
    try {
    
      statAddAuthorReferenceReference = this.kbm.getConnection().prepareStatement(__ADD_AUTHORREFERENCE_REFERENCE);
      statCheckAuthorReferenceReference = this.kbm.getConnection().prepareStatement(__CHECK_AUTHORREFERENCE_REFERENCE);
      statRemoveAuthorReferenceReference = this.kbm.getConnection().prepareStatement(__REMOVE_AUTHORREFERENCE_REFERENCE);
      statSelectAuthorReferenceReference = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORREFERENCES_REFERENCE);
      statSelectMaxPosition = this.kbm.getConnection().prepareStatement(__SELECT_MAX_POSITION);
      statUpdatePosition = this.kbm.getConnection().prepareStatement(__UPDATE_POSITION);
      
    } catch (SQLException e) {
    
      //throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      throw new KnowledgeBaseException(e);
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param referenceID
   * @param authorReferenceID
   * @param position
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addAuthorReferenceReference(Integer referenceID, Integer authorReferenceID, int position, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statAddAuthorReferenceReference.clearParameters();

      this.statAddAuthorReferenceReference.setInt(1, authorReferenceID);
      this.statAddAuthorReferenceReference.setInt(2, referenceID);
      this.statAddAuthorReferenceReference.setInt(3, position);

      result = this.statAddAuthorReferenceReference.executeUpdate() > 0;

    } catch (SQLException e) {
      
      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceEvent(CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().getAuthorReference(authorReferenceID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceEvent(CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().getReference(referenceID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceRelationAuthorReferenceEvent());

      AuthorReferenceGroup authorReferenceGroup = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().getAuthorReferenceGroup(authorReferenceID);
      
      if (authorReferenceGroup != null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceGroupEvent(authorReferenceGroup));
      }
      
    }
      
    return result;

  }

  /**
   *
   * @param referenceID
   * @param authorReferenceID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeAuthorReferenceReference(Integer referenceID, Integer authorReferenceID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statRemoveAuthorReferenceReference.clearParameters();

      this.statRemoveAuthorReferenceReference.setInt(1, authorReferenceID);
      this.statRemoveAuthorReferenceReference.setInt(2, referenceID);

      result = this.statRemoveAuthorReferenceReference.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    /// Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceEvent(CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().getAuthorReference(authorReferenceID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceEvent(CurrentProject.getInstance().getFactoryDAO().getReferenceDAO().getReference(referenceID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceRelationAuthorReferenceEvent());

      AuthorReferenceGroup authorReferenceGroup = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO().getAuthorReferenceGroup(authorReferenceID);
      
      if (authorReferenceGroup != null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceGroupEvent(authorReferenceGroup));
      }
      
    }
      
    return result;
  }

  /**
   *
   * @param referenceID
   * @param authorReferenceID
   * @param position
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setPosition(Integer referenceID, Integer authorReferenceID, int position, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdatePosition.clearParameters();

      this.statUpdatePosition.setInt(1, position);
      this.statUpdatePosition.setInt(2, authorReferenceID);
      this.statUpdatePosition.setInt(3, referenceID);

      result = this.statUpdatePosition.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new ReferenceRelationAuthorReferenceEvent());
    }
  
    return result;
  }
  
  /**
   * <p>Check if the <code>AuthorReference</code> and <Code>Reference<Code>
   * are associated.</p>
   *
   * @param idAuhtorReference the author reference's ID
   * @param idReference the reference's ID
   *
   * @return true if there is an association between both items.
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkAuthorReferenceReference(Integer idAuhtorReference, Integer idReference)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckAuthorReferenceReference.clearParameters();

      this.statCheckAuthorReferenceReference.setInt(1, idAuhtorReference);
      this.statCheckAuthorReferenceReference.setInt(2, idReference);

      rs = this.statCheckAuthorReferenceReference.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param referenceID
   * @param authorReferenceID
   * @return
   * @throws KnowledgeBaseException
   */
  public AuthorReferenceReference getAuthorReferenceReference(Integer referenceID, Integer authorReferenceID) 
          throws KnowledgeBaseException {

    ResultSet rs;
    AuthorReferenceReference authorReferenceReference = null;

    try {

      this.statSelectAuthorReferenceReference.clearParameters();
      
      this.statSelectAuthorReferenceReference.setInt(1, referenceID);
      this.statSelectAuthorReferenceReference.setInt(2, authorReferenceID);

      rs = this.statSelectAuthorReferenceReference.executeQuery();

      while (rs.next()) {

        authorReferenceReference = UtilsDAO.getInstance().getAuthorReferenceReference(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authorReferenceReference;
  }
  
  /**
   * Retrieve the max position of the authors of the specified reference.
   * 
   * @param referenceID the reference identifier
   * @return
   * @throws KnowledgeBaseException 
   */
  public int getMaxPosition(Integer referenceID) throws KnowledgeBaseException {
  
    ResultSet rs;
    int position = 0;
    
    try {
      
      this.statSelectMaxPosition.clearParameters();
      
      this.statSelectMaxPosition.setInt(1, referenceID);
      
      rs = this.statSelectMaxPosition.executeQuery();
      
      if (rs.next()) {
      
        position = rs.getInt("maxPosition");
      }
      
      rs.close();
      
    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    return position;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
