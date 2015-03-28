package uk.co.epii.bennevis.opendata;

import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;

import java.util.Collection;

/**
 * User: James Robinson
 * Date: 28/03/2015
 * Time: 14:38
 */
public class AltitudeLocator {

  private final ContourMap contourMap;
  private Double closestContourHeight = null;
  private Double secondClosestContourHeight = null;
  private MultiLineString closestContour = null;
  private Double closestContourDistance = null;
  private Double secondClosestContourDistance = null;
  private MultiLineString secondClosestContour = null;
  private Point point;

  public AltitudeLocator(ContourMap contourMap) {
    this.contourMap = contourMap;
  }

  public double getAltitude(Point point) {
    reset(point);
    for (Double height : contourMap.getKnownContours()) {
      processContourSet(height, contourMap.getContourSet(height));
    }
    for (Double height : contourMap.getKnownHeights(closestContourHeight, secondClosestContourHeight)) {
      for (Point knownPoint : contourMap.getPointSet(height)) {
        if (point.isWithinDistance(knownPoint, closestContourDistance)) {
          secondClosestContourDistance = closestContourDistance;
          secondClosestContourHeight = closestContourHeight;
          closestContourHeight = height;
          closestContourDistance = point.distance(knownPoint);
        }
        else if (point.isWithinDistance(knownPoint, secondClosestContourDistance)) {
          secondClosestContourHeight = height;
          secondClosestContourDistance = point.distance(knownPoint);
        }
      }
    }
    double totalDistance = closestContourDistance + secondClosestContourDistance;
    double weightOfClosestDistance = secondClosestContourDistance / totalDistance;
    double weightOfSecondClosestDistance = closestContourDistance / totalDistance;
    return weightOfClosestDistance * closestContourHeight + weightOfSecondClosestDistance * secondClosestContourHeight;
  }

  private boolean processContourSet(double height, Collection<MultiLineString> contourSet) {
    boolean change = false;
    for (MultiLineString contour: contourSet) {
      change |= processContour(height, contour);
    }
    return change;
  }

  private boolean processContour(double height, MultiLineString contour) {
    if (closestContourHeight == null || contour.isWithinDistance(point, closestContourDistance)) {
      if (closestContourHeight == null || closestContourHeight != height) {
        secondClosestContourHeight = closestContourHeight;
        secondClosestContour = closestContour;
        secondClosestContourDistance = closestContourDistance;
        closestContourHeight = height;
      }
      closestContourDistance = contour.distance(point);
      closestContour = contour;
      return true;
    }
    if (secondClosestContourHeight == null || contour.isWithinDistance(point, secondClosestContourDistance)) {
      secondClosestContourHeight = height;
      secondClosestContourDistance = contour.distance(point);
      secondClosestContour = contour;
      return true;
    }
    return false;
  }

  public void reset(Point point) {
    this.point = point;
    closestContourHeight = null;
    secondClosestContourHeight = null;
    closestContourDistance = null;
    secondClosestContourDistance = null;
  }
}
