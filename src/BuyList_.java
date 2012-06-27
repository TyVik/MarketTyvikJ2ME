import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;


public class BuyList_ extends CommonForm implements ItemCommandListener {
  private MarketTyvikJ2ME midlet;
  private Hashtable items = null;
  private ChoiceGroup choiceGroup = new ChoiceGroup("Общий список", Choice.MULTIPLE);
  private Command delCommand = new Command("Удалить", Command.ITEM, 0);
  private Command addCommand = new Command("Добавить", Command.ITEM, 0);
  private Command refreshCommand = new Command("Обновить", Command.ITEM, 0);
  private Command exitCommand = new Command("Выход", Command.EXIT, 0);
  private Command aboutCommand = new Command("О программе", Command.HELP, 0);
  private Command smsCommand = new Command("Отправить СМС", Command.ITEM, 0);
  private Command optionsCommand = new Command("Настройки", Command.ITEM, 0);
  
  public BuyList_(MarketTyvikJ2ME m) {
    super("Список покупок");
    midlet = m;
    choiceGroup.addCommand(addCommand);
    choiceGroup.addCommand(delCommand);
    choiceGroup.addCommand(refreshCommand);
    choiceGroup.setItemCommandListener(this);
    choiceGroup.setSelectedFlags(new boolean[] {  });
    form.append(choiceGroup);
    form.addCommand(exitCommand);
    form.addCommand(aboutCommand);
    form.addCommand(optionsCommand);
    form.addCommand(smsCommand);
    refreshElements();
  }
  
  public void commandAction(Command command, Displayable displayable) {
    if (command == aboutCommand) {
      About about = new About();
      about.show();
    } else if (command == exitCommand) {
      midlet.exitMIDlet();
    } else if (command == smsCommand) {
      try {
        sendSMS(MarketTyvikJ2ME.options.SMSNumber.getString(), MarketTyvikJ2ME.options.SMSText.getString());
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (command == optionsCommand) {
      MarketTyvikJ2ME.options.show();
    }
  }

  public void commandAction(Command command, Item item) {
    if (item == choiceGroup) {
      if (command == addCommand) {
        AddItem addItem = new AddItem();
        addItem.show();
      } else if (command == delCommand) {
        delElements();
      } else if (command == refreshCommand) {
        refreshElements();
      }
    }
  }
  
  private void refreshElements() {
    items = fillList();
    fillChoice();
  }

  public Hashtable fillList() {
    Hashtable result = new Hashtable();
    try {
      String[] pairs = MarketTyvikJ2ME.server.getFromServer();
      for (int i = 0; i < pairs.length; i++) {
        String[] pair = MarketTyvikJ2ME.split(pairs[i], '^', false);
        result.put(new Integer(Integer.parseInt(pair[0])), pair[1]);
      }
    } catch (Exception e) {
      showAlert("Не могу получить список с сервера\n" + e.getMessage() + "\n" + e.toString());
    }
    return result;
  }

  public void fillChoice() {
    choiceGroup.deleteAll();
    Enumeration enu = items.keys();
    while (enu.hasMoreElements()) {
      choiceGroup.append((String)items.get(enu.nextElement()), null);
    }
  }
  
  public void addElement(String element) {
    try {
      MarketTyvikJ2ME.server.addToServer(element);
    } catch (IOException e) {
      showAlert("Ошибка при добавлении на сервер\n" + e.getMessage());
    }
    refreshElements();
  }

  public void delElements() {
    try {
      int i = 0;
      Integer key;
      String value;
      while (i < choiceGroup.size()) {
        if (choiceGroup.isSelected(i)) {
          String itemString = choiceGroup.getString(i);
          Enumeration enu = items.keys();
          while (enu.hasMoreElements()) {
            key = (Integer)enu.nextElement();
            value = (String)items.get(key);
            if (value == itemString) {
              MarketTyvikJ2ME.server.delFromServer(key.intValue());
            }
          }
        }
        i++;
      }
    } catch (IOException e) {
      showAlert("Ошибка в удалении с сервера\n" + e.getMessage());
    }
    refreshElements();
  }
  
  public void showAlert(String text) {
    Alert_ alert = new Alert_(midlet, this, text);
    alert.show();
  }

  public void sendSMS(String number, String text) throws IOException {
    String address = "sms://+7"+number;
    MessageConnection smsconn = null;
    try {
      smsconn = (MessageConnection) Connector.open(address);
      TextMessage txtmessage = (TextMessage) smsconn.newMessage(MessageConnection.TEXT_MESSAGE);
      txtmessage.setAddress(address);
      txtmessage.setPayloadText(text);
      smsconn.send(txtmessage);
    } finally {
      smsconn.close();
    }
  }

}