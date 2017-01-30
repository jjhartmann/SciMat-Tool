/*
 * FindSimilarItemsByPlurals.java
 *
 * Created on 01-jun-2011, 13:12:34
 */
package scimat.api.preprocessing.deduplicate;

/**
 *
 * @author mjcobo
 */
public class FindSimilarItemsByPlurals {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private String separator;

  
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  /**
   * 
   */
  public FindSimilarItemsByPlurals() {
    
    this.separator = null;
  }

  public FindSimilarItemsByPlurals(String separator) {
    this.separator = separator;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param s1
   * @param s2
   * @return 
   */
  public boolean execute(String s1, String s2) {
    
    int i;
    String[] split1, split2;
    boolean result, flag;
    
    if (this.separator != null) {
    
      split1 = s1.split(this.separator);
      split2 = s2.split(this.separator);
      
      if (split1.length == split2.length) {
      
        i = 0;
        result = true;
        
        // split1 and split2 have the same length
        while (i < split1.length && result) {
          
          result = checkEquality(split1[i], split2[i]) || split1[i].equals(split2[i]);
          
          i++;
        }
        
      } else {
      
        result = false;
      }
      
    } else {
    
      result = checkEquality(s1, s2);
    }
    
    return result;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
  private boolean checkEquality(String s1, String s2) {
  
    return (s1 + "S").equals(s2) ||
           (s1 + "ES").equals(s2) ||
           (s1.endsWith("Y") && (s1.substring(0, s1.length() - 1) + "IES").equals(s2)) ||
           (s1 + "s").equals(s2) ||
           (s1 + "es").equals(s2) ||
           (s1.endsWith("y") && (s1.substring(0, s1.length() - 1) + "ies").equals(s2)) ||
           (s2 + "S").equals(s1) ||
           (s2 + "ES").equals(s1) ||
           (s2.endsWith("Y") && (s2.substring(0, s2.length() - 1) + "IES").equals(s1)) ||
           (s2 + "s").equals(s1) ||
           (s2 + "es").equals(s1) ||
           (s2.endsWith("y") && (s2.substring(0, s2.length() - 1) + "ies").equals(s1));
  }
}
