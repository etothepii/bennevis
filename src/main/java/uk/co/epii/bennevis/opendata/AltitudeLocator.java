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
  private GeometryDetail closest = null;
  private GeometryDetail sandwich = null;
  private Point point;

  public AltitudeLocator(ContourMap contourMap) {
    this.contourMap = contourMap;
  }

  public double getAltitude(Point point) {
    reset(point);
    for (Double height : contourMap.getKnownContours()) {
      processContourSet(height, contourMap.getContourSet(height));
    }
    for (Double height : contourMap.getKnownHeights(closest.getHeight() - 10d, closest.getHeight() + 10)) {
      processPointSet(height, contourMap.getPointSet(height));
    }
    sandwich = contourMap.getSandwichingContour(closest, point);
    if (sandwich == null) {
      return closest.getHeight();
    }
    double totalDistance = closest.getDistance() + sandwich.getDistance();
    double closestWeight = sandwich.getDistance() / totalDistance;
    double sandwichWeight = closest.getDistance() / totalDistance;
    return closest.getHeight() * closestWeight + sandwich.getHeight() * sandwichWeight;
  }

  private void processPointSet(double height, Collection<Point> pointSet) {
    for (Point point : pointSet) {
      if (point.isWithinDistance(this.point, closest.getDistance())) {
        closest = GeometryDetail.getContourDetail(this.point, point, height);
      }
    }
  }

  public GeometryDetail getClosest() {
    return closest;
  }

  public GeometryDetail getSandwich() {
    return sandwich;
  }

  private boolean processContourSet(double height, Collection<MultiLineString> contourSet) {
    boolean change = false;
    for (MultiLineString contour: contourSet) {
      change |= processContour(height, contour);
    }
    return change;
  }

  private boolean processContour(double height, MultiLineString contour) {
    if (closest == null || contour.isWithinDistance(point, closest.getDistance())) {
      closest = GeometryDetail.getContourDetail(point, contour, height);
      return true;
    }
    return false;
  }

  public void reset(Point point) {
    this.point = point;
    closest = null;
  }

  public Point getPoint() {
    return point;
  }
}
