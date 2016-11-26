package pekaapi;

import java.util.Vector;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class BollardWithTimes extends Bollard {

  private final Vector mArrivalTimes; // Vector<ArrivalTime>

  public BollardWithTimes(JSONObject obj) throws JSONException {
    super(obj.getJSONObject("bollard"));
    JSONArray times = obj.getJSONArray("times");
    mArrivalTimes = new Vector(times.length());
    for (int i = 0; i < times.length(); ++i) {
      mArrivalTimes.addElement(new ArrivalTime(times.getJSONObject(i)));
    }
  }
}
