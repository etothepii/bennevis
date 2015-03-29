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
  private final int northing;
  private final int easting;

  public AltitudeLocation(String largeSquare, String smallSquare, int northing, int easting) {
    this.largeSquare = largeSquare;
    this.smallSquare = smallSquare;
    this.northing = northing;
    this.easting = easting;
  }

  public String getLargeSquare() {
    return largeSquare;
  }

  public String getSmallSquare() {
    return smallSquare;
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
    char[] sixFig = osRef.toSixFigureString().toCharArray();
    String bigSquare = sixFig[0] + "" + sixFig[1];
    String smallSquare = sixFig[2] + "" + sixFig[5];
    return new AltitudeLocation(bigSquare, smallSquare, (int)osRef.getNorthing(), (int)osRef.getEasting());
  }
    
  private static AltitudeLocation fromRoundedOSRef(int easting, int northing) {
    return fromOSRef(new OSRef(easting, northing));
  }

  @Override
  public boolean accept(File dir, String name) {
    String upper = name.toUpperCase();
    String testFor = largeSquare.toUpperCase() + getSmallSquare();
    return upper.startsWith(testFor) && upper.endsWith(".ZIP");
  }
}
