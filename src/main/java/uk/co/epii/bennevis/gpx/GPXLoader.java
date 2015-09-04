package uk.co.epii.bennevis.gpx;

import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

  public String getName() {
    NodeList nodeList = document.getElementsByTagName("name");
    if (nodeList.getLength() == 0) {
      return "Route" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
    else {
      Node node = nodeList.item(0);
      if (node instanceof CharacterData) {
        CharacterData characterData = (CharacterData)node;
        return ((CharacterData) node).getData().trim();
      }
      else {
        return node.getTextContent().trim();
      }
    }
  }

  public OSRef[] getPoints() {
    NodeList nodeList = document.getElementsByTagName("rtept");
    ArrayList<OSRef> points = new ArrayList<OSRef>();
    OSRef prev = null;
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      NamedNodeMap attributes = node.getAttributes();
      LatLng latLng = new LatLng(
              Double.parseDouble(attributes.getNamedItem("lat").getTextContent()),
              Double.parseDouble(attributes.getNamedItem("lon").getTextContent())
      );
      latLng.toOSGB36();
      OSRef next = latLng.toOSRef();
      if (prev != null) {
        double x = next.getEasting() - prev.getEasting();
        double y = next.getNorthing() - prev.getNorthing();
        double d2 = x * x + y * y;
        if (d2 > 256) {
          double d = Math.sqrt(d2);
          int split = (int)Math.ceil(d / 16);
          for (int j = 1; j < split; j++) {
            points.add(new OSRef(prev.getEasting() + x * j / split, prev.getNorthing() + y * j / split));
          }
        }
      }
      points.add(next);
      prev = next;
    }
    return points.toArray(new OSRef[points.size()]);
  }

}
