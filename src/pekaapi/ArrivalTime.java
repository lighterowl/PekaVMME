package pekaapi;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class ArrivalTime {

  private final boolean mRealTime;
  private final int mMinutesToArrive;
  private final String mDestination;
  private final boolean mAtStopPoint;
  // "departure" field skipped due to CLDC 1.1's Date API being too limited.
  private final String mLine;

  public ArrivalTime(JSONObject obj) throws JSONException {
    mRealTime = obj.getBoolean("realTime");
    mMinutesToArrive = obj.getInt("minutes");
    mDestination = obj.getString("direction");
    mAtStopPoint = obj.getBoolean("onStopPoint");
    mLine = obj.getString("line");
  }

  public boolean isRealTime() {
    return mRealTime;
  }

  public int getMinutesToArrive() {
    return mMinutesToArrive;
  }

  public String getDestination() {
    return mDestination;
  }

  public boolean isAtStopPoint() {
    return mAtStopPoint;
  }

  public String getLine() {
    return mLine;
  }
}
