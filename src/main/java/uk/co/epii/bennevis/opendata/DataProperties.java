package uk.co.epii.bennevis.opendata;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: James Robinson
 * Date: 30/03/2015
 * Time: 17:57
 */
public class DataProperties {

  static {
    loadProperties();
  }

  public static String CONTOUR_FOLDER = System.getProperty("ContourFolder");
  public static String TEMP_LOCATION = System.getProperty("TempFolder");
  public static String POINTS_FOLDER = System.getProperty("PointsFolder");

  public static void loadProperties() {
    InputStream is = null;
    try {
      is = DataProperties.class.getResourceAsStream("/data.properties");
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

}
