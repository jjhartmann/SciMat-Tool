/*
 * DocumentFormatter.java
 *
 * Created on 03-feb-2012, 20:48:31
 */
package scimat.api.report;

import java.util.ArrayList;
import org.apache.commons.lang3.text.WordUtils;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.dao.DocumentDAO;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class DocumentFormatter {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private KnowledgeBaseManager kbm;  
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   */
  public DocumentFormatter(KnowledgeBaseManager kbm) {
    this.kbm = kbm;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param kbm
   * @param idDocument
   * @return
   * @throws KnowledgeBaseException 
   */
  public String format(Integer idDocument) throws KnowledgeBaseException {
  
    String result;
    int authorIndex;
    Document document;
    DocumentDAO documentDAO;
    ArrayList<Author> authors;
    Journal journal;
    PublishDate publishDate;
    
    documentDAO = new DocumentDAO(kbm);
    
    document = documentDAO.getDocument(idDocument);
    
    authors = documentDAO.getAuthors(document.getDocumentID());
    journal = documentDAO.getJournal(document.getDocumentID());
    publishDate = documentDAO.getPublishDate(document.getDocumentID());
   
    result = "";

    for (authorIndex = 0; authorIndex < authors.size(); authorIndex++) {

      result += authors.get(authorIndex).getAuthorName().toUpperCase() + ", ";
    }
    
    authors.clear();

    
    result += WordUtils.capitalize(document.getTitle().toLowerCase().replaceAll("\n", " ")) + ". ";
    result += (journal != null ? WordUtils.capitalize(journal.getSource().toLowerCase().replaceAll("\n", " ")) : "---") + " " + document.getVolume() + ":" + document.getIssue() + " ";
    result += document.getBeginPage() + "-" + document.getEndPage() + " ";
    result += "(" + (publishDate != null ? publishDate.getYear() : "---") + "). ";
    result += "\tTimes cited: " + document.getCitationsCount();
    
    document = null;
    journal = null;
    publishDate = null;
    
    return result;
  }
  
  public String formatShort(Integer idDocument) throws KnowledgeBaseException {
  
    String result;
    Document document;
    DocumentDAO documentDAO;
    ArrayList<Author> authors;
    Journal journal;
    PublishDate publishDate;
    
    documentDAO = new DocumentDAO(kbm);
    
    document = documentDAO.getDocument(idDocument);
    
    authors = documentDAO.getAuthors(document.getDocumentID());
    journal = documentDAO.getJournal(document.getDocumentID());
    publishDate = documentDAO.getPublishDate(document.getDocumentID());
   
    result = "";
    
    if (authors.size() > 0) {
    
      result += authors.get(0).getAuthorName().toUpperCase() + ", ";
      
      if (authors.size() > 1) {
      
        result += "et. al., ";
      }
    }
    
    authors.clear();
    
    result += WordUtils.capitalize(document.getTitle().toLowerCase().replaceAll("\n", " ")) + ". ";
    result += (journal != null ? WordUtils.capitalize(journal.getSource().toLowerCase().replaceAll("\n", " ")) : "---") + " " + document.getVolume() + ":" + document.getIssue() + " ";
    result += document.getBeginPage() + "-" + document.getEndPage() + " ";
    result += "(" + (publishDate != null ? publishDate.getYear() : "---") + "). ";
    result += "\t" + document.getCitationsCount();
    
    document = null;
    journal = null;
    publishDate = null;
    
    return result;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
