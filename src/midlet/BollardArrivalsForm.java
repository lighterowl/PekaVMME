package midlet;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import pekaapi.ArrivalTime;

public class BollardArrivalsForm extends Form {

  private final MainMidlet mMidlet;
  private final Vector mArrivalTimes;

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
  }
}
