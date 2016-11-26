package pekaapi;

import java.util.Vector;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Bollard {

  private final String mSymbol;
  private final String mTag;
  private final String mName;

  public Bollard(JSONObject obj) throws JSONException {
    mSymbol = obj.getString("symbol");
    mTag = obj.getString("tag");
    mName = obj.getString("name");
  }

  public String getSymbol() {
    return mSymbol;
  }

  public String getTag() {
    return mTag;
  }

  public String getName() {
    return mName;
  }
}
