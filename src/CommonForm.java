import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;


public class CommonForm implements CommandListener {
  protected Form form;
  
  public CommonForm(String caption) {
    super();
    form = new Form(caption);
    form.setCommandListener(this);
  }

  public void show() {
    MarketTyvikJ2ME.mainDisplay.setCurrent(form);
  }

  public void commandAction(Command arg0, Displayable arg1) {
  }
}
