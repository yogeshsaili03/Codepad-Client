/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.pad.client;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SignupController implements Initializable {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField emailid;
    @FXML
    private JFXTextField show;

    @FXML
    private JFXPasswordField repassword;

    @FXML
    private JFXTextField reshow;
    @FXML
    private JFXTextField mobile;

    @FXML
    void checkdetails(ActionEvent event) {

        if (username.getText().equals("") || password.getText().equals("") || repassword.getText().equals("") || name.getText().equals("") || mobile.getText().equals("") || emailid.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("All Fields are Mandatory...");
            alert.showAndWait();
            username.requestFocus();
        } else {
            try {
                String pass = password.getText();
                String repass = repassword.getText();
                if (pass.equals(repass)) {
                    InetAddress iAddress = InetAddress.getLocalHost();
                    String Ip = iAddress.getHostAddress();
                    System.out.println("Client Ip " + Ip);
                    Socket cs = new Socket(Ip, 1234);
                    System.out.println("Connected to Server");
                    DataOutputStream dos = new DataOutputStream(cs.getOutputStream());
                    DataInputStream dis = new DataInputStream(cs.getInputStream());

                    dos.writeUTF("SignUp");
                    dos.writeUTF(username.getText());
                    dos.writeUTF(password.getText());
                    //dos.writeUTF(repassword.getText());
                    dos.writeUTF(name.getText());
                    dos.writeUTF(mobile.getText());
                    dos.writeUTF(emailid.getText());
                    String done = dis.readUTF();
                    if (done.equals("done")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("ThankYou For Registering...");
                        alert.showAndWait();
                        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        stage.setScene(scene);
                        stage.show();
                    } else if (done.equals("fail")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Username Already Taken...");
                        alert.showAndWait();
                    } else if (done.equals("nope")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Try Later...");
                        alert.setContentText("Record Not Insered");
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Passwords not matched...");
                    password.setText("");
                    repassword.setText("");
                    password.requestFocus();
                    alert.showAndWait();
                }

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Technical Error");
                alert.setContentText("Server Down.");

                alert.showAndWait();
            }
        }

    }

    @FXML
    void makeLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {

        }
    }

    @FXML
    void Hidepass(MouseEvent event) {
        password.setText(show.getText());
        password.setVisible(true);
        show.setVisible(false);
    }

    @FXML
    void Hiderepass(MouseEvent event) {
        repassword.setText(reshow.getText());
        repassword.setVisible(true);
        reshow.setVisible(false);
    }

    @FXML
    void SHowrepass(MouseEvent event) {
        reshow.setText(repassword.getText());
        reshow.setVisible(true);
        repassword.setVisible(false);
    }

    @FXML
    void Showpass(MouseEvent event) {
        show.setText(password.getText());
        show.setVisible(true);
        password.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        show.setVisible(false);
        reshow.setVisible(false);
    }

}
