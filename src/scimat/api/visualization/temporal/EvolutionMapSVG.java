/*
 * EvolutionMapSVG.java
 *
 * Created on 10-may-2011, 11:39:22
 */
package scimat.api.visualization.temporal;

import java.util.ArrayList;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import scimat.api.analysis.temporal.EvolutionMap;
import scimat.api.analysis.temporal.EvolutionMapNexus;
import scimat.api.mapping.clustering.result.ClusterSet;

/**
 *
 * @author mjcobo
 */
public class EvolutionMapSVG {

  /***************************************************************************/
  /*                        Private attributes                               */
  /***************************************************************************/
  
  private int minRX;
  private int maxRX;
  private int horizontalGapBetweenSpheres;
  private int verticalGapBetweenSpheres;
  private int textPx;
  
  /***************************************************************************/
  /*                            Constructors                                 */
  /***************************************************************************/
  
  public EvolutionMapSVG(int minRX, int maxRX, 
          int horizontalGapBetweenSpheres, int verticalGapBetweenSpheres, 
          int textPx) {
    
    this.minRX = minRX;
    this.maxRX = maxRX;
    this.horizontalGapBetweenSpheres = horizontalGapBetweenSpheres;
    this.verticalGapBetweenSpheres = verticalGapBetweenSpheres;
    this.textPx = textPx;
  }
  
  /***************************************************************************/
  /*                           Public Methods                                */
  /***************************************************************************/
  
