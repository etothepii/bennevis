package uk.co.epii.bennevis;

import uk.co.epii.bennevis.gpx.GPXLoader;
import uk.co.epii.bennevis.opendata.ContourAltimeter;
import uk.me.jstott.jcoord.OSRef;

import java.io.*;

public class Main {

  public static void loadProperties() {
    InputStream is = null;
    try {
      is = Main.class.getResourceAsStream("/data.properties");
      System.getProperties().load(is);
    }
    catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  public static void main(String[] args) throws FileNotFoundException {
    loadProperties();
    InputStream is = null;
    OutputStream out = null;
    if (args.length == 0) {
      is = System.in;
      out = System.out;
    }
    if (args.length == 1) {
      is = new FileInputStream(args[0]);
      out = System.out;
    }
    if (args.length == 2) {
      is = new FileInputStream(args[0]);
      out = new FileOutputStream(args[1]);
    }
    GPXLoader gpxLoader = new GPXLoader();
    gpxLoader.loadFile(is);
    Altimeter altimeter = new ContourAltimeter();
    OSRef previous = null;
    double distance = 0;
    PrintWriter pw = new PrintWriter(out);
    for (OSRef osRef : gpxLoader.getPoints()) {
      pw.print(osRef.getEasting());
      pw.print(",");
      pw.print(osRef.getNorthing());
      pw.print(",");
      distance += distance(previous, osRef);
      pw.print(distance / 1609.344);
      pw.print(",");
      pw.print(altimeter.getAltitude(osRef));
      pw.println();
      previous = osRef;
    }
    pw.flush();
    pw.close();
  }

  private static double distance(OSRef previous, OSRef osRef) {
    if (previous == null) {
      return 0;
    }
    double dx = previous.getEasting() - osRef.getEasting();
    double dy = previous.getNorthing() - osRef.getNorthing();
    return Math.sqrt(dx * dx + dy * dy);
  }

}
