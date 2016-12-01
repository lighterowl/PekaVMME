package midlet;

import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import pekaapi.StopPoint;

public class StopPointsList extends List {

  private final Vector mStopPoints;
  private final MainMidlet mMidlet;
  private static final Command BACK_COMMAND = new Command("Powrót", Command.BACK, 1);
  private static final Command CHOOSE_STOP_POINT = new Command("Wybierz", Command.ITEM, 1);

  public StopPointsList(MainMidlet midlet, Vector stopPoints) {
    super("Lista przystanków", Choice.EXCLUSIVE);
    mMidlet = midlet;
    mStopPoints = stopPoints;
    addCommand(BACK_COMMAND);
    addCommand(CHOOSE_STOP_POINT);
    for (int i = 0; i < mStopPoints.size(); ++i) {
      append(((StopPoint) mStopPoints.elementAt(i)).getName(), null);
    }
    setCommandListener(new CommandListener() {
      public void commandAction(Command c, Displayable d) {
        if (c == BACK_COMMAND) {
          mMidlet.goBack();
        } else if (c == CHOOSE_STOP_POINT) {
          // ask midlet to display the bollards for the chosen stop point.
        }
      }
    });
  }
}
