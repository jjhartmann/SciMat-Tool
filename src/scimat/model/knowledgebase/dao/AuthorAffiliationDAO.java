/*
 * AuthorAffiliationDAO.java
 *
 * Created on 01-mar-2011, 14:47:56
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.relation.AuthorRelationAffiliationEvent;
import scimat.knowledgebaseevents.event.update.UpdateAffiliationEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class AuthorAffiliationDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO Author_Affiliation(Affiliation_idAffiliation,Author_idAuthor) VALUES(?,?)
   * </pre>
   */
  private final static String __ADD_AUTHOR_AFFILIATION = "INSERT INTO Author_Affiliation(Affiliation_idAffiliation,Author_idAuthor) VALUES(?,?);";

  /**
   * <pre>
   * DELETE Author_Affiliation
     WHERE Affiliation_idAffiliation = ? AND
   *       Author_idAuthor = ?;
   * </pre>
   */
  private final static String __REMOVE_AUTHOR_AFFILIATION = "DELETE FROM Author_Affiliation "
                                                          + "WHERE Affiliation_idAffiliation = ? AND "
                                                          + "      Author_idAuthor = ?;";
  
  private final static String __CHECK_AUTHOR_AFFILIATION = "SELECT Author_idAuthor "
                                                         + "FROM Author_Affiliation WHERE "
                                                         + "Author_idAuthor = ? AND Affiliation_idAffiliation = ?;";
  
  private PreparedStatement statAddAuthorAffiliation;
  private PreparedStatement statRemoveAuthorAffiliation;
  private PreparedStatement statCheckAuthorAffiation;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param kbm
   */
  public AuthorAffiliationDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException{
    this.kbm = kbm;
    
    try {
    
      this.statAddAuthorAffiliation = this.kbm.getConnection().prepareStatement(__ADD_AUTHOR_AFFILIATION);
      this.statRemoveAuthorAffiliation = this.kbm.getConnection().prepareStatement(__REMOVE_AUTHOR_AFFILIATION);
      this.statCheckAuthorAffiation = this.kbm.getConnection().prepareStatement(__CHECK_AUTHOR_AFFILIATION);
      
    } catch (SQLException e) {
    
      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param affiliationID
   * @param authorID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addAuthorAffiliation(Integer authorID, Integer affiliationID, boolean notifyObservers) throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statAddAuthorAffiliation.clearParameters();

      this.statAddAuthorAffiliation.setInt(1, affiliationID);
      this.statAddAuthorAffiliation.setInt(2, authorID);

      result = this.statAddAuthorAffiliation.executeUpdate() > 0;

      // Notify to the observer
      if (notifyObservers) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAffiliationEvent(CurrentProject.getInstance().getFactoryDAO().getAffiliationDAO().getAffiliation(affiliationID)));
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorEvent(CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().getAuthor(authorID)));
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new AuthorRelationAffiliationEvent());
      }
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   *
   * @param affiliationID
   * @param authorID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeAuthorAffiliation(Integer authorID, Integer affiliationID, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;
    
    try {

      this.statRemoveAuthorAffiliation.clearParameters();

      this.statRemoveAuthorAffiliation.setInt(1, affiliationID);
      this.statRemoveAuthorAffiliation.setInt(2, authorID);

      result = this.statRemoveAuthorAffiliation.executeUpdate() > 0;

      // Notify to the observer
      if (notifyObservers) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAffiliationEvent(CurrentProject.getInstance().getFactoryDAO().getAffiliationDAO().getAffiliation(affiliationID)));
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorEvent(CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().getAuthor(authorID)));
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new AuthorRelationAffiliationEvent());
      }
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if the <code>Author</code> and <Code>Affiliation<Code>
   * are associated.</p>
   *
   * @param idAuhtor the author's ID
   * @param idAffiliation the affiliation's ID
   *
   * @return true if there is an association between both items.
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkAuthorAffiliation(Integer idAuhtor, Integer idAffiliation)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckAuthorAffiation.clearParameters();

      this.statCheckAuthorAffiation.setInt(1, idAuhtor);
      this.statCheckAuthorAffiation.setInt(2, idAffiliation);
      
      rs = this.statCheckAuthorAffiation.executeQuery();
      
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
