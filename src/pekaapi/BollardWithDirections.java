package pekaapi;

import java.util.Vector;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class BollardWithDirections extends Bollard {

  private final Vector mDirections; // Vector<LineDirection>

  BollardWithDirections(JSONObject obj) throws JSONException {
    super(obj.getJSONObject("bollard"));
    JSONArray directions = obj.getJSONArray("directions");
    mDirections = new Vector(directions.length());
    for (int i = 0; i < directions.length(); ++i) {
      mDirections.addElement(new LineDirection(directions.getJSONObject(i)));
    }
  }

  public Vector getDirections() {
    return mDirections;
  }
}
