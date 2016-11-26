package pekaapi;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Street {

  private final int mId;
  private final String mName;

  public Street(JSONObject obj) throws JSONException {
    mId = obj.getInt("id");
    mName = obj.getString("name");
  }

  public int getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }
}
