/**
 * *********************************************************************
 *
 * Copyright (C) 2014
 *
 * M.J. Cobo (manueljesus.cobo@uca.es)
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see http://www.gnu.org/licenses/
 *
 *********************************************************************
 */
package scimat.api.loader;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author mjcobo
 */
public class WosXML {

  //--------------------------------------------------------------------------
  //                        Private attributes                               
  //--------------------------------------------------------------------------
  private final DocumentBuilder docBuilder;
  private Document dom;

  //--------------------------------------------------------------------------
  //                            Constructors                                 
  //--------------------------------------------------------------------------
  public WosXML() throws ParserConfigurationException {

    this.docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
  }

  //--------------------------------------------------------------------------
  //                           Public Methods                                
  //--------------------------------------------------------------------------
  public void setXML(String recXML) throws SAXException, IOException {

    this.dom = this.docBuilder.parse(new InputSource(new StringReader(recXML)));
  }

  /**
   *
   * @return
   * @throws javax.xml.xpath.XPathExpressionException @throws Exception
   */
  public String getUID() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/UID").
            evaluate(this.dom);

  }

  /**
   *
   * @return
   * @throws javax.xml.xpath.XPathExpressionException @throws Exception
   */
  public String getSourceTitle() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/titles/title[@type='source']").
            evaluate(this.dom);

  }

  /**
   *
   * @return
   * @throws javax.xml.xpath.XPathExpressionException @throws Exception
   */
  public String getItemTitle() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/titles/title[@type='item']").
            evaluate(this.dom);

  }

  /**
   *
   * @return
   * @throws javax.xml.xpath.XPathExpressionException @throws Exception
   */
  public String getPublicationYear() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/pub_info/@pubyear").
            evaluate(this.dom);
  }
  
  public String getPublicationCoverDate() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/pub_info/@coverdate").
            evaluate(this.dom);
  }
  
  public String getPublicationMonth() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/pub_info/@pubmonth").
            evaluate(this.dom);
  }
  
  public String getPublicationSortDate() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/pub_info/@sortdate").
            evaluate(this.dom);
  }
  
  public String getIssue() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/pub_info/@issue").
            evaluate(this.dom);
  }
  
  public String getVolume() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/pub_info/@vol").
            evaluate(this.dom);
  }
  
  public String getPageBegin() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/pub_info/page/@begin").
            evaluate(this.dom);
  }
  
  public String getPageEnd() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/pub_info/page/@end").
            evaluate(this.dom);
  }
  
  public String getPageCount() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/pub_info/page/@page_count").
            evaluate(this.dom);
  }
  
  public String getPages() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/pub_info/page").
            evaluate(this.dom);
  }
  
  public int getDocTypeCount() throws XPathExpressionException {

    NodeList nodeList;
    nodeList = (NodeList) XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/doctypes/doctype").
            evaluate(this.dom, XPathConstants.NODESET);

    return nodeList.getLength();
  }
  
  public String getDocType(int index) throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/doctypes/doctype[" + index + "]").
            evaluate(this.dom);

  }
  
  public int getKeywordPlusCount() throws XPathExpressionException {

    NodeList nodeList;
    nodeList = (NodeList) XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/item/keywords_plus/keyword").
            evaluate(this.dom, XPathConstants.NODESET);

    return nodeList.getLength();
  }
  
  public String getKeywordPlus(int index) throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/item/keywords_plus/keyword[" + index + "]").
            evaluate(this.dom);

  }
  
  public int getAuhorKeywordCount() throws XPathExpressionException {

    NodeList nodeList;
    nodeList = (NodeList) XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/fullrecord_metadata/keywords/keyword").
            evaluate(this.dom, XPathConstants.NODESET);

    return nodeList.getLength();
  }
  
  public String getAuthorKeyword(int index) throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/fullrecord_metadata/keywords/keyword[" + index + "]").
            evaluate(this.dom);

  }

  public int getAuthorsCount() throws XPathExpressionException {

    NodeList nodeList;
    nodeList = (NodeList) XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/names/name").
            evaluate(this.dom, XPathConstants.NODESET);

    return nodeList.getLength();
  }
  
  public String getAbstract() throws XPathExpressionException  {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/fullrecord_metadata/abstracts/abstract/abstract_text").
            evaluate(this.dom).trim().replaceAll("\\s{2,}", "\n");
//            replaceAll("</p>", "");
  }

  /**
   * 
   * @param index
   * @return
   * @throws XPathExpressionException 
   */
  public String getAuthorDisplayName(int index) throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/names/name[" + index + "]/display_name").
            evaluate(this.dom);
  }
  
  public String getAuthorFullName(int index) throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/names/name[" + index + "]/full_name").
            evaluate(this.dom);
  }
  
  public String getAuthorWosStandard(int index) throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/static_data/summary/names/name[" + index + "]/wos_standard").
            evaluate(this.dom);
  }

  public String getDOI() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/dynamic_data/cluster_related/identifiers/identifier[@type='doi']/@value").
            evaluate(this.dom);
  }
  
  public String getXrefDOI() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/dynamic_data/cluster_related/identifiers/identifier[@type='xref_doi']/@value").
            evaluate(this.dom);
  }
  
  public String getCitationsCount() throws XPathExpressionException {

    return XPathFactory.newInstance().newXPath().
            compile("/REC/dynamic_data/citation_related/tc_list/silo_tc[@coll_id='WOS']/@local_count").
            evaluate(this.dom);
  }

  //--------------------------------------------------------------------------
  //                           Private Methods                               
  //--------------------------------------------------------------------------
}
