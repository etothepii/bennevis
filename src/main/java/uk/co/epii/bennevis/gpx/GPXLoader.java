package uk.co.epii.bennevis.gpx;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * User: James Robinson
 * Date: 22/03/2015
 * Time: 20:01
 */
public class GPXLoader {

  private Document document;

  public void loadFile(InputStream inputStream) {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      dbf.setValidating(false);
      dbf.setIgnoringComments(false);
      dbf.setIgnoringElementContentWhitespace(true);
      dbf.setNamespaceAware(true);

      DocumentBuilder db = null;
      db = dbf.newDocumentBuilder();
      db.setEntityResolver(new EntityResolver() {
        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
          return new InputSource(new StringReader(""));
        }
      });

      document = db.parse(inputStream);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Point2D.Float[] getPoints() {
    return null;
  }

}
