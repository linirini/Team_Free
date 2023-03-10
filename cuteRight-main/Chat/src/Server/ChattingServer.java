package Server;

import Server.Models.ChatMessageDTO;
import Server.Models.DAO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChattingServer {
    private static final ChattingServer singleton = new ChattingServer();
    public static ChattingServer getInstance() {
        return singleton;
    }
    ExecutorService executorService;
    ServerSocket serverSocket;
    private HashMap<Integer, Vector<Client>> connections = new HashMap<Integer, Vector<Client>>();
//    List<Client> connections = new Vector<Client>();

    void start() {
        executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(serverSocket.getInetAddress(), 5001));
        } catch (Exception e) {
            if(!serverSocket.isClosed()) { stop(); }
            System.out.println("채팅 서버가 연결이 안됩니다.");
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Start Chatting Server.Server");
                while(true) {
                    try {
                        Socket socket = serverSocket.accept();
                        String message = "[연결 수락:" + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
                        System.out.println(message);

                        // 소켓에서 room_id를 받아서 클라이언트 초기화
                        byte[] toRead = new byte[2];
                        InputStream is = socket.getInputStream();
                        int readByCount = is.read(toRead);
                        if(readByCount == -1) throw new IOException();
                        int room_id = MessagePacker.byteArrayToInt(toRead, 2);

                        Client client = new Client(socket, room_id);
                        connections.get(room_id).add(client);
                        System.out.println("[연결 개수" + connections.size() + "]");
                    } catch (Exception e) {
                        if(!serverSocket.isClosed()) {stop();}
                        break;
                    }
                }
            }
        };
        executorService.submit(runnable);
    }

    void stop() {
        try {
            connections.forEach((key,value) -> {
                Iterator<Client> iterator = value.iterator();
                while(iterator.hasNext()) {
                    Client client = iterator.next();
                    try {
                        client.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    iterator.remove();
                }
            });
            if(serverSocket!=null && !serverSocket.isClosed()) { serverSocket.close(); }
            if(executorService!=null && !executorService.isShutdown()) { executorService.shutdown(); }
            System.out.println("서버 멈춤");
        } catch (Exception e) { }
    }

    public class Client {
        Socket socket;
        private final int room_id;

        Client(Socket socket, int room_id) {
            this.socket = socket;
            this.room_id = room_id;
            receive();
        }

        void receive() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            InputStream is = socket.getInputStream();
                            MessagePacker packet = MessagePacker.unpack(is);
                            String message = "[요청 처리: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
                            System.out.println(message);

                            // ChatMessageDTO를 DB에 저장
                            DAO dao = new DAO();
                            ChatMessageDTO chat = new ChatMessageDTO(packet.getStdId(), packet.getRoomId(), packet.getName(), packet.getMessage());
                            dao.addMessage(chat);

                            // 모든 클라이언트에 send
                            // 수정해야할 사항: 동일한 room_id를 가진 client에 넣어야 됨.
                            for(Client client : connections.get(room_id)) {
                                client.send(packet.getPacket());
                            }
                        }
                    } catch (Exception e) {
                        try {
                            e.printStackTrace();
                            connections.remove(Client.this);
                            System.out.println("클라이언트 통신 안됨");
                            socket.close();
                        } catch (IOException e2) {}
                    }
                }
            };
            executorService.submit(runnable);
        }

        void send(byte[] byteArr) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(byteArr);
                outputStream.flush();
            } catch (Exception e) {
                try {
                    System.out.println("클라이언트 통신 안됨");
                    connections.remove(Client.this);
                    socket.close();
                } catch (IOException e2) {}
            }
        }
    }

    public void addRoom(int room_id) {
        connections.put(room_id, new Vector<Client>());
    }
    public boolean hasConnection(int room_id) {
        if(connections.get(room_id) == null) {
            return false;
        }
        return true;
    }
}