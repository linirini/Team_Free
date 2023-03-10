package Server;

import Server.Models.*;
import javafx.application.Platform;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CreateRoomHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize Response Body
        OutputStream respBody = exchange.getResponseBody();
        InputStream requBody = exchange.getRequestBody();
        DAO dao = new DAO();

        try {
            // Write Response Body
            String method = exchange.getRequestMethod();
            if(method.equals("GET")) {
            	System.out.println("get userlist request");
            	//유저 정보를 가져온다.
                ArrayList<UsersDTO> allUsers =  dao.getAllUsers();
                
                //유저 정보의 일부만 넘겨준다.
                JSONArray list = new JSONArray();
                for(UsersDTO user : allUsers) {
                    JSONObject obj = new JSONObject();
                    obj.put("std_id",user.getStd_id());
                    obj.put("name", user.getName());
                    list.add(obj);
                }
                //System.out.println(list.toString());
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
                System.out.println("success to submit usersInfo");
            }
            // 방 정보를 저장한다.
            else if(method.equals("POST")) {
            	// 요청 결과 가져오기
                System.out.println("create Room request");
                BufferedReader br = new BufferedReader(new InputStreamReader(requBody, "utf-8"));
                //System.out.println(request);
                
                //json 변경
                JSONParser parser = new JSONParser();
                JSONArray list = (JSONArray)parser.parse(br);
                JSONObject Room = (JSONObject) list.get(0);
                
                //형태 변환
                String title = Room.get("title").toString();
                int limit_person = Integer.parseInt(Room.get("limit_person").toString());
                int leader_id = Integer.parseInt(Room.get("leader_id").toString());
                
                //System.out.println("title:"+Room.get("title")+" limit_person:"+Room.get("limit_person")+" leader_id:"+Room.get("leader_id"));
                ChatRoomInfoDTO crateRoomDTO = new ChatRoomInfoDTO(0,title,limit_person,0,leader_id);
                int room_id = dao.addRoom(crateRoomDTO);
                System.out.println("success to create Room, Room_id: "+room_id);

                JSONArray std_ids = (JSONArray) list.get(1);
                System.out.println(std_ids.toJSONString());
                for(Object obj : std_ids) {
                    int std_id = Integer.parseInt((String) obj);
                    System.out.println("Add std_id: " +obj +" to "+room_id);
                    dao.addMember(std_id, room_id);
                    dao.increCur_person(room_id);
                }
                System.out.println("success to add friend user");
                exchange.sendResponseHeaders(201, 0);
            }
            // Close Stream
            // 반드시, Response Header를 보낸 후에 닫아야함
            respBody.close();
            requBody.close();

        } catch ( IOException | ParseException e ) {
        	exchange.sendResponseHeaders(400, 0);
            e.printStackTrace();

            if( respBody != null ) {
                respBody.close();
            }
        } finally {
            exchange.close();
        }
    }
    public static String getRequestBody(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String line;
        while((line = br.readLine()) != null) {
        	sb.append(line.trim());
        }
        br.close();
        return sb.toString();
    }
}
