package pekaapi;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class LineDirection {

  private final String mLine;
  private final String mDestination;

  public LineDirection(JSONObject obj) throws JSONException {
    mLine = obj.getString("lineName");
    mDestination = obj.getString("direction");
  }

  public String getLine() {
    return mLine;
  }

  public String getDestination() {
    return mDestination;
  }
}
