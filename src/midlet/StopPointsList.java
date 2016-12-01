package midlet;

import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.List;
import pekaapi.StopPoint;

public class StopPointsList extends List {

  private final Vector mStopPoints;

  public StopPointsList(Vector stopPoints) {
    super("Lista przystanków", Choice.EXCLUSIVE);
    mStopPoints = stopPoints;
    for (int i = 0; i < mStopPoints.size(); ++i) {
      append(((StopPoint) mStopPoints.elementAt(i)).getName(), null);
    }
  }
}
