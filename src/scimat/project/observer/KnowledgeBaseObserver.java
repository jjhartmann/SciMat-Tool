/*
 * KnowledgeBaseObserver.java
 *
 * Created on 08-mar-2011, 18:56:09
 */
package scimat.project.observer;

import java.util.ArrayList;
import scimat.model.knowledgebase.entity.Affiliation;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.entity.Document;
import scimat.model.knowledgebase.entity.Journal;
import scimat.model.knowledgebase.entity.Period;
import scimat.model.knowledgebase.entity.PublishDate;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.entity.SubjectCategory;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class KnowledgeBaseObserver {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  // ------------------------------------------------------------------------

  /**
   *
   */
  private ArrayList<EntityObserver<Affiliation>> affiliationObservers = new ArrayList<EntityObserver<Affiliation>>();

  /**
   *
   */
  private ArrayList<EntityObserver<Author>> authorObservers = new ArrayList<EntityObserver<Author>>();

  /**
   *
   */
  private ArrayList<EntityObserver<AuthorGroup>> authorGroupObservers = new ArrayList<EntityObserver<AuthorGroup>>();

  /**
   *
   */
  private ArrayList<EntityObserver<AuthorReference>> authorReferenceObservers = new ArrayList<EntityObserver<AuthorReference>>();

  /**
   *
   */
  private ArrayList<EntityObserver<AuthorReferenceGroup>> authorReferenceGroupObservers = new ArrayList<EntityObserver<AuthorReferenceGroup>>();

  /**
   *
   */
  private ArrayList<EntityObserver<Document>> documentObservers = new ArrayList<EntityObserver<Document>>();

  /**
   *
   */
  private ArrayList<EntityObserver<Journal>> journalObservers = new ArrayList<EntityObserver<Journal>>();

  /**
   *
   */
  private ArrayList<EntityObserver<Period>> periodObservers = new ArrayList<EntityObserver<Period>>();

  /**
   *
   */
  private ArrayList<EntityObserver<PublishDate>> publishDateObservers = new ArrayList<EntityObserver<PublishDate>>();

  /**
   *
   */
  private ArrayList<EntityObserver<Reference>> referenceObservers = new ArrayList<EntityObserver<Reference>>();
  
  /**
   *
   */
  private ArrayList<EntityObserver<ReferenceGroup>> referenceGroupObservers = new ArrayList<EntityObserver<ReferenceGroup>>();

  /**
   *
   */
  private ArrayList<EntityObserver<ReferenceSource>> referenceSourceObservers = new ArrayList<EntityObserver<ReferenceSource>>();

  /**
   *
   */
  private ArrayList<EntityObserver<ReferenceSourceGroup>> referenceSourceGroupObservers = new ArrayList<EntityObserver<ReferenceSourceGroup>>();

  /**
   *
   */
  private ArrayList<EntityObserver<SubjectCategory>> subjectCategoryObservers = new ArrayList<EntityObserver<SubjectCategory>>();

  /**
   *
   */
  private ArrayList<EntityObserver<Word>> wordObservers = new ArrayList<EntityObserver<Word>>();

  /**
   *
   */
  private ArrayList<EntityObserver<WordGroup>> wordGroupObservers = new ArrayList<EntityObserver<WordGroup>>();

  // ------------------------------------------------------------------------

  /**
   *
   */
  private ArrayList<EntityObserver<Author>> authorWithoutGroupObservers = new ArrayList<EntityObserver<Author>>();

  /**
   *
   */
  private ArrayList<EntityObserver<AuthorReference>> authorReferenceWithoutGroupObservers = new ArrayList<EntityObserver<AuthorReference>>();

  /**
   *
   */
  private ArrayList<EntityObserver<Reference>> referenceWithoutGroupObservers = new ArrayList<EntityObserver<Reference>>();

  /**
   *
   */
  private ArrayList<EntityObserver<ReferenceSource>> referenceSourceWithoutGroupObservers = new ArrayList<EntityObserver<ReferenceSource>>();

  /**
   *
   */
  private ArrayList<EntityObserver<Word>> wordWithoutGroupObservers = new ArrayList<EntityObserver<Word>>();

  // ------------------------------------------------------------------------

  /**
   *
   */
  private ArrayList<AuthorGroupRelationAuthorObserver> authorGroupRelationAuthorsObservers = new ArrayList<AuthorGroupRelationAuthorObserver>();

  /**
   *
   */
  private ArrayList<AuthorReferenceGroupRelationAuthorReferenceObserver> authorReferenceGroupRelationAuthorReferencesObservers = new ArrayList<AuthorReferenceGroupRelationAuthorReferenceObserver>();

  /**
   *
   */
  private ArrayList<ReferenceGroupRelationReferenceObserver> referenceGroupRelationReferencesObservers = new ArrayList<ReferenceGroupRelationReferenceObserver>();

  /**
   *
   */
  private ArrayList<ReferenceSourceRelationReferenceObserver> referenceSourceRelationReferenceObservers = new ArrayList<ReferenceSourceRelationReferenceObserver>();

  /**
   *
   */
  private ArrayList<ReferenceSourceGroupRelationReferenceSourceObserver> referenceSourceGroupRelationReferenceSourcesObservers = new ArrayList<ReferenceSourceGroupRelationReferenceSourceObserver>();

  /**
   *
   */
  private ArrayList<WordGroupRelationWordObserver> wordGroupRelationWordsObservers = new ArrayList<WordGroupRelationWordObserver>();

  /**
   *
   */
  private ArrayList<JournalRelationDocumentObserver> journalRelationDocumentsObservers = new ArrayList<JournalRelationDocumentObserver>();

  /**
   *
   */
  private ArrayList<PublishDateRelationDocumentObserver> publishDateRelationDocumentsObservers = new ArrayList<PublishDateRelationDocumentObserver>();

  // ------------------------------------------------------------------------

  /**
   * 
   */
  private ArrayList<AuthorRelationAuthorReferenceObserver> authorRelationAuthorReferenceObservers = new ArrayList<AuthorRelationAuthorReferenceObserver>();

  // ------------------------------------------------------------------------

  /**
   *
   */
  private ArrayList<PeriodRelationPublishDateObserver> periodsRelationPublishDatesObservers = new ArrayList<PeriodRelationPublishDateObserver>();

  /**
   * 
   */
  private ArrayList<DocumentRelationReferenceObserver> documentsRelationReferencesObservers = new ArrayList<DocumentRelationReferenceObserver>();

  /**
   *
   */
  private ArrayList<DocumentRelationAffiliationObserver> documentsRelationAffiliationsObservers = new ArrayList<DocumentRelationAffiliationObserver>();

  /**
   *
   */
  private ArrayList<AuthorRelationAffiliationObserver> authorsRelationAffiliationsObservers = new ArrayList<AuthorRelationAffiliationObserver>();

  // ------------------------------------------------------------------------

  /**
   *
   */
  private ArrayList<DocumentRelationAuthorObserver> documentAuthorObservers = new ArrayList<DocumentRelationAuthorObserver>();

  /**
   * 
   */
  private ArrayList<DocumentRelationWordObserver> documentsRelationsWordObservers = new ArrayList<DocumentRelationWordObserver>();

  /**
   *
   */
  private ArrayList<ReferenceRelationAuthorReferenceObserver> referenceRelationAuthorReferenceObservers = new ArrayList<ReferenceRelationAuthorReferenceObserver>();

  /**
   * 
   */
  private ArrayList<JournalRelationSubjectCategoryRelationPublishDateObserver> journalSubjectCategoryPublishDateObservers = new ArrayList<JournalRelationSubjectCategoryRelationPublishDateObserver>();

  // ------------------------------------------------------------------------

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   */
  public void fireKnowledgeBaseRefresh() throws KnowledgeBaseException {

    fireAffiliationRefresh();
    fireAuthorGroupRefresh();
    fireAuthorRefresh();
    fireAuthorReferenceRefresh();
    fireAuthorReferenceGroupRefresh();
    fireDocumentRefresh();
    fireJournalRefresh();
    firePeriodRefresh();
    firePublishDateRefresh();
    fireReferenceGroupRefresh();
    fireReferenceRefresh();
    fireReferenceSourceGroupRefresh();
    fireReferenceSourceRefresh();
    fireSubjectCategoryRefresh();
    fireWordGroupRefresh();
    fireWordRefresh();
    
    fireAuthorWithoutGroupRefresh();
    fireAuthorReferenceWithoutGroupRefresh();
    fireReferenceWithoutGroupRefresh();
    fireReferenceSourceWithoutGroupRefresh();
    fireWordWithoutGroupRefresh();
  }
  
  /**
   * Add a new observer to the affiliations.
   *
   * @param observer the new observer
   */
  public void addAffiliationObserver(EntityObserver<Affiliation> observer) {

    this.affiliationObservers.add(observer);
  }

  /**
   * Delete a observer from the affiliations
   *
   * @param observer the observer to remove
   */
  public void removeAffiliationObserver(EntityObserver<Affiliation> observer){

    this.affiliationObservers.remove(observer);
  }

  /**
   * Notify to the affiliation's observer that a change has happened in it.
   */
  public void fireAffiliationAdded(ArrayList<Affiliation> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.affiliationObservers.size(); i++) {

      this.affiliationObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the affiliation's observer that a change has happened in it.
   */
  public void fireAffiliationRemoved(ArrayList<Affiliation> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.affiliationObservers.size(); i++) {

      this.affiliationObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the affiliation's observer that a change has happened in it.
   */
  public void fireAffiliationUpdated(ArrayList<Affiliation> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.affiliationObservers.size(); i++) {

      this.affiliationObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the affiliation's observer that a change has happened in it.
   */
  public void fireAffiliationRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.affiliationObservers.size(); i++) {

      this.affiliationObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the authors.
   *
   * @param observer the new observer
   */
  public void addAuthorObserver(EntityObserver<Author> observer) {

    this.authorObservers.add(observer);
  }

  /**
   * Delete a observer from the authors.
   *
   * @param observer the observer to remove
   */
  public void removeAuthorObserver(EntityObserver<Author> observer){

    this.authorObservers.remove(observer);
  }

  /**
   * Notify to the author's observer that a change has happened in it.
   */
  public void fireAuthorAdded(ArrayList<Author> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorObservers.size(); i++) {

      this.authorObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the author's observer that a change has happened in it.
   */
  public void fireAuthorRemoved(ArrayList<Author> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorObservers.size(); i++) {

      this.authorObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the author's observer that a change has happened in it.
   */
  public void fireAuthorUpdated(ArrayList<Author> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorObservers.size(); i++) {

      this.authorObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the author's observer that a change has happened in it.
   */
  public void fireAuthorRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorObservers.size(); i++) {

      this.authorObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the author group.
   *
   * @param observer the new observer
   */
  public void addAuthorGroupObserver(EntityObserver<AuthorGroup> observer) {

    this.authorGroupObservers.add(observer);
  }

  /**
   * Delete a observer from the author group.
   *
   * @param observer the observer to remove
   */
  public void removeAuthorGroupObserver(EntityObserver<AuthorGroup> observer){

    this.authorGroupObservers.remove(observer);
  }

  /**
   * Notify to the author group's observer that a change has happened in it.
   */
  public void fireAuthorGroupAdded(ArrayList<AuthorGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorGroupObservers.size(); i++) {

      this.authorGroupObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the author group's observer that a change has happened in it.
   */
  public void fireAuthorGroupRemoved(ArrayList<AuthorGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorGroupObservers.size(); i++) {

      this.authorGroupObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the author group's observer that a change has happened in it.
   */
  public void fireAuthorGroupUpdated(ArrayList<AuthorGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorGroupObservers.size(); i++) {

      this.authorGroupObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the author group's observer that a change has happened in it.
   */
  public void fireAuthorGroupRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorGroupObservers.size(); i++) {

      this.authorGroupObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the author references.
   *
   * @param observer the new observer
   */
  public void addAuthorReferenceObserver(EntityObserver<AuthorReference> observer) {

    this.authorReferenceObservers.add(observer);
  }

  /**
   * Delete a observer from the author references.
   *
   * @param observer the observer to remove
   */
  public void removeAuthorReferenceObserver(EntityObserver<AuthorReference> observer){

    this.authorReferenceObservers.remove(observer);
  }

  /**
   * Notify to the author reference's observer that a change has happened in it.
   */
  public void fireAuthorReferenceAdded(ArrayList<AuthorReference> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceObservers.size(); i++) {

      this.authorReferenceObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the author reference's observer that a change has happened in it.
   */
  public void fireAuthorReferenceRemoved(ArrayList<AuthorReference> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceObservers.size(); i++) {

      this.authorReferenceObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the author reference's observer that a change has happened in it.
   */
  public void fireAuthorReferenceUpdated(ArrayList<AuthorReference> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceObservers.size(); i++) {

      this.authorReferenceObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the author reference's observer that a change has happened in it.
   */
  public void fireAuthorReferenceRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceObservers.size(); i++) {

      this.authorReferenceObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the author reference groups.
   *
   * @param observer the new observer
   */
  public void addAuthorReferenceGroupObserver(EntityObserver<AuthorReferenceGroup> observer) {

    this.authorReferenceGroupObservers.add(observer);
  }

  /**
   * Delete a observer from the author reference groups.
   *
   * @param observer the observer to remove
   */
  public void removeAuthorReferenceGroupObserver(EntityObserver<AuthorReferenceGroup> observer){

    this.authorReferenceGroupObservers.remove(observer);
  }

  /**
   * Notify to the author reference group's observer that a change has
   * happened in it.
   */
  public void fireAuthorReferenceGroupAdded(ArrayList<AuthorReferenceGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceGroupObservers.size(); i++) {

      this.authorReferenceGroupObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the author reference group's observer that a change has
   * happened in it.
   */
  public void fireAuthorReferenceGroupRemoved(ArrayList<AuthorReferenceGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceGroupObservers.size(); i++) {

      this.authorReferenceGroupObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the author reference group's observer that a change has
   * happened in it.
   */
  public void fireAuthorReferenceGroupUpdated(ArrayList<AuthorReferenceGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceGroupObservers.size(); i++) {

      this.authorReferenceGroupObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the author reference group's observer that a change has
   * happened in it.
   */
  public void fireAuthorReferenceGroupRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceGroupObservers.size(); i++) {

      this.authorReferenceGroupObservers.get(i).entityRefresh();
    }
  }
  
  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the documents.
   *
   * @param observer the new observer
   */
  public void addDocumentObserver(EntityObserver<Document> observer) {

    this.documentObservers.add(observer);
  }

  /**
   * Delete a observer from the documents
   *
   * @param observer the observer to remove
   */
  public void removeDocumentObserver(EntityObserver<Document> observer){

    this.documentObservers.remove(observer);
  }

  /**
   * Notify to the document's observer that a documents have been updated.
   */
  public void fireDocumentUpdated(ArrayList<Document> documents)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.documentObservers.size(); i++) {

      this.documentObservers.get(i).entityUpdated(documents);
    }
  }

  /**
   * Notify to the document's observer that a documents have been added.
   */
  public void fireDocumentAdded(ArrayList<Document> documents) 
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.documentObservers.size(); i++) {

      this.documentObservers.get(i).entityAdded(documents);
    }
  }

  /**
   * Notify to the document's observer that a documents have been removed.
   */
  public void fireDocumentRemoved(ArrayList<Document> documents) 
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.documentObservers.size(); i++) {

      this.documentObservers.get(i).entityRemoved(documents);
    }
  }

  /**
   * Notify to the document's observer that a refresh of the data is needed.
   */
  public void fireDocumentRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.documentObservers.size(); i++) {

      this.documentObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the journals.
   *
   * @param observer the new observer
   */
  public void addJournalObserver(EntityObserver<Journal> observer) {

    this.journalObservers.add(observer);
  }

  /**
   * Delete a observer from the journals
   *
   * @param observer the observer to remove
   */
  public void removeJournalObserver(EntityObserver<Journal> observer){

    this.journalObservers.remove(observer);
  }

  /**
   * Notify to the journal's observer that a change has happened in it.
   */
  public void fireJournalAdded(ArrayList<Journal> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.journalObservers.size(); i++) {

      this.journalObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the journal's observer that a change has happened in it.
   */
  public void fireJournalRemoved(ArrayList<Journal> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.journalObservers.size(); i++) {

      this.journalObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the journal's observer that a change has happened in it.
   */
  public void fireJournalUpdated(ArrayList<Journal> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.journalObservers.size(); i++) {

      this.journalObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the journal's observer that a change has happened in it.
   */
  public void fireJournalRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.journalObservers.size(); i++) {

      this.journalObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the periods.
   *
   * @param observer the new observer
   */
  public void addPeriodObserver(EntityObserver<Period> observer) {

    this.periodObservers.add(observer);
  }

  /**
   * Delete a observer from the period
   *
   * @param observer the observer to remove
   */
  public void removePeriodObserver(EntityObserver<Period> observer){

    this.periodObservers.remove(observer);
  }

  /**
   * Notify to the period's observer that a change has happened in it.
   */
  public void firePeriodAdded(ArrayList<Period> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.periodObservers.size(); i++) {

      this.periodObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the period's observer that a change has happened in it.
   */
  public void firePeriodRemoved(ArrayList<Period> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.periodObservers.size(); i++) {

      this.periodObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the period's observer that a change has happened in it.
   */
  public void firePeriodUpdated(ArrayList<Period> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.periodObservers.size(); i++) {

      this.periodObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the period's observer that a change has happened in it.
   */
  public void firePeriodRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.periodObservers.size(); i++) {

      this.periodObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the publish dates.
   *
   * @param observer the new observer
   */
  public void addPublishDateObserver(EntityObserver<PublishDate> observer) {

    this.publishDateObservers.add(observer);
  }

  /**
   * Delete a observer from the publish dates
   *
   * @param observer the observer to remove
   */
  public void removePublishDateObserver(EntityObserver<PublishDate> observer){

    this.publishDateObservers.remove(observer);
  }

  /**
   * Notify to the publish date's observer that a change has happened in it.
   */
  public void firePublishDateAdded(ArrayList<PublishDate> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.publishDateObservers.size(); i++) {

      this.publishDateObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the publish date's observer that a change has happened in it.
   */
  public void firePublishDateRemoved(ArrayList<PublishDate> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.publishDateObservers.size(); i++) {

      this.publishDateObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the publish date's observer that a change has happened in it.
   */
  public void firePublishDateUpdated(ArrayList<PublishDate> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.publishDateObservers.size(); i++) {

      this.publishDateObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the publish date's observer that a change has happened in it.
   */
  public void firePublishDateRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.publishDateObservers.size(); i++) {
      
      this.publishDateObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the references.
   *
   * @param observer the new observer
   */
  public void addReferenceObserver(EntityObserver<Reference> observer) {

    this.referenceObservers.add(observer);
  }

  /**
   * Delete a observer from the references.
   *
   * @param observer the observer to remove
   */
  public void removeReferenceObserver(EntityObserver<Reference> observer){

    this.referenceObservers.remove(observer);
  }

  /**
   * Notify to the reference's observer that a change has happened in it.
   */
  public void fireReferenceAdded(ArrayList<Reference> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceObservers.size(); i++) {

      this.referenceObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the reference's observer that a change has happened in it.
   */
  public void fireReferenceRemoved(ArrayList<Reference> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceObservers.size(); i++) {

      this.referenceObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the reference's observer that a change has happened in it.
   */
  public void fireReferenceUpdated(ArrayList<Reference> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceObservers.size(); i++) {

      this.referenceObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the reference's observer that a change has happened in it.
   */
  public void fireReferenceRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceObservers.size(); i++) {

      this.referenceObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the reference groups.
   *
   * @param observer the new observer
   */
  public void addReferenceGroupObserver(EntityObserver<ReferenceGroup> observer) {

    this.referenceGroupObservers.add(observer);
  }

  /**
   * Delete a observer from the reference group.
   *
   * @param observer the observer to remove
   */
  public void removeReferenceGroupObserver(EntityObserver<ReferenceGroup> observer){

    this.referenceGroupObservers.remove(observer);
  }

  /**
   * Notify to the reference group's observer that a change has happened in it.
   */
  public void fireReferenceGroupAdded(ArrayList<ReferenceGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceGroupObservers.size(); i++) {

      this.referenceGroupObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the reference group's observer that a change has happened in it.
   */
  public void fireReferenceGroupRemoved(ArrayList<ReferenceGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceGroupObservers.size(); i++) {

      this.referenceGroupObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the reference group's observer that a change has happened in it.
   */
  public void fireReferenceGroupUpdated(ArrayList<ReferenceGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceGroupObservers.size(); i++) {

      this.referenceGroupObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the reference group's observer that a change has happened in it.
   */
  public void fireReferenceGroupRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceGroupObservers.size(); i++) {

      this.referenceGroupObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the reference sources.
   *
   * @param observer the new observer
   */
  public void addReferenceSourceObserver(EntityObserver<ReferenceSource> observer) {

    this.referenceSourceObservers.add(observer);
  }

  /**
   * Delete a observer from the reference sources.
   *
   * @param observer the observer to remove
   */
  public void removeReferenceSourceObserver(EntityObserver<ReferenceSource> observer){

    this.referenceSourceObservers.remove(observer);
  }

  /**
   * Notify to the reference source's observer that a change has happened in it.
   */
  public void fireReferenceSourceAdded(ArrayList<ReferenceSource> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceObservers.size(); i++) {

      this.referenceSourceObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the reference source's observer that a change has happened in it.
   */
  public void fireReferenceSourceRemoved(ArrayList<ReferenceSource> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceObservers.size(); i++) {

      this.referenceSourceObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the reference source's observer that a change has happened in it.
   */
  public void fireReferenceSourceUpdated(ArrayList<ReferenceSource> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceObservers.size(); i++) {

      this.referenceSourceObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the reference source's observer that a change has happened in it.
   */
  public void fireReferenceSourceRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceObservers.size(); i++) {

      this.referenceSourceObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the reference source groups.
   *
   * @param observer the new observer
   */
  public void addReferenceSourceGroupObserver(EntityObserver<ReferenceSourceGroup> observer) {

    this.referenceSourceGroupObservers.add(observer);
  }

  /**
   * Delete a observer from the reference source groups.
   *
   * @param observer the observer to remove
   */
  public void removeReferenceSourceGroupObserver(EntityObserver<ReferenceSourceGroup> observer){

    this.referenceSourceGroupObservers.remove(observer);
  }

  /**
   * Notify to the reference source group's observer that a change has happened in it.
   */
  public void fireReferenceSourceGroupAdded(ArrayList<ReferenceSourceGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceGroupObservers.size(); i++) {

      this.referenceSourceGroupObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the reference source group's observer that a change has happened in it.
   */
  public void fireReferenceSourceGroupRemoved(ArrayList<ReferenceSourceGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceGroupObservers.size(); i++) {

      this.referenceSourceGroupObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the reference source group's observer that a change has happened in it.
   */
  public void fireReferenceSourceGroupUpdated(ArrayList<ReferenceSourceGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceGroupObservers.size(); i++) {

      this.referenceSourceGroupObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the reference source group's observer that a change has happened in it.
   */
  public void fireReferenceSourceGroupRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceGroupObservers.size(); i++) {

      this.referenceSourceGroupObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the subject categories.
   *
   * @param observer the new observer
   */
  public void addSubjectCategoryObserver(EntityObserver<SubjectCategory> observer) {

    this.subjectCategoryObservers.add(observer);
  }

  /**
   * Delete a observer from the subject categories.
   *
   * @param observer the observer to remove
   */
  public void removeSubjectCategoryObserver(EntityObserver<SubjectCategory> observer){

    this.subjectCategoryObservers.remove(observer);
  }

  /**
   * Notify to the subject category's observer that a change has happened in it.
   */
  public void fireSubjectCategoryAdded(ArrayList<SubjectCategory> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.subjectCategoryObservers.size(); i++) {

      this.subjectCategoryObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the subject category's observer that a change has happened in it.
   */
  public void fireSubjectCategoryRemoved(ArrayList<SubjectCategory> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.subjectCategoryObservers.size(); i++) {

      this.subjectCategoryObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the subject category's observer that a change has happened in it.
   */
  public void fireSubjectCategoryUpdated(ArrayList<SubjectCategory> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.subjectCategoryObservers.size(); i++) {

      this.subjectCategoryObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the subject category's observer that a change has happened in it.
   */
  public void fireSubjectCategoryRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.subjectCategoryObservers.size(); i++) {

      this.subjectCategoryObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the words.
   *
   * @param observer the new observer
   */
  public void addWordObserver(EntityObserver<Word> observer) {

    this.wordObservers.add(observer);
  }

  /**
   * Delete a observer from the words.
   *
   * @param observer the observer to remove
   */
  public void removeWordObserver(EntityObserver<Word> observer){

    this.wordObservers.remove(observer);
  }

  /**
   * Notify to the wordGroup's observer that a change has happened in it.
   */
  public void fireWordAdded(ArrayList<Word> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordObservers.size(); i++) {

      this.wordObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the wordGroup's observer that a change has happened in it.
   */
  public void fireWordRemoved(ArrayList<Word> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordObservers.size(); i++) {

      this.wordObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the wordGroup's observer that a change has happened in it.
   */
  public void fireWordUpdated(ArrayList<Word> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordObservers.size(); i++) {

      this.wordObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the wordGroup's observer that a change has happened in it.
   */
  public void fireWordRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordObservers.size(); i++) {

      this.wordObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the word groups.
   *
   * @param observer the new observer
   */
  public void addWordGroupObserver(EntityObserver<WordGroup> observer) {

    this.wordGroupObservers.add(observer);
  }

  /**
   * Delete a observer from the word groups.
   *
   * @param observer the observer to remove
   */
  public void removeWordGroupObserver(EntityObserver<WordGroup> observer){

    this.wordGroupObservers.remove(observer);
  }

  /**
   * Notify to the word group's observer that a change has happened in it.
   */
  public void fireWordGroupAdded(ArrayList<WordGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordGroupObservers.size(); i++) {

      this.wordGroupObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the word group's observer that a change has happened in it.
   */
  public void fireWordGroupRemoved(ArrayList<WordGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordGroupObservers.size(); i++) {

      this.wordGroupObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the word group's observer that a change has happened in it.
   */
  public void fireWordGroupUpdated(ArrayList<WordGroup> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordGroupObservers.size(); i++) {

      this.wordGroupObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the word group's observer that a change has happened in it.
   */
  public void fireWordGroupRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordGroupObservers.size(); i++) {

      this.wordGroupObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the authors.
   *
   * @param observer the new observer
   */
  public void addAuthorWithoutGroupObserver(EntityObserver<Author> observer) {

    this.authorWithoutGroupObservers.add(observer);
  }

  /**
   * Delete a observer from the authors.
   *
   * @param observer the observer to remove
   */
  public void removeAuthorWithoutGroupObserver(EntityObserver<Author> observer){

    this.authorWithoutGroupObservers.remove(observer);
  }

  /**
   * Notify to the author's observer that a change has happened in it.
   */
  public void fireAuthorWithoutGroupAdded(ArrayList<Author> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorWithoutGroupObservers.size(); i++) {

      this.authorWithoutGroupObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the author's observer that a change has happened in it.
   */
  public void fireAuthorWithoutGroupRemoved(ArrayList<Author> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorWithoutGroupObservers.size(); i++) {

      this.authorWithoutGroupObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the author's observer that a change has happened in it.
   */
  public void fireAuthorWithoutGroupUpdated(ArrayList<Author> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorWithoutGroupObservers.size(); i++) {

      this.authorWithoutGroupObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the author's observer that a change has happened in it.
   */
  public void fireAuthorWithoutGroupRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorWithoutGroupObservers.size(); i++) {

      this.authorWithoutGroupObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the author references.
   *
   * @param observer the new observer
   */
  public void addAuthorReferenceWithoutGroupObserver(EntityObserver<AuthorReference> observer) {

    this.authorReferenceWithoutGroupObservers.add(observer);
  }

  /**
   * Delete a observer from the author references.
   *
   * @param observer the observer to remove
   */
  public void removeAuthorReferenceWithoutGroupObserver(EntityObserver<AuthorReference> observer){

    this.authorReferenceWithoutGroupObservers.remove(observer);
  }

  /**
   * Notify to the author reference's observer that a change has happened in it.
   */
  public void fireAuthorReferenceWithoutGroupAdded(ArrayList<AuthorReference> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceWithoutGroupObservers.size(); i++) {

      this.authorReferenceWithoutGroupObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the author reference's observer that a change has happened in it.
   */
  public void fireAuthorReferenceWithoutGroupRemoved(ArrayList<AuthorReference> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceWithoutGroupObservers.size(); i++) {

      this.authorReferenceWithoutGroupObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the author reference's observer that a change has happened in it.
   */
  public void fireAuthorReferenceWithoutGroupUpdated(ArrayList<AuthorReference> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceWithoutGroupObservers.size(); i++) {

      this.authorReferenceWithoutGroupObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the author reference's observer that a change has happened in it.
   */
  public void fireAuthorReferenceWithoutGroupRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceWithoutGroupObservers.size(); i++) {

      this.authorReferenceWithoutGroupObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the references.
   *
   * @param observer the new observer
   */
  public void addReferenceWithoutGroupObserver(EntityObserver<Reference> observer) {

    this.referenceWithoutGroupObservers.add(observer);
  }

  /**
   * Delete a observer from the references.
   *
   * @param observer the observer to remove
   */
  public void removeReferenceWithoutGroupObserver(EntityObserver<Reference> observer){

    this.referenceWithoutGroupObservers.remove(observer);
  }

  /**
   * Notify to the reference's observer that a change has happened in it.
   */
  public void fireReferenceWithoutGroupAdded(ArrayList<Reference> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceWithoutGroupObservers.size(); i++) {

      this.referenceWithoutGroupObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the reference's observer that a change has happened in it.
   */
  public void fireReferenceWithoutGroupRemoved(ArrayList<Reference> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceWithoutGroupObservers.size(); i++) {

      this.referenceWithoutGroupObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the reference's observer that a change has happened in it.
   */
  public void fireReferenceWithoutGroupUpdated(ArrayList<Reference> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceWithoutGroupObservers.size(); i++) {

      this.referenceWithoutGroupObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the reference's observer that a change has happened in it.
   */
  public void fireReferenceWithoutGroupRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceWithoutGroupObservers.size(); i++) {

      this.referenceWithoutGroupObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the reference sources.
   *
   * @param observer the new observer
   */
  public void addReferenceSourceWithoutGroupObserver(EntityObserver<ReferenceSource> observer) {

    this.referenceSourceWithoutGroupObservers.add(observer);
  }

  /**
   * Delete a observer from the reference sources.
   *
   * @param observer the observer to remove
   */
  public void removeReferenceSourceWithoutGroupObserver(EntityObserver<ReferenceSource> observer){

    this.referenceSourceWithoutGroupObservers.remove(observer);
  }

  /**
   * Notify to the reference source's observer that a change has happened in it.
   */
  public void fireReferenceSourceWithoutGroupAdded(ArrayList<ReferenceSource> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceWithoutGroupObservers.size(); i++) {

      this.referenceSourceWithoutGroupObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the reference source's observer that a change has happened in it.
   */
  public void fireReferenceSourceWithoutGroupRemoved(ArrayList<ReferenceSource> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceWithoutGroupObservers.size(); i++) {

      this.referenceSourceWithoutGroupObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the reference source's observer that a change has happened in it.
   */
  public void fireReferenceSourceWithoutGroupUpdated(ArrayList<ReferenceSource> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceWithoutGroupObservers.size(); i++) {

      this.referenceSourceWithoutGroupObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the reference source's observer that a change has happened in it.
   */
  public void fireReferenceSourceWithoutGroupRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceWithoutGroupObservers.size(); i++) {

      this.referenceSourceWithoutGroupObservers.get(i).entityRefresh();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * Add a new observer to the words.
   *
   * @param observer the new observer
   */
  public void addWordWithoutGroupObserver(EntityObserver<Word> observer) {

    this.wordWithoutGroupObservers.add(observer);
  }

  /**
   * Delete a observer from the words.
   *
   * @param observer the observer to remove
   */
  public void removeWordWithoutGroupObserver(EntityObserver<Word> observer){

    this.wordWithoutGroupObservers.remove(observer);
  }

  /**
   * Notify to the wordGroup's observer that a change has happened in it.
   */
  public void fireWordWithoutGroupAdded(ArrayList<Word> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordWithoutGroupObservers.size(); i++) {

      this.wordWithoutGroupObservers.get(i).entityAdded(items);
    }
  }

  /**
   * Notify to the wordGroup's observer that a change has happened in it.
   */
  public void fireWordWithoutGroupRemoved(ArrayList<Word> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordWithoutGroupObservers.size(); i++) {

      this.wordWithoutGroupObservers.get(i).entityRemoved(items);
    }
  }

  /**
   * Notify to the wordGroup's observer that a change has happened in it.
   */
  public void fireWordWithoutGroupUpdated(ArrayList<Word> items)
          throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordWithoutGroupObservers.size(); i++) {

      this.wordWithoutGroupObservers.get(i).entityUpdated(items);
    }
  }

  /**
   * Notify to the wordGroup's observer that a change has happened in it.
   */
  public void fireWordWithoutGroupRefresh() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordWithoutGroupObservers.size(); i++) {

      this.wordWithoutGroupObservers.get(i).entityRefresh();
    }
  }


  // ------------------------------------------------------------------------

  /**
   *
   * @param observer
   */
  public void addAuthorGroupRelationAuthorsObserver(AuthorGroupRelationAuthorObserver observer) {

    this.authorGroupRelationAuthorsObservers.add(observer);
  }
  
  /**
   *
   * @param observer
   */
  public void removeAuthorGroupRelationAuthorsObserver(AuthorGroupRelationAuthorObserver observer) {

    this.authorGroupRelationAuthorsObservers.remove(observer);
  }

  /**
   * 
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireAuthorGroupRelationAuthorsChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorGroupRelationAuthorsObservers.size(); i++) {

      this.authorGroupRelationAuthorsObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------
  
  /**
   *
   * @param observer
   */
  public void addAuthorReferenceGroupRelationAuthorReferenceObserver(AuthorReferenceGroupRelationAuthorReferenceObserver observer) {

    this.authorReferenceGroupRelationAuthorReferencesObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeAuthorReferenceGroupRelationAuthorReferenceObserver(AuthorReferenceGroupRelationAuthorReferenceObserver observer) {

    this.authorReferenceGroupRelationAuthorReferencesObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireAuthorReferenceGroupRelationAuthorReferencesChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorReferenceGroupRelationAuthorReferencesObservers.size(); i++) {

      this.authorReferenceGroupRelationAuthorReferencesObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * 
   * @param observer
   */
  public void addReferenceGroupRelationReferencesObserver(ReferenceGroupRelationReferenceObserver observer) {

    this.referenceGroupRelationReferencesObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeReferenceGroupRelationReferencesObserver(ReferenceGroupRelationReferenceObserver observer) {

    this.referenceGroupRelationReferencesObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireReferenceGroupRelationReferencesChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceGroupRelationReferencesObservers.size(); i++) {

      this.referenceGroupRelationReferencesObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------


  /**
   *
   * @param observer
   */
  public void addReferenceSourceRelationReferenceObserver(ReferenceSourceRelationReferenceObserver observer) {

    this.referenceSourceRelationReferenceObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeReferenceSourceRelationReferenceObserver(ReferenceSourceRelationReferenceObserver observer) {

    this.referenceSourceRelationReferenceObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireReferenceSourceRelationReferencesChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceRelationReferenceObservers.size(); i++) {

      this.referenceSourceRelationReferenceObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * 
   * @param observer
   */
  public void addReferenceSourceGroupRelationReferenceSourcesObserver(ReferenceSourceGroupRelationReferenceSourceObserver observer) {

    this.referenceSourceGroupRelationReferenceSourcesObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeReferenceSourceGroupRelationReferenceSourcesObserver(ReferenceSourceGroupRelationReferenceSourceObserver observer) {

    this.referenceSourceGroupRelationReferenceSourcesObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireReferenceSourceGroupRelationReferenceSourcesChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceSourceGroupRelationReferenceSourcesObservers.size(); i++) {

      this.referenceSourceGroupRelationReferenceSourcesObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * 
   * @param observer
   */
  public void addWordGroupRelationWordsObserver(WordGroupRelationWordObserver observer) {

    this.wordGroupRelationWordsObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeWordGroupRelationWordsObserver(WordGroupRelationWordObserver observer) {

    this.wordGroupRelationWordsObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireWordGroupRelationWordsChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.wordGroupRelationWordsObservers.size(); i++) {

      this.wordGroupRelationWordsObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * 
   * @param observer
   */
  public void addJournalRelationDocumentsObserver(JournalRelationDocumentObserver observer) {

    this.journalRelationDocumentsObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeJournalRelationDocumentsObserver(JournalRelationDocumentObserver observer) {

    this.journalRelationDocumentsObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireJournalRelationDocumentsChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.journalRelationDocumentsObservers.size(); i++) {

      this.journalRelationDocumentsObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * 
   * @param observer
   */
  public void addPublishDateRelationDocumentsObserver(PublishDateRelationDocumentObserver observer) {

    this.publishDateRelationDocumentsObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removePublishDateRelationDocumentsObserver(PublishDateRelationDocumentObserver observer) {

    this.publishDateRelationDocumentsObservers.add(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void firePublishDateRelationDocumentsChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.publishDateRelationDocumentsObservers.size(); i++) {

      this.publishDateRelationDocumentsObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   *
   * @param observer
   */
  public void addAuthorRelationAuthorReferenceObserver(AuthorRelationAuthorReferenceObserver observer) {

    this.authorRelationAuthorReferenceObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeAuthorRelationAuthorReferenceObserver(AuthorRelationAuthorReferenceObserver observer) {

    this.authorRelationAuthorReferenceObservers.add(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireAuthorRelationAuthorReferencesChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorRelationAuthorReferenceObservers.size(); i++) {

      this.authorRelationAuthorReferenceObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * 
   * @param observer
   */
  public void addPeriodsRelationPublishDatesObservers(PeriodRelationPublishDateObserver observer) {

    this.periodsRelationPublishDatesObservers.add(observer);
  }
  
  /**
   * 
   * @param observer
   */
  public void removePeriodsRelationPublishDatesObservers(PeriodRelationPublishDateObserver observer) {

    this.periodsRelationPublishDatesObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void firePeriodsRelationPublishDatesChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.periodsRelationPublishDatesObservers.size(); i++) {

      this.periodsRelationPublishDatesObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   *
   * @param observer
   */
  public void addDocumentsRelationReferencesObservers(DocumentRelationReferenceObserver observer) {


    this.documentsRelationReferencesObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeDocumentsRelationReferencesObservers(DocumentRelationReferenceObserver observer) {


    this.documentsRelationReferencesObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireDocumentsRelationReferencesChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.documentsRelationReferencesObservers.size(); i++) {

      this.documentsRelationReferencesObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   *
   * @param observer
   */
  public void addDocumentsRelationAffiliationsObservers(DocumentRelationAffiliationObserver observer) {

    this.documentsRelationAffiliationsObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeDocumentsRelationAffiliationsObservers(DocumentRelationAffiliationObserver observer) {

    this.documentsRelationAffiliationsObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireDocumentsRelationAffiliationsChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.documentsRelationAffiliationsObservers.size(); i++) {

      this.documentsRelationAffiliationsObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   * 
   * @param observer
   */
  public void addAuthorRelationAffiliationsObservers(AuthorRelationAffiliationObserver observer) {


    this.authorsRelationAffiliationsObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeAuthorsRelationAffiliationsObservers(AuthorRelationAffiliationObserver observer) {


    this.authorsRelationAffiliationsObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireAuthorsRelationAffiliationsChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.authorsRelationAffiliationsObservers.size(); i++) {

      this.authorsRelationAffiliationsObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   *
   * @param observer
   */
  public void addDocumentAuthorObservers(DocumentRelationAuthorObserver observer) {

    this.documentAuthorObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeDocumentAuthorObservers(DocumentRelationAuthorObserver observer) {

    this.documentAuthorObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireDocumentsRelationAuthorsChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.documentAuthorObservers.size(); i++) {

      this.documentAuthorObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   *
   * @param observer
   */
  public void addDocumentWordObservers(DocumentRelationWordObserver observer) {

    this.documentsRelationsWordObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeDocumentWordObservers(DocumentRelationWordObserver observer) {

    this.documentsRelationsWordObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireDocumentsRelationWordsChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.documentsRelationsWordObservers.size(); i++) {

      this.documentsRelationsWordObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------

  /**
   *
   * @param observer
   */
  public void addReferenceRelationAuthorReferenceObserver(ReferenceRelationAuthorReferenceObserver observer) {

    this.referenceRelationAuthorReferenceObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeReferenceRelationAuthorReferenceObserver(ReferenceRelationAuthorReferenceObserver observer) {

    this.referenceRelationAuthorReferenceObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireReferenceRelationAuthorReferencesChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.referenceRelationAuthorReferenceObservers.size(); i++) {

      this.referenceRelationAuthorReferenceObservers.get(i).relationChanged();
    }
  }

  // ------------------------------------------------------------------------
  
  /**
   *
   * @param observer
   */
  public void addJournalSubjectCategoryPublishDate(JournalRelationSubjectCategoryRelationPublishDateObserver observer) {

    this.journalSubjectCategoryPublishDateObservers.add(observer);
  }

  /**
   *
   * @param observer
   */
  public void removeJournalSubjectCategoryPublishDate(JournalRelationSubjectCategoryRelationPublishDateObserver observer) {

    this.journalSubjectCategoryPublishDateObservers.remove(observer);
  }

  /**
   *
   * @param relations
   * @throws KnowledgeBaseException
   */
  public void fireJournalSubjectCategoryPublishDatesChanged() throws KnowledgeBaseException {

    int i;

    for (i = 0; i < this.journalSubjectCategoryPublishDateObservers.size(); i++) {

      this.journalSubjectCategoryPublishDateObservers.get(i).relationChanged();
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
