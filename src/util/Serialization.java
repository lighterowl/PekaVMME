package util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Serialization {

  public static boolean initializeUTF8() {
    String xyz = "foobar";
    try {
      // some J2ME implementations in real phones use UTF8 as the name for the encoding. check which
      // one is supported and then use it throughout the application. bail if none is, since it
      // won't be possible to send properly encoded data then.
      NAME_FOR_UTF8 = "UTF-8";
      xyz.getBytes(NAME_FOR_UTF8);
    } catch (UnsupportedEncodingException ex1) {
      try {
        // pretty please...
        NAME_FOR_UTF8 = "UTF8";
        xyz.getBytes(NAME_FOR_UTF8);
      } catch (UnsupportedEncodingException ex2) {
        // nope.
        return false;
      }
    }
    return true;
  }

  public static String getUTF8Name() {
    return NAME_FOR_UTF8;
  }
  private static String NAME_FOR_UTF8;

  public static byte[] toByteArray(String s) {
    byte[] encodedString = s.getBytes();
    byte[] encodedLength = {
      (byte) (encodedString.length >>> 24),
      (byte) (encodedString.length >>> 16),
      (byte) (encodedString.length >>> 8),
      (byte) encodedString.length};
    byte[] serialized = new byte[4 + encodedString.length];
    System.arraycopy(encodedLength, 0, serialized, 0, 4);
    System.arraycopy(encodedString, 0, serialized, 4, encodedString.length);
    return serialized;
  }

  public static String fromByteArray(ByteArrayInputStream stream) throws IOException {
    byte[] encodedLength = new byte[4];
    stream.read(encodedLength);
    int length = (encodedLength[0] << 24)
            | (encodedLength[1] << 16)
            | (encodedLength[2] << 8)
            | encodedLength[3];
    byte[] encodedString = new byte[length];
    stream.read(encodedString);
    return new String(encodedString);
  }
}
