/*
 * EditDialogManager.java
 *
 * Created on 18-mar-2011, 13:56:42
 */
package scimat.gui.components.editdialog;


import javax.swing.JFrame;
import scimat.model.knowledgebase.entity.AuthorGroup;
import scimat.model.knowledgebase.entity.AuthorReferenceGroup;
import scimat.model.knowledgebase.entity.AuthorReferenceReference;
import scimat.model.knowledgebase.entity.DocumentAuthor;
import scimat.model.knowledgebase.entity.DocumentWord;
import scimat.model.knowledgebase.entity.ReferenceGroup;
import scimat.model.knowledgebase.entity.ReferenceSourceGroup;
import scimat.model.knowledgebase.entity.WordGroup;

/**
 *
 * @author mjcobo
 */
public class EditDialogManager {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /**
   *
   */
  private EditAuthorGroupDialog editAuthorGroupDialog;

  /**
   *
   */
  private EditAuthorReferenceGroupDialog editAuthorReferenceGroupDialog;

  /**
   *
   */
  private EditReferenceGroupDialog editReferenceGroupDialog;

  /**
   *
   */
  private EditReferenceSourceGroupDialog editReferenceSourceGroupDialog;

  /**
   *
   */
  private EditWordGroupDialog editWordGroupDialog;
  
  private UpdateAuthorReferenceReferencePositionDialog updateAuthorReferenceReferencePositionDialog;
  private UpdateDocumentAuthorPositionDialog updateDocumentAuthorPositionDialog;
  private UpdateDocumentWordRolDialog updateDocumentWordRolDialog;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  private EditDialogManager() {
    
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public static EditDialogManager getInstance() {

    return EditDialogManagerHolder.INSTANCE;
  }

  /**
   *
   */
  private static class EditDialogManagerHolder {

    private static final EditDialogManager INSTANCE = new EditDialogManager();
  }

  /**
   *
   * @param frame
   */
  public void init(JFrame frame) {
    
    this.editAuthorGroupDialog = new EditAuthorGroupDialog(frame);
    this.editAuthorReferenceGroupDialog = new EditAuthorReferenceGroupDialog(frame);
    this.editReferenceGroupDialog = new EditReferenceGroupDialog(frame);
    this.editReferenceSourceGroupDialog = new EditReferenceSourceGroupDialog(frame);
    this.editWordGroupDialog = new EditWordGroupDialog(frame);
    this.updateAuthorReferenceReferencePositionDialog = new UpdateAuthorReferenceReferencePositionDialog(frame);
    this.updateDocumentAuthorPositionDialog = new UpdateDocumentAuthorPositionDialog(frame);
    this.updateDocumentWordRolDialog = new UpdateDocumentWordRolDialog(frame);
  }
  
  /**
   *
   */
  public void showEditAuthorGroupDialog(AuthorGroup authorGroup) {

    this.editAuthorGroupDialog.refreshData(authorGroup);
    this.editAuthorGroupDialog.setVisible(true);
  }

  /**
   *
   */
  public void showEditAuthorReferenceGroupDialog(AuthorReferenceGroup authorReferenceGroup) {

    this.editAuthorReferenceGroupDialog.refreshData(authorReferenceGroup);
    this.editAuthorReferenceGroupDialog.setVisible(true);
  }

  /**
   *
   */
  public void showEditReferenceGroupDialog(ReferenceGroup referenceGroup) {

    this.editReferenceGroupDialog.refreshData(referenceGroup);
    this.editReferenceGroupDialog.setVisible(true);
  }
  
  /**
   *
   */
  public void showEditReferenceSourceGroupDialog(ReferenceSourceGroup referenceSourceGroup) {

    this.editReferenceSourceGroupDialog.refreshData(referenceSourceGroup);
    this.editReferenceSourceGroupDialog.setVisible(true);
  }
  
  /**
   * 
   */
  public void showEditWordGroupDialog(WordGroup wordGroup) {

    this.editWordGroupDialog.refreshData(wordGroup);
    this.editWordGroupDialog.setVisible(true);
  }
  
  /**
   * 
   * @param authorReferenceReference 
   */
  public void showUpdateAuthorReferenceReferencePositionDialog(AuthorReferenceReference authorReferenceReference) {

    this.updateAuthorReferenceReferencePositionDialog.refreshData(authorReferenceReference);
    this.updateAuthorReferenceReferencePositionDialog.setVisible(true);
  }
  
  /**
   * 
   * @param documentAuthor 
   */
  public void showUpdateDocumentAuthorPositionDialog(DocumentAuthor documentAuthor) {

    this.updateDocumentAuthorPositionDialog.refreshData(documentAuthor);
    this.updateDocumentAuthorPositionDialog.setVisible(true);
  }
  
  /**
   * 
   * @param documentWord 
   */
  public void showUpdateDocumentWordRolDialog(DocumentWord documentWord) {

    this.updateDocumentWordRolDialog.refreshData(documentWord);
    this.updateDocumentWordRolDialog.setVisible(true);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
