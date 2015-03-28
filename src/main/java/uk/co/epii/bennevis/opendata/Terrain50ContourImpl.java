package uk.co.epii.bennevis.opendata;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.epii.bennevis.IAltimeter;
import uk.co.epii.conservatives.robertwalpole.DataSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * User: James Robinson
 * Date: 27/03/2015
 * Time: 18:01
 */
public class Terrain50ContourImpl extends AbstractTerrain50 {

  private static final Logger LOG = LoggerFactory.getLogger(Terrain50FlatFileImpl.class);
  private final Map<File, IAltimeter> altimeters = new HashMap<File, IAltimeter>();

  private String rootDataFolder = "/Users/jrrpl/Downloads/terr50_cesh_gb/data/";
  private String tempLocation = "/tmp/Terrain50/";

  @Override
  protected String getRootDataFolder() {
    return rootDataFolder;
  }

  @Override
  protected double getAltitude(File zip, AltitudeLocation altitudeLocation) {
    IAltimeter altimeter = altimeters.get(zip);
    if (altimeter == null) {
      DataSet[] dataSets = loadZip(zip, altitudeLocation);
      if (dataSets == null) {
        return 0d;
      }
      altimeter = SmallSquareContour.fromDataSets(dataSets[0].getFeatureSource(), dataSets[1].getFeatureSource());
      altimeters.put(zip, altimeter);
    }
    return altimeter.getAltitude(altitudeLocation.toOSRef());
  }

  private DataSet[] loadZip(File zip, AltitudeLocation altitudeLocation) {
    try {
      ZipFile zipFile = new ZipFile(zip);
      for (Object fileHeaderObj : zipFile.getFileHeaders()) {
        FileHeader fileHeader = (FileHeader)fileHeaderObj;
        String fileName = fileHeader.getFileName().toUpperCase();
        InputStream inputStream = zipFile.getInputStream(fileHeader);
        FileOutputStream fileOutputStream = new FileOutputStream(tempLocation + fileName);
        byte[] bytes = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(bytes)) != -1) {
          fileOutputStream.write(bytes, 0, bytesRead);
        }
      }
      DataSet[] dataSets = new DataSet[] {
              DataSet.createFromFile(new File(
                      tempLocation +
                              altitudeLocation.getLargeSquare() +
                              altitudeLocation.getSmallSquare() + "_line.shp")),
              DataSet.createFromFile(new File(
                      tempLocation +
                              altitudeLocation.getLargeSquare() +
                              altitudeLocation.getSmallSquare() + "_point.shp"))
      };
      return dataSets;
    }
    catch (Exception e) {
      LOG.warn("An Exception has occured loading data from the zip file", e);
    }
    return null;
  }
}
