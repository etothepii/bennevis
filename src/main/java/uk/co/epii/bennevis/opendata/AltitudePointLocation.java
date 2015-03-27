package uk.co.epii.bennevis.opendata;

import uk.me.jstott.jcoord.OSRef;

import java.io.File;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 00:26
 */
public class AltitudePointLocation extends AltitudeLocation {

  private final int row;
  private final int col;

  public AltitudePointLocation(String largeSquare, String smallSquare, int row, int col, int northing, int easting) {
    super(largeSquare, smallSquare, northing, easting);
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public static AltitudePointLocation fromOSRef(OSRef osRef) {
    return fromRoundedOSRef(
            50 * (int) (Math.round(osRef.getEasting() / 50)),
            50 * (int) (Math.round(osRef.getNorthing() / 50)));
  }
    
  private static AltitudePointLocation fromRoundedOSRef(int roundedEasting, int roundedNorthing) {
    OSRef rounded = new OSRef(roundedEasting, roundedNorthing);
    char[] sixFig = rounded.toSixFigureString().toCharArray();
    String bigSquare = sixFig[0] + "" + sixFig[1];
    String smallSquare = sixFig[2] + "" + sixFig[5];
    int row = 199 - ((int)(rounded.getNorthing() / 50) % 200);
    int column = (int)(rounded.getEasting() / 50) % 200;
    return new AltitudePointLocation(bigSquare, smallSquare, row, column, roundedNorthing, roundedEasting);
  }

  @Override
  public boolean accept(File dir, String name) {
    String upper = name.toUpperCase();
    String testFor = getLargeSquare().toUpperCase() + getSmallSquare();
    return upper.startsWith(testFor) && upper.endsWith(".ZIP");
  }

  public static AltitudePointLocation[] cornersFromOSRef(OSRef osRef) {
    int northing = (int)osRef.getNorthing();
    int easting = (int)osRef.getEasting();
    return new AltitudePointLocation[] {
            fromRoundedOSRef(50 * (easting / 50), 50 * (northing / 50)),
            fromRoundedOSRef(50 * (easting / 50 + 1), 50 * (northing / 50)),
            fromRoundedOSRef(50 * (easting / 50), 50 * (northing / 50 + 1)),
            fromRoundedOSRef(50 * (easting / 50 + 1), 50 * (northing / 50 + 1))
    };
  }
}
