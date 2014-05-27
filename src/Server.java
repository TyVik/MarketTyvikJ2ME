import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.SocketConnection;

import org.json.me.*;


public class Server implements Runnable {
  private String address = null;
  private String type = null;
	
  public void run() {
	  SocketConnection connection = null;
	  try {
      try {
        connection = (SocketConnection)Connector.open("socket://" + MarketTyvikJ2ME.options.ServerPath.getString() + ":80");
        String request = type + " " + address + " HTTP/1.1\nHost: " + MarketTyvikJ2ME.options.ServerPath.getString() + "\n\n";
        OutputStream outStream = null;
        try {
          outStream = connection.openOutputStream();
          outStream.write(request.getBytes("utf-8"));
          outStream.flush();
        } finally {
        	if (outStream != null) {
        	  outStream.close();
        	}
        }
        InputStream inStream = null;
        String response = new String();
        try {
          inStream = connection.openInputStream();
          InputStreamReader inStreamReader = new InputStreamReader(inStream, "utf-8");
          int ch;
          while ((ch = inStreamReader.read()) != -1) {
            response += (char) ch;
          }
          System.out.println(response);
        } finally {
          inStream.close();
        }
      } finally {
      	if (connection != null) {
      		connection.close();
      	}
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public JSONObject sendRequest(String address, String protocol, String postData) throws IOException, JSONException {
    HttpConnection connection = null;
    try {
      connection = (HttpConnection) Connector.open("http://" + MarketTyvikJ2ME.options.ServerPath.getString() + "/" + address);
      connection.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
      connection.setRequestMethod(protocol);
      if (postData != null) {
        connection.setRequestProperty("Content-Type", "application/json");
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
      if ((rc == HttpConnection.HTTP_OK) || (rc == HttpConnection.HTTP_CREATED)) {
        InputStream inStream = null;
        String response = "";
        try {
          inStream = connection.openInputStream();
          InputStreamReader inStreamReader = new InputStreamReader(inStream, "utf-8");
          int ch;
          while ((ch = inStreamReader.read()) != -1) {
            response += (char) ch;
          }
          try {
            JSONObject result = new JSONObject(response);
            return result;
          } catch (JSONException ex) {
            ex.printStackTrace();
          }
        } finally {
          inStream.close();
        }
      } else {
        throw new IOException("HTTP response code: " + rc);
      }
      return null;
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
  }

  public int addToServer(String element) throws IOException, JSONException {
    JSONObject response = sendRequest("/api/v1/items/", "POST", "{\"name\":\"" + element + "\"}");
    return response.getInt("id");
  }

  public void delFromServer(int element) throws IOException, Exception {
    address = "/api/v1/items/" + Integer.toString(element) + "/";
    type = "DELETE";
    Thread t = new Thread(this);
    t.start();
  }

  public Hashtable getFromServer() throws IOException, JSONException {
    JSONObject response = sendRequest("/api/v1/items/", "GET", null);
    Hashtable result = new Hashtable();
    JSONArray objects = response.getJSONArray("objects");
    for (int i = 0; i < response.getJSONObject("meta").getInt("total_count"); i++) {
      JSONObject obj = objects.getJSONObject(i);
      result.put(new Integer(obj.getInt("id")), obj.getString("name"));
    }
    return result;
  }

}
