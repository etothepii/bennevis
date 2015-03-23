package uk.co.epii.bennevis.gpx;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.OSRef;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

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
      document.getDocumentElement().normalize();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public OSRef[] getPoints() {
    NodeList nodeList = document.getElementsByTagName("rtept");
    ArrayList<OSRef> points = new ArrayList<OSRef>();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      NamedNodeMap attributes = node.getAttributes();
      LatLng latLng = new LatLng(
              Double.parseDouble(attributes.getNamedItem("lat").getTextContent()),
              Double.parseDouble(attributes.getNamedItem("lon").getTextContent())
      );
      latLng.toOSGB36();
      points.add(latLng.toOSRef());
    }
    return points.toArray(new OSRef[points.size()]);
  }

}
