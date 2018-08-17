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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Yogesh
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private JFXTextField username;
    
    @FXML
    private JFXPasswordField passwordhide;

    @FXML
    private JFXTextField passwordshow;
    public static String name, user, A;

    @FXML
    void Hidepassword(MouseEvent event) {
        passwordhide.setText(passwordshow.getText());
        passwordhide.setVisible(true);
        passwordshow.setVisible(false);

    }

    @FXML
    void Showpassword(MouseEvent event) {
        passwordshow.setText(passwordhide.getText());
        passwordshow.setVisible(true);
        passwordhide.setVisible(false);
    }

    @FXML
    void Forgot(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ForgotPassword.fxml"));

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

        }
    }

    @FXML
    void makeLogin(ActionEvent event) {
        Socket cs;
        if (username.getText().trim().isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Your Username...");

            alert.showAndWait();

            username.requestFocus();
        } else if (passwordhide.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Your Password...");
            alert.showAndWait();

            passwordhide.requestFocus();
        } else {

            try {
                InetAddress iAddress = InetAddress.getLocalHost();
                String Ip = iAddress.getHostAddress();
                System.out.println("Client Ip " + Ip);
                cs = new Socket(Ip, 1234);
                System.out.println("Connected to Server");
                

                try {
                    DataOutputStream dos = new DataOutputStream(cs.getOutputStream());
                    DataInputStream dis = new DataInputStream(cs.getInputStream());
                    dos.writeUTF("LogIn");
                    user = username.getText();
                    dos.writeUTF(user);
                    dos.writeUTF(passwordhide.getText());

                    String done = dis.readUTF();
                    if (done.equals("AlreadyLog")) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Login Limit Restriction.");
                        alert.setContentText("Wait Until Server Restart.");

                        alert.showAndWait();
                        username.setText("");
                        passwordhide.setText("");
                        username.requestFocus();

                    } else if (done.equals("done")) {

                        try {
                            // Parent root = FXMLLoader.load(getClass().getResource("Client Home.fxml"));

                            name = dis.readUTF();
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Client Home.fxml"));
                            Parent root = (Parent) fxmlLoader.load();
                            Scene scene = new Scene(root);
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                            stage.setScene(scene);
                            stage.show();

                        } catch (IOException e) {
                            System.out.println("Exception" + e);
                        }

                    } else if (done.equals("wrongpass")) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Your Password is Wrong.");

                        alert.showAndWait();
                        passwordhide.setText("");
                        passwordhide.requestFocus();

                    } else if (done.equals("wronguser")) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Your Username is Wrong.");

                        alert.showAndWait();
                        username.setText("");
                        passwordhide.setText("");
                        username.requestFocus();

                    }
                } catch (IOException e) {

                    System.out.println("Exception 46465" + e);
                }

            } catch (IOException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Technical Error");
                alert.setContentText("Server Down.");

                alert.showAndWait();
                System.out.println("Exception " + e);
            }

        }
    }

    @FXML
    void makeSignup(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("Signup.fxml"));

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Exception " + e);
        }

    }

    @FXML

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        passwordshow.setVisible(false);
    }

}


