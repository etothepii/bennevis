package uk.co.epii.bennevis.opendata;

import uk.me.jstott.jcoord.OSRef;

import java.io.File;
import java.io.FilenameFilter;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 00:26
 */
public class AltitudeLocation implements FilenameFilter {

  private String largeSquare;
  private String smallSquare;
  private int row;
  private int col;

  public String getLargeSquare() {
    return largeSquare;
  }

  public void setLargeSquare(String largeSquare) {
    this.largeSquare = largeSquare;
  }

  public String getSmallSquare() {
    return smallSquare;
  }

  public void setSmallSquare(String smallSquare) {
    this.smallSquare = smallSquare;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public static AltitudeLocation fromOSRef(OSRef osRef) {
    return fromRoundedOSRef(
            50 * (int) (Math.round(osRef.getEasting() / 50)),
            50 * (int) (Math.round(osRef.getNorthing() / 50)));
  }
    
  private static AltitudeLocation fromRoundedOSRef(int roundedEasting, int roundedNorthing) {
    OSRef rounded = new OSRef(roundedEasting, roundedNorthing);
    char[] sixFig = rounded.toSixFigureString().toCharArray();
    String bigSquare = sixFig[0] + "" + sixFig[1];
    String smallSquare = sixFig[2] + "" + sixFig[5];
    int row = 199 - ((int)(rounded.getNorthing() / 50) % 200);
    int column = (int)(rounded.getEasting() / 50) % 200;
    AltitudeLocation altitudeLocation = new AltitudeLocation();
    altitudeLocation.setCol(column);
    altitudeLocation.setRow(row);
    altitudeLocation.setLargeSquare(bigSquare);
    altitudeLocation.setSmallSquare(smallSquare);
    return altitudeLocation;
  }

  @Override
  public boolean accept(File dir, String name) {
    String upper = name.toUpperCase();
    String testFor = largeSquare.toUpperCase() + getSmallSquare();
    return upper.startsWith(testFor) && upper.endsWith(".ZIP");
  }

  public static AltitudeLocation[] cornersFromOSRef(OSRef osRef) {
    int northing = (int)osRef.getNorthing();
    int easting = (int)osRef.getEasting();
    return new AltitudeLocation[] {
            fromRoundedOSRef(50 * (easting / 50), 50 * (northing / 50)),
            fromRoundedOSRef(50 * (easting / 50 + 1), 50 * (northing / 50)),
            fromRoundedOSRef(50 * (easting / 50), 50 * (northing / 50 + 1)),
            fromRoundedOSRef(50 * (easting / 50 + 1), 50 * (northing / 50 + 1))
    };
  }
}
