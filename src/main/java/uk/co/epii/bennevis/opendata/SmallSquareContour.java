package uk.co.epii.bennevis.opendata;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import uk.co.epii.bennevis.Altimeter;
import uk.co.epii.spencerperceval.tuple.Duple;
import uk.me.jstott.jcoord.OSRef;

import java.io.IOException;
import java.util.*;

/**
 * User: James Robinson
 * Date: 27/03/2015
 * Time: 22:27
 */
public class SmallSquareContour implements Altimeter, ContourMap {

  private static GeometryFactory geometryFactory = new GeometryFactory();

  private final Map<Double, List<MultiLineString>> contours;
  private final Map<Double, List<Point>> points;
  private final List<Double> knownContours;
  private final List<Double> knownPoints;

  public SmallSquareContour() {
    this.contours = new HashMap<Double, List<MultiLineString>>();
    this.points = new HashMap<Double, List<Point>>();
    this.knownContours = new ArrayList<Double>();
    this.knownPoints = new ArrayList<Double>();
  }

  public SmallSquareContour(
          Map<Double, List<MultiLineString>> contours,
          Map<Double, List<Point>> points,
          List<Double> knownContours,
          List<Double> knownPoints) {
    this.contours = contours;
    this.points = points;
    this.knownContours = knownContours;
    this.knownPoints = knownPoints;
  }

  public List<MultiLineString> getContourSet(double height) {
    List<MultiLineString> contourSet = contours.get(height);
    if (contourSet == null) {
      contourSet = new ArrayList<MultiLineString>();
      contours.put(height, contourSet);
    }
    return contourSet;
  }

  public List<Point> getPointSet(double height) {
    List<Point> pointSet = points.get(height);
    if (pointSet == null) {
      pointSet = new ArrayList<Point>();
      points.put(height, pointSet);
    }
    return pointSet;
  }

  public static SmallSquareContour fromDataSets(SimpleFeatureSource contours, SimpleFeatureSource points) {
    SmallSquareContour smallSquareContour = new SmallSquareContour();
    HashSet<Double> knownContours = new HashSet<Double>();
    HashSet<Double> knownPoints = new HashSet<Double>();
    SimpleFeatureIterator collection = null;
    try {
      collection = contours.getFeatures().features();
      while (collection.hasNext()) {
        SimpleFeature simpleFeature = collection.next();
        MultiLineString multiLineString = (MultiLineString) simpleFeature.getAttribute("the_geom");
        Double height = (Double)simpleFeature.getAttribute("PROP_VALUE");
        smallSquareContour.getContourSet(height).add(multiLineString);
        knownContours.add(height);
      }
      collection.close();
      collection = points.getFeatures().features();
      while (collection.hasNext()) {
        SimpleFeature simpleFeature = collection.next();
        Point point = (Point) simpleFeature.getAttribute("the_geom");
        Double height;
        try {
          height = (Double)simpleFeature.getAttribute("PROP_VALUE");
        }
        catch (ClassCastException cce) {
          height = (double)(int)(Integer)simpleFeature.getAttribute("PROP_VALUE");
        }
        smallSquareContour.getPointSet(height).add(point);
        knownPoints.add(height);
      }
    } catch (IOException e) {
      return null;
    }
    finally {
      if (collection != null) {
        collection.close();
      }
    }
    smallSquareContour.knownContours.addAll(knownContours);
    Collections.sort(smallSquareContour.knownContours);
    smallSquareContour.knownPoints.addAll(knownPoints);
    Collections.sort(smallSquareContour.knownPoints);
    return smallSquareContour;
  }

  @Override
  public double getAltitude(OSRef osref) {
    Point point = geometryFactory.createPoint(new Coordinate(osref.getEasting(), osref.getNorthing()));
    AltitudeLocator altitudeLocator = new AltitudeLocator(this);
    return altitudeLocator.getAltitude(point);
  }

  @Override
  public Collection<Double> getKnownContours() {
    return knownContours;
  }

  @Override
  public Collection<Double> getKnownHeights(double min, double max) {
    return getValuesBetween(knownPoints, min, max);
  }

  private Collection<Double> getValuesBetween(List<Double> values, double min, double max) {
    if (max < min) {
      return getKnownHeights(max, min);
    }
    int startIndex = Collections.binarySearch(values, min);
    if (startIndex < 0) {
      startIndex = ~startIndex;
    }
    int stopIndex = Collections.binarySearch(values, max);
    if (stopIndex < 0) {
      stopIndex = ~stopIndex;
    }
    else {
      stopIndex++;
    }
    ArrayList<Double> knownHeights = new ArrayList<Double>(stopIndex - startIndex);
    for (int i = startIndex; i < stopIndex; i++) {
      knownHeights.add(values.get(i));
    }
    return knownHeights;
  }

  @Override
  public GeometryDetail getSandwichingContour(GeometryDetail contour, Point point) {
    GeometryDetail sandwich = null;
    List<Duple<Double, Geometry>> candidates = new ArrayList<Duple<Double, Geometry>>();
    for (Double height : getValuesBetween(knownContours, contour.getHeight() - 10.5, contour.getHeight() + 10.5)) {
      for (MultiLineString candidate : getContourSet(height)) {
        candidates.add(new Duple<Double, Geometry>(height, candidate));
      }
    }
    for (Double height : getValuesBetween(knownPoints, contour.getHeight() - 10.5, contour.getHeight() + 10.5)) {
      for (Point candidate : getPointSet(height)) {
        candidates.add(new Duple<Double, Geometry>(height, candidate));
      }
    }
    for (Duple<Double, Geometry> duple : candidates) {
      double height = duple.getFirst();
      Geometry candidate = duple.getSecond();
      if (candidate == contour.getContour()) {
        continue;
      }
      if (sandwich == null || candidate.isWithinDistance(point, sandwich.getDistance())) {
        if (sandwiches(candidate, contour, point)) {
          sandwich = GeometryDetail.getContourDetail(point, candidate, height);
        }
      }
    }
    return sandwich;
  }

  private boolean sandwiches(Geometry candidate, GeometryDetail contour, Point point) {
    DistanceOp toCandidate = new DistanceOp(candidate, point);
    Coordinate[] coordinates = toCandidate.nearestPoints();
    LineSegment lineSegment = new LineSegment(coordinates[0], coordinates[1]);
    Geometry lineSegmentGeometry = lineSegment.toGeometry(geometryFactory);
    return !contour.getContour().crosses(lineSegmentGeometry);
  }
}
