import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;


public class AddItem extends CommonForm{
  private TextField textField = new TextField("Новая запись", null, 32, TextField.ANY);
  private Command okCommand = new Command("OK", Command.OK, 0);
  private Command cancelCommand = new Command("Отмена", Command.CANCEL, 0);

  public AddItem() {
    super("Добавление записи");
    form.append(textField);
    form.addCommand(okCommand);
    form.addCommand(cancelCommand);
  }

  public void commandAction(Command command, Displayable displayable) {
    if (command == okCommand) {
      MarketTyvikJ2ME.buyList_.addElement(textField.getString());
    }
    textField.setString("");
    MarketTyvikJ2ME.buyList_.show();
  }

}
