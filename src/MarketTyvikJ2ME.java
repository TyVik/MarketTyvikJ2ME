import java.util.Stack;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class MarketTyvikJ2ME extends MIDlet {
  private boolean midletPaused = false;
  static Display mainDisplay;
  static BuyList_ buyList_;
  static Options options;
  static Server server;

  protected void destroyApp(boolean unconditional) {
    notifyDestroyed();
  }

  private void initialize() {
    Form onStart = new Form("Запускаемся...");
    Gauge gaugeStart = new Gauge("", false, 5, 1);
    onStart.append(gaugeStart);
    mainDisplay = Display.getDisplay(this);
    mainDisplay.setCurrent(onStart);
    gaugeStart.setValue(1);
    gaugeStart.setLabel("Загрузка настроек");
    options = new Options();
    gaugeStart.setValue(2);
    gaugeStart.setLabel("Подключение к серверу");
    server = new Server();
    gaugeStart.setValue(3);
    gaugeStart.setLabel("Инициализация форм");
    buyList_ = new BuyList_(this);
  }

  public void startMIDlet() {
    buyList_.show();
  }
  
  public void exitMIDlet() {
    destroyApp(true);
  }

  public void pauseApp() {
    midletPaused = true;
  }

  protected void startApp() throws MIDletStateChangeException {
    if (midletPaused) {
      resumeMIDlet();
    } else {
      initialize();
      startMIDlet();
    }
    midletPaused = false;
  }

  public void resumeMIDlet() {
  }

  // не нашёл стандартной функции 
  public static String[] split(String toSplit, char delim, boolean ignoreEmpty) {
    StringBuffer buffer = new StringBuffer();
    Stack stringStack = new Stack();
    for (int i = 0; i < toSplit.length(); i++) {
      if (toSplit.charAt(i) != delim) {
        buffer.append((char) toSplit.charAt(i));
      } else {
        if (buffer.toString().trim().length() == 0 && ignoreEmpty) {
        } else {
          stringStack.addElement(buffer.toString());
        }
        buffer = new StringBuffer();
      }
    }
    if (buffer.length() != 0) {
      stringStack.addElement(buffer.toString());
    }
    String[] split = new String[stringStack.size()];
    for (int i = 0; i < split.length; i++) {
      split[split.length - 1 - i] = (String) stringStack.pop();
    }
    stringStack = null;
    buffer = null;
    return split;
  }

}
