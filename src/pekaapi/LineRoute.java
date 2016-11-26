package pekaapi;

import java.util.Vector;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class LineRoute {

  private final LineDirection mDirection;
  private final Vector mBollards; // Vector<Bollard>

  public LineRoute(JSONObject obj) throws JSONException {
    mDirection = new LineDirection(obj.getJSONObject("direction"));
    JSONArray bollards = obj.getJSONArray("bollards");
    mBollards = new Vector(bollards.length());
    for (int i = 0; i < bollards.length(); ++i) {
      mBollards.addElement(new Bollard(bollards.getJSONObject(i)));
    }
  }

  public LineDirection getDirection() {
    return mDirection;
  }

  public Vector getBollards() {
    return mBollards;
  }
}
