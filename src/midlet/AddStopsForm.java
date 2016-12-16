package midlet;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Form;

public class AddStopsForm extends Form {

  private static final Command BACK_COMMAND = new Command("Powrót", Command.BACK, 1);
  private static final Command SEARCH_COMMAND = new Command("Szukaj", Command.SCREEN, 1);

  public AddStopsForm(final MainMidlet parent) {
    super("Dodaj przystanek");
    final TextField patternInput = new TextField("Nazwa", "", 50, TextField.ANY);
    append(patternInput);
    final ChoiceGroup choices = new ChoiceGroup("Szukaj wśród...",
            ChoiceGroup.EXCLUSIVE,
            new String[]{"przystanków", "ulic", "linii"}, null);
    append(choices);
    addCommand(BACK_COMMAND);
    addCommand(SEARCH_COMMAND);
    setCommandListener(new CommandListener() {
      public void commandAction(Command c, Displayable d) {
        if (c == BACK_COMMAND) {
          parent.goBack();
        } else if (c == SEARCH_COMMAND) {
          String pattern = patternInput.getString();
          if (pattern.length() == 0) {
            parent.displayAlert(new Alert("Błąd", "Wpisana nazwa jest pusta", null,
                    AlertType.ERROR));
            return;
          }
          switch (choices.getSelectedIndex()) {
            case 0:
              parent.displayStopPoints(pattern);
              break;
            case 1:
              parent.displayBollardsAtStreet(pattern);
              break;
            case 2:
              // displayStopPointsByLine
              break;
          }

        }
      }
    });
  }
}
