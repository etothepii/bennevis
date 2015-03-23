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

  private final String largeSquare;
  private final String smallSquare;
  private final int row;
  private final int col;
  private final int northing;
  private final int easting;

  public AltitudeLocation(String largeSquare, String smallSquare, int row, int col, int northing, int easting) {
    this.largeSquare = largeSquare;
    this.smallSquare = smallSquare;
    this.row = row;
    this.col = col;
    this.northing = northing;
    this.easting = easting;
  }

  public String getLargeSquare() {
    return largeSquare;
  }

  public String getSmallSquare() {
    return smallSquare;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public int getNorthing() {
    return northing;
  }

  public int getEasting() {
    return easting;
  }

  public OSRef toOSRef() {
    return new OSRef(easting, northing);
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
    return new AltitudeLocation(bigSquare, smallSquare, row, column, roundedNorthing, roundedEasting);
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
