/*
 * ISIWoSLoader.java
 *
 * Created on 11-ene-2011, 13:46:29
 */
package scimat.api.loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
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
 *
 * @author mjcobo
 */
public class ISIWoSLoader implements GenericLoader {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private String file;
  private boolean importReferences;
  
  private long affiliationTime, authorTime, documentTime, journalTime, subjectCategoryTime, authorWordTime, sourceWordTime, referenceTime, publishDateTime;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param file The file to import
   * @param importReferences true if the references must be imported
   * @param kbm The knowledge base where the data will be imported.
   */
  public ISIWoSLoader(String file, boolean importReferences) {
    this.file = file;
    this.importReferences = importReferences;
    
    affiliationTime = authorTime = documentTime = journalTime = subjectCategoryTime = authorWordTime = sourceWordTime = referenceTime = publishDateTime = 0;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public void execute(KnowledgeBaseManager kbm) throws LoaderException, KnowledgeBaseException {

    ArrayList<TreeMap<String, String>> records;

    records = loadRecords();

    try {

    addRecordToKnowledgeBase(records, kbm);

    kbm.commit();
    
    printTime(records.size());
    
    CurrentProject.getInstance().getKbObserver().fireKnowledgeBaseRefresh();

    } catch (KnowledgeBaseException e) {

      e.printStackTrace(System.err);
      
      try {

        kbm.getConnection().rollback();

      } catch (SQLException s) {

        s.printStackTrace(System.err);
        
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
   * @return
   * @throws LoaderException
   */
  private ArrayList<TreeMap<String, String>> loadRecords() throws LoaderException {

    BufferedReader input = null;
    String line, field, currentFieldKey, fieldKeyToAdd;
    ArrayList<TreeMap<String, String>> records = new ArrayList<TreeMap<String, String>>();
    TreeMap<String, String> record;

    try {

      input = new BufferedReader(new FileReader(this.file));

      line = input.readLine();

      
      //if ((line == null) || (! line.equals("FN ISI Export Format"))) {
      /*if ((line == null) || (! line.equals("FN Thomson Reuters Web of Knowledge"))) {

        throw new LoaderException("Incorrect file format.");
      }*/

      line = input.readLine();

      /*if ((line == null) || (! line.equals("VR 1.0"))) {

        throw new LoaderException("Incorrect file format version.");
      }*/

      record = new TreeMap<String, String>();
      field = "";
      fieldKeyToAdd = "";

      while ((line = input.readLine()) != null) {

        if (line.length() > 1) {

          currentFieldKey = line.substring(0, 2);

          if (currentFieldKey.equals("ER")) {

            record.put(fieldKeyToAdd, field);
            records.add(record);
            record = new TreeMap<String, String>();
            fieldKeyToAdd = "";

          } else if (currentFieldKey.equals("  ")) {

            field += "\n" + line.substring(3);

          } else if (! currentFieldKey.equals("EF")) {

            if (! fieldKeyToAdd.isEmpty()) {

              record.put(fieldKeyToAdd, field);
            }

            fieldKeyToAdd = currentFieldKey;
            field = line.substring(3);
            
          } else {

            //System.out.println("end file");
          }
        }

      }

    } catch (FileNotFoundException fnfe) {

      throw new LoaderException(fnfe.getMessage(), fnfe);

    } catch (IOException ioe) {

      throw new LoaderException(ioe.getMessage(), ioe);

    } finally {

      if (input != null) {

        try {
          input.close();

        } catch (IOException ioe) {

          throw new LoaderException(ioe.getMessage(), ioe);
        }
      }
    }

    return records;
  }

  /**
   *
   * @param records
   * @param kbm
   * @throws KnowledgeBaseException
   */
  private void addRecordToKnowledgeBase(ArrayList<TreeMap<String, String>> records,
          KnowledgeBaseManager kbm) throws KnowledgeBaseException {

    int i;
    Integer documentID, journalID, publishDateID;
    TreeMap<String, String> record;
    long time, intermediateTime;

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

    for (i = 0; i < records.size(); i++) {

//      System.out.println("Record " + (i + 1));
      time = System.currentTimeMillis();

      record = records.get(i);

      // Add document
//      System.out.print("  Add document...\t\t\t\t\t");
      intermediateTime = System.currentTimeMillis();
      documentID = addDocument(record, documentDAO);
      this.documentTime += System.currentTimeMillis() - intermediateTime;
//      System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

      // Add authors and associate with the document
//      System.out.print("  Add authors and associate with the document...\t\t");
      intermediateTime = System.currentTimeMillis();
      addAuthor(documentID, record, authorDAO, documentAuthorDAO);
      this.authorTime += System.currentTimeMillis() - intermediateTime;
//      System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

      // Add affiliations and associate with the document
//      System.out.print("  Add affiliations and associate with the document...\t\t" );
      intermediateTime = System.currentTimeMillis();
      addAffiliation(documentID, record, affiliationDAO, documentAffiliationDAO);
      this.affiliationTime += System.currentTimeMillis() - intermediateTime;
//      System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

      // Add journal and associate with the document
//      System.out.print("  Add affiliations and associate with the document...\t\t");
      intermediateTime = System.currentTimeMillis();
      journalID = addJournal(documentID, record, journalDAO, documentDAO);
      this.journalTime += System.currentTimeMillis() - intermediateTime;
//      System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

      // Add publishDate and associate with the document
//      System.out.print("  Add publishDate and associate with the document...\t\t");
      intermediateTime = System.currentTimeMillis();
      publishDateID = addPublishDate(documentID, record, publishDateDAO, documentDAO);
      this.publishDateTime += System.currentTimeMillis() - intermediateTime;
//      System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

      if (this.importReferences) {

//        // Add references, reference source and reference author.
//        System.out.println("  Add references, r. source, r. author...");
        intermediateTime = System.currentTimeMillis();
        addReference(documentID, record, referenceDAO, documentReferenceDAO,
                authorReferenceDAO, referenceSourceDAO, authorReferenceReferenceDAO);
        this.referenceTime += System.currentTimeMillis() - intermediateTime;
//        System.out.println("  Total: " + (System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));
      }

      // Add subjectCategory and associate with journal and publishDate
//      System.out.print("  Add subjectCategory and ass. journal and publishDate...\t");
      /*intermediateTime = System.currentTimeMillis();
      addSubjectCategory(journalID, publishDateID, record,
              subjectCategoryDAO, journalSubjectCategoryPublishDateDAO);
      this.subjectCategoryTime += System.currentTimeMillis() - intermediateTime;*/
//      System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

      // Add author's word and associate with the document
//      System.out.print("  Add author's word and associate with the document...\t\t");
      intermediateTime = System.currentTimeMillis();
      addWord(true, documentID, record, wordDAO, documentWordDAO);
      this.authorWordTime += System.currentTimeMillis() - intermediateTime;
//      System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

      // Add source's word and associate with the document
//      System.out.print("  Add source's word and associate with the document...\t\t");
      intermediateTime = System.currentTimeMillis();
      addWord(false, documentID, record, wordDAO, documentWordDAO);
      this.sourceWordTime += System.currentTimeMillis() - intermediateTime;
//      System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));
    }
  }

  /**
   * 
   * @param record
   * @param documentDAO
   * @return
   * @throws KnowledgeBaseException
   */
  private Integer addDocument(TreeMap<String, String> record, DocumentDAO documentDAO)
          throws KnowledgeBaseException {

    String title, docAbstract, type, doi, sourceIdentifier, volume, issue, beginPage, endPage;
    int citations;

    title = record.get("TI").replaceAll("\n", " ");
    
    if (record.get("AB") != null) {
      
      docAbstract = record.get("AB").replaceAll("\n", " ");
    } else {
    
      docAbstract = null;
    }
      
    type = record.get("PT");
    doi = record.get("DI");
    sourceIdentifier = record.get("UT");
    volume = record.get("VL");
    issue = record.get("IS");
    beginPage = record.get("BP");
    endPage = record.get("EP");

    if (record.get("TC") != null) {

      citations = Integer.valueOf(record.get("TC"));
      
    } else {

      citations = 0;
    }

    return documentDAO.addDocument(title, docAbstract, type,
            citations, doi, sourceIdentifier, volume, issue, beginPage, endPage, false);
  }

  /**
   * 
   * @param documentID
   * @param record
   * @param authorDAO
   * @param documentAuthorDAO
   * @throws KnowledgeBaseException
   */
  private void addAuthor(Integer documentID, TreeMap<String, String> record,
          AuthorDAO authorDAO, DocumentAuthorDAO documentAuthorDAO)
          throws KnowledgeBaseException {

    int i;
    String authorNames, fullAuthorNames;
    String authorName, fullAuthorName;
    String[] splitAuthorName, splitFullAuthorName;
    Author author;
    Integer authorID;

    authorNames = record.get("AU");
    fullAuthorNames = record.get("AF");

    if (authorNames != null) {

      splitAuthorName = authorNames.split("\n");
      splitFullAuthorName = null;
      
      if (fullAuthorNames != null) {

        splitFullAuthorName = fullAuthorNames.split("\n");

      }

      for (i = 0; i < splitAuthorName.length; i++) {

        if (splitFullAuthorName != null) {

          authorName = splitAuthorName[i];
          fullAuthorName = splitFullAuthorName[i];
        
        } else {

          authorName = splitAuthorName[i];
          fullAuthorName = "";
        }
        
        author = authorDAO.getAuthor(authorName, fullAuthorName);

        if (author == null) {

          authorID = authorDAO.addAuthor(authorName, fullAuthorName, false);

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
  private void addAffiliation(Integer documentID, TreeMap<String, String> record,
          AffiliationDAO affiliationDAO, DocumentAffiliationDAO documentAffiliationDAO)
          throws KnowledgeBaseException {

    int i;
    String fullAffiliations, fullAffiliation;
    String[] splitFullAffiliation;
    Affiliation affiliation;
    Integer affiliationID;
    
    fullAffiliations = record.get("C1");

    if (fullAffiliations != null) {

      splitFullAffiliation = fullAffiliations.split("\n");

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
  private Integer addJournal(Integer documentID, TreeMap<String, String> record,
          JournalDAO journalDAO, DocumentDAO documentDAO)
          throws KnowledgeBaseException {

    String source, conferenceInformation;
    Journal journal;
    Integer journalID = null;

    source = record.get("SO");
    conferenceInformation = record.get("CT");

    if (source != null) {

      journal = journalDAO.getJournal(source);

      if (journal == null) {

        journalID = journalDAO.addJournal(source, conferenceInformation, false);

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
  private Integer addPublishDate(Integer documentID, TreeMap<String, String> record,
          PublishDateDAO publishDateDAO, DocumentDAO documentDAO)
          throws KnowledgeBaseException {

    String year, date;
    PublishDate publishDate;
    Integer publishDateID = null;

    year = record.get("PY");
    date = record.get("PD");

    if (date == null) {

      date = "";
    }

    if (year != null) {

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
   * @param record
   * @param subjectCategoryDAO
   * @param journalSubjectCategoryPublishDateDAO
   * @throws KnowledgeBaseException
   */
  private void addSubjectCategory(Integer journalID,
          Integer publishDateID, TreeMap<String, String> record,
          SubjectCategoryDAO subjectCategoryDAO, 
          JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO)
          throws KnowledgeBaseException {

    int i;
    String subjectCategoryNames, subjectCategoryName;
    String[] splitSC;
    SubjectCategory subjectCategory;
    Integer subjectCategoryID = null;

    subjectCategoryNames = record.get("SC");

    if (subjectCategoryNames != null) {

      splitSC = subjectCategoryNames.replaceAll("\n", " ").split(";");

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
  private void addReference(Integer documentID, TreeMap<String, String> record,
          ReferenceDAO referenceDAO, DocumentReferenceDAO documentReferenceDAO,
          AuthorReferenceDAO authorReferenceDAO, ReferenceSourceDAO referenceSourceDAO,
          AuthorReferenceReferenceDAO authorReferenceReferenceDAO)
          throws KnowledgeBaseException {

    int i, j;
    String[] splitField, splitReference;
    String field, fullReference, volume, pages, doi, year, authorName, source;
    Reference reference;
    AuthorReference authorReference;
    ReferenceSource referenceSource;
    Integer referenceID, authorReferenceID, referenceSourceID;
    long time, intermediateTime;

    field = record.get("CR");

    if (field != null) {

      splitField = record.get("CR").split("\n");

      for (i = 0; i < splitField.length; i++) {

//        System.out.println("    Reference " + i);
//        time = System.currentTimeMillis();
        
        fullReference = splitField[i];

        if (fullReference.endsWith(", DOI")) {

          i++;
          fullReference += splitField[i];
        }

//        System.out.print("      Get reference \t\t");
//        intermediateTime = System.currentTimeMillis();
        reference = referenceDAO.getReference(fullReference);
//        System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

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

          //System.out.print("      Add reference \t\t");
          //intermediateTime = System.currentTimeMillis();
          referenceID = referenceDAO.addReference(fullReference, volume, "", pages, year, doi, "ISIWoS-1.0", false);
          //System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

          //System.out.print("      Get A. reference \t\t");
          //intermediateTime = System.currentTimeMillis();
          authorReference = authorReferenceDAO.getAuthorReference(authorName);
          //System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

          if (authorReference == null) {

            //System.out.print("      Add A. reference \t\t");
            //intermediateTime = System.currentTimeMillis();
            authorReferenceID = authorReferenceDAO.addAuthorReference(authorName, false);
            //System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

          } else {

            authorReferenceID = authorReference.getAuthorReferenceID();
          }

          //System.out.print("      Check Ass. R-AR \t\t");
          //intermediateTime = System.currentTimeMillis();
            
          //if (! authorReferenceReferenceDAO.checkAuthorReferenceReference(authorReferenceID, referenceID)) {
            //System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));
            
            //System.out.print("      Add Ass. R-AR \t\t");
            //intermediateTime = System.currentTimeMillis();
            authorReferenceReferenceDAO.addAuthorReferenceReference(referenceID, authorReferenceID, 1, false);
            //System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));
          //}

          //System.out.print("      Get S. reference \t\t");
          //intermediateTime = System.currentTimeMillis();
          referenceSource = referenceSourceDAO.getReferenceSource(source);
          //System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

          if (referenceSource == null) {

            //System.out.print("      Add S. reference \t\t");
            //intermediateTime = System.currentTimeMillis();
            referenceSourceID = referenceSourceDAO.addReferenceSource(source, false);
            //System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

          } else {

            referenceSourceID = referenceSource.getReferenceSourceID();
          }

          //System.out.print("      Add Ass. r-SR \t\t");
          //intermediateTime = System.currentTimeMillis();
          referenceDAO.setReferenceSource(referenceID, referenceSourceID, false);
          //System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));
           

        } else {

          referenceID = reference.getReferenceID();
        }

        //System.out.print("      Check Ass. r-doc \t\t");
        //intermediateTime = System.currentTimeMillis();
        if (! documentReferenceDAO.checkDocumentReference(documentID, referenceID)) {
        
          //System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));

          //System.out.print("      Add Ass. r-doc \t\t");
          //intermediateTime = System.currentTimeMillis();
          documentReferenceDAO.addDocumentReference(documentID, referenceID, false);
          //System.out.println((System.currentTimeMillis() - time) + "\t" + (System.currentTimeMillis() - intermediateTime));
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
  private void addWord(boolean authorWord, Integer documentID, TreeMap<String, String> record,
          WordDAO wordDAO, DocumentWordDAO documentWordDAO)
          throws KnowledgeBaseException {

    int i;
    String wordNames, wordName;
    String[] splitWordName;
    Word word;
    Integer wordID;
    DocumentWord documentWord;

    wordNames = null;

    if (authorWord) {

      if (record.containsKey("DE")) {

        wordNames = record.get("DE").replaceAll("\n", " ");
      }

    } else {

      if (record.containsKey("ID")) {

        wordNames = record.get("ID").replaceAll("\n", " ");
      }
    }

    if (wordNames != null) {

      splitWordName = wordNames.split(";");

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

            documentWordDAO.setAuthorWord(documentID, wordID, true, false);

          } else {

            documentWordDAO.setSourceWord(documentID, wordID, true, false);
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

  private void showRecord(ArrayList<TreeMap<String, String>> records) {

    int i;
    TreeMap<String, String> record;
    String key;
    Iterator<String> it;

    for (i = 0; i < records.size(); i++) {

      record = records.get(i);

      System.out.println("Record " + i);

      it = record.keySet().iterator();

      while (it.hasNext()) {

        key = it.next();

        System.out.println("  " + key + " -> " + record.get(key));
      }
    }

  }
  
  private void printTime(int n) {
  
    System.out.println("  Documents  \t\t" + this.documentTime / (double)n);
    System.out.println("  Authors  \t\t" + this.authorTime / (double)n);
    System.out.println("  Affiliations  \t" + this.affiliationTime / (double)n);
    System.out.println("  Journals  \t\t" + this.journalTime / (double)n);
    System.out.println("  PublishDates  \t" + this.publishDateTime / (double)n);
    System.out.println("  References  \t\t" + this.referenceTime / (double)n);
    System.out.println("  A.Words  \t\t" + this.authorWordTime / (double)n);
    System.out.println("  S.Words  \t\t" + this.sourceWordTime / (double)n);
    System.out.println("  SubjectCategory  \t" + this.subjectCategoryTime / (double)n);
  }
}

