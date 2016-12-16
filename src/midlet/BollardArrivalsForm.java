package midlet;

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import pekaapi.ArrivalTime;

public class BollardArrivalsForm extends Form {

  private final MainMidlet mMidlet;
  private final Vector mArrivalTimes;
  private static final Command BACK_COMMAND = new Command("Powrót", Command.BACK, 1);

  public BollardArrivalsForm(MainMidlet midlet, Vector arrivalTimes) {
    super("Odjazdy");
    mMidlet = midlet;
    mArrivalTimes = arrivalTimes;

    for (int i = 0; i < mArrivalTimes.size(); ++i) {
      ArrivalTime t = (ArrivalTime) mArrivalTimes.elementAt(i);
      System.err.println(t.getLine());
      System.err.println(t.getDestination());
      System.err.println(t.getMinutesToArrive());
      StringItem line = new StringItem(t.getLine(), null);
      line.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
      append(line);
      StringItem dest = new StringItem(t.getDestination(), null);
      append(dest);
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
