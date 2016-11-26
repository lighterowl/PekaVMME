
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Form;

public class AddStopsForm extends Form {

  private final MainMidlet mParent;
  private static final Command BACK_COMMAND = new Command("Powrót", Command.BACK, 1);
  private static final Command SEARCH_COMMAND = new Command("Szukaj", Command.SCREEN, 1);

  public AddStopsForm(MainMidlet parent) {
    super("Dodaj przystanek");
    mParent = parent;
    append(new TextField("Nazwa", "", 50, TextField.ANY));
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
          System.out.println("foo");
        }
      }
    });
  }
}
