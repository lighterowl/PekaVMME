
import java.io.UnsupportedEncodingException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.*;

/**
 * @author Daniel
 */
public class MainMidlet extends MIDlet {

  private final MainForm mMainForm;
  private final AddStopsForm mAddStopsForm;

  public MainMidlet() {
    mMainForm = new MainForm(this);
    mAddStopsForm = new AddStopsForm(this);
  }

  public void startApp() {
    if (initializeUTF8()) {
      disp().setCurrent(mMainForm);
    }
  }

  public void pauseApp() {
  }

  public void destroyApp(boolean unconditional) {
    cleanup();
  }

  private void cleanup() {
  }

  public void shutdown() {
    cleanup();
    notifyDestroyed();
  }

  public void displayAddStopsForm() {
    disp().setCurrent(mAddStopsForm);
  }

  public void goBack() {
    Displayable current = disp().getCurrent();
    if (current == mAddStopsForm) {
      disp().setCurrent(mMainForm);
    }
  }

  public void displayAlert(Alert a) {
    Displayable current = disp().getCurrent();
    disp().setCurrent(a, current);
  }

  private Display disp() {
    return Display.getDisplay(this);
  }

  public static String getUTF8Name() {
    return NAME_FOR_UTF8;
  }
  private static String NAME_FOR_UTF8;

  private boolean initializeUTF8() {
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
        Alert utf8NotSupported = new Alert("Błąd krytyczny", "Implementacja nie wspiera UTF-8. "
                + "Aplikacja nie będzie działać poprawnie", null, AlertType.ERROR);
        disp().setCurrent(utf8NotSupported, mMainForm);
        return false;
      }
    }
    return true;
  }
}
