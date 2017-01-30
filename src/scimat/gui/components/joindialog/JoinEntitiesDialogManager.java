/*
 * JoinEntitiesDialogManager.java
 *
 * Created on 24-may-2011, 13:56:42
 */
package scimat.gui.components.joindialog;

import java.util.ArrayList;
import javax.swing.JFrame;
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

/**
 *
 * @author mjcobo
 */
public class JoinEntitiesDialogManager {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private AffiliationsJoinDialog affiliationsJoinDialog;
  private AuthorsJoinDialog authorsJoinDialog;
  private AuthorGroupsJoinDialog authorGroupsJoinDialog;
  private AuthorReferencesJoinDialog authorReferencesJoinDialog;
  private AuthorReferenceGroupsJoinDialog authorReferenceGroupsJoinDialog;
  private DocumentsJoinDialog documentsJoinDialog;
  private JournalsJoinDialog journalsJoinDialog;
  private PeriodsJoinDialog periodsJoinDialog;
  private PublishDatesJoinDialog publishDatesJoinDialog;
  private ReferencesJoinDialog referencesJoinDialog;
  private ReferenceGroupsJoinDialog referenceGroupsJoinDialog;
  private ReferenceSourcesJoinDialog referenceSourcesJoinDialog;
  private ReferenceSourceGroupsJoinDialog referenceSourceGroupsJoinDialog;
  private SubjectCategoriesJoinDialog subjectCategoriesJoinDialog;
  private WordsJoinDialog wordsJoinDialog;
  private WordGroupsJoinDialog wordGroupsJoinDialog;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  private JoinEntitiesDialogManager() {
    
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public static JoinEntitiesDialogManager getInstance() {

    return AddDialogManagerHolder.INSTANCE;
  }

  /**
   *
   */
  private static class AddDialogManagerHolder {

    private static final JoinEntitiesDialogManager INSTANCE = new JoinEntitiesDialogManager();
  }

  /**
   *
   * @param frame
   */
  public void init(JFrame frame) {

    this.affiliationsJoinDialog = new AffiliationsJoinDialog(frame);
    this.authorsJoinDialog = new AuthorsJoinDialog(frame);
    this.authorGroupsJoinDialog = new AuthorGroupsJoinDialog(frame);
    this.authorReferencesJoinDialog = new AuthorReferencesJoinDialog(frame);
    this.authorReferenceGroupsJoinDialog = new AuthorReferenceGroupsJoinDialog(frame);
    this.documentsJoinDialog = new DocumentsJoinDialog(frame);
    this.journalsJoinDialog = new JournalsJoinDialog(frame);
    this.periodsJoinDialog = new PeriodsJoinDialog(frame);
    this.publishDatesJoinDialog = new PublishDatesJoinDialog(frame);
    this.referencesJoinDialog = new ReferencesJoinDialog(frame);
    this.referenceGroupsJoinDialog = new ReferenceGroupsJoinDialog(frame);
    this.referenceSourcesJoinDialog = new ReferenceSourcesJoinDialog(frame);
    this.referenceSourceGroupsJoinDialog = new ReferenceSourceGroupsJoinDialog(frame);
    this.subjectCategoriesJoinDialog = new SubjectCategoriesJoinDialog(frame);
    this.wordsJoinDialog = new WordsJoinDialog(frame);
    this.wordGroupsJoinDialog = new WordGroupsJoinDialog(frame);
  }

  /**
   * 
   * @param items 
   */
  public void showAffiliationsJoinDialog(ArrayList<Affiliation> items) {

    this.affiliationsJoinDialog.reset();
    this.affiliationsJoinDialog.refreshData(items);
    this.affiliationsJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showAuthorsJoinDialog(ArrayList<Author> items) {

    this.authorsJoinDialog.reset();
    this.authorsJoinDialog.refreshData(items);
    this.authorsJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showAuthorGroupsJoinDialog(ArrayList<AuthorGroup> items) {

    this.authorGroupsJoinDialog.reset();
    this.authorGroupsJoinDialog.refreshData(items);
    this.authorGroupsJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showAuthorReferencesJoinDialog(ArrayList<AuthorReference> items) {

    this.authorReferencesJoinDialog.reset();
    this.authorReferencesJoinDialog.refreshData(items);
    this.authorReferencesJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showAuthorReferenceGroupsJoinDialog(ArrayList<AuthorReferenceGroup> items) {

    this.authorReferenceGroupsJoinDialog.reset();
    this.authorReferenceGroupsJoinDialog.refreshData(items);
    this.authorReferenceGroupsJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showDocumentsJoinDialog(ArrayList<Document> items) {

    this.documentsJoinDialog.reset();
    this.documentsJoinDialog.refreshData(items);
    this.documentsJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showJournalsJoinDialog(ArrayList<Journal> items) {

    this.journalsJoinDialog.reset();
    this.journalsJoinDialog.refreshData(items);
    this.journalsJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showPeriodsJoinDialog(ArrayList<Period> items) {

    this.periodsJoinDialog.reset();
    this.periodsJoinDialog.refreshData(items);
    this.periodsJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showPublishDatesJoinDialog(ArrayList<PublishDate> items) {

    this.publishDatesJoinDialog.reset();
    this.publishDatesJoinDialog.refreshData(items);
    this.publishDatesJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showReferencesJoinDialog(ArrayList<Reference> items) {

    this.referencesJoinDialog.reset();
    this.referencesJoinDialog.refreshData(items);
    this.referencesJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showReferenceGroupsJoinDialog(ArrayList<ReferenceGroup> items) {

    this.referenceGroupsJoinDialog.reset();
    this.referenceGroupsJoinDialog.refreshData(items);
    this.referenceGroupsJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showReferenceSourcesJoinDialog(ArrayList<ReferenceSource> items) {

    this.referenceSourcesJoinDialog.reset();
    this.referenceSourcesJoinDialog.refreshData(items);
    this.referenceSourcesJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showReferenceSourceGroupsJoinDialog(ArrayList<ReferenceSourceGroup> items) {

    this.referenceSourceGroupsJoinDialog.reset();
    this.referenceSourceGroupsJoinDialog.refreshData(items);
    this.referenceSourceGroupsJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showSubjectCategoriesJoinDialog(ArrayList<SubjectCategory> items) {

    this.subjectCategoriesJoinDialog.reset();
    this.subjectCategoriesJoinDialog.refreshData(items);
    this.subjectCategoriesJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showWordsJoinDialog(ArrayList<Word> items) {

    this.wordsJoinDialog.reset();
    this.wordsJoinDialog.refreshData(items);
    this.wordsJoinDialog.setVisible(true);
  }
  
  /**
   * 
   * @param items 
   */
  public void showWordGroupsJoinDialog(ArrayList<WordGroup> items) {

    this.wordGroupsJoinDialog.reset();
    this.wordGroupsJoinDialog.refreshData(items);
    this.wordGroupsJoinDialog.setVisible(true);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
