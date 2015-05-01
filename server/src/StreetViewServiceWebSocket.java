import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
@WebSocket
public class StreetViewServiceWebSocket {
	
	private MapSVModel theModel = new MapSVModel();
 
    private Session session;
	public static final List<StreetViewServiceWebSocket> users = new ArrayList<StreetViewServiceWebSocket>();
 
    // called when the socket connection with the browser is established
    @OnWebSocketConnect
    public void handleConnect(Session session) {
        System.out.println("StreetViewWebSocketServlet Connect number of users is =" + Integer.toString(users.size()));
        this.session = session;
		users.add(this);
		StringBuilder theBuilder = new StringBuilder("model:");
		String stringifiedModel = theModel.asJSONObject().toString();
		theBuilder.append(stringifiedModel);
		
		if(users.size() > 1){
			this.send("is_follower:");
			this.send(theBuilder.toString());
		}
	}
 
    // called when the connection closed
    @OnWebSocketClose
    public void handleClose(int statusCode, String reason) {
        System.out.println("StreetViewWebSocketServlet Connection closed with statusCode=" 
            + statusCode + ", reason=" + reason);
		users.remove(this);
    }
 
    // called when a message received from the browser
    @OnWebSocketMessage
    public void handleMessage(String message) {
    	// if the message is a change of model update Model;
    	// broadcast new model;
        System.out.println("StreetViewWebSocketServlet Message:" + message);
		
        theModel.updateModel(message);
        
        StringBuilder theBuilder = new StringBuilder("model:");
		String stringifiedModel = theModel.asJSONObject().toString();
		theBuilder.append(stringifiedModel);

		for (int i = 1; i < users.size(); ++i) {
	            users.get(i).send(theBuilder.toString());
		}
    }
 
    // called in case of an error
    @OnWebSocketError
    public void handleError(Throwable error) {
        System.out.println("StreetViewWebSocketServlet Error");
    	error.printStackTrace();    
    }
 
    // sends message to browser
    private void send(String message) {
        try {
            if (session.isOpen()) {
                session.getRemote().sendString(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    // closes the socket
    private void stop() {
        try {
            session.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}