/*
 * StrategicDiagramSVG.java
 *
 * Created on 06-abr-2011, 14:17:00
 */
package scimat.api.visualization.graph;

import java.util.ArrayList;
import java.util.TreeMap;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import scimat.api.dataset.NetworkPair;
import scimat.api.mapping.WholeNetwork;
import scimat.api.mapping.clustering.result.Cluster;

/**
 *
 * @author mjcobo
 */
public class ClusterNetworkSVG {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private int width;
  private int maxRX;
  private int minRX;
  private int textPx;

  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/

  /**
   * 
   * @param width
   * @param maxRX
   * @param minRX
   * @param textPx
   */
  public ClusterNetworkSVG(int width, int maxRX, int minRX, int textPx) {
    this.width = width;
    this.maxRX = maxRX;
    this.minRX = minRX;
    this.textPx = textPx;
  }

  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/

  public Document buildXML(Cluster cluster, WholeNetwork wholeNetwork,
          String propertyKey, String labelPropertyKey, double maxSize) {
    
    DOMImplementation impl;
    String svgNS;
    Document doc;

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
    
    drawBox(doc, svgNS, svgRoot);
    drawNodes(doc, svgNS, svgRoot, cluster, wholeNetwork, maxSize, propertyKey, labelPropertyKey);

    return doc;
  }
  
  public Document buildEmptyXML() {
    
    DOMImplementation impl;
    String svgNS;
    Document doc;

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
    
    drawBox(doc, svgNS, svgRoot);

    return doc;
  }

  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/

  /**
   * 
   * @param wholeNetwork
   * @param nodeID
   * @param propertyKey
   * @return
   */
  private double getNodeValue(WholeNetwork wholeNetwork, Integer nodeID, String propertyKey) {

    return (Double)wholeNetwork.getNode(nodeID).getProperties().getProperty(propertyKey).getValue();
  }
  
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
   * @param cluster
   * @param wholeNetwork
   * @param maxSize
   * @param propertyKey
   */
  private void drawNodes(Document doc, String svgNS, Element element,
          Cluster cluster, WholeNetwork wholeNetwork, double maxSize, 
          String propertyKey, String labelPropertyKey) {

    int i, realWidth;
    double[] coord, coordNodeA, coordNodeB;
    double radius, angle, currentAngle, nodeSize;
    double lineWidth;
    NetworkPair pair;
    TreeMap<Integer, double[]> coordMap = new TreeMap<Integer, double[]>();
    Element subElement;

    ArrayList<Integer> nodes = cluster.getNodes();

    // Si el tamaño maximo es igual a 0, normalizaremos por el maximo de los
    // nodos. En caso contrario, normalizaremos por ese valor.
    if (maxSize == 0) {

      for (i = 0; i < nodes.size(); i++) {

        nodeSize = getNodeValue(wholeNetwork, nodes.get(i), propertyKey);

        if (maxSize < nodeSize) {

          maxSize = nodeSize;
        }
      }
    }

    realWidth = width - 2 * maxRX;
    radius = realWidth / 2;
    angle = (Math.PI * 2) / (cluster.getNodesCount() - 1);
    currentAngle = (Math.PI * 2) / 16; // Comenzamos en 22.5 grados, para evitar solapamientos.

    // Calculamos las coordenadas de los nodos.
    for (i = 0; i < nodes.size(); i++) {

      coord = new double[2];

      // Si el nodo es el principal, lo situamos en el centro.
      // En caso contrario lo situamos en un punto de la circunferencia.

      if (cluster.getMainNode().equals(nodes.get(i))) {

        coord[0] = coord[1] = realWidth / 2 + maxRX;

      } else {

        coord[0] = Math.cos(currentAngle) * radius + realWidth / 2 + maxRX;
        coord[1] = Math.sin(currentAngle) * radius + realWidth / 2 + maxRX;

        currentAngle +=  angle;
      }

      coordMap.put(nodes.get(i), coord);
    }

    // Pintamos primero las lineas entre los nodos para que queden debajo de las
    // esferas.
    ArrayList<NetworkPair> pairs = wholeNetwork.getInternalPairs(cluster.getNodes());

    for (i = 0; i < pairs.size(); i++) {

      pair = pairs.get(i);

      lineWidth = pair.getValue() * 10.0;

      coordNodeA = coordMap.get(pair.getID().getElementA());
      coordNodeB = coordMap.get(pair.getID().getElementB());

      subElement = doc.createElementNS(svgNS, "line");
      subElement.setAttributeNS(null, "x1", String.valueOf(coordNodeA[0]));
      subElement.setAttributeNS(null, "y1", String.valueOf(coordNodeA[1]));
      subElement.setAttributeNS(null, "x2", String.valueOf(coordNodeB[0]));
      subElement.setAttributeNS(null, "y2", String.valueOf(coordNodeB[1]));
      subElement.setAttributeNS(null, "stroke", "black");
      subElement.setAttributeNS(null, "stroke-width", "1");
      subElement.setAttributeNS(null, "style", "stroke-width:" + lineWidth +
        ";stroke-miterlimit:4;stroke-dasharray:none;" +
        "stroke-linecap:round;stroke-opacity:0.5");
      element.appendChild(subElement);
    }

    // Pintamos las esferas.
    for (i = 0; i < nodes.size(); i++) {

      coord = coordMap.get(nodes.get(i));

      subElement = doc.createElementNS(svgNS, "circle");
      subElement.setAttributeNS(null, "cx", String.valueOf(coord[0]));
      subElement.setAttributeNS(null, "cy", String.valueOf(coord[1]));
      subElement.setAttributeNS(null, "r", String.valueOf(((getNodeValue(wholeNetwork, nodes.get(i), propertyKey) / maxSize) * (maxRX - minRX)) + minRX));
      subElement.setAttributeNS(null, "id", "circle" + i);
      subElement.setAttributeNS(null, "style", "fill:url(#sphereTheme);stroke-width:0.50000000000000000;fill-opacity:1;stroke-opacity:1");
      element.appendChild(subElement);

      subElement = doc.createElementNS(svgNS, "text");
      subElement.setAttributeNS(null, "x", String.valueOf(coord[0]));
      subElement.setAttributeNS(null, "y", String.valueOf(coord[1]));
      subElement.setAttributeNS(null, "id", "text5" + i);
      subElement.setAttributeNS(null, "style", "font-size:" +  this.textPx + "px;text-anchor:middle;fill:#000000;font-family:Verdana");
      subElement.setTextContent((String)wholeNetwork.getNode(nodes.get(i)).getProperties().getProperty(labelPropertyKey).getValue());
      element.appendChild(subElement);
    }
  }
}
