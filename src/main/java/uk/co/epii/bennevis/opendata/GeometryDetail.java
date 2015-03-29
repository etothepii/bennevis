package uk.co.epii.bennevis.opendata;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.distance.DistanceOp;

/**
 * User: James Robinson
 * Date: 29/03/2015
 * Time: 16:06
 */
public class GeometryDetail {

  private final Geometry contour;
  private final Coordinate closestPoint;
  private final double height;
  private final double distance;

  public GeometryDetail(Geometry contour, Coordinate closestPoint, double height, double distance) {
    this.contour = contour;
    this.closestPoint = closestPoint;
    this.height = height;
    this.distance = distance;
  }

  public Geometry getContour() {
    return contour;
  }

  public Coordinate getClosestPoint() {
    return closestPoint;
  }

  public double getHeight() {
    return height;
  }

  public double getDistance() {
    return distance;
  }

  public static GeometryDetail getContourDetail(Point point, Geometry contour, double height) {
    DistanceOp distanceOp = new DistanceOp(contour, point);
    return new GeometryDetail(contour,
            distanceOp.nearestPoints()[0], height, distanceOp.distance());
  }
}
