/*
 * DocumentAuthorDAO.java
 *
 * Created on 01-mar-2011, 16:30:47
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.relation.DocumentRelationAuthorEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateDocumentEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.entity.DocumentAuthor;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DocumentAuthorDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO Document_Author(Author_idAuthor,Document_idDocument,position) VALUES(?,?,?);
   * </pre>
   */
  private final static String __ADD_DOCUMENT_AUTHOR = "INSERT INTO Document_Author(Author_idAuthor,Document_idDocument,position) VALUES(?,?,?);";

  /**
   * <pre>
   * DELETE Document_Author
   * WHERE Author_idAuthor = ? AND
   *       Document_idDocument = ?;
   * </pre>
   */
  private final static String __REMOVE_DOCUMENT_AUTHOR = "DELETE FROM Document_Author "
                                                       + "WHERE Author_idAuthor = ? AND "
                                                       + "      Document_idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document_Author
   * SET position = ?
   * WHERE Author_idAuthor = ? AND Document_idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_POSITION = "UPDATE Document_Author "
                                                + "SET position = ? "
                                                + "WHERE Author_idAuthor = ? AND Document_idDocument = ?;";
  
  /**
   * <pre>
   * </pre>
   */
  private final static String __SELECT_DOCUMENT_AUTHOR = "SELECT d.*, a.*, da.position "
                                                     + "FROM Document_Author da, Document d, Author a "
                                                     + "WHERE a.idAuthor = ? AND "
                                                     + "      d.idDocument = ? AND "
                                                     + "      a.idAuthor = da.Author_idAuthor AND "
                                                     + "      da.Document_idDocument = d.idDocument;";
  
  private final static String __CHECK_DOCUMENT_AUTHOR = "SELECT " +
              "Document_idDocument FROM Document_Author WHERE "
              + "Document_idDocument = ? AND Author_idAuthor = ?;";
  
  private final static String __SELECT_MAX_POSITION = "SELECT MAX(position) AS maxPosition "
          + "FROM Document_Author "
          + "WHERE Document_idDocument = ?;";
  
  private PreparedStatement statAddDocumentAuthor;
  private PreparedStatement statCheckDocumentAuthor;
  private PreparedStatement statRemoveDocumentAuthor;
  private PreparedStatement statSelectDocumentAuthor;
  private PreparedStatement statSelectMaxPosition;
  private PreparedStatement statUpdatePosition;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param kbm
   */
  public DocumentAuthorDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    
    this.kbm = kbm;
    
    try {

      this.statAddDocumentAuthor = this.kbm.getConnection().prepareStatement(__ADD_DOCUMENT_AUTHOR);
      this.statCheckDocumentAuthor = this.kbm.getConnection().prepareStatement(__CHECK_DOCUMENT_AUTHOR);
      this.statRemoveDocumentAuthor = this.kbm.getConnection().prepareStatement(__REMOVE_DOCUMENT_AUTHOR);
      this.statSelectDocumentAuthor = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENT_AUTHOR);
      this.statSelectMaxPosition = this.kbm.getConnection().prepareStatement(__SELECT_MAX_POSITION);
      this.statUpdatePosition = this.kbm.getConnection().prepareStatement(__UPDATE_POSITION);
      
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
   * @param authorID
   * @param position
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addDocumentAuthor(Integer documentID, Integer authorID, int position, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statAddDocumentAuthor.clearParameters();

      this.statAddDocumentAuthor.setInt(1, authorID);
      this.statAddDocumentAuthor.setInt(2, documentID);
      this.statAddDocumentAuthor.setInt(3, position);

      result = this.statAddDocumentAuthor.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      AuthorDAO authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      Author author = authorDAO.getAuthor(authorID);
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorEvent(author));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationAuthorEvent());

      AuthorGroup authorGroup = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().getAuthorGroup(authorID);
      
      if (authorGroup != null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorGroupEvent(authorGroup));
        
      } else {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorWithoutGroupEvent(author));
      }
      
    }
      
    return result;

  }

  /**
   * 
   * @param documentID
   * @param authorID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeDocumentAuthor(Integer documentID, Integer authorID, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statRemoveDocumentAuthor.clearParameters();

      this.statRemoveDocumentAuthor.setInt(1, authorID);
      this.statRemoveDocumentAuthor.setInt(2, documentID);

      result = this.statRemoveDocumentAuthor.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {
      
      AuthorDAO authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      Author author = authorDAO.getAuthor(authorID);

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorEvent(author));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationAuthorEvent());

      AuthorGroup authorGroup = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO().getAuthorGroup(authorID);
      
      if (authorGroup != null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorGroupEvent(authorGroup));
        
      } else {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorWithoutGroupEvent(author));
      }
      
    }
      
    return result;
  }

  /**
   * 
   * @param documentID
   * @param authorID
   * @param position
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setPosition(Integer documentID, Integer authorID, int position, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statUpdatePosition.clearParameters();

      this.statUpdatePosition.setInt(1, position);
      this.statUpdatePosition.setInt(2, authorID);
      this.statUpdatePosition.setInt(3, documentID);

      result = this.statUpdatePosition.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationAuthorEvent());
    }
      
    return result;
  }

  /**
   * <p>Check if the <code>Document</code> and <Code>Author<Code> are associated.</p>
   *
   * @param idDocument the document's ID
   * @param idAuhtor the author's ID
   *
   * @return true if there is an association between both items.
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkDocumentAuthor(Integer idDocument,Integer idAuhtor)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckDocumentAuthor.clearParameters();

      this.statCheckDocumentAuthor.setInt(1, idDocument);
      this.statCheckDocumentAuthor.setInt(2, idAuhtor);

      rs = this.statCheckDocumentAuthor.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param documentID
   * @param authorID
   * @return
   * @throws KnowledgeBaseException
   */
  public DocumentAuthor getDocumentAuthor(Integer documentID, Integer authorID) 
          throws KnowledgeBaseException {

    ResultSet rs;
    DocumentAuthor documentAuthor = null;

    try {

      this.statSelectDocumentAuthor.clearParameters();

      this.statSelectDocumentAuthor.setInt(1, authorID);
      this.statSelectDocumentAuthor.setInt(2, documentID);

      rs = this.statSelectDocumentAuthor.executeQuery();

      while (rs.next()) {
        
        documentAuthor = UtilsDAO.getInstance().getDocumentAuthor(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return documentAuthor;
  }
  
  /**
   * Retrieve the max position of the authors of the specified document.
   * 
   * @param documentID the document identifier
   * @return
   * @throws KnowledgeBaseException 
   */
  public int getMaxPosition(Integer documentID) throws KnowledgeBaseException {
  
    ResultSet rs;
    int position = 0;
    
    try {
      
      this.statSelectMaxPosition.clearParameters();
      
      this.statSelectMaxPosition.setInt(1, documentID);
      
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
