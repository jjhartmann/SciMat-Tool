/*
 * DomToString.java
 *
 * Created on 06-abr-2011, 14:30:04
 */
package scimat.api.utils.xml;

import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author mjcobo
 */
public class DomToString {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public String convert(Document doc) {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = null;

    try {
      transformer = transformerFactory.newTransformer();
    } catch (javax.xml.transform.TransformerConfigurationException error) {

      return null;
    }

    Source source = new DOMSource(doc);
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

    StringWriter writer = new StringWriter();
    Result result = new StreamResult(writer);
    try {

      transformer.transform(source, result);
    } catch (javax.xml.transform.TransformerException error) {

      return null;
    }

    String s = writer.toString();
    return s;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
}
