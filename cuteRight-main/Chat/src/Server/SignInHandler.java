package Server;

import Server.Models.DAO;
import Server.Models.UsersDTO;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Set;

public class SignInHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// Initialize Response Body
		OutputStream respBody = exchange.getResponseBody();

		try {
			// Write Response Body
			String method = exchange.getRequestMethod();
			System.out.println(method);
			if (method.equals("POST")) {
				System.out.println("get SignIn request");
				InputStream is = exchange.getRequestBody();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(br);

				// user 생성
				UsersDTO user = new UsersDTO();
				user.setStd_id((int) (long) obj.get("std_id"));
				user.setName((String) obj.get("name"));
				user.setD_job((String) obj.get("d_job"));
				user.setPwd((String) obj.get("pwd"));
				user.setState((int) (long) obj.get("state"));

				DAO dao = new DAO();

				System.out.println(user.toJSONString());

				boolean flag = dao.checkDuplicate(user.getStd_id());

				//flag를 통해 아이디 중복체크 통과했을 때만 회원 정보 db에 보냄
				if (flag) {
					boolean success = dao.addSignUp(user);
					if (success) {
						// 응답코드 설정
						System.out.println("회원가입 완료");
						exchange.sendResponseHeaders(201, 0);
					} else {
						System.out.println("회원가입 실패");
						exchange.sendResponseHeaders(400, 0);
					}
				} else {
					exchange.sendResponseHeaders(409, 0);
					System.out.println("중복된 아이디, 회원가입 실패");
				}

				/* 수정 전 코드 버전
				 * boolean success = DAO.addSignUp(user);; if(success) { // 응답코드 설정
				 * System.out.println("회원가입 완료"); exchange.sendResponseHeaders(201,0); } else {
				 * System.out.println("회원가입 실패"); exchange.sendResponseHeaders(400, 0); }
				 */
			}
			// Close Stream
			// 반드시, Response Header를 보낸 후에 닫아야함
			respBody.close();

		} catch (IOException | ParseException e) {
			e.printStackTrace();

			if (respBody != null) {
				respBody.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			exchange.close();
		}
	}
}
