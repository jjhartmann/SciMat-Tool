/*
 * ExportGroupsXML.java
 *
 * Created on 30-may-2011, 14:31:39
 */
package scimat.api.export;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import org.apache.commons.lang3.StringEscapeUtils;
import scimat.model.knowledgebase.dao.AuthorGroupDAO;
import scimat.model.knowledgebase.dao.AuthorReferenceGroupDAO;
import scimat.model.knowledgebase.dao.ReferenceGroupDAO;
import scimat.model.knowledgebase.dao.ReferenceSourceGroupDAO;
import scimat.model.knowledgebase.dao.WordGroupDAO;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
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
public class ExportGroupsXML implements GenericExporter {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String path;
  
  

  
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param path 
   */
  public ExportGroupsXML(String path) {
    
    this.path = path;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param kbm
   * @throws ExportException
   * @throws KnowledgeBaseException 
   */
  public void execute() throws ExportException {
    
    int i, j;
    ArrayList<AuthorGroup> authorGroups;
    ArrayList<Author> authors;
    ArrayList<AuthorReferenceGroup> authorReferenceGroups;
    ArrayList<AuthorReference> authorReferences;
    ArrayList<ReferenceGroup> referenceGroups;
    ArrayList<Reference> references;
    ArrayList<ReferenceSourceGroup> referenceSourceGroups;
    ArrayList<ReferenceSource> referenceSources;
    ArrayList<WordGroup> wordGroups;
    ArrayList<Word> words;
    AuthorGroup authorGroup;
    AuthorReferenceGroup authorReferenceGroup;
    ReferenceGroup referenceGroup;
    ReferenceSourceGroup referenceSourceGroup;
    WordGroup wordGroup;
    AuthorGroupDAO authorGroupDAO;
    AuthorReferenceGroupDAO authorReferenceGroupDAO;
    ReferenceGroupDAO referenceGroupDAO;
    ReferenceSourceGroupDAO referenceSourceGroupDAO;
    WordGroupDAO wordGroupDAO;
    PrintStream out;
    
    try {
    
      
      authorGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorGroupDAO();
      authorReferenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getAuthorReferenceGroupDAO();
      referenceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceGroupDAO();
      referenceSourceGroupDAO = CurrentProject.getInstance().getFactoryDAO().getReferenceSourceGroupDAO();
      wordGroupDAO = CurrentProject.getInstance().getFactoryDAO().getWordGroupDAO();
      
      out = new PrintStream(this.path);

      out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      
      out.println("<groups>");
      
      // Export Author Groups.
      
      authorGroups = authorGroupDAO.getAuthorGroups();
      
      out.println("  <authorGroups>");
      
      for (i = 0; i < authorGroups.size(); i++) {
      
        authorGroup = authorGroups.get(i);
        
        out.println("    <group name=\"" + 
                StringEscapeUtils.escapeXml(authorGroup.getGroupName()) + 
                "\" stop=\"" + 
                authorGroup.isStopGroup() + "\">");
        
        authors = authorGroupDAO.getAuthors(authorGroup.getAuthorGroupID());
        
        for (j = 0; j < authors.size(); j++) {
        
          out.println("      <author>");
          
          out.println("        <authorName>" + 
                  StringEscapeUtils.escapeXml(authors.get(j).getAuthorName()) + 
                  "</authorName>");
          
          out.println("        <fullAuthorName>" + 
                  StringEscapeUtils.escapeXml(authors.get(j).getFullAuthorName()) + 
                  "</fullAuthorName>");
          
          out.println("      </author>");
        }
        
        out.println("    </group>");
      }
      
      out.println("  </authorGroups>");
      
      // Export Author Reference Groups.
      
      authorReferenceGroups = authorReferenceGroupDAO.getAuthorReferenceGroups();
      
      out.println("  <authorReferenceGroups>");
      
      for (i = 0; i < authorReferenceGroups.size(); i++) {
      
        authorReferenceGroup = authorReferenceGroups.get(i);
        
        out.println("    <group name=\"" + 
                StringEscapeUtils.escapeXml(authorReferenceGroup.getGroupName()) + 
                "\" stop=\"" + 
                authorReferenceGroup.isStopGroup() + "\">");
        
        authorReferences = authorReferenceGroupDAO.getAuthorReferences(authorReferenceGroup.getAuthorReferenceGroupID());
        
        for (j = 0; j < authorReferences.size(); j++) {
        
          out.println("      <authorReference>" + 
                  StringEscapeUtils.escapeXml(authorReferences.get(j).getAuthorName()) + 
                  "</authorReference>");
        }
        
        out.println("    </group>");
      }
      
      out.println("  </authorReferenceGroups>");
      
      // Export Reference Groups.
      
      referenceGroups = referenceGroupDAO.getReferenceGroups();
      
      out.println("  <referenceGroups>");
      
      for (i = 0; i < referenceGroups.size(); i++) {
      
        referenceGroup = referenceGroups.get(i);
        
        out.println("    <group name=\"" + 
                StringEscapeUtils.escapeXml(referenceGroup.getGroupName()) + 
                "\" stop=\"" + 
                referenceGroup.isStopGroup() + "\">");
        
        references = referenceGroupDAO.getReferences(referenceGroup.getReferenceGroupID());
        
        for (j = 0; j < references.size(); j++) {
        
          out.println("      <reference>" + 
                  StringEscapeUtils.escapeXml(references.get(j).getFullReference()) + 
                  "</reference>");
        }
        
        out.println("    </group>");
      }
      
      out.println("  </referenceGroups>");
      
      // Export Reference Source Groups.
      
      referenceSourceGroups = referenceSourceGroupDAO.getReferenceSourceGroups();      
      
      out.println("  <referenceSourceGroups>");
      
      for (i = 0; i < referenceSourceGroups.size(); i++) {
      
        referenceSourceGroup = referenceSourceGroups.get(i);
        
        out.println("    <group name=\"" + 
                StringEscapeUtils.escapeXml(referenceSourceGroup.getGroupName()) + 
                "\" stop=\"" + 
                referenceSourceGroup.isStopGroup() + "\">");
        
        referenceSources = referenceSourceGroupDAO.getReferenceSources(referenceSourceGroup.getReferenceSourceGroupID());
        
        for (j = 0; j < referenceSources.size(); j++) {
        
          out.println("      <referenceSource>" + 
                  StringEscapeUtils.escapeXml(referenceSources.get(j).getSource()) + 
                  "</referenceSource>");
        }
        
        out.println("    </group>");
      }
      
      out.println("  </referenceSourceGroups>");
      
      // Export Word Groups.
      
      wordGroups = wordGroupDAO.getWordGroups();
      
      out.println("  <wordGroups>");
      
      for (i = 0; i < wordGroups.size(); i++) {
      
        wordGroup = wordGroups.get(i);
        
        out.println("    <group name=\"" + 
                StringEscapeUtils.escapeXml(wordGroup.getGroupName()) + 
                "\" stop=\"" + 
                wordGroup.isStopGroup() + "\">");
        
        words = wordGroupDAO.getWords(wordGroup.getWordGroupID());
        
        for (j = 0; j < words.size(); j++) {
        
          out.println("      <word>" + 
                  StringEscapeUtils.escapeXml(words.get(j).getWordName()) + 
                  "</word>");
        }
        
        out.println("    </group>");
      }
      
      out.println("  </wordGroups>");
      
      out.println("</groups>");
      
      out.close();

    } catch (FileNotFoundException fnfe) {
      
      throw  new ExportException(fnfe);
      
    } catch (KnowledgeBaseException kbe) {
    
      throw new ExportException(kbe);
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
