package uk.co.epii.bennevis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * User: James Robinson
 * Date: 22/03/2015
 * Time: 20:01
 */
public class TestData {

  public static String[][] loadCsv(String name) throws IOException {
    InputStreamReader inputStreamReader = null;
    BufferedReader bufferedReader = null;
    String in;
    try {
      inputStreamReader = new InputStreamReader(load(name));
      bufferedReader = new BufferedReader(inputStreamReader);
      ArrayList<String[]> strings = new ArrayList<String[]>();
      while ((in = bufferedReader.readLine()) != null) {
        strings.add(in.split(","));
      }
      return strings.toArray(new String[strings.size()][]);
    }
    finally {
      if (bufferedReader != null) {
        bufferedReader.close();
      }
      if (inputStreamReader != null) {
        inputStreamReader.close();
      }
    }
  }

  public static InputStream load(String name) {
    return TestData.class.getResourceAsStream("/TestData/" + name);
  }

}
