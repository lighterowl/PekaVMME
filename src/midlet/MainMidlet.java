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
import util.Serialization;

/**
 * @author Daniel
 */
public class MainMidlet extends MIDlet implements VMCommunicator.ResultReceiver {

  private final MainForm mMainForm;
  private final AddStopsForm mAddStopsForm;
  private StopPointsList mStopPointsList;
  private BollardsList mBollardsList;

  public MainMidlet() {
    mMainForm = new MainForm(this);
    mAddStopsForm = new AddStopsForm(this);
  }

  public void startApp() {
    if (Serialization.initializeUTF8()) {
      disp().setCurrent(mMainForm);
    } else {
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
    } else if (current == mBollardsList) {
      disp().setCurrent(mStopPointsList);
      mBollardsList = null;
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

  public void displayBollardsAtStopPoint(final String stopPointName) {
    disp().setCurrent(createDownloadingAlert());
    Thread t = new Thread(new Runnable() {
      public void run() {
        VMCommunicator.getBollardsByStopPoint(stopPointName,
                new VMCommunicator.BollardsByStopPointReceiver() {
          public void onBollardsByStopPointReceived(final Vector bollardsWithDirections) {
            disp().callSerially(new Runnable() {
              public void run() {
                mBollardsList = new BollardsList(MainMidlet.this, bollardsWithDirections);
                disp().setCurrent(mBollardsList);
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
