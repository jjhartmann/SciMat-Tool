/*
 * WordDAO.java
 *
 * Created on 21-oct-2010, 17:49:44
 */
package scimat.model.knowledgebase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.knowledgebaseevents.event.add.AddWordEvent;
import scimat.knowledgebaseevents.event.add.AddWordWithoutGroupEvent;
import scimat.knowledgebaseevents.event.relation.DocumentRelationWordEvent;
import scimat.knowledgebaseevents.event.relation.WordGroupRelationWordEvent;
import scimat.knowledgebaseevents.event.remove.RemoveWordEvent;
import scimat.knowledgebaseevents.event.remove.RemoveWordWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateDocumentEvent;
import scimat.knowledgebaseevents.event.update.UpdateWordEvent;
import scimat.knowledgebaseevents.event.update.UpdateWordGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateWordWithoutGroupEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.DocumentWord;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class WordDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO Word(wordName) VALUES(?);
   * </pre>
   */
  private final static String __INSERT_WORD = "INSERT INTO Word(wordName) VALUES(?);";

  /**
   * <pre>
   * INSERT INTO Word(idWord,wordName) VALUES(?,?);
   * </pre>
   */
  private final static String __INSERT_WORD_WITH_ID = "INSERT INTO Word(idWord,wordName) VALUES(?,?);";

  /**
   * <pre>
   * DELETE Word
   * WHERE idWord = ?;
   * </pre>
   */
  private final static String __REMOVE_WORD = "DELETE FROM Word "
                                            + "WHERE idWord = ?;";

  /**
   * <pre>
   * UPDATE Word 
   * SET wordName = ?
   * WHERE idWord = ?;
   * </pre>
   */
  private final static String __UPDATE_WORDNAME = "UPDATE Word "
                                                + "SET wordName = ? "
                                                + "WHERE idWord = ?;";

  /**
   * <pre>
   * UPDATE Word
   * SET WordGroup_idWordGroup = ?
   * WHERE idWord = ?;
   * </pre>
   */
  private final static String __UPDATE_WORDGROUP = "UPDATE Word SET WordGroup_idWordGroup = ? WHERE idWord = ?;";

  /**
   * <pre>
   * SELECT wg.idWordGroup, wg.groupName, wg.stopGroup
   * FROM Word w, WordGroup wg
   * WHERE w.idWord = ? AND r.WordGroup_idWordGroup = wg.idWordGroup;
   * </pre>
   */
  private final static String __SELECT_WORDGROUP = "SELECT wg.* "
                                                 + "FROM Word w, WordGroup wg "
                                                 + "WHERE w.idWord = ? AND "
                                                 + "      w.WordGroup_idWordGroup = wg.idWordGroup;";
  
  /**
   * <pre>
   * </pre>
   */
  private final static String __SELECT_DOCUMENT_WORD = "SELECT d.*, w.*, dw.isAuthorWord, dw.isSourceWord, dw.isAddedWord "
                                                     + "FROM Document_Word dw, Document d, Word w "
                                                     + "WHERE w.idWord = ? AND "
                                                     + "      w.idWord = dw.Word_idWord AND "
                                                     + "      dw.Document_idDocument = d.idDocument;";
  
  /**
   * <pre>
   * </pre>
   */
  private final static String __SELECT_DOCUMENT = "SELECT d.* "
                                                + "FROM Document_Word dw, Document d "
                                                + "WHERE dw.Word_idWord = ? AND"
                                                + "      dw.Document_idDocument = d.idDocument";
  
  /**
   * <pre>
   * </pre>
   */
  private final static String __SELECT_DOCUMENT_ALL = "SELECT d.* "
                                                    + "FROM Document_Word dw, Document d "
                                                    + "WHERE dw.Word_idWord = ? AND"
                                                    + "      dw.Document_idDocument = d.idDocument;";
  
  private final static String __SELECT_WORD_BY_ID = "SELECT * FROM Word WHERE idWord = ?;";
  private final static String __SELECT_WORD_BY_NAME = "SELECT * FROM Word WHERE wordName = ?;";
  private final static String __SELECT_WORDS = "SELECT * FROM Word;";
  private final static String __SELECT_WORDS_WITHOUTGROUP = "SELECT * FROM Word WHERE WordGroup_idWordGroup IS NULL;";
  private final static String __CHECK_WORD_BY_ID = "SELECT idWord FROM Word WHERE idWord = ?;";
  private final static String __CHECK_WORD_BY_NAME = "SELECT idWord FROM Word WHERE wordName = ?;";
  
  private PreparedStatement statCheckById;
  private PreparedStatement statCheckByName;
  private PreparedStatement statAddWord;
  private PreparedStatement statAddWordWithId;
  private PreparedStatement statRemoveWord;
  private PreparedStatement statSelectDocumentAll;
  private PreparedStatement statSelectDocumentWord;
  private PreparedStatement statSelectWordGroup;
  private PreparedStatement statSelectWords;
  private PreparedStatement statSelectWordsWithoutGroup;
  private PreparedStatement statSelectWordById;
  private PreparedStatement statSelectWordByName;
  private PreparedStatement statUpdateWordGroup;
  private PreparedStatement statUpdateName;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param kbm
   */
  public WordDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {
    
    this.kbm = kbm;
    
    try {
    
      this.statCheckById = this.kbm.getConnection().prepareStatement(__CHECK_WORD_BY_ID);
      this.statCheckByName = this.kbm.getConnection().prepareStatement(__CHECK_WORD_BY_NAME);
      this.statAddWord = this.kbm.getConnection().prepareStatement(__INSERT_WORD, Statement.RETURN_GENERATED_KEYS);
      this.statAddWordWithId = this.kbm.getConnection().prepareStatement(__INSERT_WORD_WITH_ID);
      this.statRemoveWord = this.kbm.getConnection().prepareStatement(__REMOVE_WORD);
      this.statSelectDocumentAll = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENT_ALL);
      this.statSelectDocumentWord = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENT_WORD);
      this.statSelectWordGroup = this.kbm.getConnection().prepareStatement(__SELECT_WORDGROUP);
      this.statSelectWords = this.kbm.getConnection().prepareStatement(__SELECT_WORDS);
      this.statSelectWordsWithoutGroup = this.kbm.getConnection().prepareStatement(__SELECT_WORDS_WITHOUTGROUP);
      this.statSelectWordById = this.kbm.getConnection().prepareStatement(__SELECT_WORD_BY_ID);
      this.statSelectWordByName = this.kbm.getConnection().prepareStatement(__SELECT_WORD_BY_NAME);
      this.statUpdateWordGroup = this.kbm.getConnection().prepareStatement(__UPDATE_WORDGROUP);
      this.statUpdateName = this.kbm.getConnection().prepareStatement(__UPDATE_WORDNAME);
      
     } catch (SQLException e) {
     
       throw new KnowledgeBaseException(e.getMessage(), e.getCause());
     }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param word
   * @return
   * @throws KnowledgeBaseException
   */
  public Integer addWord(String word, boolean notifyObservers)
          throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddWord.clearParameters();

      this.statAddWord.setString(1, word);

      if (this.statAddWord.executeUpdate() > 0) {

        id = this.statAddWord.getGeneratedKeys().getInt(1);
        this.statAddWord.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddWordEvent(getWord(id)));
    }

    return id;
  }

  /**
   * 
   * @param wordID
   * @param wordName
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addWord(Integer wordID, String wordName, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statAddWordWithId.clearParameters();

      this.statAddWordWithId.setInt(1, wordID);
      this.statAddWordWithId.setString(2, wordName);

      result = this.statAddWordWithId.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddWordEvent(getWord(wordID)));
    }

    return result;
  }

  /**
   * 
   * @param word
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addWord(Word word, boolean notifyObservers) throws KnowledgeBaseException {

    return addWord(word.getWordID(),
                   word.getWordName(),
                   notifyObservers);
  }

  /**
   *
   * @param wordID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeWord(Integer wordID, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;
    Word word = null;
    WordGroup wordGroup = null;
    ArrayList<Document> documents = null;
    
    // Save the information before remove
    if (notifyObservers) {
      
      word = getWord(wordID);
      wordGroup = getWordGroup(wordID);
      documents = getDocuments(wordID);
    }

    try {

      this.statRemoveWord.clearParameters();

      this.statRemoveWord.setInt(1, wordID);

      result = this.statRemoveWord.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveWordEvent(word));
      
      if (wordGroup != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordGroupEvent(CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().refreshWordGroup(wordGroup)));
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new WordGroupRelationWordEvent());

      } else {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveWordWithoutGroupEvent(word));
      }
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(CurrentProject.getInstance().getFactoryDAO().getDocumentDAO().refreshDocuments(documents)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationWordEvent());
    }
    
    return result;
  }

  /**
   *
   * @param idWord the word's ID
   *
   * @return an <ocde>Word</code> or null if there is not any word with
   *         this ID
   *
   * @throws KnowledgeBaseException
   */
  public Word getWord(Integer idWord) throws KnowledgeBaseException {

    ResultSet rs;
    Word word = null;

    try {

      this.statSelectWordById.clearParameters();

      this.statSelectWordById.setInt(1, idWord);

      rs = this.statSelectWordById.executeQuery();

      if (rs.next()) {

        word = UtilsDAO.getInstance().getWord(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return word;
  }

  /**
   *
   * @param word the Word's word
   *
   * @return an <ocde>Word</code> or null if there is not any Word with
   *         this word
   *
   * @throws KnowledgeBaseException
   */
  public Word getWord(String word) throws KnowledgeBaseException {

    ResultSet rs;
    Word w = null;

    try {

      this.statSelectWordByName.clearParameters();

      this.statSelectWordByName.setString(1, word);

      rs = this.statSelectWordByName.executeQuery();

      if (rs.next()) {

        w = UtilsDAO.getInstance().getWord(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return w;
  }

  /**
   *
   * @return an <ocde>Word</code> or null if there is not any word with
   *         this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Word> getWords() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Word> wordsList = new ArrayList<Word>();

    try {

      this.statSelectWords.clearParameters();

      rs = this.statSelectWords.executeQuery();

      while (rs.next()) {

        wordsList.add(UtilsDAO.getInstance().getWord(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return wordsList;
  }

  /**
   *
   * @return an <ocde>Word</code> or null if there is not any word with
   *         this ID
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Word> getWordsWithoutGroup() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Word> wordsList = new ArrayList<Word>();

    try {

      this.statSelectWordsWithoutGroup.clearParameters();

      rs = this.statSelectWordsWithoutGroup.executeQuery();

      while (rs.next()) {

        wordsList.add(UtilsDAO.getInstance().getWord(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return wordsList;
  }

  /**
   * 
   * @param wordID
   * @param wordName
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setWordName(Integer wordID, String wordName, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;
    Word word;

    try {

      this.statUpdateName.clearParameters();

      this.statUpdateName.setString(1, wordName);
      this.statUpdateName.setInt(2, wordID);

      result = this.statUpdateName.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
    
    // Notify to the observer
    if (notifyObservers) {
      
      word = getWord(wordID);
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordEvent(word));
      
      if (getWordGroup(wordID) == null) {
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordWithoutGroupEvent(word));
      }
    }
    
    return result;
  }

  /**
   * 
   * @param wordID
   * @param wordGroupID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean setWordGroup(Integer wordID, Integer wordGroupID, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;
    WordGroup oldWordGroup = null;
    
    // Save the information before remove
    if (notifyObservers) {
    
      oldWordGroup = getWordGroup(wordID);
    }

    try {

      this.statUpdateWordGroup.clearParameters();

      if (wordGroupID != null) {
      
        this.statUpdateWordGroup.setInt(1, wordGroupID);
        
      } else {
      
        this.statUpdateWordGroup.setNull(1, Types.NULL);
      }
      
      this.statUpdateWordGroup.setInt(2, wordID);

      result = this.statUpdateWordGroup.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      if (oldWordGroup != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordGroupEvent(CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO().refreshWordGroup(oldWordGroup)));
        
        if (wordGroupID == null) {
          
          KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddWordWithoutGroupEvent(getWord(wordID)));
        }
        
      } else {
      
        if (wordGroupID != null) {
          
          KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveWordWithoutGroupEvent(getWord(wordID)));
        }
      }

      if (wordGroupID != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordGroupEvent(getWordGroup(wordID)));
      }
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new WordGroupRelationWordEvent());
    }
      
    return result;
  }

  /**
   *
   * @return
   * @throws KnowledgeBaseException
   */
  public WordGroup getWordGroup(Integer wordID) throws KnowledgeBaseException {

    ResultSet rs;
    WordGroup wordGroup = null;

    try {

      this.statSelectWordGroup.clearParameters();

      this.statSelectWordGroup.setInt(1, wordID);

      rs = this.statSelectWordGroup.executeQuery();

      if (rs.next()) {

        wordGroup = UtilsDAO.getInstance().getWordGroup(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return wordGroup;

  }

  /**
   *
   * @return an array with the {@link DocumentWord} associated with this
   * {@link WordDAO}
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<DocumentWord> getDocumentWords(Integer documentID) throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<DocumentWord> wordsList = new ArrayList<DocumentWord>();

    try {

      this.statSelectDocumentWord.clearParameters();

      this.statSelectDocumentWord.setInt(1, documentID);

      rs = this.statSelectDocumentWord.executeQuery();

      while (rs.next()) {
        
        wordsList.add(UtilsDAO.getInstance().getDocumentWord(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return wordsList;
  }

  /**
   *
   * @return an array with the {@link Word} associated with this
   * {@link DocumentDAO}
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Document> getDocuments(Integer wordID, boolean isAuthorWord,
          boolean isSourceWord, boolean isAddedWord) throws KnowledgeBaseException{

    String query, optional;
    ResultSet rs;
    ArrayList<Document> wordsList = new ArrayList<Document>();

    try {

      query = __SELECT_DOCUMENT;
      optional = "";
      
      if (isAuthorWord) {

        optional += "dw.isAuthorWord = 1";
        
      }

      if (isSourceWord) {

        if (optional.isEmpty()) {
          
          optional += "dw.isSourceWord = 1";
          
        } else {
        
          optional += " OR dw.isSourceWord = 1";
        }
      }

      if (isAddedWord) {
        
        if (optional.isEmpty()) {

          optional += "dw.isAddedWord = 1";
          
        } else {
          
          optional += " OR dw.isAddedWord = 1";
        }
      }

      if (optional.isEmpty()) {
      
        query += "";
        
      } else {
      
        query += " AND ( " + optional + ");" ;
      }

      Statement stat = this.kbm.getConnection().createStatement();
      
      rs = stat.executeQuery(query.replace("?", String.valueOf(wordID)));

      while (rs.next()) {

        wordsList.add(UtilsDAO.getInstance().getDocument(rs));
      }

      rs.close();
      stat.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return wordsList;
  }
  
  /**
   *
   * @return an array with the {@link Word} associated with this
   * {@link DocumentDAO}
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Document> getDocuments(Integer wordID) throws KnowledgeBaseException{
    
    ResultSet rs;
    ArrayList<Document> wordsList = new ArrayList<Document>();

    try {

      this.statSelectDocumentAll.clearParameters();

      this.statSelectDocumentAll.setInt(1, wordID);

      rs = this.statSelectDocumentAll.executeQuery();

      while (rs.next()) {

        wordsList.add(UtilsDAO.getInstance().getDocument(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return wordsList;
  }

  /**
   * <p>Check if there is an <code>Word</code> with this word.</p>
   *
   * @param word a string with the word
   *
   * @return true if there is an <code>Word</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkWord(String word) throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckByName.clearParameters();

      this.statCheckByName.setString(1, word);

      rs = this.statCheckByName.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * <p>Check if there is an <code>Word</code> with this ID.</p>
   *
   * @param idWord the word's ID
   *
   * @return true if there is an <code>Word</code> with this attribute
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkWord(Integer idWord) throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {

      this.statCheckById.clearParameters();

      this.statCheckById.setInt(1, idWord);

      rs = this.statCheckById.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param wordsToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Word> refreshWords(ArrayList<Word> wordsToRefresh) throws KnowledgeBaseException {
  
    int i;
    String query;
    ResultSet rs;
    ArrayList<Word> words = new ArrayList<Word>();
    
    i = 0;
    
    if (!wordsToRefresh.isEmpty()) {

      query = "SELECT * FROM Word WHERE idWord IN (" + wordsToRefresh.get(i).getWordID();
      
      for (i = 1; i < wordsToRefresh.size(); i++) {
        
        query += ", " + wordsToRefresh.get(i).getWordID();
      }
      
      query += ");";
      
      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          words.add(UtilsDAO.getInstance().getWord(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }
    
    return words;
  }
  
  /**
   * 
   * @param wordToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public Word refreshWord(Word wordToRefresh) throws KnowledgeBaseException {
  
    return getWord(wordToRefresh.getWordID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
