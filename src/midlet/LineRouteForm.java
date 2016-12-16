package midlet;

import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import pekaapi.LineRoute;

public class LineRouteForm extends Form {

  private final static Command BACK_COMMAND = new Command("Powrót", Command.BACK, 1);
  private final static Command CHOOSE_COMMAND = new Command("Wybierz", Command.ITEM, 1);

  public LineRouteForm(final MainMidlet parent, String line, final Vector lineRoutes) {
    super("Kierunki linii " + line);
    final ChoiceGroup directions = new ChoiceGroup("", Choice.EXCLUSIVE);
    for (int i = 0; i < lineRoutes.size(); ++i) {
      LineRoute r = (LineRoute) lineRoutes.elementAt(i);
      directions.append(r.getDirection().getDestination(), null);
    }
    append(directions);
    addCommand(BACK_COMMAND);
    addCommand(CHOOSE_COMMAND);
    setCommandListener(new CommandListener() {
      public void commandAction(Command c, Displayable d) {
        if (c == BACK_COMMAND) {
          parent.goBack();
        } else if (c == CHOOSE_COMMAND) {
          LineRoute selectedRoute = (LineRoute) lineRoutes.elementAt(directions.getSelectedIndex());
          parent.displayBollards(selectedRoute.getBollards());
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }
    });
  }
}
