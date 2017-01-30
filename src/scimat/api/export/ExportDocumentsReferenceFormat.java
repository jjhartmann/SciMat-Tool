/*
 * ExportDocumentsReferenceFormat.java
 *
 * Created on 19-jul-2011, 12:03:01
 */
package scimat.api.export;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
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
public class ExportDocumentsReferenceFormat {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * 
   */
  private KnowledgeBaseManager kmb;
  
  /**
   * 
   */
  private ArrayList<Document> documents;

  private String file;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param kmb
   * @param documents 
   */
  public ExportDocumentsReferenceFormat(KnowledgeBaseManager kmb, 
          ArrayList<Document> documents, String file) {
    
    this.kmb = kmb;
    this.documents = documents;
    this.file = file;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  public void execute() throws KnowledgeBaseException, FileNotFoundException {
  
    int i, j;
    Document document;
    ArrayList<Author> authors;
    Journal journal;
    PublishDate publishDate;
    DocumentDAO documentDAO;
    PrintStream out;
    
    documentDAO = new DocumentDAO(kmb);
    
    out = new PrintStream(this.file);
    
    for (i = 0; i < documents.size(); i++) {
      
      document = documents.get(i);
      authors = documentDAO.getAuthors(document.getDocumentID());
      journal = documentDAO.getJournal(document.getDocumentID());
      publishDate = documentDAO.getPublishDate(document.getDocumentID());
      
      for (j = 0; j < authors.size(); j++) {
      
        out.print(authors.get(j).getAuthorName() + ", ");
      }
      
      out.print(document.getTitle() + ". ");
      
      if (journal != null) {
      
        out.print(journal.getSource() + " ");
        out.print(document.getVolume() + ":");
        out.print(document.getIssue() + " ");
        out.print(document.getBeginPage() + "-");
        out.print(document.getEndPage() + " ");
      }
      
      if (publishDate != null) {
      
        out.print("(" + publishDate.getYear() + ") ");
      }
      
      out.println("Times cited: " + document.getCitationsCount());
    }
    
    out.close();
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
