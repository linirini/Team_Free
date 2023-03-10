package Server;

import Server.Models.ChatMessageDTO;
import Server.Models.DAO;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ChatMessageHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize Response Body
        OutputStream respBody = exchange.getResponseBody();

        try {
            // Write Response Body
            String method = exchange.getRequestMethod();
            if(method.equals("GET")) {
                String room_id;
                DAO dao = new DAO();
                // 일단 room id가 쿼리로 들어왔을 때만 전제한다.
                // Url 쿼리에서 room_id를 받아온다.
                String query = exchange.getRequestURI().getQuery();
                String[] q  = query.split("=",2);
                if(q[0].equals("room_id")) {
                    room_id = q[1];
                    ArrayList<ChatMessageDTO> messages =  dao.getRoomMessage(Integer.parseInt(room_id));
                    JSONArray list = new JSONArray();
                    for(ChatMessageDTO msg : messages) {
                        JSONObject obj = new JSONObject();
                        obj.put("std_id",msg.getStd_id());
                        obj.put("message", msg.getMessage());
                        list.add(obj);
                    }

                    // Encoding to UTF-8
                    ByteBuffer bb = StandardCharsets.UTF_8.encode(list.toJSONString());
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
                    System.out.println("success to submit messages");
                }
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