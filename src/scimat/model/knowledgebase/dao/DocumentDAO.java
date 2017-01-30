/*
 * DocumentDAO.java
 *
 * Created on 28-oct-2010, 18:20:28
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
import scimat.knowledgebaseevents.event.add.AddDocumentEvent;
import scimat.knowledgebaseevents.event.relation.DocumentRelationAffiliationEvent;
import scimat.knowledgebaseevents.event.relation.DocumentRelationAuthorEvent;
import scimat.knowledgebaseevents.event.relation.DocumentRelationReferenceEvent;
import scimat.knowledgebaseevents.event.relation.DocumentRelationWordEvent;
import scimat.knowledgebaseevents.event.relation.JournalRelationDocumentEvent;
import scimat.knowledgebaseevents.event.relation.PublishDateRelationDocumentEvent;
import scimat.knowledgebaseevents.event.remove.RemoveDocumentEvent;
import scimat.knowledgebaseevents.event.update.UpdateAffiliationEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorReferenceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateAuthorWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateDocumentEvent;
import scimat.knowledgebaseevents.event.update.UpdateJournalEvent;
import scimat.knowledgebaseevents.event.update.UpdatePeriodEvent;
import scimat.knowledgebaseevents.event.update.UpdatePublishDateEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceSourceEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceSourceGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceSourceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateReferenceWithoutGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateSubjectCategoryEvent;
import scimat.knowledgebaseevents.event.update.UpdateWordEvent;
import scimat.knowledgebaseevents.event.update.UpdateWordGroupEvent;
import scimat.knowledgebaseevents.event.update.UpdateWordWithoutGroupEvent;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.DocumentAuthor;
import scimat.model.knowledgebase.entity.DocumentWord;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class DocumentDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;

  /**
   * <pre>
   * INSERT INTO Document(title,docAbstract,type,citationsCount,doi,sourceIdentifier) VALUES(?,?,?,?,?,?);
   * </pre>
   */
  private final static String __INSERT_DOCUMENT_WITHOUT_JOURNAL_INF = "INSERT INTO Document(title,docAbstract,type,citationsCount,doi,sourceIdentifier) VALUES(?,?,?,?,?,?);";

  /**
   * <pre>
   * INSERT INTO Document(title,docAbstract,type,citationsCount,doi,sourceIdentifier,volume,issue,beginPage,endPage) VALUES(?,?,?,?,?,?,?,?,?,?);
   * </pre>
   */
  private final static String __INSERT_DOCUMENT = "INSERT INTO Document(title,docAbstract,type,citationsCount,doi,sourceIdentifier,volume,issue,beginPage,endPage) VALUES(?,?,?,?,?,?,?,?,?,?);";

  /**
   * <pre>
   * INSERT INTO Document(idDocument,title,docAbstract,type,citationsCount,doi,sourceIdentifier,volume,issue,beginPage,endPage) VALUES(?,?,?,?,?,?,?,?,?,?,?);
   * </pre>
   */
  private final static String __INSERT_DOCUMENT_WITH_ID = "INSERT INTO Document(idDocument,title,docAbstract,type,citationsCount,doi,sourceIdentifier,volume,issue,beginPage,endPage) VALUES(?,?,?,?,?,?,?,?,?,?,?);";

  /**
   * <pre>
   * DELETE Document
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __REMOVE_DOCUMENT = "DELETE FROM Document "
                                                + "WHERE idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document
   * SET title = ?
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_TITLE = "UPDATE Document "
                                             + "SET title = ? "
                                             + "WHERE idDocument = ?;";
  
  /**
   * <pre>
   * UPDATE Document
   * SET docAbstract = ?
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_DOCABSTRACT = "UPDATE Document "
                                                   + "SET docAbstract = ? "
                                                   + "WHERE idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document
   * SET type = ?
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_TYPE = "UPDATE Document "
                                             + "SET type = ? "
                                             + "WHERE idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document
   * SET citationsCount = ?
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_CITATIONSCOUNT = "UPDATE Document "
                                                      + "SET citationsCount = ? "
                                                      + "WHERE idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document
   * SET doi = ?
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_DOI = "UPDATE Document "
                                           + "SET doi = ? "
                                           + "WHERE idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document
   * SET sourceIdentifier = ?
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_SOURCEIDENTIFIER = "UPDATE Document "
                                                        + "SET sourceIdentifier = ? "
                                                        + "WHERE idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document
   * SET volume = ?
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_VOLUME = "UPDATE Document "
                                              + "SET volume = ? "
                                              + "WHERE idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document
   * SET issue = ?
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_ISSUE = "UPDATE Document "
                                             + "SET issue = ? "
                                             + "WHERE idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document
   * SET beginPage = ?
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_BEGINPAGE = "UPDATE Document "
                                                 + "SET beginPage = ? "
                                                 + "WHERE idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document
   * SET endPage = ?
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_ENDPAGE = "UPDATE Document "
                                               + "SET endPage = ? "
                                               + "WHERE idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document
   * SET title = ?,
   *     docAbstract = ?,
   *     type = ?,
   *     citationsCount = ?,
   *     doi = ?,
   *     sourceIdentifier = ?,
   *     volume = ?,
   *     issue = ?,
   *     beginPage = ?,
   *     endPage = ?
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_DOCUMENT = "UPDATE Document "
                                                + "SET title = ?, "
                                                + "    docAbstract = ?, "
                                                + "    type = ?, "
                                                + "    citationsCount = ?, "
                                                + "    doi = ?, "
                                                + "    sourceIdentifier = ?, "
                                                + "    volume = ?, "
                                                + "    issue = ?, "
                                                + "    beginPage = ?, "
                                                + "    endPage = ? "
                                                + "WHERE idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document
   * SET Journal_idJournal = ?
   * WHERE idDocument = ?;
   * </pre>
   */
  private final static String __UPDATE_JOURNAL = "UPDATE Document "
                                               + "SET Journal_idJournal = ? "
                                               + "WHERE idDocument = ?;";

  /**
   * <pre>
   * UPDATE Document
   * SET PublishDate_idPublishDate = ?
   * WHERE idDocument = ?
   * </pre>
   */
  private final static String __UPDATE_PUBLISHDATE = "UPDATE Document "
                                                   + "SET PublishDate_idPublishDate = ? "
                                                   + "WHERE idDocument = ?";
  /**
   * <pre>
   * SELECT j.idJournal, j.source, j.conferenceInformation
   * FROM Journal j, Document d
   * WHERE d.idDocument = ? AND d.Journal_idJournal = j.idJournal;
   * </pre>
   */
  private final static String __SELECT_JOURNAL = "SELECT j.* "
                                               + "FROM Journal j, Document d "
                                               + "WHERE d.idDocument = ? AND d.Journal_idJournal = j.idJournal;";

  /**
   * <pre>
   * SELECT p.idPublishDate, p.year, p.date
   * FROM PublishDate p, Document d
   * WHERE d.idDocument = ? AND
   *       p.PublishDate_idPublishDate = p.idPublishDate;
   * </pre>
   */
  private final static String __SELECT_PUBLISHDATE = "SELECT p.* "
                                                   + "FROM PublishDate p, Document d "
                                                   + "WHERE d.idDocument = ? AND "
                                                   + "      d.PublishDate_idPublishDate = p.idPublishDate;";

  /**
   * <pre>
   * </pre>
   */
  private final static String __SELECT_DOCUMENT_AUTHOR = "SELECT d.*, a.*, da.position "
                                                       + "FROM Document_Author da, Document d, Author a "
                                                       + "WHERE d.idDocument = ? AND "
                                                       + "      d.idDocument = da.Document_idDocument AND "
                                                       + "      da.Author_idAuthor = a.idAuthor;";
  
  /**
   * <pre>
   * SELECT a.idAuthor, a.authorName, a.fullAuthorName
   * FROM Document_Author da, Author a 
   * WHERE da.Document_idDocument = ? AND 
   *       da.Author_idAuthor = a.idAuthor
   * ORDER BY da.position ASC;
   * </pre>
   */
  private final static String __SELECT_AUTHOR = "SELECT a.* "
                                              + "FROM Document_Author da, Author a "
                                              + "WHERE da.Document_idDocument = ? AND "
                                              + "      da.Author_idAuthor = a.idAuthor "
                                              + "ORDER BY da.position ASC;";
  
  private final static String __SELECT_AUTHOR_WITH_GROUP = "SELECT a.* "
                                              + "FROM Document_Author da, Author a "
                                              + "WHERE da.Document_idDocument = ? AND "
                                              + "      da.Author_idAuthor = a.idAuthor AND"
                                              + "      a.AuthorGroup_idAuthorGroup ISNULL "
                                              + "ORDER BY da.position ASC;";

  /**
   * <pre>
   * SELECT a.idAffiliation, a.fullAffiliation
   * FROM Document_Affiliation da, Affiliation a
   * WHERE da.Document_idDocument = ? AND a.idAffiliation = da.Affiliation_idAffiliation;
   * </pre>
   */
  private final static String __SELECT_AFFILIATION = "SELECT a.* "
                                                   + "FROM Document_Affiliation da, Affiliation a "
                                                   + "WHERE da.Document_idDocument = ? AND a.idAffiliation = da.Affiliation_idAffiliation;";

  /**
   * <pre>
   * SELECT r.idReference, r.fullReference, r.volume, r.issue, r.page, r.doi, r.format, r.year
   * FROM Document_Reference dr, Reference r
     WHERE dr.Document_idDocument = ? and dr.Reference_idReference = r.idReference;
   * </pre>
   */
  private final static String __SELECT_REFERENCE = "SELECT r.* "
                                                 + "FROM Document_Reference dr, Reference r "
                                                 + "WHERE dr.Document_idDocument = ? and dr.Reference_idReference = r.idReference;";
  
  private final static String __SELECT_REFERENCE_WITHOUT_GROUP = "SELECT r.* "
                                                 + "FROM Document_Reference dr, Reference r "
                                                 + "WHERE dr.Document_idDocument = ? and dr.Reference_idReference = r.idReference AND r.ReferenceGroup_idReferenceGroup ISNULL;";

  /**
   * <pre>
   * </pre>
   */
  private final static String __SELECT_DOCUMENT_WORD = "SELECT d.*, w.*, dw.isAuthorWord, dw.isSourceWord, dw.isAddedWord "
                                                     + "FROM Document_Word dw, Document d, Word w "
                                                     + "WHERE d.idDocument = ? AND "
                                                     + "      d.idDocument = dw.Document_idDocument AND "
                                                     + "      dw.Word_idWord = w.idWord;";

  /**
   * <pre>
   * SELECT w.idWord, w.word
   * FROM Document_Word dw, Word w
   * WHERE dw.Document_idDocument = ? AND
   *       dw.Word_idWord = w.idWord AND
   *       dw.isAuthorWord = ? AND
   *       dw.isSourceWord = ? AND
   *       dw.isAddedWord = ?;
   * </pre>
   */
  private final static String __SELECT_WORD = "SELECT w.* "
                                            + "FROM Document_Word dw, Word w "
                                            + "WHERE dw.Document_idDocument = ? AND"
                                            + "      dw.Word_idWord = w.idWord";
  
  private final static String __SELECT_WORD_WITHOUT_GROUP = "SELECT w.* "
                                            + "FROM Document_Word dw, Word w "
                                            + "WHERE dw.Document_idDocument = ? AND"
                                            + "      dw.Word_idWord = w.idWord AND w.WordGroup_idWordGroup ISNULL ";
  
  private final static String __SELECT_WORD_ALL = "SELECT w.* "
                                                + "FROM Document_Word dw, Word w "
                                                + "WHERE dw.Document_idDocument = ? AND"
                                                + "      dw.Word_idWord = w.idWord;";
  
  private final static String __SELECT_WORD_ALL_WITHOUT_GROUP = "SELECT w.* "
                                                + "FROM Document_Word dw, Word w "
                                                + "WHERE dw.Document_idDocument = ? AND"
                                                + "      dw.Word_idWord = w.idWord AND w.WordGroup_idWordGroup ISNULL;";
  
  private final static String __SELECT_DOCUMENT_BY_ID = "SELECT * FROM Document WHERE idDocument = ?;";
  private final static String __SELECT_DOCUMENTS = "SELECT * FROM Document;";
  private final static String __CHECK_DOCUMENT_BY_ID = "SELECT idDocument FROM Document WHERE idDocument = ?;";
  
  private PreparedStatement statAddDocument;
  private PreparedStatement statAddDocumentWithoutPublicationInformation;
  private PreparedStatement statAddDocumentWithID;
  private PreparedStatement statRemoveDocument;
  private PreparedStatement statSelectAffiliation;
  private PreparedStatement statSelectAuthors;
  private PreparedStatement statSelectAuthorsWithoutGroup;
  private PreparedStatement statSelectDocuments;
  private PreparedStatement statSelectDocumentById;
  private PreparedStatement statSelectDocumentAuthor;
  private PreparedStatement statCheckDocumentById;
  private PreparedStatement statSelectDocumentWord;
  private PreparedStatement statSelectJournal;
  private PreparedStatement statSelectPublishDate;
  private PreparedStatement statSelectReferences;
  private PreparedStatement statSelectReferenceWithoutGroup;
  private PreparedStatement statSelectWords;
  private PreparedStatement statSelectWordsWithoutGroup;
  private PreparedStatement statUpdateBeginPage;
  private PreparedStatement statUpdateCitationsCount;
  private PreparedStatement statUpdateDocAbstract;
  private PreparedStatement statUpdateDocument;
  private PreparedStatement statUpdateDoi;
  private PreparedStatement statUpdateEndPage;
  private PreparedStatement statUpdateIsse;
  private PreparedStatement statUpdateJournal;
  private PreparedStatement statUpdatePublishDate;
  private PreparedStatement statUpdateSourceIdentifier;
  private PreparedStatement statUpdateTitle;
  private PreparedStatement statUpdateType;
  private PreparedStatement statUpdateVolume;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param kbm
   */
  public DocumentDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {

    this.kbm = kbm;
    
    try {
    
      this.statAddDocument = this.kbm.getConnection().prepareStatement(__INSERT_DOCUMENT, Statement.RETURN_GENERATED_KEYS);
      this.statAddDocumentWithoutPublicationInformation = this.kbm.getConnection().prepareStatement(__INSERT_DOCUMENT_WITHOUT_JOURNAL_INF, Statement.RETURN_GENERATED_KEYS);
      this.statAddDocumentWithID  = this.kbm.getConnection().prepareStatement(__INSERT_DOCUMENT_WITH_ID);
      this.statRemoveDocument = this.kbm.getConnection().prepareStatement(__REMOVE_DOCUMENT);
      this.statSelectAffiliation = this.kbm.getConnection().prepareStatement(__SELECT_AFFILIATION);
      this.statSelectAuthors = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR);
      this.statSelectAuthorsWithoutGroup = this.kbm.getConnection().prepareStatement(__SELECT_AUTHOR_WITH_GROUP);
      this.statSelectDocuments = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENTS);
      this.statSelectDocumentById = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENT_BY_ID);
      this.statSelectDocumentAuthor = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENT_AUTHOR);
      this.statCheckDocumentById = this.kbm.getConnection().prepareStatement(__CHECK_DOCUMENT_BY_ID);
      this.statSelectDocumentWord = this.kbm.getConnection().prepareStatement(__SELECT_DOCUMENT_WORD);
      this.statSelectJournal = this.kbm.getConnection().prepareStatement(__SELECT_JOURNAL);
      this.statSelectPublishDate = this.kbm.getConnection().prepareStatement(__SELECT_PUBLISHDATE);
      this.statSelectReferences = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCE);
      this.statSelectReferenceWithoutGroup = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCE_WITHOUT_GROUP);
      this.statSelectWords = this.kbm.getConnection().prepareStatement(__SELECT_WORD_ALL);
      this.statSelectWordsWithoutGroup = this.kbm.getConnection().prepareStatement(__SELECT_WORD_ALL_WITHOUT_GROUP);
      this.statUpdateBeginPage = this.kbm.getConnection().prepareStatement(__UPDATE_BEGINPAGE);
      this.statUpdateCitationsCount = this.kbm.getConnection().prepareStatement(__UPDATE_CITATIONSCOUNT);
      this.statUpdateDocAbstract = this.kbm.getConnection().prepareStatement(__UPDATE_DOCABSTRACT);
      this.statUpdateDocument = this.kbm.getConnection().prepareStatement(__UPDATE_DOCUMENT);
      this.statUpdateDoi = this.kbm.getConnection().prepareStatement(__UPDATE_DOI);
      this.statUpdateEndPage = this.kbm.getConnection().prepareStatement(__UPDATE_ENDPAGE);
      this.statUpdateIsse = this.kbm.getConnection().prepareStatement(__UPDATE_ISSUE);
      this.statUpdateJournal = this.kbm.getConnection().prepareStatement(__UPDATE_JOURNAL);
      this.statUpdatePublishDate = this.kbm.getConnection().prepareStatement(__UPDATE_PUBLISHDATE);
      this.statUpdateSourceIdentifier = this.kbm.getConnection().prepareStatement(__UPDATE_SOURCEIDENTIFIER);
      this.statUpdateTitle = this.kbm.getConnection().prepareStatement(__UPDATE_TITLE);
      this.statUpdateType = this.kbm.getConnection().prepareStatement(__UPDATE_TYPE);
      this.statUpdateVolume = this.kbm.getConnection().prepareStatement(__UPDATE_VOLUME);
      
    } catch (SQLException e) {

      e.printStackTrace(System.err);
      
      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @param title
   * @param docAbstract
   * @param type
   * @param citationsCount
   * @param doi
   * @param sourceIdentifier
   * @return
   * @throws KnowledgeBaseException
   */
  public Integer addDocument(String title, String docAbstract, String type,
          int citationsCount, String doi, String sourceIdentifier, boolean notifyObservers)
          throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddDocumentWithoutPublicationInformation.clearParameters();

      this.statAddDocumentWithoutPublicationInformation.setString(1, title);
      this.statAddDocumentWithoutPublicationInformation.setString(2, docAbstract);
      this.statAddDocumentWithoutPublicationInformation.setString(3, type);
      this.statAddDocumentWithoutPublicationInformation.setInt(4, citationsCount);
      this.statAddDocumentWithoutPublicationInformation.setString(5, doi);
      this.statAddDocumentWithoutPublicationInformation.setString(6, sourceIdentifier);

      if (this.statAddDocumentWithoutPublicationInformation.executeUpdate() == 1) {

        id = this.statAddDocumentWithoutPublicationInformation.getGeneratedKeys().getInt(1);
        this.statAddDocumentWithoutPublicationInformation.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddDocumentEvent(getDocument(id)));
    }

    return id;
  }

  /**
   * 
   * @param externalID
   * @param title
   * @param docAbstract
   * @param type
   * @param citationsCount
   * @param doi
   * @param sourceIdentifier
   * @param volume
   * @param issue
   * @param beginPage
   * @param endPage
   * @return
   * @throws KnowledgeBaseException
   */
  public Integer addDocument(String title, String docAbstract, String type,
          int citationsCount, String doi, String sourceIdentifier,
          String volume, String issue, String beginPage, String endPage, boolean notifyObservers)
          throws KnowledgeBaseException {

    Integer id;

    try {

      this.statAddDocument.clearParameters();

      this.statAddDocument.setString(1, title);
      this.statAddDocument.setString(2, docAbstract);
      this.statAddDocument.setString(3, type);
      this.statAddDocument.setInt(4, citationsCount);
      this.statAddDocument.setString(5, doi);
      this.statAddDocument.setString(6, sourceIdentifier);
      this.statAddDocument.setString(7, volume);
      this.statAddDocument.setString(8, issue);
      this.statAddDocument.setString(9, beginPage);
      this.statAddDocument.setString(10, endPage);

      if (this.statAddDocument.executeUpdate() == 1 ) {

        id = this.statAddDocument.getGeneratedKeys().getInt(1);
        this.statAddDocument.getGeneratedKeys().close();

      } else {

        id = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddDocumentEvent(getDocument(id)));
    }

    return id;
  }

  /**
   *
   * @param documentID
   * @param title
   * @param docAbstract
   * @param type
   * @param citationsCount
   * @param doi
   * @param sourceIdentifier
   * @param volume
   * @param issue
   * @param beginPage
   * @param endPage
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addDocument(Integer documentID, String title, String docAbstract,
          String type, int citationsCount, String doi, String sourceIdentifier,
          String volume, String issue, String beginPage, String endPage, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statAddDocumentWithID.clearParameters();

      this.statAddDocumentWithID.setInt(1, documentID);
      this.statAddDocumentWithID.setString(2, title);
      this.statAddDocumentWithID.setString(3, docAbstract);
      this.statAddDocumentWithID.setString(4, type);
      this.statAddDocumentWithID.setInt(5, citationsCount);
      this.statAddDocumentWithID.setString(6, doi);
      this.statAddDocumentWithID.setString(7, sourceIdentifier);
      this.statAddDocumentWithID.setString(8, volume);
      this.statAddDocumentWithID.setString(9, issue);
      this.statAddDocumentWithID.setString(10, beginPage);
      this.statAddDocumentWithID.setString(11, endPage);

      result = this.statAddDocumentWithID.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new AddDocumentEvent(getDocument(documentID)));
    }

    return result;
  }

  /**
   * 
   * @param document
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean addDocument(Document document, boolean notifyObservers) throws KnowledgeBaseException {

    return addDocument(document.getDocumentID(),
                       document.getTitle(),
                       document.getDocAbstract(),
                       document.getType(),
                       document.getCitationsCount(),
                       document.getDoi(),
                       document.getSourceIdentifier(),
                       document.getVolume(),
                       document.getIssue(),
                       document.getBeginPage(),
                       document.getEndPage(),
                       notifyObservers);
  }

  /**
   *
   * @param documentID
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean removeDocument(Integer documentID, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    int i, j;
    boolean result = false;
    Document document = null;
    PublishDate publishDate = null;
    Journal journal = null;
    ArrayList<Author> authors = null;
    ArrayList<Author> authorsWithoutGroup = null;
    ArrayList<Affiliation> affiliations = null;
    ArrayList<Word> words = null;
    ArrayList<Word> wordsWithoutGroup = null;
    ArrayList<Reference> references = null;
    ArrayList<Reference> referencesWithoutGroup = null;
    
    // Save the information before remove
    if (notifyObservers) {
    
      document = getDocument(documentID);
      publishDate = getPublishDate(documentID);
      journal = getJournal(documentID);
      authors = getAuthors(documentID);
      authorsWithoutGroup = getAuthorsWithoutGroup(documentID);
      affiliations = getAffiliations(documentID);
      words = getWords(documentID);
      wordsWithoutGroup = getWordsWithoutGroup(documentID);
      references = getReferences(documentID);
      referencesWithoutGroup = getReferencesWithoutGroup(documentID);
    }

    try {
      
      this.statRemoveDocument.clearParameters();

      this.statRemoveDocument.setInt(1, documentID);

      result = this.statRemoveDocument.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    // Notify to the observer
    if (notifyObservers) {
      
      TreeSet<ReferenceGroup> referenceGroups = new TreeSet<ReferenceGroup>();
      TreeSet<AuthorReference> authorReferences = new TreeSet<AuthorReference>();
      ArrayList<AuthorReference> authorReferencesTmp = new ArrayList<AuthorReference>();
      TreeSet<AuthorReference> authorReferencesWithoutGroup = new TreeSet<AuthorReference>();
      TreeSet<AuthorReferenceGroup> authorReferenceGroups = new TreeSet<AuthorReferenceGroup>();
      TreeSet<ReferenceSource> referenceSources = new TreeSet<ReferenceSource>();
      TreeSet<ReferenceSource> referenceSourcesWithoutGroup = new TreeSet<ReferenceSource>();
      TreeSet<ReferenceSourceGroup> referenceSourceGroups = new TreeSet<ReferenceSourceGroup>();
      AuthorReferenceGroup authorReferenceGroup;
      ReferenceSourceGroup referenceSourceGroup;
      ReferenceSource referenceSource;
      ReferenceGroup referenceGroup;
      Reference reference;
      ReferenceDAO referenceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceDAO();
      WordDAO wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();
      AuthorReferenceDAO authorReferenceDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceDAO();
      ReferenceSourceDAO referenceSourceDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceDAO();
    
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new RemoveDocumentEvent(document));
      
      // Notify to PublishDate
      if (publishDate != null) {
      
        PublishDateDAO publishDateDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO();
        
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePublishDateEvent(publishDateDAO.refreshPublishDate(publishDate)));
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new PublishDateRelationDocumentEvent());
        
        // Notify to Period
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePeriodEvent(publishDateDAO.getPeriods(publishDate.getPublishDateID())));
      }
 
      // Notify to Journal
      if (journal != null) {
        
        JournalDAO journalDAO = CurrentProject.getInstance().getFactoryDAO().getJournalDAO();
        
      
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateJournalEvent(journalDAO.refreshJournal(journal)));
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new JournalRelationDocumentEvent());
        
        // Notify to SubjectCategory
        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateSubjectCategoryEvent(journalDAO.getSubjectCategories(journal.getJournalID())));
      }
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAffiliationEvent(CurrentProject.getInstance().getFactoryDAO().getAffiliationDAO().refreshAffiliations(affiliations)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationAffiliationEvent());
      
      AuthorDAO authorDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorDAO();
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorEvent(authorDAO.refreshAuthors(authors)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorWithoutGroupEvent(authorDAO.refreshAuthors(authorsWithoutGroup)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationAuthorEvent());
      
      TreeSet<AuthorGroup> authorGroups = new TreeSet<AuthorGroup>();
      AuthorGroup authorGroup;
      
      for (i = 0; i < authors.size(); i++) {
      
        authorGroup = authorDAO.getAuthorGroup(authors.get(i).getAuthorID());
        
        if (authorGroup != null) {
        
          authorGroups.add(authorGroup);
        }
      }
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorGroupEvent(new ArrayList<AuthorGroup>(authorGroups)));
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordEvent(wordDAO.refreshWords(words)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordWithoutGroupEvent(wordDAO.refreshWords(wordsWithoutGroup)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationWordEvent());
      
      TreeSet<WordGroup> wordGroups = new TreeSet<WordGroup>();
      WordGroup wordGroup;
      
      for (i = 0; i < words.size(); i++) {
      
        wordGroup = wordDAO.getWordGroup(words.get(i).getWordID());
        
        if (wordGroup != null) {
        
          wordGroups.add(wordGroup);
        }
      }
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateWordGroupEvent(new ArrayList<WordGroup>(wordGroups)));
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceEvent(referenceDAO.refreshReferences(references)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceWithoutGroupEvent(referenceDAO.refreshReferences(referencesWithoutGroup)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new DocumentRelationReferenceEvent());
      
      for (i = 0; i < references.size(); i++) {
      
        reference = references.get(i);
        
        referenceGroup = referenceDAO.getReferenceGroup(reference.getReferenceID());
        
        if (referenceGroup != null) {
        
          referenceGroups.add(referenceGroup);
        }
        
        // Retrieve the author-reference
        authorReferencesTmp = referenceDAO.getAuthorReferences(reference.getReferenceID());
        authorReferences.addAll(authorReferencesTmp);
        
        for (j = 0; j < authorReferencesTmp.size(); j++) {
        
          authorReferenceGroup = authorReferenceDAO.getAuthorReferenceGroup(authorReferencesTmp.get(j).getAuthorReferenceID());
          
          if (authorReferenceGroup != null) {
          
            authorReferenceGroups.add(authorReferenceGroup);
            
          } else { // Retrieve the author-reference without groups
          
            authorReferencesWithoutGroup.add(authorReferencesTmp.get(j));
          }
        }
        
        // Retrieve the reference-source
        referenceSource = referenceDAO.getReferenceSource(reference.getReferenceID());
        
        if (referenceSource != null) {
        
          referenceSources.add(referenceSource);
          
          // Retrieve the reference-source
          referenceSourceGroup = referenceSourceDAO.getReferenceSourceGroup(referenceSource.getReferenceSourceID());
          
          if (referenceSourceGroup != null) {
          
            referenceSourceGroups.add(referenceSourceGroup);
          
          } else {
          
            referenceSourcesWithoutGroup.add(referenceSource);
          }
        }
      }
      
      // Notify to the observer related with the references and its associated entities.
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceGroupEvent(new ArrayList<ReferenceGroup>(referenceGroups)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceEvent(new ArrayList<AuthorReference>(authorReferences)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceWithoutGroupEvent(new ArrayList<AuthorReference>(authorReferencesWithoutGroup)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateAuthorReferenceGroupEvent(new ArrayList<AuthorReferenceGroup>(authorReferenceGroups)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceEvent(new ArrayList<ReferenceSource>(referenceSources)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceWithoutGroupEvent(new ArrayList<ReferenceSource>(referenceSourcesWithoutGroup)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateReferenceSourceGroupEvent(new ArrayList<ReferenceSourceGroup>(referenceSourceGroups)));
    }
      
    return result;
  }

  /**
   *
   * @param idDocument the document's ID
   *
   * @return a <ocde>Document</code> or null if there is not any document
   *            with this ID
   *
   * @throws KnowledgeBaseException
   */
  public Document getDocument(Integer idDocument) throws KnowledgeBaseException {

    ResultSet rs;
    Document document = null;

    try {

      this.statSelectDocumentById.clearParameters();

      this.statSelectDocumentById.setInt(1, idDocument);

      rs = this.statSelectDocumentById.executeQuery();

      if (rs.next()) {

        document = UtilsDAO.getInstance().getDocument(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return document;
  }

  /**
   *
   * @return an array with the <ocde>Document</code>
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Document> getDocuments() throws KnowledgeBaseException {

    ResultSet rs;
    ArrayList<Document> documentList = new ArrayList<Document>();

    try {

      this.statSelectDocuments.clearParameters();

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
   * @param type
   */
  public boolean setType(Integer documentID, String type, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateType.clearParameters();

      this.statUpdateType.setString(1, type);
      this.statUpdateType.setInt(2, documentID);

      result = this.statUpdateType.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocument(documentID)));
    }
    
    return result;
  }

  /**
   * @param title the title to set
   */
  public boolean setTitle(Integer documentID, String title, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateTitle.clearParameters();

      this.statUpdateTitle.setString(1, title);
      this.statUpdateTitle.setInt(2, documentID);

      result = this.statUpdateTitle.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocument(documentID)));
    }
    
    return result;
  }

  /**
   * @param citationsCount the citationsCount to set
   */
  public boolean setCitationsCount(Integer documentID, int citationsCount, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateCitationsCount.clearParameters();

      this.statUpdateCitationsCount.setInt(1, citationsCount);
      this.statUpdateCitationsCount.setInt(2, documentID);


      result = this.statUpdateCitationsCount.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocument(documentID)));
    }
    
    return result;
  }

  /**
   * @param doi the doi to set
   */
  public boolean setDoi(Integer documentID, String doi, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateDoi.clearParameters();

      this.statUpdateDoi.setString(1, doi);
      this.statUpdateDoi.setInt(2, documentID);

      result = this.statUpdateDoi.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocument(documentID)));
    }
    
    return result;
  }

  /**
   * @param sourceIdentifier the sourceIdentifier to set
   */
  public boolean setSourceIdentifier(Integer documentID, String sourceIdentifier, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateSourceIdentifier.clearParameters();

      this.statUpdateSourceIdentifier.setString(1, sourceIdentifier);
      this.statUpdateSourceIdentifier.setInt(2, documentID);

      result = this.statUpdateSourceIdentifier.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocument(documentID)));
    }
    
    return result;
  }

  /**
   * @param docAbstract the docAbstract to set
   */
  public boolean setDocAbstract(Integer documentID, String docAbstract, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateDocAbstract.clearParameters();

      this.statUpdateDocAbstract.setString(1, docAbstract);
      this.statUpdateDocAbstract.setInt(2, documentID);

      result = this.statUpdateDocAbstract.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocument(documentID)));
    }
    
    return result;
  }

  /**
   * @param volume the volume to set
   */
  public boolean setVolume(Integer documentID, String volume, boolean notifyObservers)  throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateVolume.clearParameters();

      this.statUpdateVolume.setString(1, volume);
      this.statUpdateVolume.setInt(2, documentID);
      
      result = this.statUpdateVolume.executeUpdate() > 0;
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
  }

  /**
   * @param issue the issue to set
   */
  public boolean setIssue(Integer documentID, String issue, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;
    
    try {

      this.statUpdateIsse.clearParameters();

      this.statUpdateIsse.setString(1, issue);
      this.statUpdateIsse.setInt(2, documentID);

      result = this.statUpdateIsse.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocument(documentID)));
    }
    
    return result;
  }

  /**
   * @param beginPage the beginPage to set
   */
  public boolean setBeginPage(Integer documentID, String beginPage, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateBeginPage.clearParameters();

      this.statUpdateBeginPage.setString(1, beginPage);
      this.statUpdateBeginPage.setInt(2, documentID);

      result = this.statUpdateBeginPage.executeUpdate() > 0;
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
  }

  /**
   * @param endPage the endPage to set
   */
  public boolean setEndPage(Integer documentID, String endPage, boolean notifyObservers) throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateEndPage.clearParameters();

      this.statUpdateEndPage.setString(1, endPage);
      this.statUpdateEndPage.setInt(2, documentID);

      result = this.statUpdateEndPage.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocument(documentID)));
    }
    
    return result;
  }

  /**
   * 
   * @param documentID
   * @param title
   * @param docAbstract
   * @param type
   * @param citationsCount
   * @param doi
   * @param sourceIdentifier
   * @param volume
   * @param issue
   * @param beginPage
   * @param endPage
   * @return
   * @throws KnowledgeBaseException
   */
  public boolean updateDocument(Integer documentID, String title, String docAbstract,
          String type, int citationsCount, String doi, String sourceIdentifier,
          String volume, String issue, String beginPage, String endPage, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;

    try {

      this.statUpdateDocument.clearParameters();

      this.statUpdateDocument.setString(1, title);
      this.statUpdateDocument.setString(2, docAbstract);
      this.statUpdateDocument.setString(3, type);
      this.statUpdateDocument.setInt(4, citationsCount);
      this.statUpdateDocument.setString(5, doi);
      this.statUpdateDocument.setString(6, sourceIdentifier);
      this.statUpdateDocument.setString(7, volume);
      this.statUpdateDocument.setString(8, issue);
      this.statUpdateDocument.setString(9, beginPage);
      this.statUpdateDocument.setString(10, endPage);
      this.statUpdateDocument.setInt(11, documentID);

      result = this.statUpdateDocument.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocument(documentID)));
    }
    
    return result;
  }

  /**
   * @param journalID the idJournal to set
   */
  public boolean setJournal(Integer documentID, Integer journalID, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;
    Journal oldJournal = null;
    
    // Notify to the observer
    if (notifyObservers) {
    
      oldJournal = getJournal(documentID);
    }

    try {

      this.statUpdateJournal.clearParameters();

      if (journalID != null) {
      
        this.statUpdateJournal.setInt(1, journalID);
        
      } else {
      
        this.statUpdateJournal.setNull(1, Types.NULL);
      }
      
      this.statUpdateJournal.setInt(2, documentID);

      result = this.statUpdateJournal.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      if (oldJournal != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateJournalEvent(CurrentProject.getInstance().getFactoryDAO().getJournalDAO().refreshJournal(oldJournal)));
      }

      if (journalID != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateJournalEvent(getJournal(documentID)));
      }
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new JournalRelationDocumentEvent());
    }
      
    return result;
  }

  /**
   * @param publishDateID the idPublishDate to set
   */
  public boolean setPublishDate(Integer documentID, Integer publishDateID, boolean notifyObservers)
          throws KnowledgeBaseException {
    
    boolean result = false;
    PublishDate oldPublishDate = null;
    
    // Notify to the observer
    if (notifyObservers) {
    
      oldPublishDate = getPublishDate(documentID);
    }

    try {

      this.statUpdatePublishDate.clearParameters();

      if (publishDateID != null) {
      
        this.statUpdatePublishDate.setInt(1, publishDateID);
        
      } else {
      
        this.statUpdatePublishDate.setNull(1, Types.NULL);
      }
      
      this.statUpdatePublishDate.setInt(2, documentID);

      result = this.statUpdatePublishDate.executeUpdate() > 0;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }

    // Notify to the observer
    if (notifyObservers) {

      PublishDateDAO publishDateDAO = CurrentProject.getInstance().getFactoryDAO().getPublishDateDAO();
      
      if (oldPublishDate != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePublishDateEvent(publishDateDAO.refreshPublishDate(oldPublishDate)));
        
      }

      if (publishDateID != null) {

        KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdatePublishDateEvent(getPublishDate(documentID)));
        
      }
      
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new UpdateDocumentEvent(getDocument(documentID)));
      KnowledgeBaseEventsReceiver.getInstance().addEvent(new PublishDateRelationDocumentEvent());
    }
      
    return result;
  }

  /**
   * 
   * @return
   * @throws KnowledgeBaseException
   */
  public Journal getJournal(Integer documentID) throws KnowledgeBaseException {

    ResultSet rs;
    Journal journal = null;

    try {

      this.statSelectJournal.clearParameters();

      this.statSelectJournal.setInt(1, documentID);

      rs = this.statSelectJournal.executeQuery();

      if (rs.next()) {

        journal = UtilsDAO.getInstance().getJournal(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return journal;
  }

  /**
   *
   * @return
   * @throws KnowledgeBaseException
   */
  public PublishDate getPublishDate(Integer documentID) throws KnowledgeBaseException {

    ResultSet rs;
    PublishDate publishDate = null;

    try {

      this.statSelectPublishDate.clearParameters();

      this.statSelectPublishDate.setInt(1, documentID);

      rs = this.statSelectPublishDate.executeQuery();

      if (rs.next()) {

        publishDate = UtilsDAO.getInstance().getPublishDate(rs);
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return publishDate;
  }

  /**
   *
   * @return an array with the authors associated with this document
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<DocumentAuthor> getDocumentAuthors(Integer documentID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<DocumentAuthor> documentsList = new ArrayList<DocumentAuthor>();

    try {

      this.statSelectDocumentAuthor.clearParameters();

      this.statSelectDocumentAuthor.setInt(1, documentID);

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
   * @return an array with the authors associated with this document
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Author> getAuthors(Integer documentID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Author> authors = new ArrayList<Author>();

    try {

      this.statSelectAuthors.clearParameters();

      this.statSelectAuthors.setInt(1, documentID);

      rs = this.statSelectAuthors.executeQuery();

      while (rs.next()) {
              
        authors.add(UtilsDAO.getInstance().getAuthor(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authors;
  }
  
  /**
   * 
   */
  public ArrayList<Author> getAuthorsWithoutGroup(Integer documentID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Author> authors = new ArrayList<Author>();

    try {

      this.statSelectAuthorsWithoutGroup.clearParameters();

      this.statSelectAuthorsWithoutGroup.setInt(1, documentID);

      rs = this.statSelectAuthorsWithoutGroup.executeQuery();

      while (rs.next()) {
              
        authors.add(UtilsDAO.getInstance().getAuthor(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return authors;
  }

  /**
   *
   * @return an array with the affiliation associated with this documents
   *
   * @throws KnowledgeBaseException
   */
  public ArrayList<Affiliation> getAffiliations(Integer documentID)
          throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Affiliation> affiliaitionsList = new ArrayList<Affiliation>();

    try {

      this.statSelectAffiliation.clearParameters();

      this.statSelectAffiliation.setInt(1, documentID);

      rs = this.statSelectAffiliation.executeQuery();

      while (rs.next()) {

        affiliaitionsList.add(UtilsDAO.getInstance().getAffiliation(rs));
      }

      rs.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return affiliaitionsList;
  }

  /**
   *
   * @param documentID
   *
   * @return an array with the references associated with this document
   * 
   * @throws KnowledgeBaseException
   */
  public ArrayList<Reference> getReferences(Integer documentID) throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Reference> referenceList = new ArrayList<Reference>();

    try {

      this.statSelectReferences.clearParameters();

      this.statSelectReferences.setInt(1, documentID);

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
   * @param documentID
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Reference> getReferencesWithoutGroup(Integer documentID) throws KnowledgeBaseException{

    ResultSet rs;
    ArrayList<Reference> referenceList = new ArrayList<Reference>();

    try {

      this.statSelectReferenceWithoutGroup.clearParameters();

      this.statSelectReferenceWithoutGroup.setInt(1, documentID);

      rs = this.statSelectReferenceWithoutGroup.executeQuery();

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
   * @return an array with the {@link DocumentFromWord} associated with this
   * {@link DocumentDAO}.
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
  public ArrayList<Word> getWords(Integer documentID, boolean isAuthorWord,
          boolean isSourceWord, boolean isAddedWord) throws KnowledgeBaseException{

    String query, optional;
    ResultSet rs;
    ArrayList<Word> wordsList = new ArrayList<Word>();

    try {

      query = __SELECT_WORD;
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

      rs = stat.executeQuery(query.replace("?", String.valueOf(documentID)));

      while (rs.next()) {

        wordsList.add(UtilsDAO.getInstance().getWord(rs));
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
   * @param documentID
   * @param isAuthorWord
   * @param isSourceWord
   * @param isAddedWord
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Word> getWordsWithoutGroup(Integer documentID, boolean isAuthorWord,
          boolean isSourceWord, boolean isAddedWord) throws KnowledgeBaseException{

    String query, optional;
    ResultSet rs;
    ArrayList<Word> wordsList = new ArrayList<Word>();

    try {

      query = __SELECT_WORD_WITHOUT_GROUP;
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
      
      rs = stat.executeQuery(query.replace("?", String.valueOf(documentID)));

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
   * @param documentID
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Word> getWords(Integer documentID) throws KnowledgeBaseException{
    ResultSet rs;
    ArrayList<Word> wordsList = new ArrayList<Word>();

    try {

      this.statSelectWords.clearParameters();

      this.statSelectWords.setInt(1, documentID);

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
   * @param documentID
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Word> getWordsWithoutGroup(Integer documentID) throws KnowledgeBaseException{
    ResultSet rs;
    ArrayList<Word> wordsList = new ArrayList<Word>();

    try {

      this.statSelectWordsWithoutGroup.clearParameters();

      this.statSelectWordsWithoutGroup.setInt(1, documentID);

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
   * <p>Check if there is an <code>Document</code> with this ID.</p>
   *
   * @param idDocument the document's ID
   *
   * @return true if there is an <code>Document</code> with these attributes
   *
   * @throws KnowledgeBaseException if a database access error occurs
   */
  public boolean checkDocument(Integer idDocument)
          throws KnowledgeBaseException {

    boolean result = false;
    ResultSet rs;

    try {
      
      this.statCheckDocumentById.clearParameters();

      this.statCheckDocumentById.setInt(1, idDocument);

      rs = this.statCheckDocumentById.executeQuery();
      
      result = rs.next();
      
      rs.close();
      
      return result;

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }
  
  /**
   * 
   * @param documentsToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Document> refreshDocuments(ArrayList<Document> documentsToRefresh) throws KnowledgeBaseException {
  
    int i;
    String query;
    ResultSet rs;
    ArrayList<Document> documents = new ArrayList<Document>();
    
    i = 0;
    
    if (!documentsToRefresh.isEmpty()) {

      query = "SELECT * FROM Document WHERE idDocument IN (" + documentsToRefresh.get(i).getDocumentID();
      
      for (i = 1; i < documentsToRefresh.size(); i++) {
        
        query += ", " + documentsToRefresh.get(i).getDocumentID();
      }
      
      query += ");";
      
      try {

        Statement stat = this.kbm.getConnection().createStatement();
        rs = stat.executeQuery(query);

        while (rs.next()) {

          documents.add(UtilsDAO.getInstance().getDocument(rs));
        }

        rs.close();
        stat.close();

      } catch (SQLException e) {

        throw new KnowledgeBaseException(e.getMessage(), e.getCause());
      }
    }
    
    return documents;
  }
  
  /**
   * 
   * @param documentToRefresh
   * @return
   * @throws KnowledgeBaseException 
   */
  public Document refreshDocument(Document documentToRefresh) throws KnowledgeBaseException {
  
    return getDocument(documentToRefresh.getDocumentID());
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
