package midlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Gauge;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import org.json.me.JSONException;
import pekaapi.Bollard;
import pekaapi.BollardWithTimes;
import pekaapi.VMCommunicator;
import util.Serialization;

/**
 * @author Daniel
 */
public class MainMidlet extends MIDlet implements VMCommunicator.ResultReceiver {

  private final SavedBollardList mSavedBollardsList;
  private final AddStopsForm mAddStopsForm;
  private StopPointsList mStopPointsList;
  private BollardsList mBollardsList;
  private BollardArrivalsForm mBollardArrivals;
  private final Vector mSavedBollards;

  public MainMidlet() {
    mSavedBollards = new Vector();
    mSavedBollardsList = new SavedBollardList(this, mSavedBollards);
    mAddStopsForm = new AddStopsForm(this);
  }

  public void startApp() {
    if (Serialization.initializeUTF8()) {
      restoreSavedBollards();
      disp().setCurrent(mSavedBollardsList);
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
    saveBollardsIntoStorage();
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
      disp().setCurrent(mSavedBollardsList);
    } else if (current == mStopPointsList) {
      disp().setCurrent(mSavedBollardsList);
      mStopPointsList = null;
    } else if (current == mBollardsList) {
      disp().setCurrent(mStopPointsList);
      mBollardsList = null;
    } else if (current == mBollardArrivals) {
      disp().setCurrent(mSavedBollardsList);
      mBollardArrivals = null;
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

  public void displayBollardArrivalTimes(final Bollard b) {
    disp().setCurrent(createDownloadingAlert());
    Thread t = new Thread(new Runnable() {
      public void run() {
        VMCommunicator.getTimes(b.getTag(), new VMCommunicator.TimesReceiver() {
          public void onTimesReceived(final BollardWithTimes times) {
            disp().callSerially(new Runnable() {
              public void run() {
                mBollardArrivals = new BollardArrivalsForm(MainMidlet.this,
                        times.getArrivalTimes());
                disp().setCurrent(mBollardArrivals);
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

  public void addToSavedBollards(Vector newBollards) {
    // TODO : change this to a hashtable if performance suffers.
    for (int i = 0; i < newBollards.size(); ++i) {
      Object newBollard = newBollards.elementAt(i);
      if (!mSavedBollards.contains(newBollard)) {
        mSavedBollards.addElement(newBollard);
      }
    }
    mSavedBollardsList.updateBollards();
    disp().setCurrent(mSavedBollardsList);
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
        disp().setCurrent(createNetworkErrorAlert(), mSavedBollardsList);
      }
    });
    e.printStackTrace();
  }

  public void onCommError(IOException e) {
    disp().callSerially(new Runnable() {
      public void run() {
        disp().setCurrent(createNetworkErrorAlert(), mSavedBollardsList);
      }
    });
    e.printStackTrace();
  }

  private void restoreSavedBollards() {
    RecordStore rs = null;
    RecordEnumeration recordReader = null;
    ByteArrayInputStream input = null;
    try {
      rs = RecordStore.openRecordStore("PekaVMBollards", true);
      recordReader = rs.enumerateRecords(null, null, false);
      while (recordReader.hasNextElement()) {
        input = new ByteArrayInputStream(recordReader.nextRecord());
        mSavedBollards.addElement(new Bollard(input));
        input.close();
      }
      mSavedBollardsList.updateBollards();
    } catch (Exception ex) {
      ex.printStackTrace();
      // don't do anything : any errors here are not critical.
    } finally {
      try {
        if (input != null) {
          input.close();
        }
        if (recordReader != null) {
          recordReader.destroy();
        }
        if (rs != null) {
          rs.closeRecordStore();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  private void saveBollardsIntoStorage() {
    RecordStore rs = null;
    try {
      RecordStore.deleteRecordStore("PekaVMBollards");
    } catch (RecordStoreException ex) {
      ex.printStackTrace();
    }
    try {
      rs = RecordStore.openRecordStore("PekaVMBollards", true);
      for (int i = 0; i < mSavedBollards.size(); ++i) {
        byte[] serializedBollard = ((Bollard) mSavedBollards.elementAt(i)).toByteArray();
        rs.addRecord(serializedBollard, 0, serializedBollard.length);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.closeRecordStore();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
