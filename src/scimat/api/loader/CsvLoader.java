/*
 * CsvLoader.java
 *
 * Created on 11-ene-2011, 13:46:29
 */
package scimat.api.loader;

import com.csvreader.CsvReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.dao.AffiliationDAO;
import scimat.model.knowledgebase.dao.AuthorDAO;
import scimat.model.knowledgebase.dao.AuthorReferenceDAO;
import scimat.model.knowledgebase.dao.AuthorReferenceReferenceDAO;
import scimat.model.knowledgebase.dao.DocumentAffiliationDAO;
import scimat.model.knowledgebase.dao.DocumentAuthorDAO;
import scimat.model.knowledgebase.dao.DocumentDAO;
import scimat.model.knowledgebase.dao.DocumentReferenceDAO;
import scimat.model.knowledgebase.dao.DocumentWordDAO;
import scimat.model.knowledgebase.dao.JournalDAO;
import scimat.model.knowledgebase.dao.JournalSubjectCategoryPublishDateDAO;
import scimat.model.knowledgebase.dao.PublishDateDAO;
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.dao.ReferenceSourceDAO;
import scimat.model.knowledgebase.dao.SubjectCategoryDAO;
import scimat.model.knowledgebase.dao.WordDAO;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.DocumentWord;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.SubjectCategory;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 * This class provide a loader to import data to the knowlesfe base from a 
 * CSV format.
 * 
 * Particularly, the CSV file must have in the first row must have the 
 * field's label.
 * 
 * - Title (if title is not present or is empty, the row, i.e., the record will be not taken into account)
 * - Authors (separated by //)
 * - Affiliations (separated by //)
 * - Abstract
 * - Type
 * - Citations
 * - Journal
 * - Doi
 * - Volume
 * - Issue
 * - BeginPage
 * - EndPage
 * - Year
 * - FullPublishDate (if year is not present, this field will not be taken into account)
 * - SourceKeywords (separated by //)
 * - AuthorKeywords (separated by //)
 * - References (separated by //)
 * - SubjectCategories (separated by //)
 * 
 * NOTE: The upper-case and lower-case must be taken into account.
 * 
 * @author mjcobo
 */
public class CsvLoader implements GenericLoader {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private String file;
  private char delimiter;
  private boolean importReferences;
  
  private final static String __TITLE = "Title";
  private final static String __AUTHORS = "Authors";
  private final static String __AFFILIATIONS = "Affiliations";
  private final static String __ABSTRACT = "Abstract";
  private final static String __TYPE = "Type";
  private final static String __CITATIONS = "Citations";
  private final static String __JOURNAL = "Journal";
  private final static String __DOI = "Doi";
  private final static String __VOLUME = "Volume";
  private final static String __ISSUE = "Issue";
  private final static String __BEGIN_PAGE = "BeginPage";
  private final static String __END_PAGE = "EndPage";
  private final static String __YEAR = "Year";
  private final static String __FULL_PUBLISH_DATE = "FullPublishDate";
  private final static String __SOURCE_KEYWORDS = "SourceKeywords";
  private final static String __AUTHOR_KEYWORDS = "AuthorKeywords";
  private final static String __REFERENCES = "References";
  private final static String __SUBJECT_CATEGORIES = "SubjectCategories";
  private final static String __SOURCE_IDENTIFIER = "SourceID";

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  
  
  /**
   * Creates a {@link CsvLoader} object using a file as the data source. Uses 
   * a comma as the column delimiter and ISO-8859-1 as the {@link java.nio.charset.Charset Charset}.
   * 
   * @param file The file to import
   */
  public CsvLoader(String file, boolean importReferences) {
    this.file = file;
    this.delimiter = ',';
    this.importReferences = importReferences;
  }
  
  /**
   * 
   * @param file
   * @param delimiter
   * @param importReferences 
   */
  public CsvLoader(String file, char delimiter, boolean importReferences) {
    this.file = file;
    this.delimiter = delimiter;
    this.importReferences = importReferences;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @param kbm
   * @throws LoaderException
   * @throws KnowledgeBaseException 
   */
  @Override
  public void execute(KnowledgeBaseManager kbm) throws LoaderException, KnowledgeBaseException {

    CsvReader records;
    
    try {
      
      records = new CsvReader(file, delimiter);
      //records = new CsvReader(file, delimiter, StandardCharsets.UTF_8);
      
      records.readHeaders();
      
      records.setEscapeMode(CsvReader.ESCAPE_MODE_DOUBLED);

      addRecordToKnowledgeBase(records, kbm);

      kbm.commit();
      
      records.close();
    
      CurrentProject.getInstance().getKbObserver().fireKnowledgeBaseRefresh();

    } catch (FileNotFoundException fe) {
      
      throw new LoaderException(fe); 
      
    } catch (IOException ioe) {
      
      throw new LoaderException(ioe);
      
    } catch (KnowledgeBaseException e) {

      try {

        kbm.getConnection().rollback();

      } catch (SQLException s) {

        throw  new KnowledgeBaseException(s);
      }

      throw e;

    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

  /**
   *
   * @param records
   * @param kbm
   * @throws KnowledgeBaseException
   */
  private void addRecordToKnowledgeBase(CsvReader records,
          KnowledgeBaseManager kbm) throws KnowledgeBaseException, IOException {

    int i;
    Integer documentID, journalID, publishDateID;

    // Build the DAOs
    AffiliationDAO affiliationDAO = new AffiliationDAO(kbm);
    AuthorDAO authorDAO = new AuthorDAO(kbm);
    AuthorReferenceDAO authorReferenceDAO = new AuthorReferenceDAO(kbm);
    AuthorReferenceReferenceDAO authorReferenceReferenceDAO = new AuthorReferenceReferenceDAO(kbm);
    DocumentAffiliationDAO documentAffiliationDAO = new DocumentAffiliationDAO(kbm);
    DocumentAuthorDAO documentAuthorDAO = new DocumentAuthorDAO(kbm);
    DocumentDAO documentDAO = new DocumentDAO(kbm);
    DocumentReferenceDAO documentReferenceDAO = new DocumentReferenceDAO(kbm);
    DocumentWordDAO documentWordDAO = new DocumentWordDAO(kbm);
    JournalDAO journalDAO = new JournalDAO(kbm);
    JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO = new JournalSubjectCategoryPublishDateDAO(kbm);
    PublishDateDAO publishDateDAO = new PublishDateDAO(kbm);
    ReferenceDAO referenceDAO = new ReferenceDAO(kbm);
    ReferenceSourceDAO referenceSourceDAO = new ReferenceSourceDAO(kbm);
    SubjectCategoryDAO subjectCategoryDAO = new SubjectCategoryDAO(kbm);
    WordDAO wordDAO = new WordDAO(kbm);

    i = 0;
    
    while (records.readRecord()) {
    
      System.out.println("Record " + (i + 1));
      
      // Add document
      //System.out.println("Add document...");
      documentID = addDocument(records, documentDAO);
      
      if (documentID != null) {
      
        // Add authors and associate with the document
        addAuthor(documentID, records, authorDAO, documentAuthorDAO);

        // Add affiliations and associate with the document
        addAffiliation(documentID, records, affiliationDAO, documentAffiliationDAO);

        // Add journal and associate with the document
        journalID = addJournal(documentID, records, journalDAO, documentDAO);

        // Add publishDate and associate with the document
        publishDateID = addPublishDate(documentID, records, publishDateDAO, documentDAO);

        if (this.importReferences) {
        
          // Add references, reference source and reference author.
          addReference(documentID, records, referenceDAO, documentReferenceDAO,
                  authorReferenceDAO, referenceSourceDAO, authorReferenceReferenceDAO);
        }

        addSubjectCategory(journalID, publishDateID, records,
                subjectCategoryDAO, journalSubjectCategoryPublishDateDAO);

        // Add author's word and associate with the document
        addWord(true, documentID, records, wordDAO, documentWordDAO);

        // Add source's word and associate with the document
        addWord(false, documentID, records, wordDAO, documentWordDAO);
      }
      
      i++;
    }
  }

  /**
   * 
   * @param records
   * @param documentDAO
   * @return
   * @throws KnowledgeBaseException
   */
  private Integer addDocument(CsvReader records, DocumentDAO documentDAO)
          throws KnowledgeBaseException, IOException {

    String title, docAbstract, type, doi, sourceIdentifier, volume, issue, beginPage, endPage;
    Integer documentID;
    int citations;

    title = records.get(__TITLE).replaceAll("\n", " ");
    
    if (! title.isEmpty()) {
    
      docAbstract = records.get(__ABSTRACT);
      
      if (! docAbstract.isEmpty()) {

        docAbstract = docAbstract.replaceAll("\n", " ");
        
      } else {

        docAbstract = null;
      }

      type = records.get(__TYPE);
      doi = records.get(__DOI);
      sourceIdentifier = records.get(__SOURCE_IDENTIFIER);
      volume = records.get(__VOLUME);
      issue = records.get(__ISSUE);
      beginPage = records.get(__BEGIN_PAGE);
      endPage = records.get(__END_PAGE);

      if (! records.get(__CITATIONS).isEmpty()) {

        citations = Integer.valueOf(records.get(__CITATIONS));

      } else {

        citations = 0;
      }

      documentID = documentDAO.addDocument(title, docAbstract, type,
              citations, doi, sourceIdentifier, volume, issue, beginPage, endPage, false);
      
    } else {
    
      documentID = null;
    }
    
    return documentID;
  }

  /**
   * 
   * @param documentID
   * @param record
   * @param authorDAO
   * @param documentAuthorDAO
   * @throws KnowledgeBaseException
   */
  private void addAuthor(Integer documentID, CsvReader record,
          AuthorDAO authorDAO, DocumentAuthorDAO documentAuthorDAO)
          throws KnowledgeBaseException, IOException {

    int i;
    String authorNames;
    String authorName;
    String[] splitAuthorName;
    Author author;
    Integer authorID;

    authorNames = record.get(__AUTHORS);

    if (! authorNames.isEmpty()) {

      splitAuthorName = authorNames.split("//");

      for (i = 0; i < splitAuthorName.length; i++) {

        authorName = splitAuthorName[i];
        
        author = authorDAO.getAuthor(authorName, "");

        if (author == null) {

          authorID = authorDAO.addAuthor(authorName, "", false);

        } else {

          authorID = author.getAuthorID();
        }

        if (! documentAuthorDAO.checkDocumentAuthor(documentID, authorID)) {

          documentAuthorDAO.addDocumentAuthor(documentID, authorID, i + 1, false);
        }
      }
    }
  }

  /**
   * 
   * @param documentID
   * @param record
   * @param affiliationDAO
   * @param documentAffiliationDAO
   * @throws KnowledgeBaseException
   */
  private void addAffiliation(Integer documentID, CsvReader record,
          AffiliationDAO affiliationDAO, DocumentAffiliationDAO documentAffiliationDAO)
          throws KnowledgeBaseException, IOException {

    int i;
    String fullAffiliations, fullAffiliation;
    String[] splitFullAffiliation;
    Affiliation affiliation;
    Integer affiliationID;

    fullAffiliations = record.get(__AFFILIATIONS);

    if (! fullAffiliations.isEmpty()) {

      splitFullAffiliation = fullAffiliations.split("//");

      for (i = 0; i < splitFullAffiliation.length; i++) {

        fullAffiliation = splitFullAffiliation[i];

        affiliation = affiliationDAO.getAffiliation(fullAffiliation);

        if (affiliation == null) {

          affiliationID = affiliationDAO.addAffiliation(fullAffiliation, false);

        } else {

          affiliationID = affiliation.getAffiliationID();
        }

        if (! documentAffiliationDAO.checkDocumentAffiliation(documentID, affiliationID)) {

          documentAffiliationDAO.addDocumentAffiliation(documentID, affiliationID, false);
        }
      }
    }
  }

  /**
   * 
   * @param documentID
   * @param record
   * @param journalDAO
   * @param documentDAO
   * @throws KnowledgeBaseException
   */
  private Integer addJournal(Integer documentID, CsvReader record,
          JournalDAO journalDAO, DocumentDAO documentDAO)
          throws KnowledgeBaseException, IOException {

    String source;
    Journal journal;
    Integer journalID = null;

    source = record.get(__JOURNAL);

    if (! source.isEmpty()) {

      journal = journalDAO.getJournal(source);

      if (journal == null) {

        journalID = journalDAO.addJournal(source, "", false);

      } else {

        journalID = journal.getJournalID();
      }

      documentDAO.setJournal(documentID, journalID, false);
    }

    return journalID;
  }

  /**
   * 
   * @param documentID
   * @param record
   * @param publishDateDAO
   * @param documentDAO
   * @return
   * @throws KnowledgeBaseException
   */
  private Integer addPublishDate(Integer documentID, CsvReader record,
          PublishDateDAO publishDateDAO, DocumentDAO documentDAO)
          throws KnowledgeBaseException, IOException {

    String year, date;
    PublishDate publishDate;
    Integer publishDateID = null;

    year = record.get(__YEAR);
    date = record.get(__FULL_PUBLISH_DATE);

    if (! year.isEmpty()) {

      publishDate = publishDateDAO.getPublishDate(year, date);

      if (publishDate == null) {

        publishDateID = publishDateDAO.addPublishDate(year, date, false);

      } else {

        publishDateID = publishDate.getPublishDateID();
      }

      documentDAO.setPublishDate(documentID, publishDateID, false);
    }

    return publishDateID;
  }

  /**
   * 
   * @param documentID
   * @param journalID
   * @param publishDateID
   * @param records
   * @param subjectCategoryDAO
   * @param journalSubjectCategoryPublishDateDAO
   * @throws KnowledgeBaseException
   */
  private void addSubjectCategory(Integer journalID,
          Integer publishDateID, CsvReader records,
          SubjectCategoryDAO subjectCategoryDAO, 
          JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO)
          throws KnowledgeBaseException, IOException {

    int i;
    String subjectCategoryNames, subjectCategoryName;
    String[] splitSC;
    SubjectCategory subjectCategory;
    Integer subjectCategoryID = null;

    subjectCategoryNames = records.get(__SUBJECT_CATEGORIES);

    if (! subjectCategoryNames.isEmpty()) {

      splitSC = subjectCategoryNames.split("//");

      for (i = 0; i < splitSC.length; i++) {

        subjectCategoryName = splitSC[i].trim();

        subjectCategory = subjectCategoryDAO.getSubjectCategory(subjectCategoryName);

        if (subjectCategory == null) {

          subjectCategoryID = subjectCategoryDAO.addSubjectCategory(subjectCategoryName, false);

        } else {

          subjectCategoryID = subjectCategory.getSubjectCategoryID();
        }

        if ((journalID != null) && (publishDateID != null) &&
            (! journalSubjectCategoryPublishDateDAO.checkJournalSubjectCategoryPublishDate(journalID, subjectCategoryID, publishDateID))) {

          journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(subjectCategoryID, journalID, publishDateID, false);
        }
      }
    }
  }

  /**
   *
   * @param documentID
   * @param record
   * @param referenceDAO
   * @param documentReferenceDAO
   * @param authorReferenceDAO
   * @param referenceSourceDAO
   * @param authorReferenceReferenceDAO
   * @throws KnowledgeBaseException
   */
  private void addReference(Integer documentID, CsvReader record,
          ReferenceDAO referenceDAO, DocumentReferenceDAO documentReferenceDAO,
          AuthorReferenceDAO authorReferenceDAO, ReferenceSourceDAO referenceSourceDAO,
          AuthorReferenceReferenceDAO authorReferenceReferenceDAO)
          throws KnowledgeBaseException, IOException {

    int i, j;
    String[] splitField, splitReference;
    String references, fullReference, volume, pages, doi, year, authorName, source;
    Reference reference;
    AuthorReference authorReference;
    ReferenceSource referenceSource;
    Integer referenceID, authorReferenceID, referenceSourceID;

    references = record.get(__REFERENCES);

    if (! references.isEmpty()) {

      splitField = references.split("//");

      for (i = 0; i < splitField.length; i++) {

        fullReference = splitField[i];

        reference = referenceDAO.getReference(fullReference);

        if (reference == null) {

          splitReference = fullReference.split(",");

          authorName = splitReference[0];

          if (splitReference.length >= 3) {

            year = splitReference[1].trim();
            source = splitReference[2].trim();

          } else {

            year = "";
            source = "";
          }

          if (splitReference.length >= 5) {

            volume = splitReference[3].trim();

            if (!volume.startsWith("V")) {

              volume = "";
            }

            pages = splitReference[4].trim();

            if (!pages.startsWith("P")) {

              pages = "";
            }

          } else {

            volume = "";
            pages = "";
          }

          if (splitReference.length >= 6) {

            doi = splitReference[5].trim();

            if (!doi.startsWith("DOI")) {

              doi = "";
            }

          } else {

            doi = "";
          }

          referenceID = referenceDAO.addReference(fullReference, volume, "", pages, year, doi, "ISIWoS-1.0", false);

          authorReference = authorReferenceDAO.getAuthorReference(authorName);

          if (authorReference == null) {

            authorReferenceID = authorReferenceDAO.addAuthorReference(authorName, false);

          } else {

            authorReferenceID = authorReference.getAuthorReferenceID();
          }

          if (! authorReferenceReferenceDAO.checkAuthorReferenceReference(authorReferenceID, referenceID)) {

            authorReferenceReferenceDAO.addAuthorReferenceReference(referenceID, authorReferenceID, 1, false);
          }

          referenceSource = referenceSourceDAO.getReferenceSource(source);

          if (referenceSource == null) {

            referenceSourceID = referenceSourceDAO.addReferenceSource(source, false);

          } else {

            referenceSourceID = referenceSource.getReferenceSourceID();
          }

          referenceDAO.setReferenceSource(referenceID, referenceSourceID, false);

        } else {

          referenceID = reference.getReferenceID();
        }

        if (! documentReferenceDAO.checkDocumentReference(documentID, referenceID)) {

          documentReferenceDAO.addDocumentReference(documentID, referenceID, false);
        }
      }
    }
  }

  /**
   *
   * @param authorWord a true value indactes that the word to add have to be
   *                   the authors words, otherwise, the source words will be added.
   * @param documentID
   * @param record
   * @param wordDAO
   * @param documentWordDAO
   * @throws KnowledgeBaseException
   */
  private void addWord(boolean authorWord, Integer documentID, CsvReader record,
          WordDAO wordDAO, DocumentWordDAO documentWordDAO)
          throws KnowledgeBaseException, IOException {

    int i;
    String wordNames, wordName;
    String[] splitWordName;
    Word word;
    Integer wordID;
    DocumentWord documentWord;

    wordNames = null;

    if (authorWord) {

      wordNames = record.get(__AUTHOR_KEYWORDS);

    } else {

      wordNames = record.get(__SOURCE_KEYWORDS);
    }

    if (! wordNames.isEmpty()) {

      splitWordName = wordNames.split("//");

      for (i = 0; i < splitWordName.length; i++) {

        wordName = splitWordName[i].trim().toUpperCase().replaceAll(" ", "-");

        word = wordDAO.getWord(wordName);

        if (word == null) {

          wordID = wordDAO.addWord(wordName, false);

        } else {

          wordID = word.getWordID();
        }

        documentWord = documentWordDAO.getDocumentWord(documentID, wordID);

        if (documentWord != null) {

          if (authorWord) {

            if (!documentWord.isAuthorKeyword()) {
              documentWordDAO.setAuthorWord(documentID, wordID, true, false);
            }

          } else {

            if (!documentWord.isSourceKeyword()) {
              documentWordDAO.setSourceWord(documentID, wordID, true, false);
            }
          }

        } else {

          if (authorWord) {

            documentWordDAO.addDocumentWord(documentID, wordID, true, false, false, false);

          } else {

            documentWordDAO.addDocumentWord(documentID, wordID, false, true, false, false);
          }
        }
      }
    }
  }

  private void showRecord(CsvReader records) {

    int i;

    try {
      
      while (records.readRecord()) {
        
        System.out.println("Header1: " + records.get("header1"));
    
//        for (i = 0; i < records.getColumnCount(); i++) {
//        
//          System.out.println(records.getHeader(i) + ": " + records.get(i));
//        }
      }
    
    } catch (IOException ioe) {
    
      ioe.printStackTrace(System.err);
    }
  }
}
