package Server;

import Server.Models.DAO;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Initialize Response Body
        OutputStream respBody = exchange.getResponseBody();

        try {
            // Write Response Body
            String method = exchange.getRequestMethod();
            Headers headers = exchange.getRequestHeaders();
            DAO dao = new DAO();
            if(method.equals("GET")) {
                String[] author = headers.getFirst("Authorization").split(":");
                int id = Integer.parseInt(author[0]);
                String password = author[1];

                int lg= dao.checkLogin(id, password);
                boolean checkstate2 =dao.checkState(id);

                if(lg == 1) {
//                	exchange.sendResponseHeaders(200, 0);
                    if(checkstate2) {
                        dao.setLogin(id);
                        exchange.sendResponseHeaders(200, 0);
                    } else {
                        exchange.sendResponseHeaders(409, 0);
                    }
                } else if (lg == 0 || lg == -1){
                    exchange.sendResponseHeaders(400, 0);
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
