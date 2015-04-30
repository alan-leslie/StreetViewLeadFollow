import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
 



import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
 
@WebSocket
public class StreetViewServiceWebSocket {
	
	private MapSVModel theModel = new MapSVModel();
 
    private Session session;
	public static final Set<StreetViewServiceWebSocket> users = new CopyOnWriteArraySet<StreetViewServiceWebSocket>();
 
    // called when the socket connection with the browser is established
    @OnWebSocketConnect
    public void handleConnect(Session session) {
        System.out.println("StreetViewWebSocketServlet Connect number of users is =" + Integer.toString(users.size()));
        this.session = session;
		users.add(this);
		// send model
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
		for (StreetViewServiceWebSocket user : users) {
	            user.send(message);
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