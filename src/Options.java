import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class Options  implements CommandListener {
  static RecordStore recOpt;

  private Form Options = new Form("Настройки");
  private Command okCommand = new Command("OK", Command.OK, 0);
  private Command cancelCommand = new Command("Отмена", Command.CANCEL, 0);
  public TextField SMSText = new TextField("Текст СМС", "", 25, TextField.ANY);
  public TextField SMSNumber = new TextField("Номер СМС +7", "", 10, TextField.NUMERIC);
  public TextField ServerPath = new TextField("Адрес сервера", "", 50, TextField.ANY);
  
  public Options() {
    super();
    Options.addCommand(cancelCommand);
    Options.addCommand(okCommand);
    Options.append(SMSNumber);
    Options.append(SMSText);
    Options.append(ServerPath);
    Options.setCommandListener(this);
    open();
    loadValue();
  }

  public void show(){
    MarketTyvikJ2ME.mainDisplay.setCurrent(Options);
  }

  public void open() {
    try {
      recOpt = RecordStore.openRecordStore("Options", true);
    } catch (RecordStoreFullException e) {
      e.printStackTrace();
    } catch (RecordStoreNotFoundException e) {
      e.printStackTrace();
    } catch (RecordStoreException e) {
      e.printStackTrace();
    }
  }
  
  public void close() {
    try {
      recOpt.closeRecordStore();
    } catch (RecordStoreNotOpenException e) {
      e.printStackTrace();
    } catch (RecordStoreException e) {
      e.printStackTrace();
    }
    recOpt = null;
  }

  public void commandAction(Command command, Displayable displayable) {
    if (command == okCommand) {
      saveValue();
    }
//    textField.setString("");
    MarketTyvikJ2ME.buyList_.show();
  }

  public void loadValue() {
    try {
      if (recOpt.getNumRecords() != 0){
        String rec = new String(recOpt.getRecord(1));
        String[] values = MarketTyvikJ2ME.split(rec, '&', false);
        SMSNumber.setString(values[0]);
        SMSText.setString(values[1]);
        ServerPath.setString(values[2]);
      }
    } catch (RecordStoreNotOpenException e) {
      e.printStackTrace();
    } catch (InvalidRecordIDException e) {
      e.printStackTrace();
    } catch (RecordStoreException e) {
      e.printStackTrace();
    }
  }

  public void saveValue() {
    // если записать null, то при чтении произойдёт Exception
    String values = new String("");
    values = ((SMSNumber.getString() == null)?"":SMSNumber.getString()) + "&" +
      ((SMSText.getString() == null)?"":SMSText.getString()) + "&" +
      ((ServerPath.getString() == null)?"":ServerPath.getString()) + "&";
    try {
      if (recOpt.getNumRecords() != 0){
        recOpt.setRecord(1, values.getBytes(), 0, values.length());
      } else {
        recOpt.addRecord(values.getBytes(), 0, values.length());
      }
    } catch (RecordStoreNotOpenException e) {
      e.printStackTrace();
    } catch (InvalidRecordIDException e) {
      e.printStackTrace();
    } catch (RecordStoreFullException e) {
      e.printStackTrace();
    } catch (RecordStoreException e) {
      e.printStackTrace();
    }
  }

}
