
import javax.microedition.lcdui.Alert;
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
    disp().setCurrent(mMainForm);
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
    Displayable current = Display.getDisplay(this).getCurrent();
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
}
