import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.StringItem;

public class About extends CommonForm{
  private StringItem stringItem = new StringItem("Список покупок v 0.2", "Любимому Солнышку на День рождения :*");
  private Command back = new Command("Back",Command.BACK,1);
  
  public About() {
    super("О программе");
    form.append(stringItem);
    form.addCommand(back);
  }
  
  public void commandAction(Command command, Displayable displayable) {
    if (command == back) {
      MarketTyvikJ2ME.buyList_.show();
    }
  }

}
