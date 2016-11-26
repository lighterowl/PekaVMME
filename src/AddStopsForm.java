
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Form;

public class AddStopsForm extends Form {

  private final MainMidlet mParent;

  public AddStopsForm(MainMidlet parent) {
    super("Dodaj przystanek");
    mParent = parent;
    append(new TextField("Nazwa", "", 50, TextField.ANY));
    ChoiceGroup choices = new ChoiceGroup("Szukaj wśród...",
            ChoiceGroup.EXCLUSIVE,
            new String[]{"przystanków", "ulic", "linii"}, null);
    append(choices);
  }
}
