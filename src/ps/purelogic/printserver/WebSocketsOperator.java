/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ps.purelogic.printserver;

import ps.purelogic.printserver.printer.ThermalPrinterImpl;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 *
 * @author mkhoudary
 */
public class WebSocketsOperator extends WebSocketServer implements Observer {

    private static WebSocketsOperator instance;
    private static ExecutorService threadPool;

    private WebSocketsOperator(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Socket Connected");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Socket Disconnected");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (message.equals("TEST_PRINT")) {
            try {
                ThermalPrinterImpl.printEmptySample("نموذج فارغ", "mkhoudary", "GP-U80300 Series");
            } catch (Exception ex) {
                Logger.getLogger(WebSocketsOperator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    /**
     * Sends <var>text</var> to all currently connected WebSocket clients.
     *
     * @param text The String to send across the network.
     * @throws InterruptedException When socket related I/O errors occur.
     */
    private void sendToAll(String text) {
        Collection<WebSocket> con = connections();
        synchronized (con) {
            for (WebSocket c : con) {
                c.send(text);
            }
        }
    }

    public static void initializeOperator(int port) throws UnknownHostException {
        instance = new WebSocketsOperator(port);
        instance.start();
    }

    public static void finalizeOperator() throws IOException, InterruptedException {
        instance.stop();
    }

    public static WebSocketsOperator instance() {
        return instance;
    }

    @Override
    public void update(Observable observable, final Object arg) {
        try {
            threadPool.execute(
                    new Runnable() {
                public void run() {
                    sendToAll((String) arg);
                }
            });
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
