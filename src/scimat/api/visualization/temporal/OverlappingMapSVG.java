/*
 * OverlappingMapSVG.java
 *
 * Created on 10-may-2011, 11:39:33
 */
package scimat.api.visualization.temporal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import scimat.api.analysis.temporal.OverlappingMap;

/**
 *
 * @author mjcobo
 */
public class OverlappingMapSVG {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private int sphereRadius;
  private int gapBetweenSpheres;
  private int arrowLength;
  private int textPx;
  
  private NumberFormat numberFormatter = new DecimalFormat(",##0.##");
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public OverlappingMapSVG(int sphereRadius, int gapBetweenSpheres, int arrowLength, int textPx) {
    this.sphereRadius = sphereRadius;
    this.gapBetweenSpheres = gapBetweenSpheres;
    this.arrowLength = arrowLength;
    this.textPx = textPx;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param overlappingMap
   * @return 
   */
  public Document buildXML(OverlappingMap overlappingMap) {
    
    int i, width, height, sphereX, sphereLastX, sphereY;
    DOMImplementation impl;
    String svgNS;
    Document doc;
    
    impl = SVGDOMImplementation.getDOMImplementation();
    svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
    doc = impl.createDocument(svgNS, "svg", null);
    
    width = this.sphereRadius * 2 * overlappingMap.getPeriodCount() + this.gapBetweenSpheres * (overlappingMap.getPeriodCount() - 1);
    height = this.sphereRadius * 2 + this.arrowLength;
    
    // Get the root element (the 'svg' element).
    Element svgRoot = doc.getDocumentElement();
    
    // Set the width and height attributes on the root 'svg' element.
    svgRoot.setAttributeNS(null, "width", String.valueOf(width));
    svgRoot.setAttributeNS(null, "height", String.valueOf(height));
    svgRoot.setAttributeNS(null, "viewBox", "0 0 " + width + " " + height);
    
    addDefs(doc, svgNS, svgRoot);
    
    sphereY = this.sphereRadius + this.arrowLength;
    
    if (overlappingMap.getPeriodCount() > 0) {
    
      sphereLastX = calculateSphereX(0);
      drawSpheres(sphereLastX, sphereY, doc, svgNS, svgRoot);
      drawSphereText(sphereLastX, sphereY, overlappingMap.getItemsCountInPeriod(0), doc, svgNS, svgRoot);

      for (i = 1; i < overlappingMap.getPeriodCount(); i++) {

        drawUpArrow(sphereLastX, doc, svgNS, svgRoot);
        drawUpArrowText(sphereLastX, overlappingMap.getDissapearedItemsCountInPeriod(i - 1), doc, svgNS, svgRoot);

        sphereX = calculateSphereX(i);
        drawSpheres(sphereX, sphereY, doc, svgNS, svgRoot);
        drawSphereText(sphereX, sphereY, overlappingMap.getItemsCountInPeriod(i), doc, svgNS, svgRoot);

        drawDownArrow(sphereX, doc, svgNS, svgRoot);
        drawDownArrowText(sphereX, overlappingMap.getNewItemsCountInPeriod(i), doc, svgNS, svgRoot);
        drawHorizontalArrow(sphereLastX, sphereX, sphereY, doc, svgNS, svgRoot);
        drawHorizontalArrowText(sphereLastX, sphereY, 
                overlappingMap.getOverlappedItemsCountInPeriod(i - 1), 
                overlappingMap.getOverlappingWeight(i - 1), doc, svgNS, svgRoot);

        sphereLastX = sphereX;
      }
    }
    
    return doc;
  }
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
  /**
   * 
   * @param doc
   * @param element
   */
  private void addDefs(Document doc, String svgNS, Element element) {

    Element defs = doc.createElementNS(svgNS, "defs");
    defs.setAttribute("id", "defs4");

    Element market = doc.createElementNS(svgNS, "marker");
    market.setAttributeNS(null, "refX", "0");
    market.setAttributeNS(null, "refY", "0");
    market.setAttributeNS(null, "orient", "auto");
    market.setAttributeNS(null, "id", "TriangleOutL");
    market.setAttributeNS(null, "style", "overflow:visible");

    Element path = doc.createElementNS(svgNS, "path");
    path.setAttributeNS(null, "d", "M 5.77,0 L -2.88,5 L -2.88,-5 L 5.77,0 z");
    path.setAttributeNS(null, "transform", "scale(0.8,0.8)");
    path.setAttributeNS(null, "style", "fill-rule:evenodd;stroke:#000000;stroke-width:1pt;marker-start:none");

    market.appendChild(path);

    market.appendChild(path);

    defs.appendChild(market);

    element.appendChild(defs);
  }
  
  /**
   * 
   * @param x
   * @param y
   * @param doc
   * @param svgNS
   * @param element 
   */
  private void drawSpheres(int x, int y, Document doc, String svgNS, Element element) {
    
    Element subElement;
    
    subElement = doc.createElementNS(svgNS, "circle");
    subElement.setAttributeNS(null, "cx", String.valueOf(x));
    subElement.setAttributeNS(null, "cy", String.valueOf(y));
    subElement.setAttributeNS(null, "r", String.valueOf(this.sphereRadius));
    subElement.setAttributeNS(null, "fill", "#FFFFFF");
    subElement.setAttributeNS(null, "stroke", "#000000");
    element.appendChild(subElement);
  }
  
  /**
   * 
   * @param x
   * @param doc
   * @param svgNS
   * @param element 
   */
  private void drawUpArrow(int x, Document doc, String svgNS, Element element) {
  
    int x2;
    Element subElement;
    
    x2 = x + this.arrowLength;
    
    subElement = doc.createElementNS(svgNS, "line");
    subElement.setAttributeNS(null, "x1", String.valueOf(x));
    subElement.setAttributeNS(null, "y1", String.valueOf(this.sphereRadius));
    subElement.setAttributeNS(null, "x2", String.valueOf(x2));
    subElement.setAttributeNS(null, "y2", "0");
    subElement.setAttributeNS(null, "stroke", "black");
    subElement.setAttributeNS(null, "stroke-width", "1");
    subElement.setAttributeNS(null, "style", "marker-end:url(#TriangleOutL)");
    element.appendChild(subElement);
    
  }
  
  /**
   * 
   * @param x
   * @param doc
   * @param svgNS
   * @param element 
   */
  private void drawDownArrow(int x, Document doc, String svgNS, Element element) {
  
    int x1;
    Element subElement;
    
    x1 = x - this.arrowLength;
    
    subElement = doc.createElementNS(svgNS, "line");
    subElement.setAttributeNS(null, "x1", String.valueOf(x1));
    subElement.setAttributeNS(null, "y1", "0");
    subElement.setAttributeNS(null, "x2", String.valueOf(x));
    subElement.setAttributeNS(null, "y2", String.valueOf(this.sphereRadius));
    subElement.setAttributeNS(null, "stroke", "black");
    subElement.setAttributeNS(null, "stroke-width", "1");
    subElement.setAttributeNS(null, "style", "marker-end:url(#TriangleOutL)");
    element.appendChild(subElement);
    
  }
  
  /**
   * 
   * @param x1
   * @param x2
   * @param y
   * @param doc
   * @param svgNS
   * @param element 
   */
  private void drawHorizontalArrow(int x1, int x2, int y, Document doc, String svgNS, Element element) {
        
    Element subElement;
    
    subElement = doc.createElementNS(svgNS, "line");
    subElement.setAttributeNS(null, "x1", String.valueOf(x1 + this.sphereRadius));
    subElement.setAttributeNS(null, "y1", String.valueOf(y));
    subElement.setAttributeNS(null, "x2", String.valueOf(x2 - this.sphereRadius));
    subElement.setAttributeNS(null, "y2", String.valueOf(y));
    subElement.setAttributeNS(null, "stroke", "black");
    subElement.setAttributeNS(null, "stroke-width", "1");
    subElement.setAttributeNS(null, "style", "marker-end:url(#TriangleOutL)");
    element.appendChild(subElement);
  }
  
  private void drawUpArrowText(int sphereLastX, int dissapearingItems, Document doc, String svgNS, Element element) {
  
    int x, y;
    Element subElement;
    
    x = sphereLastX + this.arrowLength / 2;
    y = this.arrowLength / 2;
    
    subElement = doc.createElementNS(svgNS, "text");
    subElement.setAttributeNS(null, "x", String.valueOf(x));
    subElement.setAttributeNS(null, "y", String.valueOf(y - 5));
    subElement.setAttributeNS(null, "text-anchor", "middle");
    subElement.setAttributeNS(null, "transform", "rotate(-45 " + x + " " + y +")");
    subElement.setAttributeNS(null, "style", "font-size:" +  this.textPx + "px;fill:#000000;font-family:Verdana");
    subElement.setTextContent(String.valueOf(dissapearingItems));
    element.appendChild(subElement);
  }
  
  private void drawDownArrowText(int sphereX, int newItems, Document doc, String svgNS, Element element) {
  
    int x, y;
    Element subElement;
   
    x = sphereX - this.arrowLength / 2;
    y = this.arrowLength / 2;
    
    subElement = doc.createElementNS(svgNS, "text");
    subElement.setAttributeNS(null, "x", String.valueOf(x));
    subElement.setAttributeNS(null, "y", String.valueOf(y - 5));
    subElement.setAttributeNS(null, "text-anchor", "middle");
    subElement.setAttributeNS(null, "transform", "rotate(45 " + x + " " + y +")");
    subElement.setAttributeNS(null, "style", "font-size:" +  this.textPx + "px;fill:#000000;font-family:Verdana");
    subElement.setTextContent(String.valueOf(newItems));
    element.appendChild(subElement);
  }
  
  private void drawHorizontalArrowText(int sphereLastX, int y, int overlappedItems, double weight, Document doc, String svgNS, Element element) {
  
    Element subElement;
    
    subElement = doc.createElementNS(svgNS, "text");
    subElement.setAttributeNS(null, "x", String.valueOf(sphereLastX + this.sphereRadius + this.gapBetweenSpheres / 2));
    subElement.setAttributeNS(null, "y", String.valueOf(y - 5));
    subElement.setAttributeNS(null, "text-anchor", "middle");
    subElement.setAttributeNS(null, "style", "font-size:" +  this.textPx + "px;fill:#000000;font-family:Verdana");
    subElement.setTextContent(overlappedItems + " (" + this.numberFormatter.format(weight) + ")");
    element.appendChild(subElement);
  }
  
  private void drawSphereText(int sphereX, int y, int items, Document doc, String svgNS, Element element) {
  
    Element subElement;
    
    subElement = doc.createElementNS(svgNS, "text");
    subElement.setAttributeNS(null, "x", String.valueOf(sphereX));
    subElement.setAttributeNS(null, "y", String.valueOf(y));
    subElement.setAttributeNS(null, "text-anchor", "middle");
    subElement.setAttributeNS(null, "style", "font-size:" +  this.textPx + "px;fill:#000000;font-family:Verdana");
    subElement.setTextContent(String.valueOf(items));
    element.appendChild(subElement);
  }
  
  /**
   * 
   * @param period
   * @return 
   */
  private int calculateSphereX(int period) {
  
    return this.sphereRadius + this.sphereRadius * 2 * period + this.gapBetweenSpheres * period;
  }
}
