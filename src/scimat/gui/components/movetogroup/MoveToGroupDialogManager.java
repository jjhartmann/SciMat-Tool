/*
 * MoveToGroupDialogManager.java
 *
 * Created on 18-mar-2011, 13:56:42
 */
package scimat.gui.components.movetogroup;


import java.util.ArrayList;
import javax.swing.JFrame;
import scimat.model.knowledgebase.entity.Author;
import scimat.model.knowledgebase.entity.AuthorReference;
import scimat.model.knowledgebase.entity.Reference;
import scimat.model.knowledgebase.entity.ReferenceSource;
import scimat.model.knowledgebase.entity.Word;

/**
 *
 * @author mjcobo
 */
public class MoveToGroupDialogManager {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private MoveAuthorReferencesToNewGroupDialog moveAuthorReferencesToNewGroupDialog;
  private MoveAuthorsToNewGroupDialog moveAuthorsToNewGroupDialog;
  private MoveReferenceSourcesToNewGroupDialog moveReferenceSourcesToNewGroupDialog;
  private MoveReferencesToNewGroupDialog moveReferencesToNewGroupDialog;
  private MoveWordsToNewGroupDialog moveWordsToNewGroupDialog;
  private MoveSimilarAuthorReferencesToNewGroupDialog moveSimilarAuthorReferencesToNewGroupDialog;
  private MoveSimilarAuthorsToNewGroupDialog moveSimilarAuthorsToNewGroupDialog;
  private MoveSimilarReferenceSourcesToNewGroupDialog moveSimilarReferenceSourcesToNewGroupDialog;
  private MoveSimilarReferencesToNewGroupDialog moveSimilarReferencesToNewGroupDialog;
  private MoveSimilarWordsToNewGroupDialog moveSimilarWordsToNewGroupDialog;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  private MoveToGroupDialogManager() {
    
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   * 
   * @return
   */
  public static MoveToGroupDialogManager getInstance() {

    return MoveToGroupDialogHolder.INSTANCE;
  }

  /**
   *
   */
  private static class MoveToGroupDialogHolder {

    private static final MoveToGroupDialogManager INSTANCE = new MoveToGroupDialogManager();
  }

  /**
   *
   * @param frame
   */
  public void init(JFrame frame) {
    
    this.moveAuthorReferencesToNewGroupDialog = new MoveAuthorReferencesToNewGroupDialog(frame);
    this.moveAuthorsToNewGroupDialog = new MoveAuthorsToNewGroupDialog(frame);
    this.moveReferenceSourcesToNewGroupDialog = new MoveReferenceSourcesToNewGroupDialog(frame);
    this.moveReferencesToNewGroupDialog = new MoveReferencesToNewGroupDialog(frame);
    this.moveWordsToNewGroupDialog = new MoveWordsToNewGroupDialog(frame);
    this.moveSimilarAuthorReferencesToNewGroupDialog = new MoveSimilarAuthorReferencesToNewGroupDialog(frame);
    this.moveSimilarAuthorsToNewGroupDialog = new MoveSimilarAuthorsToNewGroupDialog(frame);
    this.moveSimilarReferenceSourcesToNewGroupDialog = new MoveSimilarReferenceSourcesToNewGroupDialog(frame);
    this.moveSimilarReferencesToNewGroupDialog = new MoveSimilarReferencesToNewGroupDialog(frame);
    this.moveSimilarWordsToNewGroupDialog = new MoveSimilarWordsToNewGroupDialog(frame);
  }
  
  /**
   * 
   * @param authorReferences 
   */
  public void showMoveAuthorReferencesToNewGroupDialog(ArrayList<AuthorReference> authorReferences) {

    this.moveAuthorReferencesToNewGroupDialog.refreshData(authorReferences);
    this.moveAuthorReferencesToNewGroupDialog.setVisible(true);
  }
  
  /**
   * 
   * @param authors 
   */
  public void showMoveAuthorsToNewGroupDialog(ArrayList<Author> authors) {

    this.moveAuthorsToNewGroupDialog.refreshData(authors);
    this.moveAuthorsToNewGroupDialog.setVisible(true);
  }
  
  /**
   * 
   * @param references 
   */
  public void showMoveReferencesToNewGroupDialog(ArrayList<Reference> references) {

    this.moveReferencesToNewGroupDialog.refreshData(references);
    this.moveReferencesToNewGroupDialog.setVisible(true);
  }
  
  /**
   * 
   * @param references 
   */
  public void showMoveReferenceSourcesToNewGroupDialog(ArrayList<ReferenceSource> referenceSources) {

    this.moveReferenceSourcesToNewGroupDialog.refreshData(referenceSources);
    this.moveReferenceSourcesToNewGroupDialog.setVisible(true);
  }
  
  /**
   * 
   * @param words 
   */
  public void showMoveWordsToNewGroupDialog(ArrayList<Word> words) {

    this.moveWordsToNewGroupDialog.refreshData(words);
    this.moveWordsToNewGroupDialog.setVisible(true);
  }
  
  /**
   * 
   * @param authorReferences 
   */
  public void showMoveSimilarAuthorReferencesToNewGroupDialog(ArrayList<AuthorReference> authorReferences) {

    this.moveSimilarAuthorReferencesToNewGroupDialog.refreshData(authorReferences);
    this.moveSimilarAuthorReferencesToNewGroupDialog.setVisible(true);
  }
  
  /**
   * 
   * @param authors 
   */
  public void showMoveSimilarAuthorsToNewGroupDialog(ArrayList<Author> authors) {

    this.moveSimilarAuthorsToNewGroupDialog.refreshData(authors);
    this.moveSimilarAuthorsToNewGroupDialog.setVisible(true);
  }
  
  /**
   * 
   * @param references 
   */
  public void showMoveSimilarReferencesToNewGroupDialog(ArrayList<Reference> references) {

    this.moveSimilarReferencesToNewGroupDialog.refreshData(references);
    this.moveSimilarReferencesToNewGroupDialog.setVisible(true);
  }
  
  /**
   * 
   * @param references 
   */
  public void showMoveSimilarReferenceSourcesToNewGroupDialog(ArrayList<ReferenceSource> referenceSources) {

    this.moveSimilarReferenceSourcesToNewGroupDialog.refreshData(referenceSources);
    this.moveSimilarReferenceSourcesToNewGroupDialog.setVisible(true);
  }
  
  /**
   * 
   * @param words 
   */
  public void showMoveSimilarWordsToNewGroupDialog(ArrayList<Word> words) {

    this.moveSimilarWordsToNewGroupDialog.refreshData(words);
    this.moveSimilarWordsToNewGroupDialog.setVisible(true);
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
