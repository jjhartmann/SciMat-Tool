/*
 * JoinWordEdit.java
 *
 * Created on 26-abr-2011, 18:06:00
 */
package scimat.gui.commands.edit.join;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.undo.CannotUndoException;
import scimat.gui.commands.edit.KnowledgeBaseEdit;
import scimat.gui.undostack.UndoStack;
import scimat.knowledgebaseevents.KnowledgeBaseEventsReceiver;
import scimat.model.knowledgebase.dao.DocumentWordDAO;
import scimat.model.knowledgebase.dao.WordDAO;
import scimat.model.knowledgebase.entity.DocumentWord;
import scimat.model.knowledgebase.entity.Word;
import scimat.model.knowledgebase.entity.WordGroup;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;
import scimat.project.CurrentProject;

/**
 *
 * @author mjcobo
 */
public class JoinWordEdit extends KnowledgeBaseEdit {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  private ArrayList<Word> wordsToMove;
  private Word targetWord;
  
  private ArrayList<ArrayList<DocumentWord>> documentWordOfSources = new ArrayList<ArrayList<DocumentWord>>();
  private ArrayList<WordGroup> wordGroupOfSources = new ArrayList<WordGroup>();
  
  private TreeSet<DocumentWord> documentWordOfTarget = new TreeSet<DocumentWord>();

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param wordsToMove
   * @param target
   */
  public JoinWordEdit(ArrayList<Word> wordsToMove, Word targetWord) {

    this.wordsToMove = wordsToMove;
    this.targetWord = targetWord;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  /**
   *
   * @throws KnowledgeBaseException
   */
  @Override
  public boolean execute() throws KnowledgeBaseException {

    boolean successful = true;
    int i, j;
    WordDAO wordDAO;
    DocumentWordDAO documentWordDAO;
    Word word;
    DocumentWord documentWord;
    ArrayList<DocumentWord> documentWords;

    try {

      i = 0;

      wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();
      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();

      // Retrieve the realations of the target item.
      this.documentWordOfTarget = new TreeSet<DocumentWord>(wordDAO.getDocumentWords(this.targetWord.getWordID()));

      while ((i < this.wordsToMove.size()) && (successful)) {

        word = this.wordsToMove.get(i);

        // Retrieve the relations of the source items
        documentWords = wordDAO.getDocumentWords(word.getWordID());
        this.documentWordOfSources.add(documentWords);

        this.wordGroupOfSources.add(wordDAO.getWordGroup(word.getWordID()));

        // Do the join
        j = 0;

        // Delete the items and its associations
        successful = wordDAO.removeWord(word.getWordID(), true);
        
        while ((j < documentWords.size()) && (successful)) {

          documentWord = documentWords.get(j);

          // If the target element is not associated with this element we perform the association.
          if (! documentWordDAO.checkDocumentWord(documentWord.getDocument().getDocumentID(), this.targetWord.getWordID())) {

            successful = documentWordDAO.addDocumentWord(documentWord.getDocument().getDocumentID(), 
                    this.targetWord.getWordID(), 
                    documentWord.isAuthorKeyword(),
                    documentWord.isSourceKeyword(),
                    documentWord.isAddedKeyword(), true);
          }

          j ++;
        }

        i ++;
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();
        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

        UndoStack.addEdit(this);

      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();

        this.errorMessage = "An error happened";

      }


    } catch (KnowledgeBaseException e) {

      CurrentProject.getInstance().getKnowledgeBase().rollback();

      successful = false;
      this.errorMessage = "An exception happened.";

      throw e;
    }

    return successful;

  }

  /**
   *
   * @throws CannotUndoException
   */
  @Override
  public void undo() throws CannotUndoException {
    super.undo();

    int i, j;
    boolean successful = true;
    TreeSet<DocumentWord> tmpDocumentWords;
    WordDAO wordDAO;
    Word word;
    DocumentWordDAO documentWordDAO;
    DocumentWord documentWord;
    Iterator<DocumentWord> itDocumentWord;

    try {

      wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();
      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();

      // Restore the target entity
      
      tmpDocumentWords = new TreeSet<DocumentWord>(wordDAO.getDocumentWords(this.targetWord.getWordID()));
      tmpDocumentWords.removeAll(this.documentWordOfTarget);

      itDocumentWord = tmpDocumentWords.iterator();
      
      while ((itDocumentWord.hasNext()) && (successful)) {
        
        successful = documentWordDAO.removeDocumentWord(itDocumentWord.next().getDocument().getDocumentID(), this.targetWord.getWordID(), true);
      }

      i = 0;
      
      // Restore the source entities

      while ((i < this.wordsToMove.size()) && (successful)) {

        word = this.wordsToMove.get(i);

        successful = wordDAO.addWord(word, true);

        j = 0;

        while ((j < this.documentWordOfSources.get(i).size()) && (successful)) {

          documentWord = this.documentWordOfSources.get(i).get(j);

          successful = documentWordDAO.addDocumentWord(documentWord.getDocument().getDocumentID(),
                                                       word.getWordID(), 
                                                       documentWord.isAuthorKeyword(),
                                                       documentWord.isSourceKeyword(),
                                                       documentWord.isAddedKeyword(), true);

          j++;
        }

        if ((this.wordGroupOfSources.get(i) != null) && (successful)) {

          successful = wordDAO.setWordGroup(word.getWordID(), 
                  this.wordGroupOfSources.get(i).getWordGroupID(), true);
        }

        i++;
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();
        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();
      }

    } catch (KnowledgeBaseException e) {

      e.printStackTrace(System.err);

      try{

        CurrentProject.getInstance().getKnowledgeBase().rollback();

      } catch (KnowledgeBaseException e2) {

        e2.printStackTrace(System.err);

      }
    }
  }

  /**
   *
   * @throws CannotUndoException
   */
  @Override
  public void redo() throws CannotUndoException {
    super.redo();

    boolean successful = true;
    int i, j;
    WordDAO wordDAO;
    DocumentWordDAO documentWordDAO;
    Word word;
    DocumentWord documentWord;
    ArrayList<DocumentWord> documentWords;

    try {

      i = 0;

      wordDAO = CurrentProject.getInstance().getFactoryDAO().getWordDAO();
      documentWordDAO = CurrentProject.getInstance().getFactoryDAO().getDocumentWordDAO();

      while ((i < this.wordsToMove.size()) && (successful)) {

        word = this.wordsToMove.get(i);

        // Retrieve the relations of the source items
        documentWords = this.documentWordOfSources.get(i);

        // Do the join
        j = 0;

        successful = wordDAO.removeWord(word.getWordID(), true);
        
        while ((j < documentWords.size()) && (successful)) {

          documentWord = documentWords.get(j);

          // If the target element is not associated with this element we perform the association.
          if (! documentWordDAO.checkDocumentWord(documentWord.getDocument().getDocumentID(), 
                  this.targetWord.getWordID())) {

            successful = documentWordDAO.addDocumentWord(documentWord.getDocument().getDocumentID(), 
                    this.targetWord.getWordID(), 
                    documentWord.isAuthorKeyword(),
                    documentWord.isSourceKeyword(),
                    documentWord.isAddedKeyword(), true);
          }

          j ++;
        }

        i ++;
      }

      if (successful) {

        CurrentProject.getInstance().getKnowledgeBase().commit();
        KnowledgeBaseEventsReceiver.getInstance().fireEvents();

      } else {

        CurrentProject.getInstance().getKnowledgeBase().rollback();
      }

    } catch (KnowledgeBaseException e) {

      e.printStackTrace(System.err);

      try{

        CurrentProject.getInstance().getKnowledgeBase().rollback();

      } catch (KnowledgeBaseException e2) {

        e2.printStackTrace(System.err);

      }
    }
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
