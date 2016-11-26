package pekaapi;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class StopPoint {

  private final String mSymbol;
  private final String mName;

  public StopPoint(JSONObject obj) throws JSONException {
    mSymbol = obj.getString("symbol");
    mName = obj.getString("name");
  }

  public String getSymbol() {
    return mSymbol;
  }

  public String getName() {
    return mName;
  }
}
