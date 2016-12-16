package midlet;

import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import pekaapi.Bollard;
import pekaapi.BollardWithDirections;
import pekaapi.LineDirection;

public class BollardsList extends List {

  private final MainMidlet mMidlet;
  private final Vector mBollards;
  private static final Command ADD_BOLLARDS = new Command("Dodaj", Command.ITEM, 1);
  private static final Command BACK_COMMAND = new Command("Powrót", Command.BACK, 1);

  public BollardsList(MainMidlet midlet, Vector bollards) {
    super("Lista słupków", Choice.MULTIPLE);
    mMidlet = midlet;
    mBollards = bollards;
    for (int i = 0; i < mBollards.size(); ++i) {
      Bollard b = (Bollard) mBollards.elementAt(i);
      append(b.getTag() + getBollardSummary(b), null);
    }
    addCommand(ADD_BOLLARDS);
    addCommand(BACK_COMMAND);
    setCommandListener(new CommandListener() {
      public void commandAction(Command c, Displayable d) {
        if (c == ADD_BOLLARDS) {
          boolean[] selectedFlags = new boolean[size()];
          int numSelected = getSelectedFlags(selectedFlags);
          Vector newBollards = new Vector(numSelected);
          for (int i = 0; i < selectedFlags.length; ++i) {
            if (selectedFlags[i]) {
              newBollards.addElement(mBollards.elementAt(i));
            }
          }
          mMidlet.addToSavedBollards(newBollards);
        } else if (c == BACK_COMMAND) {
          mMidlet.goBack();
        }
      }
    });
  }

  private String getBollardSummary(Bollard b) {
    if (b instanceof BollardWithDirections) {
      BollardWithDirections bollard = (BollardWithDirections) b;
      Vector directions = bollard.getDirections();
      StringBuffer buf = new StringBuffer();
      buf.append('\n');
      for (int i = 0; i < directions.size(); ++i) {
        LineDirection d = (LineDirection) directions.elementAt(i);
        buf.append(d.getLine()).append(" => ").append(d.getDestination());
        if (i != directions.size() - 1) {
          buf.append('\n');
        }
      }
      return buf.toString();
    } else {
      StringBuffer buf = new StringBuffer();
      buf.append('\n').append(b.getName());
      return buf.toString();
    }
  }
}
