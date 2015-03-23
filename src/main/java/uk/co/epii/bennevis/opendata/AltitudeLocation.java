package uk.co.epii.bennevis.opendata;

import uk.me.jstott.jcoord.OSRef;

/**
 * User: James Robinson
 * Date: 23/03/2015
 * Time: 00:26
 */
public class AltitudeLocation {

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
    return null;
  }
}
