package Client;

import java.io.IOException;
import java.net.*;

public class SocketConnection {
    public static boolean socketConnect = false;
    public static Socket socket = null;

    public static void setSocketConnect(boolean state) {
        socketConnect = state;
    }
    public static void setSocket(Socket s) {
        socket = s;
    }

    public static void connect() throws UnknownHostException, SocketException {
        if(!socketConnect) {
            // 자신의 IP주소 넣기
            final String SERVER_IP = InetAddress.getLocalHost().getHostAddress();
            final int SERVER_PORT = 5001;

            socket = new Socket();

            try {
                socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
                System.out.println("success connetion to server");
                SocketConnection.setSocketConnect(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connetion is Refused!!!!");
                if(!socket.isClosed()) { close(); }
            }
        }
    }
    public static void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                socketConnect = false;
                System.out.println("success disconneted to server");
            }
        } catch (IOException e) { }
    }
}
