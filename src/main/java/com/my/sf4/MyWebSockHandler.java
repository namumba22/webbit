package com.my.sf4;

import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import javax.websocket.server.ServerEndpoint;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyWebSockHandler extends BaseWebSocketHandler {
    private static Queue<WebSocketConnection> queue = new ConcurrentLinkedQueue<WebSocketConnection>();
    private static Thread rateThread;

    static {
        rateThread = new Thread() {
            public void run() {
                DecimalFormat df = new DecimalFormat("#.####");
                while (true) {
                    if (queue != null) {
                        double d = 2 + Math.random();
                        send("USD Rate: " + df.format(d));
                    }
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        rateThread.start();
    }

    private static void send(String msg) {
        try {
   /* Send the new rate to all open WebSocket sessions */
            ArrayList<WebSocketConnection> closedSessions = new ArrayList();
            for (WebSocketConnection connection : queue) {
                connection.send("fahrenheit:" + 2 + Math.random());
            }
            System.out.println("Sending " + msg + " to " + queue.size() + " clients");
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public void onOpen(WebSocketConnection connection) {
        queue.add(connection);
    }

    public void onClose(WebSocketConnection connection) {
        queue.remove(connection);
    }

}
