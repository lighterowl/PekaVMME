package midlet;

import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import pekaapi.BollardWithDirections;
import pekaapi.LineDirection;

public class BollardsList extends List {

  private final MainMidlet mMidlet;
  private final Vector mBollards;
  private static final Command ADD_BOLLARDS = new Command("Zaznacz", Command.ITEM, 1);
  private static final Command BACK_COMMAND = new Command("Powrót", Command.BACK, 1);

  public BollardsList(MainMidlet midlet, Vector bollardsWithDirections) {
    super("Lista słupków", Choice.MULTIPLE);
    mMidlet = midlet;
    mBollards = bollardsWithDirections;
    for (int i = 0; i < mBollards.size(); ++i) {
      BollardWithDirections b = (BollardWithDirections) mBollards.elementAt(i);
      append(b.getTag() + '\n' + getBollardSummary(b), null);
    }
    addCommand(ADD_BOLLARDS);
    addCommand(BACK_COMMAND);
    setCommandListener(new CommandListener() {
      public void commandAction(Command c, Displayable d) {
        if (c == ADD_BOLLARDS) {
          // notify the midlet about the chosen bollards
        } else if (c == BACK_COMMAND) {
          mMidlet.goBack();
        }
      }
    });
  }

  private String getBollardSummary(BollardWithDirections bollard) {
    Vector directions = bollard.getDirections();
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < directions.size(); ++i) {
      LineDirection d = (LineDirection) directions.elementAt(i);
      buf.append(d.getLine()).append(" => ").append(d.getDestination());
      if (i != directions.size() - 1) {
        buf.append('\n');
      }
    }
    return buf.toString();
  }
}
