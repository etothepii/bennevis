package uk.co.epii.bennevis;

import com.sun.net.httpserver.HttpServer;
import uk.co.epii.bennevis.encoder.JSONEncoder;
import uk.co.epii.bennevis.gpx.GPXLoader;
import uk.co.epii.bennevis.opendata.ContourAltimeter;
import uk.me.jstott.jcoord.OSRef;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {

  public static void main(String[] args) throws Exception {
    if (args.length == 1 && args[0].toUpperCase().equals("SERVER")) {
      runServer(args);
    }
    else {
      runLocally(args);
    }
  }

  private static void runServer(String[] args) throws IOException {
    InetSocketAddress addr = new InetSocketAddress(DataProperties.PORT);
    HttpServer server = HttpServer.create(addr, 0);
    server.createContext("/", new DefaultHttpHandlerImpl());
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();
    System.out.println("Server is listening on port " + DataProperties.PORT);
  }

  public static void runLocally(String[] args) throws FileNotFoundException {
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
    PrintWriter pw = new PrintWriter(out);
    pw.print(new JSONEncoder().encode(gpxLoader.getPoints(), altimeter));
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
