package pekaapi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import midlet.MainMidlet;
import org.json.me.JSONException;

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

  private static void addMethodParams(OutputStream connOutputStream, String method,
          String jsonParams) throws IOException {
    try {
      String postParams = "method=" + method + "&p0=" + jsonParams;
      byte[] encodedParams = postParams.getBytes(MainMidlet.getUTF8Name());
      connOutputStream.write(encodedParams);
    } catch (UnsupportedEncodingException ex) {
      // this should never happen, since we check for this at app initialization time and explicitly
      // forbid the application from running.
    }
  }
}
