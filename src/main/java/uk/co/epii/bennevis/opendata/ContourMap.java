package uk.co.epii.bennevis.opendata;

import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;

import java.util.Collection;

/**
 * User: James Robinson
 * Date: 28/03/2015
 * Time: 14:41
 */
public interface ContourMap {

  public Collection<MultiLineString> getContourSet(double height);
  public Collection<Point> getPointSet(double height);
  public Collection<Double> getKnownContours();
  public Collection<Double> getKnownHeights(double min, double max);
  public GeometryDetail getSandwichingContour(GeometryDetail contour, Point point);

}
