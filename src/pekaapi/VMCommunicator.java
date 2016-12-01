package pekaapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import midlet.MainMidlet;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class VMCommunicator {

  private interface ResultReceiver {

    public void onJSONError(JSONException e);

    public void onCommError(IOException e);
  }

  public interface GetStopPointsReceiver extends ResultReceiver {

    public void onStopPointsReceived(Vector stopPoints);
  }

  public interface BollardsByStopPointReceiver extends ResultReceiver {

    public void onBollardsByStopPointReceived(Vector bollardsWithDirections);
  }

  public interface BollardsByStreetReceiver extends ResultReceiver {

    public void onBollardsByStreetReceived(Vector bollardsWithDirections);
  }

  public interface BollardsByLineReceiver extends ResultReceiver {

    public void onBollardsByLineReceived(Vector lineRoutes);
  }

  public interface LinesReceiver extends ResultReceiver {

    public void onLinesReceived(Vector lineNames);
  }

  public interface StreetsReceiver extends ResultReceiver {

    public void onStreesReceived(Vector streets);
  }

  public interface TimesReceiver extends ResultReceiver {

    public void onTimesReceived(BollardWithTimes times);
  }

  public interface BollardsWithTimesReceiver extends ResultReceiver {

    public void onBollardsWithTimesReceived(Vector bollardsWithTimes);
  }

  private static HttpConnection createConnection() throws IOException {
    HttpConnection conn = (HttpConnection) Connector.open(
            "http://www.peka.poznan.pl/vm/method.vm?ts=" + System.currentTimeMillis());
    conn.setRequestMethod(HttpConnection.POST);
    conn.setRequestProperty("User-Agent", "PEKA-VirtualMonitor-J2ME/1.0");
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    return conn;
  }

  private static class Invocation {

    public Invocation(final String m, final String a) {
      methodName = m;
      arguments = a;
    }
    public final String methodName;
    public final String arguments;
  }

  private static void addMethodParams(OutputStream connOutputStream, Invocation params)
          throws IOException {
    try {
      String postParams = "method=" + params.methodName + "&p0=" + params.arguments;
      byte[] encodedParams = postParams.getBytes(MainMidlet.getUTF8Name());
      connOutputStream.write(encodedParams);
    } catch (UnsupportedEncodingException ex) {
      // this should never happen, since we check for this at app initialization time and explicitly
      // forbid the application from running.
    }
  }

  private interface MethodContext {

    Invocation getParams();

    void parse(JSONObject o) throws JSONException;
  }

  private static void readVirtualMonitorData(ResultReceiver rcv, MethodContext m) {
    HttpConnection conn = null;
    OutputStream out = null;
    InputStream in = null;
    IOException ioEx = null;
    JSONException jsonEx = null;
    try {
      conn = createConnection();
      out = conn.openOutputStream();
      addMethodParams(out, m.getParams());
      in = conn.openInputStream();
      m.parse(getJSONFromInputStream(in));
    } catch (IOException ex) {
      ioEx = ex;
    } catch (JSONException ex) {
      jsonEx = ex;
    } finally {
      try {
        // no option but to ignore these.
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (IOException e) {
      }
    }
    if (ioEx != null) {
      rcv.onCommError(ioEx);
    }
    if (jsonEx != null) {
      rcv.onJSONError(jsonEx);
    }
  }

  public static void getStopPoints(final String pattern, final GetStopPointsReceiver cbk) {
    readVirtualMonitorData(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("pattern", pattern);
          return new Invocation("getStopPoints", o.toString());
        } catch (JSONException e) {
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray result = o.getJSONArray("success");
        Vector stopPoints = new Vector(result.length());
        for (int i = 0; i < result.length(); ++i) {
          stopPoints.addElement(new StopPoint(result.getJSONObject(i)));
        }
        cbk.onStopPointsReceived(stopPoints);
      }
    });
  }

  public static void getBollardsByStopPoint(final String name,
          final BollardsByStopPointReceiver cbk) {
    readVirtualMonitorData(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("name", name);
          return new Invocation("getBollardsByStopPoint", o.toString());
        } catch (JSONException e) {
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray bollards = o.getJSONObject("success").getJSONArray("bollards");
        Vector bollardsWithDirections = new Vector(bollards.length());
        for (int i = 0; i < bollards.length(); ++i) {
          bollardsWithDirections.addElement(new BollardWithDirections(bollards.getJSONObject(i)));
        }
        cbk.onBollardsByStopPointReceived(bollardsWithDirections);
      }
    });
  }

  public static void getBollardsByStreet(final String name, final BollardsByStreetReceiver cbk) {
    readVirtualMonitorData(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("name", name);
          return new Invocation("getBollardsByStreet", o.toString());
        } catch (JSONException e) {
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray bollards = o.getJSONObject("success").getJSONArray("bollards");
        Vector bollardsWithDirections = new Vector(bollards.length());
        for (int i = 0; i < bollards.length(); ++i) {
          bollardsWithDirections.addElement(new BollardWithDirections(bollards.getJSONObject(i)));
        }
        cbk.onBollardsByStreetReceived(bollardsWithDirections);
      }
    });
  }

  public static void getBollardsByLine(final String name, final BollardsByLineReceiver cbk) {
    readVirtualMonitorData(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("name", name);
          return new Invocation("getBollardsByLine", o.toString());
        } catch (JSONException e) {
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray directions = o.getJSONObject("success").getJSONArray("directions");
        Vector lineRoutes = new Vector(directions.length());
        for (int i = 0; i < directions.length(); ++i) {
          lineRoutes.addElement(new LineRoute(directions.getJSONObject(i)));
        }
        cbk.onBollardsByLineReceived(lineRoutes);
      }
    });
  }

  public static void getLines(final String pattern, final LinesReceiver cbk) {
    readVirtualMonitorData(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("pattern", pattern);
          return new Invocation("getLines", o.toString());
        } catch (JSONException e) {
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray names = o.getJSONArray("success");
        Vector lineNames = new Vector(names.length());
        for (int i = 0; i < names.length(); ++i) {
          lineNames.addElement(names.getJSONObject(i).getString("name"));
        }
        cbk.onLinesReceived(lineNames);
      }
    });
  }

  public static void getStreets(final String pattern, final StreetsReceiver cbk) {
    readVirtualMonitorData(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("pattern", pattern);
          return new Invocation("getStreets", o.toString());
        } catch (JSONException e) {
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray streetsArray = o.getJSONArray("success");
        Vector streets = new Vector(streetsArray.length());
        for (int i = 0; i < streetsArray.length(); ++i) {
          streets.addElement(new Street(streetsArray.getJSONObject(i)));
        }
        cbk.onStreesReceived(streets);
      }
    });
  }

  public static void getTimes(final String bollardSymbol, final TimesReceiver cbk) {
    readVirtualMonitorData(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("symbol", bollardSymbol);
          return new Invocation("getTimes", o.toString());
        } catch (JSONException e) {
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        cbk.onTimesReceived(new BollardWithTimes(o.getJSONObject("success")));
      }
    });
  }

  public static void getTimesForAllBollards(final String stopPointName,
          final BollardsWithTimesReceiver cbk) {
    readVirtualMonitorData(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("name", stopPointName);
          return new Invocation("getTimesForAllBollards", o.toString());
        } catch (JSONException e) {
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray array = o.getJSONObject("success").getJSONArray("bollardsWithTimes");
        Vector bollardsWithTimes = new Vector(array.length());
        for (int i = 0; i < array.length(); ++i) {
          bollardsWithTimes.addElement(new BollardWithTimes(array.getJSONObject(i)));
        }
        cbk.onBollardsWithTimesReceived(bollardsWithTimes);
      }
    });
  }

  private static JSONObject getJSONFromInputStream(InputStream in)
          throws IOException, JSONException {
    // okay, bear with me, but this is why this is needed :
    // * we can't use StringBuffer, since its interface is not compatible with a UTF-8 bytestream :
    // it expects to be passed chars or strings. neither works here : a char is a 16-bit UTF-16
    // value, and we can't construct Strings from every byte we read since it might not represent a
    // whole UTF-8 character.
    // * we could theoretically use Vector, but it requires a *single* Object on input. not only
    // would we have to perform boxing, but also call read() with essentially a 1-byte buffer, which
    // is an atrocity.
    // so, the only viable solution is to slurp the whole bytestream into a manually-resized array
    // and then interpret its contents as UTF-8. honestly, I've never expected to do such relatively
    // low-level stuff in Java.

    byte[] buf = new byte[4096]; // hopefully enough for one go...
    int readBytes;
    int offset = 0;
    while ((readBytes = in.read(buf, offset, buf.length - offset)) != -1) {
      offset += readBytes;
      if (offset == buf.length) {
        byte[] newbuf = new byte[buf.length * 2];
        System.arraycopy(buf, 0, newbuf, 0, buf.length);
        buf = newbuf;
      }
    }
    return new JSONObject(new String(buf, 0, offset, MainMidlet.getUTF8Name()));
  }
}
