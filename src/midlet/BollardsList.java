package midlet;

import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import pekaapi.Bollard;

public class BollardsList extends List {

  private final MainMidlet mMidlet;
  private final Vector mBollards;
  private static final Command ADD_BOLLARDS = new Command("Dodaj", Command.ITEM, 1);
  private static final Command BOLLARD_INFO = new Command("Szczegóły", Command.ITEM, 2);
  private static final Command BACK_COMMAND = new Command("Powrót", Command.BACK, 1);

  public BollardsList(MainMidlet midlet, Vector bollards) {
    super("Lista słupków", Choice.MULTIPLE);
    mMidlet = midlet;
    mBollards = bollards;
    for (int i = 0; i < mBollards.size(); ++i) {
      append(((Bollard) mBollards.elementAt(i)).getTag(), null);
    }
    addCommand(ADD_BOLLARDS);
    addCommand(BOLLARD_INFO);
    addCommand(BACK_COMMAND);
    setCommandListener(new CommandListener() {
      public void commandAction(Command c, Displayable d) {
        if (c == ADD_BOLLARDS) {
          // notify the midlet about the chosen bollards
        } else if (c == BOLLARD_INFO) {
          // display a form with bollard details
        } else if (c == BACK_COMMAND) {
          mMidlet.goBack();
        }
      }
    });
  }
}