  /**
   * 
   * @param evolutionMap
   * @return 
   */
  public Document buildXML(ArrayList<ClusterSet> clusterSets, EvolutionMap evolutionMap, 
          String volumePropertyName, String labelPropertyName) {
    
    int i, j, width, height, maxClusterCount;
    double maxClusterSize, tmpValue;
    double[][] lastCoords, currentCoords;
    ClusterSet clusterSet;
    DOMImplementation impl;
    String svgNS;
    Document doc;
    
    // - Calculate the max number of cluster per period
    // - Calculate the max size of the clusters
    
    maxClusterCount = 0;
    maxClusterSize = 0.0;
    
            
    for (i = 0; i < clusterSets.size(); i++) {
    
      clusterSet = clusterSets.get(i);
      
      if (clusterSet.getClustersCount() > maxClusterCount) {
      
        maxClusterCount = clusterSet.getClustersCount();
      }
      
      if (volumePropertyName != null) {

        for (j = 0; j < clusterSet.getClustersCount(); j++) {

          tmpValue = (Double) clusterSet.getCluster(j).getProperties().getProperty(volumePropertyName).getValue();

          if (tmpValue > maxClusterSize) {

            maxClusterSize = tmpValue;
          }
        }
      }
    }
    
    impl = SVGDOMImplementation.getDOMImplementation();
    svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
    doc = impl.createDocument(svgNS, "svg", null);
    
    //width = this.maxRX * 2 * clusterSets.size() + this.horizontalGapBetweenSpheres * (clusterSets.size() - 1);
    width = this.maxRX * 2 * clusterSets.size() + this.horizontalGapBetweenSpheres * clusterSets.size();
    //height = this.maxRX * 2 * maxClusterCount + this.verticalGapBetweenSpheres * (maxClusterCount - 1);
    height = this.maxRX + this.maxRX * 2 * maxClusterCount + this.verticalGapBetweenSpheres * maxClusterCount - 1;
    
    // Get the root element (the 'svg' element).
    Element svgRoot = doc.getDocumentElement();
    
    // Set the width and height attributes on the root 'svg' element.
    svgRoot.setAttributeNS(null, "width", String.valueOf(width));
    svgRoot.setAttributeNS(null, "height", String.valueOf(height));
    //svgRoot.setAttributeNS(null, "viewBox", "0 0 " + width + " " + height);

    addGradient(doc, svgNS, svgRoot);
    
    if (clusterSets.size() > 0) {
      
      lastCoords = drawCluster(clusterSets.get(0), 0, maxClusterSize, volumePropertyName, labelPropertyName, height, doc, svgNS, svgRoot);
      
      for (i = 1; i < clusterSets.size(); i++) {
        
        currentCoords = drawCluster(clusterSets.get(i), i, maxClusterSize, volumePropertyName, labelPropertyName, height, doc, svgNS, svgRoot);
        
        drawNexus(lastCoords, currentCoords, i - 1, i, clusterSets, evolutionMap, doc, svgNS, svgRoot);
        
        lastCoords = currentCoords;
      }
    }
    
    return doc;
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
  
  /***************************************************************************/
  /*                           Private Methods                               */
  /***************************************************************************/
  
  private double[][] drawCluster(ClusterSet clusterSet, int period, 
          double maxClusterSize, String volumePropertyName, String labelPropertyName, 
          int height, Document doc, String svgNS, Element element) {
  
    int i, x, y, extraVerticalGap;
    double[][] coords;
    double volume;
    Element subElement;
    
    coords = new double[clusterSet.getClustersCount()][3];
    
    if (clusterSet.getClustersCount() != 0) {
    
      extraVerticalGap = (height - this.maxRX * 2 * clusterSet.getClustersCount() + this.verticalGapBetweenSpheres * clusterSet.getClustersCount()) / clusterSet.getClustersCount();
    
    } else {
    
      extraVerticalGap = 0;
    }
    
    for (i = 0; i < clusterSet.getClustersCount(); i++) {
      
      x = this.maxRX + this.maxRX * 2 * period + this.horizontalGapBetweenSpheres * period;
      y = this.maxRX + this.maxRX * 2 * i + this.verticalGapBetweenSpheres + extraVerticalGap * i;
      
      if ((volumePropertyName == null) || (maxClusterSize == 0.0)) {

        volume = this.maxRX;

      } else {

        volume = (Double) clusterSet.getCluster(i).getProperties().getProperty(volumePropertyName).getValue();
        volume = ((volume / maxClusterSize) * (maxRX - minRX)) + minRX;
      }
      
      coords[i][0] = x;
      coords[i][1] = y;
      coords[i][2] = volume;

      subElement = doc.createElementNS(svgNS, "circle");
      subElement.setAttributeNS(null, "cx", String.valueOf(x));
      subElement.setAttributeNS(null, "cy", String.valueOf(y));
      subElement.setAttributeNS(null, "r", String.valueOf(volume));
      subElement.setAttributeNS(null, "fill", "url(#sphereTheme)");
      //subElement.setAttributeNS(null, "stroke", "#00FF00");
      element.appendChild(subElement);
      
      
      subElement = doc.createElementNS(svgNS, "text");
      subElement.setAttributeNS(null, "x", String.valueOf(x + volume));
      subElement.setAttributeNS(null, "y", String.valueOf(y));
      subElement.setAttributeNS(null, "id", "text23");
      subElement.setAttributeNS(null, "style", "font-size:" + this.textPx + "px;text-anchor:start;fill:#000000;font-family:Verdana");
      subElement.setTextContent((String) clusterSet.getCluster(i).getProperties().getProperty(labelPropertyName).getValue());
      element.appendChild(subElement);
    }
    
    return coords;
  }
  
  /**
   * 
   * @param coordsPeriodI
   * @param coordsPeriodJ
   * @param clusterSetI
   * @param clusterSetJ
   * @param clusterSets
   * @param evolutionMap
   * @param doc
   * @param svgNS
   * @param element 
   */
  private void drawNexus(double[][] coordsPeriodI, double[][] coordsPeriodJ, 
          int clusterSetI, int clusterSetJ, ArrayList<ClusterSet> clusterSets, 
          EvolutionMap evolutionMap, Document doc, String svgNS, Element element) {
  
    int i, j, clusterCountI, clusterCountJ;
    double x1, y1;
    EvolutionMapNexus nexus;
    
    
    clusterCountI = clusterSets.get(clusterSetI).getClustersCount();
    clusterCountJ = clusterSets.get(clusterSetJ).getClustersCount();
    
    for (i = 0; i < clusterCountI; i++) {
    
      x1 = coordsPeriodI[i][0] + coordsPeriodI[i][2];
      y1 = coordsPeriodI[i][1];
      
      for (j = 0; j < clusterCountJ; j++) {
    
        nexus = evolutionMap.getEvolutionNexus(clusterSetI, i, j);
        
        if (nexus != null) {
        
          drawArrow(x1, 
                    y1, 
                    coordsPeriodJ[j][0] - coordsPeriodJ[j][2], 
                    coordsPeriodJ[j][1], 
                    nexus.getWeight(),
                    nexus.isShareMainNode(), 
                    doc, svgNS, element);
        }
      }
    }
  }
  
  /**
   * 
   * @param x1
   * @param x2
   * @param y1
   * @param y2
   * @param solidLine
   * @param doc
   * @param svgNS
   * @param element 
   */
  private void drawArrow(double x1, double y1, double x2, double y2, double nexusWeight, boolean solidLine,
          Document doc, String svgNS, Element element) {
        
    Element subElement;
    
    subElement = doc.createElementNS(svgNS, "line");
    subElement.setAttributeNS(null, "x1", String.valueOf(x1));
    subElement.setAttributeNS(null, "y1", String.valueOf(y1));
    subElement.setAttributeNS(null, "x2", String.valueOf(x2));
    subElement.setAttributeNS(null, "y2", String.valueOf(y2));
    subElement.setAttributeNS(null, "stroke", "black");
    subElement.setAttributeNS(null, "stroke-width", String.valueOf(nexusWeight * 5));
    
    if (!solidLine) {
    
      subElement.setAttributeNS(null, "style", "stroke-dasharray: 9, 5");
    }
    
    element.appendChild(subElement);
  }
}
