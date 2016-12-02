package pekaapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import util.Serialization;

public class Bollard {

  private final String mSymbol;
  private final String mTag;
  private final String mName;

  public Bollard(JSONObject obj) throws JSONException {
    mSymbol = obj.getString("symbol");
    mTag = obj.getString("tag");
    mName = obj.getString("name");
  }

  public Bollard(ByteArrayInputStream serialized) throws IOException {
    mSymbol = Serialization.fromByteArray(serialized);
    mTag = Serialization.fromByteArray(serialized);
    mName = Serialization.fromByteArray(serialized);
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

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    output.write(Serialization.toByteArray(mSymbol));
    output.write(Serialization.toByteArray(mTag));
    output.write(Serialization.toByteArray(mName));
    return output.toByteArray();
  }
}
