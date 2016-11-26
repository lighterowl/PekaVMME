
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.*;

/**
 * @author Daniel
 */
public class MainMidlet extends MIDlet {

  private MainForm mMainForm;
  private AddStopsForm mAddStopsForm;

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
}
