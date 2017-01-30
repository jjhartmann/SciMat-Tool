/*
 * ImportGroupsXML.java
 *
 * Created on 30-may-2011, 18:26:43
 */
package scimat.api.imports;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import java.io.FileReader;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import scimat.model.knowledgebase.dao.AuthorDAO;
import scimat.model.knowledgebase.dao.AuthorGroupDAO;
import scimat.model.knowledgebase.dao.AuthorReferenceDAO;
import scimat.model.knowledgebase.dao.AuthorReferenceGroupDAO;
import scimat.model.knowledgebase.dao.ReferenceDAO;
import scimat.model.knowledgebase.dao.ReferenceGroupDAO;
import scimat.model.knowledgebase.dao.ReferenceSourceDAO;
import scimat.model.knowledgebase.dao.ReferenceSourceGroupDAO;
import scimat.model.knowledgebase.dao.WordDAO;
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

/**
 *
 * @author mjcobo
 */
public class ImportGroupsXML {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private KnowledgeBaseManager kbm;
  private String path;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param kbm
   * @param path 
   */
  public ImportGroupsXML(KnowledgeBaseManager kbm, String path) {
    this.kbm = kbm;
    this.path = path;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  public void execute() throws ImportException {
  
    try {
     
      XMLReader parser = XMLReaderFactory.createXMLReader();

      parser.setContentHandler(new GroupsHandler());

      parser.parse(new InputSource(new FileReader(path)));
      
      this.kbm.commit();
      
    } catch (Exception e) {
    
      try {
      
        System.out.println("Doing rollback");
        this.kbm.rollback();
        
      } catch (KnowledgeBaseException kbe) {
      
        kbe.printStackTrace(System.err);
        throw new ImportException(kbe);
      }
      
      e.printStackTrace(System.err);
      throw new ImportException(e);
    }
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
  /***************************************************************************/
  /*                           Private Calsses                               */
  /***************************************************************************/
  
  private class GroupsHandler extends DefaultHandler {
    
    private final static String __GROUPS = "groups";
    private final static String __GROUP = "group";
    private final static String __GROUP_NAME = "name";
    private final static String __STOP_GROUP = "stop";
    private final static String __AUTHOR_GROUPS = "authorGroups";
    private final static String __AUTHORREFERENCE_GROUPS = "authorReferenceGroups";
    private final static String __REFERENCE_GROUPS = "referenceGroups";
    private final static String __REFERENCESOURCE_GROUPS = "referenceSourceGroups";
    private final static String __WORD_GROUPS = "wordGroups";
    private final static String __AUTHOR = "author";
    private final static String __AUTHOR_NAME = "authorName";
    private final static String __AUTHOR_FULL_NAME = "fullAuthorName";
    private final static String __AUTHORREFERENCE = "authorReference";
    private final static String __REFERENCE = "reference";
    private final static String __REFERENCESOURCE = "referenceSource";
    private final static String __WORD = "word";
    
    private StringBuffer currentValue;
    
    private String groupName;
    private String stopGroup;
    private String authorName;
    private String fullAuthorName;
    private String authorReferenceName;
    private String fullReference;
    private String source;
    private String wordName;
    
    private AuthorDAO authorDAO;
    private AuthorGroupDAO authorGroupDAO;
    private AuthorReferenceDAO authorReferenceDAO;
    private AuthorReferenceGroupDAO authorReferenceGroupDAO;
    private ReferenceDAO referenceDAO;
    private ReferenceGroupDAO referenceGroupDAO;
    private ReferenceSourceDAO referenceSourceDAO;
    private ReferenceSourceGroupDAO referenceSourceGroupDAO;
    private WordDAO wordDAO;
    private WordGroupDAO wordGroupDAO;
    
    private Author author;
    private AuthorGroup authorGroup;
    private AuthorReference authorReference;
    private AuthorReferenceGroup authorReferenceGroup;
    private Reference reference;
    private ReferenceGroup referenceGroup;
    private ReferenceSource referenceSource;
    private ReferenceSourceGroup referenceSourceGroup;
    private Word word;
    private WordGroup wordGroup;

    /**
     * 
     */
    public GroupsHandler() throws KnowledgeBaseException {
      
      this.currentValue = new StringBuffer();
      
      this.groupName = null;
      this.stopGroup = null;
      this.authorName = null;
      this.fullAuthorName = null;
      this.authorReferenceName = null;
      this.fullReference = null;
      this.source = null;
      this.wordName = null;
      
      this.authorDAO = new AuthorDAO(kbm);
      this.authorGroupDAO = new AuthorGroupDAO(kbm);
      this.authorReferenceDAO = new AuthorReferenceDAO(kbm);
      this.authorReferenceGroupDAO = new AuthorReferenceGroupDAO(kbm);
      this.referenceDAO = new ReferenceDAO(kbm);
      this.referenceGroupDAO = new ReferenceGroupDAO(kbm);
      this.referenceSourceDAO = new ReferenceSourceDAO(kbm);
      this.referenceSourceGroupDAO = new ReferenceSourceGroupDAO(kbm);
      this.wordDAO = new WordDAO(kbm);
      this.wordGroupDAO = new WordGroupDAO(kbm);
    }
    
    /**
     * 
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException 
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      
      this.currentValue = new StringBuffer();
      
      if (localName.equals(__GROUPS)) {
      
      } else if (localName.equals(__GROUP)) {
        
        this.groupName = attributes.getValue(__GROUP_NAME);
        
        if (this.groupName != null) {
        
          this.groupName.trim();
          
        } else {
        
          throw new SAXException("The attribute name is requiered.");
        }
        
        this.stopGroup = attributes.getValue(__STOP_GROUP);
        
        if (this.stopGroup != null) {
        
          this.stopGroup.trim();
          
        } else {
        
          throw new SAXException("The attribute stop is requiered.");
        }
        
        this.authorName = null;
        this.fullAuthorName = null;
        this.authorReferenceName = null;
        this.fullReference = null;
        this.referenceSource = null;
        this.wordName = null;
      
      } else if (localName.equals(__AUTHOR_GROUPS)) {
        
        this.authorGroup = null;
      
      } else if (localName.equals(__AUTHOR)) {
        
        this.author = null;
        
      } else if (localName.equals(__AUTHORREFERENCE_GROUPS)) {
        
        this.authorReferenceGroup = null;
      
      } else if (localName.equals(__AUTHORREFERENCE)) {
        
        this.authorReference = null;
      
      } else if (localName.equals(__REFERENCE_GROUPS)) {
        
        this.referenceGroup = null;
      
      } else if (localName.equals(__REFERENCE)) {
        
        this.reference = null;
      
      } else if (localName.equals(__REFERENCESOURCE_GROUPS)) {
        
        this.referenceSourceGroup = null;
      
      } else if (localName.equals(__REFERENCESOURCE)) {
        
        this.referenceSource = null;
      
      } else if (localName.equals(__WORD_GROUPS)) {
        
        this.wordGroup = null;
      
      } else if (localName.equals(__WORD)) {
        
        this.word = null;
      
      }
    }

    /**
     * 
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException 
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      
      try {

        if (localName.equals(__GROUPS)) {
        } else if (localName.equals(__GROUP)) {
          
          this.authorGroup = null;
          this.authorReferenceGroup = null;
          this.referenceGroup = null;
          this.referenceSourceGroup = null;
          this.wordGroup = null;
          
        } else if (localName.equals(__AUTHOR_GROUPS)) {
          
          this.authorGroup = null;
          
        } else if (localName.equals(__AUTHOR)) {
          
          this.author = authorDAO.getAuthor(this.authorName, this.fullAuthorName);

          if (this.author != null) { // If the item exist --> add group and set it to the item
            
            if (this.authorGroup == null) {
            
              if (! this.authorGroupDAO.checkAuthorGroup(this.groupName)) {
              
                this.authorGroup = this.authorGroupDAO.getAuthorGroup(this.authorGroupDAO.addAuthorGroup(this.groupName, Boolean.getBoolean(this.stopGroup), false));
              
              } else {
              
                this.authorGroup = this.authorGroupDAO.getAuthorGroup(this.groupName);
              }
            }
            
            authorDAO.setAuthorGroup(this.author.getAuthorID(), this.authorGroup.getAuthorGroupID(), false);
          }

        } else if (localName.equals(__AUTHOR_NAME)) {

          this.authorName = this.currentValue.toString().trim();

        } else if (localName.equals(__AUTHOR_FULL_NAME)) {

          this.fullAuthorName = this.currentValue.toString().trim();

        } else if (localName.equals(__AUTHORREFERENCE_GROUPS)) {
          
          this.authorReferenceGroup = null;
          
        } else if (localName.equals(__AUTHORREFERENCE)) {

          this.authorReferenceName = this.currentValue.toString().trim();
          
          this.authorReference = authorReferenceDAO.getAuthorReference(this.authorReferenceName);

          if (this.authorReference != null) { // If the item exist --> add group and set it to the item
            
            if (this.authorReferenceGroup == null) {
            
              if (! this.authorReferenceGroupDAO.checkAuthorReferenceGroup(this.groupName)) {
              
                this.authorReferenceGroup = this.authorReferenceGroupDAO.getAuthorReferenceGroup(this.authorReferenceGroupDAO.addAuthorReferenceGroup(this.groupName, Boolean.getBoolean(this.stopGroup), false));
              
              } else {
              
                this.authorReferenceGroup = this.authorReferenceGroupDAO.getAuthorReferenceGroup(this.groupName);
              }
            }
            
            authorReferenceDAO.setAuthorReferenceGroup(this.authorReference.getAuthorReferenceID(), this.authorReferenceGroup.getAuthorReferenceGroupID(), false);
          }

        } else if (localName.equals(__REFERENCE_GROUPS)) {
          
          this.referenceGroup = null;
          
        } else if (localName.equals(__REFERENCE)) {

          this.fullReference = this.currentValue.toString().trim();
          
          this.reference = referenceDAO.getReference(this.fullReference);

          if (this.reference != null) { // If the item exist --> add group and set it to the item
            
            if (this.referenceGroup == null) {
            
              if (! this.referenceGroupDAO.checkReferenceGroup(this.groupName)) {
              
                this.referenceGroup = this.referenceGroupDAO.getReferenceGroup(this.referenceGroupDAO.addReferenceGroup(this.groupName, Boolean.getBoolean(this.stopGroup), false));
              
              } else {
              
                this.referenceGroup = this.referenceGroupDAO.getReferenceGroup(this.groupName);
              }
            }
            
            referenceDAO.setReferenceGroup(this.reference.getReferenceID(), this.referenceGroup.getReferenceGroupID(), false);
          }

        } else if (localName.equals(__REFERENCESOURCE_GROUPS)) {
          
          this.referenceSourceGroup = null;
          
        } else if (localName.equals(__REFERENCESOURCE)) {

          this.source = this.currentValue.toString().trim();
          
          this.referenceSource = referenceSourceDAO.getReferenceSource(this.source);

          if (this.referenceSource != null) { // If the item exist --> add group and set it to the item
            
            if (this.referenceSourceGroup == null) {
            
              if (! this.referenceSourceGroupDAO.checkReferenceSourceGroup(this.groupName)) {
              
                this.referenceSourceGroup = this.referenceSourceGroupDAO.getReferenceSourceGroup(this.referenceSourceGroupDAO.addReferenceSourceGroup(this.groupName, Boolean.getBoolean(this.stopGroup), false));
              
              } else {
              
                this.referenceSourceGroup = this.referenceSourceGroupDAO.getReferenceSourceGroup(this.groupName);
              }
            }
            
            referenceSourceDAO.setReferenceSourceGroup(this.referenceSource.getReferenceSourceID(), this.referenceSourceGroup.getReferenceSourceGroupID(), false);
          }

        } else if (localName.equals(__WORD_GROUPS)) {
          
          this.wordGroup = null;
          
        } else if (localName.equals(__WORD)) {

          this.wordName = this.currentValue.toString().trim();
          
          this.word = wordDAO.getWord(this.wordName);

          if (this.word != null) { // If the item exist --> add group and set it to the item
            
            if (this.wordGroup == null) {
            
              if (! this.wordGroupDAO.checkWordGroup(this.groupName)) {
              
                this.wordGroup = this.wordGroupDAO.getWordGroup(this.wordGroupDAO.addWordGroup(this.groupName, Boolean.getBoolean(this.stopGroup), false));
              
              } else {
              
                this.wordGroup = this.wordGroupDAO.getWordGroup(this.groupName);
              }
            }
            
            wordDAO.setWordGroup(this.word.getWordID(), this.wordGroup.getWordGroupID(), false);
          }

        }

      } catch (KnowledgeBaseException kbe) {
        
        throw new SAXException(kbe);
      }
    }

    /**
     * 
     * @param ch
     * @param start
     * @param length
     * @throws SAXException 
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      
      this.currentValue.append(ch, start, length);
    }
  
    
  }
}
