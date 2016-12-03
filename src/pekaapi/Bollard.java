package pekaapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + (this.mSymbol != null ? this.mSymbol.hashCode() : 0);
    hash = 89 * hash + (this.mTag != null ? this.mTag.hashCode() : 0);
    hash = 89 * hash + (this.mName != null ? this.mName.hashCode() : 0);
    return hash;
  }

  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Bollard other = (Bollard) obj;
    if ((this.mSymbol == null) ? (other.mSymbol != null) : !this.mSymbol.equals(other.mSymbol)) {
      return false;
    }
    if ((this.mTag == null) ? (other.mTag != null) : !this.mTag.equals(other.mTag)) {
      return false;
    }
    if ((this.mName == null) ? (other.mName != null) : !this.mName.equals(other.mName)) {
      return false;
    }
    return true;
  }
}
