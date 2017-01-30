/*
 * KnowledgeBaseInvalidValueException.java
 *
 * Created on 05-nov-2010, 19:16:00
 */
package scimat.model.knowledgebase.exception;

/**
 *
 * @author mjcobo
 */
public class KnowledgeBaseInvalidValueException extends Exception {

  /**
   * Creates a new instance of {@code KnowledgeBaseInvalidValueException}
   * without detail message.
   */
  public KnowledgeBaseInvalidValueException() {

  }

  /**
   * Constructs an instance of {@code KnowledgeBaseInvalidValueException}
   * with the specified detail message.
   *
   * @param message the detail message.
   */
  public KnowledgeBaseInvalidValueException(String message) {
    super(message);
  }

  /**
   * Constructs an instance of @code KnowledgeBaseInvalidValueException}
   * with the  specified detail cause.
   *
   * @param cause the cause (which is saved for later retrieval by the
   *              @code KnowledgeBaseInvalidValueException.getCause()}
   *              method). (A null value is permitted, and indicates that the
   *              cause is nonexistent or unknown.)
   */
  public KnowledgeBaseInvalidValueException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs an instance of @code KnowledgeBaseInvalidValueException}
   * with the specified detail cause.
   *
   * @param message the detail message.
   * @param cause the cause (which is saved for later retrieval by the
   *              @code KnowledgeBaseInvalidValueException.getCause()}
   *              method). (A null value is permitted, and indicates that the
   *              cause is nonexistent or unknown.)
   */
  public KnowledgeBaseInvalidValueException(String message, Throwable cause) {
    super(message, cause);
  }
}
