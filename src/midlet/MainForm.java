package midlet;

import midlet.MainMidlet;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

public class MainForm extends Form {

  private static final Command EXIT_COMMAND = new Command("Wyjście", Command.EXIT, 1);
  private static final Command ADD_STOPS_COMMAND = new Command("Dodaj", Command.SCREEN, 1);
  private final MainMidlet mParent;

  public MainForm(MainMidlet parent) {
    super("Przystanki");
    mParent = parent;
    addCommand(EXIT_COMMAND);
    addCommand(ADD_STOPS_COMMAND);
    setCommandListener(new CommandListener() {
      public void commandAction(Command c, Displayable d) {
        if (c == EXIT_COMMAND) {
          mParent.shutdown();
        } else if (c == ADD_STOPS_COMMAND) {
          mParent.displayAddStopsForm();
        }
      }
    });
  }
}
