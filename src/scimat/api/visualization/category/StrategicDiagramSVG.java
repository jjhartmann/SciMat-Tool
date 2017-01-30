/*
 * StrategicDiagramSVG.java
 *
 * Created on 06-abr-2011, 14:17:00
 */
package scimat.api.visualization.category;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import scimat.api.analysis.category.StrategicDiagram;
import scimat.api.analysis.category.StrategicDiagramItem;

/**
 *
 * @author mjcobo
 */
public class StrategicDiagramSVG {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private int width;
  private int maxRX;
  private int minRX;
  private int textPx;
  
  private NumberFormat numberFormatter = new DecimalFormat(",##0.##");

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param width
   * @param height
   * @param maxRX
   * @param minRX
   */
  public StrategicDiagramSVG(int width, int maxRX, int minRX, int textPx) {
    
    this.width = width;
    this.maxRX = maxRX;
    this.minRX = minRX;
    this.textPx = textPx;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public Document buildXML(StrategicDiagram strategicDiagram) {

    int realWidth, realHeight;
    double maxSize;
    DOMImplementation impl;
    String svgNS;
    Document doc;
    ArrayList<StrategicDiagramItem> strategicDiagramItems;

    strategicDiagramItems = strategicDiagram.getItems();

    // Sorts the items by the volume in decresing order.
    Collections.sort(strategicDiagramItems, new Comparator<StrategicDiagramItem>() {

      @Override
      public int compare(StrategicDiagramItem o1, StrategicDiagramItem o2) {

        return Double.valueOf(o2.getVolume()).compareTo(o1.getVolume());
      }
    });

    // El maximo tamaño lo dara el primer elemento.
    if (strategicDiagramItems.size() > 0) {

      maxSize = strategicDiagramItems.get(0).getVolume();
      
    } else {

      maxSize = 0;
    }

    realWidth = width - 2 * maxRX;

    impl = SVGDOMImplementation.getDOMImplementation();
    svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
    doc = impl.createDocument(svgNS, "svg", null);

    // Get the root element (the 'svg' element).
    Element svgRoot = doc.getDocumentElement();

    // Set the width and height attributes on the root 'svg' element.
    svgRoot.setAttributeNS(null, "width", String.valueOf(width));
    svgRoot.setAttributeNS(null, "height", String.valueOf(width));
    svgRoot.setAttributeNS(null, "viewBox", "0 0 " + width + " " + width);

    addGradient(doc, svgNS, svgRoot);

    //Element g = doc.createElement("g");

    drawAxis(doc, svgNS, svgRoot, maxRX);
    drawBox(doc, svgNS, svgRoot);
    drawCluster(doc, svgNS, svgRoot, strategicDiagramItems, realWidth, realWidth, maxSize);

    //svgRoot.appendChild(g);

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
  private void addGradient(Document doc, String svgNS, Element element) {

    Element radialGradient = doc.createElementNS(svgNS, "radialGradient");
    radialGradient.setAttributeNS(null, "id", "sphereTheme");

    Element stop = doc.createElementNS(svgNS, "stop");
    stop.setAttributeNS(null, "offset", "0");
    stop.setAttributeNS(null, "style", "stop-color:#C6DD3A");
    radialGradient.appendChild(stop);
    
    stop = doc.createElementNS(svgNS, "stop");
    stop.setAttributeNS(null, "offset", "0.211");
    stop.setAttributeNS(null, "style", "stop-color:#C3DA39");
    radialGradient.appendChild(stop);
    
    stop = doc.createElementNS(svgNS, "stop");
    stop.setAttributeNS(null, "offset", "0.3881");
    stop.setAttributeNS(null, "style", "stop-color:#B8CF37");
    radialGradient.appendChild(stop);
    
    stop = doc.createElementNS(svgNS, "stop");
    stop.setAttributeNS(null, "offset", "0.5529");
    stop.setAttributeNS(null, "style", "stop-color:#A7BE34");
    radialGradient.appendChild(stop);
    
    stop = doc.createElementNS(svgNS, "stop");
    stop.setAttributeNS(null, "offset", "0.7103");
    stop.setAttributeNS(null, "style", "stop-color:#8EA630");
    radialGradient.appendChild(stop);
    
    stop = doc.createElementNS(svgNS, "stop");
    stop.setAttributeNS(null, "offset", "0.861");
    stop.setAttributeNS(null, "style", "stop-color:#6F872A");
    radialGradient.appendChild(stop);
    
    stop = doc.createElementNS(svgNS, "stop");
    stop.setAttributeNS(null, "offset", "1");
    stop.setAttributeNS(null, "style", "stop-color:#4B6323");
    radialGradient.appendChild(stop);

    element.appendChild(radialGradient);
  }

  /**
   *
   * @param doc
   * @param svgNS
   * @param element
   * @param maxRX
   */
  private void drawAxis(Document doc, String svgNS, Element element, int maxRX) {

    Element subElement;

    subElement = doc.createElementNS(svgNS, "line");
    subElement.setAttributeNS(null, "x1", String.valueOf(maxRX));
    subElement.setAttributeNS(null, "y1", String.valueOf(width / 2));
    subElement.setAttributeNS(null, "x2", String.valueOf(width - maxRX));
    subElement.setAttributeNS(null, "y2", String.valueOf(width / 2));
    subElement.setAttributeNS(null, "stroke", "black");
    subElement.setAttributeNS(null, "stroke-width", "1");
    element.appendChild(subElement);

    subElement = doc.createElementNS(svgNS, "line");
    subElement.setAttributeNS(null, "x1", String.valueOf(width / 2));
    subElement.setAttributeNS(null, "y1", String.valueOf(maxRX));
    subElement.setAttributeNS(null, "x2", String.valueOf(width / 2));
    subElement.setAttributeNS(null, "y2", String.valueOf(width - maxRX));
    subElement.setAttributeNS(null, "stroke", "black");
    subElement.setAttributeNS(null, "stroke-width", "1");
    element.appendChild(subElement);

    subElement = doc.createElementNS(svgNS, "text");
    subElement.setAttributeNS(null, "x", String.valueOf(width - maxRX - 10));
    subElement.setAttributeNS(null, "y", String.valueOf(width / 2));
    subElement.setAttributeNS(null, "id", "text23");
    subElement.setAttributeNS(null, "style", "font-size:" + this.textPx + "px;text-anchor:end;fill:#000000;font-family:Verdana");
    subElement.setTextContent("centrality");
    element.appendChild(subElement);

    subElement = doc.createElementNS(svgNS, "text");
    subElement.setAttributeNS(null, "x", String.valueOf(width / 2));
    subElement.setAttributeNS(null, "y", String.valueOf(maxRX));
    subElement.setAttributeNS(null, "id", "text24");
    subElement.setAttributeNS(null, "style", "font-size:" + this.textPx + "px;text-anchor:end;fill:#000000;font-family:Verdana");
    subElement.setTextContent("density");
    element.appendChild(subElement);
  }

  /**
   * 
   * @param doc
   * @param svgNS
   * @param element
   */
  private void drawBox(Document doc, String svgNS, Element element) {

    Element subElement;
    int maxCoord = width - 1;

    subElement = doc.createElementNS(svgNS, "line");
    subElement.setAttributeNS(null, "x1", "0");
    subElement.setAttributeNS(null, "y1", "0");
    subElement.setAttributeNS(null, "x2", String.valueOf(maxCoord));
    subElement.setAttributeNS(null, "y2", "0");
    subElement.setAttributeNS(null, "stroke", "black");
    subElement.setAttributeNS(null, "stroke-width", "1");
    element.appendChild(subElement);

    subElement = doc.createElementNS(svgNS, "line");
    subElement.setAttributeNS(null, "x1", "0");
    subElement.setAttributeNS(null, "y1", String.valueOf(maxCoord));
    subElement.setAttributeNS(null, "x2", String.valueOf(maxCoord));
    subElement.setAttributeNS(null, "y2", String.valueOf(maxCoord));
    subElement.setAttributeNS(null, "stroke", "black");
    subElement.setAttributeNS(null, "stroke-width", "1");
    element.appendChild(subElement);

    subElement = doc.createElementNS(svgNS, "line");
    subElement.setAttributeNS(null, "x1", "0");
    subElement.setAttributeNS(null, "y1", "0");
    subElement.setAttributeNS(null, "x2", "0");
    subElement.setAttributeNS(null, "y2", String.valueOf(maxCoord));
    subElement.setAttributeNS(null, "stroke", "black");
    subElement.setAttributeNS(null, "stroke-width", "1");
    element.appendChild(subElement);

    subElement = doc.createElementNS(svgNS, "line");
    subElement.setAttributeNS(null, "x1", String.valueOf(maxCoord));
    subElement.setAttributeNS(null, "y1", "0");
    subElement.setAttributeNS(null, "x2", String.valueOf(maxCoord));
    subElement.setAttributeNS(null, "y2", String.valueOf(maxCoord));
    subElement.setAttributeNS(null, "stroke", "black");
    subElement.setAttributeNS(null, "stroke-width", "1");
    element.appendChild(subElement);
  }

  /**
   * 
   * @param doc
   * @param svgNS
   * @param element
   * @param strategicDiagramItems
   */
  private void drawCluster(Document doc, String svgNS, Element element,
          ArrayList<StrategicDiagramItem> strategicDiagramItems,
          int realWidth, int realHeight, double maxSize) {

    int i;
    double x, y, size;
    StrategicDiagramItem item;
    Element subElement, tspanElement;

    for (i = 0; i < strategicDiagramItems.size(); i++) {

      item = strategicDiagramItems.get(i);

      x = (item.getValueAxisX() * realWidth) + maxRX;
      y = Math.abs((item.getValueAxisY() * realHeight) - realHeight) + maxRX;
      size = (((item.getVolume()) / maxSize) * (maxRX - minRX)) + minRX;

      if (size > 0.0) {
      
        subElement = doc.createElementNS(svgNS, "circle");
        subElement.setAttributeNS(null, "cx", String.valueOf(x));
        subElement.setAttributeNS(null, "cy", String.valueOf(y));
        subElement.setAttributeNS(null, "r", String.valueOf(size));
        subElement.setAttributeNS(null, "id", "circle15");
        subElement.setAttributeNS(null, "style", "opacity:1;fill:url(#sphereTheme);fill-opacity:1;fill-rule:nonzero;stroke:#000000;stroke-width:0.58200002;stroke-linecap:round;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-dashoffset:0;stroke-opacity:1");
        element.appendChild(subElement);
      }

      subElement = doc.createElementNS(svgNS, "text");
      subElement.setAttributeNS(null, "x", String.valueOf(x));
      subElement.setAttributeNS(null, "y", String.valueOf(y));
      subElement.setAttributeNS(null, "id", "text23");
      subElement.setAttributeNS(null, "style", "font-size:" + this.textPx + "px;text-anchor:middle;fill:#000000;font-family:Verdana");
      //subElement.setTextContent(StringEscapeUtils.escapeXml(item.getLabel()));
      subElement.setTextContent(item.getLabel());

      tspanElement = doc.createElementNS(svgNS, "tspan");
      tspanElement.setAttribute("x", String.valueOf(x));
      tspanElement.setAttribute("y", String.valueOf(y + 15));
      tspanElement.setAttributeNS(null, "id", "text25");
      tspanElement.setTextContent(this.numberFormatter.format(item.getVolume()));

      subElement.appendChild(tspanElement);
      element.appendChild(subElement);
    }

  }
}
