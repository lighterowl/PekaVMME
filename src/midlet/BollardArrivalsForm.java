package midlet;

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import pekaapi.ArrivalTime;
import util.Sorting;

public class BollardArrivalsForm extends Form {

  private final MainMidlet mMidlet;
  private final Vector mArrivalTimes;
  private static final Command BACK_COMMAND = new Command("Powrót", Command.BACK, 1);

  public BollardArrivalsForm(MainMidlet midlet, Vector arrivalTimes) {
    super("Odjazdy");
    mMidlet = midlet;
    mArrivalTimes = arrivalTimes;

    // ensure that the vector is always sorted according to the ETA in ascending order.
    util.Sorting.sort(mArrivalTimes, new Sorting.Comparator() {
      public boolean isLessThan(Object a, Object b) {
        ArrivalTime ta = (ArrivalTime) a;
        ArrivalTime tb = (ArrivalTime) b;
        return ta.getMinutesToArrive() < tb.getMinutesToArrive();
      }
    });

    for (int i = 0; i < mArrivalTimes.size(); ++i) {
      ArrivalTime t = (ArrivalTime) mArrivalTimes.elementAt(i);
      StringBuffer buf = new StringBuffer();
      buf.append(t.getLine()).append(" => ").append(t.getDestination()).append(" : ").
              append(t.getMinutesToArrive()).append(" min");
      append(new StringItem(buf.toString(), null));
    }

    addCommand(BACK_COMMAND);
    setCommandListener(new CommandListener() {
      public void commandAction(Command c, Displayable d) {
        if (c == BACK_COMMAND) {
          mMidlet.goBack();
        }
      }
    });
  }
}
