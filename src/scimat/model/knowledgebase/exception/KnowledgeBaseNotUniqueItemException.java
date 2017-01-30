/*
 * KnowledgeBaseNotUniqueItemException.java
 *
 * Created on 05-nov-2010, 19:16:00
 */
package scimat.model.knowledgebase.exception;

/**
 *
 * @author mjcobo
 */
public class KnowledgeBaseNotUniqueItemException extends Exception {

  /**
   * Creates a new instance of <code>KnowledgeBaseNotUniqueItemException</code>
   * without detail message.
   */
  public KnowledgeBaseNotUniqueItemException() {

  }

  /**
   * Constructs an instance of <code>KnowledgeBaseNotUniqueItemException</code>
   * with the specified detail message.
   *
   * @param message the detail message.
   */
  public KnowledgeBaseNotUniqueItemException(String message) {
    super(message);
  }

  /**
   * Constructs an instance of <code>KnowledgeBaseNotUniqueItemException</code> 
   * with the  specified detail cause.
   *
   * @param cause the cause (which is saved for later retrieval by the
   *              <code>KnowledgeBaseNotUniqueItemException.getCause()</code> 
   *              method). (A null value is permitted, and indicates that the
   *              cause is nonexistent or unknown.)
   */
  public KnowledgeBaseNotUniqueItemException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs an instance of <code>KnowledgeBaseNotUniqueItemException</code> with the
   * specified detail cause.
   *
   * @param message the detail message.
   * @param cause the cause (which is saved for later retrieval by the
   *              <code>KnowledgeBaseNotUniqueItemException.getCause()</code> method). (A null value is
   *              permitted, and indicates that the cause is nonexistent or
   *              unknown.)
   */
  public KnowledgeBaseNotUniqueItemException(String message, Throwable cause) {
    super(message, cause);
  }
}
