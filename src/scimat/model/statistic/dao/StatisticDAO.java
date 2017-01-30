/*
 * StatisticDAO.java
 *
 * Created on 26-ene-2012, 11:53:27
 */
package scimat.model.statistic.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import scimat.model.knowledgebase.KnowledgeBaseManager;
import scimat.model.knowledgebase.exception.KnowledgeBaseException;

/**
 *
 * @author mjcobo
 */
public class StatisticDAO {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  /**
   * The knowlege base manager
   */
  private KnowledgeBaseManager kbm;
  
  private static final String __SELECT_AUTHORGROUPS_FREQ = "SELECT count(DISTINCT ag.idAuthorGroup) AS freq\n" +
                                                           "FROM Period p\n" +
                                                           "     LEFT OUTER JOIN PublishDate_Period pup ON p.idPeriod = pup.Period_idPeriod\n" +
                                                           "     LEFT OUTER JOIN PublishDate pu ON pup.PublishDate_idPublishDate = pu.idPublishDate\n" +
                                                           "     LEFT OUTER JOIN Document d ON pu.idPublishDate = d.PublishDate_idPublishDate\n" +
                                                           "     LEFT OUTER JOIN Document_Author da ON d.idDocument = da.Document_idDocument\n" +
                                                           "     LEFT OUTER JOIN Author a ON da.Author_idAuthor = a.idAuthor\n" +
                                                           "     LEFT OUTER JOIN AuthorGroup ag ON a.AuthorGroup_idAuthorGroup = ag.idAuthorGroup\n" +
                                                           "     WHERE p.idPeriod = ?\n" +
                                                           "GROUP BY p.idPeriod, d.idDocument;";
  
  private static final String __SELECT_UNIQUE_AUTHORGROUPS_COUNT = "SELECT count(DISTINCT ag.idAuthorGroup) AS count\n" +
                                                                   "FROM Period p\n" +
                                                                   "     LEFT OUTER JOIN PublishDate_Period pup ON p.idPeriod = pup.Period_idPeriod\n" +
                                                                   "     LEFT OUTER JOIN PublishDate pu ON pup.PublishDate_idPublishDate = pu.idPublishDate\n" +
                                                                   "     LEFT OUTER JOIN Document d ON pu.idPublishDate = d.PublishDate_idPublishDate\n" +
                                                                   "     LEFT OUTER JOIN Document_Author da ON d.idDocument = da.Document_idDocument\n" +
                                                                   "     LEFT OUTER JOIN Author a ON da.Author_idAuthor = a.idAuthor\n" +
                                                                   "     LEFT OUTER JOIN AuthorGroup ag ON a.AuthorGroup_idAuthorGroup = ag.idAuthorGroup\n" +
                                                                   "     WHERE p.idPeriod = ?\n;";
  
  private static final String __SELECT_REFERENCEGROUPS_FREQ = "SELECT count(DISTINCT rg.idReferenceGroup) AS freq\n" +
                                                           "FROM Period p\n" +
                                                           "     LEFT OUTER JOIN PublishDate_Period pup ON p.idPeriod = pup.Period_idPeriod\n" +
                                                           "     LEFT OUTER JOIN PublishDate pu ON pup.PublishDate_idPublishDate = pu.idPublishDate\n" +
                                                           "     LEFT OUTER JOIN Document d ON pu.idPublishDate = d.PublishDate_idPublishDate\n" +
                                                           "     LEFT OUTER JOIN Document_Reference dr ON d.idDocument = dr.Document_idDocument\n" +
                                                           "     LEFT OUTER JOIN Reference r ON dr.Reference_idReference = r.idReference\n" +
                                                           "     LEFT OUTER JOIN ReferenceGroup rg ON r.ReferenceGroup_idReferenceGroup = rg.idReferenceGroup\n" +
                                                           "     WHERE p.idPeriod = ?\n" +
                                                           "GROUP BY p.idPeriod, d.idDocument;";
  
  private static final String __SELECT_UNIQUE_REFERENCEGROUPS_COUNT = "SELECT count(DISTINCT rg.idReferenceGroup) AS freq\n" +
                                                                      "FROM Period p\n" +
                                                                      "     LEFT OUTER JOIN PublishDate_Period pup ON p.idPeriod = pup.Period_idPeriod\n" +
                                                                      "     LEFT OUTER JOIN PublishDate pu ON pup.PublishDate_idPublishDate = pu.idPublishDate\n" +
                                                                      "     LEFT OUTER JOIN Document d ON pu.idPublishDate = d.PublishDate_idPublishDate\n" +
                                                                      "     LEFT OUTER JOIN Document_Reference dr ON d.idDocument = dr.Document_idDocument\n" +
                                                                      "     LEFT OUTER JOIN Reference r ON dr.Reference_idReference = r.idReference\n" +
                                                                      "     LEFT OUTER JOIN ReferenceGroup rg ON r.ReferenceGroup_idReferenceGroup = rg.idReferenceGroup\n" +
                                                                      "     WHERE p.idPeriod = ?;";
  
