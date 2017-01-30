/*
 * DocumentWordDAO.java
 *
 * Created on 02-mar-2011, 14:24:26
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.relation.DocumentRelationWordEvent;
import scimat.knowledgebaseevents.event.update.UpdateDocumentEvent;
import scimat.knowledgebaseevents.event.update.UpdateWordEvent;
import scimat.knowledgebaseevents.event.update.UpdateWordGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateWordWithoutGroupEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.DocumentWord;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DocumentWordDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO Document_Word(Document_idDocument,Word_idWord,isAuthorWord,isSourceWord,isAddedWord) VALUES(?,?,?,?,?);
   * </pre>
   */
  private final static String __ADD_DOCUMENT_WORD = "INSERT INTO Document_Word(Document_idDocument,Word_idWord,isAuthorWord,isSourceWord,isAddedWord) VALUES(?,?,?,?,?);";

  /**
   * <pre>
   * DELETE Document_Word
   + WHERE Document_idDocument = ?
   * AND Word_idWord = ?;
   * </pre>
   */
  private final static String __REMOVE_DOCUMENT_WORD = "DELETE FROM Document_Word "
                                                     + "WHERE Document_idDocument = ? AND "
                                                     + "      Word_idWord = ?;";

  /**
   * <pre>
   * UPDATE Document_Word
   * SET isAuthorWord = ?
   * WHERE Word_idWord = ? AND
   *       Document_idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_AUTHORKEYWORD = "UPDATE Document_Word "
                                                     + "SET isAuthorWord = ? "
                                                     + "WHERE Word_idWord = ? AND "
                                                     + "      Document_idDocument = ?;";

   /**
    * <pre>
    * UPDATE Document_Word
    * SET isSourceWord = ?
    * WHERE Word_idWord = ? AND
    *       Document_idDocument = ?;
    * </pre>
    */
   private final static String __UPDATE_SOURCEKEYWORD = "UPDATE Document_Word "
                                                      + "SET isSourceWord = ? "
                                                      + "WHERE Word_idWord = ? AND Document_idDocument = ?;";

   /**
    * <pre>
    * UPDATE Document_Word
    * SET isAddedWord = ? 
    * WHERE Word_idWord = ? AND
    *       Document_idDocument = ?;
    * </pre>
    */
   private final static String __UPDATE_ADDEDKEYWORD = "UPDATE Document_Word "
                                                     + "SET isAddedWord = ? "
                                                     + "WHERE Word_idWord = ? AND "
                                                     + "      Document_idDocument = ?;";

  /**
   * <pre>
   * </pre>
   */
  private final static String __SELECT_DOCUMENT_WORD = "SELECT d.*, w.*, dw.isAuthorWord, dw.isSourceWord, dw.isAddedWord "
                                                     + "FROM Document_Word dw, Document d, Word w "
                                                     + "WHERE w.idWord = ? AND "
                                                     + "      d.idDocument = ? AND "
                                                     + "      w.idWord = dw.Word_idWord AND "
                                                     + "      dw.Document_idDocument = d.idDocument;";
  
  private final static String __CHECK_DOCUMENT_WORD = "SELECT " +
              "Document_idDocument FROM Document_Word WHERE "
              + "Document_idDocument = ? AND Word_idWord = ?;";
  
  private PreparedStatement statAddDocumentWord;
  private PreparedStatement statCheckDocumentWord;
  private PreparedStatement statRemoveDocumentWord;
  private PreparedStatement statSelectDocumentWord;
  private PreparedStatement statUpdateAddedWord;
  private PreparedStatement statUpdateAuthorWord;
  private PreparedStatement statUpdateSourcedWord;
  

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param kbm
   */
  public DocumentWordDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException{
    
    this.kbm = kbm;
    
    try {
    
      this.statAddDocumentWord = this.kbm.getConnection().prepareStatement(__ADD_DOCUMENT_WORD);
      this.statCheckDocumentWord = this.kbm.getConnection().prepareStatement(__CHECK_DOCUMENT_WORD);
      this.statRemoveDocumentWord = this.kbm.getConnection().prepareStatement(__REMOVE_DOCUMENT_WORD);
      this.statSelectDocumentWord = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENT_WORD);
      this.statUpdateAddedWord = this.kbm.getConnection().prepareStatement(__UPDATE_ADDEDKEYWORD);
      this.statUpdateAuthorWord = this.kbm.getConnection().prepareStatement(__UPDATE_AUTHORKEYWORD);
      this.statUpdateSourcedWord = this.kbm.getConnection().prepareStatement(__UPDATE_SOURCEKEYWORD);
      
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
   * @param wordID
   * @param isAuthorWord
   * @param isSourceWord
   * @param isAddedWord
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addDocumentWord(Integer documentID, Integer wordID,
          boolean isAuthorWord, boolean isSourceWord,
          boolean isAddedWord, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statAddDocumentWord.clearParameters();

      this.statAddDocumentWord.setInt(1, documentID);
      this.statAddDocumentWord.setInt(2, wordID);
      this.statAddDocumentWord.setBoolean(3, isAuthorWord);
      this.statAddDocumentWord.setBoolean(4, isSourceWord);
      this.statAddDocumentWord.setBoolean(5, isAddedWord);

      result = this.statAddDocumentWord.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {
      
      WordDAO wordDAO = new WordDAO(kbm);
      Word word = wordDAO.getWord(wordID);

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordEvent(word));
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationWordEvent());

      WordGroup wordGroup = wordDAO.getWordGroup(wordID);
      
      if (wordGroup != null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordGroupEvent(wordGroup));
        
      } else {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordWithoutGroupEvent(word));
      }
      
    }
      
    return result;
  }

  /**
   * 
   * @param documentID
   * @param wordID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeDocumentWord(Integer documentID, Integer wordID, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statRemoveDocumentWord.clearParameters();

      this.statRemoveDocumentWord.setInt(1, documentID);
      this.statRemoveDocumentWord.setInt(2, wordID);

      result = this.statRemoveDocumentWord.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {
      
      WordDAO wordDAO = new WordDAO(kbm);

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordEvent(wordDAO.getWord(wordID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationWordEvent());
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordWithoutGroupEvent(wordDAO.getWord(wordID)));

      WordGroup wordGroup = wordDAO.getWordGroup(wordID);
      
      if (wordGroup != null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordGroupEvent(wordGroup));
      }
      
    }
      
    return result;
  }

  /**
   * 
   * @param documentID
   * @param wordID
   * @param isAuthorWord
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setAuthorWord(Integer documentID, Integer wordID, boolean isAuthorWord, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateAuthorWord.clearParameters();

      this.statUpdateAuthorWord.setBoolean(1, isAuthorWord);
      this.statUpdateAuthorWord.setInt(2, wordID);
      this.statUpdateAuthorWord.setInt(3, documentID);

      result = this.statUpdateAuthorWord.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordEvent(CurrentProject.getInstance().getFactoryDAO().getWordDAO().getWord(wordID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationWordEvent());
    }
      
    return result;
  }

  /**
   * 
   * @param documentID
   * @param wordID
   * @param isSourceWord
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setSourceWord(Integer documentID, Integer wordID, boolean isSourceWord, boolean notifyObservers)
          throws KnowledgeBaseException {

    boolean result = false;
    
    try {

      this.statUpdateSourcedWord.clearParameters();

      this.statUpdateSourcedWord.setBoolean(1, isSourceWord);
      this.statUpdateSourcedWord.setInt(2, wordID);
      this.statUpdateSourcedWord.setInt(3, documentID);

      result = this.statUpdateSourcedWord.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordEvent(CurrentProject.getInstance().getFactoryDAO().getWordDAO().getWord(wordID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationWordEvent());
    }
      
    return result;
  }

  /**
   * 
   * @param documentID
   * @param wordID
   * @param isAddedWord
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setAddedWord(Integer documentID, Integer wordID, boolean isAddedWord, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateAddedWord.clearParameters();

      this.statUpdateAddedWord.setBoolean(1, isAddedWord);
      this.statUpdateAddedWord.setInt(2, wordID);
      this.statUpdateAddedWord.setInt(3, documentID);

      result = this.statUpdateAddedWord.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordEvent(CurrentProject.getInstance().getFactoryDAO().getWordDAO().getWord(wordID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationWordEvent());
    }
      
    return result;
  }

  /**
   * <p>Check if the <code>Document</code> and <Code>Word<Code> are associated.</p>
   *
   * @param idDocument the document's ID
   * @param idWord the word's ID
   *
   * @return true if there is an association between both items.
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkDocumentWord(Integer idDocument, Integer idWord)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckDocumentWord.clearParameters();

      this.statCheckDocumentWord.setInt(1, idDocument);
      this.statCheckDocumentWord.setInt(2, idWord);

      rs = this.statCheckDocumentWord.executeQuery();
      
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
   * @param wordID
   * @return
   * @throws KnowledgeBaseException
   */
  public DocumentWord getDocumentWord(Integer documentID, Integer wordID) 
          throws KnowledgeBaseException {

    ResultSet rs;
    DocumentWord documentWord = null;

    try {

      this.statSelectDocumentWord.clearParameters();

      this.statSelectDocumentWord.setInt(1, wordID);
      this.statSelectDocumentWord.setInt(2, documentID);

      rs = this.statSelectDocumentWord.executeQuery();

      while (rs.next()) {

        documentWord = UtilsDAO.getInstance().getDocumentWord(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return documentWord;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
