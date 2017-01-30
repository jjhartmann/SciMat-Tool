/*
 * DocumentAffiliationDAO.java
 *
 * Created on 01-mar-2011, 14:47:43
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.relation.DocumentRelationAffiliationEvent;
import scimat.knowledgebaseevents.event.update.UpdateAffiliationEvent;
import scimat.knowledgebaseevents.event.update.UpdateDocumentEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DocumentAffiliationDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO Document_Affiliation(Affiliation_idAffiliation,Document_idDocument) VALUES(?,?);
   * </pre>
   */
  private final static String __ADD_DOCUMENT_AFFILIATION = "INSERT INTO Document_Affiliation(Affiliation_idAffiliation,Document_idDocument) VALUES(?,?);";

  /**
   * <pre>
   * DELETE Document_Affiliation </br>
   * WHERE Affiliation_idAffiliation = ? AND
   *       Document_idDocument = ?;
   * </pre>
   */
  private final static String __REMOVE_DOCUMENT_AFFILIATION = "DELETE FROM Document_Affiliation "
                                                            + "WHERE Affiliation_idAffiliation = ? AND "
                                                            + "      Document_idDocument = ?;";
  
  private final static String __CHECK_DOCUMENT_AFFILIATION = "SELECT "
              + "Affiliation_idAffiliation FROM Document_Affiliation WHERE "
              + "Affiliation_idAffiliation = ? AND Document_idDocument = ?;";
  
  private PreparedStatement statAddDocumentAffiliation;
  private PreparedStatement statCheckDocumentAffiliation;
  private PreparedStatement statRemoveDocumentAffiliation;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param kbm
   */
  public DocumentAffiliationDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    
    this.kbm = kbm;
    
    try {
      
      this.statAddDocumentAffiliation = this.kbm.getConnection().prepareStatement(__ADD_DOCUMENT_AFFILIATION);
      this.statCheckDocumentAffiliation = this.kbm.getConnection().prepareStatement(__CHECK_DOCUMENT_AFFILIATION);
      this.statRemoveDocumentAffiliation = this.kbm.getConnection().prepareStatement(__REMOVE_DOCUMENT_AFFILIATION);
      
    } catch (SQLException e) {
    
      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param documentID
   * @param affiliationID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addDocumentAffiliation(Integer documentID, Integer affiliationID, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statAddDocumentAffiliation.clearParameters();

      this.statAddDocumentAffiliation.setInt(1, affiliationID);
      this.statAddDocumentAffiliation.setInt(2, documentID);

      result = this.statAddDocumentAffiliation.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAffiliationEvent(CurrentProject.getInstance().getFactoryDAO().getAffiliationDAO().getAffiliation(affiliationID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationAffiliationEvent());
    }
      
    return result;
  }

  /**
   * 
   * @param documentID
   * @param affiliationID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeDocumentAffiliation(Integer documentID, Integer affiliationID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statRemoveDocumentAffiliation.clearParameters();

      this.statRemoveDocumentAffiliation.setInt(1, affiliationID);
      this.statRemoveDocumentAffiliation.setInt(2, documentID);

      result = this.statRemoveDocumentAffiliation.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAffiliationEvent(CurrentProject.getInstance().getFactoryDAO().getAffiliationDAO().getAffiliation(affiliationID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationAffiliationEvent());
    }
      
    return result;
  }

  /**
   * <p>Check if the <code>Document</code> and <Code>Affiliation<Code>
   * are associated.</p>
   *
   * @param idDocument the document's ID
   * @param idAffiliation the affiliation's ID
   *
   * @return true if there is an association between both items.
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkDocumentAffiliation(Integer idDocument, Integer idAffiliation)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckDocumentAffiliation.clearParameters();

      this.statCheckDocumentAffiliation.setInt(1, idAffiliation);
      this.statCheckDocumentAffiliation.setInt(2, idDocument);
      
      rs = this.statCheckDocumentAffiliation.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
