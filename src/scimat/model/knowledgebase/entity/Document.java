/*
 * Document.java
 *
 * Created on 28-oct-2010, 18:20:28
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;

/**
 *
 * @author mjcobo
 */
public class Document implements Serializable, Comparable<Document>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private Integer documentID;

  /**
   * 
   */
  private String type;

  /**
   *
   */
  private String title;

  /**
   *
   */
  private String docAbstract;

  /**
   *
   */
  private int citationsCount;

  /**
   *
   */
  private String doi;

  /**
   * 
   */
  private String sourceIdentifier;

  /**
   *
   */
  private String volume;

  /**
   *
   */
  private String issue;

  /**
   *
   */
  private String beginPage;

  /**
   *
   */
  private String endPage;
  private String authors;
  private String year;
  private int wordsCount;
  private int authorWordsCount;
  private int sourceWordsCount;
  private int addedWordsCount;
  private int affiliationsCount;
  private int referencesCount;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   *
   * @param documentID
   * @param type
   * @param title
   * @param docAbstract
   * @param citationsCount
   * @param doi
   * @param sourceIdentifier
   * @param volume
   * @param issue
   * @param beginPage
   * @param endPage
   */
  public Document(Integer documentID, String type, String title,
          String docAbstract, int citationsCount, String doi, String sourceIdentifier,
          String volume, String issue, String beginPage, String endPage, String authors, 
          String year, int wordsCount, int authorWordsCount, int sourceWordsCount, 
          int addedWordsCount, int affiliationsCount, int referencesCount) {

    this.documentID = documentID;
    this.type = type;
    this.title = title;
    this.docAbstract = docAbstract;
    this.citationsCount = citationsCount;
    this.doi = doi;
    this.sourceIdentifier = sourceIdentifier;
    this.volume = volume;
    this.issue = issue;
    this.beginPage = beginPage;
    this.endPage = endPage;
    this.authors = authors;
    this.year = year;
    this.wordsCount = wordsCount;
    this.authorWordsCount = authorWordsCount;
    this.sourceWordsCount = sourceWordsCount;
    this.addedWordsCount = addedWordsCount;
    this.affiliationsCount = affiliationsCount;
    this.referencesCount = referencesCount;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the idDocument
   */
  public Integer getDocumentID() {
    return documentID;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @return the docAbstract
   */
  public String getDocAbstract() {
    return docAbstract;
  }

  /**
   * @return the citationsCount
   */
  public int getCitationsCount() {
    return citationsCount;
  }

  /**
   * @return the doi
   */
  public String getDoi() {
    return doi;
  }

  /**
   *
   * @return the source identifier
   */
  public String getSourceIdentifier() {
    return sourceIdentifier;
  }

  /**
   * @return the volume
   */
  public String getVolume() {
    return volume;
  }

  /**
   * @return the issue
   */
  public String getIssue() {
    return issue;
  }

  /**
   * @return the beginPage
   */
  public String getBeginPage() {
    return beginPage;
  }

  /**
   * @return the endPage
   */
  public String getEndPage() {
    return endPage;
  }

  /**
   * @return the authors
   */
  public String getAuthors() {
    return authors;
  }

  /**
   * @return the year
   */
  public String getYear() {
    return year;
  }

  /**
   * @return the words
   */
  public int getWordsCount() {
    return wordsCount;
  }

  /**
   * @return the authorWords
   */
  public int getAuthorWordsCount() {
    return authorWordsCount;
  }

  /**
   * @return the sourceWords
   */
  public int getSourceWordsCount() {
    return sourceWordsCount;
  }

  /**
   * @return the addedWords
   */
  public int getAddedWordsCount() {
    return addedWordsCount;
  }

  /**
   * 
   * @return 
   */
  public int getAffiliationsCount() {
    return affiliationsCount;
  }

  /**
   * 
   * @return 
   */
  public int getReferencesCount() {
    return referencesCount;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {

    String result = "";

    result += "(";
    result += "idDocument: " + this.documentID + ", ";
    result += "title: \"" + this.title + "\", ";
    result += "docAbstract: \"" + this.docAbstract + "\", ";
    result += "type: " + this.type + ", ";
    result += "citationsCount: " + this.citationsCount + ", ";
    result += "doi: " + this.doi + ", ";
    result += "sourceIdentifier: " + this.sourceIdentifier + ", ";
    result += "volume: " + this.volume + ", ";
    result += "issue: " + this.issue + ", ";
    result += "beginPage: " + this.beginPage + ", ";
    result += "endPage: " + this.endPage + ", ";
    result += "authors: \"" + this.authors + "\", ";
    result += "year: " + this.year + ", ";
    result += "words: " + this.wordsCount + ", ";
    result += "authorWordsCount: " + this.authorWordsCount + ", ";
    result += "sourceWordsCount: " + this.sourceWordsCount + ", ";
    result += "addededWordsCount: " + this.addedWordsCount + ", ";
    result += "affiliationsCount: " + this.affiliationsCount + ", ";
    result += "referencesCount: " + this.referencesCount;
    result += ")";
    
    return result;
  }

  /**
   * 
   * @return
   */
  @Override
  public Document clone() {
    
    return new Document(this.documentID, this.type, this.title, 
            this.docAbstract, this.citationsCount, this.doi, this.sourceIdentifier, this.volume,
            this.issue, this.beginPage, this.endPage, this.authors, this.year, this.wordsCount,
            this.authorWordsCount, this.sourceWordsCount, this.addedWordsCount,
            this.affiliationsCount, this.referencesCount);
  }

  /**
   * 
   * @param o
   * @return
   */
  public int compareTo(Document o) {
    return this.documentID.compareTo(o.documentID);
  }
  
  /**
   * 
   * @param obj
   * @return 
   */
  @Override
  public boolean equals(Object obj) {
    
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    Document item = (Document)obj;
    
    return this.documentID.equals(item.documentID);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    return this.documentID;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
