/*
 * UpgradeKnowledgeBaseVersion.java
 *
 * Created on 08-dic-2011, 21:05:45
 */
package scimat.model.upgrade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.sqlite.SQLiteConfig;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.dao.FactoryDAO;
import scimat.model.knowledgebase.exception.IncorrectFormatKnowledgeBaseException;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;



/**
 *
 * @author mjcobo
 */
public class UpgradeKnowledgeBaseVersion {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String newKnowledgeBaseFile;
  private String oldKnowledgeBaseFile;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param newKnowledgeBaseFile
   * @param olKnowledgeBaseFile 
   */
  public UpgradeKnowledgeBaseVersion(String newKnowledgeBaseFile, String olKnowledgeBaseFile) {
    this.newKnowledgeBaseFile = newKnowledgeBaseFile;
    this.oldKnowledgeBaseFile = olKnowledgeBaseFile;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  public void execute() throws KnowledgeBaseException {
  
    KnowledgeBaseVersion kbVersion;
    
    kbVersion = CurrentProject.getInstance().checkKnowledgeBaseVersion(oldKnowledgeBaseFile);
    
    switch (kbVersion) {
    
      case V_1_01:
        updgradeFromV1_01();
        break;
        
      case V_1_02:
        updgradeFromV1_02();
        break;
        
      case V_1_03:
        throw new IncorrectFormatKnowledgeBaseException("The knowledge base has a current version.");
        
      case UNDEFINED:
        throw new IncorrectFormatKnowledgeBaseException("Incorrect knowledge base format.");
        
      default:
        throw new IncorrectFormatKnowledgeBaseException("Incorrect knowledge base format.");
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
  /**
   * 
   * @throws KnowledgeBaseException 
   */
  private void updgradeFromV1_01() throws KnowledgeBaseException {
    
    int i, j;
    Integer authorID, authorGroupID, authorReferenceID, authorReferenceGroupID, 
            documentID, journalId, referenceID, referenceGroupID, referenceSourceID, 
            referenceSourceGroupID, publishDateID, wordID, wordGroupID;
    KnowledgeBaseManager newKnowledgeBaseManager = null;
    Connection conn = null;
    PreparedStatement stat = null;
    ResultSet rs = null;
    FactoryDAO newFactoryDAO;
    
    try {
      
      // Connect to the database with lower version
      SQLiteConfig config = new SQLiteConfig();
      config.enforceForeignKeys(true);
      //config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
      conn = DriverManager.getConnection("jdbc:sqlite:" + this.oldKnowledgeBaseFile, config.toProperties());
      
      // Create the new knowledge base
      newKnowledgeBaseManager = new KnowledgeBaseManager();
      newKnowledgeBaseManager.createKnowledgeBase(this.newKnowledgeBaseFile);
      
      // Build the Factory DAO of the new version
      newFactoryDAO = new FactoryDAO(newKnowledgeBaseManager);
    
      // Add AuthorGroup
      
      stat = conn.prepareStatement("Select idAuthorGroup, groupName, stopGroup FROM AuthorGroup;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getAuthorGroupDAO().addAuthorGroup(rs.getInt("idAuthorGroup"), 
                rs.getString("groupName"), 
                rs.getBoolean("stopGroup"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add AuthorReferenceGroup
      stat = conn.prepareStatement("Select idAuthorReferenceGroup, groupName,stopGroup FROM AuthorReferenceGroup;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getAuthorReferenceGroupDAO().addAuthorReferenceGroup(rs.getInt("idAuthorReferenceGroup"), 
                rs.getString("groupName"), 
                rs.getBoolean("stopGroup"), 
                false); // Do not notify to the observers
      
      }
      
      rs.close();
      stat.close();
    
      // Add ReferenceGroup
      stat = conn.prepareStatement("Select idReferenceGroup, groupName, stopGroup FROM ReferenceGroup;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getReferenceGroupDAO().addReferenceGroup(rs.getInt("idReferenceGroup"), 
                rs.getString("groupName"), 
                rs.getBoolean("stopGroup"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add ReferenceSourceGroup
      stat = conn.prepareStatement("Select idReferenceSourceGroup, groupName, stopGroup FROM ReferenceSourceGroup;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getReferenceSourceGroupDAO().addReferenceSourceGroup(rs.getInt("idReferenceSourceGroup"), 
                rs.getString("groupName"), 
                rs.getBoolean("stopGroup"), 
                false); // Do not notify to the observers
      
      }
      
      rs.close();
      stat.close();
    
      // Add WordGroup
      stat = conn.prepareStatement("Select idWordGroup, groupName, stopGroup FROM WordGroup;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getWordGroupDAO().addWordGroup(rs.getInt("idWordGroup"), 
                rs.getString("groupName"), 
                rs.getBoolean("stopGroup"), 
                false); // Do not notify to the observers
      
      }
      
      rs.close();
      stat.close();
    
      // Add Affiliation
      stat = conn.prepareStatement("Select idAffiliation, fullAffiliation FROM Affiliation;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getAffiliationDAO().addAffiliation(rs.getInt("idAffiliation"), 
                rs.getString("fullAffiliation"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Author
      stat = conn.prepareStatement("Select idAuthor, authorName, fullAuthorName, AuthorGroup_idAuthorGroup FROM Author;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        authorID = rs.getInt("idAuthor");
        
        newFactoryDAO.getAuthorDAO().addAuthor(authorID, 
                rs.getString("authorName"), 
                rs.getString("fullAuthorName"), 
                false); // Do not notify to the observers
        
        authorGroupID = rs.getInt("AuthorGroup_idAuthorGroup");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
        
          newFactoryDAO.getAuthorDAO().setAuthorGroup(authorID, 
                  authorGroupID, 
                  false);// Do not notify to the observers
        }
      }
      
      rs.close();
      stat.close();
    
      // Add AuthorReference
      stat = conn.prepareStatement("Select idAuthorReference, authorName, AuthorReferenceGroup_idAuthorReferenceGroup, Author_idAuthor FROM AuthorReference;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        authorReferenceID = rs.getInt("idAuthorReference");
        
        newFactoryDAO.getAuthorReferenceDAO().addAuthorReference(authorReferenceID, 
                rs.getString("authorName"), 
                false); // Do not notify to the observers
        
        authorReferenceGroupID = rs.getInt("AuthorReferenceGroup_idAuthorReferenceGroup");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
         
          newFactoryDAO.getAuthorReferenceDAO().setAuthorReferenceGroup(authorReferenceID, 
                  authorReferenceGroupID, 
                  false); // Do not notify to the observers
        }
        
        authorID = rs.getInt("Author_idAuthor");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
          
          newFactoryDAO.getAuthorReferenceDAO().setAuthor(authorReferenceID, 
                  authorID, 
                  false); // Do not notify to the observers
        }
      }
      
      rs.close();
      stat.close();
    
      // Add Journal
      stat = conn.prepareStatement("Select idJournal, source, conferenceInformation FROM Journal;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getJournalDAO().addJournal(rs.getInt("idJournal"), 
                rs.getString("source"), 
                rs.getString("conferenceInformation"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Period
      stat = conn.prepareStatement("Select idPeriod, name, position FROM Period;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getPeriodDAO().addPeriod(rs.getInt("idPeriod"), 
                rs.getString("name"), 
                rs.getInt("position"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add PublishDate
      stat = conn.prepareStatement("Select idPublishDate, year, date FROM PublishDate;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getPublishDateDAO().addPublishDate(rs.getInt("idPublishDate"), 
                rs.getString("year"), 
                rs.getString("date"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add ReferenceSource
      stat = conn.prepareStatement("Select idReferenceSource, source, ReferenceSourceGroup_idReferencesourceGroup FROM ReferenceSource;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        referenceSourceID = rs.getInt("idReferenceSource");
        
        newFactoryDAO.getReferenceSourceDAO().addReferenceSource(referenceSourceID, 
                rs.getString("source"), 
                false); // Do not notify to the observers
        
        referenceSourceGroupID = rs.getInt("ReferenceSourceGroup_idReferencesourceGroup");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
          
          newFactoryDAO.getReferenceSourceDAO().setReferenceSourceGroup(referenceSourceID, 
                  referenceSourceGroupID, 
                  false); // Do not notify to the observers
        }
      }
      
      rs.close();
      stat.close();
    
      // Add Reference
      stat = conn.prepareStatement("Select idReference, fullReference, ReferenceSource_idReferenceSource, volume, issue, page, doi, format, year, ReferenceGroup_idReferenceGroup FROM Reference;");
      rs = stat.executeQuery();
      
      while (rs.next()) {

        referenceID = rs.getInt("idReference");

        newFactoryDAO.getReferenceDAO().addReference(rs.getInt("idReference"),
                rs.getString("fullReference"),
                rs.getString("volume"),
                rs.getString("issue"),
                rs.getString("page"),
                rs.getString("year"),
                rs.getString("doi"),
                rs.getString("format"),
                false); // Do not notify to the observers

        referenceSourceID = rs.getInt("ReferenceSource_idReferenceSource");

        if (!rs.wasNull()) { // If the las retrieved column was not null

          newFactoryDAO.getReferenceDAO().setReferenceSource(referenceID,
                  referenceSourceID,
                  false); // Do not notify to the observers
        }

        referenceGroupID = rs.getInt("ReferenceGroup_idReferenceGroup");

        if (!rs.wasNull()) { // If the las retrieved column was not null
          
          newFactoryDAO.getReferenceDAO().setReferenceGroup(referenceID, 
                  referenceGroupID, 
                  false); // Do not notify to the observers
        }
      }
      
      rs.close();
      stat.close();
    
      // Add SubjectCategory
      stat = conn.prepareStatement("Select idSubjectCategory, subjectCategoryName FROM SubjectCategory;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getSubjectCategoryDAO().addSubjectCategory(rs.getInt("idSubjectCategory"), 
                rs.getString("subjectCategoryName"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Word
      stat = conn.prepareStatement("Select idWord, wordName, WordGroup_idWordGroup FROM Word;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        wordID = rs.getInt("idWord");
        
        newFactoryDAO.getWordDAO().addWord(wordID, 
                rs.getString("wordName"), 
                false);
        
        wordGroupID = rs.getInt("WordGroup_idWordGroup");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
          
          newFactoryDAO.getWordDAO().setWordGroup(wordID, 
                  wordGroupID, 
                  false); // Do not notify to the observers
        }
      }
      
      rs.close();
      stat.close();
    
      // Add Document
      stat = conn.prepareStatement("Select idDocument, title, type, docAbstract, volume, issue, beginPage, endPage, citationsCount, doi, sourceIdentifier, PublishDate_idPublishDate, Journal_idJournal FROM Document;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        documentID = rs.getInt("idDocument");
        
        newFactoryDAO.getDocumentDAO().addDocument(rs.getInt("idDocument"), 
                rs.getString("title"), 
                rs.getString("docAbstract"), 
                rs.getString("type"), 
                rs.getInt("citationsCount"), 
                rs.getString("doi"), 
                rs.getString("sourceIdentifier"), 
                rs.getString("volume"), 
                rs.getString("issue"), 
                rs.getString("beginPage"), 
                rs.getString("endPage"), 
                false); // Do not notify to the observers
        
        publishDateID = rs.getInt("PublishDate_idPublishDate");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
        
          newFactoryDAO.getDocumentDAO().setPublishDate(documentID, 
                  publishDateID, 
                  false); // Do not notify to the observers
        }
        
        journalId = rs.getInt("Journal_idJournal");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
          
          newFactoryDAO.getDocumentDAO().setJournal(documentID, 
                  journalId, 
                  false); // Do not notify to the observers
        }
      }
      
      rs.close();
      stat.close();
    
      // Add PublishDate_Period
      stat = conn.prepareStatement("Select PublishDate_idPublishDate, Period_idPeriod FROM PublishDate_Period;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getPublishDatePeriodDAO().addPublishDatePeriod(rs.getInt("Period_idPeriod"), 
                rs.getInt("PublishDate_idPublishDate"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Journal_SubjectCategory_PublishDate
      stat = conn.prepareStatement("Select Journal_idJournal, SubjectCategory_idSubjectCategory, PublishDate_idPublishDate FROM Journal_SubjectCategory_PublishDate;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getJournalSubjectCategoryPublishDateDAO().addSubjectCategoryToJournal(rs.getInt("SubjectCategory_idSubjectCategory"), 
                rs.getInt("Journal_idJournal"), 
                rs.getInt("PublishDate_idPublishDate"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Document_Author
      stat = conn.prepareStatement("Select Author_idAuthor, Document_idDocument, position FROM Document_Author;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getDocumentAuthorDAO().addDocumentAuthor(rs.getInt("Document_idDocument"), 
                rs.getInt("Author_idAuthor"), 
                rs.getInt("position"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Document_Affiliation
      stat = conn.prepareStatement("Select Affiliation_idAffiliation, Document_idDocument FROM Document_Affiliation;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getDocumentAffiliationDAO().addDocumentAffiliation(rs.getInt("Document_idDocument"), 
                rs.getInt("Affiliation_idAffiliation"),
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Document_Word
      stat = conn.prepareStatement("Select Document_idDocument, Word_idWord, authorKeyword, sourceKeyword, extractedKeyword FROM Document_Word;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getDocumentWordDAO().addDocumentWord(rs.getInt("Document_idDocument"), 
                rs.getInt("Word_idWord"),
                rs.getBoolean("authorKeyword"),
                rs.getBoolean("sourceKeyword"),
                rs.getBoolean("extractedKeyword"),
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Document_Refernece
      stat = conn.prepareStatement("Select Reference_idReference, Document_idDocument FROM Document_Reference;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getDocumentReferenceDAO().addDocumentReference(rs.getInt("Document_idDocument"), 
                rs.getInt("Reference_idReference"),
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add AuthorReference_Reference
      stat = conn.prepareStatement("Select AuthorReference_idAuthorReference, Reference_idReference, position FROM AuthorReference_Reference;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getAuthorReferenceReferenceDAO().addAuthorReferenceReference(rs.getInt("Reference_idReference"), 
                rs.getInt("AuthorReference_idAuthorReference"), 
                rs.getInt("position"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Author_Affiliation
      stat = conn.prepareStatement("Select Affiliation_idAffiliation, Author_idAuthor FROM Author_Affiliation;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getAuthorAffiliationDAO().addAuthorAffiliation(rs.getInt("Author_idAuthor"), 
                rs.getInt("Affiliation_idAffiliation"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
      
      newKnowledgeBaseManager.commit();
      newKnowledgeBaseManager.close();
      conn.close();
      
    } catch (SQLException sqle) {
      try {
        conn.close();
      } catch (SQLException ex) {
        
        throw new KnowledgeBaseException(ex.getCause());
      }
      
    } catch (KnowledgeBaseException kbe) {
    
      newKnowledgeBaseManager.rollback();
      newKnowledgeBaseManager.close();
      
      throw kbe;
    }
  }
  
  /**
   * 
   * @throws scimat.model.knowledgebase.exception.KnowledgeBaseException 
   */
  private void updgradeFromV1_02() throws scimat.model.knowledgebase.exception.KnowledgeBaseException {
    
    int i, j;
    Integer authorID, authorGroupID, authorReferenceID, authorReferenceGroupID, 
            documentID, journalId, referenceID, referenceGroupID, referenceSourceID, 
            referenceSourceGroupID, publishDateID, wordID, wordGroupID;
    KnowledgeBaseManager newKnowledgeBaseManager = null;
    Connection conn = null;
    PreparedStatement stat = null;
    ResultSet rs = null;
    FactoryDAO newFactoryDAO;
    
    try {
      
      // Connect to the database with lower version
      SQLiteConfig config = new SQLiteConfig();
      config.enforceForeignKeys(true);
      //config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
      conn = DriverManager.getConnection("jdbc:sqlite:" + this.oldKnowledgeBaseFile, config.toProperties());
      
      // Create the new knowledge base
      newKnowledgeBaseManager = new KnowledgeBaseManager();
      newKnowledgeBaseManager.createKnowledgeBase(this.newKnowledgeBaseFile);
      
      // Build the Factory DAO of the new version
      newFactoryDAO = new FactoryDAO(newKnowledgeBaseManager);
    
      // Add AuthorGroup
      System.out.println("Adding AuthorGroup...");
      stat = conn.prepareStatement("Select idAuthorGroup, groupName, stopGroup FROM AuthorGroup;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getAuthorGroupDAO().addAuthorGroup(rs.getInt("idAuthorGroup"), 
                rs.getString("groupName"), 
                rs.getBoolean("stopGroup"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add AuthorReferenceGroup
      System.out.println("Adding AuthorReferenceGroup...");
      stat = conn.prepareStatement("Select idAuthorReferenceGroup, groupName,stopGroup FROM AuthorReferenceGroup;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getAuthorReferenceGroupDAO().addAuthorReferenceGroup(rs.getInt("idAuthorReferenceGroup"), 
                rs.getString("groupName"), 
                rs.getBoolean("stopGroup"), 
                false); // Do not notify to the observers
      
      }
      
      rs.close();
      stat.close();
    
      // Add ReferenceGroup
      System.out.println("Adding ReferenceGroup...");
      stat = conn.prepareStatement("Select idReferenceGroup, groupName, stopGroup FROM ReferenceGroup;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getReferenceGroupDAO().addReferenceGroup(rs.getInt("idReferenceGroup"), 
                rs.getString("groupName"), 
                rs.getBoolean("stopGroup"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add ReferenceSourceGroup
      stat = conn.prepareStatement("Select idReferenceSourceGroup, groupName, stopGroup FROM ReferenceSourceGroup;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getReferenceSourceGroupDAO().addReferenceSourceGroup(rs.getInt("idReferenceSourceGroup"), 
                rs.getString("groupName"), 
                rs.getBoolean("stopGroup"), 
                false); // Do not notify to the observers
      
      }
      
      rs.close();
      stat.close();
    
      // Add WordGroup
      System.out.println("Adding WordGroup...");
      stat = conn.prepareStatement("Select idWordGroup, groupName, stopGroup FROM WordGroup;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getWordGroupDAO().addWordGroup(rs.getInt("idWordGroup"), 
                rs.getString("groupName"), 
                rs.getBoolean("stopGroup"), 
                false); // Do not notify to the observers
      
      }
      
      rs.close();
      stat.close();
    
      // Add Affiliation
      System.out.println("Adding Afiiliation...");
      stat = conn.prepareStatement("Select idAffiliation, fullAffiliation FROM Affiliation;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getAffiliationDAO().addAffiliation(rs.getInt("idAffiliation"), 
                rs.getString("fullAffiliation"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Author
      System.out.println("Adding Author...");
      stat = conn.prepareStatement("Select idAuthor, authorName, fullAuthorName, AuthorGroup_idAuthorGroup FROM Author;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        authorID = rs.getInt("idAuthor");
        
        newFactoryDAO.getAuthorDAO().addAuthor(authorID, 
                rs.getString("authorName"), 
                rs.getString("fullAuthorName"), 
                false); // Do not notify to the observers
        
        authorGroupID = rs.getInt("AuthorGroup_idAuthorGroup");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
        
          newFactoryDAO.getAuthorDAO().setAuthorGroup(authorID, 
                  authorGroupID, 
                  false);// Do not notify to the observers
        }
      }
      
      rs.close();
      stat.close();
    
      // Add AuthorReference
      System.out.println("Adding AuthorReference...");
      stat = conn.prepareStatement("Select idAuthorReference, authorName, AuthorReferenceGroup_idAuthorReferenceGroup, Author_idAuthor FROM AuthorReference;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        authorReferenceID = rs.getInt("idAuthorReference");
        
        newFactoryDAO.getAuthorReferenceDAO().addAuthorReference(authorReferenceID, 
                rs.getString("authorName"), 
                false); // Do not notify to the observers
        
        authorReferenceGroupID = rs.getInt("AuthorReferenceGroup_idAuthorReferenceGroup");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
         
          newFactoryDAO.getAuthorReferenceDAO().setAuthorReferenceGroup(authorReferenceID, 
                  authorReferenceGroupID, 
                  false); // Do not notify to the observers
        }
        
        authorID = rs.getInt("Author_idAuthor");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
          
          newFactoryDAO.getAuthorReferenceDAO().setAuthor(authorReferenceID, 
                  authorID, 
                  false); // Do not notify to the observers
        }
      }
      
      rs.close();
      stat.close();
    
      // Add Journal
      System.out.println("Adding Journal...");
      stat = conn.prepareStatement("Select idJournal, source, conferenceInformation FROM Journal;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getJournalDAO().addJournal(rs.getInt("idJournal"), 
                rs.getString("source"), 
                rs.getString("conferenceInformation"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Period
      System.out.println("Adding Period...");
      stat = conn.prepareStatement("Select idPeriod, name, position FROM Period;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getPeriodDAO().addPeriod(rs.getInt("idPeriod"), 
                rs.getString("name"), 
                rs.getInt("position"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add PublishDate
      stat = conn.prepareStatement("Select idPublishDate, year, date FROM PublishDate;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getPublishDateDAO().addPublishDate(rs.getInt("idPublishDate"), 
                rs.getString("year"), 
                rs.getString("date"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add ReferenceSource
      System.out.println("Adding ReferenceSource...");
      stat = conn.prepareStatement("Select idReferenceSource, source, ReferenceSourceGroup_idReferencesourceGroup FROM ReferenceSource;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        referenceSourceID = rs.getInt("idReferenceSource");
        
        newFactoryDAO.getReferenceSourceDAO().addReferenceSource(referenceSourceID, 
                rs.getString("source"), 
                false); // Do not notify to the observers
        
        referenceSourceGroupID = rs.getInt("ReferenceSourceGroup_idReferencesourceGroup");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
          
          newFactoryDAO.getReferenceSourceDAO().setReferenceSourceGroup(referenceSourceID, 
                  referenceSourceGroupID, 
                  false); // Do not notify to the observers
        }
      }
      
      rs.close();
      stat.close();
    
      // Add Reference
      System.out.println("Adding Reference...");
      stat = conn.prepareStatement("Select idReference, fullReference, ReferenceSource_idReferenceSource, volume, issue, page, doi, format, year, ReferenceGroup_idReferenceGroup FROM Reference;");
      rs = stat.executeQuery();
      
      while (rs.next()) {

        referenceID = rs.getInt("idReference");

        newFactoryDAO.getReferenceDAO().addReference(rs.getInt("idReference"),
                rs.getString("fullReference"),
                rs.getString("volume"),
                rs.getString("issue"),
                rs.getString("page"),
                rs.getString("year"),
                rs.getString("doi"),
                rs.getString("format"),
                false); // Do not notify to the observers

        referenceSourceID = rs.getInt("ReferenceSource_idReferenceSource");

        if (!rs.wasNull()) { // If the las retrieved column was not null

          newFactoryDAO.getReferenceDAO().setReferenceSource(referenceID,
                  referenceSourceID,
                  false); // Do not notify to the observers
        }

        referenceGroupID = rs.getInt("ReferenceGroup_idReferenceGroup");

        if (!rs.wasNull()) { // If the las retrieved column was not null
          
          newFactoryDAO.getReferenceDAO().setReferenceGroup(referenceID, 
                  referenceGroupID, 
                  false); // Do not notify to the observers
        }
      }
      
      rs.close();
      stat.close();
    
      // Add SubjectCategory
      System.out.println("Adding SubjectCategory...");
      stat = conn.prepareStatement("Select idSubjectCategory, subjectCategoryName FROM SubjectCategory;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getSubjectCategoryDAO().addSubjectCategory(rs.getInt("idSubjectCategory"), 
                rs.getString("subjectCategoryName"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Word
      System.out.println("Adding Word...");
      stat = conn.prepareStatement("Select idWord, wordName, WordGroup_idWordGroup FROM Word;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        wordID = rs.getInt("idWord");
        
        newFactoryDAO.getWordDAO().addWord(wordID, 
                rs.getString("wordName"), 
                false);
        
        wordGroupID = rs.getInt("WordGroup_idWordGroup");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
          
          newFactoryDAO.getWordDAO().setWordGroup(wordID, 
                  wordGroupID, 
                  false); // Do not notify to the observers
        }
      }
      
      rs.close();
      stat.close();
    
      // Add Document
      System.out.println("Adding Document...");
      stat = conn.prepareStatement("Select idDocument, title, type, docAbstract, volume, issue, beginPage, endPage, citationsCount, doi, sourceIdentifier, PublishDate_idPublishDate, Journal_idJournal FROM Document;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        documentID = rs.getInt("idDocument");
        
        newFactoryDAO.getDocumentDAO().addDocument(rs.getInt("idDocument"), 
                rs.getString("title"), 
                rs.getString("docAbstract"), 
                rs.getString("type"), 
                rs.getInt("citationsCount"), 
                rs.getString("doi"), 
                rs.getString("sourceIdentifier"), 
                rs.getString("volume"), 
                rs.getString("issue"), 
                rs.getString("beginPage"), 
                rs.getString("endPage"), 
                false); // Do not notify to the observers
        
        publishDateID = rs.getInt("PublishDate_idPublishDate");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
        
          newFactoryDAO.getDocumentDAO().setPublishDate(documentID, 
                  publishDateID, 
                  false); // Do not notify to the observers
        }
        
        journalId = rs.getInt("Journal_idJournal");
        
        if (! rs.wasNull()) { // If the las retrieved column was not null
          
          newFactoryDAO.getDocumentDAO().setJournal(documentID, 
                  journalId, 
                  false); // Do not notify to the observers
        }
      }
      
      rs.close();
      stat.close();
    
      // Add PublishDate_Period
      System.out.println("Adding PublishDate to Period...");
      stat = conn.prepareStatement("Select PublishDate_idPublishDate, Period_idPeriod FROM PublishDate_Period;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getPublishDatePeriodDAO().addPublishDatePeriod(rs.getInt("Period_idPeriod"), 
                rs.getInt("PublishDate_idPublishDate"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Journal_SubjectCategory_PublishDate
      System.out.println("Adding Journal To SubjectCategory...");
      stat = conn.prepareStatement("Select Journal_idJournal, SubjectCategory_idSubjectCategory, PublishDate_idPublishDate FROM Journal_SubjectCategory_PublishDate;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getJournalSubjectCategoryPublishDateDAO().addSubjectCategoryToJournal(rs.getInt("SubjectCategory_idSubjectCategory"), 
                rs.getInt("Journal_idJournal"), 
                rs.getInt("PublishDate_idPublishDate"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Document_Author
      System.out.println("Adding Document to Author...");
      stat = conn.prepareStatement("Select Author_idAuthor, Document_idDocument, position FROM Document_Author;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getDocumentAuthorDAO().addDocumentAuthor(rs.getInt("Document_idDocument"), 
                rs.getInt("Author_idAuthor"), 
                rs.getInt("position"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Document_Affiliation
      System.out.println("Adding Document to Affiliation...");
      stat = conn.prepareStatement("Select Affiliation_idAffiliation, Document_idDocument FROM Document_Affiliation;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getDocumentAffiliationDAO().addDocumentAffiliation(rs.getInt("Document_idDocument"), 
                rs.getInt("Affiliation_idAffiliation"),
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Document_Word
      System.out.println("Adding Document to Word...");
      stat = conn.prepareStatement("Select Document_idDocument, Word_idWord, isAuthorWord, isSourceWord, isAddedWord FROM Document_Word;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getDocumentWordDAO().addDocumentWord(rs.getInt("Document_idDocument"), 
                rs.getInt("Word_idWord"),
                rs.getBoolean("isAuthorWord"),
                rs.getBoolean("isSourceWord"),
                rs.getBoolean("isAddedWord"),
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Document_Refernece
      System.out.println("Adding Document to Reference...");
      stat = conn.prepareStatement("Select Reference_idReference, Document_idDocument FROM Document_Reference;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getDocumentReferenceDAO().addDocumentReference(rs.getInt("Document_idDocument"), 
                rs.getInt("Reference_idReference"),
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add AuthorReference_Reference
      System.out.println("Adding AuthorReference To Reference...");
      stat = conn.prepareStatement("Select AuthorReference_idAuthorReference, Reference_idReference, position FROM AuthorReference_Reference;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getAuthorReferenceReferenceDAO().addAuthorReferenceReference(rs.getInt("Reference_idReference"), 
                rs.getInt("AuthorReference_idAuthorReference"), 
                rs.getInt("position"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
    
      // Add Author_Affiliation
      System.out.println("Adding Author to Affiliation...");
      stat = conn.prepareStatement("Select Affiliation_idAffiliation, Author_idAuthor FROM Author_Affiliation;");
      rs = stat.executeQuery();
      
      while (rs.next()) {
      
        newFactoryDAO.getAuthorAffiliationDAO().addAuthorAffiliation(rs.getInt("Author_idAuthor"), 
                rs.getInt("Affiliation_idAffiliation"), 
                false); // Do not notify to the observers
      }
      
      rs.close();
      stat.close();
      
      newKnowledgeBaseManager.commit();
      newKnowledgeBaseManager.close();
      conn.close();
      
    } catch (SQLException sqle) {
      try {
        conn.close();
      } catch (SQLException ex) {
        
        throw new KnowledgeBaseException(ex.getCause());
      }
      
      throw new KnowledgeBaseException(sqle.getCause());
      
    } catch (KnowledgeBaseException kbe) {
    
      newKnowledgeBaseManager.rollback();
      newKnowledgeBaseManager.close();
      
      throw kbe;
    }
  }
}
