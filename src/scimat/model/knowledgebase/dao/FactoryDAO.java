/*
 * FactoryDAO.java
 *
 * Created on 11-mar-2011, 13:42:06
 */
package scimat.model.knowledgebase.dao;

import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.model.statistic.dao.StatisticDAO;

/**
 *
 * @author mjcobo
 */
public class FactoryDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private KnowledgeBaseManager kbm;

  private AffiliationDAO affiliationDAO;
  private AuthorAffiliationDAO authorAffiliationDAO;
  private AuthorDAO authorDAO;
  private AuthorGroupDAO authorGroupDAO;
  private AuthorReferenceDAO authorReferenceDAO;
  private AuthorReferenceGroupDAO authorReferenceGroupDAO;
  private AuthorReferenceReferenceDAO authorReferenceReferenceDAO;
  private DocumentAffiliationDAO documentAffiliationDAO;
  private DocumentAuthorDAO documentAuthorDAO;
  private DocumentDAO documentDAO;
  private DocumentReferenceDAO documentReferenceDAO;
  private DocumentWordDAO documentWordDAO;
  private JournalDAO journalDAO;
  private JournalSubjectCategoryPublishDateDAO journalSubjectCategoryPublishDateDAO;
  private PeriodDAO periodDAO;
  private PublishDateDAO publishDateDAO;
  private PublishDatePeriodDAO publishDatePeriodDAO;
  private ReferenceDAO referenceDAO;
  private ReferenceGroupDAO referenceGroupDAO;
  private ReferenceSourceDAO referenceSourceDAO;
  private ReferenceSourceGroupDAO referenceSourceGroupDAO;
  private SubjectCategoryDAO subjectCategoryDAO;
  private WordDAO wordDAO;
  private WordGroupDAO wordGroupDAO;
  private StatisticDAO statisticDAO;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  public FactoryDAO(KnowledgeBaseManager kbm) throws KnowledgeBaseException {

    this.kbm = kbm;

    affiliationDAO = new AffiliationDAO(kbm);
    authorAffiliationDAO = new AuthorAffiliationDAO(kbm);
    authorDAO = new AuthorDAO(kbm);
    authorGroupDAO = new AuthorGroupDAO(kbm);
    authorReferenceDAO = new AuthorReferenceDAO(kbm);
    authorReferenceGroupDAO = new AuthorReferenceGroupDAO(kbm);
    authorReferenceReferenceDAO = new AuthorReferenceReferenceDAO(kbm);
    documentAffiliationDAO = new DocumentAffiliationDAO(kbm);
    documentAuthorDAO = new DocumentAuthorDAO(kbm);
    documentDAO = new DocumentDAO(kbm);
    documentReferenceDAO = new DocumentReferenceDAO(kbm);
    documentWordDAO = new DocumentWordDAO(kbm);
    journalDAO = new JournalDAO(kbm);
    journalSubjectCategoryPublishDateDAO = new JournalSubjectCategoryPublishDateDAO(kbm);
    periodDAO = new PeriodDAO(kbm);
    publishDateDAO = new PublishDateDAO(kbm);
    publishDatePeriodDAO = new PublishDatePeriodDAO(kbm);
    referenceDAO = new ReferenceDAO(kbm);
    referenceGroupDAO = new ReferenceGroupDAO(kbm);
    referenceSourceDAO = new ReferenceSourceDAO(kbm);
    referenceSourceGroupDAO = new ReferenceSourceGroupDAO(kbm);
    subjectCategoryDAO = new SubjectCategoryDAO(kbm);
    wordDAO = new WordDAO(kbm);
    wordGroupDAO = new WordGroupDAO(kbm);
    statisticDAO = new StatisticDAO(kbm);
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * @return the kbm
   */
  public KnowledgeBaseManager getKbm() {
    return kbm;
  }

  /**
   * @return the affiliationDAO
   */
  public AffiliationDAO getAffiliationDAO() {
    return affiliationDAO;
  }

  /**
   * @return the authorAffiliationDAO
   */
  public AuthorAffiliationDAO getAuthorAffiliationDAO() {
    return authorAffiliationDAO;
  }

  /**
   * @return the authorDAO
   */
  public AuthorDAO getAuthorDAO() {
    return authorDAO;
  }

  /**
   * @return the authorGroupDAO
   */
  public AuthorGroupDAO getAuthorGroupDAO() {
    return authorGroupDAO;
  }

  /**
   * @return the authorReferenceDAO
   */
  public AuthorReferenceDAO getAuthorReferenceDAO() {
    return authorReferenceDAO;
  }

  /**
   * @return the authorReferenceGroupDAO
   */
  public AuthorReferenceGroupDAO getAuthorReferenceGroupDAO() {
    return authorReferenceGroupDAO;
  }

  /**
   * @return the authorReferenceReferenceDAO
   */
  public AuthorReferenceReferenceDAO getAuthorReferenceReferenceDAO() {
    return authorReferenceReferenceDAO;
  }

  /**
   * @return the documentAffiliationDAO
   */
  public DocumentAffiliationDAO getDocumentAffiliationDAO() {
    return documentAffiliationDAO;
  }

  /**
   * @return the documentAuthorDAO
   */
  public DocumentAuthorDAO getDocumentAuthorDAO() {
    return documentAuthorDAO;
  }

  /**
   * @return the documentDAO
   */
  public DocumentDAO getDocumentDAO() {
    return documentDAO;
  }

  /**
   * @return the documentReferenceDAO
   */
  public DocumentReferenceDAO getDocumentReferenceDAO() {
    return documentReferenceDAO;
  }

  /**
   * @return the documentWordDAO
   */
  public DocumentWordDAO getDocumentWordDAO() {
    return documentWordDAO;
  }

  /**
   * @return the journalDAO
   */
  public JournalDAO getJournalDAO() {
    return journalDAO;
  }

  /**
   * @return the journalSubjectCategoryPublishDateDAO
   */
  public JournalSubjectCategoryPublishDateDAO getJournalSubjectCategoryPublishDateDAO() {
    return journalSubjectCategoryPublishDateDAO;
  }

  /**
   * @return the periodDAO
   */
  public PeriodDAO getPeriodDAO() {
    return periodDAO;
  }

  /**
   * @return the publishDateDAO
   */
  public PublishDateDAO getPublishDateDAO() {
    return publishDateDAO;
  }

  /**
   * @return the publishDatePeriodDAO
   */
  public PublishDatePeriodDAO getPublishDatePeriodDAO() {
    return publishDatePeriodDAO;
  }

  /**
   * @return the referenceDAO
   */
  public ReferenceDAO getReferenceDAO() {
    return referenceDAO;
  }

  /**
   * @return the referenceGroupDAO
   */
  public ReferenceGroupDAO getReferenceGroupDAO() {
    return referenceGroupDAO;
  }

  /**
   * @return the referenceSourceDAO
   */
  public ReferenceSourceDAO getReferenceSourceDAO() {
    return referenceSourceDAO;
  }

  /**
   * @return the referenceSourceGroupDAO
   */
  public ReferenceSourceGroupDAO getReferenceSourceGroupDAO() {
    return referenceSourceGroupDAO;
  }

  /**
   * @return the subjectCategoryDAO
   */
  public SubjectCategoryDAO getSubjectCategoryDAO() {
    return subjectCategoryDAO;
  }

  /**
   * @return the wordDAO
   */
  public WordDAO getWordDAO() {
    return wordDAO;
  }

  /**
   * @return the wordGroupDAO
   */
  public WordGroupDAO getWordGroupDAO() {
    return wordGroupDAO;
  }

  /**
   * 
   */
  public StatisticDAO getStatisticDAO() {
    return statisticDAO;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
