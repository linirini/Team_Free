import org.json.simple.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class test {
    public static void main(String[] args) {
        try {
            URL url = new URL("http://localhost:3000/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Authorization", "idExample:passwordExample");

            // 요청 방식 구하기
            System.out.println("getRequestMethod():" + http.getRequestMethod());
            // 응답 코드 구하기
            System.out.println("getResponseCode():"    + http.getResponseCode());
            // 응답 메시지 구하기
            System.out.println("getResponseMessage():" + http.getResponseMessage());

            if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String resBody = getResponseBody(http.getInputStream());
                System.out.println(resBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getResponseBody(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }
}
