package uk.co.epii.bennevis;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.epii.bennevis.encoder.JSONEncoder;
import uk.co.epii.bennevis.gpx.GPXLoader;
import uk.co.epii.bennevis.opendata.ContourAltimeter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * User: James Robinson
 * Date: 30/03/2015
 * Time: 19:43
 */
public class DefaultHttpHandlerImpl implements HttpHandler {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultHttpHandlerImpl.class);

  public Altimeter altimeter = new ContourAltimeter();
  public JSONEncoder jsonEncoder = new JSONEncoder();

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    LOG.debug("Received a request");
    String requestMethod = httpExchange.getRequestMethod();
    if (requestMethod.toUpperCase().equals("POST")) {
      GPXLoader gpxLoader = new GPXLoader();
      OutputStream outputStream = null;
      try {
        gpxLoader.loadFile(httpExchange.getRequestBody());
        Headers responseHeaders = httpExchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, 0);
        outputStream = httpExchange.getResponseBody();
        outputStream.write(jsonEncoder.encode(gpxLoader.getPoints(), altimeter).getBytes());
      }
      finally {
        if (outputStream != null) {
          outputStream.close();
        }
      }
    }
  }
}