  private static final String __SELECT_WORDGROUPS_FREQ = "SELECT count(DISTINCT wg.idWordGroup) AS freq\n" +
                                                         "FROM Period p\n" +
                                                         "     LEFT OUTER JOIN PublishDate_Period pup ON p.idPeriod = pup.Period_idPeriod\n" +
                                                         "     LEFT OUTER JOIN PublishDate pu ON pup.PublishDate_idPublishDate = pu.idPublishDate\n" +
                                                         "     LEFT OUTER JOIN Document d ON pu.idPublishDate = d.PublishDate_idPublishDate\n" +
                                                         "     LEFT OUTER JOIN Document_Word dw ON d.idDocument = dw.Document_idDocument\n" +
                                                         "     LEFT OUTER JOIN Word w ON dw.Word_idWord = w.idWord\n" +
                                                         "     LEFT OUTER JOIN WordGroup wg ON w.WordGroup_idWordGroup = wg.idWordGroup\n" +
                                                         "     WHERE p.idPeriod = ?\n" +
                                                         "GROUP BY p.idPeriod, d.idDocument;";
  
  private static final String __SELECT_UNIQUE_WORDGROUPS_COUNT = "SELECT count(DISTINCT wg.idWordGroup) AS count\n" +
                                                                 "FROM Period p\n" +
                                                                 "     LEFT OUTER JOIN PublishDate_Period pup ON p.idPeriod = pup.Period_idPeriod\n" +
                                                                 "     LEFT OUTER JOIN PublishDate pu ON pup.PublishDate_idPublishDate = pu.idPublishDate\n" +
                                                                 "     LEFT OUTER JOIN Document d ON pu.idPublishDate = d.PublishDate_idPublishDate\n" +
                                                                 "     LEFT OUTER JOIN Document_Word dw ON d.idDocument = dw.Document_idDocument\n" +
                                                                 "     LEFT OUTER JOIN Word w ON dw.Word_idWord = w.idWord\n" +
                                                                 "     LEFT OUTER JOIN WordGroup wg ON w.WordGroup_idWordGroup = wg.idWordGroup\n" +
                                                                 "     WHERE p.idPeriod = ?\n;";
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   * @param kbm 
   */
  public StatisticDAO(KnowledgeBaseManager kbm) {
    this.kbm = kbm;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param periodID
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Integer> getAuthorGroupsCountPerDocument(Integer periodID) throws KnowledgeBaseException {
  
    ResultSet rs;
    ArrayList<Integer> count;
    
    count = new ArrayList<Integer>();
    
    try {

      PreparedStatement stat = this.kbm.getConnection().prepareStatement(__SELECT_AUTHORGROUPS_FREQ);

      stat.setInt(1, periodID);

      rs = stat.executeQuery();

      while (rs.next()) {

        count.add(rs.getInt("freq"));
      }

      rs.close();
      stat.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    return count;
  }
  
  /**
   * 
   * @param periodID
   * @return
   * @throws KnowledgeBaseException 
   */
  public int getUniqueAuthorGroupsCount(Integer periodID) throws KnowledgeBaseException {
  
    ResultSet rs;
    int count = 0;
    
    try {

      PreparedStatement stat = this.kbm.getConnection().prepareStatement(__SELECT_UNIQUE_AUTHORGROUPS_COUNT);

      stat.setInt(1, periodID);

      rs = stat.executeQuery();

      if (rs.next()) {
      
        count = rs.getInt("count");
      }

      rs.close();
      stat.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    return count;
  }
  
  /**
   * 
   * @param periodID
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Integer> getReferenceGroupsCountPerDocument(Integer periodID) throws KnowledgeBaseException {
  
    ResultSet rs;
    ArrayList<Integer> count;
    
    count = new ArrayList<Integer>();
    
    try {

      PreparedStatement stat = this.kbm.getConnection().prepareStatement(__SELECT_REFERENCEGROUPS_FREQ);

      stat.setInt(1, periodID);

      rs = stat.executeQuery();

      while (rs.next()) {

        count.add(rs.getInt("freq"));
      }

      rs.close();
      stat.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    return count;
  }
  
  /**
   * 
   * @param periodID
   * @return
   * @throws KnowledgeBaseException 
   */
  public int getUniqueReferenceGroupsCount(Integer periodID) throws KnowledgeBaseException {
  
    ResultSet rs;
    int count = 0;
    
    try {

      PreparedStatement stat = this.kbm.getConnection().prepareStatement(__SELECT_UNIQUE_REFERENCEGROUPS_COUNT);

      stat.setInt(1, periodID);

      rs = stat.executeQuery();

      if (rs.next()) {
      
        count = rs.getInt("count");
      }

      rs.close();
      stat.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    return count;
  }
  
  /**
   * 
   * @param periodID
   * @return
   * @throws KnowledgeBaseException 
   */
  public ArrayList<Integer> getWordGroupsCountPerDocument(Integer periodID) throws KnowledgeBaseException {
  
    ResultSet rs;
    ArrayList<Integer> count;
    
    count = new ArrayList<Integer>();
    
    try {

      PreparedStatement stat = this.kbm.getConnection().prepareStatement(__SELECT_WORDGROUPS_FREQ);

      stat.setInt(1, periodID);

      rs = stat.executeQuery();

      while (rs.next()) {

        count.add(rs.getInt("freq"));
      }

      rs.close();
      stat.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    return count;
  }
  
  /**
   * 
   * @param periodID
   * @return
   * @throws KnowledgeBaseException 
   */
  public int getUniqueWordGroupsCount(Integer periodID) throws KnowledgeBaseException {
  
    ResultSet rs;
    int count = 0;
    
    try {

      PreparedStatement stat = this.kbm.getConnection().prepareStatement(__SELECT_UNIQUE_WORDGROUPS_COUNT);

      stat.setInt(1, periodID);

      rs = stat.executeQuery();

      if (rs.next()) {
      
        count = rs.getInt("count");
      }

      rs.close();
      stat.close();

    } catch (SQLException e) {

      throw new KnowledgeBaseException(e.getMessage(), e.getCause());
    }
    
    return count;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
