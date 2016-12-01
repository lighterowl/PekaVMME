package util;

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
}
