
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Form;

public class AddStopsForm extends Form {

  private final MainMidlet mParent;
  private final TextField mPatternInput;
  private static final Command BACK_COMMAND = new Command("Powrót", Command.BACK, 1);
  private static final Command SEARCH_COMMAND = new Command("Szukaj", Command.SCREEN, 1);

  public AddStopsForm(MainMidlet parent) {
    super("Dodaj przystanek");
    mParent = parent;
    mPatternInput = new TextField("Nazwa", "", 50, TextField.ANY);
    append(mPatternInput);
    ChoiceGroup choices = new ChoiceGroup("Szukaj wśród...",
            ChoiceGroup.EXCLUSIVE,
            new String[]{"przystanków", "ulic", "linii"}, null);
    append(choices);
    addCommand(BACK_COMMAND);
    addCommand(SEARCH_COMMAND);
    setCommandListener(new CommandListener() {
      public void commandAction(Command c, Displayable d) {
        if (c == BACK_COMMAND) {
          mParent.goBack();
        } else if (c == SEARCH_COMMAND) {
          if (mPatternInput.getString().length() == 0) {
            mParent.displayAlert(new Alert("Błąd", "Wpisana nazwa jest pusta", null, AlertType.ERROR));
            return;
          }
          System.out.println("foo");
        }
      }
    });
  }
}
