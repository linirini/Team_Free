package Client.Controllers;

import Client.Status;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private TextField idInput;
    @FXML private PasswordField pwdInput;
    @FXML private Button loginBtn;
    @FXML private Button signUpBtn;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(idInput.getText().equals("")) {
            idInput.setPromptText("학번(숫자)");
        }
       loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String id = idInput.getText();
                String pw = pwdInput.getText();
                boolean isNumeric =  id.matches("[+-]?\\d*(\\.\\d+)?");

                System.out.println("아이디: " + id);
                System.out.println("패스워드: " + pw);
                
                if(id==""){ //id 입력했는지 확인
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("비밀번호를 입력하세요.");
                    alert.showAndWait();
                    return;
                }
                if(!isNumeric) { //id에 입력한 값이 숫자인지 확인
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("아이디에 숫자를 입력하세요.");
                    alert.showAndWait();
                    return;
                }
                if(pw==""){ //비밀번호 입력했는지 확인
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("비밀번호를 입력하세요.");
                    alert.showAndWait();
                    return;
                }

                // 서버와 연결
                try {
                    URL url = new URL("http://localhost:3000/login");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("GET");
                    http.setRequestProperty("Authorization", id+":"+pw);

                    // 요청 방식 구하기
                    System.out.println("getRequestMethod():" + http.getRequestMethod());
                    // 응답 코드 구하기
                    System.out.println("getResponseCode():" + http.getResponseCode());
                    // 응답 메시지 구하기
                    System.out.println("getResponseMessage():" + http.getResponseMessage());
                    if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // 유저 정보를 저장한다.
                        Status.setId(id);
                        Status.setPw(pw);
                        System.out.println("로그인에 성공했습니다.");

                        // 현재 창을 종료한다.
                        Stage currStage = (Stage) idInput.getScene().getWindow();
                        currStage.close();

                        // 새 창을 띄운다.
                        Stage stage = new Stage();
                        Parent root = (Parent) FXMLLoader.load(getClass().getResource("/Client/Views/Main.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } else if (http.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        // 경고 메세지를 출력한다.
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("잘못된 입력 정보입니다.");
                        alert.showAndWait();
                    } else if (http.getResponseCode() == HttpURLConnection.HTTP_CONFLICT) {
                        // 경고 메세지를 출력한다.
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("이미 로그인이 되어있습니다.");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        signUpBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // 새 창을 띄운다.
                    Stage stage = new Stage();
                    Parent root = (Parent) FXMLLoader.load(getClass().getResource("/Client/Views/Signin.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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