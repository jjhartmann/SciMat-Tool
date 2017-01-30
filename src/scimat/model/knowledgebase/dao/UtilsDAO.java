/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scimat.model.knowledgebase.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.entity.AuthorReferenceReference;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.DocumentAuthor;
import scimat.model.knowledgebase.entity.DocumentWord;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.entity.JournalSubjectCategoryPublishDate;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.entity.SubjectCategory;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.entity.WordGroup;

/**
 *
 * @author mjcobo
 */
public class UtilsDAO {

  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   */
  private UtilsDAO() {
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @return 
   */
  public static UtilsDAO getInstance() {
    return UtilsDAOHolder.INSTANCE;
  }
  
  /**
   * 
   */
  private static class UtilsDAOHolder {

    private static final UtilsDAO INSTANCE = new UtilsDAO();
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Affiliation getAffiliation(ResultSet rs) throws SQLException {

    return new Affiliation(rs.getInt("idAffiliation"),
            rs.getString("fullAffiliation"),
            rs.getInt("affiliation_documentsCount"),
            rs.getInt("affiliation_authorsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getAffiliationID(ResultSet rs) throws SQLException {

    return rs.getInt("idAffiliation");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Author getAuthor(ResultSet rs) throws SQLException {

    return new Author(rs.getInt("idAuthor"),
            rs.getString("authorName"),
            rs.getString("fullAuthorName"),
            rs.getInt("author_documentsCount"),
            rs.getInt("author_affiliationsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getAuthorID(ResultSet rs) throws SQLException {

    return rs.getInt("idAuthor");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public AuthorGroup getAuthorGroup(ResultSet rs) throws SQLException {

    return new AuthorGroup(rs.getInt("idAuthorGroup"),
            rs.getString("groupName"),
            rs.getBoolean("stopGroup"),
            rs.getInt("itemsCount"),
            rs.getInt("authorGroup_documentsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getAuthorGroupID(ResultSet rs) throws SQLException {

    return rs.getInt("idAuthorGroup");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public AuthorReference getAuthorReference(ResultSet rs) throws SQLException {

    return new AuthorReference(rs.getInt("idAuthorReference"),
            rs.getString("authorName"),
            rs.getInt("authorReference_referencesCount"),
            rs.getInt("authorReference_documentsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getAuthorReferenceID(ResultSet rs) throws SQLException {

    return rs.getInt("idAuthorReference");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public AuthorReferenceGroup getAuthorReferenceGroup(ResultSet rs) throws SQLException {

    return new AuthorReferenceGroup(rs.getInt("idAuthorReferenceGroup"),
            rs.getString("groupName"),
            rs.getBoolean("stopGroup"),
            rs.getInt("itemsCount"),
            rs.getInt("authorReferenceGroup_documentsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getAuthorReferenceGroupID(ResultSet rs) throws SQLException {

    return rs.getInt("idAuthorReferenceGroup");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public AuthorReferenceReference getAuthorReferenceReference(ResultSet rs) throws SQLException {

    return new AuthorReferenceReference(getAuthorReference(rs), 
            getReference(rs), 
            rs.getInt("position"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public DocumentAuthor getDocumentAuthor(ResultSet rs) throws SQLException {

    return new DocumentAuthor(getDocument(rs), 
            getAuthor(rs), 
            rs.getInt("position"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Document getDocument(ResultSet rs) throws SQLException {

    return new Document(rs.getInt("idDocument"),
            rs.getString("type"),
            rs.getString("title"),
            rs.getString("docAbstract"),
            rs.getInt("citationsCount"),
            rs.getString("doi"),
            rs.getString("sourceIdentifier"),
            rs.getString("volume"),
            rs.getString("issue"),
            rs.getString("beginPage"),
            rs.getString("endPage"),
            rs.getString("authors"),
            rs.getString("year"),
            rs.getInt("document_wordsCount"),
            rs.getInt("authorWordsCount"),
            rs.getInt("sourceWordsCount"),
            rs.getInt("addedWordsCount"),
            rs.getInt("document_affiliationsCount"),
            rs.getInt("document_referencesCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getDocumentID(ResultSet rs) throws SQLException {

    return rs.getInt("idDocument");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public DocumentWord getDocumentWord(ResultSet rs) throws SQLException {

    return new DocumentWord(getDocument(rs), 
            getWord(rs),
            rs.getBoolean("isAuthorWord"),
            rs.getBoolean("isSourceWord"),
            rs.getBoolean("isAddedWord"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Journal getJournal(ResultSet rs) throws SQLException {

    return new Journal(rs.getInt("idJournal"),
            rs.getString("source"),
            rs.getString("conferenceInformation"),
            rs.getInt("journal_documentsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getJournalID(ResultSet rs) throws SQLException {

    return rs.getInt("idJournal");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public JournalSubjectCategoryPublishDate getJournalSubjectCategoryPublishDate(ResultSet rs) throws SQLException {
  
    return new JournalSubjectCategoryPublishDate(getJournal(rs), 
            getSubjectCategory(rs), 
            getPublishDate(rs));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Period getPeriod(ResultSet rs) throws SQLException {

    return new Period(rs.getInt("idPeriod"),
            rs.getString("name"),
            rs.getInt("position"),
            rs.getInt("period_publishDatesCount"),
            rs.getInt("period_documentsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getPeriodID(ResultSet rs) throws SQLException {

    return rs.getInt("idPeriod");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public PublishDate getPublishDate(ResultSet rs) throws SQLException {

    return new PublishDate(rs.getInt("idPublishDate"),
            rs.getString("year"),
            rs.getString("date"),
            rs.getInt("publishDate_documentsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getPublishDateID(ResultSet rs) throws SQLException {

    return rs.getInt("idPublishDate");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Reference getReference(ResultSet rs) throws SQLException {

    return new Reference(rs.getInt("idReference"),
            rs.getString("fullReference"),
            rs.getString("volume"),
            rs.getString("issue"),
            rs.getString("page"),
            rs.getString("year"),
            rs.getString("doi"),
            rs.getString("format"),
            rs.getInt("reference_documentsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getReferenceID(ResultSet rs) throws SQLException {

    return rs.getInt("idReference");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public ReferenceGroup getReferenceGroup(ResultSet rs) throws SQLException {

    return new ReferenceGroup(rs.getInt("idReferenceGroup"),
            rs.getString("groupName"),
            rs.getBoolean("stopGroup"),
            rs.getInt("itemsCount"),
            rs.getInt("referenceGroup_documentsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getReferenceGroupID(ResultSet rs) throws SQLException {

    return rs.getInt("idReferenceGroup");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public ReferenceSource getReferenceSource(ResultSet rs) throws SQLException {

    return new ReferenceSource(rs.getInt("idReferenceSource"),
            rs.getString("source"),
            rs.getInt("referenceSource_referencesCount"),
            rs.getInt("referenceSource_documentsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getReferenceSourceID(ResultSet rs) throws SQLException {

    return rs.getInt("idReferenceSource");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public ReferenceSourceGroup getReferenceSourceGroup(ResultSet rs) throws SQLException {

    return new ReferenceSourceGroup(rs.getInt("idReferenceSourceGroup"),
            rs.getString("groupName"),
            rs.getBoolean("stopGroup"),
            rs.getInt("itemsCount"),
            rs.getInt("referenceSourceGroup_documentsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getReferenceSourceGroupID(ResultSet rs) throws SQLException {

    return rs.getInt("idReferenceSourceGroup");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public SubjectCategory getSubjectCategory(ResultSet rs) throws SQLException {

    return new SubjectCategory(rs.getInt("idSubjectCategory"),
            rs.getString("subjectCategoryName"),
            rs.getInt("subjectCategory_journalsCount"),
            rs.getInt("subjectCategory_documentsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getSubjectCategoryID(ResultSet rs) throws SQLException {

    return rs.getInt("idSubjectCategory");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Word getWord(ResultSet rs) throws SQLException {

    return new Word(rs.getInt("idWord"),
            rs.getString("wordName"),
            rs.getInt("word_documentsCount"),
            rs.getInt("roleAuthorCount"),
            rs.getInt("roleSourceCount"),
            rs.getInt("roleAddedCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getWordID(ResultSet rs) throws SQLException {

    return rs.getInt("idWord");
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public WordGroup getWordGroup(ResultSet rs) throws SQLException {

    return new WordGroup(rs.getInt("idWordGroup"),
            rs.getString("groupName"),
            rs.getBoolean("stopGroup"),
            rs.getInt("itemsCount"),
            rs.getInt("wordGroup_documentsCount"));
  }
  
  /**
   * 
   * @param rs
   * @return
   * @throws SQLException 
   */
  public Integer getWordGroupID(ResultSet rs) throws SQLException {

    return rs.getInt("idWordGroup");
  }
}
