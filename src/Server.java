import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;


public class Server {
  
  public String sendRequest(String address, String postData) throws IOException {
    HttpConnection connection = null;
    String result = "";
    try {
      String params = new String(address.getBytes("utf-8"));
      connection = (HttpConnection) Connector.open(MarketTyvikJ2ME.options.ServerPath.getString() + "/" + params);
      connection.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
	    if (postData == null) {
        connection.setRequestMethod(HttpConnection.GET);
	    } else {
        connection.setRequestMethod(HttpConnection.POST);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", Integer.toString(postData.length()));
        OutputStream outStream = null;
		    try {
		      outStream = connection.openOutputStream();
          outStream.write(postData.getBytes("utf-8"));
          outStream.flush();
		    } finally {
          if (outStream != null) {
            outStream.close();
          }
		    }
	    }
      int rc = connection.getResponseCode();
      if (rc == HttpConnection.HTTP_OK) {
        InputStream inStream = null;
	      try {
	        inStream = connection.openInputStream();
          InputStreamReader inStreamReader = new InputStreamReader(inStream, "utf-8");
          int ch;
          while ((ch = inStreamReader.read()) != -1) {
            result += (char) ch;
          }
		      return result;
		    } finally {
		      inStream.close();
		    }
	    } else {
        throw new IOException("HTTP response code: " + rc);
      }
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
  }

  public void addToServer(String element) throws IOException{
    sendRequest("api/add/" + element, null);
  }

  public void delFromServer(int element) throws IOException{
    sendRequest("api/delete/" + Integer.toString(element), null);
  }
  public String[] getFromServer() throws IOException{
    return MarketTyvikJ2ME.split(sendRequest("api/list", null), '$', false);
  }
}
