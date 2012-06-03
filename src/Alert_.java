import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

public class Alert_ implements CommandListener {
  private Alert alert = new Alert("Внимание!", null, null, AlertType.ALARM);
  private Command backCommand = new Command("Назад", Command.BACK, 0);
  private Command exitCommand = new Command("Выход", Command.EXIT, 0);
  private MarketTyvikJ2ME midlet;
  private CommonForm backForm;

  public Alert_(MarketTyvikJ2ME m, CommonForm form, String text) {
    super();
    midlet = m;
    backForm = form;
    alert.setString(text);
    alert.addCommand(backCommand);
    alert.addCommand(exitCommand);
    alert.setCommandListener(this);
    alert.setTimeout(Alert.FOREVER);
  }

  public void show() {
    MarketTyvikJ2ME.mainDisplay.setCurrent(alert);
  }

  public void commandAction(Command command, Displayable displayable) {
    if (command == backCommand) {
      backForm.show();
    } else if (command == exitCommand) {
      midlet.exitMIDlet();
    }
  }
}
