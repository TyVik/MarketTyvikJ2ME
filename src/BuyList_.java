import java.io.IOException;

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
  private ChoiceGroup choiceGroup = new ChoiceGroup("Общий список", Choice.MULTIPLE);;
  private Command delCommand = new Command("Удалить", Command.ITEM, 0);
  private Command addCommand = new Command("Добавить", Command.ITEM, 0);
  private Command exitCommand = new Command("Выход", Command.EXIT, 0);
  private Command aboutCommand = new Command("О программе", Command.HELP, 0);
  private Command smsCommand = new Command("Отправить СМС", Command.ITEM, 0);
  private Command optionsCommand = new Command("Настройки", Command.ITEM, 0);
  
  public BuyList_(MarketTyvikJ2ME m) {
    super("Список покупок");
    midlet = m;
    choiceGroup.addCommand(addCommand);
    choiceGroup.addCommand(delCommand);
    choiceGroup.setItemCommandListener(this);
    choiceGroup.setSelectedFlags(new boolean[] {  });
    form.append(choiceGroup);
    form.addCommand(aboutCommand);
    form.addCommand(exitCommand);
    form.addCommand(smsCommand);
    form.addCommand(optionsCommand);
    fillList();
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
      }
    }
  }

  public void fillList() {
    try {
      String[] strs = MarketTyvikJ2ME.server.getFromServer();
      for (int i = 0; i < strs.length; i++) {
        choiceGroup.append(strs[i], null);
      }
    } catch (Exception e) {
      showAlert("Не могу получить список с сервера\n" + e.getMessage());
    }
  }
  
  public void addElement(String element) {
    choiceGroup.append(element, null);
    try {
      MarketTyvikJ2ME.server.addToServer(element);
      choiceGroup.append(element, null);
    } catch (IOException e) {
      showAlert("Ошибка при добавлении на сервер\n" + e.getMessage());
   }
  }

  public void delElements() {
    String item = "";
    int i = 0;
    while (i < choiceGroup.size()) {
      if (choiceGroup.isSelected(i)) {
        item = item + "itemsSelected%5B%5D=" + choiceGroup.getString(i) + "&";
        choiceGroup.delete(i);
      } else {
        i++;
      }
    }
    try {
      MarketTyvikJ2ME.server.sendRequest(item);
    } catch (IOException e) {
      showAlert("Ошибка в удалении с сервера\n" + e.getMessage());
   }
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