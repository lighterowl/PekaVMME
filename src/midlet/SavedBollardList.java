package midlet;

import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import pekaapi.Bollard;

public class SavedBollardList extends List {

  private static final Command EXIT_COMMAND = new Command("Wyjście", Command.EXIT, 1);
  private static final Command ADD_STOPS_COMMAND = new Command("Dodaj", Command.SCREEN, 1);
  private static final Command SHOW_TIMES_COMMAND = new Command("Odjazdy", Command.ITEM, 1);
  private final MainMidlet mParent;
  private final Vector mSavedBollards;

  public SavedBollardList(MainMidlet parent, Vector savedBollards) {
    super("Przystanki", Choice.EXCLUSIVE);
    mParent = parent;
    mSavedBollards = savedBollards;
    addCommand(EXIT_COMMAND);
    addCommand(ADD_STOPS_COMMAND);
    addCommand(SHOW_TIMES_COMMAND);
    setCommandListener(new CommandListener() {
      public void commandAction(Command c, Displayable d) {
        if (c == EXIT_COMMAND) {
          mParent.shutdown();
        } else if (c == ADD_STOPS_COMMAND) {
          mParent.displayAddStopsForm();
        } else if (c == SHOW_TIMES_COMMAND) {
          // get the currently selected bollard and retrieve current times
        }
      }
    });
  }

  public void updateBollards() {
    deleteAll();
    for (int i = 0; i < mSavedBollards.size(); ++i) {
      Bollard b = (Bollard) mSavedBollards.elementAt(i);
      StringBuffer buf = new StringBuffer();
      buf.append(b.getTag()).append(" // ").append(b.getName());
      append(buf.toString(), null);
    }
  }
}
