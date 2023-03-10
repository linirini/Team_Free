package Server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    private final String DEFAULT_HOSTNAME = "0.0.0.0";
    private final int DEFAULT_PORT = 8080;
    private final int DEFAULT_BACKLOG = 0;
    private HttpServer httpServer = null;
    private ChattingServer chattingServer = null;

    // 생성자
    public Server() throws IOException {
        createServer(DEFAULT_HOSTNAME, DEFAULT_PORT);
    }
    public Server(int port) throws IOException {
        createServer(DEFAULT_HOSTNAME, port);
    }
    public Server(String host, int port) throws IOException {
        createServer(host, port);
    }

    // 서버 생성
    private void createServer(String host, int port) throws IOException {
        // HTTP Server.Server 생성
        this.httpServer = HttpServer.create(new InetSocketAddress(host, port), DEFAULT_BACKLOG);
        // 싱글톤 패턴
        this.chattingServer = ChattingServer.getInstance();
        // HTTP Server.Server Context 설정
        httpServer.createContext("/", new RootHandler());
        httpServer.createContext("/login", new LoginHandler());
        httpServer.createContext("/logout", new LogoutHandler());
        httpServer.createContext("/signin", new SignInHandler());
        httpServer.createContext("/main",new RootHandler());
        httpServer.createContext("/chatRoom", new ChatRoomHandler());
        httpServer.createContext("/chatMessage", new ChatMessageHandler());
        httpServer.createContext("/createRoom", new CreateRoomHandler());
        httpServer.createContext("/userList",new UserListHandler());
    }

    // 서버 실행
    public void start() {
        httpServer.start();
        chattingServer.start();
    }

    // 서버 중지
    public void stop(int delay) {
        httpServer.stop(delay);
        chattingServer.stop();
    }

    public static void main(String[] args) {
        Server httpServerManager = null;

        try {
            // 시작 로그
            System.out.println(
                    String.format(
                            "[%s][HTTP SERVER][START]",
                            new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(new Date())
                    )
            );

            // 서버 생성
            httpServerManager = new Server("localhost", 3000);
            httpServerManager.start();
            // Shutdown Hook
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    // 종료 로그
                    System.out.println(
                            String.format(
                                    "[%s][HTTP SERVER][STOP]",
                                    new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(new Date())
                            )
                    );
                }
            }));

            // Enter를 입력하면 종료
            System.out.print("Please press 'Enter' to stop the server.");
            System.in.read();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            // 종료
            // 0초 대기후  종료
            httpServerManager.stop(0);
        }
    }
}
