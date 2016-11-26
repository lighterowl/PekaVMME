
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
    Display.getDisplay(this).setCurrent(mMainForm);
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
    Display.getDisplay(this).setCurrent(mAddStopsForm);
  }

  public void goBack() {
    Displayable current = Display.getDisplay(this).getCurrent();
    if (current == mAddStopsForm) {
      Display.getDisplay(this).setCurrent(mMainForm);
    }
  }

  public void displayAlert(Alert a) {
    Displayable current = Display.getDisplay(this).getCurrent();
    Display.getDisplay(this).setCurrent(a, current);
  }
}
