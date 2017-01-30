/*
 * KnowledgeBaseManager.java
 *
 * Created on 21-oct-2010, 17:47:44
 */
package scimat.model.knowledgebase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import org.sqlite.SQLiteConfig;
import scimat.model.knowledgebase.exception.IncorrectFormatKnowledgeBaseException;

/**
 * This class implements all the necessary methods to manage the knowledge base.
 * 
 * @author mjcobo
 */
public class KnowledgeBaseManager {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private Connection conn;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  /**
   * 
   * @throws KnowledgeBaseException
   */
  public KnowledgeBaseManager() {

    this.conn = null;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  //---------------------------------------------------------------------------
  // Data base creation and management.
  //---------------------------------------------------------------------------
  /**
   * 
   * @param filePath
   * @return
   * @throws KnowledgeBaseException
   */
  public void createKnowledgeBase(String filePath) throws KnowledgeBaseException {

    Statement stat = null;

    try {

      Class.forName("org.sqlite.JDBC");

      // If the file exists, delete it to make a new database.
      File file = new File(filePath);

      if (file.exists()) {

        if (!file.delete()) {

          throw new KnowledgeBaseException("The file " + filePath + " is locked.");
        }

      }
      
      // To configure the database
      //PRAGMA main.page_size = 4096;
      //PRAGMA main.cache_size=10000;
      //PRAGMA main.locking_mode=EXCLUSIVE;
      //PRAGMA main.synchronous=NORMAL;
      //PRAGMA main.journal_mode=WAL;


      // Connect to the database
      SQLiteConfig config = new SQLiteConfig();
      config.enforceForeignKeys(true);
      config.setPageSize(4096);
      config.setPageSize(65536);
      config.setCacheSize(10000);
      config.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
      this.conn = DriverManager.getConnection("jdbc:sqlite:" + filePath, config.toProperties());
      //this.conn = DriverManager.getConnection("jdbc:sqlite:", config.toProperties());
      
      this.conn.createStatement().execute("PRAGMA journal_mode = WAL;");
      
      this.conn.setAutoCommit(false);
      
      this.conn.createStatement().execute("PRAGMA main.locking_mode=EXCLUSIVE;");
      commit();

      stat = this.conn.createStatement();
      
      stat.addBatch("CREATE TABLE KnowledgeBaseVersion(knowledgeBaseVersion VARCHAR NOT NULL);");
      
      
      stat.addBatch("INSERT INTO KnowledgeBaseVersion VALUES('v1_03')");

      stat.addBatch("CREATE TABLE AuthorGroup(\n"
              + "  idAuthorGroup INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idAuthorGroup>=0),\n"
              + "  groupName VARCHAR NOT NULL UNIQUE,\n"
              + "  stopGroup BOOLEAN DEFAULT FALSE,\n"
              + "  itemsCount INTEGER CHECK(itemsCount>=0) DEFAULT 0,\n"
              + "  authorGroup_documentsCount INTEGER CHECK(authorGroup_documentsCount>=0) DEFAULT 0\n"
              + ");");

      stat.addBatch("CREATE TABLE AuthorReferenceGroup(\n"
              + "  idAuthorReferenceGroup INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idAuthorReferenceGroup>=0),\n"
              + "  groupName VARCHAR NOT NULL UNIQUE,\n"
              + "  stopGroup BOOLEAN DEFAULT FALSE,\n"
              + "  itemsCount INTEGER CHECK(itemsCount>=0) DEFAULT 0,\n"
              + "  authorReferenceGroup_documentsCount INTEGER CHECK(authorReferenceGroup_documentsCount>=0) DEFAULT 0\n"
              + ");");

      stat.addBatch("CREATE TABLE ReferenceGroup(\n"
              + "  idReferenceGroup INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idReferenceGroup>=0),\n"
              + "  groupName VARCHAR NOT NULL UNIQUE,\n"
              + "  stopGroup BOOLEAN DEFAULT FALSE,\n"
              + "  itemsCount INTEGER CHECK(itemsCount>=0) DEFAULT 0,\n"
              + "  referenceGroup_documentsCount INTEGER CHECK(referenceGroup_documentsCount>=0) DEFAULT 0\n"
              + ");");

      stat.addBatch("CREATE TABLE ReferenceSourceGroup(\n"
              + "  idReferenceSourceGroup INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idReferenceSourceGroup>=0),\n"
              + "  groupName VARCHAR NOT NULL UNIQUE,\n"
              + "  stopGroup BOOLEAN DEFAULT FALSE,\n"
              + "  itemsCount INTEGER CHECK(itemsCount>=0) DEFAULT 0,\n"
              + "  referenceSourceGroup_documentsCount INTEGER CHECK(referenceSourceGroup_documentsCount>=0) DEFAULT 0\n"
              + ");");

      stat.addBatch("CREATE TABLE WordGroup(\n"
              + "  idWordGroup INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idWordGroup>=0),\n"
              + "  groupName VARCHAR NOT NULL UNIQUE,\n"
              + "  stopGroup BOOLEAN DEFAULT FALSE,\n"
              + "  itemsCount INTEGER CHECK(itemsCount>=0) DEFAULT 0,\n"
              + "  wordGroup_documentsCount INTEGER CHECK(wordGroup_documentsCount>=0) DEFAULT 0\n"
              + ");");

      stat.addBatch("CREATE TABLE Affiliation(\n"
              + "  idAffiliation INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idAffiliation>=0),\n"
              + "  fullAffiliation VARCHAR NOT NULL UNIQUE,\n"
              + "  affiliation_documentsCount INTEGER CHECK(affiliation_documentsCount>=0) DEFAULT 0,\n"
              + "  affiliation_authorsCount INTEGER CHECK(affiliation_authorsCount>=0) DEFAULT 0\n"
              + ");");

      stat.addBatch("CREATE TABLE Author(\n"
              + "  idAuthor INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idAuthor>=0),\n"
              + "  authorName VARCHAR NOT NULL,\n"
              + "  fullAuthorName VARCHAR NOT NULL,\n"
              + "  author_documentsCount INTEGER CHECK(author_documentsCount>=0) DEFAULT 0,\n"
              + "  author_affiliationsCount INTEGER CHECK(author_affiliationsCount>=0) DEFAULT 0,\n"
              + "  AuthorGroup_idAuthorGroup INTEGER CHECK(AuthorGroup_idAuthorGroup>=0),\n"
              + "  CONSTRAINT author_UNIQUE\n"
              + "     UNIQUE(authorName,fullAuthorName),\n"
              + "  CONSTRAINT fk_Author_AuthorGroup\n"
              + "     FOREIGN KEY(AuthorGroup_idAuthorGroup)\n"
              + "     REFERENCES AuthorGroup(idAuthorGroup)\n"
              + "     ON DELETE SET NULL\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE AuthorReference(\n"
              + "  idAuthorReference INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idAuthorReference>=0),\n"
              + "  authorName VARCHAR NOT NULL UNIQUE,\n"
              + "  authorReference_referencesCount INTEGER CHECK(authorReference_referencesCount>=0) DEFAULT 0,\n"
              + "  authorReference_documentsCount INTEGER CHECK(authorReference_documentsCount>=0) DEFAULT 0,\n"
              + "  AuthorReferenceGroup_idAuthorReferenceGroup INTEGER CHECK(AuthorReferenceGroup_idAuthorReferenceGroup>=0),\n"
              + "  Author_idAuthor INTEGER UNIQUE CHECK(Author_idAuthor>=0),\n"
              + "  CONSTRAINT fk_AuthorReference_AuthorReferenceGroup\n"
              + "     FOREIGN KEY(AuthorReferenceGroup_idAuthorReferenceGroup)\n"
              + "     REFERENCES AuthorReferenceGroup(idAuthorReferenceGroup)\n"
              + "     ON DELETE SET NULL\n"
              + "     ON UPDATE CASCADE,\n"
              + "  CONSTRAINT fk_AuthorReference_Author\n"
              + "     FOREIGN KEY(Author_idAuthor)\n"
              + "     REFERENCES Author(idAuthor)\n"
              + "     ON DELETE SET NULL\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE Journal(\n"
              + "  idJournal INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idJournal>=0),\n"
              + "  source VARCHAR NOT NULL UNIQUE,\n"
              + "  conferenceInformation VARCHAR,\n"
              + "  journal_documentsCount INTEGER CHECK(journal_documentsCount>=0) DEFAULT 0\n"
              + ");");

      stat.addBatch("CREATE TABLE Period(\n"
              + "  idPeriod INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idPeriod>=0),\n"
              + "  name VARCHAR NOT NULL UNIQUE,\n"
              + "  position INTEGER CHECK(position > 0) DEFAULT 0,\n"
              + "  period_documentsCount INTEGER CHECK(period_documentsCount>=-1) DEFAULT 0,\n"
              + "  period_publishDatesCount INTEGER CHECK(period_publishDatesCount>=0) DEFAULT 0\n"
              + ");");

      stat.addBatch("CREATE TABLE PublishDate(\n"
              + "  idPublishDate INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idPublishDate>=0),\n"
              + "  year VARCHAR NOT NULL,\n"
              + "  date VARCHAR NOT NULL,\n"
              + "  publishDate_documentsCount INTEGER CHECK(publishDate_documentsCount>=0) DEFAULT 0,\n"
              + "  CONSTRAINT publishDate_UNIQUE\n"
              + "     UNIQUE(year,date)\n"
              + ");");

      stat.addBatch("CREATE TABLE ReferenceSource(\n"
              + "  idReferenceSource INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idReferenceSource>=0),\n"
              + "  source VARCHAR NOT NULL UNIQUE,\n"
              + "  referenceSource_referencesCount INTEGER CHECK(referenceSource_referencesCount>=0) DEFAULT 0,\n"
              + "  referenceSource_documentsCount INTEGER CHECK(referenceSource_documentsCount>=0) DEFAULT 0,\n"
              + "  ReferenceSourceGroup_idReferencesourceGroup INTEGER CHECK(ReferenceSourceGroup_idReferenceSourceGroup>=0),\n"
              + "  CONSTRAINT fk_Reference_ReferenceSourceGroup\n"
              + "     FOREIGN KEY(ReferenceSourceGroup_idReferencesourceGroup)\n"
              + "     REFERENCES ReferenceSourceGroup(idReferenceSourceGroup)\n"
              + "     ON DELETE SET NULL\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE Reference(\n"
              + "  idReference INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idReference>=0),\n"
              + "  fullReference VARCHAR NOT NULL UNIQUE,\n"
              + "  ReferenceSource_idReferenceSource INTEGER,\n"
              + "  volume VARCHAR,\n"
              + "  issue VARCHAR,\n"
              + "  page VARCHAR,\n"
              + "  doi VARCHAR,\n"
              + "  format VARCHAR,\n"
              + "  year VARCHAR,\n"
              + "  reference_documentsCount INTEGER CHECK(reference_documentsCount>=0) DEFAULT 0,\n"
              + "  ReferenceGroup_idReferenceGroup INTEGER CHECK(ReferenceGroup_idReferenceGroup>=0),\n"
              + "  CONSTRAINT fk_Reference_ReferenceSource\n"
              + "     FOREIGN KEY(ReferenceSource_idReferenceSource)\n"
              + "     REFERENCES ReferenceSource(idReferenceSource)\n"
              + "     ON DELETE SET NULL\n"
              + "     ON UPDATE CASCADE,\n"
              + "  CONSTRAINT fk_Reference_ReferenceGroup\n"
              + "     FOREIGN KEY(ReferenceGroup_idReferenceGroup)\n"
              + "     REFERENCES ReferenceGroup(idReferenceGroup)\n"
              + "     ON DELETE SET NULL\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE SubjectCategory(\n"
              + "  idSubjectCategory INTEGER PRIMARY KEY NOT NULL CHECK(idSubjectCategory>=0),\n"
              + "  subjectCategoryName VARCHAR NOT NULL UNIQUE,\n"
              + "  subjectCategory_documentsCount INTEGER CHECK(subjectCategory_documentsCount>=0) DEFAULT 0,\n"
              + "  subjectCategory_journalsCount INTEGER CHECK(subjectCategory_journalsCount>=0) DEFAULT 0"
              + ");");

      stat.addBatch("CREATE TABLE Word(\n"
              + "  idWord INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idWord>=0),\n"
              + "  wordName VARCHAR NOT NULL UNIQUE,\n"
              + "  WordGroup_idWordGroup INTEGER CHECK(WordGroup_idWordGroup>=0),\n"
              + "  word_documentsCount INTEGER CHECK(word_documentsCount>=0) DEFAULT 0,\n"
              + "  roleAuthorCount INTEGER CHECK(roleAuthorCount>=0) DEFAULT 0,\n"
              + "  roleSourceCount INTEGER CHECK(roleSourceCount>=0) DEFAULT 0,\n"
              + "  roleAddedCount INTEGER CHECK(roleAddedCount>=0) DEFAULT 0,\n"
              + "  CONSTRAINT fk_Word_WordGroup\n"
              + "     FOREIGN KEY(WordGroup_idWordGroup)\n"
              + "     REFERENCES WordGroup(idWordGroup)\n"
              + "     ON DELETE SET NULL\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE Document(\n"
              + "  idDocument INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK(idDocument>=0),\n"
              + "  title VARCHAR NOT NULL,\n"
              + "  type VARCHAR,\n"
              + "  docAbstract VARCHAR,\n"
              + "  volume VARCHAR,\n"
              + "  issue VARCHAR,\n"
              + "  beginPage VARCHAR,\n"
              + "  endPage VARCHAR,\n"
              + "  citationsCount INTEGER CHECK(citationsCount>=0) DEFAULT 0,\n"
              + "  doi VARCHAR,\n"
              + "  sourceIdentifier VARCHAR,\n"
              + "  document_wordsCount INTEGER CHECK(document_wordsCount>=0) DEFAULT 0,\n"
              + "  authorWordsCount INTEGER CHECK(authorWordsCount>=0) DEFAULT 0,\n"
              + "  sourceWordsCount INTEGER CHECK(sourceWordsCount>=0) DEFAULT 0,\n"
              + "  addedWordsCount INTEGER CHECK(addedWordsCount>=0) DEFAULT 0,\n"
              + "  document_affiliationsCount INTEGER CHECK(document_affiliationsCount>=0) DEFAULT 0,\n"
              + "  document_referencesCount INTEGER CHECK(document_referencesCount>=0) DEFAULT 0,\n"
              + "  year VARCHAR DEFAULT NULL,\n"
              + "  authors VARCHAR DEFAULT NULL,\n"
              + "  PublishDate_idPublishDate INTEGER CHECK(PublishDate_idPublishDate>=0),\n"
              + "  Journal_idJournal INTEGER CHECK(Journal_idJournal>=0),\n"
              + "  CONSTRAINT fk_Document_PublishDate\n"
              + "     FOREIGN KEY(PublishDate_idPublishDate)\n"
              + "     REFERENCES PublishDate(idPublishDate)\n"
              + "     ON DELETE SET NULL\n"
              + "     ON UPDATE CASCADE,\n"
              + "  CONSTRAINT fk_Document_Journal\n"
              + "     FOREIGN KEY(Journal_idJournal)\n"
              + "     REFERENCES Journal(idJournal)\n"
              + "     ON DELETE SET NULL\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE PublishDate_Period(\n"
              + "  PublishDate_idPublishDate INTEGER NOT NULL CHECK(PublishDate_idPublishDate>=0),\n"
              + "  Period_idPeriod INTEGER NOT NULL CHECK(Period_idPeriod>=0),\n"
              + "  PRIMARY KEY(PublishDate_idPublishDate,Period_idPeriod),\n"
              + "  CONSTRAINT fk_PublishDatePeriod_PublishDate\n"
              + "     FOREIGN KEY(PublishDate_idPublishDate)\n"
              + "     REFERENCES PublishDate(idPublishDate)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE,\n"
              + "  CONSTRAINT fk_PublishDatePeriod_Period\n"
              + "     FOREIGN KEY(Period_idPeriod)\n"
              + "     REFERENCES Period(idPeriod)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE Journal_SubjectCategory_PublishDate(\n"
              + "  Journal_idJournal INTEGER NOT NULL CHECK(Journal_idJournal>=0),\n"
              + "  SubjectCategory_idSubjectCategory INTEGER NOT NULL CHECK(SubjectCategory_idSubjectCategory>=0),\n"
              + "  PublishDate_idPublishDate INTEGER NOT NULL CHECK(PublishDate_idPublishDate>=0),\n"
              + "  PRIMARY KEY(Journal_idJournal,SubjectCategory_idSubjectCategory,PublishDate_idPublishDate),\n"
              + "  CONSTRAINT fk_JournalSubjectCategoryPublishDate_Journal\n"
              + "     FOREIGN KEY(Journal_idJournal)\n"
              + "     REFERENCES Journal(idJournal)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE,\n"
              + "  CONSTRAINT fk_JournalSubjectCategoryPublishDate_SubjectCategory\n"
              + "     FOREIGN KEY(SubjectCategory_idSubjectCategory)\n"
              + "     REFERENCES SubjectCategory(idSubjectCategory)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE,\n"
              + "  CONSTRAINT fk_JournalSubjectCategoryPublishDate_PublishDate\n"
              + "     FOREIGN KEY(PublishDate_idPublishDate)\n"
              + "     REFERENCES PublishDate(idPublishDate)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE Document_Author(\n"
              + "  Author_idAuthor INTEGER NOT NULL CHECK(Author_idAuthor>=0),\n"
              + "  Document_idDocument INTEGER NOT NULL CHECK(Document_idDocument>=0),\n"
              + "  position INTEGER CHECK(position >= 1),\n"
              + "  PRIMARY KEY(Author_idAuthor,Document_idDocument),\n"
              + "  CONSTRAINT fk_DocumentAuthor_Author\n"
              + "     FOREIGN KEY(Author_idAuthor)\n"
              + "     REFERENCES Author(idAuthor)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE,\n"
              + "  CONSTRAINT fk_DocumentAuthor_Document\n"
              + "     FOREIGN KEY(Document_idDocument)\n"
              + "     REFERENCES Document(idDocument)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE Document_Affiliation(\n"
              + "  Affiliation_idAffiliation INTEGER NOT NULL CHECK(Affiliation_idAffiliation>=0),\n"
              + "  Document_idDocument INTEGER NOT NULL CHECK(Document_idDocument>=0),\n"
              + "  PRIMARY KEY(Affiliation_idAffiliation,Document_idDocument),\n"
              + "  CONSTRAINT fk_DocumentAffiliation_Affiliation\n"
              + "     FOREIGN KEY(Affiliation_idAffiliation)\n"
              + "     REFERENCES Affiliation(idAffiliation)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE,\n"
              + "  CONSTRAINT fk_DocumentAffiliation_Document\n"
              + "     FOREIGN KEY(Document_idDocument)\n"
              + "     REFERENCES Document(idDocument)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE Document_Word(\n"
              + "  Document_idDocument INTEGER NOT NULL CHECK(Document_idDocument>=0),\n"
              + "  Word_idWord INTEGER NOT NULL CHECK(Word_idWord>=0),\n"
              + "  isAuthorWord BOOLEAN DEFAULT FALSE,\n"
              + "  isSourceWord BOOLEAN DEFAULT FALSE,\n"
              + "  isAddedWord BOOLEAN DEFAULT FALSE,\n"
              + "  PRIMARY KEY(Document_idDocument,Word_idWord),\n"
              + "  CONSTRAINT fk_DocumentWord_Document\n"
              + "     FOREIGN KEY(Document_idDocument)\n"
              + "     REFERENCES Document(idDocument)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE,\n"
              + "  CONSTRAINT fk_DocumentWord_Word\n"
              + "     FOREIGN KEY(Word_idWord)\n"
              + "     REFERENCES Word(idWord)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE Document_Reference(\n"
              + "  Reference_idReference INTEGER NOT NULL CHECK(Reference_idReference>=0),\n"
              + "  Document_idDocument INTEGER NOT NULL CHECK(Document_idDocument>=0),\n"
              + "  PRIMARY KEY(Reference_idReference,Document_idDocument),\n"
              + "  CONSTRAINT fk_DocumentReference_Reference\n"
              + "     FOREIGN KEY(Reference_idReference)\n"
              + "     REFERENCES Reference(idReference)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE,\n"
              + "  CONSTRAINT fk_DocumentReference_Document\n"
              + "     FOREIGN KEY(Document_idDocument)\n"
              + "     REFERENCES Document(idDocument)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE AuthorReference_Reference(\n"
              + "  AuthorReference_idAuthorReference INTEGER NOT NULL CHECK(AuthorReference_idAuthorReference>=0),\n"
              + "  Reference_idReference INTEGER NOT NULL CHECK(Reference_idReference>=0),\n"
              + "  position INTEGER CHECK(position >= 1),\n"
              + "  PRIMARY KEY(AuthorReference_idAuthorReference,Reference_idReference),\n"
              + "  CONSTRAINT fk_AuthorReference_Reference_AuthorReference\n"
              + "     FOREIGN KEY(AuthorReference_idAuthorReference)\n"
              + "     REFERENCES AuthorReference(idAuthorReference)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE,\n"
              + "  CONSTRAINT fk_AuthorReference_Reference_Reference\n"
              + "     FOREIGN KEY(Reference_idReference)\n"
              + "     REFERENCES Reference(idReference)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE TABLE Author_Affiliation(\n"
              + "  Affiliation_idAffiliation INTEGER NOT NULL CHECK(Affiliation_idAffiliation>=0),\n"
              + "  Author_idAuthor INTEGER NOT NULL CHECK(Author_idAuthor>=0),\n"
              + "  PRIMARY KEY(Affiliation_idAffiliation,Author_idAuthor),\n"
              + "  CONSTRAINT fk_AffiliationAuthor_Affiliation\n"
              + "     FOREIGN KEY(Affiliation_idAffiliation)\n"
              + "     REFERENCES Affiliation(idAffiliation)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE,\n"
              + "  CONSTRAINT fk_AffiliationAuthor_Author\n"
              + "     FOREIGN KEY(Author_idAuthor)\n"
              + "     REFERENCES Author(idAuthor)\n"
              + "     ON DELETE CASCADE\n"
              + "     ON UPDATE CASCADE\n"
              + ");");

      stat.addBatch("CREATE INDEX fk_PublishDatePeriod_Period ON PublishDate_Period(Period_idPeriod);");
      stat.addBatch("CREATE INDEX fk_PublishDatePeriod_PublishDate ON PublishDate_Period(PublishDate_idPublishDate);");
      stat.addBatch("CREATE UNIQUE INDEX fk_PublishDatePeriod ON PublishDate_Period(Period_idPeriod, PublishDate_idPublishDate);");

      stat.addBatch("CREATE INDEX fk_JournalSubjectCategoryPublishDate_SubjectCategory ON Journal_SubjectCategory_PublishDate(SubjectCategory_idSubjectCategory);");
      stat.addBatch("CREATE INDEX fk_JournalSubjectCategoryPublishDate_Journal ON Journal_SubjectCategory_PublishDate(Journal_idJournal);");
      stat.addBatch("CREATE INDEX fk_JournalSubjectCategoryPublishDate_PublishDate ON Journal_SubjectCategory_PublishDate(PublishDate_idPublishDate);");
      stat.addBatch("CREATE UNIQUE INDEX fk_JournalSubjectCategoryPublishDate ON Journal_SubjectCategory_PublishDate(SubjectCategory_idSubjectCategory, PublishDate_idPublishDate, Journal_idJournal);");
      stat.addBatch("CREATE INDEX fk_JournalSubjectCategoryPublishDate_partial1 ON Journal_SubjectCategory_PublishDate(Journal_idJournal, SubjectCategory_idSubjectCategory);");
      stat.addBatch("CREATE INDEX fk_JournalSubjectCategoryPublishDate_partial2 ON Journal_SubjectCategory_PublishDate(SubjectCategory_idSubjectCategory, PublishDate_idPublishDate);");
      stat.addBatch("CREATE INDEX fk_JournalSubjectCategoryPublishDate_partial3 ON Journal_SubjectCategory_PublishDate(PublishDate_idPublishDate, Journal_idJournal);");

      stat.addBatch("CREATE INDEX fk_DocumentAuthor_Document ON Document_Author(Document_idDocument);");
      stat.addBatch("CREATE INDEX fk_DocumentAuthor_Author ON Document_Author(Author_idAuthor);");
      stat.addBatch("CREATE UNIQUE INDEX fk_DocumentAuthor ON Document_Author(Document_idDocument, Author_idAuthor);");

      stat.addBatch("CREATE INDEX fk_DocumentAffiliation_Affiliation ON Document_Affiliation(Affiliation_idAffiliation);");
      stat.addBatch("CREATE INDEX fk_DocumentAffiliation_Document ON Document_Affiliation(Document_idDocument);");
      stat.addBatch("CREATE UNIQUE INDEX fk_DocumentAffiliation ON Document_Affiliation(Affiliation_idAffiliation, Document_idDocument);");

      stat.addBatch("CREATE INDEX fk_DocumentWord_Word ON Document_Word(Word_idWord);");
      stat.addBatch("CREATE INDEX fk_DocumentWord_Document ON Document_Word(Document_idDocument);");
      stat.addBatch("CREATE UNIQUE INDEX fk_DocumentWord ON Document_Word(Document_idDocument, Word_idWord);");

      stat.addBatch("CREATE INDEX fk_ReferenceDocument_Document ON Document_Reference(Document_idDocument);");
      stat.addBatch("CREATE INDEX fk_ReferenceDocument_Reference ON Document_Reference(Reference_idReference);");
      stat.addBatch("CREATE UNIQUE INDEX fk_ReferenceDocument ON Document_Reference(Document_idDocument, Reference_idReference);");

      stat.addBatch("CREATE INDEX fk_AuthorReference_Reference_Reference ON AuthorReference_Reference(Reference_idReference);");
      stat.addBatch("CREATE INDEX fk_AuthorReference_Reference_AuthorReference ON AuthorReference_Reference(AuthorReference_idAuthorReference);");
      stat.addBatch("CREATE UNIQUE INDEX fk_AuthorReference_Reference ON AuthorReference_Reference(Reference_idReference, AuthorReference_idAuthorReference);");

      stat.addBatch("CREATE INDEX fk_AffiliationAuthor_Affiliation ON Author_Affiliation(Affiliation_idAffiliation);");
      stat.addBatch("CREATE INDEX fk_AffiliationAuthor_Author ON Author_Affiliation(Author_idAuthor);");
      stat.addBatch("CREATE UNIQUE INDEX fk_AffiliationAuthor ON Author_Affiliation(Affiliation_idAffiliation, Author_idAuthor);");

      stat.addBatch("CREATE INDEX fk_Document_PublishDate ON Document(PublishDate_idPublishDate);");

      stat.addBatch("CREATE INDEX fk_Document_Journal ON Document(Journal_idJournal);");
      stat.addBatch("CREATE INDEX fk_Document_Journal_full ON Document(idDocument, Journal_idJournal);");

      stat.addBatch("CREATE UNIQUE INDEX fk_AuthorReference_Author ON AuthorReference(Author_idAuthor);");

      stat.addBatch("CREATE INDEX fk_Reference_ReferenceSource ON Reference(ReferenceSource_idReferenceSource);");

      stat.addBatch("CREATE INDEX fk_AuthorReference_AuthorReferenceGroup ON AuthorReference(AuthorReferenceGroup_idAuthorReferenceGroup);");

      stat.addBatch("CREATE INDEX fk_Reference_ReferenceGroup ON Reference(ReferenceGroup_idReferenceGroup);");

      stat.addBatch("CREATE INDEX fk_ReferenceSource_ReferenceSourceGroup ON ReferenceSource(ReferenceSourceGroup_idReferenceSourceGroup);");

      stat.addBatch("CREATE INDEX fk_Author_AuthorGroup ON Author(AuthorGroup_idAuthorGroup);");

      stat.addBatch("CREATE INDEX fk_Word_WordGroup ON Word(WordGroup_idWordGroup);");
      
      /*stat.addBatch("CREATE UNIQUE INDEX fk_Document_PublishDate ON Document(idDocument, PublishDate_idPublishDate);");

      stat.addBatch("CREATE UNIQUE INDEX fk_Document_Journal ON Document(idDocument, Journal_idJournal);");

      stat.addBatch("CREATE UNIQUE INDEX fk_AuthorReference_Author ON AuthorReference(idAuthorReference, Author_idAuthor);");

      stat.addBatch("CREATE UNIQUE INDEX fk_Reference_ReferenceSource ON Reference(idReference, ReferenceSource_idReferenceSource);");

      stat.addBatch("CREATE UNIQUE INDEX fk_AuthorReference_AuthorReferenceGroup ON AuthorReference(idAuthorReference, AuthorReferenceGroup_idAuthorReferenceGroup);");

      stat.addBatch("CREATE UNIQUE INDEX fk_Reference_ReferenceGroup ON Reference(idReference, ReferenceGroup_idReferenceGroup);");

      stat.addBatch("CREATE UNIQUE INDEX fk_ReferenceSource_ReferenceSourceGroup ON ReferenceSource(idReferenceSource, ReferenceSourceGroup_idReferenceSourceGroup);");

      stat.addBatch("CREATE UNIQUE INDEX fk_Author_AuthorGroup ON Author(idAuthor, AuthorGroup_idAuthorGroup);");

      stat.addBatch("CREATE UNIQUE INDEX fk_Word_WordGroup ON Word(idWord, WordGroup_idWordGroup);");*/

      stat.addBatch("CREATE UNIQUE INDEX idx_Affiliation_pk ON Affiliation(idAffiliation);");

      stat.addBatch("CREATE UNIQUE INDEX idx_Author_pk ON Author(idAuthor);");

      stat.addBatch("CREATE UNIQUE INDEX idx_AuthorGroup_pk ON AuthorGroup(idAuthorGroup);");

      stat.addBatch("CREATE UNIQUE INDEX idx_AuthorReference_pk ON AuthorReference(idAuthorReference);");

      stat.addBatch("CREATE UNIQUE INDEX idx_AuthorReferenceGroup_pk ON AuthorReferenceGroup(idAuthorReferenceGroup);");

      stat.addBatch("CREATE UNIQUE INDEX idx_Document_pk ON Document(idDocument);");

      stat.addBatch("CREATE UNIQUE INDEX idx_Journal_pk ON Journal(idJournal);");

      stat.addBatch("CREATE UNIQUE INDEX idx_Period_pk ON Period(idPeriod);");

      stat.addBatch("CREATE UNIQUE INDEX idx_PublishDate_pk ON PublishDate(idPublishDate);");

      stat.addBatch("CREATE UNIQUE INDEX idx_Reference_pk ON Reference(idReference);");

      stat.addBatch("CREATE UNIQUE INDEX idx_ReferenceGroup_pk ON ReferenceGroup(idReferenceGroup);");

      stat.addBatch("CREATE UNIQUE INDEX idx_ReferenceSource_pk ON ReferenceSource(idReferenceSource);");

      stat.addBatch("CREATE UNIQUE INDEX idx_ReferenceSourceGroup_pk ON ReferenceSourceGroup(idReferenceSourceGroup);");

      stat.addBatch("CREATE UNIQUE INDEX idx_SubjectCategory_pk ON SubjectCategory(idSubjectCategory);");

      stat.addBatch("CREATE UNIQUE INDEX idx_Word_pk ON Word(idWord);");

      stat.addBatch("CREATE UNIQUE INDEX idx_WordGroup_pk ON WordGroup(idWordGroup);");
      
      
      
      
      
      /*stat.addBatch("CREATE UNIQUE INDEX idx_Affiliation_unique ON Affiliation(fullAffiliation);");

      stat.addBatch("CREATE UNIQUE INDEX idx_Author_unique ON Author(authorName, fullAuthorName);");

      stat.addBatch("CREATE UNIQUE INDEX idx_AuthorGroup_unique ON AuthorGroup(groupName);");

      stat.addBatch("CREATE UNIQUE INDEX idx_AuthorReference_unique ON AuthorReference(authorName);");

      stat.addBatch("CREATE UNIQUE INDEX idx_AuthorReferenceGroup_unique ON AuthorReferenceGroup(groupName);");

      stat.addBatch("CREATE UNIQUE INDEX idx_Journal_unique ON Journal(source);");

      stat.addBatch("CREATE UNIQUE INDEX idx_Period_unique ON Period(name);");

      stat.addBatch("CREATE UNIQUE INDEX idx_PublishDate_unique ON PublishDate(year, date);");

      stat.addBatch("CREATE UNIQUE INDEX idx_Reference_unique ON Reference(fullReference);");

      stat.addBatch("CREATE UNIQUE INDEX idx_ReferenceGroup_unique ON ReferenceGroup(groupName);");

      stat.addBatch("CREATE UNIQUE INDEX idx_ReferenceSource_unique ON ReferenceSource(source);");

      stat.addBatch("CREATE UNIQUE INDEX idx_ReferenceSourceGroup_unique ON ReferenceSourceGroup(groupName);");

      stat.addBatch("CREATE UNIQUE INDEX idx_SubjectCategory_unique ON SubjectCategory(subjectCategoryName);");

      stat.addBatch("CREATE UNIQUE INDEX idx_Word_unique ON Word(wordName);");

      stat.addBatch("CREATE UNIQUE INDEX idx_WordGroup_unique ON WordGroup(groupName);");*/

      stat.addBatch("CREATE TRIGGER aggregateData_delete_Document_Word AFTER DELETE ON Document_Word FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Word \n"
              + "      SET word_documentsCount = word_documentsCount - 1,\n"
              + "          roleAuthorCount = CASE old.isAuthorWord WHEN 1 THEN roleAuthorCount - 1 ELSE roleAuthorCount END,\n"
              + "          roleSourceCount = CASE old.isSourceWord WHEN 1 THEN roleSourceCount - 1 ELSE roleSourceCount END,\n"
              + "          roleAddedCount = CASE old.isAddedWord WHEN 1 THEN roleAddedCount - 1 ELSE roleAddedCount END\n"
              + "      WHERE idWord = old.Word_idWord;\n"
              + "    UPDATE Document \n"
              + "      SET document_wordsCount = document_wordsCount - 1,\n"
              + "          authorWordsCount = CASE old.isAuthorWord WHEN 1 THEN authorWordsCount - 1 ELSE authorWordsCount END,\n"
              + "          sourceWordsCount = CASE old.isSourceWord WHEN 1 THEN sourceWordsCount - 1 ELSE sourceWordsCount END,\n"
              + "          addedWordsCount = CASE old.isAddedWord WHEN 1 THEN addedWordsCount - 1 ELSE addedWordsCount END \n"
              + "      WHERE idDocument = old.Document_idDocument;\n"
              + "    UPDATE WordGroup \n"
              + "      SET wordGroup_documentsCount = (SELECT COUNT(DISTINCT dw.Document_idDocument) \n"
              + "                                      FROM WordGroup wg, Word w, Document_Word dw\n"
              + "                                      WHERE wg.idWordGroup = WordGroup.idWordGroup AND\n"
              + "                                            wg.idWordGroup = w.WordGroup_idWordGroup AND\n"
              + "                                            w.idWord = dw.Word_idWord)\n"
              + "      WHERE idWordGroup = (SELECT WordGroup_idWordGroup \n"
              + "                           FROM Word \n"
              + "                           WHERE idWord = old.Word_idWord);\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_insert_Document_Word AFTER INSERT ON Document_Word FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Word \n"
              + "      SET word_documentsCount = word_documentsCount + 1,\n"
              + "          roleAuthorCount = CASE new.isAuthorWord WHEN 1 THEN roleAuthorCount + 1 ELSE roleAuthorCount END,\n"
              + "          roleSourceCount = CASE new.isSourceWord WHEN 1 THEN roleSourceCount + 1 ELSE roleSourceCount END,\n"
              + "          roleAddedCount = CASE new.isAddedWord WHEN 1 THEN roleAddedCount + 1 ELSE roleAddedCount END\n"
              + "      WHERE idWord = new.Word_idWord;\n"
              + "    UPDATE Document \n"
              + "      SET document_wordsCount = document_wordsCount + 1,\n"
              + "          authorWordsCount = CASE new.isAuthorWord WHEN 1 THEN authorWordsCount + 1 ELSE authorWordsCount END,\n"
              + "          sourceWordsCount = CASE new.isSourceWord WHEN 1 THEN sourceWordsCount + 1 ELSE sourceWordsCount END,\n"
              + "          addedWordsCount = CASE new.isAddedWord WHEN 1 THEN addedWordsCount + 1 ELSE addedWordsCount END \n"
              + "      WHERE idDocument = new.Document_idDocument;\n"
              + "    UPDATE WordGroup \n"
              + "      SET wordGroup_documentsCount = (SELECT COUNT(DISTINCT dw.Document_idDocument) \n"
              + "                                      FROM WordGroup wg, Word w, Document_Word dw\n"
              + "                                      WHERE wg.idWordGroup = WordGroup.idWordGroup AND\n"
              + "                                            wg.idWordGroup = w.WordGroup_idWordGroup AND\n"
              + "                                            w.idWord = dw.Word_idWord)\n"
              + "      WHERE idWordGroup = (SELECT WordGroup_idWordGroup \n"
              + "                           FROM Word \n"
              + "                           WHERE idWord = new.Word_idWord);\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_update_Document_Word_isAuthorWord AFTER UPDATE OF isAuthorWord ON Document_Word FOR EACH ROW\n"
              + "  WHEN new.isAuthorWord != old.isAuthorWord\n"
              + "  BEGIN\n"
              + "    UPDATE Word\n"
              + "      SET roleAuthorCount = CASE new.isAuthorWord WHEN 1 THEN roleAuthorCount + 1 ELSE roleAuthorCount - 1 END\n"
              + "      WHERE idWord = old.Word_idWord;\n"
              + "    UPDATE Document\n"
              + "      SET authorWordsCount = CASE new.isAuthorWord WHEN 1 THEN authorWordsCount + 1 ELSE authorWordsCount - 1 END\n"
              + "      WHERE idDocument = old.Document_idDocument;\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_update_Document_Word_isSourceWord AFTER UPDATE OF isSourceWord ON Document_Word FOR EACH ROW\n"
              + "  WHEN new.isSourceWord != old.isSourceWord\n"
              + "  BEGIN\n"
              + "    UPDATE Word\n"
              + "      SET roleSourceCount = CASE new.isSourceWord WHEN 1 THEN roleSourceCount + 1 ELSE roleSourceCount - 1 END\n"
              + "      WHERE idWord = old.Word_idWord;\n"
              + "    UPDATE Document\n"
              + "      SET sourceWordsCount = CASE new.isSourceWord WHEN 1 THEN sourceWordsCount + 1 ELSE sourceWordsCount - 1 END\n"
              + "      WHERE idDocument = old.Document_idDocument;\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_update_Document_Word_isAddedWord AFTER UPDATE OF isAddedWord ON Document_Word FOR EACH ROW\n"
              + "  WHEN new.isAddedWord != old.isAddedWord\n"
              + "  BEGIN\n"
              + "    UPDATE Word\n"
              + "      SET roleAddedCount = CASE new.isAddedWord WHEN 1 THEN roleAddedCount + 1 ELSE roleAddedCount - 1 END\n"
              + "      WHERE idWord = old.Word_idWord;\n"
              + "    UPDATE Document\n"
              + "      SET addedWordsCount = CASE new.isAddedWord WHEN 1 THEN addedWordsCount + 1 ELSE addedWordsCount - 1 END\n"
              + "      WHERE idDocument = old.Document_idDocument;\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_update_Author_AuthorGroup_idAuthorGroup AFTER UPDATE OF AuthorGroup_idAuthorGroup ON Author FOR EACH ROW\n"
              + "  WHEN new.AuthorGroup_idAuthorGroup IS NOT old.AuthorGroup_idAuthorGroup\n"
              + "  BEGIN\n"
              + "    UPDATE AuthorGroup\n"
              + "      SET itemsCount = itemsCount - 1\n"
              + "      WHERE idAuthorGroup IN (old.AuthorGroup_idAuthorGroup);\n"
              + "    UPDATE AuthorGroup\n"
              + "      SET itemsCount = itemsCount + 1\n"
              + "      WHERE idAuthorGroup IN (new.AuthorGroup_idAuthorGroup);\n"
              + "    UPDATE AuthorGroup\n"
              + "      SET authorGroup_documentsCount = (SELECT COUNT(DISTINCT da.Document_idDocument) \n"
              + "                                        FROM AuthorGroup ag, Author a, Document_Author da \n"
              + "                                        WHERE ag.idAuthorGroup = AuthorGroup.idAuthorGroup AND\n"
              + "                                              ag.idAuthorGroup = a.AuthorGroup_idAuthorGroup AND\n"
              + "                                              a.idAuthor = da.Author_idAuthor)\n"
              + "      WHERE idAuthorGroup IN (new.AuthorGroup_idAuthorGroup, old.AuthorGroup_idAuthorGroup);\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_update_AuthorReference_AuthorReferenceGroup_idAuthorReferenceGroup AFTER UPDATE OF AuthorReferenceGroup_idAuthorReferenceGroup ON AuthorReference FOR EACH ROW\n"
              + "  WHEN new.AuthorReferenceGroup_idAuthorReferenceGroup IS NOT old.AuthorReferenceGroup_idAuthorReferenceGroup\n"
              + "  BEGIN\n"
              + "    UPDATE AuthorReferenceGroup\n"
              + "      SET itemsCount = itemsCount - 1\n"
              + "      WHERE idAuthorReferenceGroup IN (old.AuthorReferenceGroup_idAuthorReferenceGroup);\n"
              + "    UPDATE AuthorReferenceGroup\n"
              + "      SET itemsCount = itemsCount + 1\n"
              + "      WHERE idAuthorReferenceGroup IN (new.AuthorReferenceGroup_idAuthorReferenceGroup);\n"
              + "    UPDATE AuthorReferenceGroup\n"
              + "      SET authorReferenceGroup_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                                 FROM AuthorReferenceGroup arg, AuthorReference ar, AuthorReference_Reference arr, Reference r, Document_Reference dr \n"
              + "                                                 WHERE arg.idAuthorReferenceGroup = AuthorReferenceGroup.idAuthorReferenceGroup AND\n"
              + "                                                       arg.idAuthorReferenceGroup = ar.AuthorReferenceGroup_idAuthorReferenceGroup AND\n"
              + "                                                       ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND\n"
              + "                                                       arr.Reference_idReference = r.idReference AND\n"
              + "                                                       r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idAuthorReferenceGroup IN (new.AuthorReferenceGroup_idAuthorReferenceGroup, old.AuthorReferenceGroup_idAuthorReferenceGroup);\n"
              + "  END;");
      
      stat.addBatch("CREATE TRIGGER aggregateData_update_Reference_ReferenceGroup_idReferenceGroup AFTER UPDATE OF ReferenceGroup_idReferenceGroup ON Reference FOR EACH ROW\n"
              + "  WHEN new.ReferenceGroup_idReferenceGroup IS NOT old.ReferenceGroup_idReferenceGroup\n"
              + "  BEGIN\n"
              + "    UPDATE ReferenceGroup\n"
              + "      SET itemsCount = itemsCount - 1\n"
              + "      WHERE idReferenceGroup IN (old.ReferenceGroup_idReferenceGroup);\n"
              + "    UPDATE ReferenceGroup\n"
              + "      SET itemsCount = itemsCount + 1\n"
              + "      WHERE idReferenceGroup IN (new.ReferenceGroup_idReferenceGroup);\n"
              + "    UPDATE ReferenceGroup\n"
              + "      SET referenceGroup_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                           FROM ReferenceGroup rg, Reference r, Document_Reference dr\n"
              + "                                           WHERE rg.idReferenceGroup = ReferenceGroup.idReferenceGroup AND\n"
              + "                                                 rg.idReferenceGroup = r.ReferenceGroup_idReferenceGroup AND\n"
              + "                                                 r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceGroup IN (new.ReferenceGroup_idReferenceGroup, old.ReferenceGroup_idReferenceGroup);\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_update_ReferenceSource_ReferenceSourceGroup_idReferenceSourceGroup AFTER UPDATE OF ReferenceSourceGroup_idReferenceSourceGroup ON ReferenceSource FOR EACH ROW\n"
              + "  WHEN new.ReferenceSourceGroup_idReferenceSourceGroup IS NOT old.ReferenceSourceGroup_idReferenceSourceGroup\n"
              + "  BEGIN\n"
              + "    UPDATE ReferenceSourceGroup\n"
              + "      SET itemsCount = itemsCount - 1\n"
              + "      WHERE idReferenceSourceGroup IN (old.ReferenceSourceGroup_idReferenceSourceGroup);\n"
              + "    UPDATE ReferenceSourceGroup\n"
              + "      SET itemsCount = itemsCount + 1\n"
              + "      WHERE idReferenceSourceGroup IN (new.ReferenceSourceGroup_idReferenceSourceGroup);\n"
              + "    UPDATE ReferenceSourceGroup\n"
              + "      SET referenceSourceGroup_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                                 FROM ReferenceSourceGroup rsg, ReferenceSource rs, Reference r, Document_Reference dr\n"
              + "                                                 WHERE rsg.idReferenceSourceGroup = ReferenceSourceGroup.idReferenceSourceGroup AND\n"
              + "                                                       rsg.idReferenceSourceGroup = rs.ReferenceSourceGroup_idReferenceSourceGroup AND\n"
              + "                                                       rs.idReferenceSource = r.ReferenceSource_idReferenceSource AND\n"
              + "                                                       r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceSourceGroup IN (ReferenceSourceGroup.idReferenceSourceGroup);\n"
              + "  END;");
      
      stat.addBatch("CREATE TRIGGER aggregateData_update_Word_WordGroup_idWordGroup AFTER UPDATE OF WordGroup_idWordGroup ON Word FOR EACH ROW\n"
              + "  WHEN new.WordGroup_idWordGroup IS NOT old.WordGroup_idWordGroup\n"
              + "  BEGIN\n"
              + "    UPDATE WordGroup\n"
              + "      SET itemsCount = itemsCount - 1\n"
              + "      WHERE idWordGroup IN (old.WordGroup_idWordGroup);\n"
              + "    UPDATE WordGroup\n"
              + "      SET itemsCount = itemsCount + 1\n"
              + "      WHERE idWordGroup IN (new.WordGroup_idWordGroup);\n"
              + "    UPDATE WordGroup\n"
              + "      SET wordGroup_documentsCount = (SELECT COUNT(DISTINCT dw.Document_idDocument) \n"
              + "                                      FROM WordGroup wg, Word w, Document_Word dw \n"
              + "                                      WHERE wg.idWordGroup = WordGroup.idWordGroup AND\n"
              + "                                            wg.idWordGroup = w.WordGroup_idWordGroup AND\n"
              + "                                            w.idWord = dw.Word_idWord)\n"
              + "      WHERE idWordGroup IN (new.WordGroup_idWordGroup, old.WordGroup_idWordGroup);\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_delete_Document_Affiliation AFTER DELETE ON Document_Affiliation FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Affiliation\n"
              + "      SET affiliation_documentsCount = affiliation_documentsCount - 1\n"
              + "      WHERE idAffiliation = old.Affiliation_idAffiliation;\n"
              + "    UPDATE Document\n"
              + "      SET document_affiliationsCount = document_affiliationsCount - 1\n"
              + "      WHERE idDocument = old.Document_idDocument;\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_insert_Document_Affiliation AFTER INSERT ON Document_Affiliation FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Affiliation\n"
              + "      SET affiliation_documentsCount = affiliation_documentsCount + 1\n"
              + "      WHERE idAffiliation = new.Affiliation_idAffiliation;\n"
              + "    UPDATE Document\n"
              + "      SET document_affiliationsCount = document_affiliationsCount + 1\n"
              + "      WHERE idDocument = new.Document_idDocument;\n"
              + "  END;");
      
      stat.addBatch("CREATE TRIGGER aggregateData_delete_Document_Author AFTER DELETE ON Document_Author FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Author\n"
              + "      SET author_documentsCount = author_documentsCount - 1\n"
              + "      WHERE idAuthor = old.Author_idAuthor;\n"
              + "    UPDATE Document\n"
              + "      SET authors = (SELECT group_concat(a.authorName, \", \")\n"
              + "                     FROM Author a, Document_Author da\n"
              + "                     WHERE da.Document_idDocument = Document.idDocument AND "
              + "                           a.idAuthor = da.Author_idAuthor\n"
              + "                     ORDER BY da.position)\n"
              + "      WHERE idDocument = old.Document_idDocument;\n"
              + "    UPDATE AuthorGroup\n"
              + "      SET authorGroup_documentsCount = (SELECT COUNT(DISTINCT da.Document_idDocument) \n"
              + "                            FROM AuthorGroup ag, Author a, Document_Author da \n"
              + "                            WHERE ag.idAuthorGroup = AuthorGroup.idAuthorGroup AND\n"
              + "                                  ag.idAuthorGroup = a.AuthorGroup_idAuthorGroup AND\n"
              + "                                  a.idAuthor = da.Author_idAuthor)\n"
              + "      WHERE idAuthorGroup = (SELECT AuthorGroup_idAuthorGroup FROM Author WHERE idAuthor = old.Author_idAuthor);\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_insert_Document_Author AFTER INSERT ON Document_Author FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Author\n"
              + "      SET author_documentsCount = author_documentsCount + 1\n"
              + "      WHERE idAuthor = new.Author_idAuthor;\n"
              + "    UPDATE Document\n"
              + "      SET authors = (SELECT group_concat(authorName, \", \")\n"
              + "                     FROM Author a, Document_Author da\n"
              + "                     WHERE da.Document_idDocument = Document.idDocument AND "
              + "                           a.idAuthor = da.Author_idAuthor\n"
              + "                     ORDER BY da.position)\n"
              + "      WHERE idDocument = new.Document_idDocument;\n"
              + "    UPDATE AuthorGroup\n"
              + "      SET authorGroup_documentsCount = (SELECT COUNT(DISTINCT da.Document_idDocument) \n"
              + "                            FROM AuthorGroup ag, Author a, Document_Author da \n"
              + "                            WHERE ag.idAuthorGroup = AuthorGroup.idAuthorGroup AND\n"
              + "                                  ag.idAuthorGroup = a.AuthorGroup_idAuthorGroup AND\n"
              + "                                  a.idAuthor = da.Author_idAuthor)\n"
              + "      WHERE idAuthorGroup = (SELECT AuthorGroup_idAuthorGroup FROM Author WHERE idAuthor = new.Author_idAuthor);\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_update_Document_Author_position AFTER UPDATE OF position ON Document_Author FOR EACH ROW\n"
              + "  WHEN new.position != old.position\n"
              + "  BEGIN\n"
              + "    UPDATE Document\n"
              + "      SET authors = (SELECT group_concat(authorName, \", \")\n"
              + "                     FROM Author a, Document_Author da\n"
              + "                     WHERE da.Document_idDocument = Document.idDocument AND "
              + "                           a.idAuthor = da.Author_idAuthor\n"
              + "                     ORDER BY da.position)\n"
              + "      WHERE idDocument = old.Document_idDocument;\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_delete_PublishDate_Period AFTER DELETE ON PublishDate_Period FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Period\n"
              + "      SET period_publishDatesCount = period_publishDatesCount - 1,\n"
              + "          period_documentsCount = (SELECT COUNT(d.idDocument)\n"
              + "                                   FROM Period p, PublishDate_Period pup, PublishDate pu, Document d\n"
              + "                                   WHERE p.idPeriod = Period.idPeriod AND\n"
              + "                                         p.idPeriod = pup.Period_idPeriod AND\n"
              + "                                         pup.PublishDate_idPublishDate = pu.idPublishDate AND\n"
              + "                                         pu.idPublishDate = d.PublishDate_idPublishDate)\n"
              + "      WHERE idPeriod = old.Period_idPeriod;\n"
              + "  END;");
      
      stat.addBatch("CREATE TRIGGER aggregateData_insert_PublishDate_Period AFTER INSERT ON PublishDate_Period FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Period\n"
              + "      SET period_publishDatesCount = period_publishDatesCount + 1,\n"
              + "          period_documentsCount = (SELECT COUNT(d.idDocument)\n"
              + "                                   FROM Period p, PublishDate_Period pup, PublishDate pu, Document d\n"
              + "                                   WHERE p.idPeriod = Period.idPeriod AND\n"
              + "                                         p.idPeriod = pup.Period_idPeriod AND\n"
              + "                                         pup.PublishDate_idPublishDate = pu.idPublishDate AND\n"
              + "                                         pu.idPublishDate = d.PublishDate_idPublishDate)\n"
              + "      WHERE idPeriod = new.Period_idPeriod;\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_update_Document_PublishDate_idPublishDate AFTER UPDATE OF PublishDate_idPublishDate ON Document FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Document\n"
              + "      SET year = (SELECT year FROM PublishDate WHERE idPublishDate = new.PublishDate_idPublishDate)\n"
              + "      WHERE idDocument = new.idDocument;\n"
              + "    UPDATE PublishDate\n"
              + "      SET publishDate_documentsCount = (SELECT count(d.idDocument)\n"
              + "                                        FROM Document d\n"
              + "                                        WHERE d.PublishDate_idPublishDate = PublishDate.idPublishDate)\n"
              + "      WHERE idPublishDate IN (new.PublishDate_idPublishDate, old.PublishDate_idPublishDate);\n"
              + "    UPDATE Period\n"
              + "      SET period_documentsCount = (SELECT count(d.idDocument)\n"
              + "                                   FROM Period p, PublishDate_Period pup, PublishDate pu, Document d\n"
              + "                                   WHERE p.idPeriod = Period.idPeriod AND\n"
              + "                                         p.idPeriod = pup.Period_idPeriod AND\n"
              + "                                         pup.PublishDate_idPublishDate = pu.idPublishDate AND\n"
              + "                                         pu.idPublishDate = d.PublishDate_idPublishDate)\n"
              + "      WHERE idPeriod IN (SELECT p.idPeriod\n"
              + "                         FROM Period p, PublishDate_Period pup\n"
              + "                         WHERE pup.PublishDate_idPublishDate IN (new.PublishDate_idPublishDate, old.PublishDate_idPublishDate) AND"
              + "                               p.idPeriod = pup.Period_idPeriod);\n"
              + "  END;");
      
      /*stat.addBatch("CREATE TRIGGER aggregateData_update_Document_Journal_idJournal AFTER UPDATE OF Journal_idJournal ON Document FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Journal\n"
              + "      SET journal_documentsCount = journal_documentsCount - 1\n"
              + "      WHERE idJournal IN (old.Journal_idJournal);\n"
              + "    UPDATE Journal\n"
              + "      SET journal_documentsCount = journal_documentsCount + 1\n"
              + "      WHERE idJournal IN (new.Journal_idJournal);\n"
              + "    UPDATE SubjectCategory\n"
              + "      SET subjectCategory_documentsCount = (SELECT count(DISTINCT d.idDocument)\n"
              + "                                            FROM SubjectCategory sc, Journal_SubjectCategory_PublishDate jsp, Journal j, Document d\n"
              + "                                            WHERE sc.idSubjectCategory = SubjectCategory.idSubjectCategory AND\n"
              + "                                                  sc.idSubjectCategory = jsp.SubjectCategory_idSubjectCategory AND\n"
              + "                                                  jsp.Journal_idJournal = j.idJournal AND\n"
              + "                                                  j.idJournal = d.Journal_idJournal)\n"
              + "      WHERE idSubjectCategory IN (SELECT DISTINCT sjp.SubjectCategory_idSubjectCategory\n"
              + "                                  FROM Journal_SubjectCategory_PublishDate sjp\n"
              + "                                  WHERE sjp.Journal_idJournal IN (new.Journal_idJournal, old.Journal_idJournal));\n"
              + "  END;");*/
      
       stat.addBatch("CREATE TRIGGER aggregateData_update_Document_Journal_idJournal AFTER UPDATE OF Journal_idJournal ON Document FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Journal\n"
              + "      SET journal_documentsCount = journal_documentsCount - 1\n"
              + "      WHERE idJournal IN (old.Journal_idJournal);\n"
              + "    UPDATE Journal\n"
              + "      SET journal_documentsCount = journal_documentsCount + 1\n"
              + "      WHERE idJournal IN (new.Journal_idJournal);\n"
              + "    UPDATE SubjectCategory\n"
              + "          SET subjectCategory_documentsCount = subjectCategory_documentsCount + 1\n"
              + "      WHERE idSubjectCategory IN (SELECT DISTINCT sjp.SubjectCategory_idSubjectCategory\n"
              + "                                  FROM Journal_SubjectCategory_PublishDate sjp\n"
              + "                                  WHERE sjp.Journal_idJournal IN (new.Journal_idJournal));\n"
              + "    UPDATE SubjectCategory\n"
              + "          SET subjectCategory_documentsCount = subjectCategory_documentsCount - 1\n"
              + "      WHERE idSubjectCategory IN (SELECT DISTINCT sjp.SubjectCategory_idSubjectCategory\n"
              + "                                  FROM Journal_SubjectCategory_PublishDate sjp\n"
              + "                                  WHERE sjp.Journal_idJournal IN (old.Journal_idJournal));\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_delete_Document_Reference AFTER DELETE ON Document_Reference FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Reference\n"
              + "      SET reference_documentsCount = reference_documentsCount - 1\n"
              + "      WHERE idReference = old.Reference_idReference;\n"
              + "    UPDATE Document\n"
              + "      SET document_referencesCount = document_referencesCount - 1\n"
              + "      WHERE idDocument = old.Document_idDocument;\n"
              + "    UPDATE ReferenceGroup\n"
              + "      SET referenceGroup_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                           FROM ReferenceGroup rg, Reference r, Document_Reference dr \n"
              + "                                           WHERE rg.idReferenceGroup = ReferenceGroup.idReferenceGroup AND\n"
              + "                                                 rg.idReferenceGroup = r.ReferenceGroup_idReferenceGroup AND\n"
              + "                                                 r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceGroup = (SELECT ReferenceGroup_idReferenceGroup FROM Reference WHERE idReference = old.Reference_idReference);\n"
              + "    UPDATE ReferenceSource\n"
              + "      SET referenceSource_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                            FROM ReferenceSource rs, Reference r, Document_Reference dr \n"
              + "                                            WHERE rs.idReferenceSource = ReferenceSource.idReferenceSource AND\n"
              + "                                                  rs.idReferenceSource = r.ReferenceSource_idReferenceSource AND\n"
              + "                                                  r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceSource = (SELECT ReferenceSource_idReferenceSource FROM Reference WHERE idReference = old.Reference_idReference);\n"
              + "    UPDATE ReferenceSourceGroup\n"
              + "      SET referenceSourceGroup_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                                 FROM ReferenceSourceGroup rsg, ReferenceSource rs, Reference r, Document_Reference dr\n"
              + "                                                 WHERE rsg.idReferenceSourceGroup = ReferenceSourceGroup.idReferenceSourceGroup AND\n"
              + "                                                       rsg.idReferenceSourceGroup = rs.ReferenceSourceGroup_idReferenceSourceGroup AND\n"
              + "                                                       rs.idReferenceSource = r.ReferenceSource_idReferenceSource AND\n"
              + "                                                       r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceSourceGroup = (SELECT rs.ReferenceSourceGroup_idReferenceSourceGroup \n"
              + "                                      FROM ReferenceSource rs, Reference r\n"
              + "                                      WHERE r.idReference = old.Reference_idReference AND"
              + "                                            rs.idReferenceSource = r.ReferenceSource_idReferenceSource);\n"
              + "    UPDATE AuthorReference\n"
              + "      SET authorReference_documentsCount = (SELECT count(DISTINCT dr.Document_idDocument)\n"
              + "                                            FROM AuthorReference ar, AuthorReference_Reference arr, Reference r, Document_Reference dr\n"
              + "                                            WHERE ar.idAuthorReference = AuthorReference.idAuthorReference AND\n"
              + "                                                 ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND\n"
              + "                                                 arr.Reference_idReference = r.idReference AND\n"
              + "                                                 r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idAuthorReference IN (SELECT arr.AuthorReference_idAuthorReference\n"
              + "                                  FROM AuthorReference_Reference arr, Document_Reference dr, Reference r\n"
              + "                                  WHERE r.idReference = old.Reference_idReference AND\n"
              + "                                       arr.Reference_idReference = r.idReference AND\n"
              + "                                       r.idReference = dr.Reference_idReference);\n"
              + "    UPDATE AuthorReferenceGroup\n"
              + "      SET authorReferenceGroup_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                                 FROM AuthorReferenceGroup arg, AuthorReference ar, AuthorReference_Reference arr, Reference r, Document_Reference dr \n"
              + "                                                 WHERE arg.idAuthorReferenceGroup = AuthorReferenceGroup.idAuthorReferenceGroup AND\n"
              + "                                                      arg.idAuthorReferenceGroup = ar.AuthorReferenceGroup_idAuthorReferenceGroup AND\n"
              + "                                                      ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND\n"
              + "                                                      arr.Reference_idReference = r.idReference AND\n"
              + "                                                      r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idAuthorReferenceGroup IN (SELECT DISTINCT ar.AuthorReferenceGroup_idAuthorReferenceGroup\n"
              + "                                       FROM AuthorReference ar, AuthorReference_Reference arr, Reference r, Document_Reference dr\n"
              + "                                       WHERE r.idReference = old.Reference_idReference AND\n"
              + "                                            ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND\n"
              + "                                            arr.Reference_idReference = r.idReference AND\n"
              + "                                            r.idReference = dr.Reference_idReference);\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_insert_Document_Reference AFTER INSERT ON Document_Reference FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Reference\n"
              + "      SET reference_documentsCount = reference_documentsCount + 1\n"
              + "      WHERE idReference = new.Reference_idReference;\n"
              + "    UPDATE Document\n"
              + "      SET document_referencesCount = document_referencesCount + 1\n"
              + "      WHERE idDocument = new.Document_idDocument;\n"
              + "    UPDATE ReferenceGroup\n"
              + "      SET referenceGroup_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                           FROM ReferenceGroup rg, Reference r, Document_Reference dr \n"
              + "                                           WHERE rg.idReferenceGroup = ReferenceGroup.idReferenceGroup AND\n"
              + "                                                 rg.idReferenceGroup = r.ReferenceGroup_idReferenceGroup AND\n"
              + "                                                 r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceGroup = (SELECT ReferenceGroup_idReferenceGroup FROM Reference WHERE idReference = new.Reference_idReference);\n"
              + "    UPDATE ReferenceSource\n"
              + "      SET referenceSource_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                            FROM ReferenceSource rs, Reference r, Document_Reference dr \n"
              + "                                            WHERE rs.idReferenceSource = ReferenceSource.idReferenceSource AND\n"
              + "                                                  rs.idReferenceSource = r.ReferenceSource_idReferenceSource AND\n"
              + "                                                  r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceSource = (SELECT ReferenceSource_idReferenceSource FROM Reference WHERE idReference = new.Reference_idReference);\n"
              + "    UPDATE ReferenceSourceGroup\n"
              + "      SET referenceSourceGroup_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                                 FROM ReferenceSourceGroup rsg, ReferenceSource rs, Reference r, Document_Reference dr\n"
              + "                                                 WHERE rsg.idReferenceSourceGroup = ReferenceSourceGroup.idReferenceSourceGroup AND\n"
              + "                                                       rsg.idReferenceSourceGroup = rs.ReferenceSourceGroup_idReferenceSourceGroup AND\n"
              + "                                                       rs.idReferenceSource = r.ReferenceSource_idReferenceSource AND\n"
              + "                                                       r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceSourceGroup = (SELECT rs.ReferenceSourceGroup_idReferenceSourceGroup \n"
              + "                                      FROM ReferenceSource rs, Reference r\n"
              + "                                      WHERE r.idReference = new.Reference_idReference AND"
              + "                                            rs.idReferenceSource = r.ReferenceSource_idReferenceSource);\n"
              + "    UPDATE AuthorReference\n"
              + "      SET authorReference_documentsCount = (SELECT count(DISTINCT dr.Document_idDocument)\n"
              + "                                            FROM AuthorReference ar, AuthorReference_Reference arr, Reference r, Document_Reference dr\n"
              + "                                            WHERE ar.idAuthorReference = AuthorReference.idAuthorReference AND\n"
              + "                                                 ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND\n"
              + "                                                 arr.Reference_idReference = r.idReference AND\n"
              + "                                                 r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idAuthorReference IN (SELECT arr.AuthorReference_idAuthorReference\n"
              + "                                  FROM AuthorReference_Reference arr, Document_Reference dr, Reference r\n"
              + "                                  WHERE r.idReference = new.Reference_idReference AND\n"
              + "                                       arr.Reference_idReference = r.idReference AND\n"
              + "                                       r.idReference = dr.Reference_idReference);\n"
              + "    UPDATE AuthorReferenceGroup\n"
              + "      SET authorReferenceGroup_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                                 FROM AuthorReferenceGroup arg, AuthorReference ar, AuthorReference_Reference arr, Reference r, Document_Reference dr \n"
              + "                                                 WHERE arg.idAuthorReferenceGroup = AuthorReferenceGroup.idAuthorReferenceGroup AND\n"
              + "                                                      arg.idAuthorReferenceGroup = ar.AuthorReferenceGroup_idAuthorReferenceGroup AND\n"
              + "                                                      ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND\n"
              + "                                                      arr.Reference_idReference = r.idReference AND\n"
              + "                                                      r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idAuthorReferenceGroup IN (SELECT DISTINCT ar.AuthorReferenceGroup_idAuthorReferenceGroup\n"
              + "                                       FROM AuthorReference ar, AuthorReference_Reference arr, Reference r, Document_Reference dr\n"
              + "                                       WHERE r.idReference = new.Reference_idReference AND\n"
              + "                                            ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND\n"
              + "                                            arr.Reference_idReference = r.idReference AND\n"
              + "                                            r.idReference = dr.Reference_idReference);\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_delete_AuthorReference_Reference AFTER DELETE ON AuthorReference_Reference FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE AuthorReference\n"
              + "      SET authorReference_referencesCount = authorReference_referencesCount - 1,\n"
              + "          authorReference_documentsCount = (SELECT count(DISTINCT dr.Document_idDocument)\n"
              + "                                            FROM AuthorReference ar, AuthorReference_Reference arr, Reference r, Document_Reference dr\n"
              + "                                            WHERE ar.idAuthorReference = AuthorReference.idAuthorReference AND\n"
              + "                                                  ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND\n"
              + "                                                  arr.Reference_idReference = r.idReference AND\n"
              + "                                                  r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idAuthorReference = old.AuthorReference_idAuthorReference;\n"
              + "    UPDATE AuthorReferenceGroup\n"
              + "      SET authorReferenceGroup_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                                 FROM AuthorReferenceGroup arg, AuthorReference ar, AuthorReference_Reference arr, Reference r, Document_Reference dr \n"
              + "                                                 WHERE arg.idAuthorReferenceGroup =  AuthorReferenceGroup.idAuthorReferenceGroup AND\n"
              + "                                                       arg.idAuthorReferenceGroup = ar.AuthorReferenceGroup_idAuthorReferenceGroup AND\n"
              + "                                                       ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND\n"
              + "                                                       arr.Reference_idReference = r.idReference AND\n"
              + "                                                       r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idAuthorReferenceGroup = (SELECT AuthorReferenceGroup_idAuthorReferenceGroup FROM AuthorReference WHERE idAuthorReference = old.AuthorReference_idAuthorReference);\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_insert_AuthorReference_Reference AFTER INSERT ON AuthorReference_Reference FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE AuthorReference\n"
              + "      SET authorReference_referencesCount = authorReference_referencesCount + 1,\n"
              + "          authorReference_documentsCount = (SELECT count(DISTINCT dr.Document_idDocument)\n"
              + "                                            FROM AuthorReference ar, AuthorReference_Reference arr, Reference r, Document_Reference dr\n"
              + "                                            WHERE ar.idAuthorReference = AuthorReference.idAuthorReference AND\n"
              + "                                                  ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND\n"
              + "                                                  arr.Reference_idReference = r.idReference AND\n"
              + "                                                  r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idAuthorReference = new.AuthorReference_idAuthorReference;\n"
              + "    UPDATE AuthorReferenceGroup\n"
              + "      SET authorReferenceGroup_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                                                 FROM AuthorReferenceGroup arg, AuthorReference ar, AuthorReference_Reference arr, Reference r, Document_Reference dr \n"
              + "                                                 WHERE arg.idAuthorReferenceGroup =  AuthorReferenceGroup.idAuthorReferenceGroup AND\n"
              + "                                                       arg.idAuthorReferenceGroup = ar.AuthorReferenceGroup_idAuthorReferenceGroup AND\n"
              + "                                                       ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND\n"
              + "                                                       arr.Reference_idReference = r.idReference AND\n"
              + "                                                       r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idAuthorReferenceGroup = (SELECT AuthorReferenceGroup_idAuthorReferenceGroup FROM AuthorReference WHERE idAuthorReference = new.AuthorReference_idAuthorReference);\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_update_ReferenceSource_idReferenceSource AFTER UPDATE OF ReferenceSource_idReferenceSource ON Reference FOR EACH ROW\n"
              + "  WHEN new.ReferenceSource_idReferenceSource IS NOT old.ReferenceSource_idReferenceSource\n"
              + "  BEGIN\n"
              + "    UPDATE ReferenceSource\n"
              + "      SET referenceSource_referencesCount = referenceSource_referencesCount - 1\n"
              + "      WHERE idReferenceSource IN (old.ReferenceSource_idReferenceSource);\n"
              + "    UPDATE ReferenceSource\n"
              + "      SET referenceSource_referencesCount = referenceSource_referencesCount + 1\n"
              + "      WHERE idReferenceSource IN (new.ReferenceSource_idReferenceSource);\n"
              + "    UPDATE ReferenceSource\n"
              + "      SET referenceSource_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument)\n"
              + "                            FROM ReferenceSource rs, Reference r, Document_Reference dr\n"
              + "                             WHERE idReferenceSource = ReferenceSource.idReferenceSource AND\n"
              + "                                   rs.idReferenceSource = r.ReferenceSource_idReferenceSource AND\n"
              + "                                   r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceSource IN (new.ReferenceSource_idReferenceSource, old.ReferenceSource_idReferenceSource);\n"
              + "    UPDATE ReferenceSourceGroup\n"
              + "      SET referenceSourceGroup_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument) \n"
              + "                            FROM ReferenceSourceGroup rsg, ReferenceSource rs, Reference r, Document_Reference dr \n"
              + "                            WHERE rsg.idReferenceSourceGroup = ReferenceSourceGroup.idReferenceSourceGroup AND\n"
              + "                                 rsg.idReferenceSourceGroup = rs.ReferenceSourceGroup_idReferenceSourceGroup AND\n"
              + "                                 rs.idReferenceSource = r.ReferenceSource_idReferenceSource AND\n"
              + "                                 r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceSourceGroup = (SELECT rs.ReferenceSourceGroup_idReferenceSourceGroup \n"
              + "                                      FROM Reference r, ReferenceSource rs\n"
              + "                                      WHERE r.idReference = new.idReference AND r.ReferenceSource_idReferenceSource = rs.idReferenceSource);\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_delete_Author_Affiliation AFTER DELETE ON Author_Affiliation FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Affiliation\n"
              + "      SET affiliation_authorsCount = affiliation_authorsCount - 1\n"
              + "      WHERE idAffiliation = old.Affiliation_idAffiliation;\n"
              + "    UPDATE Author\n"
              + "      SET author_affiliationsCount = author_affiliationsCount - 1\n"
              + "      WHERE idAuthor = old.Author_idAuthor;\n"
              + "  END;");

      stat.addBatch("CREATE TRIGGER aggregateData_insert_Author_Affiliation AFTER INSERT ON Author_Affiliation FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Affiliation\n"
              + "      SET affiliation_authorsCount = affiliation_authorsCount + 1\n"
              + "      WHERE idAffiliation = new.Affiliation_idAffiliation;\n"
              + "    UPDATE Author\n"
              + "      SET author_affiliationsCount = author_affiliationsCount + 1\n"
              + "      WHERE idAuthor = new.Author_idAuthor;\n"
              + "  END;");
      
      stat.addBatch("CREATE TRIGGER aggregateData_delete_AuthorReference AFTER DELETE ON AuthorReference FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE AuthorReferenceGroup\n"
              + "      SET itemsCount = itemsCount - 1,\n"
              + "          authorReferenceGroup_documentsCount = (SELECT COUNT (DISTINCT dr.Document_idDocument)\n"
              + "                                                 FROM AuthorReferenceGroup arg, AuthorReference ar, AuthorReference_Reference arr, Reference r, Document_Reference dr\n"
              + "                                                 WHERE arg.idAuthorReferenceGroup = AuthorReferenceGroup.idAuthorReferenceGroup AND\n"
              + "                                                       arg.idAuthorReferenceGroup = ar.AuthorReferenceGroup_idAuthorReferenceGroup AND\n"
              + "                                                       ar.idAuthorReference = arr.AuthorReference_idAuthorReference AND\n"
              + "                                                       arr.Reference_idReference = r.idReference AND\n"
              + "                                                       r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idAuthorReferenceGroup = old.AuthorReferenceGroup_idAuthorReferenceGroup;\n"
              + "  END;");
      
      
      stat.addBatch("CREATE TRIGGER aggregateData_delete_ReferenceSource AFTER DELETE ON ReferenceSource FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE ReferenceSourceGroup\n"
              + "      SET itemsCount = itemsCount - 1,\n"
              + "          referenceSourceGroup_documentsCount = (SELECT COUNT (DISTINCT dr.Document_idDocument)\n"
              + "                                                 FROM ReferenceSourceGroup rsg, ReferenceSource rs, Reference r, Document_Reference dr\n"
              + "                                                 WHERE rsg.idReferenceSourceGroup = ReferenceSourceGroup.idReferenceSourceGroup AND\n"
              + "                                                       rsg.idReferenceSourceGroup = rs.ReferenceSourceGroup_idReferenceSourceGroup AND\n"
              + "                                                       rs.idReferenceSource = r.ReferenceSource_idReferenceSource AND\n"
              + "                                                       r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceSourceGroup = old.ReferenceSourceGroup_idReferenceSourceGroup;\n"
              + "  END;");
      
      stat.addBatch("CREATE TRIGGER aggregateData_delete_Reference AFTER DELETE ON Reference FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE ReferenceGroup\n"
              + "      SET itemsCount = itemsCount - 1,\n"
              + "          referenceGroup_documentsCount = (SELECT COUNT (DISTINCT dr.Document_idDocument)\n"
              + "                                           FROM ReferenceGroup rg, Reference r, Document_Reference dr\n"
              + "                                           WHERE rg.idReferenceGroup = ReferenceGroup.idReferenceGroup AND\n"
              + "                                                 rg.idReferenceGroup = r.ReferenceGroup_idReferenceGroup AND\n"
              + "                                                 r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceGroup = old.ReferenceGroup_idReferenceGroup;\n"
              + "    UPDATE ReferenceSource\n"
              + "      SET referenceSource_referencesCount = referenceSource_referencesCount - 1,\n"
              + "          referenceSource_documentsCount = (SELECT COUNT(DISTINCT dr.Document_idDocument)\n"
              + "                                            FROM ReferenceSource rs, Reference r, Document_Reference dr\n"
              + "                                            WHERE idReferenceSource = ReferenceSource.idReferenceSource AND\n"
              + "                                                  rs.idReferenceSource = r.ReferenceSource_idReferenceSource AND\n"
              + "                                                  r.idReference = dr.Reference_idReference)\n"
              + "      WHERE idReferenceSource = old.ReferenceSource_idReferenceSource;\n"
              + "  END;");
      
       stat.addBatch("CREATE TRIGGER aggregateData_delete_Author AFTER DELETE ON Author FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE AuthorGroup\n"
              + "      SET itemsCount = itemsCount - 1,\n"
              + "          authorGroup_documentsCount = (SELECT COUNT (DISTINCT da.Document_idDocument)\n"
              + "                                        FROM AuthorGroup ag, Author a, Document_Author da\n"
              + "                                        WHERE ag.idAuthorGroup = AuthorGroup.idAuthorGroup AND\n"
              + "                                             ag.idAuthorGroup = a.AuthorGroup_idAuthorGroup AND\n"
              + "                                             a.idAuthor = da.Author_idAuthor)\n"
              + "      WHERE idAuthorGroup = old.AuthorGroup_idAuthorGroup;\n"
              + "  END;");
      
       stat.addBatch("CREATE TRIGGER aggregateData_delete_Word AFTER DELETE ON Word FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE WordGroup\n"
              + "      SET itemsCount = itemsCount - 1,\n"
              + "          wordGroup_documentsCount = (SELECT COUNT (DISTINCT dw.Document_idDocument)\n"
              + "                                      FROM WordGroup wg, Word w, Document_Word dw\n"
              + "                                      WHERE wg.idWordGroup = WordGroup.idWordGroup AND\n"
              + "                                            wg.idWordGroup = w.WordGroup_idWordGroup AND\n"
              + "                                            w.idWord = dw.Word_idWord)\n"
              + "      WHERE idWordGroup = old.WordGroup_idWordGroup;\n"
              + "  END;");
       
       stat.addBatch("CREATE TRIGGER aggregateData_delete_Document AFTER DELETE ON Document FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE Journal\n"
              + "      SET journal_documentsCount = journal_documentsCount - 1\n"
              + "      WHERE idJournal = old.Journal_idJournal;\n"
              + "    UPDATE PublishDate\n"
              + "      SET publishDate_documentsCount = publishDate_documentsCount - 1\n"
              + "      WHERE idPublishDate = old.PublishDate_idPublishDate;\n"
              + "    UPDATE Period\n"
              + "      SET period_documentsCount = (SELECT count(d.idDocument)\n"
              + "                                   FROM Period p, PublishDate_Period pup, PublishDate pu, Document d\n"
              + "                                   WHERE p.idPeriod = Period.idPeriod AND\n"
              + "                                         p.idPeriod = pup.Period_idPeriod AND\n"
              + "                                         pup.PublishDate_idPublishDate = pu.idPublishDate AND\n"
              + "                                         pu.idPublishDate = d.PublishDate_idPublishDate)\n"
              + "      WHERE idPeriod IN (SELECT p.idPeriod\n"
              + "                         FROM Period p, PublishDate_Period pup, PublishDate pu\n"
              + "                         WHERE pu.idPublishDate = old.PublishDate_idPublishDate AND\n"
              + "                               p.idPeriod = pup.Period_idPeriod AND\n"
              + "                               pup.PublishDate_idPublishDate = pu.idPublishDate);\n"
              + "    UPDATE SubjectCategory\n"
              + "      SET subjectCategory_documentsCount = (SELECT count(DISTINCT d.idDocument)\n"
              + "                                            FROM SubjectCategory sc, Journal_SubjectCategory_PublishDate jsp, Journal j, Document d\n"
              + "                                            WHERE sc.idSubjectCategory = SubjectCategory.idSubjectCategory AND\n"
              + "                                                  sc.idSubjectCategory = jsp.SubjectCategory_idSubjectCategory AND\n"
              + "                                                  jsp.Journal_idJournal = j.idJournal AND\n"
              + "                                                  j.idJournal = d.Journal_idJournal)\n"
              + "      WHERE idSubjectCategory IN (SELECT sjp.SubjectCategory_idSubjectCategory\n"
              + "                                  FROM Journal_SubjectCategory_PublishDate sjp\n"
              + "                                  WHERE sjp.Journal_idJournal = old.Journal_idJournal);\n"
              + "  END;");
       
       stat.addBatch("CREATE TRIGGER aggregateData_insert_Journal_SubjectCategory_PublishDate AFTER INSERT ON Journal_SubjectCategory_PublishDate FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE SubjectCategory\n"
              + "      SET subjectCategory_journalsCount = (SELECT count(DISTINCT j.idJournal)\n"
              + "                                            FROM SubjectCategory sc, Journal_SubjectCategory_PublishDate jsp, Journal j\n"
              + "                                            WHERE sc.idSubjectCategory = SubjectCategory.idSubjectCategory AND\n"
              + "                                                  sc.idSubjectCategory = jsp.SubjectCategory_idSubjectCategory AND\n"
              + "                                                  jsp.Journal_idJournal = j.idJournal),\n"
              + "          subjectCategory_documentsCount = subjectCategory_documentsCount + (SELECT count(d.idDocument)\n"
              + "                                                                             FROM Journal j, Document d\n"
              + "                                                                             WHERE j.idJournal = new.Journal_idJournal AND\n"
              + "                                                                                   j.idJournal = d.Journal_idJournal)\n"
              + "      WHERE idSubjectCategory = new.SubjectCategory_idSubjectCategory;\n"
              + "  END;");
       
       stat.addBatch("CREATE TRIGGER aggregateData_delete_Journal_SubjectCategory_PublishDate AFTER DELETE ON Journal_SubjectCategory_PublishDate FOR EACH ROW\n"
              + "  BEGIN\n"
              + "    UPDATE SubjectCategory\n"
              + "      SET subjectCategory_journalsCount = (SELECT count(DISTINCT j.idJournal)\n"
              + "                                            FROM SubjectCategory sc, Journal_SubjectCategory_PublishDate jsp, Journal j\n"
              + "                                            WHERE sc.idSubjectCategory = SubjectCategory.idSubjectCategory AND\n"
              + "                                                  sc.idSubjectCategory = jsp.SubjectCategory_idSubjectCategory AND\n"
              + "                                                  jsp.Journal_idJournal = j.idJournal),\n"
              + "          subjectCategory_documentsCount = subjectCategory_documentsCount - (SELECT count(d.idDocument)\n"
              + "                                                                             FROM Journal j, Document d\n"
              + "                                                                             WHERE j.idJournal = old.Journal_idJournal AND\n"
              + "                                                                                   j.idJournal = d.Journal_idJournal)\n"
              + "      WHERE idSubjectCategory = old.SubjectCategory_idSubjectCategory;\n"
              + "  END;");

       
       
      stat.executeBatch();

      stat.close();

      commit();

    } catch (ClassNotFoundException e1) {

      rollback();

      throw new KnowledgeBaseException(e1.getMessage(), e1.getCause());

    } catch (SQLException e2) {

      e2.printStackTrace(System.err);
      
      //rollback();

      throw new KnowledgeBaseException(e2);
    }
  }

  /**
   * 
   * @param filePath
   * @param check true if the knowledge base structure must be checked
   * @throws KnowledgeBaseException
   */
  public void loadKnowledgeBase(String filePath, boolean check) throws KnowledgeBaseException {

    try {

      Class.forName("org.sqlite.JDBC");

      // Connect to the database
      SQLiteConfig config = new SQLiteConfig();
      config.enforceForeignKeys(true);
//      config.setPageSize(4096);
//      config.setCacheSize(512);
//      config.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
      this.conn = DriverManager.getConnection("jdbc:sqlite:" + filePath, config.toProperties());
      this.conn.setAutoCommit(false);

      // Check if the database is incorrect.

      if (check && (!checkKnowledgeBaseStructure())) {

        close();

        throw new IncorrectFormatKnowledgeBaseException("The knowledge base format is incorrect.");
      }

    } catch (ClassNotFoundException e1) {

      close();

      throw new KnowledgeBaseException(e1.getMessage(), e1.getCause());

    } catch (SQLException e2) {

      close();

      throw new KnowledgeBaseException(e2.getMessage(), e2.getCause());
    }
  }

  /**
   * Close the connection with the knowledge base. The connection will be set
   * as null.
   *
   * @throws KnowledgeBaseException
   */
  public void close() throws KnowledgeBaseException {

    try {

      if (this.conn != null) {

        this.conn.close();

        this.conn = null;
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());

    }
  }

  /**
   * 
   * @return
   */
  public Connection getConnection() {

    return this.conn;
  }

  /**
   * 
   * @return
   */
  public boolean isClosed() {

    return this.conn == null;
  }

  /**
   * Delete all items from the knowledge base. The data base keeps its structure.
   * The knowledge base must be loaded. {@code isClosed() == false}
   * 
   * @throws KnowledgeBaseException if an error occurs or the knowledge base is closed.
   */
  public void clearKnowledgeBase() throws KnowledgeBaseException {

    try {

      if (!isClosed()) {

        Statement stat = this.conn.createStatement();

        stat.addBatch("DELETE FROM Document;");
        stat.addBatch("DELETE FROM Author;");
        stat.addBatch("DELETE FROM AuthorReference;");
        stat.addBatch("DELETE FROM Word;");
        stat.addBatch("DELETE FROM Reference;");
        stat.addBatch("DELETE FROM ReferenceSource;");
        stat.addBatch("DELETE FROM Journal;");
        stat.addBatch("DELETE FROM PublishDate;");
        stat.addBatch("DELETE FROM Period;");
        stat.addBatch("DELETE FROM Affiliation;");
        stat.addBatch("DELETE FROM SubjectCategory;");
        stat.addBatch("DELETE FROM WordGroup;");
        stat.addBatch("DELETE FROM AuthorGroup;");
        stat.addBatch("DELETE FROM AuthorReferenceGroup;");
        stat.addBatch("DELETE FROM ReferenceGroup;");
        stat.addBatch("DELETE FROM ReferenceSourceGroup;");
        stat.addBatch("DELETE FROM sqlite_sequence;");

        stat.executeBatch();
        stat.close();

        commit();

      } else {

        throw new KnowledgeBaseException("The knowledge base must be loaded.");
      }

    } catch (SQLException e) {

      rollback();

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
  }

  /**
   * 
   * @throws KnowledgeBaseException if an error occurs or the knowledge base is closed.
   */
  public void commit() throws KnowledgeBaseException {

    try {

      if (!isClosed()) {

        this.conn.commit();

      } else {

        throw new KnowledgeBaseException("The knowledge base must be loaded.");
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e);
    }
  }

  /**
   * 
   * @throws KnowledgeBaseException if an error occurs or the knowledge base is closed.
   */
  public void rollback() throws KnowledgeBaseException {

    try {

      if (!isClosed()) {

        this.conn.rollback();

      } else {

        throw new KnowledgeBaseException("The knowledge base must be loaded.");
      }

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e);
    }
  }

  /**
   * 
   * @throws KnowledgeBaseException if an error occurs or the knowledge base is closed.
   */
  public boolean checkKnowledgeBaseStructure() throws KnowledgeBaseException {

    boolean valid = false;
    PreparedStatement stat = null;
    ResultSet rs = null;

    try {

      if (! isClosed()) {

        stat = getConnection().prepareStatement("SELECT knowledgeBaseVersion FROM KnowledgeBaseVersion WHERE knowledgeBaseVersion = 'v1_03' LIMIT 1;");
        rs = stat.executeQuery();
        valid = rs.next();
        rs.close();
        stat.close();
        
        if (valid) {

          stat = getConnection().prepareStatement("SELECT idAuthorGroup, groupName, stopGroup FROM AuthorGroup LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idAuthorReferenceGroup, groupName,stopGroup FROM AuthorReferenceGroup LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idReferenceGroup, groupName, stopGroup FROM ReferenceGroup LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idReferenceSourceGroup, groupName, stopGroup FROM ReferenceSourceGroup LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idWordGroup, groupName, stopGroup FROM WordGroup LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idAffiliation, fullAffiliation FROM Affiliation LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idAuthor, authorName, fullAuthorName, AuthorGroup_idAuthorGroup FROM Author LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idAuthorReference, authorName, AuthorReferenceGroup_idAuthorReferenceGroup, Author_idAuthor FROM AuthorReference LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idJournal, source, conferenceInformation FROM Journal LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idPeriod, name, position FROM Period LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idPublishDate, year, date FROM PublishDate LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idReferenceSource, source, ReferenceSourceGroup_idReferencesourceGroup FROM ReferenceSource LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idReference, fullReference, ReferenceSource_idReferenceSource, volume, issue, page, doi, format, year, ReferenceGroup_idReferenceGroup FROM Reference LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idSubjectCategory, subjectCategoryName FROM SubjectCategory LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idWord, wordName, WordGroup_idWordGroup FROM Word LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT idDocument, title, type, docAbstract, volume, issue, beginPage, endPage, citationsCount, doi, sourceIdentifier, PublishDate_idPublishDate, Journal_idJournal FROM Document LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT PublishDate_idPublishDate, Period_idPeriod FROM PublishDate_Period LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT Journal_idJournal, SubjectCategory_idSubjectCategory, PublishDate_idPublishDate FROM Journal_SubjectCategory_PublishDate LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT Author_idAuthor, Document_idDocument, position FROM Document_Author LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT Affiliation_idAffiliation, Document_idDocument FROM Document_Affiliation LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT Document_idDocument, Word_idWord, isAuthorWord, isSourceWord, isAddedWord FROM Document_Word LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT Reference_idReference, Document_idDocument FROM Document_Reference LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT AuthorReference_idAuthorReference, Reference_idReference, position FROM AuthorReference_Reference LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          stat = getConnection().prepareStatement("SELECT Affiliation_idAffiliation, Author_idAuthor FROM Author_Affiliation LIMIT 1;");
          rs = stat.executeQuery();
          rs.close();
          stat.close();

          valid = true;
        }

      } else {

        valid = false;

        throw new KnowledgeBaseException("The knowledge base must be loaded.");
      }

    } catch (SQLException e) {

      if (rs != null) {

        try {

          rs.close();

        } catch (SQLException ex) {

          throw new KnowledgeBaseException(ex.getMessage(), ex.getCause());
        }
      }

      if (stat != null) {

        try {

          stat.close();

        } catch (SQLException ex) {

          throw new KnowledgeBaseException(ex.getMessage(), ex.getCause());
        }
      }

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return valid;

  }
  
  /**
   * 
   * @throws KnowledgeBaseException if an error occurs or the knowledge base is closed.
   */
  public boolean checkKnowledgeBaseStructureV1_02() throws KnowledgeBaseException {

    boolean valid = false;
    PreparedStatement stat = null;
    ResultSet rs = null;

    try {

      if (!isClosed()) {

        stat = getConnection().prepareStatement("SELECT idAuthorGroup, groupName, stopGroup FROM AuthorGroup LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idAuthorReferenceGroup, groupName,stopGroup FROM AuthorReferenceGroup LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idReferenceGroup, groupName, stopGroup FROM ReferenceGroup LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idReferenceSourceGroup, groupName, stopGroup FROM ReferenceSourceGroup LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idWordGroup, groupName, stopGroup FROM WordGroup LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idAffiliation, fullAffiliation FROM Affiliation LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idAuthor, authorName, fullAuthorName, AuthorGroup_idAuthorGroup FROM Author LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idAuthorReference, authorName, AuthorReferenceGroup_idAuthorReferenceGroup, Author_idAuthor FROM AuthorReference LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idJournal, source, conferenceInformation FROM Journal LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idPeriod, name, position FROM Period LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idPublishDate, year, date FROM PublishDate LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idReferenceSource, source, ReferenceSourceGroup_idReferencesourceGroup FROM ReferenceSource LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idReference, fullReference, ReferenceSource_idReferenceSource, volume, issue, page, doi, format, year, ReferenceGroup_idReferenceGroup FROM Reference LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idSubjectCategory, subjectCategoryName FROM SubjectCategory LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idWord, wordName, WordGroup_idWordGroup FROM Word LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT idDocument, title, type, docAbstract, volume, issue, beginPage, endPage, citationsCount, doi, sourceIdentifier, PublishDate_idPublishDate, Journal_idJournal FROM Document LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT PublishDate_idPublishDate, Period_idPeriod FROM PublishDate_Period LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT Journal_idJournal, SubjectCategory_idSubjectCategory, PublishDate_idPublishDate FROM Journal_SubjectCategory_PublishDate LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT Author_idAuthor, Document_idDocument, position FROM Document_Author LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT Affiliation_idAffiliation, Document_idDocument FROM Document_Affiliation LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT Document_idDocument, Word_idWord, isAuthorWord, isSourceWord, isAddedWord FROM Document_Word LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT Reference_idReference, Document_idDocument FROM Document_Reference LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT AuthorReference_idAuthorReference, Reference_idReference, position FROM AuthorReference_Reference LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("SELECT Affiliation_idAffiliation, Author_idAuthor FROM Author_Affiliation LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat.close();

        valid = true;

      } else {

        valid = false;

        throw new KnowledgeBaseException("The knowledge base must be loaded.");
      }

    } catch (SQLException e) {

      if (rs != null) {

        try {

          rs.close();

        } catch (SQLException ex) {

          throw new KnowledgeBaseException(ex.getMessage(), ex.getCause());
        }
      }

      if (stat != null) {

        try {

          stat.close();

        } catch (SQLException ex) {

          throw new KnowledgeBaseException(ex.getMessage(), ex.getCause());
        }
      }

      //throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return valid;

  }

  /**
   * 
   * @return
   * @throws KnowledgeBaseException 
   */
  public boolean checkKnowledgeBaseStructureV1_01() throws KnowledgeBaseException {

    boolean valid = false;
    PreparedStatement stat = null;
    ResultSet rs = null;

    try {

      if (!isClosed()) {

        stat = getConnection().prepareStatement("Select idAuthorGroup, groupName, stopGroup FROM AuthorGroup LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idAuthorReferenceGroup, groupName,stopGroup FROM AuthorReferenceGroup LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idReferenceGroup, groupName, stopGroup FROM ReferenceGroup LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idReferenceSourceGroup, groupName, stopGroup FROM ReferenceSourceGroup LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idWordGroup, groupName, stopGroup FROM WordGroup LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idAffiliation, fullAffiliation FROM Affiliation LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idAuthor, authorName, fullAuthorName, AuthorGroup_idAuthorGroup FROM Author LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idAuthorReference, authorName, AuthorReferenceGroup_idAuthorReferenceGroup, Author_idAuthor FROM AuthorReference LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idJournal, source, conferenceInformation FROM Journal LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idPeriod, name, position FROM Period LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idPublishDate, year, date FROM PublishDate LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idReferenceSource, source, ReferenceSourceGroup_idReferencesourceGroup FROM ReferenceSource LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idReference, fullReference, ReferenceSource_idReferenceSource, volume, issue, page, doi, format, year, ReferenceGroup_idReferenceGroup FROM Reference LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idSubjectCategory, subjectCategoryName FROM SubjectCategory LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idWord, wordName, WordGroup_idWordGroup FROM Word LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select idDocument, title, type, docAbstract, volume, issue, beginPage, endPage, citationsCount, doi, sourceIdentifier, PublishDate_idPublishDate, Journal_idJournal FROM Document LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select PublishDate_idPublishDate, Period_idPeriod FROM PublishDate_Period LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select Journal_idJournal, SubjectCategory_idSubjectCategory, PublishDate_idPublishDate FROM Journal_SubjectCategory_PublishDate LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select Author_idAuthor, Document_idDocument, position FROM Document_Author LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select Affiliation_idAffiliation, Document_idDocument FROM Document_Affiliation LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select Document_idDocument, Word_idWord, authorKeyword, sourceKeyword, extractedKeyword FROM Document_Word LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select Reference_idReference, Document_idDocument FROM Document_Reference LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select AuthorReference_idAuthorReference, Reference_idReference, position FROM AuthorReference_Reference LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat = getConnection().prepareStatement("Select Affiliation_idAffiliation, Author_idAuthor FROM Author_Affiliation LIMIT 1;");
        rs = stat.executeQuery();
        rs.close();
        stat.close();

        stat.close();

        valid = true;

      } else {

        valid = false;

        throw new KnowledgeBaseException("The knowledge base must be loaded.");
      }

    } catch (SQLException e) {

      if (rs != null) {

        try {

          rs.close();

        } catch (SQLException ex) {

          throw new KnowledgeBaseException(ex.getMessage(), ex.getCause());
        }
      }

      if (stat != null) {

        try {

          stat.close();

        } catch (SQLException ex) {

          throw new KnowledgeBaseException(ex.getMessage(), ex.getCause());
        }
      }

      //throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }

    return valid;

  }
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
