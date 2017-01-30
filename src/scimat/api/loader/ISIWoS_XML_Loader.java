/**
 * *********************************************************************
 *
 * Copyright (C) 2014
 *
 * M.J. Cobo (manueljesus.cobo@uca.es)
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see http://www.gnu.org/licenses/
 *
 *********************************************************************
 */
package scimat.api.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.dao.AuthorDAO;
import scimat.model.knowledgebase.dao.DocumentAuthorDAO;
import scimat.model.knowledgebase.dao.DocumentDAO;
import scimat.model.knowledgebase.dao.DocumentWordDAO;
import scimat.model.knowledgebase.dao.JournalDAO;
import scimat.model.knowledgebase.dao.PublishDateDAO;
import scimat.model.knowledgebase.dao.WordDAO;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.DocumentWord;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class ISIWoS_XML_Loader implements GenericLoader {

  //--------------------------------------------------------------------------
  //                        Private attributes                               
  //--------------------------------------------------------------------------
  private final String filePath;

  //--------------------------------------------------------------------------
  //                            Constructors                                 
  //--------------------------------------------------------------------------
  /**
   *
   * @param filePath
   */
  public ISIWoS_XML_Loader(String filePath) {
    this.filePath = filePath;
  }

  //--------------------------------------------------------------------------
  //                           Public Methods                                
  //--------------------------------------------------------------------------
  @Override
  public void execute(KnowledgeBaseManager kbm) throws LoaderException, KnowledgeBaseException {

    DocumentBuilder db;
    Document records;

    try {

      db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

      records = db.parse(new File(this.filePath));

      addRecordToKnowledgeBase(records, kbm);

      kbm.commit();

      CurrentProject.getInstance().getKbObserver().fireKnowledgeBaseRefresh();

    } catch (FileNotFoundException ex) {

      throw new LoaderException(ex);

    } catch (SAXException | ParserConfigurationException | IOException ex) {

      throw new LoaderException(ex);

    } catch (KnowledgeBaseException e) {

      try {

        kbm.getConnection().rollback();

      } catch (SQLException s) {

        throw new KnowledgeBaseException(s);
      }

      throw e;

    } catch (TransformerException | XPathExpressionException ex) {
      throw new LoaderException(ex);
    }
  }

  //--------------------------------------------------------------------------
  //                           Private Methods                               
  //--------------------------------------------------------------------------
  private void addRecordToKnowledgeBase(Document records,
          KnowledgeBaseManager kbm)
          throws KnowledgeBaseException,
          IOException,
          TransformerException,
          ParserConfigurationException,
          SAXException,
          XPathExpressionException {

    int i;
    NodeList recNodes;
    String rawXML;
    WosXML wosXML;
    Integer documentID, journalID, publishDateID;

    // Build the DAOs
    AuthorDAO authorDAO = new AuthorDAO(kbm);
    DocumentAuthorDAO documentAuthorDAO = new DocumentAuthorDAO(kbm);
    DocumentDAO documentDAO = new DocumentDAO(kbm);
    DocumentWordDAO documentWordDAO = new DocumentWordDAO(kbm);
    JournalDAO journalDAO = new JournalDAO(kbm);
    PublishDateDAO publishDateDAO = new PublishDateDAO(kbm);
    WordDAO wordDAO = new WordDAO(kbm);
    wosXML = new WosXML();
    recNodes = records.getDocumentElement().getElementsByTagName("REC");

    for (i = 0; i < recNodes.getLength(); i++) {

      System.out.println("Record " + (i + 1));

      rawXML = nodeToString(recNodes.item(i));

      wosXML.setXML(rawXML);

      documentID = addDocument(wosXML, documentDAO);

      if (documentID != null) {
      // Add authors and associate with the document
      addAuthor(documentID, wosXML, authorDAO, documentAuthorDAO);

      // Add journal and associate with the document
      journalID = addJournal(documentID, wosXML, journalDAO, documentDAO);

      //        // Add publishDate and associate with the document
      publishDateID = addPublishDate(documentID, wosXML, publishDateDAO, documentDAO);

      // Add author's word and associate with the document
      addAuthorWord(documentID, wosXML, wordDAO, documentWordDAO);

        // Add source's word and associate with the document
      addSourceWord(documentID, wosXML, wordDAO, documentWordDAO);

      }
    }
  }

  private Integer addDocument(WosXML wosXML, DocumentDAO documentDAO)
          throws KnowledgeBaseException, IOException, XPathExpressionException {

    int i;
    String title, docAbstract, type, doi, sourceIdentifier, volume, issue, beginPage, endPage, citationsText;
    Integer documentID;
    int citations;

    title = wosXML.getItemTitle();

    if (!title.isEmpty()) {

      docAbstract = wosXML.getAbstract();

      if (!docAbstract.isEmpty()) {

        docAbstract = docAbstract.replaceAll("\n", " ");

      } else {

        docAbstract = null;
      }

      type = wosXML.getDocType(1);
      
      for (i = 2; i <= wosXML.getDocTypeCount(); i++) {
        
        type += ", " + wosXML.getDocType(i);
      }
      
      doi = wosXML.getDOI();

      if (doi.isEmpty()) {

        doi = wosXML.getXrefDOI();
      }

      sourceIdentifier = wosXML.getUID();
      volume = wosXML.getVolume();
      issue = wosXML.getIssue();
      beginPage = wosXML.getPageBegin();
      endPage = wosXML.getPageEnd();
      citationsText = wosXML.getCitationsCount();

      if (!citationsText.isEmpty()) {

        citations = Integer.valueOf(citationsText);

      } else {

        citations = 0;
      }

//      System.out.println("UID: " + sourceIdentifier);
//      System.out.println("\tTitle: " + title);
//      System.out.println("\tAbstract: " + docAbstract);
//      System.out.println("\tType: " + type);
//      System.out.println("\tCitations: " + citations);
//      System.out.println("\tDoi: " + doi);
//      System.out.println("\tVolume: " + volume);
//      System.out.println("\tIssue: " + issue);
//      System.out.println("\tBegin page: " + beginPage);
//      System.out.println("\tEnd page: " + endPage);
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
  private void addAuthor(Integer documentID, WosXML wosXML,
          AuthorDAO authorDAO, DocumentAuthorDAO documentAuthorDAO)
          throws KnowledgeBaseException, IOException, XPathExpressionException {

    int i;
    String authorName, fullAuthorName;
    Author author;
    Integer authorID;

    for (i = 1; i <= wosXML.getAuthorsCount(); i++) {

      authorName = wosXML.getAuthorWosStandard(i);
      fullAuthorName = wosXML.getAuthorFullName(i);
//      System.out.println("\tAuthor: " + authorName + " - " + fullAuthorName);

      author = authorDAO.getAuthor(authorName, fullAuthorName);

      if (author == null) {

        authorID = authorDAO.addAuthor(authorName, fullAuthorName, false);

      } else {

        authorID = author.getAuthorID();
      }

      if (!documentAuthorDAO.checkDocumentAuthor(documentID, authorID)) {

        documentAuthorDAO.addDocumentAuthor(documentID, authorID, i, false);
      }
    }
  }

  private Integer addJournal(Integer documentID, WosXML wosXML,
          JournalDAO journalDAO, DocumentDAO documentDAO)
          throws KnowledgeBaseException, XPathExpressionException {

    String source;
    Journal journal;
    Integer journalID = null;

    source = wosXML.getSourceTitle();
//    System.out.println("\tJournal: " + source);

    if (!source.isEmpty()) {

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

  private Integer addPublishDate(Integer documentID, WosXML wosXML,
          PublishDateDAO publishDateDAO, DocumentDAO documentDAO)
          throws KnowledgeBaseException, XPathExpressionException {

    String year, date;
    PublishDate publishDate;
    Integer publishDateID = null;

    year = wosXML.getPublicationYear();
    date = wosXML.getPublicationCoverDate();
//    System.out.println("\tDate: " + year + " - " + date);

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

  private void addAuthorWord(Integer documentID, WosXML wosXML,
          WordDAO wordDAO, DocumentWordDAO documentWordDAO)
          throws KnowledgeBaseException, XPathExpressionException {

    int i;
    String wordNames, wordName;
    String[] splitWordName;
    Word word;
    Integer wordID;
    DocumentWord documentWord;

    wordNames = null;

    for (i = 1; i <= wosXML.getAuhorKeywordCount(); i++) {

      wordName = wosXML.getAuthorKeyword(i).trim().toUpperCase().replaceAll(" ", "-");
//      System.out.println("\tWord: " + wordName);

      word = wordDAO.getWord(wordName);

      if (word == null) {

        wordID = wordDAO.addWord(wordName, false);

      } else {

        wordID = word.getWordID();
      }

      documentWord = documentWordDAO.getDocumentWord(documentID, wordID);

      if (documentWord != null) {

        documentWordDAO.setAuthorWord(documentID, wordID, true, false);

      } else {

          documentWordDAO.addDocumentWord(documentID, wordID, true, false, false, false);
      }
    }

  }

  private void addSourceWord(Integer documentID, WosXML wosXML,
          WordDAO wordDAO, DocumentWordDAO documentWordDAO)
          throws KnowledgeBaseException, XPathExpressionException {

    int i;
    String wordNames, wordName;
    String[] splitWordName;
    Word word;
    Integer wordID;
    DocumentWord documentWord;

    wordNames = null;

    for (i = 1; i <= wosXML.getKeywordPlusCount(); i++) {

      wordName = wosXML.getKeywordPlus(i).trim().toUpperCase().replaceAll(" ", "-");
//      System.out.println("\tWord: " + wordName);

      word = wordDAO.getWord(wordName);

      if (word == null) {

        wordID = wordDAO.addWord(wordName, false);

      } else {

        wordID = word.getWordID();
      }

      documentWord = documentWordDAO.getDocumentWord(documentID, wordID);

      if (documentWord != null) {

        documentWordDAO.setSourceWord(documentID, wordID, true, false);

      } else {

          documentWordDAO.addDocumentWord(documentID, wordID, false, true, false, false);
      }
    }

  }

  private String nodeToString(Node node) throws TransformerException {

    StringWriter sw = new StringWriter();

    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

    transformer.transform(new DOMSource(node), new StreamResult(sw));

    return sw.toString();
  }
}
