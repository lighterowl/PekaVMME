package midlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Gauge;
import javax.microedition.midlet.*;
import org.json.me.JSONException;
import pekaapi.VMCommunicator;

/**
 * @author Daniel
 */
public class MainMidlet extends MIDlet implements VMCommunicator.ResultReceiver {

  private final MainForm mMainForm;
  private final AddStopsForm mAddStopsForm;
  private StopPointsList mStopPointsList;

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
    } else if (current == mStopPointsList) {
      disp().setCurrent(mMainForm);
      mStopPointsList = null;
    }
  }

  public void displayAlert(Alert a) {
    Displayable current = disp().getCurrent();
    disp().setCurrent(a, current);
  }

  public void displayStopPoints(final String pattern) {
    disp().setCurrent(createDownloadingAlert());
    Thread t = new Thread(new Runnable() {
      public void run() {
        VMCommunicator.getStopPoints(pattern, new VMCommunicator.GetStopPointsReceiver() {
          public void onStopPointsReceived(final Vector stopPoints) {
            disp().callSerially(new Runnable() {
              public void run() {
                mStopPointsList = new StopPointsList(MainMidlet.this, stopPoints);
                disp().setCurrent(mStopPointsList);
              }
            });
          }

          public void onJSONError(JSONException e) {
            onJSONError(e);
          }

          public void onCommError(IOException e) {
            onCommError(e);
          }
        });
      }
    });
    t.start();
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
                + "Aplikacja zakończy działanie", null, AlertType.ERROR);
        utf8NotSupported.setTimeout(Alert.FOREVER);
        disp().setCurrent(utf8NotSupported);
        utf8NotSupported.setCommandListener(new CommandListener() {
          public void commandAction(Command c, Displayable d) {
            // nothing else to do.
            shutdown();
          }
        });
        return false;
      }
    }
    return true;
  }

  private Alert createDownloadingAlert() {
    Alert downloading = new Alert("Pobieranie danych", "Proszę czekać", null, AlertType.INFO);
    downloading.setTimeout(Alert.FOREVER);
    downloading.addCommand(new Command("Przerwij", Command.CANCEL, 1));
    downloading.setCommandListener(new CommandListener() {
      public void commandAction(Command c, Displayable d) {
        // TODO : stop the network connection thread
      }
    });
    downloading.setIndicator(new Gauge(null, false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING));
    return downloading;
  }

  private Alert createNetworkErrorAlert() {
    Alert networkError = new Alert("Błąd sieci", "Pobieranie danych nie powiodło się", null,
            AlertType.ERROR);
    networkError.setTimeout(Alert.FOREVER);
    return networkError;
  }

  public void onJSONError(JSONException e) {
    disp().callSerially(new Runnable() {
      public void run() {
        disp().setCurrent(createNetworkErrorAlert(), mMainForm);
      }
    });
    e.printStackTrace();
  }

  public void onCommError(IOException e) {
    disp().callSerially(new Runnable() {
      public void run() {
        disp().setCurrent(createNetworkErrorAlert(), mMainForm);
      }
    });
    e.printStackTrace();
  }
}
