package uk.co.epii.bennevis.opendata;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.junit.Test;
import uk.co.epii.conservatives.robertwalpole.DataSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * User: James Robinson
 * Date: 28/03/2015
 * Time: 23:16
 */
public class AltitudeLocatorTest {

  @Test
  public void canFindAltitude1() {
    DataSet contourDataSet = DataSet.createFromFile(new File("/tmp/Terrain50/SS74_LINE.SHP"));
    DataSet pointDataSet = DataSet.createFromFile(new File("/tmp/Terrain50/SS74_POINT.SHP"));
    ContourMap contourMap = SmallSquareContour.fromDataSets(contourDataSet.getFeatureSource(), pointDataSet.getFeatureSource());
    GeometryFactory geometryFactory = new GeometryFactory();
    AltitudeLocator altitudeLocator = new AltitudeLocator(contourMap);
    double result = altitudeLocator.getAltitude(geometryFactory.createPoint(new Coordinate(271620d, 149691d)));
    double expected = 220d;
    assertTrue(result + " should be above the " + expected + " contour.", result > expected);
  }

  @Test
  public void canFindAltitude2() {
    DataSet contourDataSet = DataSet.createFromFile(new File("/tmp/Terrain50/SS74_LINE.SHP"));
    DataSet pointDataSet = DataSet.createFromFile(new File("/tmp/Terrain50/SS74_POINT.SHP"));
    ContourMap contourMap = SmallSquareContour.fromDataSets(contourDataSet.getFeatureSource(), pointDataSet.getFeatureSource());
    GeometryFactory geometryFactory = new GeometryFactory();
    AltitudeLocator altitudeLocator = new AltitudeLocator(contourMap);
    double result = altitudeLocator.getAltitude(geometryFactory.createPoint(new Coordinate(271439d, 149768d)));
    double expected = 240d;
    assertTrue(result + " should be above the " + expected + " contour.", result > expected);
  }

  private void draw(AltitudeLocator altitudeLocator) {
    draw(altitudeLocator.getClosest().getContour().getCoordinates(),
            altitudeLocator.getSandwich().getContour().getCoordinates(),
            altitudeLocator.getPoint(),
            altitudeLocator.getClosest().getHeight(),
            altitudeLocator.getSandwich().getHeight(),
            altitudeLocator.getClosest().getClosestPoint(),
            altitudeLocator.getSandwich().getClosestPoint());
  }

  private void draw(final Coordinate[] closestContour,
                    final Coordinate[] secondClosestContour,
                    final Point point,
                    double closestHeight,
                    double secondClosestHeight,
                    final Coordinate closestContourPoint,
                    final Coordinate secondClosestContourPoint) {
    double _maxX = point.getX();
    double _maxY = point.getY();
    double _minX = point.getX();
    double _minY = point.getY();
    for (Coordinate coordinate : closestContour) {
      if (coordinate.x < _minX) {
        _minX = coordinate.x;
      }
      if (coordinate.y < _minY) {
        _minY = coordinate.y;
      }
      if (coordinate.x > _maxX) {
        _maxX = coordinate.x;
      }
      if (coordinate.y > _maxY) {
        _maxY = coordinate.y;
      }
    }
    for (Coordinate coordinate : secondClosestContour) {
      if (coordinate.x < _minX) {
        _minX = coordinate.x;
      }
      if (coordinate.y < _minY) {
        _minY = coordinate.y;
      }
      if (coordinate.x > _maxX) {
        _maxX = coordinate.x;
      }
      if (coordinate.y > _maxY) {
        _maxY = coordinate.y;
      }
    }
    final double minX = _minX;
    final double minY = _minY;
    final double maxX = _maxX;
    final double maxY = _maxY;
    final int[] closestContourX = new int[closestContour.length];
    final int[] closestContourY = new int[closestContour.length];
    final int[] secondClosestContourX = new int[secondClosestContour.length];
    final int[] secondClosestContourY = new int[secondClosestContour.length];
    for (int i = 0; i < closestContour.length; i++) {
      closestContourX[i] = (int)(closestContour[i].x);
      closestContourY[i] = (int)(closestContour[i].y);
    }
    for (int i = 0; i < secondClosestContour.length; i++) {
      secondClosestContourX[i] = (int)secondClosestContour[i].x;
      secondClosestContourY[i] = (int)secondClosestContour[i].y;
    }
    final JPanel panel = new JPanel() {
      @Override
      public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D)g2;
        int width = getWidth();
        int height = getHeight();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, width, height);
        double scaleX = width / (maxX - minX);
        double scaleY = height / (maxY - minY);
        float scale = (float)Math.min(scaleX, scaleY);
        g.setTransform(getAffineTransform(width, height, minX, maxX, minY, maxY));
        g.setStroke(new BasicStroke(5f / scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(Color.RED);
        g.drawPolyline(closestContourX, closestContourY, closestContour.length);
        g.setStroke(new BasicStroke(10f / scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine((int) closestContourPoint.x, (int) closestContourPoint.y, (int) closestContourPoint.x, (int) closestContourPoint.y);
        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(5f / scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawPolyline(secondClosestContourX, secondClosestContourY, secondClosestContour.length);
        g.setStroke(new BasicStroke(10f / scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine((int) secondClosestContourPoint.x, (int) secondClosestContourPoint.y, (int) secondClosestContourPoint.x, (int) secondClosestContourPoint.y);
        g.setColor(Color.BLACK);
        g.drawLine((int)point.getX(), (int)point.getY(), (int)point.getX(), (int)point.getY());
      }
    };

    panel.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        AffineTransform affineTransform =
                getAffineTransform(panel.getWidth(), panel.getHeight(), minX, maxX, minY, maxY);
        Point2D.Double clicked = new Point2D.Double(x, y);
        Point2D.Double map = new Point2D.Double(0, 0);
        try {
          affineTransform.createInverse().transform(clicked, map);
          System.out.println(map);
        } catch (NoninvertibleTransformException e1) {
          e1.printStackTrace();
        }
      }
    });

    JFrame frame = new JFrame(point.toString() + " " + closestHeight + "m " + secondClosestHeight +"m");
    frame.getContentPane().add(panel);
    frame.setSize(new Dimension(800, 600));
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    try {
      Thread.sleep(500000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private AffineTransform getAffineTransform(int width, int height, double minX, double maxX, double minY, double maxY) {
    double scaleX = (maxX - minX) / (width - 10);
    double scaleY = (maxY - minY) / (height - 10);
    double scale;
    double xOffSet = 0;
    double yOffSet = 0;
    if (scaleX < scaleY) {
      scale = scaleY;
    }
    else {
      scale = scaleX;
    }
    yOffSet = height - ((height - (maxY - minY) / scale) / 2) - 5;
    xOffSet = ((width - (maxX - minX) / scale) / 2);
    System.out.println("xOffset: " + xOffSet);
    System.out.println("yOffset: " + yOffSet);
    System.out.println("minX: " + minX);
    System.out.println("minY: " + minY);
    System.out.println("scale: " + scale);

    AffineTransform affineTransform = AffineTransform.getTranslateInstance(xOffSet, yOffSet);
    affineTransform.concatenate(AffineTransform.getScaleInstance(1d/scale, -1d/scale));
    affineTransform.concatenate(AffineTransform.getTranslateInstance(-minX, -minY));
    return affineTransform;
  }

}
