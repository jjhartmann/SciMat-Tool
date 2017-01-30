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
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class RISLoader implements GenericLoader {

  /**
   * ************************************************************************
   */
  /*                        Private attributes                               */
  /**
   * ************************************************************************
   */
  private String file;
  private boolean importReferences;

  /**************************************************************************
  /*                            Constructors                                */
  /**************************************************************************/
  /**
   *
   * @param file The file to import
   * @param importReferences true if the references must be imported
   */
  public RISLoader(String file, boolean importReferences) {
    this.file = file;
    this.importReferences = importReferences;
  }

  /**************************************************************************/
  /*                           Public Methods                               */
  /**************************************************************************/
  
  
  /**
   * 
   * @param kbm
   * @throws LoaderException
   * @throws KnowledgeBaseException 
   */
  @Override
  public void execute(KnowledgeBaseManager kbm) throws LoaderException, KnowledgeBaseException {

    ArrayList<TreeMap<String, String>> records;

    records = loadRecords();

    try {

      addRecordToKnowledgeBase(records, kbm);

      kbm.commit();

      CurrentProject.getInstance().getKbObserver().fireKnowledgeBaseRefresh();

    } catch (KnowledgeBaseException e) {

      try {

        kbm.getConnection().rollback();

      } catch (SQLException s) {

        throw new KnowledgeBaseException(s);
      }

      kbm.close();

      throw e;

    }
  }

  /**
   * ************************************************************************
   */
  /*                           Private Methods                               */
  /**
   * ************************************************************************
   */
  /**
   *
   * @return @throws LoaderException
   */
  private ArrayList<TreeMap<String, String>> loadRecords() throws LoaderException {

    int position;
    BufferedReader input = null;
    String line, field, fieldKey, tmpField;
    ArrayList<TreeMap<String, String>> records = new ArrayList<TreeMap<String, String>>();
    TreeMap<String, String> record;

    try {

      input = new BufferedReader(new FileReader(this.file));

      record = new TreeMap<String, String>();
      fieldKey = null;

      while ((line = input.readLine()) != null) {

        if (line.matches("\\w{2}  - .*")) {

          fieldKey = line.substring(0, 2);
          field = line.substring(6);

          if (fieldKey.equals("ER")) {

            records.add(record);
            record = new TreeMap<String, String>();

          } else {

            if (fieldKey.startsWith("N1")) {

              position = field.indexOf(":");

              if (position >= 0) {

                fieldKey = field.substring(0, position);

                field = field.substring(position + 1).trim();
              }
            }

            tmpField = record.get(fieldKey);

            if (tmpField != null) {

              tmpField += "\n" + field;

            } else {

              tmpField = field;
            }

            record.put(fieldKey, tmpField);

          }

        } else if (fieldKey != null) {

          tmpField = record.get(fieldKey);

          if (tmpField != null) {

            tmpField += "\n" + line;

          } else {

            tmpField = line;
          }

          record.put(fieldKey, tmpField);
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

      System.out.println("Record " + (i + 1));

      record = records.get(i);

      // Add document
      //System.out.println("Add document...");
      documentID = addDocument(record, documentDAO);

      // Add authors and associate with the document
      addAuthor(documentID, record, authorDAO, documentAuthorDAO);

      // Add affiliations and associate with the document
      addAffiliation(documentID, record, affiliationDAO, documentAffiliationDAO);

      // Add journal and associate with the document
      journalID = addJournal(documentID, record, journalDAO, documentDAO);

      // Add publishDate and associate with the document
      publishDateID = addPublishDate(documentID, record, publishDateDAO, documentDAO);

      if (this.importReferences) {
        // Add references, reference source and reference author.
        addReference(documentID, record, referenceDAO, documentReferenceDAO,
                authorReferenceDAO, referenceSourceDAO, authorReferenceReferenceDAO);
      }

      /*addSubjectCategory(journalID, publishDateID, record,
       subjectCategoryDAO, journalSubjectCategoryPublishDateDAO);*/
      // Add word and associate with the document
      addWord(documentID, record, wordDAO, documentWordDAO);
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

    String title, docAbstract, type, doi, sourceIdentifier, volume, issue, beginPage, endPage, citationString;
    int citations;

    title = record.get("T1");
    docAbstract = record.get("AB");
    type = record.get("TY");
    doi = record.get("doi");
    sourceIdentifier = record.get("UR");
    volume = record.get("VL");
    issue = record.get("IS");
    beginPage = record.get("SP");
    endPage = record.get("EP");

    citationString = record.get("Cited By (since 1996)");

    citations = 0;

    if (citationString != null) {

      try {

        citations = Integer.valueOf(citationString);

      } catch (NumberFormatException nfe) {

        System.err.println("Error reading citations. The field" + citationString + " does not match.");
      }

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
    String authorNames;
    String authorName;
    String[] splitAuthorName;
    Author author;
    Integer authorID;

    authorNames = record.get("AU");

    if (authorNames != null) {

      splitAuthorName = authorNames.split("\n");

      for (i = 0; i < splitAuthorName.length; i++) {

        if (splitAuthorName != null) {

          authorName = splitAuthorName[i];

        } else {

          authorName = splitAuthorName[i];
        }

        author = authorDAO.getAuthor(authorName, "");

        if (author == null) {

          authorID = authorDAO.addAuthor(authorName, "", false);

        } else {

          authorID = author.getAuthorID();
        }

        if (!documentAuthorDAO.checkDocumentAuthor(documentID, authorID)) {

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

    fullAffiliations = record.get("AD");

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

        if (!documentAffiliationDAO.checkDocumentAffiliation(documentID, affiliationID)) {

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

    String source;
    Journal journal;
    Integer journalID = null;

    source = record.get("JF");

    if (source != null) {

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
  private Integer addPublishDate(Integer documentID, TreeMap<String, String> record,
          PublishDateDAO publishDateDAO, DocumentDAO documentDAO)
          throws KnowledgeBaseException {

    String date, year;
    PublishDate publishDate;
    Integer publishDateID = null;

    date = record.get("PY");

    if (date != null) {

      date = date.trim();

      if (!date.isEmpty()) {
        year = date.substring(0, 4);

      // REVISAR
        // Que no falle si el campo fecha es menor que 3
        publishDate = publishDateDAO.getPublishDate(year, date);

        if (publishDate == null) {

          publishDateID = publishDateDAO.addPublishDate(year, date, false);

        } else {

          publishDateID = publishDate.getPublishDateID();
        }

        documentDAO.setPublishDate(documentID, publishDateID, false);
      }
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
  /*private void addSubjectCategory(Integer journalID,
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

   subjectCategoryID = subjectCategoryDAO.addSubjectCategory(subjectCategoryName);

   } else {

   subjectCategoryID = subjectCategory.getSubjectCategoryID();
   }

   if ((journalID != null) && (publishDateID != null) &&
   (! journalSubjectCategoryPublishDateDAO.checkJournalSubjectCategoryPublishDate(journalID, subjectCategoryID, publishDateID))) {

   journalSubjectCategoryPublishDateDAO.addSubjectCategoryToJournal(subjectCategoryID, journalID, publishDateID);
   }
   }
   }
   }*/
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
    String field, fullReference, volume, issue, pages, doi, year, authorName, source;
    Reference reference;
    AuthorReference authorReference;
    ReferenceSource referenceSource;
    Integer referenceID, authorReferenceID, referenceSourceID;

    field = record.get("References");

    if (field != null) {

      splitField = record.get("References").split(";");

      for (i = 0; i < splitField.length; i++) {

        fullReference = splitField[i].trim();

        /*if (fullReference.endsWith(", DOI")) {

         i++;
         fullReference += splitField[i];
         }*/
        reference = referenceDAO.getReference(fullReference);

        if (reference == null) {

          /*splitReference = fullReference.split(",");

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
           }*/
          referenceID = referenceDAO.addReference(fullReference, "", "", "", "", "", "RIS", false);

          /*authorReference = authorReferenceDAO.getAuthorReference(authorName);

           if (authorReference == null) {

           authorReferenceID = authorReferenceDAO.addAuthorReference(authorName);

           } else {

           authorReferenceID = authorReference.getAuthorReferenceID();
           }

           if (! authorReferenceReferenceDAO.checkAuthorReferenceReference(authorReferenceID, referenceID)) {

           authorReferenceReferenceDAO.addAuthorReferenceReference(referenceID, authorReferenceID, 1);
           }

           referenceSource = referenceSourceDAO.getReferenceSource(source);

           if (referenceSource == null) {

           referenceSourceID = referenceSourceDAO.addReferenceSource(source);

           } else {

           referenceSourceID = referenceSource.getReferenceSourceID();
           }

           referenceDAO.setReferenceSource(referenceID, referenceSourceID);*/
        } else {

          referenceID = reference.getReferenceID();
        }

        if (!documentReferenceDAO.checkDocumentReference(documentID, referenceID)) {

          documentReferenceDAO.addDocumentReference(documentID, referenceID, false);
        }
      }
    }
  }

  /**
   *
   * @param authorWord a true value indactes that the word to add have to be the
   * authors words, otherwise, the source words will be added.
   * @param documentID
   * @param record
   * @param wordDAO
   * @param documentWordDAO
   * @throws KnowledgeBaseException
   */
  private void addWord(Integer documentID, TreeMap<String, String> record,
          WordDAO wordDAO, DocumentWordDAO documentWordDAO)
          throws KnowledgeBaseException {

    int i;
    String wordNames, wordName;
    String[] splitWordName;
    Word word;
    Integer wordID;
    DocumentWord documentWord;

    wordNames = null;

    if (record.containsKey("KW")) {

      wordNames = record.get("KW");
    }

    if (wordNames != null) {

      splitWordName = wordNames.replaceAll(" +", "-").toUpperCase().split("\n");

      for (i = 0; i < splitWordName.length; i++) {

        wordName = splitWordName[i].trim();

        word = wordDAO.getWord(wordName);

        if (word == null) {

          wordID = wordDAO.addWord(wordName, false);

        } else {

          wordID = word.getWordID();
        }

        documentWord = documentWordDAO.getDocumentWord(documentID, wordID);

        if (documentWord == null) {

          documentWordDAO.addDocumentWord(documentID, wordID, true, false, false, false);

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
}
