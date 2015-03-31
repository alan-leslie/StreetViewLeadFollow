import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
 
public class StreetViewServiceServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
 
        ServletContextHandler ctx = new ServletContextHandler();
        ctx.setContextPath("/");
        ctx.addServlet(StockServiceSocketServlet.class, "/street_view");
 
        server.setHandler(ctx);
 
        server.start();
        server.join();
    }

    public static class StockServiceSocketServlet extends WebSocketServlet {
 
        /**
		 * 
		 */
		private static final long serialVersionUID = -9047950350718227469L;

		@Override
        public void configure(WebSocketServletFactory factory) {
            factory.register(StreetViewServiceWebSocket.class);
        }
    }
}