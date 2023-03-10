package Server;

import Server.Models.ChatRoomMemberDTO;
import Server.Models.DAO;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class UserListHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize Response Body
        OutputStream respBody = exchange.getResponseBody();

        try {
            // Write Response Body
            String method = exchange.getRequestMethod();
            DAO dao = new DAO();
            if(method.equals("GET")) {
                // room_id 받아오기
                String[] querys = exchange.getRequestURI().getQuery().split("=");
                int room_id = Integer.parseInt(querys[1]);
                System.out.println("room_id = "+ room_id);

                // 방과 관련된 유저의 정보를 가져온다.
                JSONArray list = new JSONArray();
                ArrayList<ChatRoomMemberDTO> members = dao.getRoomMembers(room_id);
                for(ChatRoomMemberDTO member : members) {
                	list.add(member.toJSONObject());
                	System.out.println(member.toJSONString());
                }
                

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
            } 
            // Close Stream
            // 반드시, Response Header를 보낸 후에 닫아야함
            respBody.close();

        } catch ( IOException e ) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, 0);
            if( respBody != null ) {
                respBody.close();
            }
        } finally {
            exchange.close();
        }
    }
}
