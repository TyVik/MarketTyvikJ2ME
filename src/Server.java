import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;


public class Server {
  private String address;
  
  public Server(String addr) {
    super();
    address = addr;
  }

  public void sendRequest(String item) throws IOException{
    HttpConnection c = null;
    OutputStream os = null;
    int rc;
    try {
      c = (HttpConnection) Connector.open("http://"+address+"/index.php?r=share/index");
      c.setRequestMethod(HttpConnection.POST);
      c.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
      c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      c.setRequestProperty("Content-Length", Integer.toString(item.length()));
      os = c.openOutputStream();
      os.write(item.getBytes("utf-8"));
      os.flush();
      rc = c.getResponseCode();
      if (rc != HttpConnection.HTTP_OK) {
        throw new IOException("HTTP response code: " + rc);
      }
    } finally {
      if (os != null) {
        os.close();
      }
      if (c != null) {
        c.close();
      }
    }
  }

  public void addToServer(String element) throws IOException{
    sendRequest("Share[Text]=" + element + "\n");
  }

  public String[] getFromServer() throws IOException{
    HttpConnection c = null;
    InputStream is = null;
    InputStreamReader isr = null;
    String result = "";
    try {
      c = (HttpConnection) Connector.open("http://"+address+"/index.php?r=share/getList");
      is = c.openInputStream();
      isr = new InputStreamReader(is, "utf-8");
      int ch;
      while ((ch = isr.read()) != -1) {
        result += (char) ch;
      }
    } catch (IllegalArgumentException e) {
      throw new IOException("Не задан адрес сервера");
    } finally {
      if (is != null) {
        is.close();
      }
      if (c != null) {
        c.close();
      }
    }
    return MarketTyvikJ2ME.split(result, '&', false);
  }
}
