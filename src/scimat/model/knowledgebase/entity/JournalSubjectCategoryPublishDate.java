/*
 * JournalSubjectCategoryPublishDate.java
 *
 * Created on 28-feb-2011, 19:05:59
 */
package scimat.model.knowledgebase.entity;

import java.io.Serializable;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author mjcobo
 */
public class JournalSubjectCategoryPublishDate implements Serializable, Comparable<JournalSubjectCategoryPublishDate>, Cloneable {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private Journal journal;
  private SubjectCategory subjectCategory;
  private PublishDate publishDate;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param journal
   * @param subjectCategory
   * @param publishDate
   */
  public JournalSubjectCategoryPublishDate(Journal journal, SubjectCategory subjectCategory, PublishDate publishDate) {
    this.journal = journal;
    this.subjectCategory = subjectCategory;
    this.publishDate = publishDate;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the journal
   */
  public Journal getJournal() {
    return journal;
  }

  /**
   * @return the subjectCategory
   */
  public SubjectCategory getSubjectCategory() {
    return subjectCategory;
  }

  /**
   * @return the publishDate
   */
  public PublishDate getPublishDate() {
    return publishDate;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {

    String result = "";

    result += "(";
    result += "Journal: " + this.journal + ", ";
    result += "SubjectCategory: " + this.subjectCategory + ", ";
    result += "PublishDate: " + this.publishDate;
    result += ")";

    return result;
  }

  /**
   *
   * @param o
   * @return
   */
  public int compareTo(JournalSubjectCategoryPublishDate o) {
    
    int result;

    // First compare the SubjectCategory. If they are the same, compare the
    // Journal. If they are the same compare the PublishDate

    result = this.subjectCategory.compareTo(o.subjectCategory);

    if (result == 0) {

      result = this.journal.compareTo(o.journal);

      if (result == 0) {

        result = this.publishDate.compareTo(o.publishDate);
      }
    }

    return result;
  }

  /**
   *
   * @return
   */
  @Override
  protected JournalSubjectCategoryPublishDate clone() {

    return new JournalSubjectCategoryPublishDate(journal, subjectCategory, publishDate);
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

    JournalSubjectCategoryPublishDate item = (JournalSubjectCategoryPublishDate)obj;
    
    return this.journal.equals(item.journal) && 
           this.subjectCategory.equals(item.subjectCategory) && 
           this.publishDate.equals(item.publishDate);
  }

  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    
    return new HashCodeBuilder(87, 3).append(this.journal).append(this.subjectCategory).append(this.publishDate).toHashCode();
    
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
