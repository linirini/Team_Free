package Server;

import Server.Models.ChatRoomInfoDTO;
import Server.Models.DAO;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RootHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize Response Body
        OutputStream respBody = exchange.getResponseBody();

        try {
            // Write Response Body
            String method = exchange.getRequestMethod();
            DAO dao = new DAO();
            if(method.equals("GET")) {
                // std_id 받아오기
                String[] querys = exchange.getRequestURI().getQuery().split("=");
                int id = Integer.parseInt(querys[1]);
                System.out.println("id = "+ id);

                // 받은 std_id가 0보다 작으면 오픈채팅, 0보다 크면 내 채팅방 목록을 가져온다.
                JSONArray list = new JSONArray();
                if(id > 0) {
                    // 내 채팅방 목록 가져와서 json형식으로 변환
                    ArrayList<Integer> room_ids = dao.getMyRoomId(id);
                    for(Integer room_id : room_ids) {
                        ChatRoomInfoDTO room = dao.getMyRoomInfo(room_id);
                        System.out.println("room_id: " + room_id + " room: " + room.toJSONString());
                        list.add(room.toJSONObject());
                    }
                } else {
                    id = - id;
                    ArrayList<ChatRoomInfoDTO> rooms = dao.getAllRoom(id);
                    ArrayList<ChatRoomInfoDTO> roomToBeRemoved = dao.getOpenRoom(id);
                    for(ChatRoomInfoDTO room : roomToBeRemoved) {
                        rooms.removeIf(e->(e.getRoom_id()==room.getRoom_id()));
                    }
                    for (ChatRoomInfoDTO room : rooms) {
                        if(room.getCur_person() < room.getLimit_person()) {
                            list.add(room.toJSONObject());
                        }
                    }
                }
                System.out.println(list.toJSONString());

                // Encoding to UTF-8
                ByteBuffer bb = Charset.forName("UTF-8").encode(list.toJSONString());
                int contentLength = bb.limit();
                byte[] content = new byte[contentLength];
                bb.get(content, 0, contentLength);

                // Set Response Headers
                Headers headers = exchange.getResponseHeaders();
                headers.add("Content-Type", "application/json");
                headers.add("Content-Length", String.valueOf(contentLength));

                // Send Response Headers
                exchange.sendResponseHeaders(200, contentLength);

                respBody.write(content);
            } else if(method.equals("POST")) {
                List<String> author = exchange.getRequestHeaders().get("Authorization");
                System.out.println(author.get(0));

                InputStream is = exchange.getRequestBody();
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String line;
                while((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                System.out.println(sb.toString());

                String msg = "this is POST METHOD";
                // Encoding to UTF-8
                ByteBuffer bb = Charset.forName("UTF-8").encode(msg);
                int contentLength = bb.limit();
                byte[] content = new byte[contentLength];
                bb.get(content, 0, contentLength);

                // Set Response Headers
                Headers headers = exchange.getResponseHeaders();
                headers.add("Content-Type", "application/text");
                headers.add("Content-Length", String.valueOf(contentLength));

                // Send Response Headers
                exchange.sendResponseHeaders(200, contentLength);

                respBody.write(content);
            }
            // Close Stream
            // 반드시, Response Header를 보낸 후에 닫아야함
            respBody.close();

        } catch ( IOException e ) {
            e.printStackTrace();

            if( respBody != null ) {
                respBody.close();
            }
        } finally {
            exchange.close();
        }
    }
}
